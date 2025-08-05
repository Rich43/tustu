package jdk.management.jfr;

import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;

/* loaded from: jfr.jar:jdk/management/jfr/Stringifier.class */
final class Stringifier {
    private final StringBuilder sb = new StringBuilder();
    private boolean first = true;

    Stringifier() {
    }

    public void add(String str, Object obj) {
        if (this.first) {
            this.first = false;
        } else {
            this.sb.append(" ");
        }
        boolean z2 = obj instanceof String;
        this.sb.append(str).append("=");
        if (obj == null) {
            this.sb.append(FXMLLoader.NULL_KEYWORD);
            return;
        }
        if (z2) {
            this.sb.append(PdfOps.DOUBLE_QUOTE__TOKEN);
        }
        this.sb.append(obj);
        if (z2) {
            this.sb.append(PdfOps.DOUBLE_QUOTE__TOKEN);
        }
    }

    public String toString() {
        return this.sb.toString();
    }
}
