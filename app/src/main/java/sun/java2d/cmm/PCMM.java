package sun.java2d.cmm;

import java.awt.color.ICC_Profile;

/* loaded from: rt.jar:sun/java2d/cmm/PCMM.class */
public interface PCMM {
    Profile loadProfile(byte[] bArr);

    void freeProfile(Profile profile);

    int getProfileSize(Profile profile);

    void getProfileData(Profile profile, byte[] bArr);

    void getTagData(Profile profile, int i2, byte[] bArr);

    int getTagSize(Profile profile, int i2);

    void setTagData(Profile profile, int i2, byte[] bArr);

    ColorTransform createTransform(ICC_Profile iCC_Profile, int i2, int i3);

    ColorTransform createTransform(ColorTransform[] colorTransformArr);
}
