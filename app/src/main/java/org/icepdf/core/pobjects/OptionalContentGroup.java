package org.icepdf.core.pobjects;

import java.util.HashMap;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.Utils;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/OptionalContentGroup.class */
public class OptionalContentGroup extends Dictionary implements OptionalContents {
    public static final Name TYPE = new Name("OCG");
    public static final Name NAME_KEY = new Name("Name");
    public static final Name USAGE_KEY = new Name("Usage");
    private String name;
    private HashMap usage;
    private boolean visible;
    private boolean isOCG;

    public OptionalContentGroup(String name, boolean visible) {
        super(null, null);
        this.visible = true;
        this.name = name;
        this.visible = visible;
    }

    public OptionalContentGroup(Library library, HashMap entries) {
        super(library, entries);
        this.visible = true;
        this.isOCG = true;
    }

    @Override // org.icepdf.core.pobjects.OptionalContents
    public boolean isOCG() {
        return this.isOCG;
    }

    public String getName() {
        if (this.name == null) {
            this.name = Utils.convertStringObject(this.library, (StringObject) this.library.getObject(this.entries, NAME_KEY));
        }
        return this.name;
    }

    public HashMap getUsage() {
        if (this.usage == null) {
            this.usage = this.library.getDictionary(this.entries, USAGE_KEY);
        }
        return this.usage;
    }

    @Override // org.icepdf.core.pobjects.OptionalContents
    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
