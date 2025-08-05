package org.apache.commons.math3.exception.util;

import java.io.Serializable;
import java.util.Locale;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/exception/util/Localizable.class */
public interface Localizable extends Serializable {
    String getSourceString();

    String getLocalizedString(Locale locale);
}
