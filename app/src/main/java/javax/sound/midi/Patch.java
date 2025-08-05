package javax.sound.midi;

/* loaded from: rt.jar:javax/sound/midi/Patch.class */
public class Patch {
    private final int bank;
    private final int program;

    public Patch(int i2, int i3) {
        this.bank = i2;
        this.program = i3;
    }

    public int getBank() {
        return this.bank;
    }

    public int getProgram() {
        return this.program;
    }
}
