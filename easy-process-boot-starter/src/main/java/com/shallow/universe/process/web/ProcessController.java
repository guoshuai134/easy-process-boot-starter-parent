package com.shallow.universe.process.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shallow.universe.process.core.ProcessEngine;
import com.shallow.universe.process.core.ProcessException;
import com.shallow.universe.process.model.Process;
import com.shallow.universe.process.util.ApiResultTools;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * ClassName:
 * Description: TODO
 *
 * @author: Guo Shuai
 * @version: 1.0
 * @Date: 2022\1\10 0010
 */
@RestController
@RequestMapping("/easy/process")
public class ProcessController {

    @Resource
    private ProcessEngine processEngine;

    @PostMapping("/deploy")
    public ApiResult<String> deploy(@RequestBody Process process) {
        try {
            //部署
            processEngine.processServiceInstance().deploy(process);
            //返回
            return ApiResultTools.success();
        } catch (ProcessException e) {
            e.printStackTrace();
            return ApiResultTools.failure(e.getMessage());
        }
    }

    @GetMapping("/find")
    public ApiResult<List<Process>> find(Integer page, Integer size, String title) {
        //获取分页对象
        IPage<Process> pageResult = processEngine.processServiceInstance().title(title).page(page, size);
        //返回
        return ApiResultTools.success(pageResult.getRecords()).setTotal(pageResult.getTotal());
    }

    @GetMapping("/design/{sid}")
    public ApiResult<Process> design(@PathVariable Long sid) {
        return ApiResultTools.success(processEngine.processServiceInstance().getDesignView(sid));
    }

    @GetMapping("/detail/{sid}")
    public ApiResult<Process> detail(@PathVariable Long sid) {
        //查询
        Process process = processEngine.processServiceInstance().getById(sid);
        //判断是否存在
        if (process != null) {
            return ApiResultTools.success(process);
        } else {
            return ApiResultTools.failure("记录未找到");
        }
    }

    @DeleteMapping("/{sid}")
    public ApiResult<String> delete(@PathVariable Long sid) {
        try {
            processEngine.processServiceInstance().destroy(sid);
            return ApiResultTools.success();
        } catch (ProcessException e) {
            e.printStackTrace();
            return ApiResultTools.failure(e.getMessage());
        }
    }

    @PostMapping("/update")
    public ApiResult<String> update(@RequestBody Process process) {
        try {
            //更新
            processEngine.processServiceInstance().updateById(process);
            return ApiResultTools.success();
        } catch (ProcessException e) {
            e.printStackTrace();
            return ApiResultTools.failure(e.getMessage());
        }
    }

    @PostMapping("/reset")
    public ApiResult<String> reset(@RequestBody Process process) {
        try {
            //部署
            processEngine.processServiceInstance().reset(process);
            //返回
            return ApiResultTools.success();
        } catch (ProcessException e) {
            e.printStackTrace();
            //返回错误信息
            return ApiResultTools.failure(e.getMessage());
        }
    }
}