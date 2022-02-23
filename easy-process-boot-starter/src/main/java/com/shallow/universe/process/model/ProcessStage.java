package com.shallow.universe.process.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


/**
 * The type ShallowProcessNode.
 * <p>
 * comment：
 *
 * @author Guo Shuai
 * @version 1.0.0
 * @date 2021-12-30 14:31:26
 */
@Data
@ApiModel(value = "ProcessStage对象", description = "流程阶段")
@TableName(value = "easy_process_stage")
@Accessors(chain = true)
public class ProcessStage implements Serializable {

    // fields start
    /**
     * 主键
     */
    @TableId(value = "SID", type = IdType.AUTO)
    @ApiModelProperty(value = "主键", notes = "字符长度为：19")
    private Long sid;
    /**
     * 标题
     */
    @TableField(value = "TITLE")
    @ApiModelProperty(value = "标题", notes = "字符长度为：100")
    private String title;
    /**
     * 描述
     */
    @TableField(value = "DESCRIPTION")
    @ApiModelProperty(value = "描述", notes = "字符长度为：255")
    private String description;
    /**
     * 部门
     */
    @TableField(value = "DEPARTMENT")
    @ApiModelProperty(value = "部门", notes = "字符长度为：100")
    private String department;
    /**
     * 用户
     */
    @TableField(value = "USER")
    @ApiModelProperty(value = "用户", notes = "字符长度为：100")
    private String user;
    /**
     * 角色
     */
    @TableField(value = "ROLE")
    @ApiModelProperty(value = "角色", notes = "字符长度为：100")
    private String role;
    /**
     * 顺序
     */
    @TableField(value = "ORDERED")
    @ApiModelProperty(value = "顺序", notes = "字符长度为：10")
    private Integer ordered;
    /**
     * 模式
     */
    @TableField(value = "MODE")
    @ApiModelProperty(value = "模式", notes = "字符长度为：10")
    private Integer mode;
    /**
     * 字段
     */
    @TableField(value = "FIELD")
    @ApiModelProperty(value = "字段", notes = "字符长度为：100")
    private String field;
    /**
     * 条件
     */
    @TableField(value = "`CONDITION`")
    @ApiModelProperty(value = "条件", notes = "字符长度为：10")
    private String condition;
    /**
     * 条件值
     */
    @TableField(value = "VALUE")
    @ApiModelProperty(value = "条件值", notes = "字符长度为：100")
    private String value;
    /**
     * 流程ID
     */
    @TableField(value = "PROCESS_ID")
    @ApiModelProperty(value = "流程ID", notes = "字符长度为：19")
    private Long processId;
    /**
     * 创建时间
     */
    @TableField(value = "CREATED", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间", notes = "字符长度为：19")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date created;
    /**
     * 更新时间
     */
    @TableField(value = "LAST_UPDATED", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间", notes = "字符长度为：19")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdated;
    // fields end

    @TableField(exist = false)
    private Long businessId;
}
