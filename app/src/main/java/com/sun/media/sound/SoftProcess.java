package com.sun.media.sound;

/* loaded from: rt.jar:com/sun/media/sound/SoftProcess.class */
public interface SoftProcess extends SoftControl {
    void init(SoftSynthesizer softSynthesizer);

    @Override // com.sun.media.sound.SoftControl
    double[] get(int i2, String str);

    void processControlLogic();

    void reset();
}
