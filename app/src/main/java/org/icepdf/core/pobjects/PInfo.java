package org.icepdf.core.pobjects;

import java.util.HashMap;
import org.icepdf.core.pobjects.fonts.ofont.Encoding;
import org.icepdf.core.pobjects.security.SecurityManager;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/PInfo.class */
public class PInfo extends Dictionary {
    public static final Name RESOURCES_KEY = new Name("Resources");
    public static final Name TITLE_KEY = new Name("Title");
    public static final Name AUTHOR_KEY = new Name("Author");
    public static final Name SUBJECT_KEY = new Name("Subject");
    public static final Name KEYWORDS_KEY = new Name("Keywords");
    public static final Name CREATOR_KEY = new Name("Creator");
    public static final Name PRODUCER_KEY = new Name("Producer");
    public static final Name CREATIONDATE_KEY = new Name("CreationDate");
    public static final Name MODDATE_KEY = new Name("ModDate");
    public static final Name TRAPPED_KEY = new Name("Trapped");
    private SecurityManager securityManager;

    public PInfo(Library library, HashMap entries) {
        super(library, entries);
        this.securityManager = library.getSecurityManager();
    }

    public Object getCustomExtension(Name name) {
        Object value = this.library.getObject(this.entries, name);
        if (value != null && (value instanceof StringObject)) {
            StringObject text = (StringObject) value;
            return cleanString(text.getDecryptedLiteralString(this.securityManager));
        }
        return value;
    }

    public String getTitle() {
        Object value = this.library.getObject(this.entries, TITLE_KEY);
        if (value != null && (value instanceof StringObject)) {
            StringObject text = (StringObject) value;
            return cleanString(text.getDecryptedLiteralString(this.securityManager));
        }
        if (value instanceof String) {
            return (String) value;
        }
        return null;
    }

    public String getAuthor() {
        Object value = this.library.getObject(this.entries, AUTHOR_KEY);
        if (value != null && (value instanceof StringObject)) {
            StringObject text = (StringObject) value;
            return cleanString(text.getDecryptedLiteralString(this.securityManager));
        }
        if (value instanceof String) {
            return (String) value;
        }
        return null;
    }

    public String getSubject() {
        Object value = this.library.getObject(this.entries, SUBJECT_KEY);
        if (value != null && (value instanceof StringObject)) {
            StringObject text = (StringObject) value;
            return cleanString(text.getDecryptedLiteralString(this.securityManager));
        }
        if (value instanceof String) {
            return (String) value;
        }
        return null;
    }

    public String getKeywords() {
        Object value = this.library.getObject(this.entries, KEYWORDS_KEY);
        if (value != null && (value instanceof StringObject)) {
            StringObject text = (StringObject) value;
            return cleanString(text.getDecryptedLiteralString(this.securityManager));
        }
        if (value instanceof String) {
            return (String) value;
        }
        return null;
    }

    public String getCreator() {
        Object value = this.library.getObject(this.entries, CREATOR_KEY);
        if (value != null && (value instanceof StringObject)) {
            StringObject text = (StringObject) value;
            return cleanString(text.getDecryptedLiteralString(this.securityManager));
        }
        if (value instanceof String) {
            return (String) value;
        }
        return null;
    }

    public String getProducer() {
        Object value = this.library.getObject(this.entries, PRODUCER_KEY);
        if (value != null && (value instanceof StringObject)) {
            StringObject text = (StringObject) value;
            return cleanString(text.getDecryptedLiteralString(this.securityManager));
        }
        if (value instanceof String) {
            return (String) value;
        }
        return null;
    }

    public PDate getCreationDate() {
        Object value = this.library.getObject(this.entries, CREATIONDATE_KEY);
        if (value != null && (value instanceof StringObject)) {
            StringObject text = (StringObject) value;
            return new PDate(this.securityManager, text.getDecryptedLiteralString(this.securityManager));
        }
        return null;
    }

    public PDate getModDate() {
        Object value = this.library.getObject(this.entries, MODDATE_KEY);
        if (value != null && (value instanceof StringObject)) {
            StringObject text = (StringObject) value;
            return new PDate(this.securityManager, text.getDecryptedLiteralString(this.securityManager));
        }
        return null;
    }

    public String getTrappingInformation() {
        Object value = this.library.getObject(this.entries, TRAPPED_KEY);
        if (value != null && (value instanceof StringObject)) {
            StringObject text = (StringObject) value;
            return cleanString(text.getDecryptedLiteralString(this.securityManager));
        }
        if (value instanceof String) {
            return (String) value;
        }
        return null;
    }

    private String cleanString(String text) {
        String text2;
        if (text != null && text.length() > 0) {
            if (text.charAt(0) == 254 && text.charAt(1) == 255) {
                StringBuilder sb1 = new StringBuilder();
                String hexTmp = "";
                for (int i2 = 0; i2 < text.length(); i2++) {
                    char c2 = text.charAt(i2);
                    if (c2 != '\t' && c2 != '\r' && c2 != '\n') {
                        hexTmp = hexTmp + text.charAt(i2);
                    }
                }
                byte[] title1 = hexTmp.getBytes();
                for (int i3 = 2; i3 < title1.length; i3 += 2) {
                    try {
                        int b1 = title1[i3] & 255;
                        int b2 = title1[i3 + 1] & 255;
                        sb1.append((char) ((b1 * 256) + b2));
                    } catch (Exception e2) {
                    }
                }
                text2 = sb1.toString();
            } else {
                StringBuilder sb = new StringBuilder();
                Encoding enc = Encoding.getPDFDoc();
                for (int i4 = 0; i4 < text.length(); i4++) {
                    sb.append(enc.get(text.charAt(i4)));
                }
                text2 = sb.toString();
            }
            return text2;
        }
        return "";
    }
}
