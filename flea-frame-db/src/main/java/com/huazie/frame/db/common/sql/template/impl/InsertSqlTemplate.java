package com.huazie.frame.db.common.sql.template.impl;

import com.huazie.frame.common.util.ObjectUtils;
import com.huazie.frame.common.util.StringUtils;
import com.huazie.frame.db.common.DBConstants;
import com.huazie.frame.db.common.exception.SqlTemplateException;
import com.huazie.frame.db.common.sql.template.SqlTemplate;
import com.huazie.frame.db.common.sql.template.SqlTemplateEnum;
import com.huazie.frame.db.common.sql.template.TemplateTypeEnum;
import com.huazie.frame.db.common.sql.template.config.Property;
import com.huazie.frame.db.common.sql.template.config.SqlTemplateConfig;
import com.huazie.frame.db.common.sql.template.config.Template;
import com.huazie.frame.db.common.table.column.Column;
import com.huazie.frame.db.common.util.EntityUtils;
import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p> 插入SQL模板, 用于使用原生SQL实现分表; </p>
 * <p> 插入模板定义配置在<b>flea-sql-template.xml</b>; </p>
 * <p> 节点<b>{@code <insert>}</b>下即为插入SQL模板: </p>
 * <pre>
 * (1) 模板配置信息：
 * {@code <template id="insert" name="插入模板" desc="用于原生SQL中插入语句的使用">
 *     <property key="template" value="INSERT INTO ##table## (##columns## ) VALUES(##values## )"/>
 *     <property key="table" value="student"/>
 *     <!-- 这两个不填，表示全部使用表的字段
 *     <property key="columns" value="****"/>
 *     <property key="values" value="****"/>-->
 *   </template>}
 *
 * (2)使用示例
 *  示例1：
 *  SqlTemplate<Student> sqlTemplate = new InsertSqlTemplate<Student>();
 *  // 这个对应{@code <template id="insert" >}
 *  sqlTemplate.setId("insert");
 *  // 实体类对应的表名
 *  sqlTemplate.setTableName("student");
 *  // 实体类的实例对象
 *  sqlTemplate.setEntity(student);
 *  // 模板初始化
 *  sqlTemplate.initialize();
 *
 *  示例2：(该方式需要在实体类被@Table或者@FleaTable修饰，能获取指定的表名)
 *  ITemplate<Student> sqlTemplate = new InsertSqlTemplate<Student>("insert", student);
 *  // 模板初始化
 *  sqlTemplate.initialize();
 *
 *  示例3：
 *  ITemplate<Student> sqlTemplate = new InsertSqlTemplate<Student>("insert", "student", student);
 *  // 模板初始化
 *  sqlTemplate.initialize();
 *
 *  (3) 模板初始化后，就可以获取原生SQL和相关参数了，如下：
 *  // 获取原生SQL
 *  String sql = sqlTemplate.toNativeSql();
 *  // 获取相关参数
 *  Map<String, Object> paramMap = sqlTemplate.toNativeParams();
 * </pre>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class InsertSqlTemplate<T> extends SqlTemplate<T> {

    private static final long serialVersionUID = 8796257687684425547L;

    /**
     * <p> INSERT模板构造方法, 参考示例1 </p>
     *
     * @since 1.0.0
     */
    public InsertSqlTemplate() {
        templateType = TemplateTypeEnum.INSERT;
    }

    /**
     * <p> INSERT模板构造方法，参考示例2 </p>
     *
     * @param id     模板编号
     * @param entity 实体类的实例对象
     * @since 1.0.0
     */
    public InsertSqlTemplate(String id, T entity) {
        super(id, entity);
        templateType = TemplateTypeEnum.INSERT;
    }

    /**
     * <p> INSERT模板构造方法，参考示例3 </p>
     *
     * @param id        模板编号
     * @param tableName 表名
     * @param entity    实体类的实例对象
     * @since 1.0.0
     */
    public InsertSqlTemplate(String id, String tableName, T entity) {
        super(id, tableName, entity);
        templateType = TemplateTypeEnum.INSERT;
    }

    @Override
    protected Template getSqlTemplate(String id) {
        return SqlTemplateConfig.getConfig().getInsertTemplate(id);
    }

    @Override
    protected void initSqlTemplate(StringBuilder sql, Map<String, Object> params, final Column[] entityCols, Map<String, Property> propMap) throws Exception {

        // 获取【key=columns】的属性, 建议将表的字段都加上，以逗号分隔(类似  id, name)
        Property columns = propMap.get(SqlTemplateEnum.COLUMNS.getKey());
        // 获取【key=values】的属性(类似  :id, :name)
        Property values = propMap.get(SqlTemplateEnum.VALUES.getKey());

        String colStr;
        String valStr;
        if (ObjectUtils.isEmpty(columns) && ObjectUtils.isEmpty(values)) { // 表示将实体类的全部变量替换
            colStr = StringUtils.strCombined(entityCols, Column.COLUMN_TAB_COL_NAME, DBConstants.SQLConstants.SQL_BLANK, DBConstants.SQLConstants.SQL_COMMA);
            valStr = StringUtils.strCombined(entityCols, Column.COLUMN_ATTR_NAME, DBConstants.SQLConstants.SQL_BLANK + DBConstants.SQLConstants.SQL_COLON, DBConstants.SQLConstants.SQL_COMMA);
            createParamMap(params, entityCols);// 设置SQL参数
        } else if (ObjectUtils.isNotEmpty(columns) && ObjectUtils.isNotEmpty(values)) {
            colStr = columns.getValue();
            valStr = values.getValue();

            String[] colArr = StringUtils.split(StringUtils.trim(colStr), DBConstants.SQLConstants.SQL_COMMA);
            String[] valArr = StringUtils.split(StringUtils.trim(valStr), DBConstants.SQLConstants.SQL_COMMA);

            // 校验表字段列和 属性值变量(是否合法, 是否一一对应)
            Column[] realEntityCols = check(entityCols, colArr, valArr);
            // 设置SQL参数
            createParamMap(params, realEntityCols);
        } else {
            throw new SqlTemplateException("ERROR-DB-SQT0000000020", getId());
        }

        StringUtils.replace(sql, createPlaceHolder(SqlTemplateEnum.COLUMNS.getKey()), colStr);
        StringUtils.replace(sql, createPlaceHolder(SqlTemplateEnum.VALUES.getKey()), valStr);
    }

    /**
     * 校验表字段列和 属性值变量是否合法(包括 是否一一对应，是否都是必填项)
     *
     * @param entityCols 实体类对象的属性数组
     * @param cols       表属性列数组
     * @param values     表属性列对应值数组
     * @return 模板配置中对应的实体类对象的属性数组
     * @throws Exception
     * @since 1.0.0
     */
    private Column[] check(final Column[] entityCols, String[] cols, String[] values) throws Exception {
        if (ArrayUtils.isEmpty(cols)) {
            throw new SqlTemplateException("ERROR-DB-SQT0000000017", getId());
        }

        if (ArrayUtils.isEmpty(values)) {
            throw new SqlTemplateException("ERROR-DB-SQT0000000021", getId());
        }

        if (!ArrayUtils.isSameLength(cols, values)) {
            throw new SqlTemplateException("ERROR-DB-SQT0000000022", getId());
        }

        List<Column> entityColsList = new ArrayList<Column>();
        for (int n = 0; n < cols.length; n++) {
            String tabColumnName = StringUtils.trim(cols[n]);//表字段名
            String attrName = StringUtils.trim(values[n]);//该表字段对应的属性变量值 (如 :paraId )

            if (StringUtils.isBlank(attrName)) {
                throw new SqlTemplateException("ERROR-DB-SQT0000000023", getId(), tabColumnName);
            }

            Column column = (Column) EntityUtils.getEntity(entityCols, Column.COLUMN_TAB_COL_NAME, tabColumnName);
            if (ObjectUtils.isEmpty(column)) {
                throw new SqlTemplateException("ERROR-DB-SQT0000000024", getId(), tabColumnName);
            }

            String attrN = column.getAttrName();
            if (!attrName.equals(StringUtils.strCat(DBConstants.SQLConstants.SQL_COLON, attrN))) {
                throw new SqlTemplateException("ERROR-DB-SQT0000000025", getId(), tabColumnName, attrName);
            }
            entityColsList.add(column);
        }
        return entityColsList.toArray(new Column[0]);
    }

}
