# 工程简介
一款简单的工作流设计器，完整功能请拉取design分支

# 使用步骤
#### 1. 访问http://host:port/shallow-ui/process-design.html
#### 2. 在流程设计模块设计流程并保存 
#### 3. 用@ProcessEntity注解标注要做流程的表名已经要开的流程名称，用@PrimaryKey注解标注出主键
```
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
```
##### 4. 调用方法开启流程，gs为用户名，soft为部门（可选）
```
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
```
#### 5. 调用方法进行审批
```
    @Test
    public void test04() {
        TaskStep taskStep = new TaskStep();

        taskStep.setUser("guest").setRole("经理")
                .setTaskId(1L).setOpinion(ProcessConstant.APPROVE_ALLOW_STATUS)
                .setReason("同意").setStageId(1L);

        processEngine.taskStepServiceInstance().nextStep(taskStep);
    }
```
#### 6.查询自己的审批单
```
    @Test
    public void test03() {
        System.out.println(processEngine.taskServiceInstance().approveUser("admin").page(1, 5).getRecords());

        System.out.println(processEngine.taskServiceInstance().approveRole("主管").executeQuery());

        System.out.println(processEngine.taskServiceInstance().submitBy("gs").executeQuery());
    }
```