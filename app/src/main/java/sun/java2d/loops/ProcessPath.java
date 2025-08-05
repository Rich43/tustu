package sun.java2d.loops;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.QuadCurve2D;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/* loaded from: rt.jar:sun/java2d/loops/ProcessPath.class */
public class ProcessPath {
    public static final int PH_MODE_DRAW_CLIP = 0;
    public static final int PH_MODE_FILL_CLIP = 1;
    public static EndSubPathHandler noopEndSubPathHandler = new EndSubPathHandler() { // from class: sun.java2d.loops.ProcessPath.1
        @Override // sun.java2d.loops.ProcessPath.EndSubPathHandler
        public void processEndSubPath() {
        }
    };
    private static final float UPPER_BND = 8.5070587E37f;
    private static final float LOWER_BND = -8.5070587E37f;
    private static final int FWD_PREC = 7;
    private static final int MDP_PREC = 10;
    private static final int MDP_MULT = 1024;
    private static final int MDP_HALF_MULT = 512;
    private static final int UPPER_OUT_BND = 1048576;
    private static final int LOWER_OUT_BND = -1048576;
    private static final float CALC_UBND = 1048576.0f;
    private static final float CALC_LBND = -1048576.0f;
    public static final int EPSFX = 1;
    public static final float EPSF = 9.765625E-4f;
    private static final int MDP_W_MASK = -1024;
    private static final int MDP_F_MASK = 1023;
    private static final int MAX_CUB_SIZE = 256;
    private static final int MAX_QUAD_SIZE = 1024;
    private static final int DF_CUB_STEPS = 3;
    private static final int DF_QUAD_STEPS = 2;
    private static final int DF_CUB_SHIFT = 6;
    private static final int DF_QUAD_SHIFT = 1;
    private static final int DF_CUB_COUNT = 8;
    private static final int DF_QUAD_COUNT = 4;
    private static final int DF_CUB_DEC_BND = 262144;
    private static final int DF_CUB_INC_BND = 32768;
    private static final int DF_QUAD_DEC_BND = 8192;
    private static final int DF_QUAD_INC_BND = 1024;
    private static final int CUB_A_SHIFT = 7;
    private static final int CUB_B_SHIFT = 11;
    private static final int CUB_C_SHIFT = 13;
    private static final int CUB_A_MDP_MULT = 128;
    private static final int CUB_B_MDP_MULT = 2048;
    private static final int CUB_C_MDP_MULT = 8192;
    private static final int QUAD_A_SHIFT = 7;
    private static final int QUAD_B_SHIFT = 9;
    private static final int QUAD_A_MDP_MULT = 128;
    private static final int QUAD_B_MDP_MULT = 512;
    private static final int CRES_MIN_CLIPPED = 0;
    private static final int CRES_MAX_CLIPPED = 1;
    private static final int CRES_NOT_CLIPPED = 3;
    private static final int CRES_INVISIBLE = 4;
    private static final int DF_MAX_POINT = 256;

    /* loaded from: rt.jar:sun/java2d/loops/ProcessPath$EndSubPathHandler.class */
    public interface EndSubPathHandler {
        void processEndSubPath();
    }

    /* loaded from: rt.jar:sun/java2d/loops/ProcessPath$DrawHandler.class */
    public static abstract class DrawHandler {
        public int xMin;
        public int yMin;
        public int xMax;
        public int yMax;
        public float xMinf;
        public float yMinf;
        public float xMaxf;
        public float yMaxf;
        public int strokeControl;

        public abstract void drawLine(int i2, int i3, int i4, int i5);

        public abstract void drawPixel(int i2, int i3);

        public abstract void drawScanline(int i2, int i3, int i4);

        public DrawHandler(int i2, int i3, int i4, int i5, int i6) {
            setBounds(i2, i3, i4, i5, i6);
        }

        public void setBounds(int i2, int i3, int i4, int i5) {
            this.xMin = i2;
            this.yMin = i3;
            this.xMax = i4;
            this.yMax = i5;
            this.xMinf = i2 - 0.5f;
            this.yMinf = i3 - 0.5f;
            this.xMaxf = (i4 - 0.5f) - 9.765625E-4f;
            this.yMaxf = (i5 - 0.5f) - 9.765625E-4f;
        }

        public void setBounds(int i2, int i3, int i4, int i5, int i6) {
            this.strokeControl = i6;
            setBounds(i2, i3, i4, i5);
        }

        public void adjustBounds(int i2, int i3, int i4, int i5) {
            if (this.xMin > i2) {
                i2 = this.xMin;
            }
            if (this.xMax < i4) {
                i4 = this.xMax;
            }
            if (this.yMin > i3) {
                i3 = this.yMin;
            }
            if (this.yMax < i5) {
                i5 = this.yMax;
            }
            setBounds(i2, i3, i4, i5);
        }

        public DrawHandler(int i2, int i3, int i4, int i5) {
            this(i2, i3, i4, i5, 0);
        }
    }

    /* loaded from: rt.jar:sun/java2d/loops/ProcessPath$ProcessHandler.class */
    public static abstract class ProcessHandler implements EndSubPathHandler {
        DrawHandler dhnd;
        int clipMode;

        public abstract void processFixedLine(int i2, int i3, int i4, int i5, int[] iArr, boolean z2, boolean z3);

        public ProcessHandler(DrawHandler drawHandler, int i2) {
            this.dhnd = drawHandler;
            this.clipMode = i2;
        }
    }

    public static boolean fillPath(DrawHandler drawHandler, Path2D.Float r6, int i2, int i3) {
        FillProcessHandler fillProcessHandler = new FillProcessHandler(drawHandler);
        if (!doProcessPath(fillProcessHandler, r6, i2, i3)) {
            return false;
        }
        FillPolygon(fillProcessHandler, r6.getWindingRule());
        return true;
    }

    public static boolean drawPath(DrawHandler drawHandler, EndSubPathHandler endSubPathHandler, Path2D.Float r7, int i2, int i3) {
        return doProcessPath(new DrawProcessHandler(drawHandler, endSubPathHandler), r7, i2, i3);
    }

    public static boolean drawPath(DrawHandler drawHandler, Path2D.Float r6, int i2, int i3) {
        return doProcessPath(new DrawProcessHandler(drawHandler, noopEndSubPathHandler), r6, i2, i3);
    }

    private static float CLIP(float f2, float f3, float f4, float f5, double d2) {
        return (float) (f3 + (((d2 - f2) * (f5 - f3)) / (f4 - f2)));
    }

