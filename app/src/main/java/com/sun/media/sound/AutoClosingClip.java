package com.sun.media.sound;

import javax.sound.sampled.Clip;

/* loaded from: rt.jar:com/sun/media/sound/AutoClosingClip.class */
interface AutoClosingClip extends Clip {
    boolean isAutoClosing();

    void setAutoClosing(boolean z2);
}
