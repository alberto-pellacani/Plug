package com.plug;

import com.plug.auth.AuthenticationProvider;
import com.plug.dao.SQLAuthenticationProvider;
import com.plug.auth.SecurityFilter;

import jakarta.servlet.Filter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import javax.sql.DataSource;


@Configuration
public class PlugConfig {


    @Autowired
    private BeanFactory factory;

    /*
    @Value("${plug.authentication.provider}")
    private String authentication_provider;
    */

    /*
    @Bean
    public SecurityFilter getSecurityFilter() {
        return new SecurityFilter();
    }
     */

    @Bean
    public AuthenticationProvider getAuthenticationProvider() {

        //System.out.println("-->" + this.authentication_provider);


        return new SQLAuthenticationProvider();
    }

    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        //dataSourceBuilder.username("SA");
        //dataSourceBuilder.password("");
        DataSource d;

        return dataSourceBuilder.build();
    }



    @Bean
    @ConditionalOnProperty(
            value="plug.authorization.enabled",
            havingValue = "true",
            matchIfMissing = false)
    public FilterRegistrationBean<Filter> securityFilter(){

        FilterRegistrationBean<Filter> filters = new FilterRegistrationBean<>();
        filters.setFilter(factory.getBean(SecurityFilter.class));

        filters.addUrlPatterns("/*");
        filters.setOrder(0);
        filters.setAsyncSupported(true);



        return filters;
    }


}
