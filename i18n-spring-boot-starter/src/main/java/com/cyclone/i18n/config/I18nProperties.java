// SPDX-License-Identifier: MIT

package com.cyclone.i18n.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "i18n")
public class I18nProperties {
    private String basePath = "i18n";
    private String defaultLocale = "en";
}
