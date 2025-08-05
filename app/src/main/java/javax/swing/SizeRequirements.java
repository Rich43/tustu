package javax.swing;

import java.io.Serializable;

/* loaded from: rt.jar:javax/swing/SizeRequirements.class */
public class SizeRequirements implements Serializable {
    public int minimum;
    public int preferred;
    public int maximum;
    public float alignment;

    public SizeRequirements() {
        this.minimum = 0;
        this.preferred = 0;
        this.maximum = 0;
        this.alignment = 0.5f;
    }

    public SizeRequirements(int i2, int i3, int i4, float f2) {
        this.minimum = i2;
        this.preferred = i3;
        this.maximum = i4;
        this.alignment = f2 > 1.0f ? 1.0f : f2 < 0.0f ? 0.0f : f2;
    }

    public String toString() {
        return "[" + this.minimum + "," + this.preferred + "," + this.maximum + "]@" + this.alignment;
    }

    public static SizeRequirements getTiledSizeRequirements(SizeRequirements[] sizeRequirementsArr) {
        SizeRequirements sizeRequirements = new SizeRequirements();
        for (SizeRequirements sizeRequirements2 : sizeRequirementsArr) {
            sizeRequirements.minimum = (int) Math.min(sizeRequirements.minimum + sizeRequirements2.minimum, 2147483647L);
            sizeRequirements.preferred = (int) Math.min(sizeRequirements.preferred + sizeRequirements2.preferred, 2147483647L);
            sizeRequirements.maximum = (int) Math.min(sizeRequirements.maximum + sizeRequirements2.maximum, 2147483647L);
        }
        return sizeRequirements;
    }

    public static SizeRequirements getAlignedSizeRequirements(SizeRequirements[] sizeRequirementsArr) {
        SizeRequirements sizeRequirements = new SizeRequirements();
        SizeRequirements sizeRequirements2 = new SizeRequirements();
        for (SizeRequirements sizeRequirements3 : sizeRequirementsArr) {
            int i2 = (int) (sizeRequirements3.alignment * sizeRequirements3.minimum);
            int i3 = sizeRequirements3.minimum - i2;
            sizeRequirements.minimum = Math.max(i2, sizeRequirements.minimum);
            sizeRequirements2.minimum = Math.max(i3, sizeRequirements2.minimum);
            int i4 = (int) (sizeRequirements3.alignment * sizeRequirements3.preferred);
            int i5 = sizeRequirements3.preferred - i4;
            sizeRequirements.preferred = Math.max(i4, sizeRequirements.preferred);
            sizeRequirements2.preferred = Math.max(i5, sizeRequirements2.preferred);
            int i6 = (int) (sizeRequirements3.alignment * sizeRequirements3.maximum);
            int i7 = sizeRequirements3.maximum - i6;
            sizeRequirements.maximum = Math.max(i6, sizeRequirements.maximum);
            sizeRequirements2.maximum = Math.max(i7, sizeRequirements2.maximum);
        }
        int iMin = (int) Math.min(sizeRequirements.minimum + sizeRequirements2.minimum, 2147483647L);
        int iMin2 = (int) Math.min(sizeRequirements.preferred + sizeRequirements2.preferred, 2147483647L);
        int iMin3 = (int) Math.min(sizeRequirements.maximum + sizeRequirements2.maximum, 2147483647L);
        float f2 = 0.0f;
        if (iMin > 0) {
            float f3 = sizeRequirements.minimum / iMin;
            f2 = f3 > 1.0f ? 1.0f : f3 < 0.0f ? 0.0f : f3;
        }
        return new SizeRequirements(iMin, iMin2, iMin3, f2);
    }

    public static void calculateTiledPositions(int i2, SizeRequirements sizeRequirements, SizeRequirements[] sizeRequirementsArr, int[] iArr, int[] iArr2) {
        calculateTiledPositions(i2, sizeRequirements, sizeRequirementsArr, iArr, iArr2, true);
    }

    public static void calculateTiledPositions(int i2, SizeRequirements sizeRequirements, SizeRequirements[] sizeRequirementsArr, int[] iArr, int[] iArr2, boolean z2) {
        long j2 = 0;
        long j3 = 0;
        long j4 = 0;
        for (int i3 = 0; i3 < sizeRequirementsArr.length; i3++) {
            j2 += sizeRequirementsArr[i3].minimum;
            j3 += sizeRequirementsArr[i3].preferred;
            j4 += sizeRequirementsArr[i3].maximum;
        }
        if (i2 >= j3) {
            expandedTile(i2, j2, j3, j4, sizeRequirementsArr, iArr, iArr2, z2);
        } else {
            compressedTile(i2, j2, j3, j4, sizeRequirementsArr, iArr, iArr2, z2);
        }
    }

