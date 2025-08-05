package com.sun.xml.internal.ws.server.sei;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.message.Packet;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/Invoker.class */
public abstract class Invoker {
    public abstract Object invoke(@NotNull Packet packet, @NotNull Method method, @NotNull Object... objArr) throws IllegalAccessException, InvocationTargetException;
}
