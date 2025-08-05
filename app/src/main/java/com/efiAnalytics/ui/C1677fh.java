package com.efiAnalytics.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;

/* renamed from: com.efiAnalytics.ui.fh, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/fh.class */
public class C1677fh {

    /* renamed from: b, reason: collision with root package name */
    private static boolean f11671b = false;

    /* renamed from: a, reason: collision with root package name */
    static Stroke f11672a = new BasicStroke(2.0f);

    public static boolean a() {
        return f11671b;
    }

    public static void a(boolean z2) {
        f11671b = z2;
    }

    public static void a(C1701s c1701s, int[] iArr, int[] iArr2, float f2) {
        C1701s c1701sB = b(c1701s);
        for (int i2 = iArr[0]; i2 <= iArr[iArr.length - 1]; i2++) {
            for (int i3 = iArr2[0]; i3 <= iArr2[iArr2.length - 1]; i3++) {
                c1701sB.setValueAt(new Double(a(c1701s, i2, i3, f2)), i3, i2);
            }
        }
        a(c1701sB, c1701s);
    }

    public static double a(C1701s c1701s, int i2, int i3, float f2) {
        float f3 = 1.0f - f2;
        int columnCount = c1701s.getColumnCount();
        int rowCount = c1701s.getRowCount();
        if ((i2 != 0 || i3 != 0) && ((i2 == columnCount - 1 && i3 == rowCount - 1) || ((i2 != 0 || i3 != rowCount - 1) && ((i2 == columnCount - 1 && i3 == 0) || i2 == 0 || i3 == 0 || i2 == columnCount - 1 || i2 != columnCount - 1)))) {
        }
        float f4 = f2 / 8.0f;
        double dDoubleValue = c1701s.getValueAt(i3, i2).doubleValue();
        return (f3 * dDoubleValue) + (f4 * (i2 > 0 ? c1701s.getValueAt(i3, i2 - 1).doubleValue() : dDoubleValue)) + (f4 * ((i2 <= 0 || i3 <= 0) ? dDoubleValue : c1701s.getValueAt(i3 - 1, i2 - 1).doubleValue())) + (f4 * (i3 > 0 ? c1701s.getValueAt(i3 - 1, i2).doubleValue() : dDoubleValue)) + (f4 * ((i2 + 1 >= columnCount || i3 + 1 >= rowCount) ? dDoubleValue : c1701s.getValueAt(i3 + 1, i2 + 1).doubleValue())) + (f4 * (i2 + 1 < columnCount ? c1701s.getValueAt(i3, i2 + 1).doubleValue() : dDoubleValue)) + (f4 * (i3 + 1 < rowCount ? c1701s.getValueAt(i3 + 1, i2).doubleValue() : dDoubleValue)) + (f4 * ((i2 <= 0 || i3 + 1 >= rowCount) ? dDoubleValue : c1701s.getValueAt(i3 + 1, i2 - 1).doubleValue())) + (f4 * ((i2 + 1 >= columnCount || i3 <= 0) ? dDoubleValue : c1701s.getValueAt(i3 - 1, i2 + 1).doubleValue()));
    }

    public static double a(String[] strArr, double d2) throws NumberFormatException {
        double d3 = 10.0d;
        try {
            d3 = Double.parseDouble(strArr[strArr.length - 1]);
        } catch (Exception e2) {
            System.out.println("axisValues=" + ((Object) strArr));
            System.out.println("Exception in getYaxisPosition, axisValues[axisValues.length-1]=" + strArr[strArr.length - 1] + ", axisValues.length=" + strArr.length);
        }
        double d4 = 0.0d;
        int length = strArr.length - 1;
        while (true) {
            if (length < 0) {
                break;
            }
            double d5 = Double.parseDouble(strArr[length]);
            if (d5 == d2) {
                d4 = length;
                break;
            }
            if (d5 > d2) {
                d4 = length == strArr.length - 1 ? length : length + ((d5 - d2) / (d5 - d3));
            } else {
                if (length == 0) {
                    return length;
                }
                d3 = d5;
                length--;
            }
        }
        return d4;
    }

