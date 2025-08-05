package com.sun.xml.internal.messaging.saaj.packaging.mime.internet;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.HeaderTokenizer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/packaging/mime/internet/ParameterList.class */
public final class ParameterList {
    private final HashMap list;

    public ParameterList() {
        this.list = new HashMap();
    }

    private ParameterList(HashMap m2) {
        this.list = m2;
    }

    public ParameterList(String s2) throws ParseException {
        HeaderTokenizer h2 = new HeaderTokenizer(s2, HeaderTokenizer.MIME);
        this.list = new HashMap();
        while (true) {
            int type = h2.next().getType();
            if (type == -4) {
                return;
            }
            if (((char) type) == ';') {
                HeaderTokenizer.Token tk = h2.next();
                if (tk.getType() == -4) {
                    return;
                }
                if (tk.getType() != -1) {
                    throw new ParseException();
                }
                String name = tk.getValue().toLowerCase();
                if (((char) h2.next().getType()) != '=') {
                    throw new ParseException();
                }
                HeaderTokenizer.Token tk2 = h2.next();
                int type2 = tk2.getType();
                if (type2 != -1 && type2 != -2) {
                    throw new ParseException();
                }
                this.list.put(name, tk2.getValue());
            } else {
                throw new ParseException();
            }
        }
    }

    public int size() {
        return this.list.size();
    }

    public String get(String name) {
        return (String) this.list.get(name.trim().toLowerCase());
    }

    public void set(String name, String value) {
        this.list.put(name.trim().toLowerCase(), value);
    }

    public void remove(String name) {
        this.list.remove(name.trim().toLowerCase());
    }

    public Iterator getNames() {
        return this.list.keySet().iterator();
    }

    public String toString() {
        return toString(0);
    }

    public String toString(int used) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry e2 : this.list.entrySet()) {
            String name = (String) e2.getKey();
            String value = quote((String) e2.getValue());
            sb.append(VectorFormat.DEFAULT_SEPARATOR);
            int used2 = used + 2;
            int len = name.length() + value.length() + 1;
            if (used2 + len > 76) {
                sb.append("\r\n\t");
                used2 = 8;
            }
            sb.append(name).append('=');
            int used3 = used2 + name.length() + 1;
            if (used3 + value.length() > 76) {
                String s2 = MimeUtility.fold(used3, value);
                sb.append(s2);
                int lastlf = s2.lastIndexOf(10);
                if (lastlf >= 0) {
                    used = used3 + ((s2.length() - lastlf) - 1);
                } else {
                    used = used3 + s2.length();
                }
            } else {
                sb.append(value);
                used = used3 + value.length();
            }
        }
        return sb.toString();
    }

    private String quote(String value) {
        if ("".equals(value)) {
            return "\"\"";
        }
        return MimeUtility.quote(value, HeaderTokenizer.MIME);
    }

    public ParameterList copy() {
        return new ParameterList((HashMap) this.list.clone());
    }
}
