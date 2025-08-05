package com.sun.media.sound;

import java.util.ArrayList;
import java.util.List;

/* loaded from: rt.jar:com/sun/media/sound/DLSSampleOptions.class */
public final class DLSSampleOptions {
    int unitynote;
    short finetune;
    int attenuation;
    long options;
    List<DLSSampleLoop> loops = new ArrayList();

    public int getAttenuation() {
        return this.attenuation;
    }

    public void setAttenuation(int i2) {
        this.attenuation = i2;
    }

    public short getFinetune() {
        return this.finetune;
    }

    public void setFinetune(short s2) {
        this.finetune = s2;
    }

    public List<DLSSampleLoop> getLoops() {
        return this.loops;
    }

    public long getOptions() {
        return this.options;
    }

    public void setOptions(long j2) {
        this.options = j2;
    }

    public int getUnitynote() {
        return this.unitynote;
    }

    public void setUnitynote(int i2) {
        this.unitynote = i2;
    }
}
