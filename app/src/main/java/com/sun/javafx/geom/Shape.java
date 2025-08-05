package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/Shape.class */
public abstract class Shape {
    public static final int RECT_INTERSECTS = Integer.MIN_VALUE;
    public static final int OUT_LEFT = 1;
    public static final int OUT_TOP = 2;
    public static final int OUT_RIGHT = 4;
    public static final int OUT_BOTTOM = 8;

    public abstract RectBounds getBounds();

    public abstract boolean contains(float f2, float f3);

    public abstract boolean intersects(float f2, float f3, float f4, float f5);

    public abstract boolean contains(float f2, float f3, float f4, float f5);

    public abstract PathIterator getPathIterator(BaseTransform baseTransform);

    public abstract PathIterator getPathIterator(BaseTransform baseTransform, float f2);

    public abstract Shape copy();

    public boolean contains(Point2D p2) {
        return contains(p2.f11907x, p2.f11908y);
    }

    public boolean intersects(RectBounds r2) {
        float x2 = r2.getMinX();
        float y2 = r2.getMinY();
        float w2 = r2.getMaxX() - x2;
        float h2 = r2.getMaxY() - y2;
        return intersects(x2, y2, w2, h2);
    }

    public boolean contains(RectBounds r2) {
        float x2 = r2.getMinX();
        float y2 = r2.getMinY();
        float w2 = r2.getMaxX() - x2;
        float h2 = r2.getMaxY() - y2;
        return contains(x2, y2, w2, h2);
    }

    public static int pointCrossingsForPath(PathIterator pi, float px, float py) {
        if (pi.isDone()) {
            return 0;
        }
        float[] coords = new float[6];
        if (pi.currentSegment(coords) != 0) {
            throw new IllegalPathStateException("missing initial moveto in path definition");
        }
        pi.next();
        float movx = coords[0];
        float movy = coords[1];
        float curx = movx;
        float cury = movy;
        int crossings = 0;
        while (!pi.isDone()) {
            switch (pi.currentSegment(coords)) {
                case 0:
                    if (cury != movy) {
                        crossings += pointCrossingsForLine(px, py, curx, cury, movx, movy);
                    }
                    float f2 = coords[0];
                    curx = f2;
                    movx = f2;
                    float f3 = coords[1];
                    cury = f3;
                    movy = f3;
                    break;
                case 1:
                    float endx = coords[0];
                    float endy = coords[1];
                    crossings += pointCrossingsForLine(px, py, curx, cury, endx, endy);
                    curx = endx;
                    cury = endy;
                    break;
                case 2:
                    float endx2 = coords[2];
                    float endy2 = coords[3];
                    crossings += pointCrossingsForQuad(px, py, curx, cury, coords[0], coords[1], endx2, endy2, 0);
                    curx = endx2;
                    cury = endy2;
                    break;
                case 3:
                    float endx3 = coords[4];
                    float endy3 = coords[5];
                    crossings += pointCrossingsForCubic(px, py, curx, cury, coords[0], coords[1], coords[2], coords[3], endx3, endy3, 0);
                    curx = endx3;
                    cury = endy3;
                    break;
                case 4:
                    if (cury != movy) {
                        crossings += pointCrossingsForLine(px, py, curx, cury, movx, movy);
                    }
                    curx = movx;
                    cury = movy;
                    break;
            }
            pi.next();
        }
        if (cury != movy) {
            crossings += pointCrossingsForLine(px, py, curx, cury, movx, movy);
        }
        return crossings;
    }

    public static int pointCrossingsForLine(float px, float py, float x0, float y0, float x1, float y1) {
        if (py < y0 && py < y1) {
            return 0;
        }
        if (py >= y0 && py >= y1) {
            return 0;
        }
        if (px >= x0 && px >= x1) {
            return 0;
        }
        if (px < x0 && px < x1) {
            return y0 < y1 ? 1 : -1;
        }
        float xintercept = x0 + (((py - y0) * (x1 - x0)) / (y1 - y0));
        if (px >= xintercept) {
            return 0;
        }
        return y0 < y1 ? 1 : -1;
    }

