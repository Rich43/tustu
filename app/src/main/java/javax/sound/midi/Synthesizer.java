package javax.sound.midi;

/* loaded from: rt.jar:javax/sound/midi/Synthesizer.class */
public interface Synthesizer extends MidiDevice {
    int getMaxPolyphony();

    long getLatency();

    MidiChannel[] getChannels();

    VoiceStatus[] getVoiceStatus();

    boolean isSoundbankSupported(Soundbank soundbank);

    boolean loadInstrument(Instrument instrument);

    void unloadInstrument(Instrument instrument);

    boolean remapInstrument(Instrument instrument, Instrument instrument2);

    Soundbank getDefaultSoundbank();

    Instrument[] getAvailableInstruments();

    Instrument[] getLoadedInstruments();

    boolean loadAllInstruments(Soundbank soundbank);

    void unloadAllInstruments(Soundbank soundbank);

    boolean loadInstruments(Soundbank soundbank, Patch[] patchArr);

    void unloadInstruments(Soundbank soundbank, Patch[] patchArr);
}
