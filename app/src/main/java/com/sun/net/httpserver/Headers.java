package com.sun.net.httpserver;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/net/httpserver/Headers.class */
public class Headers implements Map<String, List<String>> {
    HashMap<String, List<String>> map = new HashMap<>(32);

    private String normalize(String str) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (length == 0) {
            return str;
        }
        char[] charArray = str.toCharArray();
        if (charArray[0] >= 'a' && charArray[0] <= 'z') {
            charArray[0] = (char) (charArray[0] - ' ');
        } else if (charArray[0] == '\r' || charArray[0] == '\n') {
            throw new IllegalArgumentException("illegal character in key");
        }
        for (int i2 = 1; i2 < length; i2++) {
            if (charArray[i2] >= 'A' && charArray[i2] <= 'Z') {
                charArray[i2] = (char) (charArray[i2] + ' ');
            } else if (charArray[i2] == '\r' || charArray[i2] == '\n') {
                throw new IllegalArgumentException("illegal character in key");
            }
        }
        return new String(charArray);
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
    public boolean containsKey(Object obj) {
        if (obj == null || !(obj instanceof String)) {
            return false;
        }
        return this.map.containsKey(normalize((String) obj));
    }

    @Override // java.util.Map
    public boolean containsValue(Object obj) {
        return this.map.containsValue(obj);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Map
    public List<String> get(Object obj) {
        return this.map.get(normalize((String) obj));
    }

    public String getFirst(String str) {
        List<String> list = this.map.get(normalize(str));
        if (list == null) {
            return null;
        }
        return list.get(0);
    }

    @Override // java.util.Map
    public List<String> put(String str, List<String> list) {
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            checkValue(it.next());
        }
        return this.map.put(normalize(str), list);
    }

    public void add(String str, String str2) {
        checkValue(str2);
        String strNormalize = normalize(str);
        List<String> linkedList = this.map.get(strNormalize);
        if (linkedList == null) {
            linkedList = new LinkedList();
            this.map.put(strNormalize, linkedList);
        }
        linkedList.add(str2);
    }

    private static void checkValue(String str) {
        int length = str.length();
        int i2 = 0;
        while (i2 < length) {
            char cCharAt = str.charAt(i2);
            if (cCharAt == '\r') {
                if (i2 >= length - 2) {
                    throw new IllegalArgumentException("Illegal CR found in header");
                }
                char cCharAt2 = str.charAt(i2 + 1);
                char cCharAt3 = str.charAt(i2 + 2);
                if (cCharAt2 != '\n') {
                    throw new IllegalArgumentException("Illegal char found after CR in header");
                }
                if (cCharAt3 != ' ' && cCharAt3 != '\t') {
                    throw new IllegalArgumentException("No whitespace found after CRLF in header");
                }
                i2 += 2;
            } else if (cCharAt == '\n') {
                throw new IllegalArgumentException("Illegal LF found in header");
            }
            i2++;
        }
    }

    public void set(String str, String str2) {
        LinkedList linkedList = new LinkedList();
        linkedList.add(str2);
        put(str, (List<String>) linkedList);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Map
    public List<String> remove(Object obj) {
        return this.map.remove(normalize((String) obj));
    }

    @Override // java.util.Map
    public void putAll(Map<? extends String, ? extends List<String>> map) {
        this.map.putAll(map);
    }

    @Override // java.util.Map
    public void clear() {
        this.map.clear();
    }

    @Override // java.util.Map
    public Set<String> keySet() {
        return this.map.keySet();
    }

    @Override // java.util.Map
    public Collection<List<String>> values() {
        return this.map.values();
    }

    @Override // java.util.Map
    public Set<Map.Entry<String, List<String>>> entrySet() {
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
}
