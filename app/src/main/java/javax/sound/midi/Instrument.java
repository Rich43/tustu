package javax.sound.midi;

/* loaded from: rt.jar:javax/sound/midi/Instrument.class */
public abstract class Instrument extends SoundbankResource {
    private final Patch patch;

    protected Instrument(Soundbank soundbank, Patch patch, String str, Class<?> cls) {
        super(soundbank, str, cls);
        this.patch = patch;
    }

    public Patch getPatch() {
        return this.patch;
    }
}
