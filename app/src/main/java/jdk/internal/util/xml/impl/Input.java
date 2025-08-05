package jdk.internal.util.xml.impl;

import java.io.Reader;

/* loaded from: rt.jar:jdk/internal/util/xml/impl/Input.class */
public class Input {
    public String pubid;
    public String sysid;
    public String xmlenc;
    public char xmlver;
    public Reader src;
    public char[] chars;
    public int chLen;
    public int chIdx;
    public Input next;

    public Input(int i2) {
        this.chars = new char[i2];
        this.chLen = this.chars.length;
    }

    public Input(char[] cArr) {
        this.chars = cArr;
        this.chLen = this.chars.length;
    }

    public Input() {
    }
}
