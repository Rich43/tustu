package com.sun.activation.registries;

import java.util.NoSuchElementException;
import java.util.Vector;

/* compiled from: MimeTypeFile.java */
/* loaded from: rt.jar:com/sun/activation/registries/LineTokenizer.class */
class LineTokenizer {
    private int maxPosition;
    private String str;
    private static final String singles = "=";
    private Vector stack = new Vector();
    private int currentPosition = 0;

    public LineTokenizer(String str) {
        this.str = str;
        this.maxPosition = str.length();
    }

    private void skipWhiteSpace() {
        while (this.currentPosition < this.maxPosition && Character.isWhitespace(this.str.charAt(this.currentPosition))) {
            this.currentPosition++;
        }
    }

    public boolean hasMoreTokens() {
        if (this.stack.size() > 0) {
            return true;
        }
        skipWhiteSpace();
        return this.currentPosition < this.maxPosition;
    }

    public String nextToken() {
        String s2;
        int size = this.stack.size();
        if (size > 0) {
            String t2 = (String) this.stack.elementAt(size - 1);
            this.stack.removeElementAt(size - 1);
            return t2;
        }
        skipWhiteSpace();
        if (this.currentPosition >= this.maxPosition) {
            throw new NoSuchElementException();
        }
        int start = this.currentPosition;
        char c2 = this.str.charAt(start);
        if (c2 == '\"') {
            this.currentPosition++;
            boolean filter = false;
            while (this.currentPosition < this.maxPosition) {
                String str = this.str;
                int i2 = this.currentPosition;
                this.currentPosition = i2 + 1;
                char c3 = str.charAt(i2);
                if (c3 == '\\') {
                    this.currentPosition++;
                    filter = true;
                } else if (c3 == '\"') {
                    if (filter) {
                        StringBuffer sb = new StringBuffer();
                        for (int i3 = start + 1; i3 < this.currentPosition - 1; i3++) {
                            char c4 = this.str.charAt(i3);
                            if (c4 != '\\') {
                                sb.append(c4);
                            }
                        }
                        s2 = sb.toString();
                    } else {
                        s2 = this.str.substring(start + 1, this.currentPosition - 1);
                    }
                    return s2;
                }
            }
        } else if (singles.indexOf(c2) >= 0) {
            this.currentPosition++;
        } else {
            while (this.currentPosition < this.maxPosition && singles.indexOf(this.str.charAt(this.currentPosition)) < 0 && !Character.isWhitespace(this.str.charAt(this.currentPosition))) {
                this.currentPosition++;
            }
        }
        return this.str.substring(start, this.currentPosition);
    }

    public void pushToken(String token) {
        this.stack.addElement(token);
    }
}
