package sun.font;

/* loaded from: rt.jar:sun/font/CompositeFontDescriptor.class */
public class CompositeFontDescriptor {
    private String faceName;
    private int coreComponentCount;
    private String[] componentFaceNames;
    private String[] componentFileNames;
    private int[] exclusionRanges;
    private int[] exclusionRangeLimits;

    public CompositeFontDescriptor(String str, int i2, String[] strArr, String[] strArr2, int[] iArr, int[] iArr2) {
        this.faceName = str;
        this.coreComponentCount = i2;
        this.componentFaceNames = strArr;
        this.componentFileNames = strArr2;
        this.exclusionRanges = iArr;
        this.exclusionRangeLimits = iArr2;
    }

    public String getFaceName() {
        return this.faceName;
    }

    public int getCoreComponentCount() {
        return this.coreComponentCount;
    }

    public String[] getComponentFaceNames() {
        return this.componentFaceNames;
    }

    public String[] getComponentFileNames() {
        return this.componentFileNames;
    }

    public int[] getExclusionRanges() {
        return this.exclusionRanges;
    }

    public int[] getExclusionRangeLimits() {
        return this.exclusionRangeLimits;
    }
}
