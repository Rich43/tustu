package com.sun.xml.internal.stream.events;

import com.sun.org.apache.xerces.internal.util.XMLChar;
import java.io.IOException;
import java.io.Writer;
import javax.xml.stream.events.Characters;

/* loaded from: rt.jar:com/sun/xml/internal/stream/events/CharacterEvent.class */
public class CharacterEvent extends DummyEvent implements Characters {
    private String fData;
    private boolean fIsCData;
    private boolean fIsIgnorableWhitespace;
    private boolean fIsSpace;
    private boolean fCheckIfSpaceNeeded;

    public CharacterEvent() {
        this.fIsSpace = false;
        this.fCheckIfSpaceNeeded = true;
        this.fIsCData = false;
        init();
    }

    public CharacterEvent(String data) {
        this.fIsSpace = false;
        this.fCheckIfSpaceNeeded = true;
        this.fIsCData = false;
        init();
        this.fData = data;
    }

    public CharacterEvent(String data, boolean flag) {
        this.fIsSpace = false;
        this.fCheckIfSpaceNeeded = true;
        init();
        this.fData = data;
        this.fIsCData = flag;
    }

    public CharacterEvent(String data, boolean flag, boolean isIgnorableWhiteSpace) {
        this.fIsSpace = false;
        this.fCheckIfSpaceNeeded = true;
        init();
        this.fData = data;
        this.fIsCData = flag;
        this.fIsIgnorableWhitespace = isIgnorableWhiteSpace;
    }

    protected void init() {
        setEventType(4);
    }

    @Override // javax.xml.stream.events.Characters
    public String getData() {
        return this.fData;
    }

    public void setData(String data) {
        this.fData = data;
        this.fCheckIfSpaceNeeded = true;
    }

    @Override // javax.xml.stream.events.Characters
    public boolean isCData() {
        return this.fIsCData;
    }

    public String toString() {
        if (this.fIsCData) {
            return "<![CDATA[" + getData() + "]]>";
        }
        return this.fData;
    }

    @Override // com.sun.xml.internal.stream.events.DummyEvent
    protected void writeAsEncodedUnicodeEx(Writer writer) throws IOException {
        if (this.fIsCData) {
            writer.write("<![CDATA[" + getData() + "]]>");
        } else {
            charEncode(writer, this.fData);
        }
    }

    @Override // javax.xml.stream.events.Characters
    public boolean isIgnorableWhiteSpace() {
        return this.fIsIgnorableWhitespace;
    }

    @Override // javax.xml.stream.events.Characters
    public boolean isWhiteSpace() {
        if (this.fCheckIfSpaceNeeded) {
            checkWhiteSpace();
            this.fCheckIfSpaceNeeded = false;
        }
        return this.fIsSpace;
    }

    private void checkWhiteSpace() {
        if (this.fData != null && this.fData.length() > 0) {
            this.fIsSpace = true;
            for (int i2 = 0; i2 < this.fData.length(); i2++) {
                if (!XMLChar.isSpace(this.fData.charAt(i2))) {
                    this.fIsSpace = false;
                    return;
                }
            }
        }
    }
}
