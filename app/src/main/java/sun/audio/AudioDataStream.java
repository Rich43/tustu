package sun.audio;

import java.io.ByteArrayInputStream;

/* loaded from: rt.jar:sun/audio/AudioDataStream.class */
public class AudioDataStream extends ByteArrayInputStream {

    /* renamed from: ad, reason: collision with root package name */
    private final AudioData f13540ad;

    public AudioDataStream(AudioData audioData) {
        super(audioData.buffer);
        this.f13540ad = audioData;
    }

    final AudioData getAudioData() {
        return this.f13540ad;
    }
}
