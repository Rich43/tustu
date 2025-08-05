package javax.sound.midi;

/* loaded from: rt.jar:javax/sound/midi/SoundbankResource.class */
public abstract class SoundbankResource {
    private final Soundbank soundBank;
    private final String name;
    private final Class dataClass;

    public abstract Object getData();

    protected SoundbankResource(Soundbank soundbank, String str, Class<?> cls) {
        this.soundBank = soundbank;
        this.name = str;
        this.dataClass = cls;
    }

    public Soundbank getSoundbank() {
        return this.soundBank;
    }

    public String getName() {
        return this.name;
    }

    public Class<?> getDataClass() {
        return this.dataClass;
    }
}
