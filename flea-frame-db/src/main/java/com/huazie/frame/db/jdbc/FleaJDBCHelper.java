package com.huazie.frame.db.jdbc;

import com.huazie.frame.common.util.ArrayUtils;
import com.huazie.frame.common.util.CollectionUtils;
import com.huazie.frame.common.util.ObjectUtils;
import com.huazie.frame.db.common.sql.pojo.SqlParam;
import com.huazie.frame.db.common.sql.template.ITemplate;
import com.huazie.frame.db.common.sql.template.TemplateTypeEnum;
import com.huazie.frame.db.common.sql.template.impl.SelectSqlTemplate;
import com.huazie.frame.db.jdbc.config.FleaJDBCConfig;
import com.huazie.frame.db.jdbc.pojo.FleaDBOperationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> 数据库工具类，主要进行数据库中数据的增删改查操作 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class FleaJDBCHelper {

    private final static Logger LOGGER = LoggerFactory.getLogger(FleaJDBCHelper.class);

    /**
     * <p> 以返回List<Map>，其中Map的键为属性名，值为相应的数据 </p>
     *
     * @param sql 数据库sql语句
     * @return 查询结果数据集合
     * @throws SQLException 数据库操作异常
     * @since 1.0.0
     */
    public static List<Map<String, Object>> query(String sql) throws SQLException {
        FleaDBOperationHandler handler = null;
        try {
            handler = queryWithReturnResultSet(sql);
            return ResultToListMap(handler.getResultSet());
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            close(handler);
        }
    }

    /**
     * <p> 用于带参数的查询，返回List<Map>，其中Map的键为属性名，值为相应的数据 </p>
     *
     * @param sql    数据库sql语句
     * @param params 参数列表
     * @return 查询结果数据集合
     * @throws SQLException 数据库操作异常
     * @since 1.0.0
     */
    public static List<Map<String, Object>> query(String sql, Object... params) throws SQLException {
        FleaDBOperationHandler handler = null;
        try {
            handler = queryWithReturnResultSet(sql, params);
            return ResultToListMap(handler.getResultSet());
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            close(handler);
        }
    }

    /**
     * <p> 用于带参数的查询，返回List<Map>，其中Map的键为属性名，值为相应的数据 </p>
     *
     * @param sql    数据库sql语句
     * @param params 参数列表
     * @return 查询结果数据集合
     * @throws SQLException 数据库操作异常
     * @since 1.0.0
     */
    public static List<Map<String, Object>> query(String sql, List<Object> params) throws SQLException {
        List<Map<String, Object>> datas = null;
        if (CollectionUtils.isNotEmpty(params)) {
            Object[] paramArr = params.toArray(new Object[0]);
            datas = query(sql, paramArr);
        }
        return datas;
    }

    /**
     * <p> 用于查询，返回结果集ResultSet </p>
     *
     * @param sql 数据库sql语句
     * @return 结果集
     * @throws SQLException 数据库操作异常
     * @since 1.0.0
     */
    public static FleaDBOperationHandler queryWithReturnResultSet(String sql) throws SQLException {
        return queryWithReturnResultSet(sql, new Object[]{});
    }

    /**
     * <p> 用于带参数的查询，返回结果集 </p>
     *
     * @param sql    数据库sql语句
     * @param params 参数列表
     * @return 结果集
     * @throws SQLException 数据库操作异常
     * @since 1.0.0
     */
    public static FleaDBOperationHandler queryWithReturnResultSet(String sql, Object... params) throws SQLException {
        FleaDBOperationHandler handler = getDBOperationHandler(sql, params);
        queryWithReturnResultSet(handler);
        return handler;
    }

    /**
     * <p> 返回单个结果的值，如count\min\max等等 </p>
     *
     * @param sql 数据库sql语句
     * @return 单个结果
     * @throws SQLException 数据库操作异常
     * @since 1.0.0
     */
    public static Object querySingle(String sql) throws SQLException {
        return querySingle(sql, new Object[]{});
    }

    /**
     * <p> 返回单个结果值，如count\min\max等 </p>
     *
     * @param sql    数据库sql语句
     * @param params 参数列表
     * @return 单个结果
     * @throws SQLException 数据库操作异常
     * @since 1.0.0
     */
    public static Object querySingle(String sql, Object... params) throws SQLException {
        Object result = null;
        FleaDBOperationHandler handler = null;
        try {
            handler = queryWithReturnResultSet(sql, params);
            ResultSet rs = handler.getResultSet();
            if (rs.next()) {
                result = rs.getObject(1);
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            close(handler);
        }
    }

    /**
     * <p> 返回单个结果值，如count\min\max等 </p>
     *
     * @param sql    数据库sql语句
     * @param params 参数列表
     * @return 单个结果
     * @throws SQLException 数据库操作异常
     * @since 1.0.0
     */
    public static Object querySingle(String sql, List<Object> params) throws SQLException {
        Object result = null;
        if (null != params && !params.isEmpty()) {
            Object[] paramArr = params.toArray(new Object[0]);
            result = querySingle(sql, paramArr);
        }
        return result;
    }

    /**
     * <p> 用于更新数据，sql语句为update语句 </p>
     *
     * @param sql 数据库sql语句
     * @return 影响行数
     * @throws SQLException 数据库操作异常
     * @since 1.0.0
     */
    public static int update(String sql) throws SQLException {
        return save(sql);
    }

    /**
     * <p> 用于更新数据，sql语句为update语句（带参数） </p>
     *
     * @param sql    数据库sql语句
     * @param params 参数列表
     * @return 影响行数
     * @throws SQLException 数据库操作异常
     * @since 1.0.0
     */
    public static int update(String sql, Object... params) throws SQLException {
        return save(sql, params);
    }

    /**
     * <p> 用于更新数据，sql语句为update语句（带参数） </p>
     *
     * @param sql    数据库sql语句
     * @param params 参数列表
     * @return 影响行数
     * @throws SQLException 数据库操作异常
     * @since 1.0.0
     */
    public static int update(String sql, List<Object> params) throws SQLException {
        return save(sql, params);
    }

    /**
     * <p> 于增加数据，sql语句为insert语句 </p>用
     *
     * @param sql 数据库sql语句
     * @return 影响行数
     * @throws SQLException 数据库操作异常
     * @since 1.0.0
     */
    public static int insert(String sql) throws SQLException {
        return save(sql);
    }

    /**
     * <p> 用于增加数据，sql语句为insert语句（带参数） </p>
     *
     * @param sql    数据库sql语句
     * @param params 参数列表
     * @return 影响行数
     * @throws SQLException 数据库操作异常
     * @since 1.0.0
     */
    public static int insert(String sql, Object... params) throws SQLException {
        return save(sql, params);
    }

    /**
     * <p> 用于增加数据，sql语句为insert语句（带参数） </p>
     *
     * @param sql    数据库sql语句
     * @param params 参数列表
     * @return 影响行数
     * @throws SQLException 数据库操作异常
     * @since 1.0.0
     */
    public static int insert(String sql, List<Object> params) throws SQLException {
        return save(sql, params);
    }

    /**
     * <p> 用于删除数据，sql语句为delete语句 </p>
     *
     * @param sql 数据库sql语句
     * @return 影响行数
     * @throws SQLException 数据库操作异常
     * @since 1.0.0
     */
    public static int delete(String sql) throws SQLException {
        return save(sql);
    }

    /**
     * <p> 用于删除数据，sql语句为delete语句（带参数） </p>
     *
     * @param sql    数据库sql语句
     * @param params 参数列表
     * @return 影响行数
     * @throws SQLException 数据库操作异常
     * @since 1.0.0
     */
    public static int delete(String sql, Object... params) throws SQLException {
        return save(sql, params);
    }

    /**
     * <p> 用于删除数据，sql语句为delete语句（带参数） </p>
     *
     * @param sql    数据库sql语句
     * @param params 参数列表
     * @return 影响行数
     * @throws SQLException 数据库操作异常
     * @since 1.0.0
     */
    public static int delete(String sql, List<Object> params) throws SQLException {
        return save(sql, params);
    }

    /**
     * <p> 处理INSERT, UPDATE, DELETE SQL语句 </p>
     *
     * @param sql 数据库sql语句
     * @return 影响行数
     * @throws SQLException 数据库操作异常
     * @since 1.0.0
     */
    private static int save(String sql) throws SQLException {
        return save(sql, new Object[]{});
    }

    /**
     * <p> 处理INSERT, UPDATE, DELETE SQL语句（带参数） </p>
     *
     * @param sql    数据库sql语句
     * @param params 参数列表
     * @return 影响行数
     * @throws SQLException 数据库操作异常
     * @since 1.0.0
     */
    private static int save(String sql, Object... params) throws SQLException {
        FleaDBOperationHandler handler = null;
        try {
            handler = getDBOperationHandler(sql, params);
            PreparedStatement preparedStatement = null;
            if (null != handler) {
                preparedStatement = handler.getPreparedStatement();
            }
            return null != preparedStatement ? preparedStatement.executeUpdate() : -1;
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            close(handler);
        }
    }

    /**
     * <p> 处理INSERT, UPDATE, DELETE SQL语句（带参数） </p>
     *
     * @param sql    数据库sql语句
     * @param params 参数列表
     * @return 影响行数
     * @throws SQLException 数据库操作异常
     * @since 1.0.0
     */
    private static int save(String sql, List<Object> params) throws SQLException {
        int retVal = -1;
        if (null != params && !params.isEmpty()) {
            Object[] paramArr = params.toArray(new Object[0]);
            retVal = save(sql, paramArr);
        }
        return retVal;
    }

    /**
     * <p> 获取一次数据库操作处理 </p>
     *
     * @return Flea一次数据库操作处理对象
     * @throws SQLException 数据库操作异常
     * @since 1.0.0
     */
    private static FleaDBOperationHandler getDBOperationHandler(String sql, Object... params) throws SQLException {

        FleaDBOperationHandler handler = new FleaDBOperationHandler();
        Connection connection = FleaJDBCConfig.getConfig().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("FleaJDBCHelper##getDBOperationHandler(String, Object...) SQL    : {}", sql);
        }

        if (null != preparedStatement && ArrayUtils.isNotEmpty(params)) {
            for (int i = 0; i < params.length; i++) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("FleaJDBCHelper##getDBOperationHandler(String, Object...) PARAM{} : {}", i + 1, params[i]);
                }
                preparedStatement.setObject(i + 1, params[i]);
            }
        }
        handler.setConnection(connection);
        handler.setPreparedStatement(preparedStatement);

        return handler;
    }

    /**
     * <p> 将结果集ResultSet转换为包含Map<String,Object>的List集合 </p>
     *
     * @param rs 结果集对象
     * @return 查询数据集合
     * @throws SQLException 数据库操作异常
     * @since 1.0.0
     */
    private static List<Map<String, Object>> ResultToListMap(ResultSet rs) throws SQLException {
        List<Map<String, Object>> listMaps = new ArrayList<Map<String, Object>>();
        if (ObjectUtils.isNotEmpty(rs)) {
            while (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                Map<String, Object> map = new HashMap<String, Object>();
                if (ObjectUtils.isNotEmpty(rsmd)) {
                    for (int i = 0; i < rsmd.getColumnCount(); i++) {
                        map.put(rsmd.getColumnLabel(i + 1), rs.getObject(i + 1));
                    }
                }
                listMaps.add(map);
            }
        }
        return listMaps;
    }

    /**
     * <p> 构建并执行 SELECT SQL模板 </p>
     *
     * @param relationId 关系编号
     * @param entity     实体类对象
     * @return 查询结果Map集合
     * @throws Exception
     * @since 1.0.0
     */
    public static List<Map<String, Object>> query(String relationId, Object entity) throws Exception {
        FleaDBOperationHandler handler = null;
        ITemplate<Object> template = new SelectSqlTemplate<Object>(relationId, entity);
        try {
            handler = queryWithReturnResultSet(template, TemplateTypeEnum.SELECT.getKey());
            return ResultToListMap(handler.getResultSet());
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            close(handler);
        }
    }

    /**
     * <p> 用于带参数的查询，返回结果集 </p>
     *
     * @param template SQL模板接口类
     * @return 结果集
     * @throws Exception 数据库操作异常
     * @since 1.0.0
     */
    private static FleaDBOperationHandler queryWithReturnResultSet(ITemplate<Object> template, String templateType) throws Exception {
        FleaDBOperationHandler handler = null;
        if (ObjectUtils.isNotEmpty(template)) {
            template.initialize(); // 初始化SQL模板（一定要初始化）
            handler = getDBOperationHandler(template.toNativeSql(), template.toNativeParams(), templateType);
            queryWithReturnResultSet(handler);
        }
        return handler;
    }

    /**
     * <p> 获取一次数据库操作处理 </p>
     *
     * @return 一次数据库操作处理对象
     * @throws SQLException 数据库操作异常
     * @since 1.0.0
     */
    private static FleaDBOperationHandler getDBOperationHandler(String sql, List<SqlParam> sqlParams, String templateType) throws SQLException {

        FleaDBOperationHandler handler = new FleaDBOperationHandler();
        Connection connection = FleaJDBCConfig.getConfig().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        if (LOGGER.isDebugEnabled()) {
            if (TemplateTypeEnum.INSERT.getKey().equals(templateType)) {
                LOGGER.debug("FleaJDBCHelper##insert(String, T) SQL = {}", sql);
            } else if (TemplateTypeEnum.UPDATE.getKey().equals(templateType)) {
                LOGGER.debug("FleaJDBCHelper##update(String, T) SQL = {}", sql);
            } else if (TemplateTypeEnum.DELETE.getKey().equals(templateType)) {
                LOGGER.debug("FleaJDBCHelper##delete(String, T) SQL = {}", sql);
            } else if (TemplateTypeEnum.SELECT.getKey().equals(templateType)) {
                LOGGER.debug("FleaJDBCHelper##query(String, T) SQL = {}", sql);
            }
        }

        if (CollectionUtils.isNotEmpty(sqlParams)) {
            for (SqlParam sqlParam : sqlParams) {
                if (TemplateTypeEnum.INSERT.getKey().equals(templateType)) {
                    LOGGER.debug("FleaJDBCHelper##insert(String, T) PARAM{} = {} , COL{} = {}", sqlParam.getIndex(), sqlParam.getAttrValue(), sqlParam.getIndex(), sqlParam.getTabColName());
                } else if (TemplateTypeEnum.UPDATE.getKey().equals(templateType)) {
                    LOGGER.debug("FleaJDBCHelper##update(String, T) PARAM{} = {} , COL{} = {}", sqlParam.getIndex(), sqlParam.getAttrValue(), sqlParam.getIndex(), sqlParam.getTabColName());
                } else if (TemplateTypeEnum.DELETE.getKey().equals(templateType)) {
                    LOGGER.debug("FleaJDBCHelper##delete(String, T) PARAM{} = {} , COL{} = {}", sqlParam.getIndex(), sqlParam.getAttrValue(), sqlParam.getIndex(), sqlParam.getTabColName());
                } else if (TemplateTypeEnum.SELECT.getKey().equals(templateType)) {
                    LOGGER.debug("FleaJDBCHelper##query(String, T) PARAM{} = {} , COL{} = {}", sqlParam.getIndex(), sqlParam.getAttrValue(), sqlParam.getIndex(), sqlParam.getTabColName());
                }
                preparedStatement.setObject(sqlParam.getIndex(), sqlParam.getAttrValue());
            }
        }
        handler.setConnection(connection);
        handler.setPreparedStatement(preparedStatement);

        return handler;
    }

    /**
     * <p> 设置查询结果集 </p>
     *
     * @param handler 一次数据库操作处理对象（只初始化了Connection 和 PreparedStatement 对象）
     * @throws SQLException 数据库操作异常
     * @since 1.0.0
     */
    private static void queryWithReturnResultSet(FleaDBOperationHandler handler) throws SQLException {
        if (ObjectUtils.isNotEmpty(handler)) {
            PreparedStatement preparedStatement = handler.getPreparedStatement();
            if (ObjectUtils.isNotEmpty(preparedStatement)) {
                handler.setResultSet(preparedStatement.executeQuery());
            }
        }
    }

    /**
     * <p> 关闭Connection，PreparedStatement，ResultSet </p>
     *
     * @param handler 一次数据库操作处理
     * @since 1.0.0
     */
    public static void close(FleaDBOperationHandler handler) {
        if (ObjectUtils.isNotEmpty(handler)) {
            FleaJDBCConfig.close(handler.getConnection(), handler.getPreparedStatement(), handler.getResultSet());
        }
    }

}