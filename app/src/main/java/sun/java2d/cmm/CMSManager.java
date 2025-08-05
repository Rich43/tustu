package sun.java2d.cmm;

import java.awt.color.CMMException;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.ServiceLoader;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/java2d/cmm/CMSManager.class */
public class CMSManager {
    public static ColorSpace GRAYspace;
    public static ColorSpace LINEAR_RGBspace;
    private static PCMM cmmImpl = null;

    public static synchronized PCMM getModule() {
        if (cmmImpl != null) {
            return cmmImpl;
        }
        cmmImpl = ((CMMServiceProvider) AccessController.doPrivileged(new PrivilegedAction<CMMServiceProvider>() { // from class: sun.java2d.cmm.CMSManager.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public CMMServiceProvider run2() {
                String property = System.getProperty("sun.java2d.cmm", "sun.java2d.cmm.lcms.LcmsServiceProvider");
                CMMServiceProvider cMMServiceProvider = null;
                Iterator it = ServiceLoader.loadInstalled(CMMServiceProvider.class).iterator();
                while (it.hasNext()) {
                    CMMServiceProvider cMMServiceProvider2 = (CMMServiceProvider) it.next();
                    cMMServiceProvider = cMMServiceProvider2;
                    if (cMMServiceProvider2.getClass().getName().equals(property)) {
                        break;
                    }
                }
                return cMMServiceProvider;
            }
        })).getColorManagementModule();
        if (cmmImpl == null) {
            throw new CMMException("Cannot initialize Color Management System.No CM module found");
        }
        if (((String) AccessController.doPrivileged(new GetPropertyAction("sun.java2d.cmm.trace"))) != null) {
            cmmImpl = new CMMTracer(cmmImpl);
        }
        return cmmImpl;
    }

    static synchronized boolean canCreateModule() {
        return cmmImpl == null;
    }

    /* loaded from: rt.jar:sun/java2d/cmm/CMSManager$CMMTracer.class */
    public static class CMMTracer implements PCMM {
        PCMM tcmm;
        String cName;

        public CMMTracer(PCMM pcmm) {
            this.tcmm = pcmm;
            this.cName = pcmm.getClass().getName();
        }

        @Override // sun.java2d.cmm.PCMM
        public Profile loadProfile(byte[] bArr) {
            System.err.print(this.cName + ".loadProfile");
            Profile profileLoadProfile = this.tcmm.loadProfile(bArr);
            System.err.printf("(ID=%s)\n", profileLoadProfile.toString());
            return profileLoadProfile;
        }

        @Override // sun.java2d.cmm.PCMM
        public void freeProfile(Profile profile) {
            System.err.printf(this.cName + ".freeProfile(ID=%s)\n", profile.toString());
            this.tcmm.freeProfile(profile);
        }

        @Override // sun.java2d.cmm.PCMM
        public int getProfileSize(Profile profile) {
            System.err.print(this.cName + ".getProfileSize(ID=" + ((Object) profile) + ")");
            int profileSize = this.tcmm.getProfileSize(profile);
            System.err.println("=" + profileSize);
            return profileSize;
        }

        @Override // sun.java2d.cmm.PCMM
        public void getProfileData(Profile profile, byte[] bArr) {
            System.err.print(this.cName + ".getProfileData(ID=" + ((Object) profile) + ") ");
            System.err.println("requested " + bArr.length + " byte(s)");
            this.tcmm.getProfileData(profile, bArr);
        }

        @Override // sun.java2d.cmm.PCMM
        public int getTagSize(Profile profile, int i2) {
            System.err.printf(this.cName + ".getTagSize(ID=%x, TagSig=%s)", profile, signatureToString(i2));
            int tagSize = this.tcmm.getTagSize(profile, i2);
            System.err.println("=" + tagSize);
            return tagSize;
        }

        @Override // sun.java2d.cmm.PCMM
        public void getTagData(Profile profile, int i2, byte[] bArr) {
            System.err.printf(this.cName + ".getTagData(ID=%x, TagSig=%s)", profile, signatureToString(i2));
            System.err.println(" requested " + bArr.length + " byte(s)");
            this.tcmm.getTagData(profile, i2, bArr);
        }

        @Override // sun.java2d.cmm.PCMM
        public void setTagData(Profile profile, int i2, byte[] bArr) {
            System.err.print(this.cName + ".setTagData(ID=" + ((Object) profile) + ", TagSig=" + i2 + ")");
            System.err.println(" sending " + bArr.length + " byte(s)");
            this.tcmm.setTagData(profile, i2, bArr);
        }

        @Override // sun.java2d.cmm.PCMM
        public ColorTransform createTransform(ICC_Profile iCC_Profile, int i2, int i3) {
            System.err.println(this.cName + ".createTransform(ICC_Profile,int,int)");
            return this.tcmm.createTransform(iCC_Profile, i2, i3);
        }

        @Override // sun.java2d.cmm.PCMM
        public ColorTransform createTransform(ColorTransform[] colorTransformArr) {
            System.err.println(this.cName + ".createTransform(ColorTransform[])");
            return this.tcmm.createTransform(colorTransformArr);
        }

        private static String signatureToString(int i2) {
            return String.format("%c%c%c%c", Character.valueOf((char) (255 & (i2 >> 24))), Character.valueOf((char) (255 & (i2 >> 16))), Character.valueOf((char) (255 & (i2 >> 8))), Character.valueOf((char) (255 & i2)));
        }
    }
}
