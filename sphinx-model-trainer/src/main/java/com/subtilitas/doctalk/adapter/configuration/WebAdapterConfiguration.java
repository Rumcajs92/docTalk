package com.subtilitas.doctalk.adapter.configuration;

import com.subtilitas.doctalk.adapter.converter.FileSourceEnumConverter;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebAdapterConfiguration extends WebMvcConfigurationSupport {
    @Override
    public FormattingConversionService mvcConversionService() {
        FormattingConversionService service =  super.mvcConversionService();
        service.addConverter(new FileSourceEnumConverter());
        return service;
    }


}
