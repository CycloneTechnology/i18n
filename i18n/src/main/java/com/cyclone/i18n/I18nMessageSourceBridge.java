// SPDX-License-Identifier: MIT

package com.cyclone.i18n;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.annotation.Role;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public final class I18nMessageSourceBridge implements MessageSourceAware {

    @Override
    public void setMessageSource(@NonNull MessageSource messageSource) {
        I18n.initialize(messageSource);
    }
}
