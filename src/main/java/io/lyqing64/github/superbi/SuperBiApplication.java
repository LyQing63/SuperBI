package io.lyqing64.github.superbi;

import io.lyqing64.github.superbi.utils.EnvLoader;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("io.lyqing64.github.superbi.mapper")
public class SuperBiApplication {

    public static void main(String[] args) {
        EnvLoader.loadEnvToSystem();
        SpringApplication.run(SuperBiApplication.class, args);
    }

}
