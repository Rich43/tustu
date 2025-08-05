package jdk.jfr.internal.settings;

import java.util.Iterator;
import java.util.Set;
import org.icepdf.core.util.PdfOps;

/* loaded from: jfr.jar:jdk/jfr/internal/settings/BooleanValue.class */
final class BooleanValue {
    private String value;
    private boolean booleanValue;

    private BooleanValue(boolean z2) {
        this.value = "false";
        this.booleanValue = z2;
        this.value = z2 ? "true" : "false";
    }

    public String union(Set<String> set) {
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            if ("true".equals(it.next())) {
                return "true";
            }
        }
        return "false";
    }

    public void setValue(String str) {
        this.value = str;
        this.booleanValue = Boolean.valueOf(str).booleanValue();
    }

    public final String getValue() {
        return this.value;
    }

    public boolean getBoolean() {
        return this.booleanValue;
    }

    public static BooleanValue valueOf(String str) {
        if ("true".equals(str)) {
            return new BooleanValue(true);
        }
        if ("false".equals(str)) {
            return new BooleanValue(false);
        }
        throw new InternalError("Unknown default value for settings '" + str + PdfOps.SINGLE_QUOTE_TOKEN);
    }
}
