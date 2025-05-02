package io.lyqing64.github.superbi.response;

import lombok.Data;

@Data
public class ChartGeneratorResponse {
    public String echartCode;  // ECharts option JSON
    public String analysis;    // 模型对数据的理解与建议
}
