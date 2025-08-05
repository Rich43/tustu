package com.sun.org.apache.xpath.internal;

import com.sun.org.apache.xpath.internal.functions.FuncExtFunction;
import java.util.Vector;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/ExtensionsProvider.class */
public interface ExtensionsProvider {
    boolean functionAvailable(String str, String str2) throws TransformerException;

    boolean elementAvailable(String str, String str2) throws TransformerException;

    Object extFunction(String str, String str2, Vector vector, Object obj) throws TransformerException;

    Object extFunction(FuncExtFunction funcExtFunction, Vector vector) throws TransformerException;
}
