package org.apache.commons.math3.ode;

import java.util.Collection;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/Parameterizable.class */
public interface Parameterizable {
    Collection<String> getParametersNames();

    boolean isSupported(String str);
}
