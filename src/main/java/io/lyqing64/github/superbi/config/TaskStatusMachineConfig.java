package io.lyqing64.github.superbi.config;

import io.lyqing64.github.superbi.enums.TaskEventEnums;
import io.lyqing64.github.superbi.enums.TaskStatusEnums;
import io.lyqing64.github.superbi.listener.TaskStateListener;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachineFactory
public class TaskStatusMachineConfig extends
        EnumStateMachineConfigurerAdapter<TaskStatusEnums, TaskEventEnums> {

    private final TaskStateListener taskStateListener;

    public TaskStatusMachineConfig(TaskStateListener taskStateListener) {
        this.taskStateListener = taskStateListener;
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<TaskStatusEnums, TaskEventEnums> config)
            throws Exception {
        config
                .withConfiguration()
                .autoStartup(true)
                .listener(taskStateListener);
    }

    @Override
    public void configure(StateMachineStateConfigurer<TaskStatusEnums, TaskEventEnums> states) throws Exception {
        states
                .withStates()
                .initial(TaskStatusEnums.WAITING)
                .state(TaskStatusEnums.PARSING)
                .state(TaskStatusEnums.PARSING_ERROR)
                .state(TaskStatusEnums.GENERATING)
                .state(TaskStatusEnums.GENERATING_ERROR)
                .state(TaskStatusEnums.SUCCESS);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<TaskStatusEnums, TaskEventEnums> transitions) throws Exception {
        transitions
                .withExternal().source(TaskStatusEnums.WAITING).target(TaskStatusEnums.PARSING).event(TaskEventEnums.UPLOAD_COMPLETE)
                .and()
                .withExternal().source(TaskStatusEnums.PARSING).target(TaskStatusEnums.PARSING_ERROR).event(TaskEventEnums.PARSING_ERROR)
                .and()
                .withExternal().source(TaskStatusEnums.PARSING).target(TaskStatusEnums.GENERATING).event(TaskEventEnums.PARSING_SUCCESS)
                .and()
                .withExternal().source(TaskStatusEnums.GENERATING).target(TaskStatusEnums.GENERATING_ERROR).event(TaskEventEnums.GENERATING_ERROR)
                .and()
                .withExternal().source(TaskStatusEnums.GENERATING).target(TaskStatusEnums.SUCCESS).event(TaskEventEnums.GENERATING_SUCCESS);
    }

    @PostConstruct
    public void init() {
        System.out.println("✅ 状态机配置已加载");
    }
}