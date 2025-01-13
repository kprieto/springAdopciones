package com.kprieto.bootcamp.mascotas.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.kprieto.bootcamp.mascotas.component.StringToDateConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer{
    private final StringToDateConverter stringToDateConverter;

    public WebConfig(StringToDateConverter stringToDateConverter) { 
        this.stringToDateConverter = stringToDateConverter;
    }

    @Override 
    public void addFormatters(FormatterRegistry registry) { 
        registry.addConverter(stringToDateConverter);
    }
}
