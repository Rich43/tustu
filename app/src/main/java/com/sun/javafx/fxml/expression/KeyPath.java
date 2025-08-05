package com.sun.javafx.fxml.expression;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.AbstractList;
import java.util.ArrayList;

/* loaded from: jfxrt.jar:com/sun/javafx/fxml/expression/KeyPath.class */
public class KeyPath extends AbstractList<String> {
    private ArrayList<String> elements;

    public KeyPath(ArrayList<String> elements) {
        if (elements == null) {
            throw new NullPointerException();
        }
        this.elements = elements;
    }

    @Override // java.util.AbstractList, java.util.List
    public String get(int index) {
        return this.elements.get(index);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.elements.size();
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = this.elements.size();
        for (int i2 = 0; i2 < n2; i2++) {
            String element = this.elements.get(i2);
            if (Character.isDigit(element.charAt(0))) {
                stringBuilder.append("[");
                stringBuilder.append(element);
                stringBuilder.append("]");
            } else {
                if (i2 > 0) {
                    stringBuilder.append(".");
                }
                stringBuilder.append(element);
            }
        }
        return stringBuilder.toString();
    }

    public static KeyPath parse(String value) {
        try {
            PushbackReader reader = new PushbackReader(new StringReader(value));
            try {
                KeyPath keyPath = parse(reader);
                reader.close();
                return keyPath;
            } catch (Throwable th) {
                reader.close();
                throw th;
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    protected static KeyPath parse(PushbackReader reader) throws IOException {
        char quote;
        ArrayList<String> elements = new ArrayList<>();
        int c2 = reader.read();
        while (c2 != -1 && (Character.isJavaIdentifierStart(c2) || c2 == 91)) {
            StringBuilder keyBuilder = new StringBuilder();
            boolean bracketed = c2 == 91;
            if (bracketed) {
                int c3 = reader.read();
                boolean quoted = c3 == 34 || c3 == 39;
                if (quoted) {
                    quote = (char) c3;
                    c3 = reader.read();
                } else {
                    quote = 0;
                }
                while (c3 != -1 && bracketed) {
                    if (Character.isISOControl(c3)) {
                        throw new IllegalArgumentException("Illegal identifier character.");
                    }
                    if (!quoted && !Character.isDigit(c3)) {
                        throw new IllegalArgumentException("Illegal character in index value.");
                    }
                    keyBuilder.append((char) c3);
                    c3 = reader.read();
                    if (quoted) {
                        quoted = c3 != quote;
                        if (!quoted) {
                            c3 = reader.read();
                        }
                    }
                    bracketed = c3 != 93;
                }
                if (quoted) {
                    throw new IllegalArgumentException("Unterminated quoted identifier.");
                }
                if (bracketed) {
                    throw new IllegalArgumentException("Unterminated bracketed identifier.");
                }
                c2 = reader.read();
            } else {
                while (c2 != -1 && c2 != 46 && c2 != 91 && Character.isJavaIdentifierPart(c2)) {
                    keyBuilder.append((char) c2);
                    c2 = reader.read();
                }
            }
            if (c2 == 46) {
                c2 = reader.read();
                if (c2 == -1) {
                    throw new IllegalArgumentException("Illegal terminator character.");
                }
            }
            if (keyBuilder.length() == 0) {
                throw new IllegalArgumentException("Missing identifier.");
            }
            elements.add(keyBuilder.toString());
        }
        if (elements.size() == 0) {
            throw new IllegalArgumentException("Invalid path.");
        }
        if (c2 != -1) {
            reader.unread(c2);
        }
        return new KeyPath(elements);
    }
}
