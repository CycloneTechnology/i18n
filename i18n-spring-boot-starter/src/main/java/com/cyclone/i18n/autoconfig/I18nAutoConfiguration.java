// SPDX-License-Identifier: MIT

package com.cyclone.i18n.autoconfig;

import com.cyclone.i18n.I18n;
import com.cyclone.i18n.I18nMessageSource;
import com.cyclone.i18n.config.I18nProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(I18nProperties.class)
public class I18nAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(MessageSource.class)
    public MessageSource messageSource(I18nProperties properties) {
        return new I18nMessageSource(properties.getBasePath(), properties.getDefaultLocale());
    }

    @Bean
    public I18n i18n() {
        return new I18n();
    }
}
