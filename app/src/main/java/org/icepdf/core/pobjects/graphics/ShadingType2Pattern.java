package org.icepdf.core.pobjects.graphics;

import java.awt.Color;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.functions.Function;
import org.icepdf.core.pobjects.graphics.batik.ext.awt.LinearGradientPaint;
import org.icepdf.core.pobjects.graphics.batik.ext.awt.MultipleGradientPaint;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/ShadingType2Pattern.class */
public class ShadingType2Pattern extends ShadingPattern {
    private static final Logger logger = Logger.getLogger(ShadingType2Pattern.class.toString());
    protected Function[] function;
    protected List<Number> domain;
    protected List coords;
    protected List<Boolean> extend;
    private LinearGradientPaint linearGradientPaint;

    public ShadingType2Pattern(Library library, HashMap entries) {
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
        Object tmp = this.library.getObject(this.shading, BACKGROUND_KEY);
        if (tmp != null && (tmp instanceof List)) {
            this.background = (List) tmp;
        }
        this.antiAlias = this.library.getBoolean(this.shading, ANTIALIAS_KEY).booleanValue();
        Object tmp2 = this.library.getObject(this.shading, DOMAIN_KEY);
        if (tmp2 instanceof List) {
            this.domain = (List) tmp2;
        } else {
            this.domain = new ArrayList(2);
            this.domain.add(new Float(0.0d));
            this.domain.add(new Float(1.0d));
        }
        Object tmp3 = this.library.getObject(this.shading, COORDS_KEY);
        if (tmp3 instanceof List) {
            this.coords = (List) tmp3;
        }
        Object tmp4 = this.library.getObject(this.shading, EXTEND_KEY);
        if (tmp4 instanceof List) {
            this.extend = (List) tmp4;
        } else {
            this.extend = new ArrayList(2);
            this.extend.add(false);
            this.extend.add(false);
        }
        Object tmp5 = this.library.getObject(this.shading, FUNCTION_KEY);
        if (tmp5 != null) {
            if (!(tmp5 instanceof List)) {
                this.function = new Function[]{Function.getFunction(this.library, tmp5)};
            } else {
                List functionTemp = (List) tmp5;
                this.function = new Function[functionTemp.size()];
                for (int i2 = 0; i2 < functionTemp.size(); i2++) {
                    this.function[i2] = Function.getFunction(this.library, functionTemp.get(i2));
                }
            }
        }
        float t0 = this.domain.get(0).floatValue();
        float t1 = this.domain.get(1).floatValue();
        Point2D.Float startPoint = new Point2D.Float(((Number) this.coords.get(0)).floatValue(), ((Number) this.coords.get(1)).floatValue());
        Point2D.Float endPoint = new Point2D.Float(((Number) this.coords.get(2)).floatValue(), ((Number) this.coords.get(3)).floatValue());
        if (startPoint.equals(endPoint)) {
            endPoint.f12396x += 1.0f;
        }
        Color[] colors = calculateColorPoints(10, startPoint, endPoint, t0, t1);
        float[] dist = calculateDomainEntries(10, t0, t1);
        this.linearGradientPaint = new LinearGradientPaint(startPoint, endPoint, dist, colors, MultipleGradientPaint.NO_CYCLE, MultipleGradientPaint.LINEAR_RGB, this.matrix);
        this.inited = true;
    }

    protected Color[] calculateColorPoints(int numberOfPoints, Point2D.Float startPoint, Point2D.Float endPoint, float t0, float t1) {
        Color[] color;
        float m2 = (startPoint.f12397y - endPoint.f12397y) / (startPoint.f12396x - endPoint.f12396x);
        float b2 = startPoint.f12397y - (m2 * startPoint.f12396x);
        if (!Float.isInfinite(m2)) {
            float xDiff = (endPoint.f12396x - startPoint.f12396x) / numberOfPoints;
            float xOffset = startPoint.f12396x;
            color = new Color[numberOfPoints + 1];
            int max = color.length;
            for (int i2 = 0; i2 < max; i2++) {
                Point2D.Float point = new Point2D.Float(xOffset, (m2 * xOffset) + b2);
                color[i2] = calculateColour(this.colorSpace, point, startPoint, endPoint, t0, t1);
                xOffset += xDiff;
            }
        } else {
            float yDiff = (endPoint.f12397y - startPoint.f12397y) / numberOfPoints;
            float yOffset = startPoint.f12397y;
            color = new Color[numberOfPoints + 1];
            int max2 = color.length;
            for (int i3 = 0; i3 < max2; i3++) {
                Point2D.Float point2 = new Point2D.Float(0.0f, yOffset);
                color[i3] = calculateColour(this.colorSpace, point2, startPoint, endPoint, t0, t1);
                yOffset += yDiff;
            }
        }
        return color;
    }

    protected float[] calculateDomainEntries(int numberOfPoints, float t0, float t1) {
        float offset = 1.0f / numberOfPoints;
        float[] domainEntries = new float[numberOfPoints + 1];
        domainEntries[0] = t0;
        int max = domainEntries.length;
        for (int i2 = 1; i2 < max; i2++) {
            domainEntries[i2] = domainEntries[i2 - 1] + offset;
        }
        domainEntries[domainEntries.length - 1] = t1;
        return domainEntries;
    }

    private Color calculateColour(PColorSpace colorSpace, Point2D.Float xy, Point2D.Float point1, Point2D.Float point2, float t0, float t1) {
        float[] output;
        float xPrime = linearMapping(xy, point1, point2);
        float t2 = parametrixValue(xPrime, t0, t1, this.extend);
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
                return colorSpace.getColor(PColorSpace.reverse(output), true);
            }
            return null;
        }
        logger.fine("Error processing Shading Type 2 Pattern.");
        return null;
    }

    private float linearMapping(Point2D.Float xy, Point2D.Float point1, Point2D.Float point2) {
        float x2 = xy.f12396x;
        float y2 = xy.f12397y;
        float x0 = point1.f12396x;
        float y0 = point1.f12397y;
        float x1 = point2.f12396x;
        float y1 = point2.f12397y;
        float top = ((x1 - x0) * (x2 - x0)) + ((y1 - y0) * (y2 - y0));
        float bottom = ((x1 - x0) * (x1 - x0)) + ((y1 - y0) * (y1 - y0));
        int map = (int) ((top / bottom) * 100.0f);
        return map / 100.0f;
    }

    private float parametrixValue(float linearMapping, float t0, float t1, List extended) {
        if (linearMapping < 0.0f && ((Boolean) extended.get(0)).booleanValue()) {
            return t0;
        }
        if (linearMapping > 1.0f && ((Boolean) extended.get(1)).booleanValue()) {
            return t1;
        }
        return t0 + ((t1 - t0) * linearMapping);
    }

    @Override // org.icepdf.core.pobjects.graphics.ShadingPattern, org.icepdf.core.pobjects.graphics.Pattern
    public Paint getPaint() {
        init();
        return this.linearGradientPaint;
    }

    @Override // org.icepdf.core.pobjects.graphics.ShadingPattern, org.icepdf.core.pobjects.Dictionary
    public String toString() {
        return super.toString() + "\n                    domain: " + ((Object) this.domain) + "\n                    coords: " + ((Object) this.coords) + "\n                    extend: " + ((Object) this.extend) + "\n                 function: " + ((Object) this.function);
    }
}
