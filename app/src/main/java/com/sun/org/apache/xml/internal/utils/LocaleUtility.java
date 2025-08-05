package com.sun.org.apache.xml.internal.utils;

import java.util.Locale;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/LocaleUtility.class */
public class LocaleUtility {
    public static final char IETF_SEPARATOR = '-';
    public static final String EMPTY_STRING = "";

    public static Locale langToLocale(String lang) {
        String language;
        String language2;
        String country;
        String variant;
        if (lang == null || lang.equals("")) {
            return Locale.getDefault();
        }
        String country2 = "";
        String variant2 = "";
        int i1 = lang.indexOf(45);
        if (i1 < 0) {
            language = lang;
        } else {
            language = lang.substring(0, i1);
            int i12 = i1 + 1;
            int i2 = lang.indexOf(45, i12);
            if (i2 < 0) {
                country2 = lang.substring(i12);
            } else {
                country2 = lang.substring(i12, i2);
                variant2 = lang.substring(i2 + 1);
            }
        }
        if (language.length() == 2) {
            language2 = language.toLowerCase();
        } else {
            language2 = "";
        }
        if (country2.length() == 2) {
            country = country2.toUpperCase();
        } else {
            country = "";
        }
        if (variant2.length() > 0 && (language2.length() == 2 || country.length() == 2)) {
            variant = variant2.toUpperCase();
        } else {
            variant = "";
        }
        return new Locale(language2, country, variant);
    }
}
