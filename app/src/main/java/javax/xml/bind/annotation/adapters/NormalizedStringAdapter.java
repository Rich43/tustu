package javax.xml.bind.annotation.adapters;

/* loaded from: rt.jar:javax/xml/bind/annotation/adapters/NormalizedStringAdapter.class */
public final class NormalizedStringAdapter extends XmlAdapter<String, String> {
    @Override // javax.xml.bind.annotation.adapters.XmlAdapter
    public String unmarshal(String text) {
        if (text == null) {
            return null;
        }
        int i2 = text.length() - 1;
        while (i2 >= 0 && !isWhiteSpaceExceptSpace(text.charAt(i2))) {
            i2--;
        }
        if (i2 < 0) {
            return text;
        }
        char[] buf = text.toCharArray();
        int i3 = i2;
        buf[i3] = ' ';
        for (int i4 = i2 - 1; i4 >= 0; i4--) {
            if (isWhiteSpaceExceptSpace(buf[i4])) {
                buf[i4] = ' ';
            }
        }
        return new String(buf);
    }

    @Override // javax.xml.bind.annotation.adapters.XmlAdapter
    public String marshal(String s2) {
        return s2;
    }

    protected static boolean isWhiteSpaceExceptSpace(char ch) {
        if (ch >= ' ') {
            return false;
        }
        return ch == '\t' || ch == '\n' || ch == '\r';
    }
}
