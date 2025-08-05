package com.sun.prism.impl.ps;

import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.prism.Graphics;
import com.sun.prism.impl.shape.BasicRoundRectRep;

/* compiled from: CachingRoundRectRep.java */
/* loaded from: jfxrt.jar:com/sun/prism/impl/ps/CachingRoundRectRepState.class */
class CachingRoundRectRepState extends CachingShapeRepState {
    CachingRoundRectRepState() {
    }

    @Override // com.sun.prism.impl.ps.CachingShapeRepState
    void fillNoCache(Graphics g2, Shape shape) {
        BasicRoundRectRep.fillRoundRect(g2, (RoundRectangle2D) shape);
    }

    @Override // com.sun.prism.impl.ps.CachingShapeRepState
    void drawNoCache(Graphics g2, Shape shape) {
        BasicRoundRectRep.drawRoundRect(g2, (RoundRectangle2D) shape);
    }
}
