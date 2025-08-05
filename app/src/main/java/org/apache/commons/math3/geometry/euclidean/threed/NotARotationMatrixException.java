package org.apache.commons.math3.geometry.euclidean.threed;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.Localizable;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/threed/NotARotationMatrixException.class */
public class NotARotationMatrixException extends MathIllegalArgumentException {
    private static final long serialVersionUID = 5647178478658937642L;

    public NotARotationMatrixException(Localizable specifier, Object... parts) {
        super(specifier, parts);
    }
}
