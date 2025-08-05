package org.icepdf.core.pobjects.graphics;

import java.awt.Color;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.functions.Function;
import org.icepdf.core.pobjects.graphics.batik.ext.awt.MultipleGradientPaint;
import org.icepdf.core.pobjects.graphics.batik.ext.awt.RadialGradientPaint;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/ShadingType3Pattern.class */
public class ShadingType3Pattern extends ShadingPattern {
    private static final Logger logger = Logger.getLogger(ShadingType3Pattern.class.toString());
    protected Function[] function;
    protected List<Number> domain;
    protected List coords;
    protected List<Boolean> extend;
    protected RadialGradientPaint radialGradientPaint;

    public ShadingType3Pattern(Library library, HashMap entries) {
        super(library, entries);
    }

    @Override // org.icepdf.core.pobjects.graphics.ShadingPattern, org.icepdf.core.pobjects.Dictionary
    public synchronized void init() {
        if (this.inited) {
            return;
        }
        if (this.shading == null) {
            this.shading = this.library.getDictionary(this.entries, SHADING_KEY);
        }
        this.shadingType = this.library.getInt(this.shading, SHADING_TYPE_KEY);
        this.bBox = this.library.getRectangle(this.shading, BBOX_KEY);
        this.colorSpace = PColorSpace.getColorSpace(this.library, this.library.getObject(this.shading, COLORSPACE_KEY));
        if (this.library.getObject(this.shading, BACKGROUND_KEY) != null && (this.library.getObject(this.shading, BACKGROUND_KEY) instanceof List)) {
            this.background = (List) this.library.getObject(this.shading, BACKGROUND_KEY);
        }
        this.antiAlias = this.library.getBoolean(this.shading, ANTIALIAS_KEY).booleanValue();
        Object tmp = this.library.getObject(this.shading, DOMAIN_KEY);
        if (tmp instanceof List) {
            this.domain = (List) tmp;
        } else {
            this.domain = new ArrayList(2);
            this.domain.add(new Float(0.0d));
            this.domain.add(new Float(1.0d));
        }
        Object tmp2 = this.library.getObject(this.shading, COORDS_KEY);
        if (tmp2 instanceof List) {
            this.coords = (List) tmp2;
        }
        Object tmp3 = this.library.getObject(this.shading, EXTEND_KEY);
        if (tmp3 instanceof List) {
            this.extend = (List) tmp3;
        } else {
            this.extend = new ArrayList(2);
            this.extend.add(false);
            this.extend.add(false);
        }
        Object tmp4 = this.library.getObject(this.shading, FUNCTION_KEY);
        if (tmp4 != null) {
            if (!(tmp4 instanceof List)) {
                this.function = new Function[]{Function.getFunction(this.library, tmp4)};
            } else {
                List functionTemp = (List) tmp4;
                this.function = new Function[functionTemp.size()];
                for (int i2 = 0; i2 < functionTemp.size(); i2++) {
                    this.function[i2] = Function.getFunction(this.library, functionTemp.get(i2));
                }
            }
        }
        float t0 = this.domain.get(0).floatValue();
        float t1 = this.domain.get(1).floatValue();
        float[] s2 = {0.0f, 0.25f, 0.5f, 0.75f, 1.0f};
        Point2D.Float center = new Point2D.Float(((Number) this.coords.get(0)).floatValue(), ((Number) this.coords.get(1)).floatValue());
        Point2D.Float focus = new Point2D.Float(((Number) this.coords.get(3)).floatValue(), ((Number) this.coords.get(4)).floatValue());
        float radius = ((Number) this.coords.get(2)).floatValue();
        float radius2 = ((Number) this.coords.get(5)).floatValue();
        if (radius2 > radius) {
            radius = radius2;
        }
        Color color1 = calculateColour(this.colorSpace, s2[0], t0, t1);
        Color color2 = calculateColour(this.colorSpace, s2[1], t0, t1);
        Color color3 = calculateColour(this.colorSpace, s2[2], t0, t1);
        Color color4 = calculateColour(this.colorSpace, s2[3], t0, t1);
        Color color5 = calculateColour(this.colorSpace, s2[4], t0, t1);
        if (color1 == null || color2 == null) {
            return;
        }
        Color[] colors = {color1, color2, color3, color4, color5};
        this.radialGradientPaint = new RadialGradientPaint(center, radius, focus, s2, colors, MultipleGradientPaint.NO_CYCLE, MultipleGradientPaint.LINEAR_RGB, this.matrix);
        this.inited = true;
    }

    private Color calculateColour(PColorSpace colorSpace, float s2, float t0, float t1) {
        float[] output;
        float t2 = parametrixValue(s2, t0, t1, this.extend);
        float[] input = {t2};
        if (this.function != null) {
            int length = this.function.length;
            if (length == 1) {
                output = this.function[0].calculate(input);
            } else {
                output = new float[length];
                for (int i2 = 0; i2 < length; i2++) {
                    output[i2] = this.function[i2].calculate(input)[0];
                }
            }
            if (output != null) {
                if (!(colorSpace instanceof DeviceN)) {
                    output = PColorSpace.reverse(output);
                }
                return colorSpace.getColor(output);
            }
            return null;
        }
        logger.fine("Error processing Shading Type 3 Pattern.");
        return null;
    }

    private float parametrixValue(float linearMapping, float t0, float t1, List extended) {
        return t0 + ((t1 - t0) * linearMapping);
    }

    @Override // org.icepdf.core.pobjects.graphics.ShadingPattern, org.icepdf.core.pobjects.graphics.Pattern
    public Paint getPaint() {
        init();
        return this.radialGradientPaint;
    }

    public String toSting() {
        return super.toString() + "\n                    domain: " + ((Object) this.domain) + "\n                    coords: " + ((Object) this.coords) + "\n                    extend: " + ((Object) this.extend) + "\n                 function: " + ((Object) this.function);
    }
}
