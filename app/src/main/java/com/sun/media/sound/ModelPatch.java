package com.sun.media.sound;

import javax.sound.midi.Patch;

/* loaded from: rt.jar:com/sun/media/sound/ModelPatch.class */
public final class ModelPatch extends Patch {
    private boolean percussion;

    public ModelPatch(int i2, int i3) {
        super(i2, i3);
        this.percussion = false;
    }

    public ModelPatch(int i2, int i3, boolean z2) {
        super(i2, i3);
        this.percussion = false;
        this.percussion = z2;
    }

    public boolean isPercussion() {
        return this.percussion;
    }
}