    public static double b(String[] strArr, double d2) {
        double d3 = 0.0d;
        if (Double.parseDouble(strArr[strArr.length - 1]) < Double.parseDouble(strArr[0])) {
            double length = 0.0d;
            int length2 = strArr.length - 1;
            while (true) {
                if (length2 <= 0) {
                    break;
                }
                double d4 = Double.parseDouble(strArr[length2]);
                if (d4 == d2) {
                    length = length2;
                    break;
                }
                if (d4 > d2) {
                    length = (length2 != strArr.length - 1 || d2 > d4) ? (length2 + 1.0d) - ((d2 - d3) / (d4 - d3)) : strArr.length - 1;
                } else {
                    d3 = d4;
                    length2--;
                }
            }
            return length;
        }
        double length3 = strArr.length - 1;
        int i2 = 0;
        while (true) {
            if (i2 >= strArr.length) {
                break;
            }
            double d5 = Double.parseDouble(strArr[i2]);
            if (d5 == d2) {
                length3 = i2;
                break;
            }
            if (d5 > d2) {
                length3 = (i2 != 0 || d2 > d5) ? (i2 - 1.0d) + ((d2 - d3) / (d5 - d3)) : 0.0d;
            } else {
                d3 = d5;
                i2++;
            }
        }
        return length3;
    }

    public static int a(C1701s c1701s) {
        if (c1701s.J() >= 0) {
            return c1701s.J();
        }
        int i2 = 0;
        for (int i3 = 0; i3 < c1701s.getRowCount(); i3++) {
            for (int i4 = 0; i4 < c1701s.getColumnCount(); i4++) {
                String string = c1701s.getValueAt(i3, i4).toString();
                if (string.indexOf(".") != -1) {
                    String strSubstring = string.substring(string.indexOf(".") + 1);
                    int i5 = 0;
                    while (i5 < 4 && i5 < strSubstring.length() && strSubstring.charAt(i5) != '0') {
                        i5++;
                    }
                    if (i5 > i2) {
                        i2 = i5;
                    }
                }
            }
        }
        return i2;
    }

    public static C1701s b(C1701s c1701s) {
        return a(c1701s, (C1701s) null);
    }

    public static C1701s a(C1701s c1701s, C1701s c1701s2) {
        if (c1701s2 == null) {
            c1701s2 = new C1701s();
            c1701s2.a(c1701s.getRowCount(), c1701s.getColumnCount());
        }
        if (c1701s.getRowCount() == c1701s2.getRowCount() && c1701s.getColumnCount() == c1701s2.getColumnCount()) {
            c1701s2.e(c1701s.a());
            c1701s2.c(c1701s.b());
            c1701s2.f(c1701s.z());
            c1701s2.d(c1701s.v());
            if (c1701s.w() != null && c1701s.w().trim().length() > 0) {
                c1701s2.e(c1701s.w());
            }
            for (int i2 = 0; i2 < c1701s.getRowCount(); i2++) {
                for (int i3 = 0; i3 < c1701s.getColumnCount(); i3++) {
                    c1701s2.setValueAt(new Double(c1701s.getValueAt(i2, i3).doubleValue()), i2, i3);
                }
            }
            c1701s2.a(c1701s.D());
        } else {
            for (int i4 = 0; i4 < c1701s2.getRowCount(); i4++) {
                c1701s2.b("" + ((int) c(c1701s.a(), (i4 * (c1701s.getRowCount() - 1.0d)) / (c1701s2.getRowCount() - 1.0d))), i4);
            }
            for (int i5 = 0; i5 < c1701s2.getColumnCount(); i5++) {
                c1701s2.a("" + ((int) c(c1701s.b(), (i5 * (c1701s.getColumnCount() - 1.0d)) / (c1701s2.getColumnCount() - 1.0d))), i5);
            }
            for (int i6 = 0; i6 < c1701s2.getRowCount(); i6++) {
                for (int i7 = 0; i7 < c1701s2.getColumnCount(); i7++) {
                    c1701s2.setValueAt(Double.valueOf(a(c1701s, Double.parseDouble(c1701s2.b()[i7]), Double.parseDouble(c1701s2.a()[i6]))), i6, i7);
                }
            }
        }
        return c1701s2;
    }

    public static C1701s b(C1701s c1701s, C1701s c1701s2) {
        for (int i2 = 0; i2 < c1701s2.getRowCount(); i2++) {
            for (int i3 = 0; i3 < c1701s2.getColumnCount(); i3++) {
                c1701s2.setValueAt(Double.valueOf(a(c1701s, Double.parseDouble(c1701s2.b()[i3]), Double.parseDouble(c1701s2.a()[i2]))), i2, i3);
            }
        }
        return c1701s2;
    }

    public static Color a(double d2, double d3, double d4) {
        return !f11671b ? c(d2, d3, d4) : b(d2, d3, d4);
    }

