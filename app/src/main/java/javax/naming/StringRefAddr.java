package javax.naming;

/* loaded from: rt.jar:javax/naming/StringRefAddr.class */
public class StringRefAddr extends RefAddr {
    private String contents;
    private static final long serialVersionUID = -8913762495138505527L;

    public StringRefAddr(String str, String str2) {
        super(str);
        this.contents = str2;
    }

    @Override // javax.naming.RefAddr
    public Object getContent() {
        return this.contents;
    }
}
