package com.sun.xml.internal.bind.v2.runtime;

import com.sun.istack.internal.NotNull;
import javax.xml.namespace.NamespaceContext;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/NamespaceContext2.class */
public interface NamespaceContext2 extends NamespaceContext {
    String declareNamespace(String str, String str2, boolean z2);

    int force(@NotNull String str, @NotNull String str2);
}
