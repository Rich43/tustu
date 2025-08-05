package com.sun.prism.impl.ps;

/* loaded from: jfxrt.jar:com/sun/prism/impl/ps/CachingEllipseRep.class */
public class CachingEllipseRep extends CachingShapeRep {
    @Override // com.sun.prism.impl.ps.CachingShapeRep
    CachingShapeRepState createState() {
        return new CachingEllipseRepState();
    }
}
