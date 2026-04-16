package com.cyclone.i18n;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class I18nMessageInterpolator {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{([^}]+)}");

    public String interpolate(String message, Map<String, Object> context) {
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(message);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String key = matcher.group(1);
            Object value = context.get(key); // flat lookup first
            if (value == null) {
                value = resolveNestedKey(context, key); // try nested lookup
            }
            matcher.appendReplacement(sb, Matcher.quoteReplacement(value != null ? value.toString() : "${" + key + "}"));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private Object resolveNestedKey(Map<String, Object> context, String key) {
        String[] parts = key.split("\\.");
        Object current = context;
        for (String part : parts) {
            if (current instanceof Map<?, ?> map) {
                current = map.get(part);
            } else {
                return null;
            }
        }
        return current;
    }
}
