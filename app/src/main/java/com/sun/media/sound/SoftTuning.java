package com.sun.media.sound;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import javax.sound.midi.Patch;

/* loaded from: rt.jar:com/sun/media/sound/SoftTuning.class */
public final class SoftTuning {
    private String name;
    private final double[] tuning;
    private Patch patch;

    public SoftTuning() {
        this.name = null;
        this.tuning = new double[128];
        this.patch = null;
        this.name = "12-TET";
        for (int i2 = 0; i2 < this.tuning.length; i2++) {
            this.tuning[i2] = i2 * 100;
        }
    }

    public SoftTuning(byte[] bArr) {
        this.name = null;
        this.tuning = new double[128];
        this.patch = null;
        for (int i2 = 0; i2 < this.tuning.length; i2++) {
            this.tuning[i2] = i2 * 100;
        }
        load(bArr);
    }

    public SoftTuning(Patch patch) {
        this.name = null;
        this.tuning = new double[128];
        this.patch = null;
        this.patch = patch;
        this.name = "12-TET";
        for (int i2 = 0; i2 < this.tuning.length; i2++) {
            this.tuning[i2] = i2 * 100;
        }
    }

    public SoftTuning(Patch patch, byte[] bArr) {
        this.name = null;
        this.tuning = new double[128];
        this.patch = null;
        this.patch = patch;
        for (int i2 = 0; i2 < this.tuning.length; i2++) {
            this.tuning[i2] = i2 * 100;
        }
        load(bArr);
    }

    private boolean checksumOK(byte[] bArr) {
        int i2 = bArr[1] & 255;
        for (int i3 = 2; i3 < bArr.length - 2; i3++) {
            i2 ^= bArr[i3] & 255;
        }
        return (bArr[bArr.length - 2] & 255) == (i2 & 127);
    }

