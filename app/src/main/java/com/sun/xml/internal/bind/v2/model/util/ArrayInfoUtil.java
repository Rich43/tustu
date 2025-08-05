package com.sun.xml.internal.bind.v2.model.util;

import com.sun.xml.internal.bind.v2.TODO;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/util/ArrayInfoUtil.class */
public class ArrayInfoUtil {
    private ArrayInfoUtil() {
    }

    public static QName calcArrayTypeName(QName n2) {
        String uri;
        if (n2.getNamespaceURI().equals("http://www.w3.org/2001/XMLSchema")) {
            TODO.checkSpec("this URI");
            uri = "http://jaxb.dev.java.net/array";
        } else {
            uri = n2.getNamespaceURI();
        }
        return new QName(uri, n2.getLocalPart() + "Array");
    }
}