    public static void a(Graphics2D graphics2D, Polygon polygon, double[] dArr, double d2, double d3) {
        Color colorB;
        Color colorB2;
        Color colorB3;
        Color colorB4;
        boolean z2 = !f11671b;
        if (polygon.npoints != 4) {
            throw new ArrayIndexOutOfBoundsException("Polygon must contain 4 points");
        }
        if (dArr.length != 4) {
            throw new ArrayIndexOutOfBoundsException("vals must contain 4 values");
        }
        if (z2) {
            graphics2D.setStroke(f11672a);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            colorB = a(dArr[0], d2, d3);
            colorB2 = a(dArr[1], d2, d3);
            colorB3 = a(dArr[2], d2, d3);
            colorB4 = a(dArr[3], d2, d3);
        } else {
            colorB = b(dArr[0], d2, d3);
            colorB2 = b(dArr[1], d2, d3);
            colorB3 = b(dArr[2], d2, d3);
            colorB4 = b(dArr[3], d2, d3);
        }
        float fAbs = Math.abs(polygon.ypoints[0] - polygon.ypoints[3]);
        int i2 = polygon.ypoints[0] < polygon.ypoints[3] ? polygon.ypoints[0] : polygon.ypoints[3];
        float fAbs2 = Math.abs(polygon.ypoints[1] - polygon.ypoints[2]);
        float f2 = fAbs > fAbs2 ? fAbs : fAbs2;
        for (int i3 = 0; i3 < f2; i3++) {
            float f3 = (i3 * fAbs) / (f2 * fAbs);
            float f4 = (i3 * fAbs2) / (f2 * fAbs2);
            Color colorA = a(colorB, colorB4, f3);
            Color colorA2 = a(colorB2, colorB3, f4);
            int iRound = polygon.xpoints[0] - Math.round((polygon.xpoints[0] - polygon.xpoints[3]) * f3);
            int iRound2 = polygon.xpoints[1] - Math.round((polygon.xpoints[1] - polygon.xpoints[2]) * f4);
            graphics2D.setPaint(new GradientPaint(iRound, polygon.ypoints[0] + i3, colorA, iRound2, polygon.ypoints[1] + i3, colorA2));
            graphics2D.drawLine(iRound, polygon.ypoints[0] - i3, iRound2, polygon.ypoints[1] - i3);
        }
    }

    public static Color a(Color color, Color color2, float f2) {
        return new Color(Math.round((color.getRed() * f2) + (color2.getRed() * (1.0f - f2))), Math.round((color.getGreen() * f2) + (color2.getGreen() * (1.0f - f2))), Math.round((color.getBlue() * f2) + (color2.getBlue() * (1.0f - f2))), Math.round((color.getAlpha() * f2) + (color2.getAlpha() * (1.0f - f2))));
    }

    public static Color b(double d2, double d3, double d4) {
        return a(d2, d3, d4, 50);
    }

    public static Color a(double d2, double d3, double d4, int i2) {
        if (d2 > d4) {
            d2 = d4;
        }
        if (d2 < d3) {
            d2 = d3;
        }
        if (d3 == d4) {
            return Color.WHITE;
        }
        float f2 = (float) ((d2 - d3) / (d4 - d3));
        float f3 = 3.0f * f2;
        float f4 = ((double) f2) >= 0.1d ? 2.0f * (1.0f - f2) : 0.92f + (10.0f * f2);
        if (f3 > 1.0f) {
            f3 = 1.0f;
        }
        if (f4 > 1.0f) {
            f4 = 1.0f;
        }
        if (f4 < 0.0f) {
            f4 = 0.0f;
        }
        return new Color(f3, f4, 0.0f);
    }

    public static Color b(double d2, double d3, double d4, int i2) {
        int i3;
        int i4;
        int iPow;
        if (d2 > d4) {
            d2 = d4;
        } else if (d2 < d3) {
            d2 = d3;
        }
        double d5 = (d2 - d3) / (d4 - d3);
        if (d5 < 0.33334d) {
            i3 = 3 * ((int) (d5 * 255.0d));
            i4 = 0;
            iPow = 255 - ((int) (Math.pow(d5, 2.0d) * 255.0d));
        } else if (d5 < 0.66667d) {
            double d6 = (d5 - 0.33334d) * 3.0d;
            i3 = 255;
            i4 = (int) (255.0d * d6);
            iPow = 192 - ((int) (d6 * 192.0d));
        } else {
            i3 = 255 - ((int) (((d5 - 0.6667d) * 3.0d) * 255.0d));
            i4 = 255;
            iPow = 0;
        }
        return new Color(i2 + ((int) ((i4 / 255.0d) * (255 - i2))), i2 + ((int) ((i3 / 255.0d) * (255 - i2))), i2 + ((int) ((iPow / 255.0d) * (255 - i2))));
    }

