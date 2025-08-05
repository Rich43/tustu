package org.icepdf.core.pobjects.graphics;

import java.awt.Color;
import java.util.HashMap;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/DeviceRGB.class */
public class DeviceRGB extends PColorSpace {
    public static final Name DEVICERGB_KEY = new Name("DeviceRGB");
    public static final Name RGB_KEY = new Name("RGB");

    DeviceRGB(Library l2, HashMap h2) {
        super(l2, h2);
    }

    @Override // org.icepdf.core.pobjects.graphics.PColorSpace
    public int getNumComponents() {
        return 3;
    }

    private float validateColorRange(float component) {
        if (component < 0.0f) {
            return 0.0f;
        }
        if (component > 1.0f) {
            return 1.0f;
        }
        return component;
    }

    @Override // org.icepdf.core.pobjects.graphics.PColorSpace
    public Color getColor(float[] colours, boolean fillAndStroke) {
        return new Color(validateColorRange(colours[2]), validateColorRange(colours[1]), validateColorRange(colours[0]));
    }
}
