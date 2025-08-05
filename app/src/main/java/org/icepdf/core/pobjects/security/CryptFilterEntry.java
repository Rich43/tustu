package org.icepdf.core.pobjects.security;

import java.util.HashMap;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/security/CryptFilterEntry.class */
public class CryptFilterEntry extends Dictionary {
    public static final Name TYPE = new Name("CryptFilter");
    public static final Name AUTHEVENT_KEY = new Name("AuthEvent");
    public static final Name CFM_KEY = new Name("CFM");
    public static final Name LENGTH_KEY = new Name("Length");

    public CryptFilterEntry(Library library, HashMap entries) {
        super(library, entries);
    }

    public Name getType() {
        return TYPE;
    }

    public Name getCryptFilterMethod() {
        Object tmp = this.library.getObject(this.entries, CFM_KEY);
        if (tmp instanceof Name) {
            return (Name) tmp;
        }
        return null;
    }

    public Name getAuthEvent() {
        Object tmp = this.library.getObject(this.entries, AUTHEVENT_KEY);
        if (tmp instanceof Name) {
            return (Name) tmp;
        }
        return null;
    }

    public int getLength() {
        int length = this.library.getInt(this.entries, LENGTH_KEY);
        return Math.min(length * 8, 128);
    }
}
