package sun.font;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.TextAttribute;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: rt.jar:sun/font/Underline.class */
abstract class Underline {
    private static final float DEFAULT_THICKNESS = 1.0f;
    private static final boolean USE_THICKNESS = true;
    private static final boolean IGNORE_THICKNESS = false;
    private static final ConcurrentHashMap<Object, Underline> UNDERLINES = new ConcurrentHashMap<>(6);
    private static final Underline[] UNDERLINE_LIST;

    abstract void drawUnderline(Graphics2D graphics2D, float f2, float f3, float f4, float f5);

    abstract float getLowerDrawLimit(float f2);

    abstract Shape getUnderlineShape(float f2, float f3, float f4, float f5);

    Underline() {
    }

    /* loaded from: rt.jar:sun/font/Underline$StandardUnderline.class */
    private static final class StandardUnderline extends Underline {
        private float shift;
        private float thicknessMultiplier;
        private float[] dashPattern;
        private boolean useThickness;
        private BasicStroke cachedStroke = null;

        StandardUnderline(float f2, float f3, float[] fArr, boolean z2) {
            this.shift = f2;
            this.thicknessMultiplier = f3;
            this.dashPattern = fArr;
            this.useThickness = z2;
        }

        private BasicStroke createStroke(float f2) {
            if (this.dashPattern == null) {
                return new BasicStroke(f2, 0, 0);
            }
            return new BasicStroke(f2, 0, 0, 10.0f, this.dashPattern, 0.0f);
        }

        private float getLineThickness(float f2) {
            if (this.useThickness) {
                return f2 * this.thicknessMultiplier;
            }
            return 1.0f * this.thicknessMultiplier;
        }

        private Stroke getStroke(float f2) {
            float lineThickness = getLineThickness(f2);
            BasicStroke basicStrokeCreateStroke = this.cachedStroke;
            if (basicStrokeCreateStroke == null || basicStrokeCreateStroke.getLineWidth() != lineThickness) {
                basicStrokeCreateStroke = createStroke(lineThickness);
                this.cachedStroke = basicStrokeCreateStroke;
            }
            return basicStrokeCreateStroke;
        }

        @Override // sun.font.Underline
        void drawUnderline(Graphics2D graphics2D, float f2, float f3, float f4, float f5) {
            Stroke stroke = graphics2D.getStroke();
            graphics2D.setStroke(getStroke(f2));
            graphics2D.draw(new Line2D.Float(f3, f5 + this.shift, f4, f5 + this.shift));
            graphics2D.setStroke(stroke);
        }

        @Override // sun.font.Underline
        float getLowerDrawLimit(float f2) {
            return this.shift + getLineThickness(f2);
        }

        @Override // sun.font.Underline
        Shape getUnderlineShape(float f2, float f3, float f4, float f5) {
            return getStroke(f2).createStrokedShape(new Line2D.Float(f3, f5 + this.shift, f4, f5 + this.shift));
        }
    }

    /* loaded from: rt.jar:sun/font/Underline$IMGrayUnderline.class */
    private static class IMGrayUnderline extends Underline {
        private BasicStroke stroke = new BasicStroke(1.0f, 0, 0, 10.0f, new float[]{1.0f, 1.0f}, 0.0f);

        IMGrayUnderline() {
        }

        @Override // sun.font.Underline
        void drawUnderline(Graphics2D graphics2D, float f2, float f3, float f4, float f5) {
            Stroke stroke = graphics2D.getStroke();
            graphics2D.setStroke(this.stroke);
            Line2D.Float r0 = new Line2D.Float(f3, f5, f4, f5);
            graphics2D.draw(r0);
            r0.y1 += 1.0f;
            r0.y2 += 1.0f;
            r0.x1 += 1.0f;
            graphics2D.draw(r0);
            graphics2D.setStroke(stroke);
        }

        @Override // sun.font.Underline
        float getLowerDrawLimit(float f2) {
            return 2.0f;
        }

        @Override // sun.font.Underline
        Shape getUnderlineShape(float f2, float f3, float f4, float f5) {
            GeneralPath generalPath = new GeneralPath();
            Line2D.Float r0 = new Line2D.Float(f3, f5, f4, f5);
            generalPath.append(this.stroke.createStrokedShape(r0), false);
            r0.y1 += 1.0f;
            r0.y2 += 1.0f;
            r0.x1 += 1.0f;
            generalPath.append(this.stroke.createStrokedShape(r0), false);
            return generalPath;
        }
    }

    static {
        UNDERLINES.put(TextAttribute.UNDERLINE_ON, underlineArr[0]);
        UNDERLINES.put(TextAttribute.UNDERLINE_LOW_ONE_PIXEL, underlineArr[1]);
        UNDERLINES.put(TextAttribute.UNDERLINE_LOW_TWO_PIXEL, underlineArr[2]);
        UNDERLINES.put(TextAttribute.UNDERLINE_LOW_DOTTED, underlineArr[3]);
        UNDERLINES.put(TextAttribute.UNDERLINE_LOW_GRAY, underlineArr[4]);
        Underline[] underlineArr = {new StandardUnderline(0.0f, 1.0f, null, true), new StandardUnderline(1.0f, 1.0f, null, false), new StandardUnderline(1.0f, 2.0f, null, false), new StandardUnderline(1.0f, 1.0f, new float[]{1.0f, 1.0f}, false), new IMGrayUnderline(), new StandardUnderline(1.0f, 1.0f, new float[]{4.0f, 4.0f}, false)};
        UNDERLINES.put(TextAttribute.UNDERLINE_LOW_DASHED, underlineArr[5]);
        UNDERLINE_LIST = underlineArr;
    }

    static Underline getUnderline(Object obj) {
        if (obj == null) {
            return null;
        }
        return UNDERLINES.get(obj);
    }

    static Underline getUnderline(int i2) {
        if (i2 < 0) {
            return null;
        }
        return UNDERLINE_LIST[i2];
    }
}
