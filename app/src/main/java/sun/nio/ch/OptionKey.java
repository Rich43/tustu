package sun.nio.ch;

/* loaded from: rt.jar:sun/nio/ch/OptionKey.class */
class OptionKey {
    private int level;
    private int name;

    OptionKey(int i2, int i3) {
        this.level = i2;
        this.name = i3;
    }

    int level() {
        return this.level;
    }

    int name() {
        return this.name;
    }
}
