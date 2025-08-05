package com.sun.prism.impl;

import com.sun.prism.MeshView;
import com.sun.prism.impl.Disposer;

/* loaded from: jfxrt.jar:com/sun/prism/impl/BaseMeshView.class */
public abstract class BaseMeshView extends BaseGraphicsResource implements MeshView {
    protected BaseMeshView(Disposer.Record disposerRecord) {
        super(disposerRecord);
    }

    public boolean isValid() {
        return true;
    }
}
