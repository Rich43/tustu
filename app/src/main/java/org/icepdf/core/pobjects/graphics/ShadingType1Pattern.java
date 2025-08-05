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

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/ShadingType1Pattern.class */
public class ShadingType1Pattern extends ShadingType2Pattern {
    private static final Logger logger = Logger.getLogger(ShadingType1Pattern.class.toString());
    private LinearGradientPaint linearGradientPaint;

    public ShadingType1Pattern(Library library, HashMap entries) {
        super(library, entries);
    }

    @Override // org.icepdf.core.pobjects.graphics.ShadingType2Pattern, org.icepdf.core.pobjects.graphics.ShadingPattern, org.icepdf.core.pobjects.Dictionary
    public synchronized void init() {
        if (this.inited) {
            return;
        }
        this.inited = true;
        if (this.shading == null) {
            this.shading = this.library.getDictionary(this.entries, SHADING_KEY);
        }
        this.colorSpace = PColorSpace.getColorSpace(this.library, this.library.getObject(this.shading, COLORSPACE_KEY));
        Object tmp = this.library.getObject(this.shading, DOMAIN_KEY);
        if (tmp instanceof List) {
            this.domain = (List) tmp;
        } else {
            this.domain = new ArrayList(2);
            this.domain.add(new Float(0.0d));
            this.domain.add(new Float(1.0d));
            this.domain.add(new Float(0.0d));
            this.domain.add(new Float(1.0d));
        }
        Object tmp2 = this.library.getObject(this.shading, FUNCTION_KEY);
        if (tmp2 != null) {
            if (!(tmp2 instanceof List)) {
                this.function = new Function[]{Function.getFunction(this.library, tmp2)};
            } else {
                List functionTemp = (List) tmp2;
                this.function = new Function[functionTemp.size()];
                for (int i2 = 0; i2 < functionTemp.size(); i2++) {
                    this.function[i2] = Function.getFunction(this.library, functionTemp.get(i2));
                }
            }
        }
        Point2D.Float startPoint = new Point2D.Float(this.domain.get(0).floatValue(), this.domain.get(2).floatValue());
        Point2D.Float endPoint = new Point2D.Float(this.domain.get(0).floatValue(), this.domain.get(3).floatValue());
        float t0 = this.domain.get(0).floatValue();
        float t1 = this.domain.get(3).floatValue();
        Color[] colors = calculateColorPoints(10, startPoint, endPoint, t0, t1);
        float[] dist = calculateDomainEntries(10, t0, t1);
        this.linearGradientPaint = new LinearGradientPaint(startPoint, endPoint, dist, colors, MultipleGradientPaint.NO_CYCLE, MultipleGradientPaint.LINEAR_RGB, this.matrix);
    }

    @Override // org.icepdf.core.pobjects.graphics.ShadingType2Pattern, org.icepdf.core.pobjects.graphics.ShadingPattern, org.icepdf.core.pobjects.graphics.Pattern
    public Paint getPaint() {
        return null;
    }

    public String toSting() {
        return super.toString() + "\n                    domain: " + ((Object) this.domain) + "\n                    matrix: " + ((Object) this.matrix) + "\n                 function: " + ((Object) this.function);
    }
}
