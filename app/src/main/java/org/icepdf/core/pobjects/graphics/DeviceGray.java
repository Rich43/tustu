package org.icepdf.core.pobjects.graphics;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.util.HashMap;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/DeviceGray.class */
public class DeviceGray extends PColorSpace {
    public static final Name DEVICEGRAY_KEY = new Name("DeviceGray");
    public static final Name G_KEY = new Name("G");
    private static final ColorSpace RGB_COLOR_SPACE = ColorSpace.getInstance(1000);
    private static HashMap<Float, Color> colorHashMap = new HashMap<>(255);

    public DeviceGray(Library l2, HashMap h2) {
        super(l2, h2);
    }

    @Override // org.icepdf.core.pobjects.graphics.PColorSpace
    public int getNumComponents() {
        return 1;
    }

    @Override // org.icepdf.core.pobjects.graphics.PColorSpace
    public Color getColor(float[] f2, boolean fillAndStroke) {
        float gray = ((double) f2[0]) > 1.0d ? f2[0] / 255.0f : f2[0];
        Color color = colorHashMap.get(Float.valueOf(f2[0]));
        if (color != null) {
            return color;
        }
        Color color2 = new Color(RGB_COLOR_SPACE, new Color(gray, gray, gray).getRGBComponents(null), 1.0f);
        colorHashMap.put(Float.valueOf(f2[0]), color2);
        return color2;
    }
}
