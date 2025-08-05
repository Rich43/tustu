package org.icepdf.core.pobjects.graphics;

import java.awt.Color;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.functions.Function;
import org.icepdf.core.util.ColorUtil;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/Separation.class */
public class Separation extends PColorSpace {
    public static final Name SEPARATION_KEY = new Name("Separation");
    protected Color namedColor;
    protected PColorSpace alternate;
    protected Function tintTransform;
    private boolean isAll;
    public static final String COLORANT_ALL = "all";
    private boolean isNone;
    public static final String COLORANT_NONE = "none";
    private float tint;
    private ConcurrentHashMap<Integer, Color> colorTable1B;
    private ConcurrentHashMap<Integer, Color> colorTable3B;
    private ConcurrentHashMap<Integer, Color> colorTable4B;

    protected Separation(Library l2, HashMap h2, Object name, Object alternateSpace, Object tintTransform) {
        super(l2, h2);
        this.tint = 1.0f;
        this.alternate = getColorSpace(l2, alternateSpace);
        this.colorTable1B = new ConcurrentHashMap<>(256);
        this.colorTable3B = new ConcurrentHashMap<>(256);
        this.colorTable4B = new ConcurrentHashMap<>(256);
        this.tintTransform = Function.getFunction(l2, l2.getObject(tintTransform));
        if (name instanceof Name) {
            String colorName = ((Name) name).getName().toLowerCase();
            if (!colorName.equals("red") && !colorName.equals("blue") && !colorName.equals("blue") && !colorName.equals("black") && !colorName.equals("auto")) {
                if (colorName.equals("all")) {
                    this.isAll = true;
                    return;
                } else {
                    if (colorName.equals(COLORANT_NONE)) {
                        this.isNone = true;
                        return;
                    }
                    return;
                }
            }
            int colorVaue = ColorUtil.convertNamedColor(colorName.toLowerCase());
            if (colorVaue != -1) {
                this.namedColor = new Color(colorVaue);
            }
            if (colorName.equalsIgnoreCase("auto")) {
                this.namedColor = Color.BLACK;
            }
        }
    }

    @Override // org.icepdf.core.pobjects.graphics.PColorSpace
    public int getNumComponents() {
        return 1;
    }

    public boolean isNamedColor() {
        return this.namedColor != null;
    }

    @Override // org.icepdf.core.pobjects.graphics.PColorSpace
    public Color getColor(float[] components, boolean fillAndStroke) {
        if (this.namedColor != null) {
            this.tint = components[0];
            float[] colour = this.namedColor.getComponents(null);
            this.namedColor = new Color(colour[0], colour[1], colour[2], this.tint);
            if (this.tint < 0.1f && colour[0] == 0.0f && colour[1] == 0.0f && colour[2] == 0.0f) {
                return Color.WHITE;
            }
            return this.namedColor;
        }
        if (this.tintTransform == null) {
            float colour2 = components[0];
            float[] alternateColour = new float[this.alternate.getNumComponents()];
            int max = this.alternate.getNumComponents();
            for (int i2 = 0; i2 < max; i2++) {
                alternateColour[i2] = colour2;
            }
            return this.alternate.getColor(alternateColour);
        }
        if (this.alternate != null && !this.isNone) {
            int key = 0;
            int bands = components.length;
            int i3 = 0;
            int bit = 0;
            while (i3 < bands) {
                key |= (((int) (components[i3] * 255.0f)) & 255) << bit;
                i3++;
                bit += 8;
            }
            if (bands == 1) {
                return addColorToCache(this.colorTable1B, key, this.alternate, this.tintTransform, components);
            }
            if (bands == 3) {
                return addColorToCache(this.colorTable3B, key, this.alternate, this.tintTransform, components);
            }
            if (bands == 4) {
                return addColorToCache(this.colorTable4B, key, this.alternate, this.tintTransform, components);
            }
        }
        if (this.isNone) {
            return new Color(0, 0, 0, 0);
        }
        return this.namedColor;
    }

    private static Color addColorToCache(ConcurrentHashMap<Integer, Color> colorCache, int key, PColorSpace alternate, Function tintTransform, float[] f2) {
        Color color = colorCache.get(Integer.valueOf(key));
        if (color == null) {
            float[] y2 = tintTransform.calculate(reverse(f2));
            Color color2 = alternate.getColor(reverse(y2));
            colorCache.put(Integer.valueOf(key), color2);
            return color2;
        }
        return color;
    }

    public float getTint() {
        return this.tint;
    }
}
