package cn.java.receive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@ComponentScan("cn.java.receive")
@RestController
@SpringBootApplication //Spring Boot核心注解，用于开启自动配置
public class DemoApplication {

    @RequestMapping("/")
    String index() throws Exception {
        return "Hello Spring Boot";
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }


}
