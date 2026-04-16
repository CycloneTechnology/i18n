package com.cyclone.i18n;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.lang.reflect.Method;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class I18nStaticTests {
    @SuppressWarnings("WriteOnlyObject")
    private static final I18n i18n = new I18n();

    private void logTestInfo(TestInfo testInfo) {
        log.info("{}",
                testInfo.getTestMethod().map(Method::getName).orElse("<unknown Method>"));
    }

    @BeforeAll
    static void setup() {
        String basePath = "i18n";
        String defaultLocale = "en";
        i18n.setMessageSource(new I18nMessageSource(basePath, defaultLocale));
    }

    @Test
    void testEnglishMessageResolution(TestInfo testInfo) {
        logTestInfo(testInfo);

        String message = I18n.get("greeting", Locale.ENGLISH);
        assertEquals("Hello!", message);
    }

    @Test
    void testBritishEnglishMessageResolution(TestInfo testInfo) {
        logTestInfo(testInfo);

        String message = I18n.get("greeting", Locale.UK);
        assertEquals("Hello from GB!", message);
    }

    @Test
    void testFallbackToLanguageOnlyLocale(TestInfo testInfo) {
        logTestInfo(testInfo);

        Locale locale = Locale.forLanguageTag("en-GB-oed");
        String message = I18n.get("greeting", locale);
        assertEquals("Hello from GB!", message);
    }

    @Test
    void testFallbackToDefaultLocale(TestInfo testInfo) {
        logTestInfo(testInfo);

        Locale locale = Locale.forLanguageTag("es-ES");
        String message = I18n.get("greeting", locale);
        assertEquals("Hello!", message); // fallback to default en
    }

    @Test
    void testFrenchMessageResolution(TestInfo testInfo) {
        logTestInfo(testInfo);

        Locale locale = Locale.FRENCH;
        String message = I18n.get("greeting", locale);
        assertEquals("Bonjour!", message);
    }

    @Test
    void testGermanMessageResolution(TestInfo testInfo) {
        logTestInfo(testInfo);

        Locale locale = Locale.GERMAN;
        String message = I18n.get("greeting", locale);
        assertEquals("Hallo!", message);
    }

    @Test
    void testInterpolationInEnglish(TestInfo testInfo) {
        logTestInfo(testInfo);

        Locale locale = Locale.ENGLISH;
        String message = I18n.get("welcome", locale);
        assertEquals("Welcome! Your lucky number is 42", message);
    }

    @Test
    void testInterpolationInFrench(TestInfo testInfo) {
        logTestInfo(testInfo);

        Locale locale = Locale.FRENCH;
        String message = I18n.get("welcome", locale);
        assertEquals("Bienvenue ! Votre numéro chanceux est 42", message);
    }

    @Test
    void testInterpolationInGerman(TestInfo testInfo) {
        logTestInfo(testInfo);

        Locale locale = Locale.GERMAN;
        String message = I18n.get("welcome", locale);
        assertEquals("Willkommen! Ihre Glückszahl ist 42", message);
    }

    @Test
    void testNumericKeyInEnglish(TestInfo testInfo) {
        logTestInfo(testInfo);

        String message = I18n.get("123", Locale.ENGLISH);
        assertEquals("Numeric key message", message);
    }

    @Test
    void testNumericKeyInFrench(TestInfo testInfo) {
        logTestInfo(testInfo);

        String message = I18n.get("456", Locale.FRENCH);
        assertEquals("Message clé numérique", message);
    }

    @Test
    void testNestedKeyInGerman(TestInfo testInfo) {
        logTestInfo(testInfo);

        String message = I18n.get("nested.message", Locale.GERMAN);
        assertEquals("Dies ist eine verschachtelte Nachricht", message);
    }

    @Test
    void testMissingMessageWithDefaultFallbackInFrench(TestInfo testInfo) {
        logTestInfo(testInfo);

        Locale locale = Locale.FRENCH;
        String message = I18n.get("farewell", locale);
        assertEquals("Goodbye!", message); // fallback to default en
    }

    @Test
    void testFormattedMessageInGerman(TestInfo testInfo) {
        logTestInfo(testInfo);

        Locale locale = Locale.GERMAN;
        Object[] args = new Object[] { "Phil", 99 };
        String message = I18n.get("formatted.message", locale, args);
        assertEquals("Hallo Phil, Sie haben 99 Punkte", message);
    }

    @Test
    void testMissingMessageReturnsDefaultValue(TestInfo testInfo) {
        logTestInfo(testInfo);

        String message = I18n.get("nonexistent.key", "Default Message", Locale.ENGLISH);
        assertEquals("Default Message", message);
    }
}
