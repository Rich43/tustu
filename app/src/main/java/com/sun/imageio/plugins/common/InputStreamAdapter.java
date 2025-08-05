package com.sun.imageio.plugins.common;

import java.io.IOException;
import java.io.InputStream;
import javax.imageio.stream.ImageInputStream;

/* loaded from: rt.jar:com/sun/imageio/plugins/common/InputStreamAdapter.class */
public class InputStreamAdapter extends InputStream {
    ImageInputStream stream;

    public InputStreamAdapter(ImageInputStream imageInputStream) {
        this.stream = imageInputStream;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        return this.stream.read();
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        return this.stream.read(bArr, i2, i3);
    }
}
