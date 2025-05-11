package io.lyqing64.github.superbi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.lyqing64.github.superbi.domain.ChartGenerate;
import io.lyqing64.github.superbi.enums.TaskStatusEnums;
import io.lyqing64.github.superbi.mapper.ChartGenerateMapper;
import io.lyqing64.github.superbi.service.ChartGenerateService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qingly
 * @description 针对表【chart_generate】的数据库操作Service实现
 * @createDate 2025-05-01 18:24:23
 */
@Service
public class ChartGenerateServiceImpl extends ServiceImpl<ChartGenerateMapper, ChartGenerate>
        implements ChartGenerateService {

    @Override
    public ChartGenerate getByTaskId(Long taskId) {
        return this.getOne(new LambdaQueryWrapper<ChartGenerate>()
                .eq(ChartGenerate::getFileId, taskId)
                .last("LIMIT 1"));
    }

    @Override
    public ChartGenerate openNewTask(Long taskId) {
        ChartGenerate chartGenerate = new ChartGenerate();
        chartGenerate.setFileId(taskId);
        chartGenerate.setStatus(TaskStatusEnums.WAITING.getCode());
        this.save(chartGenerate);
        return chartGenerate;
    }

    @Override
    public List<ChartGenerate> getByTasks() {
        return this.list(new LambdaQueryWrapper<ChartGenerate>().ne(ChartGenerate::getStatus, TaskStatusEnums.SUCCESS.getCode()));
    }
}




