package java.nio.charset;

/* loaded from: rt.jar:java/nio/charset/CodingErrorAction.class */
public class CodingErrorAction {
    private String name;
    public static final CodingErrorAction IGNORE = new CodingErrorAction("IGNORE");
    public static final CodingErrorAction REPLACE = new CodingErrorAction("REPLACE");
    public static final CodingErrorAction REPORT = new CodingErrorAction("REPORT");

    private CodingErrorAction(String str) {
        this.name = str;
    }

    public String toString() {
        return this.name;
    }
}