    public static int pointCrossingsForQuad(float px, float py, float x0, float y0, float xc, float yc, float x1, float y1, int level) {
        if (py < y0 && py < yc && py < y1) {
            return 0;
        }
        if (py >= y0 && py >= yc && py >= y1) {
            return 0;
        }
        if (px >= x0 && px >= xc && px >= x1) {
            return 0;
        }
        if (px < x0 && px < xc && px < x1) {
            return py >= y0 ? py < y1 ? 1 : 0 : py >= y1 ? -1 : 0;
        }
        if (level > 52) {
            return pointCrossingsForLine(px, py, x0, y0, x1, y1);
        }
        float x0c = (x0 + xc) / 2.0f;
        float y0c = (y0 + yc) / 2.0f;
        float xc1 = (xc + x1) / 2.0f;
        float yc1 = (yc + y1) / 2.0f;
        float xc2 = (x0c + xc1) / 2.0f;
        float yc2 = (y0c + yc1) / 2.0f;
        if (Float.isNaN(xc2) || Float.isNaN(yc2)) {
            return 0;
        }
        return pointCrossingsForQuad(px, py, x0, y0, x0c, y0c, xc2, yc2, level + 1) + pointCrossingsForQuad(px, py, xc2, yc2, xc1, yc1, x1, y1, level + 1);
    }

    public static int pointCrossingsForCubic(float px, float py, float x0, float y0, float xc0, float yc0, float xc1, float yc1, float x1, float y1, int level) {
        if (py < y0 && py < yc0 && py < yc1 && py < y1) {
            return 0;
        }
        if (py >= y0 && py >= yc0 && py >= yc1 && py >= y1) {
            return 0;
        }
        if (px >= x0 && px >= xc0 && px >= xc1 && px >= x1) {
            return 0;
        }
        if (px < x0 && px < xc0 && px < xc1 && px < x1) {
            return py >= y0 ? py < y1 ? 1 : 0 : py >= y1 ? -1 : 0;
        }
        if (level > 52) {
            return pointCrossingsForLine(px, py, x0, y0, x1, y1);
        }
        float xmid = (xc0 + xc1) / 2.0f;
        float ymid = (yc0 + yc1) / 2.0f;
        float xc02 = (x0 + xc0) / 2.0f;
        float yc02 = (y0 + yc0) / 2.0f;
        float xc12 = (xc1 + x1) / 2.0f;
        float yc12 = (yc1 + y1) / 2.0f;
        float xc0m = (xc02 + xmid) / 2.0f;
        float yc0m = (yc02 + ymid) / 2.0f;
        float xmc1 = (xmid + xc12) / 2.0f;
        float ymc1 = (ymid + yc12) / 2.0f;
        float xmid2 = (xc0m + xmc1) / 2.0f;
        float ymid2 = (yc0m + ymc1) / 2.0f;
        if (Float.isNaN(xmid2) || Float.isNaN(ymid2)) {
            return 0;
        }
        return pointCrossingsForCubic(px, py, x0, y0, xc02, yc02, xc0m, yc0m, xmid2, ymid2, level + 1) + pointCrossingsForCubic(px, py, xmid2, ymid2, xmc1, ymc1, xc12, yc12, x1, y1, level + 1);
    }

