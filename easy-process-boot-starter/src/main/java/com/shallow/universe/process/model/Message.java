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
 * The type ShallowProcess.
 * <p>
 * comment：流程定义
 *
 * @author Guo Shuai
 * @version 1.0.0
 * @date 2021-12-30 14:31:25
 */
@Data
@ApiModel(value = "Message对象", description = "消息对象")
@TableName(value = "easy_message")
@Accessors(chain = true)
public class Message implements Serializable {

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
    @TableField(value = "USER")
    @ApiModelProperty(value = "接收用户", notes = "字符长度为：100")
    private String user;
    /**
     * 任务ID
     */
    @TableField(value = "TASK_ID")
    @ApiModelProperty(value = "任务ID", notes = "字符长度为：10")
    private Long taskId;
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

    /**
     * 任务
     */
    @ApiModelProperty(value = "任务")
    @TableField(exist = false)
    private Task task;
}
