package org.icepdf.core.pobjects.graphics;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Form;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PdfOps;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/SoftMask.class */
public class SoftMask extends Dictionary {
    private static final Logger logger = Logger.getLogger(SoftMask.class.toString());
    public static final Name S_KEY = new Name(PdfOps.S_TOKEN);
    public static final Name G_KEY = new Name("G");
    public static final Name BC_KEY = new Name("BC");
    public static final String SOFT_MASK_TYPE_ALPHA = "Alpha";
    public static final String SOFT_MASK_TYPE_LUMINOSITY = "Luminosity";

    public SoftMask(Library library, HashMap dictionary) {
        super(library, dictionary);
    }

    public Name getS() {
        return this.library.getName(this.entries, S_KEY);
    }

    public Form getG() {
        Object GKey = this.library.getObject(this.entries, G_KEY);
        if (GKey != null && (GKey instanceof Form)) {
            Form smaskForm = (Form) GKey;
            smaskForm.init();
            return smaskForm;
        }
        return null;
    }

    public List<Number> getBC() {
        Object BCKey = this.library.getObject(this.entries, BC_KEY);
        if (BCKey instanceof List) {
            return (List) BCKey;
        }
        return null;
    }
}
