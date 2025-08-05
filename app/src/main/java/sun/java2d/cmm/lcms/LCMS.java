package sun.java2d.cmm.lcms;

import java.awt.color.CMMException;
import java.awt.color.ICC_Profile;
import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.java2d.cmm.ColorTransform;
import sun.java2d.cmm.PCMM;
import sun.java2d.cmm.Profile;
import sun.java2d.cmm.lcms.LCMSProfile;

/* loaded from: rt.jar:sun/java2d/cmm/lcms/LCMS.class */
public class LCMS implements PCMM {
    private static LCMS theLcms = null;

    private native long loadProfileNative(byte[] bArr, Object obj);

    private native int getProfileSizeNative(long j2);

    private native void getProfileDataNative(long j2, byte[] bArr);

    static native byte[] getTagNative(long j2, int i2);

    private native void setTagDataNative(long j2, int i2, byte[] bArr);

    public static native synchronized LCMSProfile getProfileID(ICC_Profile iCC_Profile);

    private static native long createNativeTransform(long[] jArr, int i2, int i3, boolean z2, int i4, boolean z3, Object obj);

    public static native void colorConvert(LCMSTransform lCMSTransform, LCMSImageLayout lCMSImageLayout, LCMSImageLayout lCMSImageLayout2);

    public static native void freeTransform(long j2);

    public static native void initLCMS(Class cls, Class cls2, Class cls3);

    @Override // sun.java2d.cmm.PCMM
    public Profile loadProfile(byte[] bArr) {
        Object obj = new Object();
        long jLoadProfileNative = loadProfileNative(bArr, obj);
        if (jLoadProfileNative != 0) {
            return new LCMSProfile(jLoadProfileNative, obj);
        }
        return null;
    }

    private LCMSProfile getLcmsProfile(Profile profile) {
        if (profile instanceof LCMSProfile) {
            return (LCMSProfile) profile;
        }
        throw new CMMException("Invalid profile: " + ((Object) profile));
    }

    @Override // sun.java2d.cmm.PCMM
    public void freeProfile(Profile profile) {
    }

    @Override // sun.java2d.cmm.PCMM
    public int getProfileSize(Profile profile) {
        int profileSizeNative;
        synchronized (profile) {
            profileSizeNative = getProfileSizeNative(getLcmsProfile(profile).getLcmsPtr());
        }
        return profileSizeNative;
    }

    @Override // sun.java2d.cmm.PCMM
    public void getProfileData(Profile profile, byte[] bArr) {
        synchronized (profile) {
            getProfileDataNative(getLcmsProfile(profile).getLcmsPtr(), bArr);
        }
    }

    @Override // sun.java2d.cmm.PCMM
    public int getTagSize(Profile profile, int i2) {
        int size;
        LCMSProfile lcmsProfile = getLcmsProfile(profile);
        synchronized (lcmsProfile) {
            LCMSProfile.TagData tag = lcmsProfile.getTag(i2);
            size = tag == null ? 0 : tag.getSize();
        }
        return size;
    }

    @Override // sun.java2d.cmm.PCMM
    public void getTagData(Profile profile, int i2, byte[] bArr) {
        LCMSProfile lcmsProfile = getLcmsProfile(profile);
        synchronized (lcmsProfile) {
            LCMSProfile.TagData tag = lcmsProfile.getTag(i2);
            if (tag != null) {
                tag.copyDataTo(bArr);
            }
        }
    }

    @Override // sun.java2d.cmm.PCMM
    public synchronized void setTagData(Profile profile, int i2, byte[] bArr) {
        LCMSProfile lcmsProfile = getLcmsProfile(profile);
        synchronized (lcmsProfile) {
            lcmsProfile.clearTagCache();
            setTagDataNative(lcmsProfile.getLcmsPtr(), i2, bArr);
        }
    }

    static long createTransform(LCMSProfile[] lCMSProfileArr, int i2, int i3, boolean z2, int i4, boolean z3, Object obj) {
        long[] jArr = new long[lCMSProfileArr.length];
        for (int i5 = 0; i5 < lCMSProfileArr.length; i5++) {
            if (lCMSProfileArr[i5] == null) {
                throw new CMMException("Unknown profile ID");
            }
            jArr[i5] = lCMSProfileArr[i5].getLcmsPtr();
        }
        return createNativeTransform(jArr, i2, i3, z2, i4, z3, obj);
    }

    @Override // sun.java2d.cmm.PCMM
    public ColorTransform createTransform(ICC_Profile iCC_Profile, int i2, int i3) {
        return new LCMSTransform(iCC_Profile, i2, i2);
    }

    @Override // sun.java2d.cmm.PCMM
    public synchronized ColorTransform createTransform(ColorTransform[] colorTransformArr) {
        return new LCMSTransform(colorTransformArr);
    }

    private LCMS() {
    }

    static synchronized PCMM getModule() {
        if (theLcms != null) {
            return theLcms;
        }
        AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.java2d.cmm.lcms.LCMS.1
            @Override // java.security.PrivilegedAction
            public Object run() {
                System.loadLibrary("awt");
                System.loadLibrary("lcms");
                return null;
            }
        });
        initLCMS(LCMSTransform.class, LCMSImageLayout.class, ICC_Profile.class);
        theLcms = new LCMS();
        return theLcms;
    }
}
