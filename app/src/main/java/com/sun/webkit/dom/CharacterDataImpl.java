package com.sun.webkit.dom;

import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/CharacterDataImpl.class */
public class CharacterDataImpl extends NodeImpl implements CharacterData {
    public static final byte DIRECTIONALITY_LEFT_TO_RIGHT = 0;
    public static final byte DIRECTIONALITY_RIGHT_TO_LEFT = 1;
    public static final byte DIRECTIONALITY_EUROPEAN_NUMBER = 3;
    public static final byte DIRECTIONALITY_EUROPEAN_NUMBER_SEPARATOR = 4;
    public static final byte DIRECTIONALITY_EUROPEAN_NUMBER_TERMINATOR = 5;
    public static final byte DIRECTIONALITY_ARABIC_NUMBER = 6;
    public static final byte DIRECTIONALITY_COMMON_NUMBER_SEPARATOR = 7;
    public static final byte DIRECTIONALITY_PARAGRAPH_SEPARATOR = 10;
    public static final byte DIRECTIONALITY_SEGMENT_SEPARATOR = 11;
    public static final byte DIRECTIONALITY_WHITESPACE = 12;
    public static final byte DIRECTIONALITY_OTHER_NEUTRALS = 13;
    public static final byte DIRECTIONALITY_LEFT_TO_RIGHT_EMBEDDING = 14;
    public static final byte DIRECTIONALITY_LEFT_TO_RIGHT_OVERRIDE = 15;
    public static final byte DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC = 2;
    public static final byte DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING = 16;
    public static final byte DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE = 17;
    public static final byte DIRECTIONALITY_POP_DIRECTIONAL_FORMAT = 18;
    public static final byte DIRECTIONALITY_NONSPACING_MARK = 8;
    public static final byte DIRECTIONALITY_BOUNDARY_NEUTRAL = 9;
    public static final byte UNASSIGNED = 0;
    public static final byte UPPERCASE_LETTER = 1;
    public static final byte LOWERCASE_LETTER = 2;
    public static final byte TITLECASE_LETTER = 3;
    public static final byte MODIFIER_LETTER = 4;
    public static final byte OTHER_LETTER = 5;
    public static final byte NON_SPACING_MARK = 6;
    public static final byte ENCLOSING_MARK = 7;
    public static final byte COMBINING_SPACING_MARK = 8;
    public static final byte DECIMAL_DIGIT_NUMBER = 9;
    public static final byte LETTER_NUMBER = 10;
    public static final byte OTHER_NUMBER = 11;
    public static final byte SPACE_SEPARATOR = 12;
    public static final byte LINE_SEPARATOR = 13;
    public static final byte PARAGRAPH_SEPARATOR = 14;
    public static final byte CONTROL = 15;
    public static final byte FORMAT = 16;
    public static final byte PRIVATE_USE = 18;
    public static final byte SURROGATE = 19;
    public static final byte DASH_PUNCTUATION = 20;
    public static final byte START_PUNCTUATION = 21;
    public static final byte END_PUNCTUATION = 22;
    public static final byte CONNECTOR_PUNCTUATION = 23;
    public static final byte OTHER_PUNCTUATION = 24;
    public static final byte MATH_SYMBOL = 25;
    public static final byte CURRENCY_SYMBOL = 26;
    public static final byte MODIFIER_SYMBOL = 27;
    public static final byte OTHER_SYMBOL = 28;
    public static final byte INITIAL_QUOTE_PUNCTUATION = 29;
    public static final byte FINAL_QUOTE_PUNCTUATION = 30;

    static native String getDataImpl(long j2);

    static native void setDataImpl(long j2, String str);

    static native int getLengthImpl(long j2);

    static native long getPreviousElementSiblingImpl(long j2);

    static native long getNextElementSiblingImpl(long j2);

    static native String substringDataImpl(long j2, int i2, int i3);

    static native void appendDataImpl(long j2, String str);

    static native void insertDataImpl(long j2, int i2, String str);

    static native void deleteDataImpl(long j2, int i2, int i3);

    static native void replaceDataImpl(long j2, int i2, int i3, String str);

    static native void removeImpl(long j2);

    CharacterDataImpl(long peer) {
        super(peer);
    }

    static Node getImpl(long peer) {
        return create(peer);
    }

    @Override // org.w3c.dom.CharacterData
    public String getData() {
        return getDataImpl(getPeer());
    }

    @Override // org.w3c.dom.CharacterData
    public void setData(String value) {
        setDataImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.CharacterData
    public int getLength() {
        return getLengthImpl(getPeer());
    }

    public Element getPreviousElementSibling() {
        return ElementImpl.getImpl(getPreviousElementSiblingImpl(getPeer()));
    }

    public Element getNextElementSibling() {
        return ElementImpl.getImpl(getNextElementSiblingImpl(getPeer()));
    }

    @Override // org.w3c.dom.CharacterData
    public String substringData(int offset, int length) throws DOMException {
        return substringDataImpl(getPeer(), offset, length);
    }

    @Override // org.w3c.dom.CharacterData
    public void appendData(String data) {
        appendDataImpl(getPeer(), data);
    }

    @Override // org.w3c.dom.CharacterData
    public void insertData(int offset, String data) throws DOMException {
        insertDataImpl(getPeer(), offset, data);
    }

    @Override // org.w3c.dom.CharacterData
    public void deleteData(int offset, int length) throws DOMException {
        deleteDataImpl(getPeer(), offset, length);
    }

    @Override // org.w3c.dom.CharacterData
    public void replaceData(int offset, int length, String data) throws DOMException {
        replaceDataImpl(getPeer(), offset, length, data);
    }

    public void remove() throws DOMException {
        removeImpl(getPeer());
    }
}
