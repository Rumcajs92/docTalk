package com.subtilitas.doctalk.adapter.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("com.subtilitas.doctalk.adapter")
@EnableJpaRepositories("com.subtilitas.doctalk.adapter.repository")
@EntityScan("com.subtilitas.doctalk.adapter.model")
public class AdapterConfiguration {

}
