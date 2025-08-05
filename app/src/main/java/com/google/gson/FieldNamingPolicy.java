package com.google.gson;

import java.lang.reflect.Field;
import java.util.Locale;

/* loaded from: gson-2.9.0.jar:com/google/gson/FieldNamingPolicy.class */
public enum FieldNamingPolicy implements FieldNamingStrategy {
    IDENTITY { // from class: com.google.gson.FieldNamingPolicy.1
        @Override // com.google.gson.FieldNamingStrategy
        public String translateName(Field f2) {
            return f2.getName();
        }
    },
    UPPER_CAMEL_CASE { // from class: com.google.gson.FieldNamingPolicy.2
        @Override // com.google.gson.FieldNamingStrategy
        public String translateName(Field f2) {
            return upperCaseFirstLetter(f2.getName());
        }
    },
    UPPER_CAMEL_CASE_WITH_SPACES { // from class: com.google.gson.FieldNamingPolicy.3
        @Override // com.google.gson.FieldNamingStrategy
        public String translateName(Field f2) {
            return upperCaseFirstLetter(separateCamelCase(f2.getName(), ' '));
        }
    },
    UPPER_CASE_WITH_UNDERSCORES { // from class: com.google.gson.FieldNamingPolicy.4
        @Override // com.google.gson.FieldNamingStrategy
        public String translateName(Field f2) {
            return separateCamelCase(f2.getName(), '_').toUpperCase(Locale.ENGLISH);
        }
    },
    LOWER_CASE_WITH_UNDERSCORES { // from class: com.google.gson.FieldNamingPolicy.5
        @Override // com.google.gson.FieldNamingStrategy
        public String translateName(Field f2) {
            return separateCamelCase(f2.getName(), '_').toLowerCase(Locale.ENGLISH);
        }
    },
    LOWER_CASE_WITH_DASHES { // from class: com.google.gson.FieldNamingPolicy.6
        @Override // com.google.gson.FieldNamingStrategy
        public String translateName(Field f2) {
            return separateCamelCase(f2.getName(), '-').toLowerCase(Locale.ENGLISH);
        }
    },
    LOWER_CASE_WITH_DOTS { // from class: com.google.gson.FieldNamingPolicy.7
        @Override // com.google.gson.FieldNamingStrategy
        public String translateName(Field f2) {
            return separateCamelCase(f2.getName(), '.').toLowerCase(Locale.ENGLISH);
        }
    };

    static String separateCamelCase(String name, char separator) {
        StringBuilder translation = new StringBuilder();
        int length = name.length();
        for (int i2 = 0; i2 < length; i2++) {
            char character = name.charAt(i2);
            if (Character.isUpperCase(character) && translation.length() != 0) {
                translation.append(separator);
            }
            translation.append(character);
        }
        return translation.toString();
    }

    static String upperCaseFirstLetter(String s2) {
        int length = s2.length();
        for (int i2 = 0; i2 < length; i2++) {
            char c2 = s2.charAt(i2);
            if (Character.isLetter(c2)) {
                if (Character.isUpperCase(c2)) {
                    return s2;
                }
                char uppercased = Character.toUpperCase(c2);
                if (i2 == 0) {
                    return uppercased + s2.substring(1);
                }
                return s2.substring(0, i2) + uppercased + s2.substring(i2 + 1);
            }
        }
        return s2;
    }
}
