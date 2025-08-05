package com.sun.org.apache.xpath.internal.objects;

import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xml.internal.dtm.DTMIterator;
import com.sun.org.apache.xml.internal.utils.XMLString;
import com.sun.org.apache.xpath.internal.Expression;
import com.sun.org.apache.xpath.internal.XPathContext;
import java.util.Vector;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/objects/XRTreeFragSelectWrapper.class */
public class XRTreeFragSelectWrapper extends XRTreeFrag implements Cloneable {
    static final long serialVersionUID = -6526177905590461251L;

    public XRTreeFragSelectWrapper(Expression expr) {
        super(expr);
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject, com.sun.org.apache.xpath.internal.Expression
    public void fixupVariables(Vector vars, int globalsSize) {
        ((Expression) this.m_obj).fixupVariables(vars, globalsSize);
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject, com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt) throws TransformerException {
        XObject m_selected = ((Expression) this.m_obj).execute(xctxt);
        m_selected.allowDetachToRelease(this.m_allowRelease);
        if (m_selected.getType() == 3) {
            return m_selected;
        }
        return new XString(m_selected.str());
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XRTreeFrag, com.sun.org.apache.xpath.internal.objects.XObject, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void detach() {
        throw new RuntimeException(XSLMessages.createXPATHMessage("ER_DETACH_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER", null));
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XRTreeFrag, com.sun.org.apache.xpath.internal.objects.XObject
    public double num() throws TransformerException {
        throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NUM_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER", null));
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XRTreeFrag, com.sun.org.apache.xpath.internal.objects.XObject
    public XMLString xstr() {
        throw new RuntimeException(XSLMessages.createXPATHMessage("ER_XSTR_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER", null));
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XRTreeFrag, com.sun.org.apache.xpath.internal.objects.XObject
    public String str() {
        throw new RuntimeException(XSLMessages.createXPATHMessage("ER_STR_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER", null));
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XRTreeFrag, com.sun.org.apache.xpath.internal.objects.XObject
    public int getType() {
        return 3;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XRTreeFrag, com.sun.org.apache.xpath.internal.objects.XObject
    public int rtf() {
        throw new RuntimeException(XSLMessages.createXPATHMessage("ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER", null));
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XRTreeFrag
    public DTMIterator asNodeIterator() {
        throw new RuntimeException(XSLMessages.createXPATHMessage("ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER", null));
    }
}
