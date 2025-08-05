package com.sun.xml.internal.bind.v2.runtime.output;

import com.sun.xml.internal.bind.marshaller.SAX2DOMEx;
import com.sun.xml.internal.bind.v2.runtime.AssociationMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/output/DOMOutput.class */
public final class DOMOutput extends SAXOutput {
    private final AssociationMap assoc;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DOMOutput.class.desiredAssertionStatus();
    }

    public DOMOutput(Node node, AssociationMap assoc) {
        super(new SAX2DOMEx(node));
        this.assoc = assoc;
        if (!$assertionsDisabled && assoc == null) {
            throw new AssertionError();
        }
    }

    private SAX2DOMEx getBuilder() {
        return (SAX2DOMEx) this.out;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.SAXOutput, com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void endStartTag() throws SAXException {
        super.endStartTag();
        Object op = this.nsContext.getCurrent().getOuterPeer();
        if (op != null) {
            this.assoc.addOuter(getBuilder().getCurrentElement(), op);
        }
        Object ip = this.nsContext.getCurrent().getInnerPeer();
        if (ip != null) {
            this.assoc.addInner(getBuilder().getCurrentElement(), ip);
        }
    }
}
