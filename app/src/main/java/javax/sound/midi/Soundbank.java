package javax.sound.midi;

/* loaded from: rt.jar:javax/sound/midi/Soundbank.class */
public interface Soundbank {
    String getName();

    String getVersion();

    String getVendor();

    String getDescription();

    SoundbankResource[] getResources();

    Instrument[] getInstruments();

    Instrument getInstrument(Patch patch);
}
