package com.club;

import com.club.config.AppConfig;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@OpenAPIDefinition(servers = { @Server(url = "/", description = "Default Server URL") })
public class ClubSystemAdminApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ClubSystemAdminApplication.class, args);
        AppConfig appConfig = applicationContext.getBean(AppConfig.class);
        System.out.println("""
                  _   _  ____       ____  _    _  _____
                 | \\ | |/ __ \\     |  _ \\| |  | |/ ____|
                 |  \\| | |  | |    | |_) | |  | | |  __
                 | . ` | |  | |    |  _ <| |  | | | |_ |
                 | |\\  | |__| |    | |_) | |__| | |__| |
                 |_| \\_|\\____/     |____/ \\____/ \\_____|
                """);
        System.out.println("项目地址：http://localhost:" + appConfig.getPort() + "/doc.html");
        ProcessBuilder proc = new ProcessBuilder("cmd.exe", "/c", "start http://localhost:" + appConfig.getPort() + "/doc.html");
        proc.start();
    }

}
