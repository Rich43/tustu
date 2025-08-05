package sun.swing;

/* loaded from: rt.jar:sun/swing/StringUIClientPropertyKey.class */
public class StringUIClientPropertyKey implements UIClientPropertyKey {
    private final String key;

    public StringUIClientPropertyKey(String str) {
        this.key = str;
    }

    public String toString() {
        return this.key;
    }
}
