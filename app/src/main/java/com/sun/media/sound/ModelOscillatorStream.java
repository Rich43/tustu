package com.sun.media.sound;

import java.io.IOException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.VoiceStatus;

/* loaded from: rt.jar:com/sun/media/sound/ModelOscillatorStream.class */
public interface ModelOscillatorStream {
    void setPitch(float f2);

    void noteOn(MidiChannel midiChannel, VoiceStatus voiceStatus, int i2, int i3);

    void noteOff(int i2);

    int read(float[][] fArr, int i2, int i3) throws IOException;

    void close() throws IOException;
}
