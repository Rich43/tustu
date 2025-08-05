package com.sun.org.apache.xerces.internal.xs.datatypes;

import java.math.BigDecimal;
import java.math.BigInteger;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xs/datatypes/XSDecimal.class */
public interface XSDecimal {
    BigDecimal getBigDecimal();

    BigInteger getBigInteger() throws NumberFormatException;

    long getLong() throws NumberFormatException;

    int getInt() throws NumberFormatException;

    short getShort() throws NumberFormatException;

    byte getByte() throws NumberFormatException;
}
