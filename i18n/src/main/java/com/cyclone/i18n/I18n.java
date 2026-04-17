// SPDX-License-Identifier: MIT

package com.cyclone.i18n;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class I18n {

    private static MessageSource messageSource;

    public static synchronized void initialize(MessageSource source) {
        if (messageSource != null) {
            throw new IllegalStateException("MessageSource already initialized");
        }
        messageSource = Objects.requireNonNull(source);
    }

    static void resetForTests() {
        messageSource = null;
    }

    private static MessageSource source() {
        if (messageSource == null) {
            throw new IllegalStateException("I18n MessageSource not initialized");
        }
        return messageSource;
    }

    public static String get(String code, String defaultMessage, Locale locale, Object... args) {
        String content;
        try {
            content = source().getMessage(code, args, locale);
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
