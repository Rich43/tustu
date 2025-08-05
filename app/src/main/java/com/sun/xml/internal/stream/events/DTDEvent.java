package com.sun.xml.internal.stream.events;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import javax.xml.stream.events.DTD;

/* loaded from: rt.jar:com/sun/xml/internal/stream/events/DTDEvent.class */
public class DTDEvent extends DummyEvent implements DTD {
    private String fDoctypeDeclaration;
    private List fNotations;
    private List fEntities;

    public DTDEvent() {
        init();
    }

    public DTDEvent(String doctypeDeclaration) {
        init();
        this.fDoctypeDeclaration = doctypeDeclaration;
    }

    public void setDocumentTypeDeclaration(String doctypeDeclaration) {
        this.fDoctypeDeclaration = doctypeDeclaration;
    }

    @Override // javax.xml.stream.events.DTD
    public String getDocumentTypeDeclaration() {
        return this.fDoctypeDeclaration;
    }

    public void setEntities(List entites) {
        this.fEntities = entites;
    }

    @Override // javax.xml.stream.events.DTD
    public List getEntities() {
        return this.fEntities;
    }

    public void setNotations(List notations) {
        this.fNotations = notations;
    }

    @Override // javax.xml.stream.events.DTD
    public List getNotations() {
        return this.fNotations;
    }

    @Override // javax.xml.stream.events.DTD
    public Object getProcessedDTD() {
        return null;
    }

    protected void init() {
        setEventType(11);
    }

    public String toString() {
        return this.fDoctypeDeclaration;
    }

    @Override // com.sun.xml.internal.stream.events.DummyEvent
    protected void writeAsEncodedUnicodeEx(Writer writer) throws IOException {
        writer.write(this.fDoctypeDeclaration);
    }
}
