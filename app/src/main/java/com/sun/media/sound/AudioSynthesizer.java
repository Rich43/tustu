package com.sun.media.sound;

import java.util.Map;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.SourceDataLine;

/* loaded from: rt.jar:com/sun/media/sound/AudioSynthesizer.class */
public interface AudioSynthesizer extends Synthesizer {
    AudioFormat getFormat();

    AudioSynthesizerPropertyInfo[] getPropertyInfo(Map<String, Object> map);

    void open(SourceDataLine sourceDataLine, Map<String, Object> map) throws MidiUnavailableException;

    AudioInputStream openStream(AudioFormat audioFormat, Map<String, Object> map) throws MidiUnavailableException;
}
