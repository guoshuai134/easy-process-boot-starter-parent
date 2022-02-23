package com.example;

import com.example.model.Leaves;
import com.example.service.LeavesService;
import com.shallow.universe.process.core.ProcessEngine;
import com.shallow.universe.process.core.constant.ProcessConstant;
import com.shallow.universe.process.model.TaskStep;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = Application.class)
public class ApplicationTests {

    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private LeavesService leavesService;

    @Test
    public void test02() {
        Leaves leaves = new Leaves().setContent("123").setDays("2");
        leavesService.save(leaves);

        processEngine.taskServiceInstance().runTask(leaves, "gs", "soft");
    }

    @Test
    public void test03() {
        System.out.println(processEngine.taskServiceInstance().approveUser("admin").page(1, 5).getRecords());

        System.out.println(processEngine.taskServiceInstance().approveRole("主管").executeQuery());

        System.out.println(processEngine.taskServiceInstance().submitBy("gs").executeQuery());
    }

    @Test
    public void test04() {
        TaskStep taskStep = new TaskStep();

        taskStep.setUser("guest").setRole("经理")
                .setTaskId(1L).setOpinion(ProcessConstant.APPROVE_ALLOW_STATUS)
                .setReason("同意").setStageId(1L);

        processEngine.taskStepServiceInstance().nextStep(taskStep);
    }

    @Test
    public void test05() {
        TaskStep taskStep = new TaskStep();

        taskStep.setUser("gs").setUser("xw").setRole("主管")
                .setTaskId(1L).setOpinion(ProcessConstant.APPROVE_ALLOW_STATUS)
                .setReason("同意").setStageId(1L);

        processEngine.taskStepServiceInstance().nextStep(taskStep);
    }

    @Test
    public void test06() {
        TaskStep taskStep = new TaskStep();

        taskStep.setUser("admin").setRole("经理")
                .setTaskId(1L).setOpinion(ProcessConstant.APPROVE_ALLOW_STATUS)
                .setReason("同意").setStageId(2L);

        processEngine.taskStepServiceInstance().nextStep(taskStep);
    }

    @Test
    public void test07() {
        System.out.println(processEngine.taskServiceInstance().target("leaves").finished().executeQuery());
    }

    @Test
    public void test08() {
        System.out.println(processEngine.processStageServiceInstance().processId(1L).executeQuery());
    }

    @Test
    public void test09() {
        Leaves leaves = new Leaves().setContent("123");
        leavesService.save(leaves);

        processEngine.taskServiceInstance().runTask(leaves, "gs", "soft", "经理", "老板");
    }

    @Test
    public void test10() {
        processEngine.taskServiceInstance().destroy(1L);
    }

    @Test
    public void test12() {
        processEngine.processServiceInstance().destroy(2L);
    }

    @Test
    public void test13() {
        System.out.println(processEngine.messageServiceInstance().user("gs").executeQuery());
    }
}
