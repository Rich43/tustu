package java.awt.color;

import sun.java2d.cmm.Profile;
import sun.java2d.cmm.ProfileDeferralInfo;

/* loaded from: rt.jar:java/awt/color/ICC_ProfileGray.class */
public class ICC_ProfileGray extends ICC_Profile {
    static final long serialVersionUID = -1124721290732002649L;

    ICC_ProfileGray(Profile profile) {
        super(profile);
    }

    ICC_ProfileGray(ProfileDeferralInfo profileDeferralInfo) {
        super(profileDeferralInfo);
    }

    @Override // java.awt.color.ICC_Profile
    public float[] getMediaWhitePoint() {
        return super.getMediaWhitePoint();
    }

    public float getGamma() {
        return super.getGamma(ICC_Profile.icSigGrayTRCTag);
    }

    public short[] getTRC() {
        return super.getTRC(ICC_Profile.icSigGrayTRCTag);
    }
}
