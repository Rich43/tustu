package com.sun.javafx.font.coretext;

import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.FontStrikeDesc;
import java.lang.ref.WeakReference;

/* loaded from: jfxrt.jar:com/sun/javafx/font/coretext/CTStrikeDisposer.class */
class CTStrikeDisposer implements DisposerRecord {
    private FontResource fontResource;
    private FontStrikeDesc desc;
    private long fontRef;
    private boolean disposed = false;

    public CTStrikeDisposer(FontResource font, FontStrikeDesc desc, long fontRef) {
        this.fontRef = 0L;
        this.fontResource = font;
        this.desc = desc;
        this.fontRef = fontRef;
    }

    @Override // com.sun.javafx.font.DisposerRecord
    public synchronized void dispose() {
        if (!this.disposed) {
            WeakReference<FontStrike> ref = this.fontResource.getStrikeMap().get(this.desc);
            if (ref != null) {
                Object o2 = ref.get();
                if (o2 == null) {
                    this.fontResource.getStrikeMap().remove(this.desc);
                }
            }
            if (this.fontRef != 0) {
                OS.CFRelease(this.fontRef);
                this.fontRef = 0L;
            }
            this.disposed = true;
        }
    }
}
