package com.sun.xml.internal.ws.transport;

import java.io.Serializable;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

/* loaded from: rt.jar:com/sun/xml/internal/ws/transport/Headers.class */
public class Headers extends TreeMap<String, List<String>> {
    private static final InsensitiveComparator INSTANCE = new InsensitiveComparator();

    public Headers() {
        super(INSTANCE);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/transport/Headers$InsensitiveComparator.class */
    private static final class InsensitiveComparator implements Comparator<String>, Serializable {
        private InsensitiveComparator() {
        }

        @Override // java.util.Comparator
        public int compare(String o1, String o2) {
            if (o1 == null && o2 == null) {
                return 0;
            }
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return 1;
            }
            return o1.compareToIgnoreCase(o2);
        }
    }

    public void add(String key, String value) {
        List<String> list = get(key);
        if (list == null) {
            list = new LinkedList();
            put(key, list);
        }
        list.add(value);
    }

    public String getFirst(String key) {
        List<String> l2 = get(key);
        if (l2 == null) {
            return null;
        }
        return l2.get(0);
    }

    public void set(String key, String value) {
        LinkedList<String> l2 = new LinkedList<>();
        l2.add(value);
        put(key, l2);
    }
}
