package com.sun.org.apache.xerces.internal.jaxp.validation;

import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import javax.xml.transform.dom.DOMResult;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/validation/DOMDocumentHandler.class */
interface DOMDocumentHandler extends XMLDocumentHandler {
    void setDOMResult(DOMResult dOMResult);

    void doctypeDecl(DocumentType documentType) throws XNIException;

    void characters(Text text) throws XNIException;

    void cdata(CDATASection cDATASection) throws XNIException;

    void comment(Comment comment) throws XNIException;

    void processingInstruction(ProcessingInstruction processingInstruction) throws XNIException;

    void setIgnoringCharacters(boolean z2);
}
