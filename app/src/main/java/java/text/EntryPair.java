package java.text;

/* loaded from: rt.jar:java/text/EntryPair.class */
final class EntryPair {
    public String entryName;
    public int value;
    public boolean fwd;

    public EntryPair(String str, int i2) {
        this(str, i2, true);
    }

    public EntryPair(String str, int i2, boolean z2) {
        this.entryName = str;
        this.value = i2;
        this.fwd = z2;
    }
}
