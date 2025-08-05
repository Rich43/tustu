package com.sun.pisces;

/* loaded from: jfxrt.jar:com/sun/pisces/GradientColorMap.class */
public final class GradientColorMap {
    public static final int CYCLE_NONE = 0;
    public static final int CYCLE_REPEAT = 1;
    public static final int CYCLE_REFLECT = 2;
    int cycleMethod;
    private static final int LG_RAMP_SIZE = 8;
    private static final int RAMP_SIZE = 256;
    int[] fractions;
    int[] rgba;
    int[] colors = null;

    GradientColorMap(int[] fractions, int[] rgba, int cycleMethod) {
        this.fractions = null;
        this.rgba = null;
        this.cycleMethod = cycleMethod;
        int numStops = fractions.length;
        if (fractions[0] != 0) {
            int[] nfractions = new int[numStops + 1];
            int[] nrgba = new int[numStops + 1];
            System.arraycopy(fractions, 0, nfractions, 1, numStops);
            System.arraycopy(rgba, 0, nrgba, 1, numStops);
            nfractions[0] = 0;
            nrgba[0] = rgba[0];
            fractions = nfractions;
            rgba = nrgba;
            numStops++;
        }
        if (fractions[numStops - 1] != 65536) {
            int[] nfractions2 = new int[numStops + 1];
            int[] nrgba2 = new int[numStops + 1];
            System.arraycopy(fractions, 0, nfractions2, 0, numStops);
            System.arraycopy(rgba, 0, nrgba2, 0, numStops);
            nfractions2[numStops] = 65536;
            nrgba2[numStops] = rgba[numStops - 1];
            fractions = nfractions2;
            rgba = nrgba2;
        }
        this.fractions = new int[fractions.length];
        System.arraycopy(fractions, 0, this.fractions, 0, fractions.length);
        this.rgba = new int[rgba.length];
        System.arraycopy(rgba, 0, this.rgba, 0, rgba.length);
        createRamp();
    }

    private int pad(int frac) {
        switch (this.cycleMethod) {
            case 0:
                if (frac < 0) {
                    return 0;
                }
                if (frac > 65535) {
                    return 65535;
                }
                return frac;
            case 1:
                return frac & 65535;
            case 2:
                if (frac < 0) {
                    frac = -frac;
                }
                int frac2 = frac & 131071;
                if (frac2 > 65535) {
                    frac2 = 131071 - frac2;
                }
                return frac2;
            default:
                throw new RuntimeException("Unknown cycle method: " + this.cycleMethod);
        }
    }

    private int findStop(int frac) {
        int numStops = this.fractions.length;
        for (int i2 = 1; i2 < numStops; i2++) {
            if (this.fractions[i2] > frac) {
                return i2;
            }
        }
        return 1;
    }

    private void accumColor(int frac, int[] r2, int[] g2, int[] b2, int[] a2, int[] red, int[] green, int[] blue, int[] alpha) {
        int stop = findStop(frac);
        int frac2 = frac - this.fractions[stop - 1];
        int delta = this.fractions[stop] - this.fractions[stop - 1];
        red[0] = red[0] + r2[stop - 1] + ((frac2 * (r2[stop] - r2[stop - 1])) / delta);
        green[0] = green[0] + g2[stop - 1] + ((frac2 * (g2[stop] - g2[stop - 1])) / delta);
        blue[0] = blue[0] + b2[stop - 1] + ((frac2 * (b2[stop] - b2[stop - 1])) / delta);
        alpha[0] = alpha[0] + a2[stop - 1] + ((frac2 * (a2[stop] - a2[stop - 1])) / delta);
    }

    private int getColorAA(int frac, int[] r2, int[] g2, int[] b2, int[] a2, int[] red, int[] green, int[] blue, int[] alpha) {
        int stop = findStop(frac);
        int delta = 192;
        if (this.fractions[stop - 1] < pad(frac - 192) && pad(frac + 192) < this.fractions[stop]) {
            delta = 0;
        }
        int total = 0;
        int i2 = -delta;
        while (true) {
            int i3 = i2;
            if (i3 <= delta) {
                int f2 = pad(frac + i3);
                accumColor(f2, r2, g2, b2, a2, red, green, blue, alpha);
                total++;
                i2 = i3 + 64;
            } else {
                alpha[0] = alpha[0] / total;
                red[0] = red[0] / total;
                green[0] = green[0] / total;
                blue[0] = blue[0] / total;
                return (alpha[0] << 24) | (red[0] << 16) | (green[0] << 8) | blue[0];
            }
        }
    }

    private void createRamp() {
        this.colors = new int[256];
        int[] alpha = new int[1];
        int[] red = new int[1];
        int[] green = new int[1];
        int[] blue = new int[1];
        int numStops = this.fractions.length;
        int[] a2 = new int[numStops];
        int[] r2 = new int[numStops];
        int[] g2 = new int[numStops];
        int[] b2 = new int[numStops];
        for (int i2 = 0; i2 < numStops; i2++) {
            a2[i2] = (this.rgba[i2] >> 24) & 255;
            r2[i2] = (this.rgba[i2] >> 16) & 255;
            g2[i2] = (this.rgba[i2] >> 8) & 255;
            b2[i2] = this.rgba[i2] & 255;
        }
        this.colors[0] = this.rgba[0];
        this.colors[255] = this.rgba[numStops - 1];
        for (int i3 = 1; i3 < 255; i3++) {
            alpha[0] = 0;
            blue[0] = 0;
            green[0] = 0;
            red[0] = 0;
            this.colors[i3] = getColorAA(i3 << 8, r2, g2, b2, a2, red, green, blue, alpha);
        }
    }
}
