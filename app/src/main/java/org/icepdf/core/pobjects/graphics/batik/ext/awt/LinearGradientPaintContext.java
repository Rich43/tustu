package org.icepdf.core.pobjects.graphics.batik.ext.awt;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import org.icepdf.core.pobjects.graphics.batik.ext.awt.MultipleGradientPaint;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/batik/ext/awt/LinearGradientPaintContext.class */
final class LinearGradientPaintContext extends MultipleGradientPaintContext {
    private float dgdX;
    private float dgdY;
    private float gc;
    private float pixSz;
    private static final int DEFAULT_IMPL = 1;
    private static final int ANTI_ALIAS_IMPL = 3;
    private int fillMethod;

    public LinearGradientPaintContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform t2, RenderingHints hints, Point2D dStart, Point2D dEnd, float[] fractions, Color[] colors, MultipleGradientPaint.CycleMethodEnum cycleMethod, MultipleGradientPaint.ColorSpaceEnum colorSpace) throws NoninvertibleTransformException {
        super(cm, deviceBounds, userBounds, t2, hints, fractions, colors, cycleMethod, colorSpace);
        Point2D.Float start = new Point2D.Float((float) dStart.getX(), (float) dStart.getY());
        Point2D.Float end = new Point2D.Float((float) dEnd.getX(), (float) dEnd.getY());
        float dx = end.f12396x - start.f12396x;
        float dy = end.f12397y - start.f12397y;
        float dSq = (dx * dx) + (dy * dy);
        float constX = dx / dSq;
        float constY = dy / dSq;
        this.dgdX = (this.a00 * constX) + (this.a10 * constY);
        this.dgdY = (this.a01 * constX) + (this.a11 * constY);
        float dgdXAbs = Math.abs(this.dgdX);
        float dgdYAbs = Math.abs(this.dgdY);
        if (dgdXAbs > dgdYAbs) {
            this.pixSz = dgdXAbs;
        } else {
            this.pixSz = dgdYAbs;
        }
        this.gc = ((this.a02 - start.f12396x) * constX) + ((this.a12 - start.f12397y) * constY);
        Object colorRend = hints.get(RenderingHints.KEY_COLOR_RENDERING);
        Object rend = hints.get(RenderingHints.KEY_RENDERING);
        this.fillMethod = 1;
        if (cycleMethod == MultipleGradientPaint.REPEAT || this.hasDiscontinuity) {
            if (rend == RenderingHints.VALUE_RENDER_QUALITY) {
                this.fillMethod = 3;
            }
            if (colorRend == RenderingHints.VALUE_COLOR_RENDER_SPEED) {
                this.fillMethod = 1;
            } else if (colorRend == RenderingHints.VALUE_COLOR_RENDER_QUALITY) {
                this.fillMethod = 3;
            }
        }
    }

    protected void fillHardNoCycle(int[] pixels, int off, int adjust, int x2, int y2, int w2, int h2) {
        float gradStepsF;
        float preGradStepsF;
        int preVal;
        int postVal;
        int val;
        float initConst = (this.dgdX * x2) + this.gc;
        for (int i2 = 0; i2 < h2; i2++) {
            float g2 = initConst + (this.dgdY * (y2 + i2));
            int rowLimit = off + w2;
            if (this.dgdX == 0.0f) {
                if (g2 <= 0.0f) {
                    val = this.gradientUnderflow;
                } else if (g2 >= 1.0f) {
                    val = this.gradientOverflow;
                } else {
                    int gradIdx = 0;
                    while (gradIdx < this.gradientsLength - 1 && g2 >= this.fractions[gradIdx + 1]) {
                        gradIdx++;
                    }
                    float idx = (((g2 - this.fractions[gradIdx]) * 255.0f) / this.normalizedIntervals[gradIdx]) + 0.5f;
                    val = this.gradients[gradIdx][(int) idx];
                }
                while (off < rowLimit) {
                    int i3 = off;
                    off++;
                    pixels[i3] = val;
                }
            } else {
                if (this.dgdX >= 0.0f) {
                    gradStepsF = (1.0f - g2) / this.dgdX;
                    preGradStepsF = (float) Math.ceil((0.0f - g2) / this.dgdX);
                    preVal = this.gradientUnderflow;
                    postVal = this.gradientOverflow;
                } else {
                    gradStepsF = (0.0f - g2) / this.dgdX;
                    preGradStepsF = (float) Math.ceil((1.0f - g2) / this.dgdX);
                    preVal = this.gradientOverflow;
                    postVal = this.gradientUnderflow;
                }
                int gradSteps = gradStepsF > ((float) w2) ? w2 : (int) gradStepsF;
                int preGradSteps = preGradStepsF > ((float) w2) ? w2 : (int) preGradStepsF;
                int gradLimit = off + gradSteps;
                if (preGradSteps > 0) {
                    int preGradLimit = off + preGradSteps;
                    while (off < preGradLimit) {
                        int i4 = off;
                        off++;
                        pixels[i4] = preVal;
                    }
                    g2 += this.dgdX * preGradSteps;
                }
                if (this.dgdX > 0.0f) {
                    int gradIdx2 = 0;
                    while (gradIdx2 < this.gradientsLength - 1 && g2 >= this.fractions[gradIdx2 + 1]) {
                        gradIdx2++;
                    }
                    while (off < gradLimit) {
                        float delta = g2 - this.fractions[gradIdx2];
                        int[] grad = this.gradients[gradIdx2];
                        double stepsD = Math.ceil((this.fractions[gradIdx2 + 1] - g2) / this.dgdX);
                        int steps = stepsD > ((double) w2) ? w2 : (int) stepsD;
                        int subGradLimit = off + steps;
                        if (subGradLimit > gradLimit) {
                            subGradLimit = gradLimit;
                        }
                        int idx2 = ((int) (((delta * 255.0f) / this.normalizedIntervals[gradIdx2]) * 65536.0f)) + 32768;
                        int step = (int) (((this.dgdX * 255.0f) / this.normalizedIntervals[gradIdx2]) * 65536.0f);
                        while (off < subGradLimit) {
                            int i5 = off;
                            off++;
                            pixels[i5] = grad[idx2 >> 16];
                            idx2 += step;
                        }
                        g2 = (float) (g2 + (this.dgdX * stepsD));
                        gradIdx2++;
                    }
                } else {
                    int gradIdx3 = this.gradientsLength - 1;
                    while (gradIdx3 > 0 && g2 <= this.fractions[gradIdx3]) {
                        gradIdx3--;
                    }
                    while (off < gradLimit) {
                        float delta2 = g2 - this.fractions[gradIdx3];
                        int[] grad2 = this.gradients[gradIdx3];
                        double stepsD2 = Math.ceil(delta2 / (-this.dgdX));
                        int steps2 = stepsD2 > ((double) w2) ? w2 : (int) stepsD2;
                        int subGradLimit2 = off + steps2;
                        if (subGradLimit2 > gradLimit) {
                            subGradLimit2 = gradLimit;
                        }
                        int idx3 = ((int) (((delta2 * 255.0f) / this.normalizedIntervals[gradIdx3]) * 65536.0f)) + 32768;
                        int step2 = (int) (((this.dgdX * 255.0f) / this.normalizedIntervals[gradIdx3]) * 65536.0f);
                        while (off < subGradLimit2) {
                            int i6 = off;
                            off++;
                            pixels[i6] = grad2[idx3 >> 16];
                            idx3 += step2;
                        }
                        g2 = (float) (g2 + (this.dgdX * stepsD2));
                        gradIdx3--;
                    }
                }
                while (off < rowLimit) {
                    int i7 = off;
                    off++;
                    pixels[i7] = postVal;
                }
            }
            off += adjust;
        }
    }

    protected void fillSimpleNoCycle(int[] pixels, int off, int adjust, int x2, int y2, int w2, int h2) {
        int gradSteps;
        int preGradSteps;
        int preVal;
        int postVal;
        int val;
        float initConst = (this.dgdX * x2) + this.gc;
        float step = this.dgdX * this.fastGradientArraySize;
        int fpStep = (int) (step * 65536.0f);
        int[] grad = this.gradient;
        for (int i2 = 0; i2 < h2; i2++) {
            float g2 = (float) (((initConst + (this.dgdY * (y2 + i2))) * this.fastGradientArraySize) + 0.5d);
            int rowLimit = off + w2;
            float check = this.dgdX * this.fastGradientArraySize * w2;
            if (check < 0.0f) {
                check = -check;
            }
            if (check < 0.3d) {
                if (g2 <= 0.0f) {
                    val = this.gradientUnderflow;
                } else if (g2 >= this.fastGradientArraySize) {
                    val = this.gradientOverflow;
                } else {
                    val = grad[(int) g2];
                }
                while (off < rowLimit) {
                    int i3 = off;
                    off++;
                    pixels[i3] = val;
                }
            } else {
                if (this.dgdX > 0.0f) {
                    gradSteps = (int) ((this.fastGradientArraySize - g2) / step);
                    preGradSteps = (int) Math.ceil(0.0f - (g2 / step));
                    preVal = this.gradientUnderflow;
                    postVal = this.gradientOverflow;
                } else {
                    gradSteps = (int) ((0.0f - g2) / step);
                    preGradSteps = (int) Math.ceil((this.fastGradientArraySize - g2) / step);
                    preVal = this.gradientOverflow;
                    postVal = this.gradientUnderflow;
                }
                if (gradSteps > w2) {
                    gradSteps = w2;
                }
                int gradLimit = off + gradSteps;
                if (preGradSteps > 0) {
                    if (preGradSteps > w2) {
                        preGradSteps = w2;
                    }
                    int preGradLimit = off + preGradSteps;
                    while (off < preGradLimit) {
                        int i4 = off;
                        off++;
                        pixels[i4] = preVal;
                    }
                    g2 += step * preGradSteps;
                }
                int i5 = (int) (g2 * 65536.0f);
                while (true) {
                    int fpG = i5;
                    if (off >= gradLimit) {
                        break;
                    }
                    int i6 = off;
                    off++;
                    pixels[i6] = grad[fpG >> 16];
                    i5 = fpG + fpStep;
                }
                while (off < rowLimit) {
                    int i7 = off;
                    off++;
                    pixels[i7] = postVal;
                }
            }
            off += adjust;
        }
    }

    protected void fillSimpleRepeat(int[] pixels, int off, int adjust, int x2, int y2, int w2, int h2) {
        float initConst = (this.dgdX * x2) + this.gc;
        float step = (this.dgdX - ((int) this.dgdX)) * this.fastGradientArraySize;
        if (step < 0.0f) {
            step += this.fastGradientArraySize;
        }
        int[] grad = this.gradient;
        for (int i2 = 0; i2 < h2; i2++) {
            float g2 = (initConst + (this.dgdY * (y2 + i2))) - ((int) r0);
            if (g2 < 0.0f) {
                g2 += 1.0f;
            }
            float g3 = (float) ((g2 * this.fastGradientArraySize) + 0.5d);
            int rowLimit = off + w2;
            while (off < rowLimit) {
                int idx = (int) g3;
                if (idx >= this.fastGradientArraySize) {
                    g3 -= this.fastGradientArraySize;
                    idx -= this.fastGradientArraySize;
                }
                int i3 = off;
                off++;
                pixels[i3] = grad[idx];
                g3 += step;
            }
            off += adjust;
        }
    }

    protected void fillSimpleReflect(int[] pixels, int off, int adjust, int x2, int y2, int w2, int h2) {
        float initConst = (this.dgdX * x2) + this.gc;
        int[] grad = this.gradient;
        for (int i2 = 0; i2 < h2; i2++) {
            float g2 = (initConst + (this.dgdY * (y2 + i2))) - (2 * ((int) (r0 / 2.0f)));
            float step = this.dgdX;
            if (g2 < 0.0f) {
                g2 = -g2;
                step = -step;
            }
            float step2 = step - (2.0f * (((int) step) / 2.0f));
            if (step2 < 0.0f) {
                step2 = (float) (step2 + 2.0d);
            }
            int reflectMax = 2 * this.fastGradientArraySize;
            float g3 = (float) ((g2 * this.fastGradientArraySize) + 0.5d);
            float step3 = step2 * this.fastGradientArraySize;
            int rowLimit = off + w2;
            while (off < rowLimit) {
                int idx = (int) g3;
                if (idx >= reflectMax) {
                    g3 -= reflectMax;
                    idx -= reflectMax;
                }
                if (idx <= this.fastGradientArraySize) {
                    int i3 = off;
                    off++;
                    pixels[i3] = grad[idx];
                } else {
                    int i4 = off;
                    off++;
                    pixels[i4] = grad[reflectMax - idx];
                }
                g3 += step3;
            }
            off += adjust;
        }
    }

    @Override // org.icepdf.core.pobjects.graphics.batik.ext.awt.MultipleGradientPaintContext
    protected void fillRaster(int[] pixels, int off, int adjust, int x2, int y2, int w2, int h2) {
        float initConst = (this.dgdX * x2) + this.gc;
        if (this.fillMethod == 3) {
            for (int i2 = 0; i2 < h2; i2++) {
                float g2 = initConst + (this.dgdY * (y2 + i2));
                int rowLimit = off + w2;
                while (off < rowLimit) {
                    int i3 = off;
                    off++;
                    pixels[i3] = indexGradientAntiAlias(g2, this.pixSz);
                    g2 += this.dgdX;
                }
                off += adjust;
            }
            return;
        }
        if (!this.isSimpleLookup) {
            if (this.cycleMethod == MultipleGradientPaint.NO_CYCLE) {
                fillHardNoCycle(pixels, off, adjust, x2, y2, w2, h2);
                return;
            }
            for (int i4 = 0; i4 < h2; i4++) {
                float g3 = initConst + (this.dgdY * (y2 + i4));
                int rowLimit2 = off + w2;
                while (off < rowLimit2) {
                    int i5 = off;
                    off++;
                    pixels[i5] = indexIntoGradientsArrays(g3);
                    g3 += this.dgdX;
                }
                off += adjust;
            }
            return;
        }
        if (this.cycleMethod == MultipleGradientPaint.NO_CYCLE) {
            fillSimpleNoCycle(pixels, off, adjust, x2, y2, w2, h2);
        } else if (this.cycleMethod == MultipleGradientPaint.REPEAT) {
            fillSimpleRepeat(pixels, off, adjust, x2, y2, w2, h2);
        } else {
            fillSimpleReflect(pixels, off, adjust, x2, y2, w2, h2);
        }
    }
}