    public void load(byte[] bArr) {
        if ((bArr[1] & 255) == 126 || (bArr[1] & 255) == 127) {
            switch (bArr[3] & 255) {
                case 8:
                    switch (bArr[4] & 255) {
                        case 1:
                            try {
                                this.name = new String(bArr, 6, 16, "ascii");
                            } catch (UnsupportedEncodingException e2) {
                                this.name = null;
                            }
                            int i2 = 22;
                            for (int i3 = 0; i3 < 128; i3++) {
                                int i4 = i2;
                                int i5 = i2 + 1;
                                int i6 = bArr[i4] & 255;
                                int i7 = i5 + 1;
                                int i8 = bArr[i5] & 255;
                                i2 = i7 + 1;
                                int i9 = bArr[i7] & 255;
                                if (i6 != 127 || i8 != 127 || i9 != 127) {
                                    this.tuning[i3] = 100.0d * ((((i6 * 16384) + (i8 * 128)) + i9) / 16384.0d);
                                }
                            }
                            break;
                        case 2:
                            int i10 = bArr[6] & 255;
                            int i11 = 7;
                            for (int i12 = 0; i12 < i10; i12++) {
                                int i13 = i11;
                                int i14 = i11 + 1;
                                int i15 = bArr[i13] & 255;
                                int i16 = i14 + 1;
                                int i17 = bArr[i14] & 255;
                                int i18 = i16 + 1;
                                int i19 = bArr[i16] & 255;
                                i11 = i18 + 1;
                                int i20 = bArr[i18] & 255;
                                if (i17 != 127 || i19 != 127 || i20 != 127) {
                                    this.tuning[i15] = 100.0d * ((((i17 * 16384) + (i19 * 128)) + i20) / 16384.0d);
                                }
                            }
                            break;
                        case 4:
                            if (checksumOK(bArr)) {
                                try {
                                    this.name = new String(bArr, 7, 16, "ascii");
                                } catch (UnsupportedEncodingException e3) {
                                    this.name = null;
                                }
                                int i21 = 23;
                                for (int i22 = 0; i22 < 128; i22++) {
                                    int i23 = i21;
                                    int i24 = i21 + 1;
                                    int i25 = bArr[i23] & 255;
                                    int i26 = i24 + 1;
                                    int i27 = bArr[i24] & 255;
                                    i21 = i26 + 1;
                                    int i28 = bArr[i26] & 255;
                                    if (i25 != 127 || i27 != 127 || i28 != 127) {
                                        this.tuning[i22] = 100.0d * ((((i25 * 16384) + (i27 * 128)) + i28) / 16384.0d);
                                    }
                                }
                                break;
                            }
                            break;
                        case 5:
                            if (checksumOK(bArr)) {
                                try {
                                    this.name = new String(bArr, 7, 16, "ascii");
                                } catch (UnsupportedEncodingException e4) {
                                    this.name = null;
                                }
                                int[] iArr = new int[12];
                                for (int i29 = 0; i29 < 12; i29++) {
                                    iArr[i29] = (bArr[i29 + 23] & 255) - 64;
                                }
                                for (int i30 = 0; i30 < this.tuning.length; i30++) {
                                    this.tuning[i30] = (i30 * 100) + iArr[i30 % 12];
                                }
                                break;
                            }
                            break;
                        case 6:
                            if (checksumOK(bArr)) {
                                try {
                                    this.name = new String(bArr, 7, 16, "ascii");
                                } catch (UnsupportedEncodingException e5) {
                                    this.name = null;
                                }
                                double[] dArr = new double[12];
                                for (int i31 = 0; i31 < 12; i31++) {
                                    dArr[i31] = (((((bArr[(i31 * 2) + 23] & 255) * 128) + (bArr[(i31 * 2) + 24] & 255)) / 8192.0d) - 1.0d) * 100.0d;
                                }
                                for (int i32 = 0; i32 < this.tuning.length; i32++) {
                                    this.tuning[i32] = (i32 * 100) + dArr[i32 % 12];
                                }
                                break;
                            }
                            break;
                        case 7:
                            int i33 = bArr[7] & 255;
                            int i34 = 8;
                            for (int i35 = 0; i35 < i33; i35++) {
                                int i36 = i34;
                                int i37 = i34 + 1;
                                int i38 = bArr[i36] & 255;
                                int i39 = i37 + 1;
                                int i40 = bArr[i37] & 255;
                                int i41 = i39 + 1;
                                int i42 = bArr[i39] & 255;
                                i34 = i41 + 1;
                                int i43 = bArr[i41] & 255;
                                if (i40 != 127 || i42 != 127 || i43 != 127) {
                                    this.tuning[i38] = 100.0d * ((((i40 * 16384) + (i42 * 128)) + i43) / 16384.0d);
                                }
                            }
                            break;
                        case 8:
                            int[] iArr2 = new int[12];
                            for (int i44 = 0; i44 < 12; i44++) {
                                iArr2[i44] = (bArr[i44 + 8] & 255) - 64;
                            }
                            for (int i45 = 0; i45 < this.tuning.length; i45++) {
                                this.tuning[i45] = (i45 * 100) + iArr2[i45 % 12];
                            }
                            break;
                        case 9:
                            double[] dArr2 = new double[12];
                            for (int i46 = 0; i46 < 12; i46++) {
                                dArr2[i46] = (((((bArr[(i46 * 2) + 8] & 255) * 128) + (bArr[(i46 * 2) + 9] & 255)) / 8192.0d) - 1.0d) * 100.0d;
                            }
                            for (int i47 = 0; i47 < this.tuning.length; i47++) {
                                this.tuning[i47] = (i47 * 100) + dArr2[i47 % 12];
                            }
                            break;
                    }
            }
        }
    }

    public double[] getTuning() {
        return Arrays.copyOf(this.tuning, this.tuning.length);
    }

    public double getTuning(int i2) {
        return this.tuning[i2];
    }

    public Patch getPatch() {
        return this.patch;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }
}
