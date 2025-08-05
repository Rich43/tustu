package com.sun.prism.impl.ps;

import com.sun.javafx.geom.Ellipse2D;
import com.sun.javafx.geom.Shape;
import com.sun.prism.Graphics;

/* compiled from: CachingEllipseRep.java */
/* loaded from: jfxrt.jar:com/sun/prism/impl/ps/CachingEllipseRepState.class */
class CachingEllipseRepState extends CachingShapeRepState {
    CachingEllipseRepState() {
    }

    @Override // com.sun.prism.impl.ps.CachingShapeRepState
    void fillNoCache(Graphics g2, Shape shape) {
        Ellipse2D e2 = (Ellipse2D) shape;
        g2.fillEllipse(e2.f11899x, e2.f11900y, e2.width, e2.height);
    }

    @Override // com.sun.prism.impl.ps.CachingShapeRepState
    void drawNoCache(Graphics g2, Shape shape) {
        Ellipse2D e2 = (Ellipse2D) shape;
        g2.drawEllipse(e2.f11899x, e2.f11900y, e2.width, e2.height);
    }
}
