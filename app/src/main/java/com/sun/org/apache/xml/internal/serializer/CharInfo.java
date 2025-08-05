package com.sun.org.apache.xml.internal.serializer;

import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
import com.sun.org.apache.xml.internal.serializer.utils.SystemIDResolver;
import com.sun.org.apache.xml.internal.serializer.utils.Utils;
import com.sun.org.apache.xml.internal.serializer.utils.WrappedRuntimeException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/CharInfo.class */
final class CharInfo {
    private HashMap m_charToString;
    public static final String HTML_ENTITIES_RESOURCE = "com.sun.org.apache.xml.internal.serializer.HTMLEntities";
    public static final String XML_ENTITIES_RESOURCE = "com.sun.org.apache.xml.internal.serializer.XMLEntities";
    public static final char S_HORIZONAL_TAB = '\t';
    public static final char S_LINEFEED = '\n';
    public static final char S_CARRIAGERETURN = '\r';
    final boolean onlyQuotAmpLtGt;
    private static final int ASCII_MAX = 128;
    private boolean[] isSpecialAttrASCII;
    private boolean[] isSpecialTextASCII;
    private boolean[] isCleanTextASCII;
    private int[] array_of_bits;
    private static final int SHIFT_PER_WORD = 5;
    private static final int LOW_ORDER_BITMASK = 31;
    private int firstWordNotUsed;
    private static HashMap m_getCharInfoCache = new HashMap();

    private CharInfo(String entitiesResource, String method) {
        this(entitiesResource, method, false);
    }

    private CharInfo(String entitiesResource, String method, boolean internal) throws NumberFormatException {
        BufferedReader reader;
        this.m_charToString = new HashMap();
        this.isSpecialAttrASCII = new boolean[128];
        this.isSpecialTextASCII = new boolean[128];
        this.isCleanTextASCII = new boolean[128];
        this.array_of_bits = createEmptySetOfIntegers(65535);
        ResourceBundle entities = null;
        boolean noExtraEntities = true;
        try {
            if (internal) {
                entities = PropertyResourceBundle.getBundle(entitiesResource);
            } else {
                ClassLoader cl = SecuritySupport.getContextClassLoader();
                if (cl != null) {
                    entities = PropertyResourceBundle.getBundle(entitiesResource, Locale.getDefault(), cl);
                }
            }
        } catch (Exception e2) {
        }
        if (entities != null) {
            Enumeration keys = entities.getKeys();
            while (keys.hasMoreElements()) {
                String name = keys.nextElement2();
                int code = Integer.parseInt(entities.getString(name));
                defineEntity(name, (char) code);
                if (extraEntity(code)) {
                    noExtraEntities = false;
                }
            }
            set(10);
            set(13);
        } else {
            InputStream is = null;
            String err = null;
            try {
                try {
                    if (internal) {
                        is = CharInfo.class.getResourceAsStream(entitiesResource);
                    } else {
                        ClassLoader cl2 = SecuritySupport.getContextClassLoader();
                        if (cl2 != null) {
                            try {
                                is = cl2.getResourceAsStream(entitiesResource);
                            } catch (Exception e3) {
                                err = e3.getMessage();
                            }
                        }
                        if (is == null) {
                            try {
                                URL url = new URL(entitiesResource);
                                is = url.openStream();
                            } catch (Exception e4) {
                                err = e4.getMessage();
                            }
                        }
                    }
                    if (is == null) {
                        throw new RuntimeException(Utils.messages.createMessage("ER_RESOURCE_COULD_NOT_FIND", new Object[]{entitiesResource, err}));
                    }
                    try {
                        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    } catch (UnsupportedEncodingException e5) {
                        reader = new BufferedReader(new InputStreamReader(is));
                    }
                    String line = reader.readLine();
                    while (line != null) {
                        if (line.length() == 0 || line.charAt(0) == '#') {
                            line = reader.readLine();
                        } else {
                            int index = line.indexOf(32);
                            if (index > 1) {
                                String name2 = line.substring(0, index);
                                int index2 = index + 1;
                                if (index2 < line.length()) {
                                    String value = line.substring(index2);
                                    int index3 = value.indexOf(32);
                                    int code2 = Integer.parseInt(index3 > 0 ? value.substring(0, index3) : value);
                                    defineEntity(name2, (char) code2);
                                    if (extraEntity(code2)) {
                                        noExtraEntities = false;
                                    }
                                }
                            }
                            line = reader.readLine();
                        }
                    }
                    is.close();
                    set(10);
                    set(13);
                    if (is != null) {
                        try {
                            is.close();
                        } catch (Exception e6) {
                        }
                    }
                } catch (Exception e7) {
                    throw new RuntimeException(Utils.messages.createMessage("ER_RESOURCE_COULD_NOT_LOAD", new Object[]{entitiesResource, e7.toString(), entitiesResource, e7.toString()}));
                }
            } catch (Throwable th) {
                if (0 != 0) {
                    try {
                        is.close();
                    } catch (Exception e8) {
                    }
                }
                throw th;
            }
        }
        for (int ch = 0; ch < 128; ch++) {
            if (((32 <= ch || 10 == ch || 13 == ch || 9 == ch) && !get(ch)) || 34 == ch) {
                this.isCleanTextASCII[ch] = true;
                this.isSpecialTextASCII[ch] = false;
            } else {
                this.isCleanTextASCII[ch] = false;
                this.isSpecialTextASCII[ch] = true;
            }
        }
        this.onlyQuotAmpLtGt = noExtraEntities;
        for (int i2 = 0; i2 < 128; i2++) {
            this.isSpecialAttrASCII[i2] = get(i2);
        }
        if ("xml".equals(method)) {
            this.isSpecialAttrASCII[9] = true;
        }
    }

