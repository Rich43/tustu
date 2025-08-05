package com.sun.imageio.plugins.png;

import com.sun.imageio.plugins.common.InputStreamAdapter;
import com.sun.imageio.plugins.common.SubImageInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import javax.imageio.stream.ImageInputStream;

/* compiled from: PNGImageReader.java */
/* loaded from: rt.jar:com/sun/imageio/plugins/png/PNGImageDataEnumeration.class */
class PNGImageDataEnumeration implements Enumeration<InputStream> {
    boolean firstTime = true;
    ImageInputStream stream;
    int length;

    public PNGImageDataEnumeration(ImageInputStream imageInputStream) throws IOException {
        this.stream = imageInputStream;
        this.length = imageInputStream.readInt();
        imageInputStream.readInt();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Enumeration
    public InputStream nextElement() {
        try {
            this.firstTime = false;
            return new InputStreamAdapter(new SubImageInputStream(this.stream, this.length));
        } catch (IOException e2) {
            return null;
        }
    }

    @Override // java.util.Enumeration
    public boolean hasMoreElements() {
        if (this.firstTime) {
            return true;
        }
        try {
            this.stream.readInt();
            this.length = this.stream.readInt();
            if (this.stream.readInt() == 1229209940) {
                return true;
            }
            return false;
        } catch (IOException e2) {
            return false;
        }
    }
}
