package com.sun.xml.internal.fastinfoset.stax.events;

import javax.xml.stream.events.StartDocument;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/stax/events/StartDocumentEvent.class */
public class StartDocumentEvent extends EventBase implements StartDocument {
    protected String _systemId;
    protected String _encoding;
    protected boolean _standalone;
    protected String _version;
    private boolean _encodingSet;
    private boolean _standaloneSet;

    public void reset() {
        this._encoding = "UTF-8";
        this._standalone = true;
        this._version = "1.0";
        this._encodingSet = false;
        this._standaloneSet = false;
    }

    public StartDocumentEvent() {
        this(null, null);
    }

    public StartDocumentEvent(String encoding) {
        this(encoding, null);
    }

    public StartDocumentEvent(String encoding, String version) {
        this._encoding = "UTF-8";
        this._standalone = true;
        this._version = "1.0";
        this._encodingSet = false;
        this._standaloneSet = false;
        if (encoding != null) {
            this._encoding = encoding;
            this._encodingSet = true;
        }
        if (version != null) {
            this._version = version;
        }
        setEventType(7);
    }

    @Override // com.sun.xml.internal.fastinfoset.stax.events.EventBase, javax.xml.stream.events.EntityDeclaration
    public String getSystemId() {
        return super.getSystemId();
    }

    @Override // javax.xml.stream.events.StartDocument
    public String getCharacterEncodingScheme() {
        return this._encoding;
    }

    @Override // javax.xml.stream.events.StartDocument
    public boolean encodingSet() {
        return this._encodingSet;
    }

    @Override // javax.xml.stream.events.StartDocument
    public boolean isStandalone() {
        return this._standalone;
    }

    @Override // javax.xml.stream.events.StartDocument
    public boolean standaloneSet() {
        return this._standaloneSet;
    }

    @Override // javax.xml.stream.events.StartDocument
    public String getVersion() {
        return this._version;
    }

    public void setStandalone(boolean standalone) {
        this._standaloneSet = true;
        this._standalone = standalone;
    }

    public void setStandalone(String s2) {
        this._standaloneSet = true;
        if (s2 == null) {
            this._standalone = true;
        } else if (s2.equals("yes")) {
            this._standalone = true;
        } else {
            this._standalone = false;
        }
    }

    public void setEncoding(String encoding) {
        this._encoding = encoding;
        this._encodingSet = true;
    }

    void setDeclaredEncoding(boolean value) {
        this._encodingSet = value;
    }

    public void setVersion(String s2) {
        this._version = s2;
    }

    void clear() {
        this._encoding = "UTF-8";
        this._standalone = true;
        this._version = "1.0";
        this._encodingSet = false;
        this._standaloneSet = false;
    }

    public String toString() {
        String s2;
        String s3 = ("<?xml version=\"" + this._version + PdfOps.DOUBLE_QUOTE__TOKEN) + " encoding='" + this._encoding + PdfOps.SINGLE_QUOTE_TOKEN;
        if (this._standaloneSet) {
            if (this._standalone) {
                s2 = s3 + " standalone='yes'?>";
            } else {
                s2 = s3 + " standalone='no'?>";
            }
        } else {
            s2 = s3 + "?>";
        }
        return s2;
    }

    @Override // com.sun.xml.internal.fastinfoset.stax.events.EventBase, javax.xml.stream.events.XMLEvent
    public boolean isStartDocument() {
        return true;
    }
}
