package com.open.gateway.configuration;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.open.common.utils.DateUtils;
import com.open.gateway.filter.BodySecurityFilter;
import com.open.gateway.filter.BodyTokenFilter;
import com.open.gateway.filter.GlobalTraceInterceptor;
import com.open.gateway.filter.MyFastJsonHttpMessageConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class GatewayMvcConfigurer extends WebMvcConfigurationSupport {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/js/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastConverter = new MyFastJsonHttpMessageConverter();
        FormHttpMessageConverter httpMessageConverter = new FormHttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XML);
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        supportedMediaTypes.add(MediaType.TEXT_XML);
        fastConverter.setSupportedMediaTypes(supportedMediaTypes);
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setDateFormat(DateUtils.DATETIME_PATTERN);
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        converters.addAll(Arrays.asList(fastConverter, httpMessageConverter));
        super.configureMessageConverters(converters);
    }

    @Bean
    public FilterRegistrationBean customizeSignFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(bodyTokenFilter());
        registration.addUrlPatterns("/gateway");
        registration.setName("bodyTokenFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public FilterRegistrationBean customizeSecurityFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(bodySecurityFilter());
        registration.addUrlPatterns("/gateway");
        registration.setName("bodySecurityFilter");
        registration.setOrder(2);
        return registration;
    }

    @Bean
    public BodySecurityFilter bodySecurityFilter() {
        return new BodySecurityFilter();
    }

    @Bean
    public BodyTokenFilter bodyTokenFilter() {
        return new BodyTokenFilter();
    }

    @Bean
    public GlobalTraceInterceptor globalTraceInterceptor() {
        return new GlobalTraceInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalTraceInterceptor())
            .excludePathPatterns(new String[]{"/static/**"})
            .addPathPatterns("/**");
    }
}
