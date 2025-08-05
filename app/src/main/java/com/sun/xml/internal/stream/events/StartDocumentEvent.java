package com.sun.xml.internal.stream.events;

import java.io.IOException;
import java.io.Writer;
import javax.xml.stream.Location;
import javax.xml.stream.events.StartDocument;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/xml/internal/stream/events/StartDocumentEvent.class */
public class StartDocumentEvent extends DummyEvent implements StartDocument {
    protected String fSystemId;
    protected String fEncodingScheam;
    protected boolean fStandalone;
    protected String fVersion;
    private boolean fEncodingSchemeSet;
    private boolean fStandaloneSet;
    private boolean nestedCall;

    public StartDocumentEvent() {
        this.fEncodingSchemeSet = false;
        this.fStandaloneSet = false;
        this.nestedCall = false;
        init("UTF-8", "1.0", true, null);
    }

    public StartDocumentEvent(String encoding) {
        this.fEncodingSchemeSet = false;
        this.fStandaloneSet = false;
        this.nestedCall = false;
        init(encoding, "1.0", true, null);
    }

    public StartDocumentEvent(String encoding, String version) {
        this.fEncodingSchemeSet = false;
        this.fStandaloneSet = false;
        this.nestedCall = false;
        init(encoding, version, true, null);
    }

    public StartDocumentEvent(String encoding, String version, boolean standalone) {
        this.fEncodingSchemeSet = false;
        this.fStandaloneSet = false;
        this.nestedCall = false;
        this.fStandaloneSet = true;
        init(encoding, version, standalone, null);
    }

    public StartDocumentEvent(String encoding, String version, boolean standalone, Location loc) {
        this.fEncodingSchemeSet = false;
        this.fStandaloneSet = false;
        this.nestedCall = false;
        this.fStandaloneSet = true;
        init(encoding, version, standalone, loc);
    }

    protected void init(String encoding, String version, boolean standalone, Location loc) {
        setEventType(7);
        this.fEncodingScheam = encoding;
        this.fVersion = version;
        this.fStandalone = standalone;
        if (encoding != null && !encoding.equals("")) {
            this.fEncodingSchemeSet = true;
        } else {
            this.fEncodingSchemeSet = false;
            this.fEncodingScheam = "UTF-8";
        }
        this.fLocation = loc;
    }

    @Override // javax.xml.stream.events.StartDocument
    public String getSystemId() {
        if (this.fLocation == null) {
            return "";
        }
        return this.fLocation.getSystemId();
    }

    @Override // javax.xml.stream.events.StartDocument
    public String getCharacterEncodingScheme() {
        return this.fEncodingScheam;
    }

    @Override // javax.xml.stream.events.StartDocument
    public boolean isStandalone() {
        return this.fStandalone;
    }

    @Override // javax.xml.stream.events.StartDocument
    public String getVersion() {
        return this.fVersion;
    }

    public void setStandalone(boolean flag) {
        this.fStandaloneSet = true;
        this.fStandalone = flag;
    }

    public void setStandalone(String s2) {
        this.fStandaloneSet = true;
        if (s2 == null) {
            this.fStandalone = true;
        } else if (s2.equals("yes")) {
            this.fStandalone = true;
        } else {
            this.fStandalone = false;
        }
    }

    @Override // javax.xml.stream.events.StartDocument
    public boolean encodingSet() {
        return this.fEncodingSchemeSet;
    }

    @Override // javax.xml.stream.events.StartDocument
    public boolean standaloneSet() {
        return this.fStandaloneSet;
    }

    public void setEncoding(String encoding) {
        this.fEncodingScheam = encoding;
    }

    void setDeclaredEncoding(boolean value) {
        this.fEncodingSchemeSet = value;
    }

    public void setVersion(String s2) {
        this.fVersion = s2;
    }

    void clear() {
        this.fEncodingScheam = "UTF-8";
        this.fStandalone = true;
        this.fVersion = "1.0";
        this.fEncodingSchemeSet = false;
        this.fStandaloneSet = false;
    }

    public String toString() {
        String s2;
        String s3 = ("<?xml version=\"" + this.fVersion + PdfOps.DOUBLE_QUOTE__TOKEN) + " encoding='" + this.fEncodingScheam + PdfOps.SINGLE_QUOTE_TOKEN;
        if (this.fStandaloneSet) {
            if (this.fStandalone) {
                s2 = s3 + " standalone='yes'?>";
            } else {
                s2 = s3 + " standalone='no'?>";
            }
        } else {
            s2 = s3 + "?>";
        }
        return s2;
    }

    @Override // com.sun.xml.internal.stream.events.DummyEvent, javax.xml.stream.events.XMLEvent
    public boolean isStartDocument() {
        return true;
    }

    @Override // com.sun.xml.internal.stream.events.DummyEvent
    protected void writeAsEncodedUnicodeEx(Writer writer) throws IOException {
        writer.write(toString());
    }
}
