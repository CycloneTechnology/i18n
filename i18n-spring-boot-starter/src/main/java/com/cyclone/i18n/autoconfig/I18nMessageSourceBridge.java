package com.cyclone.i18n.autoconfig;

import com.cyclone.i18n.I18n;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

@Component
final class I18nMessageSourceBridge implements MessageSourceAware {

    @Override
    public void setMessageSource(MessageSource messageSource) {
        I18n.initialize(messageSource);
    }
}
