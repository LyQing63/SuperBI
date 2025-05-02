package io.lyqing64.github.superbi.listener;

import io.lyqing64.github.superbi.enums.TaskEventEnums;
import io.lyqing64.github.superbi.enums.TaskStatusEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TaskStateListener extends StateMachineListenerAdapter<TaskStatusEnums, TaskEventEnums> {
    @Override
    public void stateChanged(State<TaskStatusEnums, TaskEventEnums> from, State<TaskStatusEnums, TaskEventEnums> to) {
        if (from == null || from.getId() == to.getId()) return;
        log.info("任务状态变更: {} → {}", from.getId(), to.getId());
    }
}