package javax.swing.text.html;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.HashMap;
import javax.swing.border.AbstractBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.html.CSS;

/* loaded from: rt.jar:javax/swing/text/html/CSSBorder.class */
class CSSBorder extends AbstractBorder {
    static final int COLOR = 0;
    static final int STYLE = 1;
    static final int WIDTH = 2;
    static final int TOP = 0;
    static final int RIGHT = 1;
    static final int BOTTOM = 2;
    static final int LEFT = 3;
    final AttributeSet attrs;
    static final CSS.Attribute[][] ATTRIBUTES = {new CSS.Attribute[]{CSS.Attribute.BORDER_TOP_COLOR, CSS.Attribute.BORDER_RIGHT_COLOR, CSS.Attribute.BORDER_BOTTOM_COLOR, CSS.Attribute.BORDER_LEFT_COLOR}, new CSS.Attribute[]{CSS.Attribute.BORDER_TOP_STYLE, CSS.Attribute.BORDER_RIGHT_STYLE, CSS.Attribute.BORDER_BOTTOM_STYLE, CSS.Attribute.BORDER_LEFT_STYLE}, new CSS.Attribute[]{CSS.Attribute.BORDER_TOP_WIDTH, CSS.Attribute.BORDER_RIGHT_WIDTH, CSS.Attribute.BORDER_BOTTOM_WIDTH, CSS.Attribute.BORDER_LEFT_WIDTH}};
    static final CSS.CssValue[] PARSERS = {new CSS.ColorValue(), new CSS.BorderStyle(), new CSS.BorderWidthValue(null, 0)};
    static final Object[] DEFAULTS = {CSS.Attribute.BORDER_COLOR, PARSERS[1].parseCssValue(CSS.Attribute.BORDER_STYLE.getDefaultValue()), PARSERS[2].parseCssValue(CSS.Attribute.BORDER_WIDTH.getDefaultValue())};
    static java.util.Map<CSS.Value, BorderPainter> borderPainters = new HashMap();

    /* loaded from: rt.jar:javax/swing/text/html/CSSBorder$BorderPainter.class */
    interface BorderPainter {
        void paint(Polygon polygon, Graphics graphics, Color color, int i2);
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [javax.swing.text.html.CSS$Attribute[], javax.swing.text.html.CSS$Attribute[][]] */
    static {
        registerBorderPainter(CSS.Value.NONE, new NullPainter());
        registerBorderPainter(CSS.Value.HIDDEN, new NullPainter());
        registerBorderPainter(CSS.Value.SOLID, new SolidPainter());
        registerBorderPainter(CSS.Value.DOUBLE, new DoublePainter());
        registerBorderPainter(CSS.Value.DOTTED, new DottedDashedPainter(1));
        registerBorderPainter(CSS.Value.DASHED, new DottedDashedPainter(3));
        registerBorderPainter(CSS.Value.GROOVE, new GrooveRidgePainter(CSS.Value.GROOVE));
        registerBorderPainter(CSS.Value.RIDGE, new GrooveRidgePainter(CSS.Value.RIDGE));
        registerBorderPainter(CSS.Value.INSET, new InsetOutsetPainter(CSS.Value.INSET));
        registerBorderPainter(CSS.Value.OUTSET, new InsetOutsetPainter(CSS.Value.OUTSET));
    }

    CSSBorder(AttributeSet attributeSet) {
        this.attrs = attributeSet;
    }

    private Color getBorderColor(int i2) {
        CSS.ColorValue colorValue;
        Object attribute = this.attrs.getAttribute(ATTRIBUTES[0][i2]);
        if (attribute instanceof CSS.ColorValue) {
            colorValue = (CSS.ColorValue) attribute;
        } else {
            colorValue = (CSS.ColorValue) this.attrs.getAttribute(CSS.Attribute.COLOR);
            if (colorValue == null) {
                colorValue = (CSS.ColorValue) PARSERS[0].parseCssValue(CSS.Attribute.COLOR.getDefaultValue());
            }
        }
        return colorValue.getValue();
    }

