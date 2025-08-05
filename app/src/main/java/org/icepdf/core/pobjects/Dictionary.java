package org.icepdf.core.pobjects;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.util.HashMap;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/Dictionary.class */
public class Dictionary {
    public static final Name TYPE_KEY = new Name(Constants._ATT_TYPE);
    public static final Name SUBTYPE_KEY = new Name("Subtype");
    public static final Name LENGTH_KEY = new Name("Length");
    public static final Name FORM_TYPE_KEY = new Name("FormType");
    protected Library library;
    protected HashMap<Object, Object> entries;
    protected boolean inited;
    protected boolean isDeleted;
    protected boolean isNew;
    private Reference pObjectReference;

    public Dictionary(Library library, HashMap entries) {
        this.library = library;
        this.entries = entries;
        if (this.entries == null) {
            this.entries = new HashMap<>();
        }
    }

    public void setPObjectReference(Reference reference) {
        this.pObjectReference = reference;
    }

    public Reference getPObjectReference() {
        return this.pObjectReference;
    }

    public void init() {
    }

    public HashMap<Object, Object> getEntries() {
        return this.entries;
    }

    public Object getObject(Name key) {
        return this.library.getObject(this.entries, key);
    }

    protected Number getNumber(Name key) {
        return this.library.getNumber(this.entries, key);
    }

    public int getInt(Name key) {
        return this.library.getInt(this.entries, key);
    }

    public float getFloat(Name key) {
        return this.library.getFloat(this.entries, key);
    }

    public Library getLibrary() {
        return this.library;
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    public boolean isNew() {
        return this.isNew;
    }

    public void setNew(boolean aNew) {
        this.isNew = aNew;
    }

    public String toString() {
        return getClass().getName() + "=" + this.entries.toString();
    }
}