    public static Color c(double d2, double d3, double d4) {
        if (d2 > d4) {
            d2 = d4;
        }
        if (d2 < d3) {
            d2 = d3;
        }
        int i2 = 255 - 120;
        int i3 = 120 - (2 * ((int) (120 * ((d2 - d3) / (d4 - d3)))));
        int iAbs = 0;
        int i4 = 0;
        int i5 = 0;
        if (i3 > 0) {
            i5 = i3;
            i4 = 120 - i5;
        } else if (i3 < 0) {
            iAbs = Math.abs(i3);
            i4 = 120 - iAbs;
        } else if (i3 == 0) {
            i4 = 120;
        }
        int i6 = (int) (i4 * 0.85d);
        if (iAbs < 0) {
            iAbs = 0;
        }
        if (i6 < 0) {
            i6 = 0;
        }
        if (i5 < 0) {
            i5 = 0;
        }
        if (iAbs + i2 > 255) {
            iAbs = 120;
        }
        if (i6 + i2 > 255) {
            i6 = 120;
        }
        if (i5 + i2 > 255) {
            i5 = 120;
        }
        return new Color(iAbs + i2, i6 + i2, i5 + i2);
    }

    public static double a(C1701s c1701s, double d2, double d3) {
        return c1701s.a((c1701s.a().length - 1) - a(c1701s.a(), d3), b(c1701s.b(), d2));
    }

    public static double c(String[] strArr, double d2) {
        int i2 = (int) d2;
        int i3 = i2 < strArr.length - 2 ? i2 + 1 : i2;
        double d3 = d2 - i2;
        return (Double.parseDouble(strArr[i2]) * (1.0d - d3)) + (Double.parseDouble(strArr[i3]) * d3);
    }

    public static double c(C1701s c1701s) {
        double d2 = Double.MIN_VALUE;
        for (int i2 = 0; i2 < c1701s.getRowCount(); i2++) {
            for (int i3 = 0; i3 < c1701s.getColumnCount(); i3++) {
                double dDoubleValue = c1701s.getValueAt(i2, i3).doubleValue();
                if (dDoubleValue > d2) {
                    d2 = dDoubleValue;
                }
            }
        }
        return d2;
    }

    public static double d(C1701s c1701s) {
        double dDoubleValue = 0.0d;
        int i2 = 0;
        for (int i3 = 0; i3 < c1701s.getRowCount(); i3++) {
            for (int i4 = 0; i4 < c1701s.getColumnCount(); i4++) {
                dDoubleValue = ((dDoubleValue * i2) + c1701s.getValueAt(i3, i4).doubleValue()) / (i2 + 1);
                i2++;
            }
        }
        return dDoubleValue;
    }

    public static C1701s c(C1701s c1701s, C1701s c1701s2) {
        if (c1701s2 == null) {
            c1701s2 = new C1701s();
            c1701s2.a(c1701s.getRowCount(), c1701s.getColumnCount());
        }
        if (c1701s.getRowCount() == c1701s2.getRowCount() && c1701s.getColumnCount() == c1701s2.getColumnCount()) {
            c1701s2.e(c1701s.a());
            c1701s2.c(c1701s.b());
            c1701s2.f(c1701s.z());
            c1701s2.d(c1701s.v());
            if (c1701s.w() != null && c1701s.w().trim().length() > 0) {
                c1701s2.e(c1701s.w());
            }
        } else {
            for (int i2 = 0; i2 < c1701s2.getRowCount(); i2++) {
                c1701s2.b("" + ((int) c(c1701s.a(), (i2 * (c1701s.getRowCount() - 1.0d)) / (c1701s2.getRowCount() - 1.0d))), i2);
            }
            for (int i3 = 0; i3 < c1701s2.getColumnCount(); i3++) {
                c1701s2.a("" + ((int) c(c1701s.b(), (i3 * (c1701s.getColumnCount() - 1.0d)) / (c1701s2.getColumnCount() - 1.0d))), i3);
            }
        }
        return c1701s2;
    }
}
