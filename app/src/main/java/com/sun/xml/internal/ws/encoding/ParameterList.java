package com.sun.xml.internal.ws.encoding;

import com.sun.xml.internal.ws.encoding.HeaderTokenizer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/ParameterList.class */
final class ParameterList {
    private final Map<String, String> list;

    ParameterList(String s2) throws WebServiceException {
        HeaderTokenizer h2 = new HeaderTokenizer(s2, com.sun.xml.internal.messaging.saaj.packaging.mime.internet.HeaderTokenizer.MIME);
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
                    throw new WebServiceException();
                }
                String name = tk.getValue().toLowerCase();
                if (((char) h2.next().getType()) != '=') {
                    throw new WebServiceException();
                }
                HeaderTokenizer.Token tk2 = h2.next();
                int type2 = tk2.getType();
                if (type2 != -1 && type2 != -2) {
                    throw new WebServiceException();
                }
                this.list.put(name, tk2.getValue());
            } else {
                throw new WebServiceException();
            }
        }
    }

    int size() {
        return this.list.size();
    }

    String get(String name) {
        return this.list.get(name.trim().toLowerCase());
    }

    Iterator<String> getNames() {
        return this.list.keySet().iterator();
    }
}
