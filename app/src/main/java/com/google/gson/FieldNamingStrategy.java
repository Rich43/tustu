package com.google.gson;

import java.lang.reflect.Field;

/* loaded from: gson-2.9.0.jar:com/google/gson/FieldNamingStrategy.class */
public interface FieldNamingStrategy {
    String translateName(Field field);
}
