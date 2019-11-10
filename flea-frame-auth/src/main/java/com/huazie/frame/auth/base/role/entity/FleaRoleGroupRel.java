package com.huazie.frame.auth.base.role.entity;

import com.huazie.frame.common.FleaEntity;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * <p> Flea角色组关联（角色，用户，用户组）表对应的实体类 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Entity
@Table(name = "flea_role_group_rel")
public class FleaRoleGroupRel extends FleaEntity {

    private static final long serialVersionUID = 3903426247530091605L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "FLEA_ROLE_GROUP_REL_SEQ")
    @SequenceGenerator(name = "FLEA_ROLE_GROUP_REL_SEQ")
    @Column(name = "role_group_rel_id", unique = true, nullable = false)
    private Long roleGroupRelId; // 角色组关联编号

    @Column(name = "role_group_id", nullable = false)
    private Long roleGroupId; // 角色组编号

    @Column(name = "relat_id", nullable = false)
    private Long relatId; // 关联编号

    @Column(name = "relat_type", nullable = false)
    private String relatType; // 关联类型

    @Column(name = "relat_state", nullable = false)
    private Integer relatState; // 关联状态

    @Column(name = "create_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate; // 创建日期

    @Column(name = "done_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date doneDate; // 修改日期

    @Column(name = "remarks")
    private String remarks; // 备注信息

    @Column(name = "relat_ext_a")
    private String relatExtA; // 关联扩展字段A

    @Column(name = "relat_ext_b")
    private String relatExtB; // 关联扩展字段B

    @Column(name = "relat_ext_c")
    private String relatExtC; // 关联扩展字段C

    @Column(name = "relat_ext_x")
    private String relatExtX; // 关联扩展字段X

    @Column(name = "relat_ext_y")
    private String relatExtY; // 关联扩展字段Y

    @Column(name = "relat_ext_z")
    private String relatExtZ; // 关联扩展字段Z

    public Long getRoleGroupRelId() {
        return roleGroupRelId;
    }

    public void setRoleGroupRelId(Long roleGroupRelId) {
        this.roleGroupRelId = roleGroupRelId;
    }

    public Long getRoleGroupId() {
        return roleGroupId;
    }

    public void setRoleGroupId(Long roleGroupId) {
        this.roleGroupId = roleGroupId;
    }

    public Long getRelatId() {
        return relatId;
    }

    public void setRelatId(Long relatId) {
        this.relatId = relatId;
    }

    public String getRelatType() {
        return relatType;
    }

    public void setRelatType(String relatType) {
        this.relatType = relatType;
    }

    public Integer getRelatState() {
        return relatState;
    }

    public void setRelatState(Integer relatState) {
        this.relatState = relatState;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(Date doneDate) {
        this.doneDate = doneDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRelatExtA() {
        return relatExtA;
    }

    public void setRelatExtA(String relatExtA) {
        this.relatExtA = relatExtA;
    }

    public String getRelatExtB() {
        return relatExtB;
    }

    public void setRelatExtB(String relatExtB) {
        this.relatExtB = relatExtB;
    }

    public String getRelatExtC() {
        return relatExtC;
    }

    public void setRelatExtC(String relatExtC) {
        this.relatExtC = relatExtC;
    }

    public String getRelatExtX() {
        return relatExtX;
    }

    public void setRelatExtX(String relatExtX) {
        this.relatExtX = relatExtX;
    }

    public String getRelatExtY() {
        return relatExtY;
    }

    public void setRelatExtY(String relatExtY) {
        this.relatExtY = relatExtY;
    }

    public String getRelatExtZ() {
        return relatExtZ;
    }

    public void setRelatExtZ(String relatExtZ) {
        this.relatExtZ = relatExtZ;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}