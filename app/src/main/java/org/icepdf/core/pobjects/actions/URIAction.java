package org.icepdf.core.pobjects.actions;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.util.HashMap;
import org.icepdf.core.pobjects.LiteralStringObject;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.StringObject;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/actions/URIAction.class */
public class URIAction extends Action {
    public static final Name URI_KEY = new Name(Constants._ATT_URI);
    private StringObject URI;
    private boolean isMap;

    public URIAction(Library l2, HashMap h2) {
        super(l2, h2);
    }

    public void setURI(String URI) {
        StringObject tmp = new LiteralStringObject(URI, getPObjectReference(), this.library.securityManager);
        this.entries.put(URI_KEY, tmp);
        this.URI = tmp;
    }

    public String getURI() {
        Object actionURI = getObject(URI_KEY);
        if (actionURI instanceof StringObject) {
            this.URI = (StringObject) actionURI;
        }
        return this.URI.getDecryptedLiteralString(this.library.securityManager);
    }

    public boolean isMap() {
        return this.isMap;
    }
}
