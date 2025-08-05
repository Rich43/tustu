package com.sun.media.sound;

/* loaded from: rt.jar:com/sun/media/sound/SoftAudioProcessor.class */
public interface SoftAudioProcessor {
    void globalParameterControlChange(int[] iArr, long j2, long j3);

    void init(float f2, float f3);

    void setInput(int i2, SoftAudioBuffer softAudioBuffer);

    void setOutput(int i2, SoftAudioBuffer softAudioBuffer);

    void setMixMode(boolean z2);

    void processAudio();

    void processControlLogic();
}
