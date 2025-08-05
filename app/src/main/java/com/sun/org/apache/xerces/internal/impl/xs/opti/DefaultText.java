package com.sun.org.apache.xerces.internal.impl.xs.opti;

import org.w3c.dom.DOMException;
import org.w3c.dom.Text;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/opti/DefaultText.class */
public class DefaultText extends NodeImpl implements Text {
    @Override // org.w3c.dom.CharacterData
    public String getData() throws DOMException {
        return null;
    }

    @Override // org.w3c.dom.CharacterData
    public void setData(String data) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.CharacterData
    public int getLength() {
        return 0;
    }

    @Override // org.w3c.dom.CharacterData
    public String substringData(int offset, int count) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.CharacterData
    public void appendData(String arg) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.CharacterData
    public void insertData(int offset, String arg) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.CharacterData
    public void deleteData(int offset, int count) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.CharacterData
    public void replaceData(int offset, int count, String arg) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.Text
    public Text splitText(int offset) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.Text
    public boolean isElementContentWhitespace() {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.Text
    public String getWholeText() {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.Text
    public Text replaceWholeText(String content) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }
}
