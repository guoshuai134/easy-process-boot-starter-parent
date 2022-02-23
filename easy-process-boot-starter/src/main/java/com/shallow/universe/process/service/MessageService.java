package com.shallow.universe.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shallow.universe.process.model.Message;
import com.shallow.universe.process.model.Task;
import com.shallow.universe.process.repository.MessageMapper;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * ClassName:
 * Description: TODO
 *
 * @author: Guo Shuai
 * @version: 1.0
 * @Date: 2022\1\12 0012
 */
public class MessageService extends BaseService<MessageMapper, Message> {

    @Resource
    private TaskService taskService;

    public MessageService user(String... users) {
        return (MessageService) putValue("userList", Arrays.asList(users));
    }

    @Override
    public IPage<Message> page(Integer page, Integer size) {
        //获取分页数据
        IPage<Message> pageResult = super.page(page, size);
        //根据任务ID查询任务
        fillTask(pageResult.getRecords());

        return pageResult;
    }

    @Override
    public List<Message> executeQuery() {
        //获取数据
        List<Message> messages = super.executeQuery();
        //填充任务数据
        fillTask(messages);

        return messages;
    }

    /**
     * 填充任务数据
     *
     * @param messages
     */
    private void fillTask(List<Message> messages) {
        for (Message message : messages) {
            Task task = taskService.getById(message.getTaskId());
            task.setTargetData(taskService.executeTargetQuery(task));
            message.setTask(task);
        }
    }
}
