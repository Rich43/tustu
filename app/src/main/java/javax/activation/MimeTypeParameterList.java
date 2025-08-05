package javax.activation;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: rt.jar:javax/activation/MimeTypeParameterList.class */
public class MimeTypeParameterList {
    private Hashtable parameters = new Hashtable();
    private static final String TSPECIALS = "()<>@,;:/[]?=\\\"";

    public MimeTypeParameterList() {
    }

    public MimeTypeParameterList(String parameterList) throws MimeTypeParseException {
        parse(parameterList);
    }

    /* JADX WARN: Code restructure failed: missing block: B:27:0x007c, code lost:
    
        throw new javax.activation.MimeTypeParseException("Couldn't find the '=' that separates a parameter name from its value.");
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0172, code lost:
    
        if (r8 >= r0) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x017e, code lost:
    
        throw new javax.activation.MimeTypeParseException("More characters encountered in input than expected.");
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x017f, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void parse(java.lang.String r6) throws javax.activation.MimeTypeParseException {
        /*
            Method dump skipped, instructions count: 384
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.activation.MimeTypeParameterList.parse(java.lang.String):void");
    }

    public int size() {
        return this.parameters.size();
    }

    public boolean isEmpty() {
        return this.parameters.isEmpty();
    }

    public String get(String name) {
        return (String) this.parameters.get(name.trim().toLowerCase(Locale.ENGLISH));
    }

    public void set(String name, String value) {
        this.parameters.put(name.trim().toLowerCase(Locale.ENGLISH), value);
    }

    public void remove(String name) {
        this.parameters.remove(name.trim().toLowerCase(Locale.ENGLISH));
    }

    public Enumeration getNames() {
        return this.parameters.keys();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.ensureCapacity(this.parameters.size() * 16);
        Enumeration keys = this.parameters.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement2();
            buffer.append(VectorFormat.DEFAULT_SEPARATOR);
            buffer.append(key);
            buffer.append('=');
            buffer.append(quote((String) this.parameters.get(key)));
        }
        return buffer.toString();
    }

    private static boolean isTokenChar(char c2) {
        return c2 > ' ' && c2 < 127 && TSPECIALS.indexOf(c2) < 0;
    }

    private static int skipWhiteSpace(String rawdata, int i2) {
        int length = rawdata.length();
        while (i2 < length && Character.isWhitespace(rawdata.charAt(i2))) {
            i2++;
        }
        return i2;
    }

    private static String quote(String value) {
        boolean needsQuotes = false;
        int length = value.length();
        for (int i2 = 0; i2 < length && !needsQuotes; i2++) {
            needsQuotes = !isTokenChar(value.charAt(i2));
        }
        if (needsQuotes) {
            StringBuffer buffer = new StringBuffer();
            buffer.ensureCapacity((int) (length * 1.5d));
            buffer.append('\"');
            for (int i3 = 0; i3 < length; i3++) {
                char c2 = value.charAt(i3);
                if (c2 == '\\' || c2 == '\"') {
                    buffer.append('\\');
                }
                buffer.append(c2);
            }
            buffer.append('\"');
            return buffer.toString();
        }
        return value;
    }

    private static String unquote(String value) {
        int valueLength = value.length();
        StringBuffer buffer = new StringBuffer();
        buffer.ensureCapacity(valueLength);
        boolean escaped = false;
        for (int i2 = 0; i2 < valueLength; i2++) {
            char currentChar = value.charAt(i2);
            if (!escaped && currentChar != '\\') {
                buffer.append(currentChar);
            } else if (escaped) {
                buffer.append(currentChar);
                escaped = false;
            } else {
                escaped = true;
            }
        }
        return buffer.toString();
    }
}