    private void defineEntity(String name, char value) {
        String entityString = "&" + name + ';';
        defineChar2StringMapping(entityString, value);
    }

    String getOutputStringForChar(char value) {
        CharKey charKey = new CharKey();
        charKey.setChar(value);
        return (String) this.m_charToString.get(charKey);
    }

    final boolean isSpecialAttrChar(int value) {
        if (value < 128) {
            return this.isSpecialAttrASCII[value];
        }
        return get(value);
    }

    final boolean isSpecialTextChar(int value) {
        if (value < 128) {
            return this.isSpecialTextASCII[value];
        }
        return get(value);
    }

    final boolean isTextASCIIClean(int value) {
        return this.isCleanTextASCII[value];
    }

    static CharInfo getCharInfoInternal(String entitiesFileName, String method) {
        CharInfo charInfo = (CharInfo) m_getCharInfoCache.get(entitiesFileName);
        if (charInfo != null) {
            return charInfo;
        }
        CharInfo charInfo2 = new CharInfo(entitiesFileName, method, true);
        m_getCharInfoCache.put(entitiesFileName, charInfo2);
        return charInfo2;
    }

    static CharInfo getCharInfo(String entitiesFileName, String method) {
        String absoluteEntitiesFileName;
        try {
            return new CharInfo(entitiesFileName, method, false);
        } catch (Exception e2) {
            if (entitiesFileName.indexOf(58) < 0) {
                absoluteEntitiesFileName = SystemIDResolver.getAbsoluteURIFromRelative(entitiesFileName);
            } else {
                try {
                    absoluteEntitiesFileName = SystemIDResolver.getAbsoluteURI(entitiesFileName, null);
                } catch (TransformerException te) {
                    throw new WrappedRuntimeException(te);
                }
            }
            return new CharInfo(absoluteEntitiesFileName, method, false);
        }
    }

    private static int arrayIndex(int i2) {
        return i2 >> 5;
    }

    private static int bit(int i2) {
        int ret = 1 << (i2 & 31);
        return ret;
    }

    private int[] createEmptySetOfIntegers(int max) {
        this.firstWordNotUsed = 0;
        int[] arr = new int[arrayIndex(max - 1) + 1];
        return arr;
    }

    private final void set(int i2) {
        setASCIIdirty(i2);
        int j2 = i2 >> 5;
        int k2 = j2 + 1;
        if (this.firstWordNotUsed < k2) {
            this.firstWordNotUsed = k2;
        }
        int[] iArr = this.array_of_bits;
        iArr[j2] = iArr[j2] | (1 << (i2 & 31));
    }

    private final boolean get(int i2) {
        boolean in_the_set = false;
        int j2 = i2 >> 5;
        if (j2 < this.firstWordNotUsed) {
            in_the_set = (this.array_of_bits[j2] & (1 << (i2 & 31))) != 0;
        }
        return in_the_set;
    }

    private boolean extraEntity(int entityValue) {
        boolean extra = false;
        if (entityValue < 128) {
            switch (entityValue) {
                case 34:
                case 38:
                case 60:
                case 62:
                    break;
                default:
                    extra = true;
                    break;
            }
        }
        return extra;
    }

    private void setASCIIdirty(int j2) {
        if (0 <= j2 && j2 < 128) {
            this.isCleanTextASCII[j2] = false;
            this.isSpecialTextASCII[j2] = true;
        }
    }

    private void setASCIIclean(int j2) {
        if (0 <= j2 && j2 < 128) {
            this.isCleanTextASCII[j2] = true;
            this.isSpecialTextASCII[j2] = false;
        }
    }

    private void defineChar2StringMapping(String outputString, char inputChar) {
        CharKey character = new CharKey(inputChar);
        this.m_charToString.put(character, outputString);
        set(inputChar);
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/CharInfo$CharKey.class */
    private static class CharKey {
        private char m_char;

        public CharKey(char key) {
            this.m_char = key;
        }

        public CharKey() {
        }

        public final void setChar(char c2) {
            this.m_char = c2;
        }

        public final int hashCode() {
            return this.m_char;
        }

        public final boolean equals(Object obj) {
            return ((CharKey) obj).m_char == this.m_char;
        }
    }
}
