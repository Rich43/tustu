package org.icepdf.core.pobjects.annotations;

import java.awt.geom.AffineTransform;
import java.util.HashMap;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/annotations/GenericAnnotation.class */
public class GenericAnnotation extends Annotation {
    public GenericAnnotation(Library l2, HashMap h2) {
        super(l2, h2);
    }

    @Override // org.icepdf.core.pobjects.annotations.Annotation
    public void resetAppearanceStream(double dx, double dy, AffineTransform pageTransform) {
    }
}
