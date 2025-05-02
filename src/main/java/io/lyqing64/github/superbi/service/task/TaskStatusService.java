package io.lyqing64.github.superbi.service.task;

import cn.hutool.core.bean.BeanUtil;
import io.lyqing64.github.superbi.domain.ChartGenerate;
import io.lyqing64.github.superbi.enums.TaskEventEnums;
import io.lyqing64.github.superbi.enums.TaskStatusEnums;
import io.lyqing64.github.superbi.service.ChartGenerateService;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskStatusService {

    private final StateMachineFactory<TaskStatusEnums, TaskEventEnums> factory;
    private final ChartGenerateService chartGenerateService;

    public TaskStatusEnums sendEvent(Long taskId, TaskEventEnums event) {
        // 查询状态
        ChartGenerate byTaskId = chartGenerateService.getByTaskId(taskId);
        // 空判断
        if (BeanUtil.isEmpty(byTaskId)) {
            throw new RuntimeException("任务不存在");
        }
        TaskStatusEnums currentState = TaskStatusEnums.valueOf(byTaskId.getStatus());

        // 2. 创建状态机，设置初始状态
        StateMachine<TaskStatusEnums, TaskEventEnums> sm = factory.getStateMachine();
        sm.stopReactively().block();
        sm.getStateMachineAccessor().doWithAllRegions(access ->
                access.resetStateMachine(new DefaultStateMachineContext<>(currentState, null, null, null))
        );
        sm.startReactively().block();

        // 3. 发送事件
        boolean accepted = sm.sendEvent(event);
        if (!accepted) {
            throw new IllegalStateException("非法状态流转：" + currentState + " + " + event);
        }

        TaskStatusEnums newState = sm.getState().getId();

        // 4. 更新数据库
        byTaskId.setStatus(newState.getCode());
        chartGenerateService.updateById(byTaskId);
        return sm.getState().getId();
    }
}