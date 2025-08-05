package com.sun.media.sound;

import java.util.ArrayList;
import java.util.List;

/* loaded from: rt.jar:com/sun/media/sound/DLSRegion.class */
public final class DLSRegion {
    public static final int OPTION_SELFNONEXCLUSIVE = 1;
    int keyfrom;
    int keyto;
    int velfrom;
    int velto;
    int options;
    int exclusiveClass;
    int fusoptions;
    int phasegroup;
    long channel;
    DLSSampleOptions sampleoptions;
    List<DLSModulator> modulators = new ArrayList();
    DLSSample sample = null;

    public List<DLSModulator> getModulators() {
        return this.modulators;
    }

    public long getChannel() {
        return this.channel;
    }

    public void setChannel(long j2) {
        this.channel = j2;
    }

    public int getExclusiveClass() {
        return this.exclusiveClass;
    }

    public void setExclusiveClass(int i2) {
        this.exclusiveClass = i2;
    }

    public int getFusoptions() {
        return this.fusoptions;
    }

    public void setFusoptions(int i2) {
        this.fusoptions = i2;
    }

    public int getKeyfrom() {
        return this.keyfrom;
    }

    public void setKeyfrom(int i2) {
        this.keyfrom = i2;
    }

    public int getKeyto() {
        return this.keyto;
    }

    public void setKeyto(int i2) {
        this.keyto = i2;
    }

    public int getOptions() {
        return this.options;
    }

    public void setOptions(int i2) {
        this.options = i2;
    }

    public int getPhasegroup() {
        return this.phasegroup;
    }

    public void setPhasegroup(int i2) {
        this.phasegroup = i2;
    }

    public DLSSample getSample() {
        return this.sample;
    }

    public void setSample(DLSSample dLSSample) {
        this.sample = dLSSample;
    }

    public int getVelfrom() {
        return this.velfrom;
    }

    public void setVelfrom(int i2) {
        this.velfrom = i2;
    }

    public int getVelto() {
        return this.velto;
    }

    public void setVelto(int i2) {
        this.velto = i2;
    }

    public void setModulators(List<DLSModulator> list) {
        this.modulators = list;
    }

    public DLSSampleOptions getSampleoptions() {
        return this.sampleoptions;
    }

    public void setSampleoptions(DLSSampleOptions dLSSampleOptions) {
        this.sampleoptions = dLSSampleOptions;
    }
}
