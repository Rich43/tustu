package jdk.internal.util.xml.impl;

import jdk.internal.org.xml.sax.Attributes;

/* loaded from: rt.jar:jdk/internal/util/xml/impl/Attrs.class */
public class Attrs implements Attributes {
    private char mLength;
    private char mAttrIdx = 0;
    String[] mItems = new String[64];

    public void setLength(char c2) {
        if (c2 > ((char) (this.mItems.length >> 3))) {
            this.mItems = new String[c2 << 3];
        }
        this.mLength = c2;
    }

    @Override // jdk.internal.org.xml.sax.Attributes
    public int getLength() {
        return this.mLength;
    }

    @Override // jdk.internal.org.xml.sax.Attributes
    public String getURI(int i2) {
        if (i2 < 0 || i2 >= this.mLength) {
            return null;
        }
        return this.mItems[i2 << 3];
    }

    @Override // jdk.internal.org.xml.sax.Attributes
    public String getLocalName(int i2) {
        if (i2 < 0 || i2 >= this.mLength) {
            return null;
        }
        return this.mItems[(i2 << 3) + 2];
    }

    @Override // jdk.internal.org.xml.sax.Attributes
    public String getQName(int i2) {
        if (i2 < 0 || i2 >= this.mLength) {
            return null;
        }
        return this.mItems[(i2 << 3) + 1];
    }

    @Override // jdk.internal.org.xml.sax.Attributes
    public String getType(int i2) {
        if (i2 < 0 || i2 >= (this.mItems.length >> 3)) {
            return null;
        }
        return this.mItems[(i2 << 3) + 4];
    }

    @Override // jdk.internal.org.xml.sax.Attributes
    public String getValue(int i2) {
        if (i2 < 0 || i2 >= this.mLength) {
            return null;
        }
        return this.mItems[(i2 << 3) + 3];
    }

    @Override // jdk.internal.org.xml.sax.Attributes
    public int getIndex(String str, String str2) {
        char c2 = this.mLength;
        char c3 = 0;
        while (true) {
            char c4 = c3;
            if (c4 < c2) {
                if (!this.mItems[c4 << 3].equals(str) || !this.mItems[(c4 << 3) + 2].equals(str2)) {
                    c3 = (char) (c4 + 1);
                } else {
                    return c4;
                }
            } else {
                return -1;
            }
        }
    }

    int getIndexNullNS(String str, String str2) {
        char c2 = this.mLength;
        if (str != null) {
            char c3 = 0;
            while (true) {
                char c4 = c3;
                if (c4 < c2) {
                    if (!this.mItems[c4 << 3].equals(str) || !this.mItems[(c4 << 3) + 2].equals(str2)) {
                        c3 = (char) (c4 + 1);
                    } else {
                        return c4;
                    }
                } else {
                    return -1;
                }
            }
        } else {
            char c5 = 0;
            while (true) {
                char c6 = c5;
                if (c6 < c2) {
                    if (!this.mItems[(c6 << 3) + 2].equals(str2)) {
                        c5 = (char) (c6 + 1);
                    } else {
                        return c6;
                    }
                } else {
                    return -1;
                }
            }
        }
    }

    @Override // jdk.internal.org.xml.sax.Attributes
    public int getIndex(String str) {
        char c2 = this.mLength;
        char c3 = 0;
        while (true) {
            char c4 = c3;
            if (c4 < c2) {
                if (!this.mItems[(c4 << 3) + 1].equals(str)) {
                    c3 = (char) (c4 + 1);
                } else {
                    return c4;
                }
            } else {
                return -1;
            }
        }
    }

    @Override // jdk.internal.org.xml.sax.Attributes
    public String getType(String str, String str2) {
        int index = getIndex(str, str2);
        if (index >= 0) {
            return this.mItems[(index << 3) + 4];
        }
        return null;
    }

    @Override // jdk.internal.org.xml.sax.Attributes
    public String getType(String str) {
        int index = getIndex(str);
        if (index >= 0) {
            return this.mItems[(index << 3) + 4];
        }
        return null;
    }

    @Override // jdk.internal.org.xml.sax.Attributes
    public String getValue(String str, String str2) {
        int index = getIndex(str, str2);
        if (index >= 0) {
            return this.mItems[(index << 3) + 3];
        }
        return null;
    }

    @Override // jdk.internal.org.xml.sax.Attributes
    public String getValue(String str) {
        int index = getIndex(str);
        if (index >= 0) {
            return this.mItems[(index << 3) + 3];
        }
        return null;
    }

    public boolean isDeclared(int i2) {
        if (i2 < 0 || i2 >= this.mLength) {
            throw new ArrayIndexOutOfBoundsException("");
        }
        return this.mItems[(i2 << 3) + 5] != null;
    }

    public boolean isDeclared(String str) {
        int index = getIndex(str);
        if (index < 0) {
            throw new IllegalArgumentException("");
        }
        return this.mItems[(index << 3) + 5] != null;
    }

    public boolean isDeclared(String str, String str2) {
        int index = getIndex(str, str2);
        if (index < 0) {
            throw new IllegalArgumentException("");
        }
        return this.mItems[(index << 3) + 5] != null;
    }

    public boolean isSpecified(int i2) {
        if (i2 < 0 || i2 >= this.mLength) {
            throw new ArrayIndexOutOfBoundsException("");
        }
        String str = this.mItems[(i2 << 3) + 5];
        return str == null || str.charAt(0) == 'd';
    }

    public boolean isSpecified(String str, String str2) {
        int index = getIndex(str, str2);
        if (index < 0) {
            throw new IllegalArgumentException("");
        }
        String str3 = this.mItems[(index << 3) + 5];
        return str3 == null || str3.charAt(0) == 'd';
    }

    public boolean isSpecified(String str) {
        int index = getIndex(str);
        if (index < 0) {
            throw new IllegalArgumentException("");
        }
        String str2 = this.mItems[(index << 3) + 5];
        return str2 == null || str2.charAt(0) == 'd';
    }
}
