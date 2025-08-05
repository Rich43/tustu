package org.icepdf.core.pobjects.security;

import java.util.HashMap;
import java.util.Set;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/security/CryptFilter.class */
public class CryptFilter extends Dictionary {
    public HashMap<Name, CryptFilterEntry> cryptFilters;

    public CryptFilter(Library library, HashMap entries) {
        super(library, entries);
    }

    public CryptFilterEntry getCryptFilterByName(Name cryptFilterName) {
        if (this.cryptFilters == null) {
            this.cryptFilters = new HashMap<>(1);
            Set cryptKeys = this.entries.keySet();
            for (Object name : cryptKeys) {
                this.cryptFilters.put((Name) name, new CryptFilterEntry(this.library, (HashMap) this.entries.get(name)));
            }
        }
        return this.cryptFilters.get(cryptFilterName);
    }
}
