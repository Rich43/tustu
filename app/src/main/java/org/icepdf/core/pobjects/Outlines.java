package org.icepdf.core.pobjects;

import java.util.HashMap;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PdfOps;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/Outlines.class */
public class Outlines extends Dictionary {
    public static final Name D_KEY = new Name(PdfOps.D_TOKEN);
    public static final Name COUNT_KEY = new Name("Count");
    private Integer count;

    public Outlines(Library l2, HashMap h2) {
        super(l2, h2);
        if (this.entries != null) {
            this.count = Integer.valueOf(this.library.getInt(this.entries, COUNT_KEY));
        }
    }

    public OutlineItem getRootOutlineItem() {
        if (this.count == null) {
            return null;
        }
        return new OutlineItem(this.library, this.entries);
    }
}
