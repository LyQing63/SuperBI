package io.lyqing64.github.superbi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.lyqing64.github.superbi.domain.ChartGenerate;

import java.util.List;

/**
 * @author qingly
 * @description 针对表【chart_generate】的数据库操作Service
 * @createDate 2025-05-01 18:24:23
 */
public interface ChartGenerateService extends IService<ChartGenerate> {
    ChartGenerate getByTaskId(Long taskId);

    ChartGenerate openNewTask(Long taskId);

    List<ChartGenerate> getByTasks();
}
