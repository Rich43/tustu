package org.icepdf.core.pobjects.actions;

import com.sun.glass.ui.Platform;
import java.util.HashMap;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.StringObject;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PdfOps;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/actions/FileSpecification.class */
public class FileSpecification extends Dictionary {
    public static final Name FS_KEY = new Name("FS");
    public static final Name F_KEY = new Name(PdfOps.F_TOKEN);
    public static final Name UF_KEY = new Name("UF");
    public static final Name DOS_KEY = new Name("DOS");
    public static final Name MAC_KEY = new Name(Platform.MAC);
    public static final Name UNIX_KEY = new Name("Unix");
    public static final Name ID_KEY = new Name("ID");
    public static final Name V_KEY = new Name("V");
    public static final Name EF_KEY = new Name("EF");
    public static final Name RF_KEY = new Name("RF");
    public static final Name DESC_KEY = new Name("Desc");
    public static final Name CI_KEY = new Name("CI");

    public FileSpecification(Library l2, HashMap h2) {
        super(l2, h2);
    }

    public Name getType() {
        return this.library.getName(this.entries, TYPE_KEY);
    }

    public Name getFileSystemName() {
        return this.library.getName(this.entries, FS_KEY);
    }

    public String getFileSpecification() {
        Object tmp = this.library.getObject(this.entries, F_KEY);
        if (tmp instanceof StringObject) {
            return ((StringObject) tmp).getDecryptedLiteralString(this.library.getSecurityManager());
        }
        return null;
    }

    public String getUnicodeFileSpecification() {
        Object tmp = this.library.getObject(this.entries, UF_KEY);
        if (tmp instanceof StringObject) {
            return ((StringObject) tmp).getDecryptedLiteralString(this.library.getSecurityManager());
        }
        return null;
    }

    public String getDos() {
        Object tmp = this.library.getObject(this.entries, DOS_KEY);
        if (tmp instanceof StringObject) {
            return ((StringObject) tmp).getDecryptedLiteralString(this.library.getSecurityManager());
        }
        return null;
    }

    public String getMac() {
        Object tmp = this.library.getObject(this.entries, MAC_KEY);
        if (tmp instanceof StringObject) {
            return ((StringObject) tmp).getDecryptedLiteralString(this.library.getSecurityManager());
        }
        return null;
    }

    public String getUnix() {
        Object tmp = this.library.getObject(this.entries, UNIX_KEY);
        if (tmp instanceof StringObject) {
            return ((StringObject) tmp).getDecryptedLiteralString(this.library.getSecurityManager());
        }
        return null;
    }

    public String getId() {
        Object tmp = this.library.getObject(this.entries, ID_KEY);
        if (tmp != null) {
            return tmp.toString();
        }
        return null;
    }

    public Boolean isVolitile() {
        return this.library.getBoolean(this.entries, V_KEY);
    }

    public HashMap getEmbeddedFileDictionary() {
        return this.library.getDictionary(this.entries, EF_KEY);
    }

    public HashMap getRelatedFilesDictionary() {
        return this.library.getDictionary(this.entries, RF_KEY);
    }

    public String getDescription() {
        Object description = this.library.getObject(this.entries, DESC_KEY);
        if (description instanceof StringObject) {
            StringObject tmp = (StringObject) description;
            return tmp.getDecryptedLiteralString(this.library.securityManager);
        }
        if (description instanceof String) {
            return description.toString();
        }
        return null;
    }

    public HashMap getCollectionItemDictionary() {
        return this.library.getDictionary(this.entries, CI_KEY);
    }
}
