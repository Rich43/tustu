package com.sun.prism.impl.ps;

/* loaded from: jfxrt.jar:com/sun/prism/impl/ps/CachingRoundRectRep.class */
public class CachingRoundRectRep extends CachingShapeRep {
    @Override // com.sun.prism.impl.ps.CachingShapeRep
    CachingShapeRepState createState() {
        return new CachingRoundRectRepState();
    }
}
