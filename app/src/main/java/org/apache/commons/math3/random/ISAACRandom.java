package org.apache.commons.math3.random;

import java.io.Serializable;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/random/ISAACRandom.class */
public class ISAACRandom extends BitsStreamGenerator implements Serializable {
    private static final long serialVersionUID = 7288197941165002400L;
    private static final int SIZE_L = 8;
    private static final int SIZE = 256;
    private static final int H_SIZE = 128;
    private static final int MASK = 1020;
    private static final int GLD_RATIO = -1640531527;
    private final int[] rsl;
    private final int[] mem;
    private int count;
    private int isaacA;
    private int isaacB;
    private int isaacC;
    private final int[] arr;
    private int isaacX;
    private int isaacI;
    private int isaacJ;

    public ISAACRandom() {
        this.rsl = new int[256];
        this.mem = new int[256];
        this.arr = new int[8];
        setSeed(System.currentTimeMillis() + System.identityHashCode(this));
    }

    public ISAACRandom(long seed) {
        this.rsl = new int[256];
        this.mem = new int[256];
        this.arr = new int[8];
        setSeed(seed);
    }

    public ISAACRandom(int[] seed) {
        this.rsl = new int[256];
        this.mem = new int[256];
        this.arr = new int[8];
        setSeed(seed);
    }

    @Override // org.apache.commons.math3.random.BitsStreamGenerator, org.apache.commons.math3.random.RandomGenerator
    public void setSeed(int seed) {
        setSeed(new int[]{seed});
    }

    @Override // org.apache.commons.math3.random.BitsStreamGenerator, org.apache.commons.math3.random.RandomGenerator
    public void setSeed(long seed) {
        setSeed(new int[]{(int) (seed >>> 32), (int) (seed & 4294967295L)});
    }

    @Override // org.apache.commons.math3.random.BitsStreamGenerator, org.apache.commons.math3.random.RandomGenerator
    public void setSeed(int[] seed) {
        if (seed == null) {
            setSeed(System.currentTimeMillis() + System.identityHashCode(this));
            return;
        }
        int seedLen = seed.length;
        int rslLen = this.rsl.length;
        System.arraycopy(seed, 0, this.rsl, 0, FastMath.min(seedLen, rslLen));
        if (seedLen < rslLen) {
            for (int j2 = seedLen; j2 < rslLen; j2++) {
                long k2 = this.rsl[j2 - seedLen];
                this.rsl[j2] = (int) (((1812433253 * (k2 ^ (k2 >> 30))) + j2) & 4294967295L);
            }
        }
        initState();
    }

    @Override // org.apache.commons.math3.random.BitsStreamGenerator
    protected int next(int bits) {
        if (this.count < 0) {
            isaac();
            this.count = 255;
        }
        int[] iArr = this.rsl;
        int i2 = this.count;
        this.count = i2 - 1;
        return iArr[i2] >>> (32 - bits);
    }

    private void isaac() {
        this.isaacI = 0;
        this.isaacJ = 128;
        int i2 = this.isaacB;
        int i3 = this.isaacC + 1;
        this.isaacC = i3;
        this.isaacB = i2 + i3;
        while (this.isaacI < 128) {
            isaac2();
        }
        this.isaacJ = 0;
        while (this.isaacJ < 128) {
            isaac2();
        }
    }

    private void isaac2() {
        this.isaacX = this.mem[this.isaacI];
        this.isaacA ^= this.isaacA << 13;
        int i2 = this.isaacA;
        int[] iArr = this.mem;
        int i3 = this.isaacJ;
        this.isaacJ = i3 + 1;
        this.isaacA = i2 + iArr[i3];
        isaac3();
        this.isaacX = this.mem[this.isaacI];
        this.isaacA ^= this.isaacA >>> 6;
        int i4 = this.isaacA;
        int[] iArr2 = this.mem;
        int i5 = this.isaacJ;
        this.isaacJ = i5 + 1;
        this.isaacA = i4 + iArr2[i5];
        isaac3();
        this.isaacX = this.mem[this.isaacI];
        this.isaacA ^= this.isaacA << 2;
        int i6 = this.isaacA;
        int[] iArr3 = this.mem;
        int i7 = this.isaacJ;
        this.isaacJ = i7 + 1;
        this.isaacA = i6 + iArr3[i7];
        isaac3();
        this.isaacX = this.mem[this.isaacI];
        this.isaacA ^= this.isaacA >>> 16;
        int i8 = this.isaacA;
        int[] iArr4 = this.mem;
        int i9 = this.isaacJ;
        this.isaacJ = i9 + 1;
        this.isaacA = i8 + iArr4[i9];
        isaac3();
    }

    private void isaac3() {
        this.mem[this.isaacI] = this.mem[(this.isaacX & 1020) >> 2] + this.isaacA + this.isaacB;
        this.isaacB = this.mem[((this.mem[this.isaacI] >> 8) & 1020) >> 2] + this.isaacX;
        int[] iArr = this.rsl;
        int i2 = this.isaacI;
        this.isaacI = i2 + 1;
        iArr[i2] = this.isaacB;
    }

