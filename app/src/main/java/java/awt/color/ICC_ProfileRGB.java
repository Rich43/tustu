package java.awt.color;

import sun.java2d.cmm.Profile;
import sun.java2d.cmm.ProfileDeferralInfo;

/* loaded from: rt.jar:java/awt/color/ICC_ProfileRGB.class */
public class ICC_ProfileRGB extends ICC_Profile {
    static final long serialVersionUID = 8505067385152579334L;
    public static final int REDCOMPONENT = 0;
    public static final int GREENCOMPONENT = 1;
    public static final int BLUECOMPONENT = 2;

    ICC_ProfileRGB(Profile profile) {
        super(profile);
    }

    ICC_ProfileRGB(ProfileDeferralInfo profileDeferralInfo) {
        super(profileDeferralInfo);
    }

    @Override // java.awt.color.ICC_Profile
    public float[] getMediaWhitePoint() {
        return super.getMediaWhitePoint();
    }

    public float[][] getMatrix() {
        float[][] fArr = new float[3][3];
        float[] xYZTag = getXYZTag(1918392666);
        fArr[0][0] = xYZTag[0];
        fArr[1][0] = xYZTag[1];
        fArr[2][0] = xYZTag[2];
        float[] xYZTag2 = getXYZTag(1733843290);
        fArr[0][1] = xYZTag2[0];
        fArr[1][1] = xYZTag2[1];
        fArr[2][1] = xYZTag2[2];
        float[] xYZTag3 = getXYZTag(1649957210);
        fArr[0][2] = xYZTag3[0];
        fArr[1][2] = xYZTag3[1];
        fArr[2][2] = xYZTag3[2];
        return fArr;
    }

    @Override // java.awt.color.ICC_Profile
    public float getGamma(int i2) {
        int i3;
        switch (i2) {
            case 0:
                i3 = 1918128707;
                break;
            case 1:
                i3 = 1733579331;
                break;
            case 2:
                i3 = 1649693251;
                break;
            default:
                throw new IllegalArgumentException("Must be Red, Green, or Blue");
        }
        return super.getGamma(i3);
    }

    @Override // java.awt.color.ICC_Profile
    public short[] getTRC(int i2) {
        int i3;
        switch (i2) {
            case 0:
                i3 = 1918128707;
                break;
            case 1:
                i3 = 1733579331;
                break;
            case 2:
                i3 = 1649693251;
                break;
            default:
                throw new IllegalArgumentException("Must be Red, Green, or Blue");
        }
        return super.getTRC(i3);
    }
}
