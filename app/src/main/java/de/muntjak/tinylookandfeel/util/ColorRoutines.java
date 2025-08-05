package de.muntjak.tinylookandfeel.util;

import java.awt.Color;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/util/ColorRoutines.class */
public class ColorRoutines {
    private static final int RGB = 1;
    private static final int RBG = 2;
    private static final int GBR = 3;
    private static final int GRB = 4;
    private static final int BRG = 5;
    private static final int BGR = 6;
    private static float[] hsb = new float[3];
    private boolean preserveGrey;
    private int chue;
    private int csat;
    private int cbri;
    private int fr;
    private int fg;
    private int fb;
    private int hi;
    private int lo;
    private int md;
    private boolean hiIsR;
    private boolean hiIsG;
    private boolean hiIsB;
    private boolean mdIsR;
    private boolean mdIsG;
    private boolean mdIsB;
    private boolean loIsR;
    private boolean loIsG;
    private boolean loIsB;

    ColorRoutines(HSBReference hSBReference) {
        this.chue = hSBReference.hue;
        this.csat = hSBReference.getSaturation();
        this.cbri = hSBReference.getBrightness();
        this.preserveGrey = hSBReference.isPreserveGrey();
        Color hSBColor = Color.getHSBColor((float) (this.chue / 360.0d), 1.0f, 1.0f);
        this.fr = hSBColor.getRed();
        this.fg = hSBColor.getGreen();
        this.fb = hSBColor.getBlue();
        if (this.fr >= this.fg && this.fg >= this.fb) {
            this.hi = this.fr;
            this.md = this.fg;
            this.lo = this.fb;
            this.hiIsR = true;
            this.mdIsG = true;
            this.loIsB = true;
            return;
        }
        if (this.fr >= this.fb && this.fb >= this.fg) {
            this.hi = this.fr;
            this.md = this.fb;
            this.lo = this.fg;
            this.hiIsR = true;
            this.mdIsB = true;
            this.loIsG = true;
            return;
        }
        if (this.fg >= this.fr && this.fr >= this.fb) {
            this.hi = this.fg;
            this.md = this.fr;
            this.lo = this.fb;
            this.hiIsG = true;
            this.mdIsR = true;
            this.loIsB = true;
            return;
        }
        if (this.fg >= this.fb && this.fb >= this.fr) {
            this.hi = this.fg;
            this.md = this.fb;
            this.lo = this.fr;
            this.hiIsG = true;
            this.mdIsB = true;
            this.loIsR = true;
            return;
        }
        if (this.fb >= this.fg && this.fg >= this.fr) {
            this.hi = this.fb;
            this.md = this.fg;
            this.lo = this.fr;
            this.hiIsB = true;
            this.mdIsG = true;
            this.loIsR = true;
            return;
        }
        if (this.fb < this.fr || this.fr < this.fg) {
            return;
        }
        this.hi = this.fb;
        this.md = this.fr;
        this.lo = this.fg;
        this.hiIsB = true;
        this.mdIsR = true;
        this.loIsG = true;
    }

    private void setHSB(int i2, int i3, int i4) {
        this.chue = getHue(i2, i3, i4);
        this.csat = getSaturation(i2, i3, i4);
        this.cbri = getBrightness(i2, i3, i4);
    }

    public static Color getAverage(Color color, Color color2) {
        return new Color((int) Math.round((color.getRed() + color2.getRed()) / 2.0d), (int) Math.round((color.getGreen() + color2.getGreen()) / 2.0d), (int) Math.round((color.getBlue() + color2.getBlue()) / 2.0d));
    }

