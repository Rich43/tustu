package sun.java2d.cmm;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:sun/java2d/cmm/ProfileDeferralInfo.class */
public class ProfileDeferralInfo extends InputStream {
    public int colorSpaceType;
    public int numComponents;
    public int profileClass;
    public String filename;

    public ProfileDeferralInfo(String str, int i2, int i3, int i4) {
        this.filename = str;
        this.colorSpaceType = i2;
        this.numComponents = i3;
        this.profileClass = i4;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        return 0;
    }
}
