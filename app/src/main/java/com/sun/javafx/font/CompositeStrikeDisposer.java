package com.sun.javafx.font;

import java.lang.ref.WeakReference;

/* loaded from: jfxrt.jar:com/sun/javafx/font/CompositeStrikeDisposer.class */
class CompositeStrikeDisposer implements DisposerRecord {
    FontResource fontResource;
    FontStrikeDesc desc;
    boolean disposed = false;

    public CompositeStrikeDisposer(FontResource font, FontStrikeDesc desc) {
        this.fontResource = font;
        this.desc = desc;
    }

    @Override // com.sun.javafx.font.DisposerRecord
    public synchronized void dispose() {
        if (!this.disposed) {
            WeakReference ref = this.fontResource.getStrikeMap().get(this.desc);
            if (ref != null) {
                Object o2 = ref.get();
                if (o2 == null) {
                    this.fontResource.getStrikeMap().remove(this.desc);
                }
            }
            this.disposed = true;
        }
    }
}
