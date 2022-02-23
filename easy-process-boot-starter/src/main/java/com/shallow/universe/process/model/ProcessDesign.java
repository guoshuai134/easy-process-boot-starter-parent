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
 * ClassName:
 * Description: TODO
 *
 * @author: Guo Shuai
 * @version: 1.0
 * @Date: 2022\1\10 0010
 */
@Data
@ApiModel(value = "ProcessDesign对象", description = "流程设计")
@TableName(value = "easy_process_design")
@Accessors(chain = true)
public class ProcessDesign implements Serializable {

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
    @TableField(value = "LABEL")
    @ApiModelProperty(value = "标题", notes = "字符长度为：100")
    private String label;
    /**
     * 类型
     */
    @TableField(value = "TYPE")
    @ApiModelProperty(value = "类型", notes = "字符长度为：50")
    private String type;
    /**
     * x轴坐标
     */
    @TableField(value = "X")
    @ApiModelProperty(value = "x轴坐标", notes = "字符长度为：10")
    private String x;
    /**
     * y轴坐标
     */
    @TableField(value = "Y")
    @ApiModelProperty(value = "y轴坐标", notes = "字符长度为：10")
    private String y;
    /**
     * 阶段ID
     */
    @TableField(value = "STAGE_ID")
    @ApiModelProperty(value = "阶段ID", notes = "字符长度为：10")
    private Long stageId;
    /**
     * 源
     */
    @TableField(value = "SOURCE")
    @ApiModelProperty(value = "源", notes = "字符长度为：10")
    private Long source;
    /**
     * 目标
     */
    @TableField(value = "TARGET")
    @ApiModelProperty(value = "目标", notes = "字符长度为：10")
    private Long target;
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


    @ApiModelProperty("阶段对象")
    @TableField(exist = false)
    private ProcessStage processStage;
    /**
     * 参数
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "参数", notes = "字符长度为：1000")
    private String params;
}