    public static int rectCrossingsForPath(PathIterator pi, float rxmin, float rymin, float rxmax, float rymax) {
        if (rxmax <= rxmin || rymax <= rymin || pi.isDone()) {
            return 0;
        }
        float[] coords = new float[6];
        if (pi.currentSegment(coords) != 0) {
            throw new IllegalPathStateException("missing initial moveto in path definition");
        }
        pi.next();
        float f2 = coords[0];
        float movx = f2;
        float curx = f2;
        float f3 = coords[1];
        float movy = f3;
        float cury = f3;
        int crossings = 0;
        while (crossings != Integer.MIN_VALUE && !pi.isDone()) {
            switch (pi.currentSegment(coords)) {
                case 0:
                    if (curx != movx || cury != movy) {
                        crossings = rectCrossingsForLine(crossings, rxmin, rymin, rxmax, rymax, curx, cury, movx, movy);
                    }
                    float f4 = coords[0];
                    curx = f4;
                    movx = f4;
                    float f5 = coords[1];
                    cury = f5;
                    movy = f5;
                    break;
                case 1:
                    float endx = coords[0];
                    float endy = coords[1];
                    crossings = rectCrossingsForLine(crossings, rxmin, rymin, rxmax, rymax, curx, cury, endx, endy);
                    curx = endx;
                    cury = endy;
                    break;
                case 2:
                    float endx2 = coords[2];
                    float endy2 = coords[3];
                    crossings = rectCrossingsForQuad(crossings, rxmin, rymin, rxmax, rymax, curx, cury, coords[0], coords[1], endx2, endy2, 0);
                    curx = endx2;
                    cury = endy2;
                    break;
                case 3:
                    float endx3 = coords[4];
                    float endy3 = coords[5];
                    crossings = rectCrossingsForCubic(crossings, rxmin, rymin, rxmax, rymax, curx, cury, coords[0], coords[1], coords[2], coords[3], endx3, endy3, 0);
                    curx = endx3;
                    cury = endy3;
                    break;
                case 4:
                    if (curx != movx || cury != movy) {
                        crossings = rectCrossingsForLine(crossings, rxmin, rymin, rxmax, rymax, curx, cury, movx, movy);
                    }
                    curx = movx;
                    cury = movy;
                    break;
            }
            pi.next();
        }
        if (crossings != Integer.MIN_VALUE && (curx != movx || cury != movy)) {
            crossings = rectCrossingsForLine(crossings, rxmin, rymin, rxmax, rymax, curx, cury, movx, movy);
        }
        return crossings;
    }

    public static int rectCrossingsForLine(int crossings, float rxmin, float rymin, float rxmax, float rymax, float x0, float y0, float x1, float y1) {
        if (y0 >= rymax && y1 >= rymax) {
            return crossings;
        }
        if (y0 <= rymin && y1 <= rymin) {
            return crossings;
        }
        if (x0 <= rxmin && x1 <= rxmin) {
            return crossings;
        }
        if (x0 >= rxmax && x1 >= rxmax) {
            if (y0 < y1) {
                if (y0 <= rymin) {
                    crossings++;
                }
                if (y1 >= rymax) {
                    crossings++;
                }
            } else if (y1 < y0) {
                if (y1 <= rymin) {
                    crossings--;
                }
                if (y0 >= rymax) {
                    crossings--;
                }
            }
            return crossings;
        }
        if (x0 > rxmin && x0 < rxmax && y0 > rymin && y0 < rymax) {
            return Integer.MIN_VALUE;
        }
        if (x1 > rxmin && x1 < rxmax && y1 > rymin && y1 < rymax) {
            return Integer.MIN_VALUE;
        }
        float xi0 = x0;
        if (y0 < rymin) {
            xi0 += ((rymin - y0) * (x1 - x0)) / (y1 - y0);
        } else if (y0 > rymax) {
            xi0 += ((rymax - y0) * (x1 - x0)) / (y1 - y0);
        }
        float xi1 = x1;
        if (y1 < rymin) {
            xi1 += ((rymin - y1) * (x0 - x1)) / (y0 - y1);
        } else if (y1 > rymax) {
            xi1 += ((rymax - y1) * (x0 - x1)) / (y0 - y1);
        }
        if (xi0 <= rxmin && xi1 <= rxmin) {
            return crossings;
        }
        if (xi0 >= rxmax && xi1 >= rxmax) {
            if (y0 < y1) {
                if (y0 <= rymin) {
                    crossings++;
                }
                if (y1 >= rymax) {
                    crossings++;
                }
            } else if (y1 < y0) {
                if (y1 <= rymin) {
                    crossings--;
                }
                if (y0 >= rymax) {
                    crossings--;
                }
            }
            return crossings;
        }
        return Integer.MIN_VALUE;
    }

