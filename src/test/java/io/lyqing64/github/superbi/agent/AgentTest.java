package io.lyqing64.github.superbi.agent;

import cn.hutool.json.JSONUtil;
import io.lyqing64.github.superbi.response.ChartGeneratorResponse;
import io.lyqing64.github.superbi.service.AiService;
import io.lyqing64.github.superbi.utils.EnvLoader;
import io.lyqing64.github.superbi.utils.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AgentTest {

    @Autowired
    private AiService aiService;

    @BeforeAll
    static void loadEnv() {
        EnvLoader.loadEnvToSystem();
    }

    @Test
    public void test() {
        String s = FileUtils.processExcelToCsvStr("/Users/qingly/Desktop/tmp/test.xlsx");
        ChartGeneratorResponse s1 = (ChartGeneratorResponse) aiService.assistant(s);
        System.out.println(JSONUtil.toJsonStr(s1));
    }
}
