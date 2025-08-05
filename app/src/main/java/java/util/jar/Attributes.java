package java.util.jar;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.Manifest;
import net.lingala.zip4j.util.InternalZipConstants;
import sun.misc.ASCIICaseInsensitiveComparator;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:java/util/jar/Attributes.class */
public class Attributes implements Map<Object, Object>, Cloneable {
    protected Map<Object, Object> map;

    public Attributes() {
        this(11);
    }

    public Attributes(int i2) {
        this.map = new HashMap(i2);
    }

    public Attributes(Attributes attributes) {
        this.map = new HashMap(attributes);
    }

    @Override // java.util.Map
    public Object get(Object obj) {
        return this.map.get(obj);
    }

    public String getValue(String str) {
        return (String) get(new Name(str));
    }

    public String getValue(Name name) {
        return (String) get(name);
    }

    @Override // java.util.Map
    public Object put(Object obj, Object obj2) {
        return this.map.put((Name) obj, (String) obj2);
    }

    public String putValue(String str, String str2) {
        return (String) put(new Name(str), str2);
    }

    @Override // java.util.Map
    public Object remove(Object obj) {
        return this.map.remove(obj);
    }

    @Override // java.util.Map
    public boolean containsValue(Object obj) {
        return this.map.containsValue(obj);
    }

    @Override // java.util.Map
    public boolean containsKey(Object obj) {
        return this.map.containsKey(obj);
    }

