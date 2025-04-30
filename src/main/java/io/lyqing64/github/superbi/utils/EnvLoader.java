package io.lyqing64.github.superbi.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvLoader {
    public static void loadEnvToSystem() {
        Dotenv dotenv = Dotenv.configure()
                .directory("./")      // 默认是根目录
                .ignoreIfMissing()   // 忽略文件缺失而不报错
                .load();

        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue()) // 注入为系统属性
        );
    }
}