    public static Color getGradient(Color color, Color color2, int i2, int i3) {
        if (i3 == 0) {
            return color;
        }
        if (i3 == i2) {
            return color2;
        }
        double d2 = (i3 * 1.1d) / i2;
        double d3 = 1.0d - d2;
        return new Color((int) Math.round((color.getRed() * d3) + (color2.getRed() * d2)), (int) Math.round((color.getGreen() * d3) + (color2.getGreen() * d2)), (int) Math.round((color.getBlue() * d3) + (color2.getBlue() * d2)));
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x0266, code lost:
    
        r19 = r18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x0272, code lost:
    
        if (r17 != 256) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x0275, code lost:
    
        r0 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x0279, code lost:
    
        r0 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x027d, code lost:
    
        if (r17 != (-1)) goto L108;
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x0280, code lost:
    
        r1 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x0284, code lost:
    
        r1 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x0286, code lost:
    
        if ((r0 | r1) == false) goto L112;
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x0289, code lost:
    
        r17 = r20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x028d, code lost:
    
        r22 = new java.awt.Color(r0, r17, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:134:0x030e, code lost:
    
        return new java.awt.Color(r0, r0, r17);
     */
    /* JADX WARN: Code restructure failed: missing block: B:140:0x0332, code lost:
    
        if (r18 == r7) goto L167;
     */
    /* JADX WARN: Code restructure failed: missing block: B:141:0x0335, code lost:
    
        r18 = getHue(r0, r17, r0);
        r19 = r18;
        r17 = r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:143:0x034b, code lost:
    
        if (r18 == r7) goto L470;
     */
    /* JADX WARN: Code restructure failed: missing block: B:145:0x0350, code lost:
    
        if (r17 < 0) goto L471;
     */
    /* JADX WARN: Code restructure failed: missing block: B:146:0x0353, code lost:
    
        r17 = r17 - 1;
        r18 = getHue(r0, r0, r17);
     */
    /* JADX WARN: Code restructure failed: missing block: B:147:0x0364, code lost:
    
        if (r17 != (-1)) goto L149;
     */
    /* JADX WARN: Code restructure failed: missing block: B:150:0x036d, code lost:
    
        if (r18 != r7) goto L153;
     */
    /* JADX WARN: Code restructure failed: missing block: B:152:0x037d, code lost:
    
        return new java.awt.Color(r0, r0, r17);
     */
    /* JADX WARN: Code restructure failed: missing block: B:154:0x0381, code lost:
    
        if (r19 >= r7) goto L157;
     */
    /* JADX WARN: Code restructure failed: missing block: B:156:0x0387, code lost:
    
        if (r18 > r7) goto L474;
     */
    /* JADX WARN: Code restructure failed: missing block: B:158:0x038d, code lost:
    
        if (r19 <= r7) goto L163;
     */
    /* JADX WARN: Code restructure failed: missing block: B:160:0x0393, code lost:
    
        if (r18 >= r7) goto L163;
     */
    /* JADX WARN: Code restructure failed: missing block: B:162:0x03a3, code lost:
    
        return new java.awt.Color(r0, r0, r17);
     */
    /* JADX WARN: Code restructure failed: missing block: B:164:0x03ad, code lost:
    
        if (java.lang.Math.abs(r18 - r7) >= r21) goto L476;
     */
    /* JADX WARN: Code restructure failed: missing block: B:165:0x03b0, code lost:
    
        r21 = java.lang.Math.abs(r18 - r7);
        r20 = r17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x03bd, code lost:
    
        r19 = r18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x03c9, code lost:
    
        if (r17 != 256) goto L170;
     */
    /* JADX WARN: Code restructure failed: missing block: B:169:0x03cc, code lost:
    
        r0 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:170:0x03d0, code lost:
    
        r0 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:172:0x03d4, code lost:
    
        if (r17 != (-1)) goto L174;
     */
    /* JADX WARN: Code restructure failed: missing block: B:173:0x03d7, code lost:
    
        r1 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:174:0x03db, code lost:
    
        r1 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:176:0x03dd, code lost:
    
        if ((r0 | r1) == false) goto L178;
     */
    /* JADX WARN: Code restructure failed: missing block: B:177:0x03e0, code lost:
    
        r17 = r20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:178:0x03e4, code lost:
    
        r22 = new java.awt.Color(r0, r0, r17);
     */
    /* JADX WARN: Code restructure failed: missing block: B:200:0x0465, code lost:
    
        return new java.awt.Color(r0, r0, r17);
     */
    /* JADX WARN: Code restructure failed: missing block: B:206:0x0489, code lost:
    
        if (r18 == r7) goto L233;
     */
    /* JADX WARN: Code restructure failed: missing block: B:207:0x048c, code lost:
    
        r18 = getHue(r0, r17, r0);
        r19 = r18;
        r17 = r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:209:0x04a2, code lost:
    
        if (r18 == r7) goto L486;
     */
    /* JADX WARN: Code restructure failed: missing block: B:211:0x04a7, code lost:
    
        if (r17 < 0) goto L487;
     */
    /* JADX WARN: Code restructure failed: missing block: B:212:0x04aa, code lost:
    
        r17 = r17 - 1;
        r18 = getHue(r0, r0, r17);
     */
    /* JADX WARN: Code restructure failed: missing block: B:213:0x04bb, code lost:
    
        if (r17 != (-1)) goto L215;
     */
    /* JADX WARN: Code restructure failed: missing block: B:216:0x04c4, code lost:
    
        if (r18 != r7) goto L219;
     */
    /* JADX WARN: Code restructure failed: missing block: B:218:0x04d4, code lost:
    
        return new java.awt.Color(r0, r0, r17);
     */
    /* JADX WARN: Code restructure failed: missing block: B:220:0x04d8, code lost:
    
        if (r19 >= r7) goto L223;
     */
    /* JADX WARN: Code restructure failed: missing block: B:222:0x04de, code lost:
    
        if (r18 > r7) goto L490;
     */
    /* JADX WARN: Code restructure failed: missing block: B:224:0x04e4, code lost:
    
        if (r19 <= r7) goto L229;
     */
    /* JADX WARN: Code restructure failed: missing block: B:226:0x04ea, code lost:
    
        if (r18 >= r7) goto L229;
     */
    /* JADX WARN: Code restructure failed: missing block: B:228:0x04fa, code lost:
    
        return new java.awt.Color(r0, r0, r17);
     */
    /* JADX WARN: Code restructure failed: missing block: B:230:0x0504, code lost:
    
        if (java.lang.Math.abs(r18 - r7) >= r21) goto L492;
     */
    /* JADX WARN: Code restructure failed: missing block: B:231:0x0507, code lost:
    
        r21 = java.lang.Math.abs(r18 - r7);
        r20 = r17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:232:0x0514, code lost:
    
        r19 = r18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:234:0x0520, code lost:
    
        if (r17 != 256) goto L236;
     */
    /* JADX WARN: Code restructure failed: missing block: B:235:0x0523, code lost:
    
        r0 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:236:0x0527, code lost:
    
        r0 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:238:0x052b, code lost:
    
        if (r17 != (-1)) goto L240;
     */
    /* JADX WARN: Code restructure failed: missing block: B:239:0x052e, code lost:
    
        r1 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:240:0x0532, code lost:
    
        r1 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:242:0x0534, code lost:
    
        if ((r0 | r1) == false) goto L244;
     */
    /* JADX WARN: Code restructure failed: missing block: B:243:0x0537, code lost:
    
        r17 = r20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:244:0x053b, code lost:
    
        r22 = new java.awt.Color(r0, r0, r17);
     */
    /* JADX WARN: Code restructure failed: missing block: B:266:0x05bc, code lost:
    
        return new java.awt.Color(r17, r0, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:272:0x05e0, code lost:
    
        if (r18 == r7) goto L299;
     */
    /* JADX WARN: Code restructure failed: missing block: B:273:0x05e3, code lost:
    
        r18 = getHue(r0, r17, r0);
        r19 = r18;
        r17 = r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:275:0x05f9, code lost:
    
        if (r18 == r7) goto L503;
     */
    /* JADX WARN: Code restructure failed: missing block: B:277:0x05fe, code lost:
    
        if (r17 < 0) goto L504;
     */
    /* JADX WARN: Code restructure failed: missing block: B:278:0x0601, code lost:
    
        r17 = r17 - 1;
        r18 = getHue(r17, r0, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:279:0x0612, code lost:
    
        if (r17 != (-1)) goto L281;
     */
    /* JADX WARN: Code restructure failed: missing block: B:282:0x061b, code lost:
    
        if (r18 != r7) goto L285;
     */
    /* JADX WARN: Code restructure failed: missing block: B:284:0x062b, code lost:
    
        return new java.awt.Color(r17, r0, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:286:0x062f, code lost:
    
        if (r19 >= r7) goto L289;
     */
    /* JADX WARN: Code restructure failed: missing block: B:288:0x0635, code lost:
    
        if (r18 > r7) goto L501;
     */
    /* JADX WARN: Code restructure failed: missing block: B:290:0x063b, code lost:
    
        if (r19 <= r7) goto L295;
     */
    /* JADX WARN: Code restructure failed: missing block: B:292:0x0641, code lost:
    
        if (r18 >= r7) goto L295;
     */
    /* JADX WARN: Code restructure failed: missing block: B:294:0x0651, code lost:
    
        return new java.awt.Color(r17, r0, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:296:0x065b, code lost:
    
        if (java.lang.Math.abs(r18 - r7) >= r21) goto L508;
     */
    /* JADX WARN: Code restructure failed: missing block: B:297:0x065e, code lost:
    
        r21 = java.lang.Math.abs(r18 - r7);
        r20 = r17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x066b, code lost:
    
        r19 = r18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:300:0x0677, code lost:
    
        if (r17 != 256) goto L302;
     */
    /* JADX WARN: Code restructure failed: missing block: B:301:0x067a, code lost:
    
        r0 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:302:0x067e, code lost:
    
        r0 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:304:0x0682, code lost:
    
        if (r17 != (-1)) goto L306;
     */
    /* JADX WARN: Code restructure failed: missing block: B:305:0x0685, code lost:
    
        r1 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:306:0x0689, code lost:
    
        r1 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:308:0x068b, code lost:
    
        if ((r0 | r1) == false) goto L310;
     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x068e, code lost:
    
        r17 = r20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:310:0x0692, code lost:
    
        r22 = new java.awt.Color(r17, r0, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:332:0x0713, code lost:
    
        return new java.awt.Color(r17, r0, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:398:0x086a, code lost:
    
        return new java.awt.Color(r0, r17, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x01db, code lost:
    
        if (r18 == r7) goto L101;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x01de, code lost:
    
        r18 = getHue(r0, r17, r0);
        r19 = r18;
        r17 = r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x01f4, code lost:
    
        if (r18 == r7) goto L453;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x01f9, code lost:
    
        if (r17 < 0) goto L454;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x01fc, code lost:
    
        r17 = r17 - 1;
        r18 = getHue(r0, r17, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x020d, code lost:
    
        if (r17 != (-1)) goto L83;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x0216, code lost:
    
        if (r18 != r7) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x0226, code lost:
    
        return new java.awt.Color(r0, r17, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x022a, code lost:
    
        if (r19 >= r7) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x0230, code lost:
    
        if (r18 > r7) goto L457;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x0236, code lost:
    
        if (r19 <= r7) goto L97;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x023c, code lost:
    
        if (r18 >= r7) goto L97;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x024c, code lost:
    
        return new java.awt.Color(r0, r17, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x0256, code lost:
    
        if (java.lang.Math.abs(r18 - r7) >= r21) goto L460;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x0259, code lost:
    
        r21 = java.lang.Math.abs(r18 - r7);
        r20 = r17;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.awt.Color getMaxSaturation(java.awt.Color r6, int r7) {
        /*
            Method dump skipped, instructions count: 2386
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: de.muntjak.tinylookandfeel.util.ColorRoutines.getMaxSaturation(java.awt.Color, int):java.awt.Color");
    }

    private static float getGreyValue(Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        int i2 = 0;
        int i3 = 0;
        if (red < green || red < blue) {
            if (green >= red && green >= blue) {
                i3 = green;
                i2 = red >= blue ? blue : red;
            } else if (blue >= red && blue >= green) {
                i3 = blue;
                i2 = red >= green ? green : red;
            }
        } else {
            if (red == 0) {
                return 0.0f;
            }
            i3 = red;
            i2 = green >= blue ? blue : green;
        }
        return (float) ((i3 + i2) / 2.0d);
    }

    public static int getBrightness(Color color) {
        return getBrightness(color.getRed(), color.getGreen(), color.getBlue());
    }

    private static int getBrightness(int i2, int i3, int i4) {
        if (i2 >= i3 && i2 >= i4) {
            return (int) Math.round((100 * i2) / 255.0d);
        }
        if (i3 >= i2 && i3 >= i4) {
            return (int) Math.round((100 * i3) / 255.0d);
        }
        if (i4 < i2 || i4 < i3) {
            return -1;
        }
        return (int) Math.round((100 * i4) / 255.0d);
    }

    public static int getSaturation(Color color) {
        return getSaturation(color.getRed(), color.getGreen(), color.getBlue());
    }

    private static int getSaturation(int i2, int i3, int i4) {
        int i5 = 0;
        int i6 = 0;
        if (i2 < i3 || i2 < i4) {
            if (i3 >= i2 && i3 >= i4) {
                i6 = i3;
                i5 = i2 >= i4 ? i4 : i2;
            } else if (i4 >= i2 && i4 >= i3) {
                i6 = i4;
                i5 = i2 >= i3 ? i3 : i2;
            }
        } else {
            if (i2 == 0) {
                return 0;
            }
            i6 = i2;
            i5 = i3 >= i4 ? i4 : i3;
        }
        return 100 - ((int) Math.round((100.0d * i5) / i6));
    }

    public static int getHue(Color color) {
        return getHue(color.getRed(), color.getGreen(), color.getBlue());
    }

    public static int calculateHue(Color color) {
        hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        return (int) Math.round(360.0d * hsb[0]);
    }

    private static int getHue(int i2, int i3, int i4) {
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        boolean z2 = true;
        if (i2 >= i3 && i2 >= i4) {
            i7 = i2;
            if (i3 == i4) {
                return 0;
            }
            if (i3 > i4) {
                i5 = i3;
                i6 = i4;
                z2 = true;
            } else {
                i6 = i3;
                i5 = i4;
                z2 = 2;
            }
        } else if (i3 >= i2 && i3 >= i4) {
            i7 = i3;
            if (i2 == i4) {
                return 120;
            }
            if (i2 > i4) {
                i5 = i2;
                i6 = i4;
                z2 = 4;
            } else {
                i6 = i2;
                i5 = i4;
                z2 = 3;
            }
        } else if (i4 >= i2 && i4 >= i3) {
            i7 = i4;
            if (i2 == i3) {
                return 240;
            }
            if (i2 > i3) {
                i5 = i2;
                i6 = i3;
                z2 = 5;
            } else {
                i6 = i2;
                i5 = i3;
                z2 = 6;
            }
        }
        double d2 = (i6 * 255.0d) / i7;
        int iRound = (int) Math.round((60.0d * (((((i5 * 255.0d) / i7) - d2) * 255.0d) / (255.0d - d2))) / 255.0d);
        switch (z2) {
            case true:
                return iRound;
            case true:
                return 360 - iRound;
            case true:
                return 120 + iRound;
            case true:
                return 120 - iRound;
            case true:
                return 240 + iRound;
            case true:
                return 240 - iRound;
            default:
                return -1;
        }
    }

    int colorize(int i2, int i3, int i4, int i5) {
        if (this.cbri == 100) {
            return colorToInt(255, 255, 255, i5);
        }
        if (this.cbri == -100) {
            return colorToInt(0, 0, 0, i5);
        }
        int i6 = i2;
        if (i3 >= i2 && i3 >= i4) {
            i6 = i3;
        } else if (i4 >= i2 && i4 >= i3) {
            i6 = i4;
        }
        int i7 = i2;
        if (i3 <= i2 && i3 <= i4) {
            i7 = i3;
        } else if (i4 <= i2 && i4 <= i3) {
            i7 = i4;
        }
        int i8 = (i6 + i7) / 2;
        if (this.preserveGrey && i2 == i3 && i2 == i4) {
            return colorToInt(i8, i8, i8, i5);
        }
        if (this.cbri < 0) {
            i8 += (i8 * this.cbri) / 100;
        } else if (this.cbri > 0) {
            i8 += ((255 - i8) * this.cbri) / 100;
        }
        int i9 = 0;
        int i10 = 0;
        int i11 = 0;
        int i12 = i8 >= 127 ? 255 - i8 : i8;
        if (this.hiIsR) {
            i9 = i8 + ((i12 * this.csat) / 100);
        } else if (this.hiIsG) {
            i10 = i8 + ((i12 * this.csat) / 100);
        } else if (this.hiIsB) {
            i11 = i8 + ((i12 * this.csat) / 100);
        }
        if (this.mdIsR) {
            i9 = i8 + (((i8 >= 127 ? (this.fr + (((255 - this.fr) * (i8 - 127)) / 128)) - i8 : ((this.fr * i8) / 127) - i8) * this.csat) / 100);
        } else if (this.mdIsG) {
            i10 = i8 + (((i8 >= 127 ? (this.fg + (((255 - this.fg) * (i8 - 127)) / 128)) - i8 : ((this.fg * i8) / 127) - i8) * this.csat) / 100);
        } else if (this.mdIsB) {
            i11 = i8 + (((i8 >= 127 ? (this.fb + (((255 - this.fb) * (i8 - 127)) / 128)) - i8 : ((this.fb * i8) / 127) - i8) * this.csat) / 100);
        }
        int i13 = i8 - (255 - i8);
        if (i13 < 0) {
            i13 = 0;
        }
        int i14 = i8 - i13;
        if (this.loIsR) {
            i9 = i8 - ((i14 * this.csat) / 100);
        } else if (this.loIsG) {
            i10 = i8 - ((i14 * this.csat) / 100);
        } else if (this.loIsB) {
            i11 = i8 - ((i14 * this.csat) / 100);
        }
        return colorToInt(i9, i10, i11, i5);
    }

    public static Color getAlphaColor(Color color, int i2) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), i2);
    }

    private static int colorToInt(int i2, int i3, int i4, int i5) {
        return i4 + (i3 * 256) + (i2 * 65536) + (i5 * 16777216);
    }

    public static Color lighten(Color color, int i2) {
        if (i2 < 0) {
            return color;
        }
        if (i2 > 100) {
            i2 = 100;
        }
        return new Color(color.getRed() + ((int) Math.round(((255 - color.getRed()) * i2) / 100.0d)), color.getGreen() + ((int) Math.round(((255 - color.getGreen()) * i2) / 100.0d)), color.getBlue() + ((int) Math.round(((255 - color.getBlue()) * i2) / 100.0d)), color.getAlpha());
    }

    public static Color darken(Color color, int i2) {
        return (i2 < 0 || i2 > 100) ? color : new Color((int) Math.round((color.getRed() * (100 - i2)) / 100.0d), (int) Math.round((color.getGreen() * (100 - i2)) / 100.0d), (int) Math.round((color.getBlue() * (100 - i2)) / 100.0d), color.getAlpha());
    }

    public static Color getAdjustedColor(Color color, int i2, int i3) {
        int iRound;
        int iRound2;
        int iRound3;
        Color colorLighten = color;
        if (i3 < 0) {
            colorLighten = darken(color, -i3);
        } else if (i3 > 0) {
            colorLighten = lighten(color, i3);
        }
        Color maxSaturation = getMaxSaturation(colorLighten, getHue(color));
        if (i2 >= 0) {
            int red = colorLighten.getRed() - maxSaturation.getRed();
            int green = colorLighten.getGreen() - maxSaturation.getGreen();
            int blue = colorLighten.getBlue() - maxSaturation.getBlue();
            iRound = colorLighten.getRed() - ((int) Math.round((red * i2) / 100.0d));
            iRound2 = colorLighten.getGreen() - ((int) Math.round((green * i2) / 100.0d));
            iRound3 = colorLighten.getBlue() - ((int) Math.round((blue * i2) / 100.0d));
        } else {
            float greyValue = getGreyValue(colorLighten);
            float red2 = colorLighten.getRed() - greyValue;
            float green2 = colorLighten.getGreen() - greyValue;
            float blue2 = colorLighten.getBlue() - greyValue;
            iRound = (int) Math.round(colorLighten.getRed() + ((red2 * i2) / 100.0d));
            iRound2 = (int) Math.round(colorLighten.getGreen() + ((green2 * i2) / 100.0d));
            iRound3 = (int) Math.round(colorLighten.getBlue() + ((blue2 * i2) / 100.0d));
        }
        return new Color(iRound, iRound2, iRound3);
    }

    public static boolean isColorDarker(Color color, Color color2) {
        return (color.getRed() + color.getGreen()) + color.getBlue() < (color2.getRed() + color2.getGreen()) + color2.getBlue();
    }
}