    public static int rectCrossingsForQuad(int crossings, float rxmin, float rymin, float rxmax, float rymax, float x0, float y0, float xc, float yc, float x1, float y1, int level) {
        if (y0 >= rymax && yc >= rymax && y1 >= rymax) {
            return crossings;
        }
        if (y0 <= rymin && yc <= rymin && y1 <= rymin) {
            return crossings;
        }
        if (x0 <= rxmin && xc <= rxmin && x1 <= rxmin) {
            return crossings;
        }
        if (x0 >= rxmax && xc >= rxmax && x1 >= rxmax) {
            if (y0 < y1) {
                if (y0 <= rymin && y1 > rymin) {
                    crossings++;
                }
                if (y0 < rymax && y1 >= rymax) {
                    crossings++;
                }
            } else if (y1 < y0) {
                if (y1 <= rymin && y0 > rymin) {
                    crossings--;
                }
                if (y1 < rymax && y0 >= rymax) {
                    crossings--;
                }
            }
            return crossings;
        }
        if (x0 < rxmax && x0 > rxmin && y0 < rymax && y0 > rymin) {
            return Integer.MIN_VALUE;
        }
        if (x1 < rxmax && x1 > rxmin && y1 < rymax && y1 > rymin) {
            return Integer.MIN_VALUE;
        }
        if (level > 52) {
            return rectCrossingsForLine(crossings, rxmin, rymin, rxmax, rymax, x0, y0, x1, y1);
        }
        float x0c = (x0 + xc) / 2.0f;
        float y0c = (y0 + yc) / 2.0f;
        float xc1 = (xc + x1) / 2.0f;
        float yc1 = (yc + y1) / 2.0f;
        float xc2 = (x0c + xc1) / 2.0f;
        float yc2 = (y0c + yc1) / 2.0f;
        if (Float.isNaN(xc2) || Float.isNaN(yc2)) {
            return 0;
        }
        int crossings2 = rectCrossingsForQuad(crossings, rxmin, rymin, rxmax, rymax, x0, y0, x0c, y0c, xc2, yc2, level + 1);
        if (crossings2 != Integer.MIN_VALUE) {
            crossings2 = rectCrossingsForQuad(crossings2, rxmin, rymin, rxmax, rymax, xc2, yc2, xc1, yc1, x1, y1, level + 1);
        }
        return crossings2;
    }

    public static int rectCrossingsForCubic(int crossings, float rxmin, float rymin, float rxmax, float rymax, float x0, float y0, float xc0, float yc0, float xc1, float yc1, float x1, float y1, int level) {
        if (y0 >= rymax && yc0 >= rymax && yc1 >= rymax && y1 >= rymax) {
            return crossings;
        }
        if (y0 <= rymin && yc0 <= rymin && yc1 <= rymin && y1 <= rymin) {
            return crossings;
        }
        if (x0 <= rxmin && xc0 <= rxmin && xc1 <= rxmin && x1 <= rxmin) {
            return crossings;
        }
        if (x0 >= rxmax && xc0 >= rxmax && xc1 >= rxmax && x1 >= rxmax) {
            if (y0 < y1) {
                if (y0 <= rymin && y1 > rymin) {
                    crossings++;
                }
                if (y0 < rymax && y1 >= rymax) {
                    crossings++;
                }
            } else if (y1 < y0) {
                if (y1 <= rymin && y0 > rymin) {
                    crossings--;
                }
                if (y1 < rymax && y0 >= rymax) {
                    crossings--;
                }
            }
            return crossings;
        }
        if (x0 > rxmin && x0 < rxmax && y0 > rymin && y0 < rymax) {
            return Integer.MIN_VALUE;
        }
        if (x1 > rxmin && x1 < rxmax && y1 > rymin && y1 < rymax) {
            return Integer.MIN_VALUE;
        }
        if (level > 52) {
            return rectCrossingsForLine(crossings, rxmin, rymin, rxmax, rymax, x0, y0, x1, y1);
        }
        float xmid = (xc0 + xc1) / 2.0f;
        float ymid = (yc0 + yc1) / 2.0f;
        float xc02 = (x0 + xc0) / 2.0f;
        float yc02 = (y0 + yc0) / 2.0f;
        float xc12 = (xc1 + x1) / 2.0f;
        float yc12 = (yc1 + y1) / 2.0f;
        float xc0m = (xc02 + xmid) / 2.0f;
        float yc0m = (yc02 + ymid) / 2.0f;
        float xmc1 = (xmid + xc12) / 2.0f;
        float ymc1 = (ymid + yc12) / 2.0f;
        float xmid2 = (xc0m + xmc1) / 2.0f;
        float ymid2 = (yc0m + ymc1) / 2.0f;
        if (Float.isNaN(xmid2) || Float.isNaN(ymid2)) {
            return 0;
        }
        int crossings2 = rectCrossingsForCubic(crossings, rxmin, rymin, rxmax, rymax, x0, y0, xc02, yc02, xc0m, yc0m, xmid2, ymid2, level + 1);
        if (crossings2 != Integer.MIN_VALUE) {
            crossings2 = rectCrossingsForCubic(crossings2, rxmin, rymin, rxmax, rymax, xmid2, ymid2, xmc1, ymc1, xc12, yc12, x1, y1, level + 1);
        }
        return crossings2;
    }

