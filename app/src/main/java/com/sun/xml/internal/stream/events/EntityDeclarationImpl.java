package com.sun.xml.internal.stream.events;

import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import java.io.IOException;
import java.io.Writer;
import javax.xml.stream.events.EntityDeclaration;
import jdk.xml.internal.JdkXmlUtils;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/xml/internal/stream/events/EntityDeclarationImpl.class */
public class EntityDeclarationImpl extends DummyEvent implements EntityDeclaration {
    private XMLResourceIdentifier fXMLResourceIdentifier;
    private String fEntityName;
    private String fReplacementText;
    private String fNotationName;

    public EntityDeclarationImpl() {
        init();
    }

    public EntityDeclarationImpl(String entityName, String replacementText) {
        this(entityName, replacementText, null);
    }

    public EntityDeclarationImpl(String entityName, String replacementText, XMLResourceIdentifier resourceIdentifier) {
        init();
        this.fEntityName = entityName;
        this.fReplacementText = replacementText;
        this.fXMLResourceIdentifier = resourceIdentifier;
    }

    public void setEntityName(String entityName) {
        this.fEntityName = entityName;
    }

    public String getEntityName() {
        return this.fEntityName;
    }

    public void setEntityReplacementText(String replacementText) {
        this.fReplacementText = replacementText;
    }

    public void setXMLResourceIdentifier(XMLResourceIdentifier resourceIdentifier) {
        this.fXMLResourceIdentifier = resourceIdentifier;
    }

    public XMLResourceIdentifier getXMLResourceIdentifier() {
        return this.fXMLResourceIdentifier;
    }

    @Override // javax.xml.stream.events.EntityDeclaration
    public String getSystemId() {
        if (this.fXMLResourceIdentifier != null) {
            return this.fXMLResourceIdentifier.getLiteralSystemId();
        }
        return null;
    }

    @Override // javax.xml.stream.events.EntityDeclaration
    public String getPublicId() {
        if (this.fXMLResourceIdentifier != null) {
            return this.fXMLResourceIdentifier.getPublicId();
        }
        return null;
    }

    @Override // javax.xml.stream.events.EntityDeclaration
    public String getBaseURI() {
        if (this.fXMLResourceIdentifier != null) {
            return this.fXMLResourceIdentifier.getBaseSystemId();
        }
        return null;
    }

    @Override // javax.xml.stream.events.EntityDeclaration
    public String getName() {
        return this.fEntityName;
    }

    @Override // javax.xml.stream.events.EntityDeclaration
    public String getNotationName() {
        return this.fNotationName;
    }

    public void setNotationName(String notationName) {
        this.fNotationName = notationName;
    }

    @Override // javax.xml.stream.events.EntityDeclaration
    public String getReplacementText() {
        return this.fReplacementText;
    }

    protected void init() {
        setEventType(15);
    }

    @Override // com.sun.xml.internal.stream.events.DummyEvent
    protected void writeAsEncodedUnicodeEx(Writer writer) throws IOException {
        writer.write("<!ENTITY ");
        writer.write(this.fEntityName);
        if (this.fReplacementText != null) {
            writer.write(" \"");
            charEncode(writer, this.fReplacementText);
            writer.write(PdfOps.DOUBLE_QUOTE__TOKEN);
        } else {
            writer.write(JdkXmlUtils.getDTDExternalDecl(getPublicId(), getSystemId()));
        }
        if (this.fNotationName != null) {
            writer.write(" NDATA ");
            writer.write(this.fNotationName);
        }
        writer.write(">");
    }
}
