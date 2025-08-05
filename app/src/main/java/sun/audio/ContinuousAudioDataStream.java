package sun.audio;

/* loaded from: rt.jar:sun/audio/ContinuousAudioDataStream.class */
public final class ContinuousAudioDataStream extends AudioDataStream {
    public ContinuousAudioDataStream(AudioData audioData) {
        super(audioData);
    }

    @Override // java.io.ByteArrayInputStream, java.io.InputStream
    public int read() {
        int i2 = super.read();
        if (i2 == -1) {
            reset();
            i2 = super.read();
        }
        return i2;
    }

    @Override // java.io.ByteArrayInputStream, java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) {
        int i4 = 0;
        while (i4 < i3) {
            int i5 = super.read(bArr, i2 + i4, i3 - i4);
            if (i5 >= 0) {
                i4 += i5;
            } else {
                reset();
            }
        }
        return i4;
    }
}
