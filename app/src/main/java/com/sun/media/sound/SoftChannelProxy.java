package com.sun.media.sound;

import javax.sound.midi.MidiChannel;

/* loaded from: rt.jar:com/sun/media/sound/SoftChannelProxy.class */
public final class SoftChannelProxy implements MidiChannel {
    private MidiChannel channel = null;

    public MidiChannel getChannel() {
        return this.channel;
    }

    public void setChannel(MidiChannel midiChannel) {
        this.channel = midiChannel;
    }

    @Override // javax.sound.midi.MidiChannel
    public void allNotesOff() {
        if (this.channel == null) {
            return;
        }
        this.channel.allNotesOff();
    }

    @Override // javax.sound.midi.MidiChannel
    public void allSoundOff() {
        if (this.channel == null) {
            return;
        }
        this.channel.allSoundOff();
    }

    @Override // javax.sound.midi.MidiChannel
    public void controlChange(int i2, int i3) {
        if (this.channel == null) {
            return;
        }
        this.channel.controlChange(i2, i3);
    }

    @Override // javax.sound.midi.MidiChannel
    public int getChannelPressure() {
        if (this.channel == null) {
            return 0;
        }
        return this.channel.getChannelPressure();
    }

    @Override // javax.sound.midi.MidiChannel
    public int getController(int i2) {
        if (this.channel == null) {
            return 0;
        }
        return this.channel.getController(i2);
    }

    @Override // javax.sound.midi.MidiChannel
    public boolean getMono() {
        if (this.channel == null) {
            return false;
        }
        return this.channel.getMono();
    }

    @Override // javax.sound.midi.MidiChannel
    public boolean getMute() {
        if (this.channel == null) {
            return false;
        }
        return this.channel.getMute();
    }

    @Override // javax.sound.midi.MidiChannel
    public boolean getOmni() {
        if (this.channel == null) {
            return false;
        }
        return this.channel.getOmni();
    }

    @Override // javax.sound.midi.MidiChannel
    public int getPitchBend() {
        if (this.channel == null) {
            return 8192;
        }
        return this.channel.getPitchBend();
    }

    @Override // javax.sound.midi.MidiChannel
    public int getPolyPressure(int i2) {
        if (this.channel == null) {
            return 0;
        }
        return this.channel.getPolyPressure(i2);
    }

    @Override // javax.sound.midi.MidiChannel
    public int getProgram() {
        if (this.channel == null) {
            return 0;
        }
        return this.channel.getProgram();
    }

    @Override // javax.sound.midi.MidiChannel
    public boolean getSolo() {
        if (this.channel == null) {
            return false;
        }
        return this.channel.getSolo();
    }

    @Override // javax.sound.midi.MidiChannel
    public boolean localControl(boolean z2) {
        if (this.channel == null) {
            return false;
        }
        return this.channel.localControl(z2);
    }

    @Override // javax.sound.midi.MidiChannel
    public void noteOff(int i2) {
        if (this.channel == null) {
            return;
        }
        this.channel.noteOff(i2);
    }

    @Override // javax.sound.midi.MidiChannel
    public void noteOff(int i2, int i3) {
        if (this.channel == null) {
            return;
        }
        this.channel.noteOff(i2, i3);
    }

    @Override // javax.sound.midi.MidiChannel
    public void noteOn(int i2, int i3) {
        if (this.channel == null) {
            return;
        }
        this.channel.noteOn(i2, i3);
    }

    @Override // javax.sound.midi.MidiChannel
    public void programChange(int i2) {
        if (this.channel == null) {
            return;
        }
        this.channel.programChange(i2);
    }

    @Override // javax.sound.midi.MidiChannel
    public void programChange(int i2, int i3) {
        if (this.channel == null) {
            return;
        }
        this.channel.programChange(i2, i3);
    }

    @Override // javax.sound.midi.MidiChannel
    public void resetAllControllers() {
        if (this.channel == null) {
            return;
        }
        this.channel.resetAllControllers();
    }

    @Override // javax.sound.midi.MidiChannel
    public void setChannelPressure(int i2) {
        if (this.channel == null) {
            return;
        }
        this.channel.setChannelPressure(i2);
    }

    @Override // javax.sound.midi.MidiChannel
    public void setMono(boolean z2) {
        if (this.channel == null) {
            return;
        }
        this.channel.setMono(z2);
    }

    @Override // javax.sound.midi.MidiChannel
    public void setMute(boolean z2) {
        if (this.channel == null) {
            return;
        }
        this.channel.setMute(z2);
    }

    @Override // javax.sound.midi.MidiChannel
    public void setOmni(boolean z2) {
        if (this.channel == null) {
            return;
        }
        this.channel.setOmni(z2);
    }

    @Override // javax.sound.midi.MidiChannel
    public void setPitchBend(int i2) {
        if (this.channel == null) {
            return;
        }
        this.channel.setPitchBend(i2);
    }

    @Override // javax.sound.midi.MidiChannel
    public void setPolyPressure(int i2, int i3) {
        if (this.channel == null) {
            return;
        }
        this.channel.setPolyPressure(i2, i3);
    }

    @Override // javax.sound.midi.MidiChannel
    public void setSolo(boolean z2) {
        if (this.channel == null) {
            return;
        }
        this.channel.setSolo(z2);
    }
}
