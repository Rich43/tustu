package org.icepdf.core.pobjects;

import java.util.HashMap;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/ViewerPreferences.class */
public class ViewerPreferences extends Dictionary {
    private NameNode root;

    public ViewerPreferences(Library l2, HashMap h2) {
        super(l2, h2);
    }

    @Override // org.icepdf.core.pobjects.Dictionary
    public void init() {
        if (this.inited) {
            return;
        }
        this.root = new NameNode(this.library, this.entries);
        this.inited = true;
    }

    public NameNode getRoot() {
        return this.root;
    }

    public boolean hasHideToolbar() {
        return this.library.isValidEntry(this.entries, new Name("HideToolbar"));
    }

    public boolean hasHideMenubar() {
        return this.library.isValidEntry(this.entries, new Name("HideMenubar"));
    }

    public boolean hasFitWindow() {
        return this.library.isValidEntry(this.entries, new Name("FitWindow"));
    }

    public boolean getHideToolbar() {
        return this.library.getBoolean(this.entries, new Name("HideToolbar")).booleanValue();
    }

    public boolean getHideMenubar() {
        return this.library.getBoolean(this.entries, new Name("HideMenubar")).booleanValue();
    }

    public boolean getFitWindow() {
        return this.library.getBoolean(this.entries, new Name("FitWindow")).booleanValue();
    }
}