    static boolean intersectsLine(float rx1, float ry1, float rwidth, float rheight, float x1, float y1, float x2, float y2) {
        int out2 = outcode(rx1, ry1, rwidth, rheight, x2, y2);
        if (out2 == 0) {
            return true;
        }
        while (true) {
            int out1 = outcode(rx1, ry1, rwidth, rheight, x1, y1);
            if (out1 != 0) {
                if ((out1 & out2) != 0) {
                    return false;
                }
                if ((out1 & 5) != 0) {
                    if ((out1 & 4) != 0) {
                        rx1 += rwidth;
                    }
                    y1 += ((rx1 - x1) * (y2 - y1)) / (x2 - x1);
                    x1 = rx1;
                } else {
                    if ((out1 & 8) != 0) {
                        ry1 += rheight;
                    }
                    x1 += ((ry1 - y1) * (x2 - x1)) / (y2 - y1);
                    y1 = ry1;
                }
            } else {
                return true;
            }
        }
    }

    static int outcode(float rx, float ry, float rwidth, float rheight, float x2, float y2) {
        int out = 0;
        if (rwidth <= 0.0f) {
            out = 0 | 5;
        } else if (x2 < rx) {
            out = 0 | 1;
        } else if (x2 > rx + rwidth) {
            out = 0 | 4;
        }
        if (rheight <= 0.0f) {
            out |= 10;
        } else if (y2 < ry) {
            out |= 2;
        } else if (y2 > ry + rheight) {
            out |= 8;
        }
        return out;
    }

