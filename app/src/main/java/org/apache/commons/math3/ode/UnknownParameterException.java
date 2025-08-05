package org.apache.commons.math3.ode;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/UnknownParameterException.class */
public class UnknownParameterException extends MathIllegalArgumentException {
    private static final long serialVersionUID = 20120902;
    private final String name;

    public UnknownParameterException(String name) {
        super(LocalizedFormats.UNKNOWN_PARAMETER, name);
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
