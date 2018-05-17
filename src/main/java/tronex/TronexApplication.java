package tronex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import tronex.tasks.SyncHistoryBlockTask;

@EnableAutoConfiguration
@SpringBootApplication
@EnableScheduling
public class TronexApplication {
    public static void main(String[] args) {

        SpringApplication.run(TronexApplication.class, args);
    }
}
