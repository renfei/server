package net.renfei.server.main;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 程序启动入口
 *
 * @author renfei
 */
@EnableFeignClients(basePackages = {"net.renfei.*"})
@SpringBootApplication(scanBasePackages = {"net.renfei.**"})
@MapperScan(basePackages = {"net.renfei.**.repositories"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
