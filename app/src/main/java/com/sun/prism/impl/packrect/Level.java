package com.sun.prism.impl.packrect;

import com.sun.javafx.geom.Rectangle;

/* loaded from: jfxrt.jar:com/sun/prism/impl/packrect/Level.class */
class Level {
    int length;
    int size;
    private int sizeOffset;
    private int lengthOffset;

    Level(int length, int size, int sizeOffset) {
        this.length = length;
        this.size = size;
        this.sizeOffset = sizeOffset;
    }

    boolean add(Rectangle rect, int x2, int y2, int requestedLength, int requestedSize, boolean vertical) {
        if (this.lengthOffset + requestedLength <= this.length && requestedSize <= this.size) {
            if (vertical) {
                rect.f11913x = this.sizeOffset;
                rect.f11914y = this.lengthOffset;
            } else {
                rect.f11913x = this.lengthOffset;
                rect.f11914y = this.sizeOffset;
            }
            this.lengthOffset += requestedLength;
            rect.f11913x += x2;
            rect.f11914y += y2;
            return true;
        }
        return false;
    }
}
