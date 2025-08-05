package com.sun.xml.internal.stream.events;

import com.sun.xml.internal.stream.dtd.nonvalidating.XMLNotationDecl;
import java.io.IOException;
import java.io.Writer;
import javax.xml.stream.events.NotationDeclaration;
import jdk.xml.internal.JdkXmlUtils;

/* loaded from: rt.jar:com/sun/xml/internal/stream/events/NotationDeclarationImpl.class */
public class NotationDeclarationImpl extends DummyEvent implements NotationDeclaration {
    String fName;
    String fPublicId;
    String fSystemId;

    public NotationDeclarationImpl() {
        this.fName = null;
        this.fPublicId = null;
        this.fSystemId = null;
        setEventType(14);
    }

    public NotationDeclarationImpl(String name, String publicId, String systemId) {
        this.fName = null;
        this.fPublicId = null;
        this.fSystemId = null;
        this.fName = name;
        this.fPublicId = publicId;
        this.fSystemId = systemId;
        setEventType(14);
    }

    public NotationDeclarationImpl(XMLNotationDecl notation) {
        this.fName = null;
        this.fPublicId = null;
        this.fSystemId = null;
        this.fName = notation.name;
        this.fPublicId = notation.publicId;
        this.fSystemId = notation.systemId;
        setEventType(14);
    }

    @Override // javax.xml.stream.events.NotationDeclaration
    public String getName() {
        return this.fName;
    }

    @Override // javax.xml.stream.events.NotationDeclaration
    public String getPublicId() {
        return this.fPublicId;
    }

    @Override // javax.xml.stream.events.NotationDeclaration
    public String getSystemId() {
        return this.fSystemId;
    }

    void setPublicId(String publicId) {
        this.fPublicId = publicId;
    }

    void setSystemId(String systemId) {
        this.fSystemId = systemId;
    }

    void setName(String name) {
        this.fName = name;
    }

    @Override // com.sun.xml.internal.stream.events.DummyEvent
    protected void writeAsEncodedUnicodeEx(Writer writer) throws IOException {
        writer.write("<!NOTATION ");
        writer.write(getName());
        writer.write(JdkXmlUtils.getDTDExternalDecl(this.fPublicId, this.fSystemId));
        writer.write(62);
    }
}
