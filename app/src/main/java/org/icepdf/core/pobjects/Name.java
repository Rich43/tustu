package org.icepdf.core.pobjects;

import java.util.logging.Logger;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/Name.class */
public class Name {
    private static final Logger logger = Logger.getLogger(Name.class.toString());
    private static final int HEX_CHAR = 35;
    private String name;

    public Name(String name) {
        if (name != null) {
            this.name = convertHexChars(new StringBuilder(name));
        }
    }

    public Name(StringBuilder name) {
        this.name = convertHexChars(name);
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Name) {
            return equals((Name) obj);
        }
        return obj != null && this.name.equals(obj);
    }

    public boolean equals(Name obj) {
        return obj != null && this.name.equals(obj.getName());
    }

    public boolean equals(String obj) {
        return obj != null && this.name.equals(obj);
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    private String convertHexChars(StringBuilder name) {
        for (int i2 = 0; i2 < name.length(); i2++) {
            try {
                if (name.charAt(i2) == '#') {
                    String hex = name.substring(i2 + 1, i2 + 3);
                    name.delete(i2, i2 + 3);
                    int charDd = Integer.parseInt(hex, 16);
                    if (charDd <= 127) {
                        name.insert(i2, (char) charDd);
                    } else {
                        name.insert(i2, convert(hex));
                    }
                }
            } catch (Throwable th) {
                logger.finer("Error parsing hexadecimal characters.");
                return name.toString();
            }
        }
        return name.toString();
    }

    private String convert(String hex) {
        StringBuilder output = new StringBuilder();
        output.append("\\u");
        int max = 4 - hex.length();
        for (int j2 = 0; j2 < max; j2++) {
            output.append("0");
        }
        output.append(hex.toLowerCase());
        return output.toString();
    }
}
