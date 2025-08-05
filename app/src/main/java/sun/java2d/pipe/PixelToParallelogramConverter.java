package sun.java2d.pipe;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import sun.java2d.SunGraphics2D;

/* loaded from: rt.jar:sun/java2d/pipe/PixelToParallelogramConverter.class */
public class PixelToParallelogramConverter extends PixelToShapeConverter implements ShapeDrawPipe {
    ParallelogramPipe outrenderer;
    double minPenSize;
    double normPosition;
    double normRoundingBias;
    boolean adjustfill;

    public PixelToParallelogramConverter(ShapeDrawPipe shapeDrawPipe, ParallelogramPipe parallelogramPipe, double d2, double d3, boolean z2) {
        super(shapeDrawPipe);
        this.outrenderer = parallelogramPipe;
        this.minPenSize = d2;
        this.normPosition = d3;
        this.normRoundingBias = 0.5d - d3;
        this.adjustfill = z2;
    }

    @Override // sun.java2d.pipe.PixelToShapeConverter, sun.java2d.pipe.PixelDrawPipe
    public void drawLine(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
        if (!drawGeneralLine(sunGraphics2D, i2, i3, i4, i5)) {
            super.drawLine(sunGraphics2D, i2, i3, i4, i5);
        }
    }

    @Override // sun.java2d.pipe.PixelToShapeConverter, sun.java2d.pipe.PixelDrawPipe
    public void drawRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
        if (i4 >= 0 && i5 >= 0) {
            if (sunGraphics2D.strokeState < 3) {
                BasicStroke basicStroke = (BasicStroke) sunGraphics2D.stroke;
                if (i4 > 0 && i5 > 0) {
                    if (basicStroke.getLineJoin() == 0 && basicStroke.getDashArray() == null) {
                        drawRectangle(sunGraphics2D, i2, i3, i4, i5, basicStroke.getLineWidth());
                        return;
                    }
                } else {
                    drawLine(sunGraphics2D, i2, i3, i2 + i4, i3 + i5);
                    return;
                }
            }
            super.drawRect(sunGraphics2D, i2, i3, i4, i5);
        }
    }

    @Override // sun.java2d.pipe.PixelToShapeConverter, sun.java2d.pipe.PixelFillPipe
    public void fillRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
        if (i4 > 0 && i5 > 0) {
            fillRectangle(sunGraphics2D, i2, i3, i4, i5);
        }
    }

    @Override // sun.java2d.pipe.ShapeDrawPipe
    public void draw(SunGraphics2D sunGraphics2D, Shape shape) {
        if (sunGraphics2D.strokeState < 3) {
            BasicStroke basicStroke = (BasicStroke) sunGraphics2D.stroke;
            if (shape instanceof Rectangle2D) {
                if (basicStroke.getLineJoin() == 0 && basicStroke.getDashArray() == null) {
                    Rectangle2D rectangle2D = (Rectangle2D) shape;
                    double width = rectangle2D.getWidth();
                    double height = rectangle2D.getHeight();
                    double x2 = rectangle2D.getX();
                    double y2 = rectangle2D.getY();
                    if (width >= 0.0d && height >= 0.0d) {
                        drawRectangle(sunGraphics2D, x2, y2, width, height, basicStroke.getLineWidth());
                        return;
                    }
                    return;
                }
            } else if (shape instanceof Line2D) {
                Line2D line2D = (Line2D) shape;
                if (drawGeneralLine(sunGraphics2D, line2D.getX1(), line2D.getY1(), line2D.getX2(), line2D.getY2())) {
                    return;
                }
            }
        }
        this.outpipe.draw(sunGraphics2D, shape);
    }

    @Override // sun.java2d.pipe.ShapeDrawPipe
    public void fill(SunGraphics2D sunGraphics2D, Shape shape) {
        if (shape instanceof Rectangle2D) {
            Rectangle2D rectangle2D = (Rectangle2D) shape;
            double width = rectangle2D.getWidth();
            double height = rectangle2D.getHeight();
            if (width > 0.0d && height > 0.0d) {
                fillRectangle(sunGraphics2D, rectangle2D.getX(), rectangle2D.getY(), width, height);
                return;
            }
            return;
        }
        this.outpipe.fill(sunGraphics2D, shape);
    }

    static double len(double d2, double d3) {
        if (d2 == 0.0d) {
            return Math.abs(d3);
        }
        if (d3 == 0.0d) {
            return Math.abs(d2);
        }
        return Math.sqrt((d2 * d2) + (d3 * d3));
    }

    double normalize(double d2) {
        return Math.floor(d2 + this.normRoundingBias) + this.normPosition;
    }

    public boolean drawGeneralLine(SunGraphics2D sunGraphics2D, double d2, double d3, double d4, double d5) {
        BasicStroke basicStroke;
        int endCap;
        double dNormalize;
        double dNormalize2;
        double dNormalize3;
        double dNormalize4;
        double d6;
        double d7;
        if (sunGraphics2D.strokeState == 3 || sunGraphics2D.strokeState == 1 || (endCap = (basicStroke = (BasicStroke) sunGraphics2D.stroke).getEndCap()) == 1 || basicStroke.getDashArray() != null) {
            return false;
        }
        double lineWidth = basicStroke.getLineWidth();
        double d8 = d4 - d2;
        double d9 = d5 - d3;
        switch (sunGraphics2D.transformState) {
            case 0:
                dNormalize = d2;
                dNormalize2 = d3;
                dNormalize3 = d4;
                dNormalize4 = d5;
                break;
            case 1:
            case 2:
                double translateX = sunGraphics2D.transform.getTranslateX();
                double translateY = sunGraphics2D.transform.getTranslateY();
                dNormalize = d2 + translateX;
                dNormalize2 = d3 + translateY;
                dNormalize3 = d4 + translateX;
                dNormalize4 = d5 + translateY;
                break;
            case 3:
            case 4:
                double[] dArr = {d2, d3, d4, d5};
                sunGraphics2D.transform.transform(dArr, 0, dArr, 0, 2);
                dNormalize = dArr[0];
                dNormalize2 = dArr[1];
                dNormalize3 = dArr[2];
                dNormalize4 = dArr[3];
                break;
            default:
                throw new InternalError("unknown TRANSFORM state...");
        }
        if (sunGraphics2D.strokeHint != 2) {
            if (sunGraphics2D.strokeState == 0 && (this.outrenderer instanceof PixelDrawPipe)) {
                ((PixelDrawPipe) this.outrenderer).drawLine(sunGraphics2D, (int) Math.floor(dNormalize - sunGraphics2D.transX), (int) Math.floor(dNormalize2 - sunGraphics2D.transY), (int) Math.floor(dNormalize3 - sunGraphics2D.transX), (int) Math.floor(dNormalize4 - sunGraphics2D.transY));
                return true;
            }
            dNormalize = normalize(dNormalize);
            dNormalize2 = normalize(dNormalize2);
            dNormalize3 = normalize(dNormalize3);
            dNormalize4 = normalize(dNormalize4);
        }
        if (sunGraphics2D.transformState >= 3) {
            double dLen = len(d8, d9);
            if (dLen == 0.0d) {
                dLen = 1.0d;
                d8 = 1.0d;
            }
            double[] dArr2 = {d9 / dLen, (-d8) / dLen};
            sunGraphics2D.transform.deltaTransform(dArr2, 0, dArr2, 0, 1);
            lineWidth *= len(dArr2[0], dArr2[1]);
        }
        double dMax = Math.max(lineWidth, this.minPenSize);
        double d10 = dNormalize3 - dNormalize;
        double d11 = dNormalize4 - dNormalize2;
        double dLen2 = len(d10, d11);
        if (dLen2 == 0.0d) {
            if (endCap == 0) {
                return true;
            }
            d6 = dMax;
            d7 = 0.0d;
        } else {
            d6 = (dMax * d10) / dLen2;
            d7 = (dMax * d11) / dLen2;
        }
        double d12 = dNormalize + (d7 / 2.0d);
        double d13 = dNormalize2 - (d6 / 2.0d);
        if (endCap == 2) {
            d12 -= d6 / 2.0d;
            d13 -= d7 / 2.0d;
            d10 += d6;
            d11 += d7;
        }
        this.outrenderer.fillParallelogram(sunGraphics2D, d2, d3, d4, d5, d12, d13, -d7, d6, d10, d11);
        return true;
    }

    public void fillRectangle(SunGraphics2D sunGraphics2D, double d2, double d3, double d4, double d5) {
        AffineTransform affineTransform = sunGraphics2D.transform;
        double scaleX = affineTransform.getScaleX();
        double shearY = affineTransform.getShearY();
        double shearX = affineTransform.getShearX();
        double scaleY = affineTransform.getScaleY();
        double translateX = (d2 * scaleX) + (d3 * shearX) + affineTransform.getTranslateX();
        double translateY = (d2 * shearY) + (d3 * scaleY) + affineTransform.getTranslateY();
        double dNormalize = scaleX * d4;
        double dNormalize2 = shearY * d4;
        double dNormalize3 = shearX * d5;
        double dNormalize4 = scaleY * d5;
        if (this.adjustfill && sunGraphics2D.strokeState < 3 && sunGraphics2D.strokeHint != 2) {
            double dNormalize5 = normalize(translateX);
            double dNormalize6 = normalize(translateY);
            dNormalize = normalize(translateX + dNormalize) - dNormalize5;
            dNormalize2 = normalize(translateY + dNormalize2) - dNormalize6;
            dNormalize3 = normalize(translateX + dNormalize3) - dNormalize5;
            dNormalize4 = normalize(translateY + dNormalize4) - dNormalize6;
            translateX = dNormalize5;
            translateY = dNormalize6;
        }
        this.outrenderer.fillParallelogram(sunGraphics2D, d2, d3, d2 + d4, d3 + d5, translateX, translateY, dNormalize, dNormalize2, dNormalize3, dNormalize4);
    }

    public void drawRectangle(SunGraphics2D sunGraphics2D, double d2, double d3, double d4, double d5, double d6) {
        AffineTransform affineTransform = sunGraphics2D.transform;
        double scaleX = affineTransform.getScaleX();
        double shearY = affineTransform.getShearY();
        double shearX = affineTransform.getShearX();
        double scaleY = affineTransform.getScaleY();
        double translateX = (d2 * scaleX) + (d3 * shearX) + affineTransform.getTranslateX();
        double translateY = (d2 * shearY) + (d3 * scaleY) + affineTransform.getTranslateY();
        double dLen = len(scaleX, shearY) * d6;
        double dLen2 = len(shearX, scaleY) * d6;
        double dNormalize = scaleX * d4;
        double dNormalize2 = shearY * d4;
        double dNormalize3 = shearX * d5;
        double dNormalize4 = scaleY * d5;
        if (sunGraphics2D.strokeState < 3 && sunGraphics2D.strokeHint != 2) {
            double dNormalize5 = normalize(translateX);
            double dNormalize6 = normalize(translateY);
            dNormalize = normalize(translateX + dNormalize) - dNormalize5;
            dNormalize2 = normalize(translateY + dNormalize2) - dNormalize6;
            dNormalize3 = normalize(translateX + dNormalize3) - dNormalize5;
            dNormalize4 = normalize(translateY + dNormalize4) - dNormalize6;
            translateX = dNormalize5;
            translateY = dNormalize6;
        }
        double dMax = Math.max(dLen, this.minPenSize);
        double dMax2 = Math.max(dLen2, this.minPenSize);
        double dLen3 = len(dNormalize, dNormalize2);
        double dLen4 = len(dNormalize3, dNormalize4);
        if (dMax >= dLen3 || dMax2 >= dLen4) {
            fillOuterParallelogram(sunGraphics2D, d2, d3, d2 + d4, d3 + d5, translateX, translateY, dNormalize, dNormalize2, dNormalize3, dNormalize4, dLen3, dLen4, dMax, dMax2);
        } else {
            this.outrenderer.drawParallelogram(sunGraphics2D, d2, d3, d2 + d4, d3 + d5, translateX, translateY, dNormalize, dNormalize2, dNormalize3, dNormalize4, dMax / dLen3, dMax2 / dLen4);
        }
    }

    public void fillOuterParallelogram(SunGraphics2D sunGraphics2D, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13, double d14, double d15) {
        double d16 = d8 / d12;
        double d17 = d9 / d12;
        double d18 = d10 / d13;
        double d19 = d11 / d13;
        if (d12 == 0.0d) {
            if (d13 == 0.0d) {
                d18 = 0.0d;
                d19 = 1.0d;
            }
            d16 = d19;
            d17 = -d18;
        } else if (d13 == 0.0d) {
            d18 = d17;
            d19 = -d16;
        }
        double d20 = d16 * d14;
        double d21 = d17 * d14;
        double d22 = d18 * d15;
        double d23 = d19 * d15;
        this.outrenderer.fillParallelogram(sunGraphics2D, d2, d3, d4, d5, d6 - ((d20 + d22) / 2.0d), d7 - ((d21 + d23) / 2.0d), d8 + d20, d9 + d21, d10 + d22, d11 + d23);
    }
}
