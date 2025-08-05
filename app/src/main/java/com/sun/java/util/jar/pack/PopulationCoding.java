package com.sun.java.util.jar.pack;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;
import org.icepdf.core.pobjects.graphics.Separation;

/* loaded from: rt.jar:com/sun/java/util/jar/pack/PopulationCoding.class */
class PopulationCoding implements CodingMethod {
    Histogram vHist;
    int[] fValues;
    int fVlen;
    long[] symtab;
    CodingMethod favoredCoding;
    CodingMethod tokenCoding;
    CodingMethod unfavoredCoding;

    /* renamed from: L, reason: collision with root package name */
    int f11851L = -1;
    static final int[] LValuesCoded;
    static final /* synthetic */ boolean $assertionsDisabled;

    PopulationCoding() {
    }

    static {
        $assertionsDisabled = !PopulationCoding.class.desiredAssertionStatus();
        LValuesCoded = new int[]{-1, 4, 8, 16, 32, 64, 128, 192, 224, 240, 248, 252};
    }

    public void setFavoredValues(int[] iArr, int i2) {
        if (!$assertionsDisabled && iArr[0] != 0) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this.fValues != null) {
            throw new AssertionError();
        }
        this.fValues = iArr;
        this.fVlen = i2;
        if (this.f11851L >= 0) {
            setL(this.f11851L);
        }
    }

    public void setFavoredValues(int[] iArr) {
        setFavoredValues(iArr, iArr.length - 1);
    }

    public void setHistogram(Histogram histogram) {
        this.vHist = histogram;
    }

    public void setL(int i2) {
        this.f11851L = i2;
        if (i2 >= 0 && this.fValues != null && this.tokenCoding == null) {
            this.tokenCoding = fitTokenCoding(this.fVlen, i2);
            if (!$assertionsDisabled && this.tokenCoding == null) {
                throw new AssertionError();
            }
        }
    }

    public static Coding fitTokenCoding(int i2, int i3) {
        if (i2 < 256) {
            return BandStructure.BYTE1;
        }
        Coding l2 = BandStructure.UNSIGNED5.setL(i3);
        if (!l2.canRepresentUnsigned(i2)) {
            return null;
        }
        Coding coding = l2;
        Coding b2 = l2;
        while (true) {
            b2 = b2.setB(b2.B() - 1);
            if (b2.umax() >= i2) {
                coding = b2;
            } else {
                return coding;
            }
        }
    }

    public void setFavoredCoding(CodingMethod codingMethod) {
        this.favoredCoding = codingMethod;
    }

    public void setTokenCoding(CodingMethod codingMethod) {
        Coding coding;
        this.tokenCoding = codingMethod;
        this.f11851L = -1;
        if ((codingMethod instanceof Coding) && this.fValues != null && (coding = (Coding) codingMethod) == fitTokenCoding(this.fVlen, coding.L())) {
            this.f11851L = coding.L();
        }
    }

    public void setUnfavoredCoding(CodingMethod codingMethod) {
        this.unfavoredCoding = codingMethod;
    }

    public int favoredValueMaxLength() {
        if (this.f11851L == 0) {
            return Integer.MAX_VALUE;
        }
        return BandStructure.UNSIGNED5.setL(this.f11851L).umax();
    }

    public void resortFavoredValues() {
        Coding coding = (Coding) this.tokenCoding;
        this.fValues = BandStructure.realloc(this.fValues, 1 + this.fVlen);
        int i2 = 1;
        for (int i3 = 1; i3 <= coding.B(); i3++) {
            int iByteMax = coding.byteMax(i3);
            if (iByteMax > this.fVlen) {
                iByteMax = this.fVlen;
            }
            if (iByteMax < coding.byteMin(i3)) {
                break;
            }
            int i4 = i2;
            int i5 = iByteMax + 1;
            if (i5 != i4) {
                if (!$assertionsDisabled && i5 <= i4) {
                    throw new AssertionError((Object) (i5 + "!>" + i4));
                }
                if (!$assertionsDisabled && coding.getLength(i4) != i3) {
                    throw new AssertionError((Object) (i3 + " != len(" + i4 + ") == " + coding.getLength(i4)));
                }
                if (!$assertionsDisabled && coding.getLength(i5 - 1) != i3) {
                    throw new AssertionError((Object) (i3 + " != len(" + (i5 - 1) + ") == " + coding.getLength(i5 - 1)));
                }
                int i6 = i4 + ((i5 - i4) / 2);
                int i7 = i4;
                int i8 = -1;
                int i9 = i4;
                for (int i10 = i4; i10 < i5; i10++) {
                    int frequency = this.vHist.getFrequency(this.fValues[i10]);
                    if (i8 != frequency) {
                        if (i3 == 1) {
                            Arrays.sort(this.fValues, i9, i10);
                        } else if (Math.abs(i7 - i6) > Math.abs(i10 - i6)) {
                            i7 = i10;
                        }
                        i8 = frequency;
                        i9 = i10;
                    }
                }
                if (i3 == 1) {
                    Arrays.sort(this.fValues, i9, i5);
                } else {
                    Arrays.sort(this.fValues, i4, i7);
                    Arrays.sort(this.fValues, i7, i5);
                }
                if (!$assertionsDisabled && coding.getLength(i4) != coding.getLength(i7)) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && coding.getLength(i4) != coding.getLength(i5 - 1)) {
                    throw new AssertionError();
                }
                i2 = iByteMax + 1;
            }
        }
        if (!$assertionsDisabled && i2 != this.fValues.length) {
            throw new AssertionError();
        }
        this.symtab = null;
    }

    public int getToken(int i2) {
        if (this.symtab == null) {
            this.symtab = makeSymtab();
        }
        int iBinarySearch = Arrays.binarySearch(this.symtab, i2 << 32);
        if (iBinarySearch < 0) {
            iBinarySearch = (-iBinarySearch) - 1;
        }
        if (iBinarySearch < this.symtab.length && i2 == ((int) (this.symtab[iBinarySearch] >>> 32))) {
            return (int) this.symtab[iBinarySearch];
        }
        return 0;
    }

    /* JADX WARN: Type inference failed for: r0v13, types: [int[], int[][]] */
    public int[][] encodeValues(int[] iArr, int i2, int i3) {
        int[] iArr2 = new int[i3 - i2];
        int i4 = 0;
        for (int i5 = 0; i5 < iArr2.length; i5++) {
            int token = getToken(iArr[i2 + i5]);
            if (token != 0) {
                iArr2[i5] = token;
            } else {
                i4++;
            }
        }
        int[] iArr3 = new int[i4];
        int i6 = 0;
        for (int i7 = 0; i7 < iArr2.length; i7++) {
            if (iArr2[i7] == 0) {
                int i8 = i6;
                i6++;
                iArr3[i8] = iArr[i2 + i7];
            }
        }
        if ($assertionsDisabled || i6 == iArr3.length) {
            return new int[]{iArr2, iArr3};
        }
        throw new AssertionError();
    }

    private long[] makeSymtab() {
        long[] jArr = new long[this.fVlen];
        for (int i2 = 1; i2 <= this.fVlen; i2++) {
            jArr[i2 - 1] = (this.fValues[i2] << 32) | i2;
        }
        Arrays.sort(jArr);
        return jArr;
    }

    private Coding getTailCoding(CodingMethod codingMethod) {
        while (codingMethod instanceof AdaptiveCoding) {
            codingMethod = ((AdaptiveCoding) codingMethod).tailCoding;
        }
        return (Coding) codingMethod;
    }

    @Override // com.sun.java.util.jar.pack.CodingMethod
    public void writeArrayTo(OutputStream outputStream, int[] iArr, int i2, int i3) throws IOException {
        int[][] iArrEncodeValues = encodeValues(iArr, i2, i3);
        writeSequencesTo(outputStream, iArrEncodeValues[0], iArrEncodeValues[1]);
    }

    void writeSequencesTo(OutputStream outputStream, int[] iArr, int[] iArr2) throws IOException {
        this.favoredCoding.writeArrayTo(outputStream, this.fValues, 1, 1 + this.fVlen);
        getTailCoding(this.favoredCoding).writeTo(outputStream, computeSentinelValue());
        this.tokenCoding.writeArrayTo(outputStream, iArr, 0, iArr.length);
        if (iArr2.length > 0) {
            this.unfavoredCoding.writeArrayTo(outputStream, iArr2, 0, iArr2.length);
        }
    }

    int computeSentinelValue() {
        Coding tailCoding = getTailCoding(this.favoredCoding);
        if (tailCoding.isDelta()) {
            return 0;
        }
        int iMoreCentral = this.fValues[1];
        int i2 = iMoreCentral;
        for (int i3 = 2; i3 <= this.fVlen; i3++) {
            i2 = this.fValues[i3];
            iMoreCentral = moreCentral(iMoreCentral, i2);
        }
        if (tailCoding.getLength(iMoreCentral) <= tailCoding.getLength(i2)) {
            return iMoreCentral;
        }
        return i2;
    }

    @Override // com.sun.java.util.jar.pack.CodingMethod
    public void readArrayFrom(InputStream inputStream, int[] iArr, int i2, int i3) throws IOException {
        setFavoredValues(readFavoredValuesFrom(inputStream, i3 - i2));
        this.tokenCoding.readArrayFrom(inputStream, iArr, i2, i3);
        int i4 = 0;
        int i5 = -1;
        int i6 = 0;
        for (int i7 = i2; i7 < i3; i7++) {
            int i8 = iArr[i7];
            if (i8 == 0) {
                if (i5 < 0) {
                    i4 = i7;
                } else {
                    iArr[i5] = i7;
                }
                i5 = i7;
                i6++;
            } else {
                iArr[i7] = this.fValues[i8];
            }
        }
        int[] iArr2 = new int[i6];
        if (i6 > 0) {
            this.unfavoredCoding.readArrayFrom(inputStream, iArr2, 0, i6);
        }
        for (int i9 = 0; i9 < i6; i9++) {
            int i10 = iArr[i4];
            iArr[i4] = iArr2[i9];
            i4 = i10;
        }
    }

    int[] readFavoredValuesFrom(InputStream inputStream, int i2) throws IOException {
        int iReduceToUnsignedRange;
        int[] iArrRealloc = new int[1000];
        HashSet hashSet = null;
        if (!$assertionsDisabled) {
            HashSet hashSet2 = new HashSet();
            hashSet = hashSet2;
            if (hashSet2 == null) {
                throw new AssertionError();
            }
        }
        int i3 = 1;
        int i4 = i2 + 1;
        int iMoreCentral = Integer.MIN_VALUE;
        int i5 = 0;
        CodingMethod codingMethod = this.favoredCoding;
        while (true) {
            CodingMethod codingMethod2 = codingMethod;
            if (codingMethod2 instanceof AdaptiveCoding) {
                AdaptiveCoding adaptiveCoding = (AdaptiveCoding) codingMethod2;
                int i6 = adaptiveCoding.headLength;
                while (i3 + i6 > iArrRealloc.length) {
                    iArrRealloc = BandStructure.realloc(iArrRealloc);
                }
                int i7 = i3 + i6;
                adaptiveCoding.headCoding.readArrayFrom(inputStream, iArrRealloc, i3, i7);
                while (i3 < i7) {
                    int i8 = i3;
                    i3++;
                    int i9 = iArrRealloc[i8];
                    if (!$assertionsDisabled && !hashSet.add(Integer.valueOf(i9))) {
                        throw new AssertionError();
                    }
                    if (!$assertionsDisabled && i3 > i4) {
                        throw new AssertionError();
                    }
                    i5 = i9;
                    iMoreCentral = moreCentral(iMoreCentral, i9);
                }
                codingMethod = adaptiveCoding.tailCoding;
            } else {
                Coding coding = (Coding) codingMethod2;
                if (!coding.isDelta()) {
                    while (true) {
                        int from = coding.readFrom(inputStream);
                        if (i3 > 1 && (from == i5 || from == iMoreCentral)) {
                            break;
                        }
                        if (i3 == iArrRealloc.length) {
                            iArrRealloc = BandStructure.realloc(iArrRealloc);
                        }
                        int i10 = i3;
                        i3++;
                        iArrRealloc[i10] = from;
                        if (!$assertionsDisabled && !hashSet.add(Integer.valueOf(from))) {
                            throw new AssertionError();
                        }
                        if (!$assertionsDisabled && i3 > i4) {
                            throw new AssertionError();
                        }
                        i5 = from;
                        iMoreCentral = moreCentral(iMoreCentral, from);
                    }
                } else {
                    long j2 = 0;
                    while (true) {
                        long from2 = j2 + coding.readFrom(inputStream);
                        if (coding.isSubrange()) {
                            iReduceToUnsignedRange = coding.reduceToUnsignedRange(from2);
                        } else {
                            iReduceToUnsignedRange = (int) from2;
                        }
                        j2 = iReduceToUnsignedRange;
                        if (i3 > 1 && (iReduceToUnsignedRange == i5 || iReduceToUnsignedRange == iMoreCentral)) {
                            break;
                        }
                        if (i3 == iArrRealloc.length) {
                            iArrRealloc = BandStructure.realloc(iArrRealloc);
                        }
                        int i11 = i3;
                        i3++;
                        iArrRealloc[i11] = iReduceToUnsignedRange;
                        if (!$assertionsDisabled && !hashSet.add(Integer.valueOf(iReduceToUnsignedRange))) {
                            throw new AssertionError();
                        }
                        if (!$assertionsDisabled && i3 > i4) {
                            throw new AssertionError();
                        }
                        i5 = iReduceToUnsignedRange;
                        iMoreCentral = moreCentral(iMoreCentral, iReduceToUnsignedRange);
                    }
                }
                return BandStructure.realloc(iArrRealloc, i3);
            }
        }
    }

    private static int moreCentral(int i2, int i3) {
        int i4 = ((i2 >> 31) ^ (i2 << 1)) - Integer.MIN_VALUE < ((i3 >> 31) ^ (i3 << 1)) - Integer.MIN_VALUE ? i2 : i3;
        if ($assertionsDisabled || i4 == moreCentralSlow(i2, i3)) {
            return i4;
        }
        throw new AssertionError();
    }

    private static int moreCentralSlow(int i2, int i3) {
        int i4 = i2;
        if (i4 < 0) {
            i4 = -i4;
        }
        if (i4 < 0) {
            return i3;
        }
        int i5 = i3;
        if (i5 < 0) {
            i5 = -i5;
        }
        if (i5 >= 0 && i4 >= i5) {
            if (i4 <= i5 && i2 < i3) {
                return i2;
            }
            return i3;
        }
        return i2;
    }

    @Override // com.sun.java.util.jar.pack.CodingMethod
    public byte[] getMetaCoding(Coding coding) {
        int i2 = this.fVlen;
        int i3 = 0;
        if (this.tokenCoding instanceof Coding) {
            Coding coding2 = (Coding) this.tokenCoding;
            if (coding2.B() == 1) {
                i3 = 1;
            } else if (this.f11851L >= 0) {
                if (!$assertionsDisabled && this.f11851L != coding2.L()) {
                    throw new AssertionError();
                }
                int i4 = 1;
                while (true) {
                    if (i4 >= LValuesCoded.length) {
                        break;
                    }
                    if (LValuesCoded[i4] == this.f11851L) {
                        i3 = i4;
                        break;
                    }
                    i4++;
                }
            }
        }
        CodingMethod codingMethod = null;
        if (i3 != 0 && this.tokenCoding == fitTokenCoding(this.fVlen, this.f11851L)) {
            codingMethod = this.tokenCoding;
        }
        int i5 = this.favoredCoding == coding ? 1 : 0;
        int i6 = (this.unfavoredCoding == coding || this.unfavoredCoding == null) ? 1 : 0;
        boolean z2 = this.tokenCoding == codingMethod;
        int i7 = z2 ? i3 : 0;
        if (!$assertionsDisabled) {
            if (z2 != (i7 > 0)) {
                throw new AssertionError();
            }
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(10);
        byteArrayOutputStream.write(141 + i5 + (2 * i6) + (4 * i7));
        if (i5 == 0) {
            try {
                byteArrayOutputStream.write(this.favoredCoding.getMetaCoding(coding));
            } catch (IOException e2) {
                throw new RuntimeException(e2);
            }
        }
        if (!z2) {
            byteArrayOutputStream.write(this.tokenCoding.getMetaCoding(coding));
        }
        if (i6 == 0) {
            byteArrayOutputStream.write(this.unfavoredCoding.getMetaCoding(coding));
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static int parseMetaCoding(byte[] bArr, int i2, Coding coding, CodingMethod[] codingMethodArr) {
        int metaCoding = i2 + 1;
        int i3 = bArr[i2] & 255;
        if (i3 < 141 || i3 >= 189) {
            return metaCoding - 1;
        }
        int i4 = i3 - 141;
        int i5 = i4 % 2;
        int i6 = (i4 / 2) % 2;
        int i7 = i4 / 4;
        boolean z2 = i7 > 0;
        int i8 = LValuesCoded[i7];
        CodingMethod[] codingMethodArr2 = {coding};
        CodingMethod[] codingMethodArr3 = {null};
        CodingMethod[] codingMethodArr4 = {coding};
        if (i5 == 0) {
            metaCoding = BandStructure.parseMetaCoding(bArr, metaCoding, coding, codingMethodArr2);
        }
        if (!z2) {
            metaCoding = BandStructure.parseMetaCoding(bArr, metaCoding, coding, codingMethodArr3);
        }
        if (i6 == 0) {
            metaCoding = BandStructure.parseMetaCoding(bArr, metaCoding, coding, codingMethodArr4);
        }
        PopulationCoding populationCoding = new PopulationCoding();
        populationCoding.f11851L = i8;
        populationCoding.favoredCoding = codingMethodArr2[0];
        populationCoding.tokenCoding = codingMethodArr3[0];
        populationCoding.unfavoredCoding = codingMethodArr4[0];
        codingMethodArr[0] = populationCoding;
        return metaCoding;
    }

    private String keyString(CodingMethod codingMethod) {
        if (codingMethod instanceof Coding) {
            return ((Coding) codingMethod).keyString();
        }
        if (codingMethod == null) {
            return Separation.COLORANT_NONE;
        }
        return codingMethod.toString();
    }

    public String toString() {
        PropMap propMapCurrentPropMap = Utils.currentPropMap();
        boolean z2 = propMapCurrentPropMap != null && propMapCurrentPropMap.getBoolean("com.sun.java.util.jar.pack.verbose.pop");
        StringBuilder sb = new StringBuilder(100);
        sb.append("pop(").append("fVlen=").append(this.fVlen);
        if (z2 && this.fValues != null) {
            sb.append(" fV=[");
            int i2 = 1;
            while (i2 <= this.fVlen) {
                sb.append(i2 == 1 ? "" : ",").append(this.fValues[i2]);
                i2++;
            }
            sb.append(";").append(computeSentinelValue());
            sb.append("]");
        }
        sb.append(" fc=").append(keyString(this.favoredCoding));
        sb.append(" tc=").append(keyString(this.tokenCoding));
        sb.append(" uc=").append(keyString(this.unfavoredCoding));
        sb.append(")");
        return sb.toString();
    }
}
