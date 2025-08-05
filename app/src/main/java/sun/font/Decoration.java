package sun.font;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;

/* loaded from: rt.jar:sun/font/Decoration.class */
public class Decoration {
    private static final int VALUES_MASK = AttributeValues.getMask(EAttribute.EFOREGROUND, EAttribute.EBACKGROUND, EAttribute.ESWAP_COLORS, EAttribute.ESTRIKETHROUGH, EAttribute.EUNDERLINE, EAttribute.EINPUT_METHOD_HIGHLIGHT, EAttribute.EINPUT_METHOD_UNDERLINE);
    private static final Decoration PLAIN = new Decoration();

    /* loaded from: rt.jar:sun/font/Decoration$Label.class */
    public interface Label {
        CoreMetrics getCoreMetrics();

        Rectangle2D getLogicalBounds();

        void handleDraw(Graphics2D graphics2D, float f2, float f3);

        Rectangle2D handleGetCharVisualBounds(int i2);

        Rectangle2D handleGetVisualBounds();

        Shape handleGetOutline(float f2, float f3);
    }

    private Decoration() {
    }

    public static Decoration getPlainDecoration() {
        return PLAIN;
    }

    public static Decoration getDecoration(AttributeValues attributeValues) {
        if (attributeValues == null || !attributeValues.anyDefined(VALUES_MASK)) {
            return PLAIN;
        }
        AttributeValues attributeValuesApplyIMHighlight = attributeValues.applyIMHighlight();
        return new DecorationImpl(attributeValuesApplyIMHighlight.getForeground(), attributeValuesApplyIMHighlight.getBackground(), attributeValuesApplyIMHighlight.getSwapColors(), attributeValuesApplyIMHighlight.getStrikethrough(), Underline.getUnderline(attributeValuesApplyIMHighlight.getUnderline()), Underline.getUnderline(attributeValuesApplyIMHighlight.getInputMethodUnderline()));
    }

    public static Decoration getDecoration(Map map) {
        if (map == null) {
            return PLAIN;
        }
        return getDecoration(AttributeValues.fromMap(map));
    }

    public void drawTextAndDecorations(Label label, Graphics2D graphics2D, float f2, float f3) {
        label.handleDraw(graphics2D, f2, f3);
    }

    public Rectangle2D getVisualBounds(Label label) {
        return label.handleGetVisualBounds();
    }

    public Rectangle2D getCharVisualBounds(Label label, int i2) {
        return label.handleGetCharVisualBounds(i2);
    }

    Shape getOutline(Label label, float f2, float f3) {
        return label.handleGetOutline(f2, f3);
    }

    /* loaded from: rt.jar:sun/font/Decoration$DecorationImpl.class */
    private static final class DecorationImpl extends Decoration {
        private Paint fgPaint;
        private Paint bgPaint;
        private boolean swapColors;
        private boolean strikethrough;
        private Underline stdUnderline;
        private Underline imUnderline;

        DecorationImpl(Paint paint, Paint paint2, boolean z2, boolean z3, Underline underline, Underline underline2) {
            super();
            this.fgPaint = null;
            this.bgPaint = null;
            this.swapColors = false;
            this.strikethrough = false;
            this.stdUnderline = null;
            this.imUnderline = null;
            this.fgPaint = paint;
            this.bgPaint = paint2;
            this.swapColors = z2;
            this.strikethrough = z3;
            this.stdUnderline = underline;
            this.imUnderline = underline2;
        }

        private static boolean areEqual(Object obj, Object obj2) {
            if (obj == null) {
                return obj2 == null;
            }
            return obj.equals(obj2);
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            try {
                DecorationImpl decorationImpl = (DecorationImpl) obj;
                if (this.swapColors != decorationImpl.swapColors || this.strikethrough != decorationImpl.strikethrough || !areEqual(this.stdUnderline, decorationImpl.stdUnderline) || !areEqual(this.fgPaint, decorationImpl.fgPaint) || !areEqual(this.bgPaint, decorationImpl.bgPaint)) {
                    return false;
                }
                return areEqual(this.imUnderline, decorationImpl.imUnderline);
            } catch (ClassCastException e2) {
                return false;
            }
        }

        public int hashCode() {
            int iHashCode = 1;
            if (this.strikethrough) {
                iHashCode = 1 | 2;
            }
            if (this.swapColors) {
                iHashCode |= 4;
            }
            if (this.stdUnderline != null) {
                iHashCode += this.stdUnderline.hashCode();
            }
            return iHashCode;
        }

        private float getUnderlineMaxY(CoreMetrics coreMetrics) {
            float fMax = 0.0f;
            if (this.stdUnderline != null) {
                fMax = Math.max(0.0f, coreMetrics.underlineOffset + this.stdUnderline.getLowerDrawLimit(coreMetrics.underlineThickness));
            }
            if (this.imUnderline != null) {
                fMax = Math.max(fMax, coreMetrics.underlineOffset + this.imUnderline.getLowerDrawLimit(coreMetrics.underlineThickness));
            }
            return fMax;
        }

