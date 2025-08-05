package com.sun.prism.impl;

import com.sun.prism.PhongMaterial;
import com.sun.prism.impl.Disposer;

/* loaded from: jfxrt.jar:com/sun/prism/impl/BasePhongMaterial.class */
public abstract class BasePhongMaterial extends BaseGraphicsResource implements PhongMaterial {
    protected BasePhongMaterial(Disposer.Record disposerRecord) {
        super(disposerRecord);
    }

    public boolean isValid() {
        return true;
    }
}
