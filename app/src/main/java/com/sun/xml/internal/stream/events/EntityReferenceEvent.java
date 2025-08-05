package com.sun.xml.internal.stream.events;

import java.io.IOException;
import java.io.Writer;
import javax.xml.stream.events.EntityDeclaration;
import javax.xml.stream.events.EntityReference;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/xml/internal/stream/events/EntityReferenceEvent.class */
public class EntityReferenceEvent extends DummyEvent implements EntityReference {
    private EntityDeclaration fEntityDeclaration;
    private String fEntityName;

    public EntityReferenceEvent() {
        init();
    }

    public EntityReferenceEvent(String entityName, EntityDeclaration entityDeclaration) {
        init();
        this.fEntityName = entityName;
        this.fEntityDeclaration = entityDeclaration;
    }

    @Override // javax.xml.stream.events.EntityReference
    public String getName() {
        return this.fEntityName;
    }

    public String toString() {
        String text = this.fEntityDeclaration.getReplacementText();
        if (text == null) {
            text = "";
        }
        return "&" + getName() + ";='" + text + PdfOps.SINGLE_QUOTE_TOKEN;
    }

    @Override // com.sun.xml.internal.stream.events.DummyEvent
    protected void writeAsEncodedUnicodeEx(Writer writer) throws IOException {
        writer.write(38);
        writer.write(getName());
        writer.write(59);
    }

    @Override // javax.xml.stream.events.EntityReference
    public EntityDeclaration getDeclaration() {
        return this.fEntityDeclaration;
    }

    protected void init() {
        setEventType(9);
    }
}