        private void drawTextAndEmbellishments(Label label, Graphics2D graphics2D, float f2, float f3) {
            label.handleDraw(graphics2D, f2, f3);
            if (!this.strikethrough && this.stdUnderline == null && this.imUnderline == null) {
                return;
            }
            float width = f2 + ((float) label.getLogicalBounds().getWidth());
            CoreMetrics coreMetrics = label.getCoreMetrics();
            if (this.strikethrough) {
                Stroke stroke = graphics2D.getStroke();
                graphics2D.setStroke(new BasicStroke(coreMetrics.strikethroughThickness, 0, 0));
                float f4 = f3 + coreMetrics.strikethroughOffset;
                graphics2D.draw(new Line2D.Float(f2, f4, width, f4));
                graphics2D.setStroke(stroke);
            }
            float f5 = coreMetrics.underlineOffset;
            float f6 = coreMetrics.underlineThickness;
            if (this.stdUnderline != null) {
                this.stdUnderline.drawUnderline(graphics2D, f6, f2, width, f3 + f5);
            }
            if (this.imUnderline != null) {
                this.imUnderline.drawUnderline(graphics2D, f6, f2, width, f3 + f5);
            }
        }

        @Override // sun.font.Decoration
        public void drawTextAndDecorations(Label label, Graphics2D graphics2D, float f2, float f3) {
            Paint paint;
            Paint paint2;
            if (this.fgPaint == null && this.bgPaint == null && !this.swapColors) {
                drawTextAndEmbellishments(label, graphics2D, f2, f3);
                return;
            }
            Paint paint3 = graphics2D.getPaint();
            if (this.swapColors) {
                paint2 = this.fgPaint == null ? paint3 : this.fgPaint;
                if (this.bgPaint == null) {
                    if (paint2 instanceof Color) {
                        Color color = (Color) paint2;
                        paint = ((33 * color.getRed()) + (53 * color.getGreen())) + (14 * color.getBlue()) > 18500 ? Color.BLACK : Color.WHITE;
                    } else {
                        paint = Color.WHITE;
                    }
                } else {
                    paint = this.bgPaint;
                }
            } else {
                paint = this.fgPaint == null ? paint3 : this.fgPaint;
                paint2 = this.bgPaint;
            }
            if (paint2 != null) {
                Rectangle2D logicalBounds = label.getLogicalBounds();
                Rectangle2D.Float r0 = new Rectangle2D.Float(f2 + ((float) logicalBounds.getX()), f3 + ((float) logicalBounds.getY()), (float) logicalBounds.getWidth(), (float) logicalBounds.getHeight());
                graphics2D.setPaint(paint2);
                graphics2D.fill(r0);
            }
            graphics2D.setPaint(paint);
            drawTextAndEmbellishments(label, graphics2D, f2, f3);
            graphics2D.setPaint(paint3);
        }

        @Override // sun.font.Decoration
        public Rectangle2D getVisualBounds(Label label) {
            Rectangle2D rectangle2DHandleGetVisualBounds = label.handleGetVisualBounds();
            if (this.swapColors || this.bgPaint != null || this.strikethrough || this.stdUnderline != null || this.imUnderline != null) {
                Rectangle2D logicalBounds = label.getLogicalBounds();
                float y2 = 0.0f;
                float height = 0.0f;
                if (this.swapColors || this.bgPaint != null) {
                    y2 = (float) logicalBounds.getY();
                    height = y2 + ((float) logicalBounds.getHeight());
                }
                rectangle2DHandleGetVisualBounds.add(new Rectangle2D.Float(0.0f, y2, (float) logicalBounds.getWidth(), Math.max(height, getUnderlineMaxY(label.getCoreMetrics())) - y2));
            }
            return rectangle2DHandleGetVisualBounds;
        }

        @Override // sun.font.Decoration
        Shape getOutline(Label label, float f2, float f3) {
            if (!this.strikethrough && this.stdUnderline == null && this.imUnderline == null) {
                return label.handleGetOutline(f2, f3);
            }
            CoreMetrics coreMetrics = label.getCoreMetrics();
            float f4 = coreMetrics.underlineThickness;
            float f5 = coreMetrics.underlineOffset;
            float width = f2 + ((float) label.getLogicalBounds().getWidth());
            Area area = null;
            if (this.stdUnderline != null) {
                area = new Area(this.stdUnderline.getUnderlineShape(f4, f2, width, f3 + f5));
            }
            if (this.strikethrough) {
                BasicStroke basicStroke = new BasicStroke(coreMetrics.strikethroughThickness, 0, 0);
                float f6 = f3 + coreMetrics.strikethroughOffset;
                Area area2 = new Area(basicStroke.createStrokedShape(new Line2D.Float(f2, f6, width, f6)));
                if (area == null) {
                    area = area2;
                } else {
                    area.add(area2);
                }
            }
            if (this.imUnderline != null) {
                Area area3 = new Area(this.imUnderline.getUnderlineShape(f4, f2, width, f3 + f5));
                if (area == null) {
                    area = area3;
                } else {
                    area.add(area3);
                }
            }
            area.add(new Area(label.handleGetOutline(f2, f3)));
            return new GeneralPath(area);
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(super.toString());
            stringBuffer.append("[");
            if (this.fgPaint != null) {
                stringBuffer.append("fgPaint: " + ((Object) this.fgPaint));
            }
            if (this.bgPaint != null) {
                stringBuffer.append(" bgPaint: " + ((Object) this.bgPaint));
            }
            if (this.swapColors) {
                stringBuffer.append(" swapColors: true");
            }
            if (this.strikethrough) {
                stringBuffer.append(" strikethrough: true");
            }
            if (this.stdUnderline != null) {
                stringBuffer.append(" stdUnderline: " + ((Object) this.stdUnderline));
            }
            if (this.imUnderline != null) {
                stringBuffer.append(" imUnderline: " + ((Object) this.imUnderline));
            }
            stringBuffer.append("]");
            return stringBuffer.toString();
        }
    }
}