    private static int CLIP(int i2, int i3, int i4, int i5, double d2) {
        return (int) (i3 + (((d2 - i2) * (i5 - i3)) / (i4 - i2)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean IS_CLIPPED(int i2) {
        return i2 == 0 || i2 == 1;
    }

    private static int TESTANDCLIP(float f2, float f3, float[] fArr, int i2, int i3, int i4, int i5) {
        double d2;
        int i6 = 3;
        if (fArr[i2] < f2 || fArr[i2] > f3) {
            if (fArr[i2] < f2) {
                if (fArr[i4] < f2) {
                    return 4;
                }
                i6 = 0;
                d2 = f2;
            } else {
                if (fArr[i4] > f3) {
                    return 4;
                }
                i6 = 1;
                d2 = f3;
            }
            fArr[i3] = CLIP(fArr[i2], fArr[i3], fArr[i4], fArr[i5], d2);
            fArr[i2] = (float) d2;
        }
        return i6;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int TESTANDCLIP(int i2, int i3, int[] iArr, int i4, int i5, int i6, int i7) {
        double d2;
        int i8 = 3;
        if (iArr[i4] < i2 || iArr[i4] > i3) {
            if (iArr[i4] < i2) {
                if (iArr[i6] < i2) {
                    return 4;
                }
                i8 = 0;
                d2 = i2;
            } else {
                if (iArr[i6] > i3) {
                    return 4;
                }
                i8 = 1;
                d2 = i3;
            }
            iArr[i5] = CLIP(iArr[i4], iArr[i5], iArr[i6], iArr[i7], d2);
            iArr[i4] = (int) d2;
        }
        return i8;
    }

    private static int CLIPCLAMP(float f2, float f3, float[] fArr, int i2, int i3, int i4, int i5, int i6, int i7) {
        fArr[i6] = fArr[i2];
        fArr[i7] = fArr[i3];
        int iTESTANDCLIP = TESTANDCLIP(f2, f3, fArr, i2, i3, i4, i5);
        if (iTESTANDCLIP == 0) {
            fArr[i6] = fArr[i2];
        } else if (iTESTANDCLIP == 1) {
            fArr[i6] = fArr[i2];
            iTESTANDCLIP = 1;
        } else if (iTESTANDCLIP == 4) {
            if (fArr[i2] > f3) {
                iTESTANDCLIP = 4;
            } else {
                fArr[i2] = f2;
                fArr[i4] = f2;
                iTESTANDCLIP = 3;
            }
        }
        return iTESTANDCLIP;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int CLIPCLAMP(int i2, int i3, int[] iArr, int i4, int i5, int i6, int i7, int i8, int i9) {
        iArr[i8] = iArr[i4];
        iArr[i9] = iArr[i5];
        int iTESTANDCLIP = TESTANDCLIP(i2, i3, iArr, i4, i5, i6, i7);
        if (iTESTANDCLIP == 0) {
            iArr[i8] = iArr[i4];
        } else if (iTESTANDCLIP == 1) {
            iArr[i8] = iArr[i4];
            iTESTANDCLIP = 1;
        } else if (iTESTANDCLIP == 4) {
            if (iArr[i4] > i3) {
                iTESTANDCLIP = 4;
            } else {
                iArr[i4] = i2;
                iArr[i6] = i2;
                iTESTANDCLIP = 3;
            }
        }
        return iTESTANDCLIP;
    }

    /* loaded from: rt.jar:sun/java2d/loops/ProcessPath$DrawProcessHandler.class */
    private static class DrawProcessHandler extends ProcessHandler {
        EndSubPathHandler processESP;

        public DrawProcessHandler(DrawHandler drawHandler, EndSubPathHandler endSubPathHandler) {
            super(drawHandler, 0);
            this.dhnd = drawHandler;
            this.processESP = endSubPathHandler;
        }

        @Override // sun.java2d.loops.ProcessPath.EndSubPathHandler
        public void processEndSubPath() {
            this.processESP.processEndSubPath();
        }

        void PROCESS_LINE(int i2, int i3, int i4, int i5, boolean z2, int[] iArr) {
            int i6 = i2 >> 10;
            int i7 = i3 >> 10;
            int i8 = i4 >> 10;
            int i9 = i5 >> 10;
            if (((i6 ^ i8) | (i7 ^ i9)) == 0) {
                if (z2 && (this.dhnd.yMin > i7 || this.dhnd.yMax <= i7 || this.dhnd.xMin > i6 || this.dhnd.xMax <= i6)) {
                    return;
                }
                if (iArr[0] == 0) {
                    iArr[0] = 1;
                    iArr[1] = i6;
                    iArr[2] = i7;
                    iArr[3] = i6;
                    iArr[4] = i7;
                    this.dhnd.drawPixel(i6, i7);
                    return;
                }
                if (i6 == iArr[3] && i7 == iArr[4]) {
                    return;
                }
                if (i6 != iArr[1] || i7 != iArr[2]) {
                    this.dhnd.drawPixel(i6, i7);
                    iArr[3] = i6;
                    iArr[4] = i7;
                    return;
                }
                return;
            }
            if ((!z2 || (this.dhnd.yMin <= i7 && this.dhnd.yMax > i7 && this.dhnd.xMin <= i6 && this.dhnd.xMax > i6)) && iArr[0] == 1 && ((iArr[1] == i6 && iArr[2] == i7) || (iArr[3] == i6 && iArr[4] == i7))) {
                this.dhnd.drawPixel(i6, i7);
            }
            this.dhnd.drawLine(i6, i7, i8, i9);
            if (iArr[0] == 0) {
                iArr[0] = 1;
                iArr[1] = i6;
                iArr[2] = i7;
                iArr[3] = i6;
                iArr[4] = i7;
            }
            if ((iArr[1] == i8 && iArr[2] == i9) || (iArr[3] == i8 && iArr[4] == i9)) {
                if (z2 && (this.dhnd.yMin > i9 || this.dhnd.yMax <= i9 || this.dhnd.xMin > i8 || this.dhnd.xMax <= i8)) {
                    return;
                } else {
                    this.dhnd.drawPixel(i8, i9);
                }
            }
            iArr[3] = i8;
            iArr[4] = i9;
        }

        void PROCESS_POINT(int i2, int i3, boolean z2, int[] iArr) {
            int i4 = i2 >> 10;
            int i5 = i3 >> 10;
            if (z2 && (this.dhnd.yMin > i5 || this.dhnd.yMax <= i5 || this.dhnd.xMin > i4 || this.dhnd.xMax <= i4)) {
                return;
            }
            if (iArr[0] == 0) {
                iArr[0] = 1;
                iArr[1] = i4;
                iArr[2] = i5;
                iArr[3] = i4;
                iArr[4] = i5;
                this.dhnd.drawPixel(i4, i5);
                return;
            }
            if (i4 == iArr[3] && i5 == iArr[4]) {
                return;
            }
            if (i4 != iArr[1] || i5 != iArr[2]) {
                this.dhnd.drawPixel(i4, i5);
                iArr[3] = i4;
                iArr[4] = i5;
            }
        }

        @Override // sun.java2d.loops.ProcessPath.ProcessHandler
        public void processFixedLine(int i2, int i3, int i4, int i5, int[] iArr, boolean z2, boolean z3) {
            int i6;
            int i7;
            int i8;
            int i9;
            int i10 = (i2 ^ i4) | (i3 ^ i5);
            if ((i10 & ProcessPath.MDP_W_MASK) == 0) {
                if (i10 == 0) {
                    PROCESS_POINT(i2 + 512, i3 + 512, z2, iArr);
                    return;
                }
                return;
            }
            if (i2 == i4 || i3 == i5) {
                i6 = i2 + 512;
                i7 = i4 + 512;
                i8 = i3 + 512;
                i9 = i5 + 512;
            } else {
                int i11 = i4 - i2;
                int i12 = i5 - i3;
                int i13 = i2 & ProcessPath.MDP_W_MASK;
                int i14 = i3 & ProcessPath.MDP_W_MASK;
                int i15 = i4 & ProcessPath.MDP_W_MASK;
                int i16 = i5 & ProcessPath.MDP_W_MASK;
                if (i13 == i2 || i14 == i3) {
                    i6 = i2 + 512;
                    i8 = i3 + 512;
                } else {
                    int i17 = i2 < i4 ? i13 + 1024 : i13;
                    int i18 = i3 < i5 ? i14 + 1024 : i14;
                    int i19 = i3 + (((i17 - i2) * i12) / i11);
                    if (i19 >= i14 && i19 <= i14 + 1024) {
                        i6 = i17;
                        i8 = i19 + 512;
                    } else {
                        i6 = i2 + (((i18 - i3) * i11) / i12) + 512;
                        i8 = i18;
                    }
                }
                if (i15 == i4 || i16 == i5) {
                    i7 = i4 + 512;
                    i9 = i5 + 512;
                } else {
                    int i20 = i2 > i4 ? i15 + 1024 : i15;
                    int i21 = i3 > i5 ? i16 + 1024 : i16;
                    int i22 = i5 + (((i20 - i4) * i12) / i11);
                    if (i22 >= i16 && i22 <= i16 + 1024) {
                        i7 = i20;
                        i9 = i22 + 512;
                    } else {
                        i7 = i4 + (((i21 - i5) * i11) / i12) + 512;
                        i9 = i21;
                    }
                }
            }
            PROCESS_LINE(i6, i8, i7, i9, z2, iArr);
        }
    }

    private static void DrawMonotonicQuad(ProcessHandler processHandler, float[] fArr, boolean z2, int[] iArr) {
        int i2 = (int) (fArr[0] * 1024.0f);
        int i3 = (int) (fArr[1] * 1024.0f);
        int i4 = (int) (fArr[4] * 1024.0f);
        int i5 = (int) (fArr[5] * 1024.0f);
        int i6 = (i2 & 1023) << 1;
        int i7 = (i3 & 1023) << 1;
        int i8 = 4;
        int i9 = 1;
        int i10 = (int) (((fArr[0] - (2.0f * fArr[2])) + fArr[4]) * 128.0f);
        int i11 = (int) (((fArr[1] - (2.0f * fArr[3])) + fArr[5]) * 128.0f);
        int i12 = 2 * i10;
        int i13 = 2 * i11;
        int i14 = i10 + ((int) ((((-2.0f) * fArr[0]) + (2.0f * fArr[2])) * 512.0f));
        int i15 = i11 + ((int) ((((-2.0f) * fArr[1]) + (2.0f * fArr[3])) * 512.0f));
        int i16 = i2;
        int i17 = i3;
        int iMax = Math.max(Math.abs(i12), Math.abs(i13));
        int i18 = i4 - i2;
        int i19 = i5 - i3;
        int i20 = i2 & MDP_W_MASK;
        int i21 = i3 & MDP_W_MASK;
        while (iMax > 8192) {
            i14 = (i14 << 1) - i10;
            i15 = (i15 << 1) - i11;
            i8 <<= 1;
            iMax >>= 2;
            i6 <<= 2;
            i7 <<= 2;
            i9 += 2;
        }
        while (true) {
            int i22 = i8;
            i8--;
            if (i22 > 1) {
                i6 += i14;
                i7 += i15;
                i14 += i12;
                i15 += i13;
                int i23 = i16;
                int i24 = i17;
                i16 = i20 + (i6 >> i9);
                i17 = i21 + (i7 >> i9);
                if (((i4 - i16) ^ i18) < 0) {
                    i16 = i4;
                }
                if (((i5 - i17) ^ i19) < 0) {
                    i17 = i5;
                }
                processHandler.processFixedLine(i23, i24, i16, i17, iArr, z2, false);
            } else {
                processHandler.processFixedLine(i16, i17, i4, i5, iArr, z2, false);
                return;
            }
        }
    }

    private static void ProcessMonotonicQuad(ProcessHandler processHandler, float[] fArr, int[] iArr) {
        float[] fArr2 = new float[6];
        float f2 = fArr[0];
        float f3 = f2;
        float f4 = f2;
        float f5 = fArr[1];
        float f6 = f5;
        float f7 = f5;
        for (int i2 = 2; i2 < 6; i2 += 2) {
            f4 = f4 > fArr[i2] ? fArr[i2] : f4;
            f3 = f3 < fArr[i2] ? fArr[i2] : f3;
            f7 = f7 > fArr[i2 + 1] ? fArr[i2 + 1] : f7;
            f6 = f6 < fArr[i2 + 1] ? fArr[i2 + 1] : f6;
        }
        if (processHandler.clipMode != 0) {
            if (processHandler.dhnd.yMaxf < f7 || processHandler.dhnd.yMinf > f6 || processHandler.dhnd.xMaxf < f4) {
                return;
            }
            if (processHandler.dhnd.xMinf > f3) {
                float f8 = processHandler.dhnd.xMinf;
                fArr[4] = f8;
                fArr[2] = f8;
                fArr[0] = f8;
            }
        } else if (processHandler.dhnd.xMaxf < f4 || processHandler.dhnd.xMinf > f3 || processHandler.dhnd.yMaxf < f7 || processHandler.dhnd.yMinf > f6) {
            return;
        }
        if (f3 - f4 > 1024.0f || f6 - f7 > 1024.0f) {
            fArr2[4] = fArr[4];
            fArr2[5] = fArr[5];
            fArr2[2] = (fArr[2] + fArr[4]) / 2.0f;
            fArr2[3] = (fArr[3] + fArr[5]) / 2.0f;
            fArr[2] = (fArr[0] + fArr[2]) / 2.0f;
            fArr[3] = (fArr[1] + fArr[3]) / 2.0f;
            float f9 = (fArr[2] + fArr2[2]) / 2.0f;
            fArr2[0] = f9;
            fArr[4] = f9;
            float f10 = (fArr[3] + fArr2[3]) / 2.0f;
            fArr2[1] = f10;
            fArr[5] = f10;
            ProcessMonotonicQuad(processHandler, fArr, iArr);
            ProcessMonotonicQuad(processHandler, fArr2, iArr);
            return;
        }
        DrawMonotonicQuad(processHandler, fArr, processHandler.dhnd.xMinf >= f4 || processHandler.dhnd.xMaxf <= f3 || processHandler.dhnd.yMinf >= f7 || processHandler.dhnd.yMaxf <= f6, iArr);
    }

    private static void ProcessQuad(ProcessHandler processHandler, float[] fArr, int[] iArr) {
        double[] dArr = new double[2];
        int i2 = 0;
        if ((fArr[0] > fArr[2] || fArr[2] > fArr[4]) && (fArr[0] < fArr[2] || fArr[2] < fArr[4])) {
            double d2 = (fArr[0] - (2.0f * fArr[2])) + fArr[4];
            if (d2 != 0.0d) {
                double d3 = (fArr[0] - fArr[2]) / d2;
                if (d3 < 1.0d && d3 > 0.0d) {
                    i2 = 0 + 1;
                    dArr[0] = d3;
                }
            }
        }
        if ((fArr[1] > fArr[3] || fArr[3] > fArr[5]) && (fArr[1] < fArr[3] || fArr[3] < fArr[5])) {
            double d4 = (fArr[1] - (2.0f * fArr[3])) + fArr[5];
            if (d4 != 0.0d) {
                double d5 = (fArr[1] - fArr[3]) / d4;
                if (d5 < 1.0d && d5 > 0.0d) {
                    if (i2 <= 0) {
                        int i3 = i2;
                        i2++;
                        dArr[i3] = d5;
                    } else if (dArr[0] > d5) {
                        int i4 = i2;
                        i2++;
                        dArr[i4] = dArr[0];
                        dArr[0] = d5;
                    } else if (dArr[0] < d5) {
                        int i5 = i2;
                        i2++;
                        dArr[i5] = d5;
                    }
                }
            }
        }
        switch (i2) {
            case 1:
                ProcessFirstMonotonicPartOfQuad(processHandler, fArr, iArr, (float) dArr[0]);
                break;
            case 2:
                ProcessFirstMonotonicPartOfQuad(processHandler, fArr, iArr, (float) dArr[0]);
                double d6 = dArr[1] - dArr[0];
                if (d6 > 0.0d) {
                    ProcessFirstMonotonicPartOfQuad(processHandler, fArr, iArr, (float) (d6 / (1.0d - dArr[0])));
                    break;
                }
                break;
        }
        ProcessMonotonicQuad(processHandler, fArr, iArr);
    }

    private static void ProcessFirstMonotonicPartOfQuad(ProcessHandler processHandler, float[] fArr, int[] iArr, float f2) {
        fArr[2] = fArr[2] + (f2 * (fArr[4] - fArr[2]));
        fArr[3] = fArr[3] + (f2 * (fArr[5] - fArr[3]));
        float f3 = fArr[2] + (f2 * (fArr[2] - fArr[2]));
        fArr[0] = f3;
        float f4 = fArr[3] + (f2 * (fArr[3] - fArr[3]));
        float[] fArr2 = {fArr[0], fArr[1], fArr[0] + (f2 * (fArr[2] - fArr[0])), fArr[1] + (f2 * (fArr[3] - fArr[1])), f3, f4};
        fArr[1] = f4;
        ProcessMonotonicQuad(processHandler, fArr2, iArr);
    }

    private static void DrawMonotonicCubic(ProcessHandler processHandler, float[] fArr, boolean z2, int[] iArr) {
        int i2 = (int) (fArr[0] * 1024.0f);
        int i3 = (int) (fArr[1] * 1024.0f);
        int i4 = (int) (fArr[6] * 1024.0f);
        int i5 = (int) (fArr[7] * 1024.0f);
        int i6 = (i2 & 1023) << 6;
        int i7 = (i3 & 1023) << 6;
        int i8 = 32768;
        int i9 = 262144;
        int i10 = 8;
        int i11 = 6;
        int i12 = (int) (((((-fArr[0]) + (3.0f * fArr[2])) - (3.0f * fArr[4])) + fArr[6]) * 128.0f);
        int i13 = (int) (((((-fArr[1]) + (3.0f * fArr[3])) - (3.0f * fArr[5])) + fArr[7]) * 128.0f);
        int i14 = (int) ((((3.0f * fArr[0]) - (6.0f * fArr[2])) + (3.0f * fArr[4])) * 2048.0f);
        int i15 = (int) ((((3.0f * fArr[1]) - (6.0f * fArr[3])) + (3.0f * fArr[5])) * 2048.0f);
        int i16 = 6 * i12;
        int i17 = 6 * i13;
        int i18 = i16 + i14;
        int i19 = i17 + i15;
        int i20 = i12 + (i14 >> 1) + ((int) ((((-3.0f) * fArr[0]) + (3.0f * fArr[2])) * 8192.0f));
        int i21 = i13 + (i15 >> 1) + ((int) ((((-3.0f) * fArr[1]) + (3.0f * fArr[3])) * 8192.0f));
        int i22 = i2;
        int i23 = i3;
        int i24 = i2 & MDP_W_MASK;
        int i25 = i3 & MDP_W_MASK;
        int i26 = i4 - i2;
        int i27 = i5 - i3;
        while (i10 > 0) {
            while (true) {
                if (Math.abs(i18) <= i9 && Math.abs(i19) <= i9) {
                    break;
                }
                i18 = (i18 << 1) - i16;
                i19 = (i19 << 1) - i17;
                i20 = (i20 << 2) - (i18 >> 1);
                i21 = (i21 << 2) - (i19 >> 1);
                i10 <<= 1;
                i9 <<= 3;
                i8 <<= 3;
                i6 <<= 3;
                i7 <<= 3;
                i11 += 3;
            }
            while ((i10 & 1) == 0 && i11 > 6 && Math.abs(i20) <= i8 && Math.abs(i21) <= i8) {
                i20 = (i20 >> 2) + (i18 >> 3);
                i21 = (i21 >> 2) + (i19 >> 3);
                i18 = (i18 + i16) >> 1;
                i19 = (i19 + i17) >> 1;
                i10 >>= 1;
                i9 >>= 3;
                i8 >>= 3;
                i6 >>= 3;
                i7 >>= 3;
                i11 -= 3;
            }
            i10--;
            if (i10 > 0) {
                i6 += i20;
                i7 += i21;
                i20 += i18;
                i21 += i19;
                i18 += i16;
                i19 += i17;
                int i28 = i22;
                int i29 = i23;
                i22 = i24 + (i6 >> i11);
                i23 = i25 + (i7 >> i11);
                if (((i4 - i22) ^ i26) < 0) {
                    i22 = i4;
                }
                if (((i5 - i23) ^ i27) < 0) {
                    i23 = i5;
                }
                processHandler.processFixedLine(i28, i29, i22, i23, iArr, z2, false);
            } else {
                processHandler.processFixedLine(i22, i23, i4, i5, iArr, z2, false);
            }
        }
    }

    private static void ProcessMonotonicCubic(ProcessHandler processHandler, float[] fArr, int[] iArr) {
        float[] fArr2 = new float[8];
        float f2 = fArr[0];
        float f3 = f2;
        float f4 = f2;
        float f5 = fArr[1];
        float f6 = f5;
        float f7 = f5;
        for (int i2 = 2; i2 < 8; i2 += 2) {
            f4 = f4 > fArr[i2] ? fArr[i2] : f4;
            f3 = f3 < fArr[i2] ? fArr[i2] : f3;
            f7 = f7 > fArr[i2 + 1] ? fArr[i2 + 1] : f7;
            f6 = f6 < fArr[i2 + 1] ? fArr[i2 + 1] : f6;
        }
        if (processHandler.clipMode != 0) {
            if (processHandler.dhnd.yMaxf < f7 || processHandler.dhnd.yMinf > f6 || processHandler.dhnd.xMaxf < f4) {
                return;
            }
            if (processHandler.dhnd.xMinf > f3) {
                float f8 = processHandler.dhnd.xMinf;
                fArr[6] = f8;
                fArr[4] = f8;
                fArr[2] = f8;
                fArr[0] = f8;
            }
        } else if (processHandler.dhnd.xMaxf < f4 || processHandler.dhnd.xMinf > f3 || processHandler.dhnd.yMaxf < f7 || processHandler.dhnd.yMinf > f6) {
            return;
        }
        if (f3 - f4 > 256.0f || f6 - f7 > 256.0f) {
            fArr2[6] = fArr[6];
            fArr2[7] = fArr[7];
            fArr2[4] = (fArr[4] + fArr[6]) / 2.0f;
            fArr2[5] = (fArr[5] + fArr[7]) / 2.0f;
            float f9 = (fArr[2] + fArr[4]) / 2.0f;
            float f10 = (fArr[3] + fArr[5]) / 2.0f;
            fArr2[2] = (f9 + fArr2[4]) / 2.0f;
            fArr2[3] = (f10 + fArr2[5]) / 2.0f;
            fArr[2] = (fArr[0] + fArr[2]) / 2.0f;
            fArr[3] = (fArr[1] + fArr[3]) / 2.0f;
            fArr[4] = (fArr[2] + f9) / 2.0f;
            fArr[5] = (fArr[3] + f10) / 2.0f;
            float f11 = (fArr[4] + fArr2[2]) / 2.0f;
            fArr2[0] = f11;
            fArr[6] = f11;
            float f12 = (fArr[5] + fArr2[3]) / 2.0f;
            fArr2[1] = f12;
            fArr[7] = f12;
            ProcessMonotonicCubic(processHandler, fArr, iArr);
            ProcessMonotonicCubic(processHandler, fArr2, iArr);
            return;
        }
        DrawMonotonicCubic(processHandler, fArr, processHandler.dhnd.xMinf > f4 || processHandler.dhnd.xMaxf < f3 || processHandler.dhnd.yMinf > f7 || processHandler.dhnd.yMaxf < f6, iArr);
    }

    private static void ProcessCubic(ProcessHandler processHandler, float[] fArr, int[] iArr) {
        double[] dArr = new double[4];
        double[] dArr2 = new double[3];
        double[] dArr3 = new double[2];
        int i2 = 0;
        if ((fArr[0] > fArr[2] || fArr[2] > fArr[4] || fArr[4] > fArr[6]) && (fArr[0] < fArr[2] || fArr[2] < fArr[4] || fArr[4] < fArr[6])) {
            dArr2[2] = (((-fArr[0]) + (3.0f * fArr[2])) - (3.0f * fArr[4])) + fArr[6];
            dArr2[1] = 2.0f * ((fArr[0] - (2.0f * fArr[2])) + fArr[4]);
            dArr2[0] = (-fArr[0]) + fArr[2];
            int iSolveQuadratic = QuadCurve2D.solveQuadratic(dArr2, dArr3);
            for (int i3 = 0; i3 < iSolveQuadratic; i3++) {
                if (dArr3[i3] > 0.0d && dArr3[i3] < 1.0d) {
                    int i4 = i2;
                    i2++;
                    dArr[i4] = dArr3[i3];
                }
            }
        }
        if ((fArr[1] > fArr[3] || fArr[3] > fArr[5] || fArr[5] > fArr[7]) && (fArr[1] < fArr[3] || fArr[3] < fArr[5] || fArr[5] < fArr[7])) {
            dArr2[2] = (((-fArr[1]) + (3.0f * fArr[3])) - (3.0f * fArr[5])) + fArr[7];
            dArr2[1] = 2.0f * ((fArr[1] - (2.0f * fArr[3])) + fArr[5]);
            dArr2[0] = (-fArr[1]) + fArr[3];
            int iSolveQuadratic2 = QuadCurve2D.solveQuadratic(dArr2, dArr3);
            for (int i5 = 0; i5 < iSolveQuadratic2; i5++) {
                if (dArr3[i5] > 0.0d && dArr3[i5] < 1.0d) {
                    int i6 = i2;
                    i2++;
                    dArr[i6] = dArr3[i5];
                }
            }
        }
        if (i2 > 0) {
            Arrays.sort(dArr, 0, i2);
            ProcessFirstMonotonicPartOfCubic(processHandler, fArr, iArr, (float) dArr[0]);
            for (int i7 = 1; i7 < i2; i7++) {
                double d2 = dArr[i7] - dArr[i7 - 1];
                if (d2 > 0.0d) {
                    ProcessFirstMonotonicPartOfCubic(processHandler, fArr, iArr, (float) (d2 / (1.0d - dArr[i7 - 1])));
                }
            }
        }
        ProcessMonotonicCubic(processHandler, fArr, iArr);
    }

    private static void ProcessFirstMonotonicPartOfCubic(ProcessHandler processHandler, float[] fArr, int[] iArr, float f2) {
        float[] fArr2 = new float[8];
        fArr2[0] = fArr[0];
        fArr2[1] = fArr[1];
        float f3 = fArr[2] + (f2 * (fArr[4] - fArr[2]));
        float f4 = fArr[3] + (f2 * (fArr[5] - fArr[3]));
        fArr2[2] = fArr[0] + (f2 * (fArr[2] - fArr[0]));
        fArr2[3] = fArr[1] + (f2 * (fArr[3] - fArr[1]));
        fArr2[4] = fArr2[2] + (f2 * (f3 - fArr2[2]));
        fArr2[5] = fArr2[3] + (f2 * (f4 - fArr2[3]));
        fArr[4] = fArr[4] + (f2 * (fArr[6] - fArr[4]));
        fArr[5] = fArr[5] + (f2 * (fArr[7] - fArr[5]));
        fArr[2] = f3 + (f2 * (fArr[4] - f3));
        fArr[3] = f4 + (f2 * (fArr[5] - f4));
        float f5 = fArr2[4] + (f2 * (fArr[2] - fArr2[4]));
        fArr2[6] = f5;
        fArr[0] = f5;
        float f6 = fArr2[5] + (f2 * (fArr[3] - fArr2[5]));
        fArr2[7] = f6;
        fArr[1] = f6;
        ProcessMonotonicCubic(processHandler, fArr2, iArr);
    }

    private static void ProcessLine(ProcessHandler processHandler, float f2, float f3, float f4, float f5, int[] iArr) {
        float[] fArr = {f2, f3, f4, f5, 0.0f, 0.0f};
        float f6 = processHandler.dhnd.xMinf;
        float f7 = processHandler.dhnd.yMinf;
        float f8 = processHandler.dhnd.xMaxf;
        float f9 = processHandler.dhnd.yMaxf;
        int iTESTANDCLIP = TESTANDCLIP(f7, f9, fArr, 1, 0, 3, 2);
        if (iTESTANDCLIP == 4) {
            return;
        }
        boolean zIS_CLIPPED = IS_CLIPPED(iTESTANDCLIP);
        int iTESTANDCLIP2 = TESTANDCLIP(f7, f9, fArr, 3, 2, 1, 0);
        if (iTESTANDCLIP2 == 4) {
            return;
        }
        boolean zIS_CLIPPED2 = IS_CLIPPED(iTESTANDCLIP2);
        boolean z2 = zIS_CLIPPED || zIS_CLIPPED2;
        if (processHandler.clipMode == 0) {
            int iTESTANDCLIP3 = TESTANDCLIP(f6, f8, fArr, 0, 1, 2, 3);
            if (iTESTANDCLIP3 == 4) {
                return;
            }
            boolean z3 = z2 || IS_CLIPPED(iTESTANDCLIP3);
            int iTESTANDCLIP4 = TESTANDCLIP(f6, f8, fArr, 2, 3, 0, 1);
            if (iTESTANDCLIP4 == 4) {
                return;
            }
            boolean z4 = zIS_CLIPPED2 || IS_CLIPPED(iTESTANDCLIP4);
            processHandler.processFixedLine((int) (fArr[0] * 1024.0f), (int) (fArr[1] * 1024.0f), (int) (fArr[2] * 1024.0f), (int) (fArr[3] * 1024.0f), iArr, z3 || z4, z4);
            return;
        }
        int iCLIPCLAMP = CLIPCLAMP(f6, f8, fArr, 0, 1, 2, 3, 4, 5);
        int i2 = (int) (fArr[0] * 1024.0f);
        int i3 = (int) (fArr[1] * 1024.0f);
        if (iCLIPCLAMP == 0) {
            processHandler.processFixedLine((int) (fArr[4] * 1024.0f), (int) (fArr[5] * 1024.0f), i2, i3, iArr, false, zIS_CLIPPED2);
        } else if (iCLIPCLAMP == 4) {
            return;
        }
        int iCLIPCLAMP2 = CLIPCLAMP(f6, f8, fArr, 2, 3, 0, 1, 4, 5);
        boolean z5 = zIS_CLIPPED2 || iCLIPCLAMP2 == 1;
        int i4 = (int) (fArr[2] * 1024.0f);
        int i5 = (int) (fArr[3] * 1024.0f);
        processHandler.processFixedLine(i2, i3, i4, i5, iArr, false, z5);
        if (iCLIPCLAMP2 == 0) {
            processHandler.processFixedLine(i4, i5, (int) (fArr[4] * 1024.0f), (int) (fArr[5] * 1024.0f), iArr, false, z5);
        }
    }

    private static boolean doProcessPath(ProcessHandler processHandler, Path2D.Float r8, float f2, float f3) {
        float[] fArr = new float[8];
        float[] fArr2 = new float[8];
        float[] fArr3 = {0.0f, 0.0f};
        float[] fArr4 = new float[2];
        int[] iArr = new int[5];
        boolean z2 = false;
        boolean z3 = false;
        iArr[0] = 0;
        processHandler.dhnd.adjustBounds(LOWER_OUT_BND, LOWER_OUT_BND, 1048576, 1048576);
        if (processHandler.dhnd.strokeControl == 2) {
            fArr3[0] = -0.5f;
            fArr3[1] = -0.5f;
            f2 = (float) (f2 - 0.5d);
            f3 = (float) (f3 - 0.5d);
        }
        PathIterator pathIterator = r8.getPathIterator(null);
        while (!pathIterator.isDone()) {
            switch (pathIterator.currentSegment(fArr)) {
                case 0:
                    if (z2 && !z3) {
                        if (processHandler.clipMode == 1 && (fArr2[0] != fArr3[0] || fArr2[1] != fArr3[1])) {
                            ProcessLine(processHandler, fArr2[0], fArr2[1], fArr3[0], fArr3[1], iArr);
                        }
                        processHandler.processEndSubPath();
                    }
                    fArr2[0] = fArr[0] + f2;
                    fArr2[1] = fArr[1] + f3;
                    if (fArr2[0] < UPPER_BND && fArr2[0] > LOWER_BND && fArr2[1] < UPPER_BND && fArr2[1] > LOWER_BND) {
                        z2 = true;
                        z3 = false;
                        fArr3[0] = fArr2[0];
                        fArr3[1] = fArr2[1];
                    } else {
                        z3 = true;
                    }
                    iArr[0] = 0;
                    break;
                case 1:
                    float f4 = fArr[0] + f2;
                    fArr2[2] = f4;
                    float f5 = fArr[1] + f3;
                    fArr2[3] = f5;
                    if (f4 < UPPER_BND && f4 > LOWER_BND && f5 < UPPER_BND && f5 > LOWER_BND) {
                        if (z3) {
                            fArr3[0] = f4;
                            fArr2[0] = f4;
                            fArr3[1] = f5;
                            fArr2[1] = f5;
                            z2 = true;
                            z3 = false;
                            break;
                        } else {
                            ProcessLine(processHandler, fArr2[0], fArr2[1], fArr2[2], fArr2[3], iArr);
                            fArr2[0] = f4;
                            fArr2[1] = f5;
                            break;
                        }
                    } else {
                        break;
                    }
                    break;
                case 2:
                    fArr2[2] = fArr[0] + f2;
                    fArr2[3] = fArr[1] + f3;
                    float f6 = fArr[2] + f2;
                    fArr2[4] = f6;
                    float f7 = fArr[3] + f3;
                    fArr2[5] = f7;
                    if (f6 < UPPER_BND && f6 > LOWER_BND && f7 < UPPER_BND && f7 > LOWER_BND) {
                        if (z3) {
                            fArr3[0] = f6;
                            fArr2[0] = f6;
                            fArr3[1] = f7;
                            fArr2[1] = f7;
                            z2 = true;
                            z3 = false;
                            break;
                        } else {
                            if (fArr2[2] < UPPER_BND && fArr2[2] > LOWER_BND && fArr2[3] < UPPER_BND && fArr2[3] > LOWER_BND) {
                                ProcessQuad(processHandler, fArr2, iArr);
                            } else {
                                ProcessLine(processHandler, fArr2[0], fArr2[1], fArr2[4], fArr2[5], iArr);
                            }
                            fArr2[0] = f6;
                            fArr2[1] = f7;
                            break;
                        }
                    } else {
                        break;
                    }
                case 3:
                    fArr2[2] = fArr[0] + f2;
                    fArr2[3] = fArr[1] + f3;
                    fArr2[4] = fArr[2] + f2;
                    fArr2[5] = fArr[3] + f3;
                    float f8 = fArr[4] + f2;
                    fArr2[6] = f8;
                    float f9 = fArr[5] + f3;
                    fArr2[7] = f9;
                    if (f8 < UPPER_BND && f8 > LOWER_BND && f9 < UPPER_BND && f9 > LOWER_BND) {
                        if (z3) {
                            float f10 = fArr2[6];
                            fArr3[0] = f10;
                            fArr2[0] = f10;
                            float f11 = fArr2[7];
                            fArr3[1] = f11;
                            fArr2[1] = f11;
                            z2 = true;
                            z3 = false;
                            break;
                        } else {
                            if (fArr2[2] < UPPER_BND && fArr2[2] > LOWER_BND && fArr2[3] < UPPER_BND && fArr2[3] > LOWER_BND && fArr2[4] < UPPER_BND && fArr2[4] > LOWER_BND && fArr2[5] < UPPER_BND && fArr2[5] > LOWER_BND) {
                                ProcessCubic(processHandler, fArr2, iArr);
                            } else {
                                ProcessLine(processHandler, fArr2[0], fArr2[1], fArr2[6], fArr2[7], iArr);
                            }
                            fArr2[0] = f8;
                            fArr2[1] = f9;
                            break;
                        }
                    } else {
                        break;
                    }
                    break;
                case 4:
                    if (z2 && !z3) {
                        z3 = false;
                        if (fArr2[0] != fArr3[0] || fArr2[1] != fArr3[1]) {
                            ProcessLine(processHandler, fArr2[0], fArr2[1], fArr3[0], fArr3[1], iArr);
                            fArr2[0] = fArr3[0];
                            fArr2[1] = fArr3[1];
                        }
                        processHandler.processEndSubPath();
                        break;
                    } else {
                        break;
                    }
                    break;
            }
            pathIterator.next();
        }
        if (z2 & (!z3)) {
            if (processHandler.clipMode == 1 && (fArr2[0] != fArr3[0] || fArr2[1] != fArr3[1])) {
                ProcessLine(processHandler, fArr2[0], fArr2[1], fArr3[0], fArr3[1], iArr);
            }
            processHandler.processEndSubPath();
            return true;
        }
        return true;
    }

    /* loaded from: rt.jar:sun/java2d/loops/ProcessPath$Point.class */
    private static class Point {

        /* renamed from: x, reason: collision with root package name */
        public int f13560x;

        /* renamed from: y, reason: collision with root package name */
        public int f13561y;
        public boolean lastPoint;
        public Point prev;
        public Point next;
        public Point nextByY;
        public Edge edge;

        public Point(int i2, int i3, boolean z2) {
            this.f13560x = i2;
            this.f13561y = i3;
            this.lastPoint = z2;
        }
    }

    /* loaded from: rt.jar:sun/java2d/loops/ProcessPath$Edge.class */
    private static class Edge {

        /* renamed from: x, reason: collision with root package name */
        int f13558x;
        int dx;

        /* renamed from: p, reason: collision with root package name */
        Point f13559p;
        int dir;
        Edge prev;
        Edge next;

        public Edge(Point point, int i2, int i3, int i4) {
            this.f13559p = point;
            this.f13558x = i2;
            this.dx = i3;
            this.dir = i4;
        }
    }

    /* loaded from: rt.jar:sun/java2d/loops/ProcessPath$FillData.class */
    private static class FillData {
        List<Point> plgPnts = new Vector(256);
        public int plgYMin;
        public int plgYMax;

        public void addPoint(int i2, int i3, boolean z2) {
            if (this.plgPnts.size() == 0) {
                this.plgYMax = i3;
                this.plgYMin = i3;
            } else {
                this.plgYMin = this.plgYMin > i3 ? i3 : this.plgYMin;
                this.plgYMax = this.plgYMax < i3 ? i3 : this.plgYMax;
            }
            this.plgPnts.add(new Point(i2, i3, z2));
        }

        public boolean isEmpty() {
            return this.plgPnts.size() == 0;
        }

        public boolean isEnded() {
            return this.plgPnts.get(this.plgPnts.size() - 1).lastPoint;
        }

        public boolean setEnded() {
            this.plgPnts.get(this.plgPnts.size() - 1).lastPoint = true;
            return true;
        }
    }

    /* loaded from: rt.jar:sun/java2d/loops/ProcessPath$ActiveEdgeList.class */
    private static class ActiveEdgeList {
        Edge head;

        private ActiveEdgeList() {
        }

        public boolean isEmpty() {
            return this.head == null;
        }

        public void insert(Point point, int i2) {
            int i3;
            int i4;
            int i5;
            int i6;
            int i7;
            Point point2 = point.next;
            int i8 = point.f13560x;
            int i9 = point.f13561y;
            int i10 = point2.f13560x;
            int i11 = point2.f13561y;
            if (i9 == i11) {
                return;
            }
            int i12 = i10 - i8;
            int i13 = i11 - i9;
            if (i9 < i11) {
                i3 = i8;
                i4 = i2 - i9;
                i5 = -1;
            } else {
                i3 = i10;
                i4 = i2 - i11;
                i5 = 1;
            }
            if (i12 > ProcessPath.CALC_UBND || i12 < ProcessPath.CALC_LBND) {
                i6 = (int) ((i12 * 1024.0d) / i13);
                i7 = i3 + ((int) ((i12 * i4) / i13));
            } else {
                i6 = (i12 << 10) / i13;
                i7 = i3 + ((i12 * i4) / i13);
            }
            Edge edge = new Edge(point, i7, i6, i5);
            edge.next = this.head;
            edge.prev = null;
            if (this.head != null) {
                this.head.prev = edge;
            }
            point.edge = edge;
            this.head = edge;
        }

        public void delete(Edge edge) {
            Edge edge2 = edge.prev;
            Edge edge3 = edge.next;
            if (edge2 != null) {
                edge2.next = edge3;
            } else {
                this.head = edge3;
            }
            if (edge3 != null) {
                edge3.prev = edge2;
            }
        }

        public void sort() {
            Edge edge = null;
            boolean z2 = true;
            while (edge != this.head.next && z2) {
                Edge edge2 = this.head;
                Edge edge3 = edge2;
                Edge edge4 = edge2;
                Edge edge5 = edge3.next;
                z2 = false;
                while (edge3 != edge) {
                    if (edge3.f13558x >= edge5.f13558x) {
                        z2 = true;
                        if (edge3 == this.head) {
                            Edge edge6 = edge5.next;
                            edge5.next = edge3;
                            edge3.next = edge6;
                            this.head = edge5;
                            edge4 = edge5;
                        } else {
                            Edge edge7 = edge5.next;
                            edge5.next = edge3;
                            edge3.next = edge7;
                            edge4.next = edge5;
                            edge4 = edge5;
                        }
                    } else {
                        edge4 = edge3;
                        edge3 = edge3.next;
                    }
                    edge5 = edge3.next;
                    if (edge5 == edge) {
                        edge = edge3;
                    }
                }
            }
            Edge edge8 = null;
            for (Edge edge9 = this.head; edge9 != null; edge9 = edge9.next) {
                edge9.prev = edge8;
                edge8 = edge9;
            }
        }
    }

    private static void FillPolygon(FillProcessHandler fillProcessHandler, int i2) {
        int i3 = fillProcessHandler.dhnd.xMax - 1;
        FillData fillData = fillProcessHandler.fd;
        int i4 = fillData.plgYMin;
        int i5 = fillData.plgYMax;
        int i6 = ((i5 - i4) >> 10) + 4;
        int i7 = (i4 - 1) & MDP_W_MASK;
        int i8 = i2 == 1 ? -1 : 1;
        List<Point> list = fillData.plgPnts;
        int size = list.size();
        if (size <= 1) {
            return;
        }
        Point[] pointArr = new Point[i6];
        list.get(0).prev = null;
        for (int i9 = 0; i9 < size - 1; i9++) {
            Point point = list.get(i9);
            Point point2 = list.get(i9 + 1);
            int i10 = ((point.f13561y - i7) - 1) >> 10;
            point.nextByY = pointArr[i10];
            pointArr[i10] = point;
            point.next = point2;
            point2.prev = point;
        }
        Point point3 = list.get(size - 1);
        int i11 = ((point3.f13561y - i7) - 1) >> 10;
        point3.nextByY = pointArr[i11];
        pointArr[i11] = point3;
        ActiveEdgeList activeEdgeList = new ActiveEdgeList();
        int i12 = i7 + 1024;
        for (int i13 = 0; i12 <= i5 && i13 < i6; i13++) {
            Point point4 = pointArr[i13];
            while (true) {
                Point point5 = point4;
                if (point5 == null) {
                    break;
                }
                if (point5.prev != null && !point5.prev.lastPoint) {
                    if (point5.prev.edge != null && point5.prev.f13561y <= i12) {
                        activeEdgeList.delete(point5.prev.edge);
                        point5.prev.edge = null;
                    } else if (point5.prev.f13561y > i12) {
                        activeEdgeList.insert(point5.prev, i12);
                    }
                }
                if (!point5.lastPoint && point5.next != null) {
                    if (point5.edge != null && point5.next.f13561y <= i12) {
                        activeEdgeList.delete(point5.edge);
                        point5.edge = null;
                    } else if (point5.next.f13561y > i12) {
                        activeEdgeList.insert(point5, i12);
                    }
                }
                point4 = point5.nextByY;
            }
            if (!activeEdgeList.isEmpty()) {
                activeEdgeList.sort();
                int i14 = 0;
                boolean z2 = false;
                int i15 = fillProcessHandler.dhnd.xMin;
                Edge edge = activeEdgeList.head;
                while (true) {
                    Edge edge2 = edge;
                    if (edge2 == null) {
                        break;
                    }
                    i14 += edge2.dir;
                    if ((i14 & i8) != 0 && !z2) {
                        i15 = ((edge2.f13558x + 1024) - 1) >> 10;
                        z2 = true;
                    }
                    if ((i14 & i8) == 0 && z2) {
                        int i16 = (edge2.f13558x - 1) >> 10;
                        if (i15 <= i16) {
                            fillProcessHandler.dhnd.drawScanline(i15, i16, i12 >> 10);
                        }
                        z2 = false;
                    }
                    edge2.f13558x += edge2.dx;
                    edge = edge2.next;
                }
                if (z2 && i15 <= i3) {
                    fillProcessHandler.dhnd.drawScanline(i15, i3, i12 >> 10);
                }
            }
            i12 += 1024;
        }
    }

    /* loaded from: rt.jar:sun/java2d/loops/ProcessPath$FillProcessHandler.class */
    private static class FillProcessHandler extends ProcessHandler {
        FillData fd;

        @Override // sun.java2d.loops.ProcessPath.ProcessHandler
        public void processFixedLine(int i2, int i3, int i4, int i5, int[] iArr, boolean z2, boolean z3) {
            int iTESTANDCLIP;
            if (z2) {
                int[] iArr2 = {i2, i3, i4, i5, 0, 0};
                int i6 = (int) (this.dhnd.xMinf * 1024.0f);
                int i7 = (int) (this.dhnd.xMaxf * 1024.0f);
                int i8 = (int) (this.dhnd.yMinf * 1024.0f);
                int i9 = (int) (this.dhnd.yMaxf * 1024.0f);
                if (ProcessPath.TESTANDCLIP(i8, i9, iArr2, 1, 0, 3, 2) == 4 || (iTESTANDCLIP = ProcessPath.TESTANDCLIP(i8, i9, iArr2, 3, 2, 1, 0)) == 4) {
                    return;
                }
                boolean zIS_CLIPPED = ProcessPath.IS_CLIPPED(iTESTANDCLIP);
                int iCLIPCLAMP = ProcessPath.CLIPCLAMP(i6, i7, iArr2, 0, 1, 2, 3, 4, 5);
                if (iCLIPCLAMP == 0) {
                    processFixedLine(iArr2[4], iArr2[5], iArr2[0], iArr2[1], iArr, false, zIS_CLIPPED);
                } else if (iCLIPCLAMP == 4) {
                    return;
                }
                int iCLIPCLAMP2 = ProcessPath.CLIPCLAMP(i6, i7, iArr2, 2, 3, 0, 1, 4, 5);
                boolean z4 = zIS_CLIPPED || iCLIPCLAMP2 == 1;
                processFixedLine(iArr2[0], iArr2[1], iArr2[2], iArr2[3], iArr, false, z4);
                if (iCLIPCLAMP2 == 0) {
                    processFixedLine(iArr2[2], iArr2[3], iArr2[4], iArr2[5], iArr, false, z4);
                    return;
                }
                return;
            }
            if (this.fd.isEmpty() || this.fd.isEnded()) {
                this.fd.addPoint(i2, i3, false);
            }
            this.fd.addPoint(i4, i5, false);
            if (z3) {
                this.fd.setEnded();
            }
        }

        FillProcessHandler(DrawHandler drawHandler) {
            super(drawHandler, 1);
            this.fd = new FillData();
        }

        @Override // sun.java2d.loops.ProcessPath.EndSubPathHandler
        public void processEndSubPath() {
            if (!this.fd.isEmpty()) {
                this.fd.setEnded();
            }
        }
    }
}
