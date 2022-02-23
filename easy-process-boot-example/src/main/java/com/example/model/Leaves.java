package com.example.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.shallow.universe.process.core.annotation.PrimaryKey;
import com.shallow.universe.process.core.annotation.ProcessEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * ClassName:
 * Description: TODO
 *
 * @author: Guo Shuai
 * @version: 1.0
 * @Date: 2021\12\31 0031
 */
@ProcessEntity(table = "leaves", process = "请假流程")
@Data
@Accessors(chain = true)
public class Leaves {

    @PrimaryKey
    @TableId(value = "sid", type = IdType.AUTO)
    private Long sid;

    @TableField(value = "content")
    private String content;

    @TableField(value = "days")
    private String days;
}