    private int getBorderWidth(int i2) {
        int value = 0;
        CSS.BorderStyle borderStyle = (CSS.BorderStyle) this.attrs.getAttribute(ATTRIBUTES[1][i2]);
        if (borderStyle != null && borderStyle.getValue() != CSS.Value.NONE) {
            CSS.LengthValue lengthValue = (CSS.LengthValue) this.attrs.getAttribute(ATTRIBUTES[2][i2]);
            if (lengthValue == null) {
                lengthValue = (CSS.LengthValue) DEFAULTS[2];
            }
            value = (int) lengthValue.getValue(true);
        }
        return value;
    }

    private int[] getWidths() {
        int[] iArr = new int[4];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            iArr[i2] = getBorderWidth(i2);
        }
        return iArr;
    }

    private CSS.Value getBorderStyle(int i2) {
        CSS.BorderStyle borderStyle = (CSS.BorderStyle) this.attrs.getAttribute(ATTRIBUTES[1][i2]);
        if (borderStyle == null) {
            borderStyle = (CSS.BorderStyle) DEFAULTS[1];
        }
        return borderStyle.getValue();
    }

    private Polygon getBorderShape(int i2) {
        Polygon polygon = null;
        int[] widths = getWidths();
        if (widths[i2] != 0) {
            polygon = new Polygon(new int[4], new int[4], 0);
            polygon.addPoint(0, 0);
            polygon.addPoint(-widths[(i2 + 3) % 4], -widths[i2]);
            polygon.addPoint(widths[(i2 + 1) % 4], -widths[i2]);
            polygon.addPoint(0, 0);
        }
        return polygon;
    }

    private BorderPainter getBorderPainter(int i2) {
        return borderPainters.get(getBorderStyle(i2));
    }

    static Color getAdjustedColor(Color color, double d2) {
        double dMin = 1.0d - Math.min(Math.abs(d2), 1.0d);
        double d3 = d2 > 0.0d ? 255.0d * (1.0d - dMin) : 0.0d;
        return new Color((int) ((color.getRed() * dMin) + d3), (int) ((color.getGreen() * dMin) + d3), (int) ((color.getBlue() * dMin) + d3));
    }

    @Override // javax.swing.border.AbstractBorder
    public Insets getBorderInsets(Component component, Insets insets) {
        int[] widths = getWidths();
        insets.set(widths[0], widths[3], widths[2], widths[1]);
        return insets;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        if (!(graphics instanceof Graphics2D)) {
            return;
        }
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        int[] widths = getWidths();
        int i6 = i2 + widths[3];
        int i7 = i3 + widths[0];
        int i8 = i4 - (widths[1] + widths[3]);
        int i9 = i5 - (widths[0] + widths[2]);
        int[] iArr = {new int[]{i6, i7}, new int[]{i6 + i8, i7}, new int[]{i6 + i8, i7 + i9}, new int[]{i6, i7 + i9}};
        for (int i10 = 0; i10 < 4; i10++) {
            CSS.Value borderStyle = getBorderStyle(i10);
            Polygon borderShape = getBorderShape(i10);
            if (borderStyle != CSS.Value.NONE && borderShape != null) {
                int i11 = i10 % 2 == 0 ? i8 : i9;
                int[] iArr2 = borderShape.xpoints;
                iArr2[2] = iArr2[2] + i11;
                int[] iArr3 = borderShape.xpoints;
                iArr3[3] = iArr3[3] + i11;
                Color borderColor = getBorderColor(i10);
                BorderPainter borderPainter = getBorderPainter(i10);
                double d2 = (i10 * 3.141592653589793d) / 2.0d;
                graphics2D.setClip(graphics.getClip());
                graphics2D.translate((int) iArr[i10][0], (int) iArr[i10][1]);
                graphics2D.rotate(d2);
                graphics2D.clip(borderShape);
                borderPainter.paint(borderShape, graphics2D, borderColor, i10);
                graphics2D.rotate(-d2);
                graphics2D.translate(-iArr[i10][0], -iArr[i10][1]);
            }
        }
        graphics2D.dispose();
    }

    /* loaded from: rt.jar:javax/swing/text/html/CSSBorder$NullPainter.class */
    static class NullPainter implements BorderPainter {
        NullPainter() {
        }

