package com.sun.media.sound;

import javax.sound.midi.MidiChannel;

/* loaded from: rt.jar:com/sun/media/sound/ModelChannelMixer.class */
public interface ModelChannelMixer extends MidiChannel {
    boolean process(float[][] fArr, int i2, int i3);

    void stop();
}
