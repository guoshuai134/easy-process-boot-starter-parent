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
import java.util.Map;


/**
 * The type ShallowTask.
 * <p>
 * comment：
 *
 * @author Guo Shuai
 * @version 1.0.0
 * @date 2021-12-30 14:31:26
 */
@Data
@ApiModel(value = "Task对象", description = "任务")
@TableName(value = "easy_task")
@Accessors(chain = true)
public class Task implements Serializable {

    // fields start
    /**
     * 主键
     */
    @TableId(value = "SID", type = IdType.AUTO)
    @ApiModelProperty(value = "主键", notes = "字符长度为：19")
    private Long sid;
    /**
     * 流程ID
     */
    @TableField(value = "PROCESS_ID")
    @ApiModelProperty(value = "流程ID", notes = "字符长度为：19")
    private Long processId;
    /**
     * 提交用户
     */
    @TableField(value = "USER")
    @ApiModelProperty(value = "提交用户", notes = "字符长度为：100")
    private String user;
    /**
     * 当前节点
     */
    @TableField(value = "CURRENT_STAGE")
    @ApiModelProperty(value = "当前节点", notes = "字符长度为：20")
    private Long currentStage;
    /**
     * 目标表
     */
    @TableField(value = "TARGET")
    @ApiModelProperty(value = "目标表", notes = "字符长度为：100")
    private String target;
    /**
     * 目标表主键
     */
    @TableField(value = "TARGET_KEY")
    @ApiModelProperty(value = "目标表主键", notes = "字符长度为：100")
    private String targetKey;
    /**
     * 目标表主键值
     */
    @TableField(value = "TARGET_KEY_VALUE")
    @ApiModelProperty(value = "目标表主键值", notes = "字符长度为：100")
    private String targetKeyValue;
    /**
     * 类路径
     */
    @TableField(value = "CLASS_PATH")
    @ApiModelProperty(value = "目标Java类路径", notes = "字符长度为：100")
    private String classPath;
    /**
     * 状态
     */
    @TableField(value = "STATUS")
    @ApiModelProperty(value = "状态", notes = "字符长度为：100")
    private String status;
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
    @ApiModelProperty(value = "是否审批过了")
    private String isApprove;

    @TableField(exist = false)
    @ApiModelProperty(value = "目标表数据")
    private Map<String, Object> targetData;
}
