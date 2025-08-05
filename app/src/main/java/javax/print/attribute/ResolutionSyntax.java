package javax.print.attribute;

import java.io.Serializable;

/* loaded from: rt.jar:javax/print/attribute/ResolutionSyntax.class */
public abstract class ResolutionSyntax implements Serializable, Cloneable {
    private static final long serialVersionUID = 2706743076526672017L;
    private int crossFeedResolution;
    private int feedResolution;
    public static final int DPI = 100;
    public static final int DPCM = 254;

    public ResolutionSyntax(int i2, int i3, int i4) {
        if (i2 < 1) {
            throw new IllegalArgumentException("crossFeedResolution is < 1");
        }
        if (i3 < 1) {
            throw new IllegalArgumentException("feedResolution is < 1");
        }
        if (i4 < 1) {
            throw new IllegalArgumentException("units is < 1");
        }
        this.crossFeedResolution = i2 * i4;
        this.feedResolution = i3 * i4;
    }

    private static int convertFromDphi(int i2, int i3) {
        if (i3 < 1) {
            throw new IllegalArgumentException(": units is < 1");
        }
        return (i2 + (i3 / 2)) / i3;
    }

    public int[] getResolution(int i2) {
        return new int[]{getCrossFeedResolution(i2), getFeedResolution(i2)};
    }

    public int getCrossFeedResolution(int i2) {
        return convertFromDphi(this.crossFeedResolution, i2);
    }

    public int getFeedResolution(int i2) {
        return convertFromDphi(this.feedResolution, i2);
    }

    public String toString(int i2, String str) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(getCrossFeedResolution(i2));
        stringBuffer.append('x');
        stringBuffer.append(getFeedResolution(i2));
        if (str != null) {
            stringBuffer.append(' ');
            stringBuffer.append(str);
        }
        return stringBuffer.toString();
    }

    public boolean lessThanOrEquals(ResolutionSyntax resolutionSyntax) {
        return this.crossFeedResolution <= resolutionSyntax.crossFeedResolution && this.feedResolution <= resolutionSyntax.feedResolution;
    }

    public boolean equals(Object obj) {
        return obj != null && (obj instanceof ResolutionSyntax) && this.crossFeedResolution == ((ResolutionSyntax) obj).crossFeedResolution && this.feedResolution == ((ResolutionSyntax) obj).feedResolution;
    }

    public int hashCode() {
        return (this.crossFeedResolution & 65535) | ((this.feedResolution & 65535) << 16);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.crossFeedResolution);
        stringBuffer.append('x');
        stringBuffer.append(this.feedResolution);
        stringBuffer.append(" dphi");
        return stringBuffer.toString();
    }

    protected int getCrossFeedResolutionDphi() {
        return this.crossFeedResolution;
    }

    protected int getFeedResolutionDphi() {
        return this.feedResolution;
    }
}
