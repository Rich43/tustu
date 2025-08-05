package com.sun.org.apache.xml.internal.utils;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/Trie.class */
public class Trie {
    public static final int ALPHA_SIZE = 128;
    private char[] m_charBuffer = new char[0];
    Node m_Root = new Node();

    public Object put(String key, Object value) {
        int len = key.length();
        if (len > this.m_charBuffer.length) {
            this.m_charBuffer = new char[len];
        }
        Node node = this.m_Root;
        int i2 = 0;
        while (true) {
            if (i2 >= len) {
                break;
            }
            Node nextNode = node.m_nextChar[Character.toUpperCase(key.charAt(i2))];
            if (nextNode != null) {
                node = nextNode;
                i2++;
            } else {
                while (i2 < len) {
                    Node newNode = new Node();
                    node.m_nextChar[Character.toUpperCase(key.charAt(i2))] = newNode;
                    node.m_nextChar[Character.toLowerCase(key.charAt(i2))] = newNode;
                    node = newNode;
                    i2++;
                }
            }
        }
        Object ret = node.m_Value;
        node.m_Value = value;
        return ret;
    }

    public Object get(String key) {
        int len = key.length();
        if (this.m_charBuffer.length < len) {
            return null;
        }
        Node node = this.m_Root;
        switch (len) {
            case 0:
                break;
            case 1:
                char ch = key.charAt(0);
                if (ch < 128 && (node = node.m_nextChar[ch]) != null) {
                    break;
                }
                break;
            default:
                key.getChars(0, len, this.m_charBuffer, 0);
                for (int i2 = 0; i2 < len; i2++) {
                    char ch2 = this.m_charBuffer[i2];
                    if (128 <= ch2) {
                        break;
                    } else {
                        node = node.m_nextChar[ch2];
                        if (node == null) {
                            break;
                        }
                    }
                }
                break;
        }
        return null;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/Trie$Node.class */
    class Node {
        Node[] m_nextChar = new Node[128];
        Object m_Value = null;

        Node() {
        }
    }
}
