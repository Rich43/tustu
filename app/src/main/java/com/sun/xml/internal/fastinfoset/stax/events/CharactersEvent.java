package com.sun.xml.internal.fastinfoset.stax.events;

import com.sun.xml.internal.fastinfoset.org.apache.xerces.util.XMLChar;
import javax.xml.stream.events.Characters;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/stax/events/CharactersEvent.class */
public class CharactersEvent extends EventBase implements Characters {
    private String _text;
    private boolean isCData;
    private boolean isSpace;
    private boolean isIgnorable;
    private boolean needtoCheck;

    public CharactersEvent() {
        super(4);
        this.isCData = false;
        this.isSpace = false;
        this.isIgnorable = false;
        this.needtoCheck = true;
    }

    public CharactersEvent(String data) {
        super(4);
        this.isCData = false;
        this.isSpace = false;
        this.isIgnorable = false;
        this.needtoCheck = true;
        this._text = data;
    }

    public CharactersEvent(String data, boolean isCData) {
        super(4);
        this.isCData = false;
        this.isSpace = false;
        this.isIgnorable = false;
        this.needtoCheck = true;
        this._text = data;
        this.isCData = isCData;
    }

    @Override // javax.xml.stream.events.Characters
    public String getData() {
        return this._text;
    }

    public void setData(String data) {
        this._text = data;
    }

    @Override // javax.xml.stream.events.Characters
    public boolean isCData() {
        return this.isCData;
    }

    public String toString() {
        if (this.isCData) {
            return "<![CDATA[" + this._text + "]]>";
        }
        return this._text;
    }

    @Override // javax.xml.stream.events.Characters
    public boolean isIgnorableWhiteSpace() {
        return this.isIgnorable;
    }

    @Override // javax.xml.stream.events.Characters
    public boolean isWhiteSpace() {
        if (this.needtoCheck) {
            checkWhiteSpace();
            this.needtoCheck = false;
        }
        return this.isSpace;
    }

    public void setSpace(boolean isSpace) {
        this.isSpace = isSpace;
        this.needtoCheck = false;
    }

    public void setIgnorable(boolean isIgnorable) {
        this.isIgnorable = isIgnorable;
        setEventType(6);
    }

    private void checkWhiteSpace() {
        if (!Util.isEmptyString(this._text)) {
            this.isSpace = true;
            for (int i2 = 0; i2 < this._text.length(); i2++) {
                if (!XMLChar.isSpace(this._text.charAt(i2))) {
                    this.isSpace = false;
                    return;
                }
            }
        }
    }
}