        @Override // javax.swing.text.html.CSSBorder.BorderPainter
        public void paint(Polygon polygon, Graphics graphics, Color color, int i2) {
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/CSSBorder$SolidPainter.class */
    static class SolidPainter implements BorderPainter {
        SolidPainter() {
        }

        @Override // javax.swing.text.html.CSSBorder.BorderPainter
        public void paint(Polygon polygon, Graphics graphics, Color color, int i2) {
            graphics.setColor(color);
            graphics.fillPolygon(polygon);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/CSSBorder$StrokePainter.class */
    static abstract class StrokePainter implements BorderPainter {
        StrokePainter() {
        }

        void paintStrokes(Rectangle rectangle, Graphics graphics, int i2, int[] iArr, Color[] colorArr) {
            boolean z2 = i2 == 0;
            int i3 = 0;
            int i4 = z2 ? rectangle.width : rectangle.height;
            while (i3 < i4) {
                for (int i5 = 0; i5 < iArr.length && i3 < i4; i5++) {
                    int i6 = iArr[i5];
                    Color color = colorArr[i5];
                    if (color != null) {
                        int i7 = rectangle.f12372x + (z2 ? i3 : 0);
                        int i8 = rectangle.f12373y + (z2 ? 0 : i3);
                        int i9 = z2 ? i6 : rectangle.width;
                        int i10 = z2 ? rectangle.height : i6;
                        graphics.setColor(color);
                        graphics.fillRect(i7, i8, i9, i10);
                    }
                    i3 += i6;
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/CSSBorder$DoublePainter.class */
    static class DoublePainter extends StrokePainter {
        DoublePainter() {
        }

        @Override // javax.swing.text.html.CSSBorder.BorderPainter
        public void paint(Polygon polygon, Graphics graphics, Color color, int i2) {
            Rectangle bounds = polygon.getBounds();
            int iMax = Math.max(bounds.height / 3, 1);
            paintStrokes(bounds, graphics, 1, new int[]{iMax, iMax}, new Color[]{color, null});
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/CSSBorder$DottedDashedPainter.class */
    static class DottedDashedPainter extends StrokePainter {
        final int factor;

        DottedDashedPainter(int i2) {
            this.factor = i2;
        }

        @Override // javax.swing.text.html.CSSBorder.BorderPainter
        public void paint(Polygon polygon, Graphics graphics, Color color, int i2) {
            Rectangle bounds = polygon.getBounds();
            int i3 = bounds.height * this.factor;
            paintStrokes(bounds, graphics, 0, new int[]{i3, i3}, new Color[]{color, null});
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/CSSBorder$ShadowLightPainter.class */
    static abstract class ShadowLightPainter extends StrokePainter {
        ShadowLightPainter() {
        }

        static Color getShadowColor(Color color) {
            return CSSBorder.getAdjustedColor(color, -0.3d);
        }

        static Color getLightColor(Color color) {
            return CSSBorder.getAdjustedColor(color, 0.7d);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/CSSBorder$GrooveRidgePainter.class */
    static class GrooveRidgePainter extends ShadowLightPainter {
        final CSS.Value type;

        GrooveRidgePainter(CSS.Value value) {
            this.type = value;
        }

        @Override // javax.swing.text.html.CSSBorder.BorderPainter
        public void paint(Polygon polygon, Graphics graphics, Color color, int i2) {
            Rectangle bounds = polygon.getBounds();
            int iMax = Math.max(bounds.height / 2, 1);
            paintStrokes(bounds, graphics, 1, new int[]{iMax, iMax}, ((i2 + 1) % 4 < 2) == (this.type == CSS.Value.GROOVE) ? new Color[]{getShadowColor(color), getLightColor(color)} : new Color[]{getLightColor(color), getShadowColor(color)});
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/CSSBorder$InsetOutsetPainter.class */
    static class InsetOutsetPainter extends ShadowLightPainter {
        CSS.Value type;

        InsetOutsetPainter(CSS.Value value) {
            this.type = value;
        }

        @Override // javax.swing.text.html.CSSBorder.BorderPainter
        public void paint(Polygon polygon, Graphics graphics, Color color, int i2) {
            graphics.setColor(((i2 + 1) % 4 < 2) == (this.type == CSS.Value.INSET) ? getShadowColor(color) : getLightColor(color));
            graphics.fillPolygon(polygon);
        }
    }

    static void registerBorderPainter(CSS.Value value, BorderPainter borderPainter) {
        borderPainters.put(value, borderPainter);
    }
}
