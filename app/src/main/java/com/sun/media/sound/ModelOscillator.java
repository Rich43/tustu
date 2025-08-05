package com.sun.media.sound;

/* loaded from: rt.jar:com/sun/media/sound/ModelOscillator.class */
public interface ModelOscillator {
    int getChannels();

    float getAttenuation();

    ModelOscillatorStream open(float f2);
}
