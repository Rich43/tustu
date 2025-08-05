package com.google.gson;

import java.lang.reflect.Type;

/* loaded from: gson-2.9.0.jar:com/google/gson/InstanceCreator.class */
public interface InstanceCreator<T> {
    T createInstance(Type type);
}
