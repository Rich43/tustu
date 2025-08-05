package sun.util.locale;

/* loaded from: rt.jar:sun/util/locale/Extension.class */
class Extension {
    private final char key;
    private String value;
    private String id;

    protected Extension(char c2) {
        this.key = c2;
    }

    Extension(char c2, String str) {
        this.key = c2;
        setValue(str);
    }

    protected void setValue(String str) {
        this.value = str;
        this.id = this.key + LanguageTag.SEP + str;
    }

    public char getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    public String getID() {
        return this.id;
    }

    public String toString() {
        return getID();
    }
}