    private void initState() {
        this.isaacA = 0;
        this.isaacB = 0;
        this.isaacC = 0;
        for (int j2 = 0; j2 < this.arr.length; j2++) {
            this.arr[j2] = GLD_RATIO;
        }
        for (int j3 = 0; j3 < 4; j3++) {
            shuffle();
        }
        for (int j4 = 0; j4 < 256; j4 += 8) {
            int[] iArr = this.arr;
            iArr[0] = iArr[0] + this.rsl[j4];
            int[] iArr2 = this.arr;
            iArr2[1] = iArr2[1] + this.rsl[j4 + 1];
            int[] iArr3 = this.arr;
            iArr3[2] = iArr3[2] + this.rsl[j4 + 2];
            int[] iArr4 = this.arr;
            iArr4[3] = iArr4[3] + this.rsl[j4 + 3];
            int[] iArr5 = this.arr;
            iArr5[4] = iArr5[4] + this.rsl[j4 + 4];
            int[] iArr6 = this.arr;
            iArr6[5] = iArr6[5] + this.rsl[j4 + 5];
            int[] iArr7 = this.arr;
            iArr7[6] = iArr7[6] + this.rsl[j4 + 6];
            int[] iArr8 = this.arr;
            iArr8[7] = iArr8[7] + this.rsl[j4 + 7];
            shuffle();
            setState(j4);
        }
        for (int j5 = 0; j5 < 256; j5 += 8) {
            int[] iArr9 = this.arr;
            iArr9[0] = iArr9[0] + this.mem[j5];
            int[] iArr10 = this.arr;
            iArr10[1] = iArr10[1] + this.mem[j5 + 1];
            int[] iArr11 = this.arr;
            iArr11[2] = iArr11[2] + this.mem[j5 + 2];
            int[] iArr12 = this.arr;
            iArr12[3] = iArr12[3] + this.mem[j5 + 3];
            int[] iArr13 = this.arr;
            iArr13[4] = iArr13[4] + this.mem[j5 + 4];
            int[] iArr14 = this.arr;
            iArr14[5] = iArr14[5] + this.mem[j5 + 5];
            int[] iArr15 = this.arr;
            iArr15[6] = iArr15[6] + this.mem[j5 + 6];
            int[] iArr16 = this.arr;
            iArr16[7] = iArr16[7] + this.mem[j5 + 7];
            shuffle();
            setState(j5);
        }
        isaac();
        this.count = 255;
        clear();
    }

    private void shuffle() {
        int[] iArr = this.arr;
        iArr[0] = iArr[0] ^ (this.arr[1] << 11);
        int[] iArr2 = this.arr;
        iArr2[3] = iArr2[3] + this.arr[0];
        int[] iArr3 = this.arr;
        iArr3[1] = iArr3[1] + this.arr[2];
        int[] iArr4 = this.arr;
        iArr4[1] = iArr4[1] ^ (this.arr[2] >>> 2);
        int[] iArr5 = this.arr;
        iArr5[4] = iArr5[4] + this.arr[1];
        int[] iArr6 = this.arr;
        iArr6[2] = iArr6[2] + this.arr[3];
        int[] iArr7 = this.arr;
        iArr7[2] = iArr7[2] ^ (this.arr[3] << 8);
        int[] iArr8 = this.arr;
        iArr8[5] = iArr8[5] + this.arr[2];
        int[] iArr9 = this.arr;
        iArr9[3] = iArr9[3] + this.arr[4];
        int[] iArr10 = this.arr;
        iArr10[3] = iArr10[3] ^ (this.arr[4] >>> 16);
        int[] iArr11 = this.arr;
        iArr11[6] = iArr11[6] + this.arr[3];
        int[] iArr12 = this.arr;
        iArr12[4] = iArr12[4] + this.arr[5];
        int[] iArr13 = this.arr;
        iArr13[4] = iArr13[4] ^ (this.arr[5] << 10);
        int[] iArr14 = this.arr;
        iArr14[7] = iArr14[7] + this.arr[4];
        int[] iArr15 = this.arr;
        iArr15[5] = iArr15[5] + this.arr[6];
        int[] iArr16 = this.arr;
        iArr16[5] = iArr16[5] ^ (this.arr[6] >>> 4);
        int[] iArr17 = this.arr;
        iArr17[0] = iArr17[0] + this.arr[5];
        int[] iArr18 = this.arr;
        iArr18[6] = iArr18[6] + this.arr[7];
        int[] iArr19 = this.arr;
        iArr19[6] = iArr19[6] ^ (this.arr[7] << 8);
        int[] iArr20 = this.arr;
        iArr20[1] = iArr20[1] + this.arr[6];
        int[] iArr21 = this.arr;
        iArr21[7] = iArr21[7] + this.arr[0];
        int[] iArr22 = this.arr;
        iArr22[7] = iArr22[7] ^ (this.arr[0] >>> 9);
        int[] iArr23 = this.arr;
        iArr23[2] = iArr23[2] + this.arr[7];
        int[] iArr24 = this.arr;
        iArr24[0] = iArr24[0] + this.arr[1];
    }

    private void setState(int start) {
        this.mem[start] = this.arr[0];
        this.mem[start + 1] = this.arr[1];
        this.mem[start + 2] = this.arr[2];
        this.mem[start + 3] = this.arr[3];
        this.mem[start + 4] = this.arr[4];
        this.mem[start + 5] = this.arr[5];
        this.mem[start + 6] = this.arr[6];
        this.mem[start + 7] = this.arr[7];
    }
}