    private static void compressedTile(int i2, long j2, long j3, long j4, SizeRequirements[] sizeRequirementsArr, int[] iArr, int[] iArr2, boolean z2) {
        float fMin = j3 - j2 == 0 ? 0.0f : Math.min(j3 - i2, j3 - j2) / (j3 - j2);
        if (z2) {
            int iMin = 0;
            for (int i3 = 0; i3 < iArr2.length; i3++) {
                iArr[i3] = iMin;
                SizeRequirements sizeRequirements = sizeRequirementsArr[i3];
                iArr2[i3] = (int) (sizeRequirements.preferred - (fMin * (sizeRequirements.preferred - sizeRequirements.minimum)));
                iMin = (int) Math.min(iMin + iArr2[i3], 2147483647L);
            }
            return;
        }
        int iMax = i2;
        for (int i4 = 0; i4 < iArr2.length; i4++) {
            SizeRequirements sizeRequirements2 = sizeRequirementsArr[i4];
            iArr2[i4] = (int) (sizeRequirements2.preferred - (fMin * (sizeRequirements2.preferred - sizeRequirements2.minimum)));
            iArr[i4] = iMax - iArr2[i4];
            iMax = (int) Math.max(iMax - iArr2[i4], 0L);
        }
    }

    private static void expandedTile(int i2, long j2, long j3, long j4, SizeRequirements[] sizeRequirementsArr, int[] iArr, int[] iArr2, boolean z2) {
        float fMin = j4 - j3 == 0 ? 0.0f : Math.min(i2 - j3, j4 - j3) / (j4 - j3);
        if (z2) {
            int iMin = 0;
            for (int i3 = 0; i3 < iArr2.length; i3++) {
                iArr[i3] = iMin;
                SizeRequirements sizeRequirements = sizeRequirementsArr[i3];
                iArr2[i3] = (int) Math.min(sizeRequirements.preferred + ((int) (fMin * (sizeRequirements.maximum - sizeRequirements.preferred))), 2147483647L);
                iMin = (int) Math.min(iMin + iArr2[i3], 2147483647L);
            }
            return;
        }
        int iMax = i2;
        for (int i4 = 0; i4 < iArr2.length; i4++) {
            SizeRequirements sizeRequirements2 = sizeRequirementsArr[i4];
            iArr2[i4] = (int) Math.min(sizeRequirements2.preferred + ((int) (fMin * (sizeRequirements2.maximum - sizeRequirements2.preferred))), 2147483647L);
            iArr[i4] = iMax - iArr2[i4];
            iMax = (int) Math.max(iMax - iArr2[i4], 0L);
        }
    }

    public static void calculateAlignedPositions(int i2, SizeRequirements sizeRequirements, SizeRequirements[] sizeRequirementsArr, int[] iArr, int[] iArr2) {
        calculateAlignedPositions(i2, sizeRequirements, sizeRequirementsArr, iArr, iArr2, true);
    }

    public static void calculateAlignedPositions(int i2, SizeRequirements sizeRequirements, SizeRequirements[] sizeRequirementsArr, int[] iArr, int[] iArr2, boolean z2) {
        int i3 = (int) (i2 * (z2 ? sizeRequirements.alignment : 1.0f - sizeRequirements.alignment));
        int i4 = i2 - i3;
        for (int i5 = 0; i5 < sizeRequirementsArr.length; i5++) {
            SizeRequirements sizeRequirements2 = sizeRequirementsArr[i5];
            int i6 = (int) (sizeRequirements2.maximum * (z2 ? sizeRequirements2.alignment : 1.0f - sizeRequirements2.alignment));
            int i7 = sizeRequirements2.maximum - i6;
            int iMin = Math.min(i3, i6);
            int iMin2 = Math.min(i4, i7);
            iArr[i5] = i3 - iMin;
            iArr2[i5] = (int) Math.min(iMin + iMin2, 2147483647L);
        }
    }

    public static int[] adjustSizes(int i2, SizeRequirements[] sizeRequirementsArr) {
        return new int[0];
    }
}
