package dynamic.chat.bot;

import org.springframework.boot.SpringApplication;

public class TestDynamicApplication {

    public static void main(String[] args) {
        SpringApplication.from(DynamicApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
