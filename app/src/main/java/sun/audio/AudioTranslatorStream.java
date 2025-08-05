package sun.audio;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:sun/audio/AudioTranslatorStream.class */
public final class AudioTranslatorStream extends NativeAudioStream {
    private final int length = 0;

    public AudioTranslatorStream(InputStream inputStream) throws IOException {
        super(inputStream);
        this.length = 0;
        throw new InvalidAudioFormatException();
    }

    @Override // sun.audio.NativeAudioStream
    public int getLength() {
        return 0;
    }
}
