package com.sun.media.sound;

/* loaded from: rt.jar:com/sun/media/sound/ModelAbstractChannelMixer.class */
public abstract class ModelAbstractChannelMixer implements ModelChannelMixer {
    @Override // com.sun.media.sound.ModelChannelMixer
    public abstract boolean process(float[][] fArr, int i2, int i3);

    @Override // com.sun.media.sound.ModelChannelMixer
    public abstract void stop();

    @Override // javax.sound.midi.MidiChannel
    public void allNotesOff() {
    }

    @Override // javax.sound.midi.MidiChannel
    public void allSoundOff() {
    }

    @Override // javax.sound.midi.MidiChannel
    public void controlChange(int i2, int i3) {
    }

    @Override // javax.sound.midi.MidiChannel
    public int getChannelPressure() {
        return 0;
    }

    @Override // javax.sound.midi.MidiChannel
    public int getController(int i2) {
        return 0;
    }

    @Override // javax.sound.midi.MidiChannel
    public boolean getMono() {
        return false;
    }

    @Override // javax.sound.midi.MidiChannel
    public boolean getMute() {
        return false;
    }

    @Override // javax.sound.midi.MidiChannel
    public boolean getOmni() {
        return false;
    }

    @Override // javax.sound.midi.MidiChannel
    public int getPitchBend() {
        return 0;
    }

    @Override // javax.sound.midi.MidiChannel
    public int getPolyPressure(int i2) {
        return 0;
    }

    @Override // javax.sound.midi.MidiChannel
    public int getProgram() {
        return 0;
    }

    @Override // javax.sound.midi.MidiChannel
    public boolean getSolo() {
        return false;
    }

    @Override // javax.sound.midi.MidiChannel
    public boolean localControl(boolean z2) {
        return false;
    }

    @Override // javax.sound.midi.MidiChannel
    public void noteOff(int i2) {
    }

    @Override // javax.sound.midi.MidiChannel
    public void noteOff(int i2, int i3) {
    }

    @Override // javax.sound.midi.MidiChannel
    public void noteOn(int i2, int i3) {
    }

    @Override // javax.sound.midi.MidiChannel
    public void programChange(int i2) {
    }

    @Override // javax.sound.midi.MidiChannel
    public void programChange(int i2, int i3) {
    }

    @Override // javax.sound.midi.MidiChannel
    public void resetAllControllers() {
    }

    @Override // javax.sound.midi.MidiChannel
    public void setChannelPressure(int i2) {
    }

    @Override // javax.sound.midi.MidiChannel
    public void setMono(boolean z2) {
    }

    @Override // javax.sound.midi.MidiChannel
    public void setMute(boolean z2) {
    }

    @Override // javax.sound.midi.MidiChannel
    public void setOmni(boolean z2) {
    }

    @Override // javax.sound.midi.MidiChannel
    public void setPitchBend(int i2) {
    }

    @Override // javax.sound.midi.MidiChannel
    public void setPolyPressure(int i2, int i3) {
    }

    @Override // javax.sound.midi.MidiChannel
    public void setSolo(boolean z2) {
    }
}
