package org.icepdf.core.pobjects.acroform;

import com.sun.org.glassfish.external.amx.AMX;
import java.util.HashMap;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.StringObject;
import org.icepdf.core.pobjects.security.SecurityManager;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/acroform/FieldDictionary.class */
public class FieldDictionary extends Dictionary {
    private static final Logger logger = Logger.getLogger(FieldDictionary.class.toString());
    public static final Name FT_KEY = new Name("FT");
    public static final Name KIDS_KEY = new Name("Kids");
    public static final Name PARENT_KEY = new Name(AMX.ATTR_PARENT);
    public static final Name T_KEY = new Name("T");
    public static final Name TU_KEY = new Name("TU");
    public static final Name TM_KEY = new Name("TM");
    public static final Name Ff_KEY = new Name("Ff");
    public static final Name V_KEY = new Name("V");
    public static final Name DV_KEY = new Name("DV");
    public static final Name AA_KEY = new Name("AA");
    private SecurityManager securityManager;
    protected Name fieldType;
    protected String partialFieldName;
    protected String alternativeFieldName;
    protected String exportMappingName;
    protected int flags;
    protected String fieldValue;

    public FieldDictionary(Library library, HashMap entries) {
        super(library, entries);
        this.securityManager = library.getSecurityManager();
        Object value = library.getName(entries, FT_KEY);
        if (value != null) {
            this.fieldType = (Name) value;
        }
        Object value2 = library.getObject(entries, T_KEY);
        if (value2 != null && (value2 instanceof StringObject)) {
            StringObject text = (StringObject) value2;
            this.partialFieldName = text.getDecryptedLiteralString(this.securityManager);
        } else if (value2 instanceof String) {
            this.partialFieldName = (String) value2;
        }
        Object value3 = library.getObject(entries, TU_KEY);
        if (value3 != null && (value3 instanceof StringObject)) {
            StringObject text2 = (StringObject) value3;
            this.alternativeFieldName = text2.getDecryptedLiteralString(this.securityManager);
        } else if (value3 instanceof String) {
            this.alternativeFieldName = (String) value3;
        }
        Object value4 = library.getObject(entries, V_KEY);
        if (value4 != null && (value4 instanceof StringObject)) {
            StringObject text3 = (StringObject) value4;
            this.fieldValue = text3.getDecryptedLiteralString(this.securityManager);
        } else if (value4 instanceof String) {
            this.fieldValue = (String) value4;
        }
    }
}
