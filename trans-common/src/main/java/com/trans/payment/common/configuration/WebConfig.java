package com.trans.payment.common.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.trans.payment.common.utils.DateUtils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

  @Bean
  public FilterRegistrationBean webFilter() {
    FilterRegistrationBean registration = new FilterRegistrationBean();
    CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
    characterEncodingFilter.setEncoding("UTF-8");
    registration.setFilter(characterEncodingFilter);
    return registration;
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    MappingJackson2HttpMessageConverter jackson2HttpMessageConverter =
            new MappingJackson2HttpMessageConverter();
    // ObjectMapper 是Jackson库的主要类。它提供一些功能将转换成Java对象匹配JSON结构,反之亦然
    ObjectMapper objectMapper = new ObjectMapper();
    SimpleModule simpleModule = new SimpleModule();
    // 序列化将Long转String类型
    simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
    simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
    SimpleModule bigIntegerModule = new SimpleModule();
    bigIntegerModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
    SimpleModule bigDecimalModule = new SimpleModule();
    bigDecimalModule.addSerializer(BigDecimal.class, ToStringSerializer.instance);
    objectMapper.registerModule(simpleModule);
    objectMapper.registerModule(bigDecimalModule);
    objectMapper.registerModule(bigIntegerModule);
    TimeZone timeZone = TimeZone.getTimeZone("GMT+8:00");
    objectMapper.setTimeZone(timeZone);
    // 日期类型处理
    objectMapper.setDateFormat(new SimpleDateFormat(DateUtils.DATETIME_PATTERN));
    // 允许出现特殊字符和转义符
    objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    // 允许出现单引号
    objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    jackson2HttpMessageConverter.setObjectMapper(objectMapper);
    converters.add(jackson2HttpMessageConverter);
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

//  @Bean
//  public Logger.Level feignLoggerLevel() {
//    return feign.Logger.Level.FULL;
//  }

//  @Bean
//  public Request.Options options() {
//    return new Request.Options(12000, 12000);
//  }

//  @Bean
//  public Retryer feignRetried() {
//    return new Retryer.Default(100, TimeUnit.SECONDS.toMillis(1L), 1);
//  }
}
