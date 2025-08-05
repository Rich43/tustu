package org.icepdf.core.pobjects;

import java.util.HashMap;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/NameTree.class */
public class NameTree extends Dictionary {
    private NameNode root;

    public NameTree(Library l2, HashMap h2) {
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

    public Object searchName(String key) {
        return this.root.searchName(key);
    }

    public NameNode getRoot() {
        return this.root;
    }
}
