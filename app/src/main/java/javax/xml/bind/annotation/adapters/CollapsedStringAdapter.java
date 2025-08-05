package javax.xml.bind.annotation.adapters;

/* loaded from: rt.jar:javax/xml/bind/annotation/adapters/CollapsedStringAdapter.class */
public class CollapsedStringAdapter extends XmlAdapter<String, String> {
    @Override // javax.xml.bind.annotation.adapters.XmlAdapter
    public String unmarshal(String text) {
        if (text == null) {
            return null;
        }
        int len = text.length();
        int s2 = 0;
        while (s2 < len && !isWhiteSpace(text.charAt(s2))) {
            s2++;
        }
        if (s2 == len) {
            return text;
        }
        StringBuilder result = new StringBuilder(len);
        if (s2 != 0) {
            for (int i2 = 0; i2 < s2; i2++) {
                result.append(text.charAt(i2));
            }
            result.append(' ');
        }
        boolean inStripMode = true;
        for (int i3 = s2 + 1; i3 < len; i3++) {
            char ch = text.charAt(i3);
            boolean b2 = isWhiteSpace(ch);
            if (!inStripMode || !b2) {
                inStripMode = b2;
                if (inStripMode) {
                    result.append(' ');
                } else {
                    result.append(ch);
                }
            }
        }
        int len2 = result.length();
        if (len2 > 0 && result.charAt(len2 - 1) == ' ') {
            result.setLength(len2 - 1);
        }
        return result.toString();
    }

    @Override // javax.xml.bind.annotation.adapters.XmlAdapter
    public String marshal(String s2) {
        return s2;
    }

    protected static boolean isWhiteSpace(char ch) {
        if (ch > ' ') {
            return false;
        }
        return ch == '\t' || ch == '\n' || ch == '\r' || ch == ' ';
    }
}
