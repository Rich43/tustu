package sun.audio;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:sun/audio/NativeAudioStream.class */
public class NativeAudioStream extends FilterInputStream {
    public NativeAudioStream(InputStream inputStream) throws IOException {
        super(inputStream);
    }

    public int getLength() {
        return 0;
    }
}