    public static void accumulate(float[] bbox, Shape s2, BaseTransform tx) {
        PathIterator pi = s2.getPathIterator(tx);
        float[] coords = new float[6];
        float mx = 0.0f;
        float my = 0.0f;
        float x0 = 0.0f;
        float y0 = 0.0f;
        while (!pi.isDone()) {
            switch (pi.currentSegment(coords)) {
                case 0:
                    mx = coords[0];
                    my = coords[1];
                    break;
                case 1:
                    break;
                case 2:
                    float x1 = coords[2];
                    float y1 = coords[3];
                    if (bbox[0] > x1) {
                        bbox[0] = x1;
                    }
                    if (bbox[1] > y1) {
                        bbox[1] = y1;
                    }
                    if (bbox[2] < x1) {
                        bbox[2] = x1;
                    }
                    if (bbox[3] < y1) {
                        bbox[3] = y1;
                    }
                    if (bbox[0] > coords[0] || bbox[2] < coords[0]) {
                        accumulateQuad(bbox, 0, x0, coords[0], x1);
                    }
                    if (bbox[1] > coords[1] || bbox[3] < coords[1]) {
                        accumulateQuad(bbox, 1, y0, coords[1], y1);
                    }
                    x0 = x1;
                    y0 = y1;
                    continue;
                    pi.next();
                    break;
                case 3:
                    float x12 = coords[4];
                    float y12 = coords[5];
                    if (bbox[0] > x12) {
                        bbox[0] = x12;
                    }
                    if (bbox[1] > y12) {
                        bbox[1] = y12;
                    }
                    if (bbox[2] < x12) {
                        bbox[2] = x12;
                    }
                    if (bbox[3] < y12) {
                        bbox[3] = y12;
                    }
                    if (bbox[0] > coords[0] || bbox[2] < coords[0] || bbox[0] > coords[2] || bbox[2] < coords[2]) {
                        accumulateCubic(bbox, 0, x0, coords[0], coords[2], x12);
                    }
                    if (bbox[1] > coords[1] || bbox[3] < coords[1] || bbox[1] > coords[3] || bbox[3] < coords[3]) {
                        accumulateCubic(bbox, 1, y0, coords[1], coords[3], y12);
                    }
                    x0 = x12;
                    y0 = y12;
                    continue;
                    pi.next();
                    break;
                case 4:
                    x0 = mx;
                    y0 = my;
                    continue;
                    pi.next();
                default:
                    pi.next();
            }
            x0 = coords[0];
            y0 = coords[1];
            if (bbox[0] > x0) {
                bbox[0] = x0;
            }
            if (bbox[1] > y0) {
                bbox[1] = y0;
            }
            if (bbox[2] < x0) {
                bbox[2] = x0;
            }
            if (bbox[3] < y0) {
                bbox[3] = y0;
            }
            pi.next();
        }
    }

    public static void accumulateQuad(float[] bbox, int off, float v0, float vc, float v1) {
        float num = v0 - vc;
        float den = (v1 - vc) + num;
        if (den != 0.0f) {
            float t2 = num / den;
            if (t2 > 0.0f && t2 < 1.0f) {
                float u2 = 1.0f - t2;
                float v2 = (v0 * u2 * u2) + (2.0f * vc * t2 * u2) + (v1 * t2 * t2);
                if (bbox[off] > v2) {
                    bbox[off] = v2;
                }
                if (bbox[off + 2] < v2) {
                    bbox[off + 2] = v2;
                }
            }
        }
    }

    public static void accumulateCubic(float[] bbox, int off, float v0, float vc0, float vc1, float v1) {
        float c2 = vc0 - v0;
        float b2 = 2.0f * ((vc1 - vc0) - c2);
        float a2 = ((v1 - vc1) - b2) - c2;
        if (a2 == 0.0f) {
            if (b2 == 0.0f) {
                return;
            }
            accumulateCubic(bbox, off, (-c2) / b2, v0, vc0, vc1, v1);
            return;
        }
        float d2 = (b2 * b2) - ((4.0f * a2) * c2);
        if (d2 < 0.0f) {
            return;
        }
        float d3 = (float) Math.sqrt(d2);
        if (b2 < 0.0f) {
            d3 = -d3;
        }
        float q2 = (b2 + d3) / (-2.0f);
        accumulateCubic(bbox, off, q2 / a2, v0, vc0, vc1, v1);
        if (q2 != 0.0f) {
            accumulateCubic(bbox, off, c2 / q2, v0, vc0, vc1, v1);
        }
    }

    public static void accumulateCubic(float[] bbox, int off, float t2, float v0, float vc0, float vc1, float v1) {
        if (t2 > 0.0f && t2 < 1.0f) {
            float u2 = 1.0f - t2;
            float v2 = (v0 * u2 * u2 * u2) + (3.0f * vc0 * t2 * u2 * u2) + (3.0f * vc1 * t2 * t2 * u2) + (v1 * t2 * t2 * t2);
            if (bbox[off] > v2) {
                bbox[off] = v2;
            }
            if (bbox[off + 2] < v2) {
                bbox[off + 2] = v2;
            }
        }
    }
}