    @Override // java.util.Map
    public void putAll(Map<? extends Object, ? extends Object> map) {
        if (!Attributes.class.isInstance(map)) {
            throw new ClassCastException();
        }
        for (Map.Entry<? extends Object, ? extends Object> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override // java.util.Map
    public void clear() {
        this.map.clear();
    }

    @Override // java.util.Map
    public int size() {
        return this.map.size();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override // java.util.Map
    public Set<Object> keySet() {
        return this.map.keySet();
    }

    @Override // java.util.Map
    public Collection<Object> values() {
        return this.map.values();
    }

    @Override // java.util.Map
    public Set<Map.Entry<Object, Object>> entrySet() {
        return this.map.entrySet();
    }

    @Override // java.util.Map
    public boolean equals(Object obj) {
        return this.map.equals(obj);
    }

    @Override // java.util.Map
    public int hashCode() {
        return this.map.hashCode();
    }

    public Object clone() {
        return new Attributes(this);
    }

    void write(DataOutputStream dataOutputStream) throws IOException {
        for (Map.Entry<Object, Object> entry : entrySet()) {
            StringBuffer stringBuffer = new StringBuffer(((Name) entry.getKey()).toString());
            stringBuffer.append(": ");
            String str = (String) entry.getValue();
            if (str != null) {
                byte[] bytes = str.getBytes(InternalZipConstants.CHARSET_UTF8);
                str = new String(bytes, 0, 0, bytes.length);
            }
            stringBuffer.append(str);
            stringBuffer.append("\r\n");
            Manifest.make72Safe(stringBuffer);
            dataOutputStream.writeBytes(stringBuffer.toString());
        }
        dataOutputStream.writeBytes("\r\n");
    }

    void writeMain(DataOutputStream dataOutputStream) throws IOException {
        String string = Name.MANIFEST_VERSION.toString();
        String value = getValue(string);
        if (value == null) {
            string = Name.SIGNATURE_VERSION.toString();
            value = getValue(string);
        }
        if (value != null) {
            dataOutputStream.writeBytes(string + ": " + value + "\r\n");
        }
        for (Map.Entry<Object, Object> entry : entrySet()) {
            String string2 = ((Name) entry.getKey()).toString();
            if (value != null && !string2.equalsIgnoreCase(string)) {
                StringBuffer stringBuffer = new StringBuffer(string2);
                stringBuffer.append(": ");
                String str = (String) entry.getValue();
                if (str != null) {
                    byte[] bytes = str.getBytes(InternalZipConstants.CHARSET_UTF8);
                    str = new String(bytes, 0, 0, bytes.length);
                }
                stringBuffer.append(str);
                stringBuffer.append("\r\n");
                Manifest.make72Safe(stringBuffer);
                dataOutputStream.writeBytes(stringBuffer.toString());
            }
        }
        dataOutputStream.writeBytes("\r\n");
    }

    void read(Manifest.FastInputStream fastInputStream, byte[] bArr) throws IOException {
        String string;
        String str = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (true) {
            int line = fastInputStream.readLine(bArr);
            if (line != -1) {
                boolean z2 = false;
                int i2 = line - 1;
                if (bArr[i2] != 10) {
                    throw new IOException("line too long");
                }
                if (i2 > 0 && bArr[i2 - 1] == 13) {
                    i2--;
                }
                if (i2 != 0) {
                    int i3 = 0;
                    if (bArr[0] == 32) {
                        if (str == null) {
                            throw new IOException("misplaced continuation line");
                        }
                        z2 = true;
                        byteArrayOutputStream.write(bArr, 1, i2 - 1);
                        if (fastInputStream.peek() == 32) {
                            continue;
                        } else {
                            string = byteArrayOutputStream.toString(InternalZipConstants.CHARSET_UTF8);
                            byteArrayOutputStream.reset();
                            try {
                                if (putValue(str, string) != null && !z2) {
                                    PlatformLogger.getLogger("java.util.jar").warning("Duplicate name in Manifest: " + str + ".\nEnsure that the manifest does not have duplicate entries, and\nthat blank lines separate individual sections in both your\nmanifest and in the META-INF/MANIFEST.MF entry in the jar file.");
                                }
                            } catch (IllegalArgumentException e2) {
                                throw new IOException("invalid header field name: " + str);
                            }
                        }
                    } else {
                        do {
                            int i4 = i3;
                            i3++;
                            if (bArr[i4] == 58) {
                                int i5 = i3 + 1;
                                if (bArr[i3] != 32) {
                                    throw new IOException("invalid header field");
                                }
                                str = new String(bArr, 0, 0, i5 - 2);
                                if (fastInputStream.peek() == 32) {
                                    byteArrayOutputStream.reset();
                                    byteArrayOutputStream.write(bArr, i5, i2 - i5);
                                } else {
                                    string = new String(bArr, i5, i2 - i5, InternalZipConstants.CHARSET_UTF8);
                                    if (putValue(str, string) != null) {
                                        PlatformLogger.getLogger("java.util.jar").warning("Duplicate name in Manifest: " + str + ".\nEnsure that the manifest does not have duplicate entries, and\nthat blank lines separate individual sections in both your\nmanifest and in the META-INF/MANIFEST.MF entry in the jar file.");
                                    }
                                }
                            }
                        } while (i3 < i2);
                        throw new IOException("invalid header field");
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    /* loaded from: rt.jar:java/util/jar/Attributes$Name.class */
    public static class Name {
        private String name;
        private int hashCode = -1;
        public static final Name MANIFEST_VERSION = new Name("Manifest-Version");
        public static final Name SIGNATURE_VERSION = new Name("Signature-Version");
        public static final Name CONTENT_TYPE = new Name("Content-Type");
        public static final Name CLASS_PATH = new Name("Class-Path");
        public static final Name MAIN_CLASS = new Name("Main-Class");
        public static final Name SEALED = new Name("Sealed");
        public static final Name EXTENSION_LIST = new Name("Extension-List");
        public static final Name EXTENSION_NAME = new Name("Extension-Name");

        @Deprecated
        public static final Name EXTENSION_INSTALLATION = new Name("Extension-Installation");
        public static final Name IMPLEMENTATION_TITLE = new Name("Implementation-Title");
        public static final Name IMPLEMENTATION_VERSION = new Name("Implementation-Version");
        public static final Name IMPLEMENTATION_VENDOR = new Name("Implementation-Vendor");

        @Deprecated
        public static final Name IMPLEMENTATION_VENDOR_ID = new Name("Implementation-Vendor-Id");

        @Deprecated
        public static final Name IMPLEMENTATION_URL = new Name("Implementation-URL");
        public static final Name SPECIFICATION_TITLE = new Name("Specification-Title");
        public static final Name SPECIFICATION_VERSION = new Name("Specification-Version");
        public static final Name SPECIFICATION_VENDOR = new Name("Specification-Vendor");

        public Name(String str) {
            if (str == null) {
                throw new NullPointerException("name");
            }
            if (!isValid(str)) {
                throw new IllegalArgumentException(str);
            }
            this.name = str.intern();
        }

        private static boolean isValid(String str) {
            int length = str.length();
            if (length > 70 || length == 0) {
                return false;
            }
            for (int i2 = 0; i2 < length; i2++) {
                if (!isValid(str.charAt(i2))) {
                    return false;
                }
            }
            return true;
        }

        private static boolean isValid(char c2) {
            return isAlpha(c2) || isDigit(c2) || c2 == '_' || c2 == '-';
        }

        private static boolean isAlpha(char c2) {
            return (c2 >= 'a' && c2 <= 'z') || (c2 >= 'A' && c2 <= 'Z');
        }

        private static boolean isDigit(char c2) {
            return c2 >= '0' && c2 <= '9';
        }

        public boolean equals(Object obj) {
            return (obj instanceof Name) && ASCIICaseInsensitiveComparator.CASE_INSENSITIVE_ORDER.compare(this.name, ((Name) obj).name) == 0;
        }

        public int hashCode() {
            if (this.hashCode == -1) {
                this.hashCode = ASCIICaseInsensitiveComparator.lowerCaseHashCode(this.name);
            }
            return this.hashCode;
        }

        public String toString() {
            return this.name;
        }
    }
}
