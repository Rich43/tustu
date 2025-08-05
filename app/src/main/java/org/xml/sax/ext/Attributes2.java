package org.xml.sax.ext;

import org.xml.sax.Attributes;

/* loaded from: rt.jar:org/xml/sax/ext/Attributes2.class */
public interface Attributes2 extends Attributes {
    boolean isDeclared(int i2);

    boolean isDeclared(String str);

    boolean isDeclared(String str, String str2);

    boolean isSpecified(int i2);

    boolean isSpecified(String str, String str2);

    boolean isSpecified(String str);
}
