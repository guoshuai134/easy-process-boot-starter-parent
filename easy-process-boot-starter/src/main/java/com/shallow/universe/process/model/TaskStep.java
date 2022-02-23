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
 * The type ShallowTaskStep.
 * <p>
 * comment：
 *
 * @author Guo Shuai
 * @version 1.0.0
 * @date 2021-12-30 14:31:26
 */
@Data
@ApiModel(value = "TaskStep对象", description = "任务步骤")
@TableName(value = "easy_task_step")
@Accessors(chain = true)
public class TaskStep implements Serializable {

    // fields start
    /**
     * 主键
     */
    @TableId(value = "SID", type = IdType.AUTO)
    @ApiModelProperty(value = "主键", notes = "字符长度为：19")
    private Long sid;
    /**
     * 任务ID
     */
    @TableField(value = "TASK_ID")
    @ApiModelProperty(value = "任务ID", notes = "字符长度为：19")
    private Long taskId;
    /**
     * 节点ID
     */
    @TableField(value = "STAGE_ID")
    @ApiModelProperty(value = "阶段ID", notes = "字符长度为：19")
    private Long stageId;
    /**
     * 审批用户
     */
    @TableField(value = "USER")
    @ApiModelProperty(value = "审批用户", notes = "字符长度为：100")
    private String user;
    /**
     * 审批角色
     */
    @TableField(value = "ROLE")
    @ApiModelProperty(value = "审批角色", notes = "字符长度为：100")
    private String role;
    /**
     * 审批意见
     */
    @TableField(value = "OPINION")
    @ApiModelProperty(value = "审批意见", notes = "字符长度为：10")
    private String opinion;
    /**
     * 原因/理由
     */
    @TableField(value = "REASON")
    @ApiModelProperty(value = "原因/理由", notes = "字符长度为：255")
    private String reason;
    /**
     * 状态-是否可用 1. 可用  0. 不可用
     */
    @TableField(value = "STATUS")
    @ApiModelProperty(value = "状态-是否可用 1. 可用  0. 不可用", notes = "字符长度为：10")
    private Integer status;
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
}
