package javax.swing.text;

import java.io.IOException;

/* loaded from: rt.jar:javax/swing/text/ChangedCharSetException.class */
public class ChangedCharSetException extends IOException {
    String charSetSpec;
    boolean charSetKey;

    public ChangedCharSetException(String str, boolean z2) {
        this.charSetSpec = str;
        this.charSetKey = z2;
    }

    public String getCharSetSpec() {
        return this.charSetSpec;
    }

    public boolean keyEqualsCharSet() {
        return this.charSetKey;
    }
}
