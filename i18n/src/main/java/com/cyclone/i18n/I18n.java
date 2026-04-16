package com.cyclone.i18n;

import lombok.NonNull;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

import java.util.Locale;

public class I18n implements MessageSourceAware {

    private static MessageSource messageSource;

    @Override
    public void setMessageSource(@NonNull MessageSource messageSource) {
        I18n.messageSource = messageSource;
    }

    public static String get(String code, String defaultMessage, Locale locale, Object... args) {
        String content;
        try {
            content = messageSource.getMessage(code, args, locale);
        } catch (Exception _) {
            content = defaultMessage;
        }
        return content;
    }

    public static String get(String code, Locale locale, Object... args) {
        return get(code, null, locale, args);
    }

    public static String get(String code, Object... args) {
        return get(code, Locale.getDefault(), args);
    }
}
