package org.icepdf.core.pobjects.graphics;

import java.awt.Graphics;
import org.icepdf.core.pobjects.Page;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/WatermarkCallback.class */
public interface WatermarkCallback {
    void paintWatermark(Graphics graphics, Page page, int i2, int i3, float f2, float f3);
}
