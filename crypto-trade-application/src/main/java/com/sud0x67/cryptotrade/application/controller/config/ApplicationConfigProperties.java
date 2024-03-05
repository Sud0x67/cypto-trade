/* (C)2024  Sud0x67@github.com */
package com.sud0x67.cryptotrade.application.controller.config;

import com.sud0x67.cryptotrade.core.config.SpotClientConfigProperties;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties
@Configuration
@ConfigurationProperties(prefix = "app")
public class ApplicationConfigProperties {

  @Getter SpotClientConfigProperties exchangeConfig = new SpotClientConfigProperties();
}
