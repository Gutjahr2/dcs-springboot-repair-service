package de.dicos.springboot.repairservice.restful.config;

import java.util.List;

import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxrs.provider.BinaryDataProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import de.dicos.springboot.repairservice.gen.api.DefaultApi;
import de.dicos.springboot.repairservice.restful.exception.AdministrationSystemResponseExceptionMapper;

@Configuration
public class AdministrationClientConfig {

    @Bean
    public DefaultApi defaultApi(@Value("${administration.api.url}") String apiUrl) {
        JAXRSClientFactoryBean clientFactoryBean = new JAXRSClientFactoryBean();
        clientFactoryBean.setAddress(apiUrl);
        clientFactoryBean.setProviders(List.of(
            new BinaryDataProvider<>(),
            new JacksonJsonProvider(),
            new AdministrationSystemResponseExceptionMapper()
        ));
        clientFactoryBean.setServiceClass(DefaultApi.class);
        return (DefaultApi) clientFactoryBean.create();
    }
}
