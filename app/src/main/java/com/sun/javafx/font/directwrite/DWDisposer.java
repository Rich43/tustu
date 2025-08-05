package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.PrismFontFactory;

/* loaded from: jfxrt.jar:com/sun/javafx/font/directwrite/DWDisposer.class */
class DWDisposer implements DisposerRecord {
    IUnknown resource;

    DWDisposer(IUnknown resource) {
        this.resource = resource;
    }

    @Override // com.sun.javafx.font.DisposerRecord
    public synchronized void dispose() {
        if (this.resource != null) {
            this.resource.Release();
            if (PrismFontFactory.debugFonts) {
                System.err.println("DisposerRecord=" + ((Object) this.resource));
            }
            this.resource = null;
        }
    }
}
