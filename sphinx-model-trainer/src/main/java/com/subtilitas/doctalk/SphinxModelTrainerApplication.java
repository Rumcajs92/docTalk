package com.subtilitas.doctalk;

import com.subtilitas.doctalk.adapter.configuration.AdapterConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({AdapterConfiguration.class})
public class SphinxModelTrainerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SphinxModelTrainerApplication .class, args);
    }
}
