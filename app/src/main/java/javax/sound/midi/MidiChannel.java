package javax.sound.midi;

/* loaded from: rt.jar:javax/sound/midi/MidiChannel.class */
public interface MidiChannel {
    void noteOn(int i2, int i3);

    void noteOff(int i2, int i3);

    void noteOff(int i2);

    void setPolyPressure(int i2, int i3);

    int getPolyPressure(int i2);

    void setChannelPressure(int i2);

    int getChannelPressure();

    void controlChange(int i2, int i3);

    int getController(int i2);

    void programChange(int i2);

    void programChange(int i2, int i3);

    int getProgram();

    void setPitchBend(int i2);

    int getPitchBend();

    void resetAllControllers();

    void allNotesOff();

    void allSoundOff();

    boolean localControl(boolean z2);

    void setMono(boolean z2);

    boolean getMono();

    void setOmni(boolean z2);

    boolean getOmni();

    void setMute(boolean z2);

    boolean getMute();

    void setSolo(boolean z2);

    boolean getSolo();
}
