package sun.audio;

import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Enumeration;

/* loaded from: rt.jar:sun/audio/AudioStreamSequence.class */
public final class AudioStreamSequence extends SequenceInputStream {

    /* renamed from: e, reason: collision with root package name */
    Enumeration f13541e;
    InputStream in;

    public AudioStreamSequence(Enumeration enumeration) {
        super(enumeration);
    }
}
