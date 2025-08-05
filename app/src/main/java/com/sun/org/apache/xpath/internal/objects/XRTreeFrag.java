package com.sun.org.apache.xpath.internal.objects;

import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.dtm.DTMIterator;
import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeIterator;
import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeList;
import com.sun.org.apache.xml.internal.utils.FastStringBuffer;
import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
import com.sun.org.apache.xml.internal.utils.XMLString;
import com.sun.org.apache.xpath.internal.Expression;
import com.sun.org.apache.xpath.internal.ExpressionNode;
import com.sun.org.apache.xpath.internal.NodeSetDTM;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.axes.RTFIterator;
import javax.xml.transform.TransformerException;
import org.w3c.dom.NodeList;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/objects/XRTreeFrag.class */
public class XRTreeFrag extends XObject implements Cloneable {
    static final long serialVersionUID = -3201553822254911567L;
    private DTMXRTreeFrag m_DTMXRTreeFrag;
    private int m_dtmRoot;
    protected boolean m_allowRelease;
    private XMLString m_xmlStr;

    public XRTreeFrag(int root, XPathContext xctxt, ExpressionNode parent) {
        super(null);
        this.m_dtmRoot = -1;
        this.m_allowRelease = false;
        this.m_xmlStr = null;
        exprSetParent(parent);
        initDTM(root, xctxt);
    }

    public XRTreeFrag(int root, XPathContext xctxt) {
        super(null);
        this.m_dtmRoot = -1;
        this.m_allowRelease = false;
        this.m_xmlStr = null;
        initDTM(root, xctxt);
    }

    private final void initDTM(int root, XPathContext xctxt) {
        this.m_dtmRoot = root;
        DTM dtm = xctxt.getDTM(root);
        if (dtm != null) {
            this.m_DTMXRTreeFrag = xctxt.getDTMXRTreeFrag(xctxt.getDTMIdentity(dtm));
        }
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public Object object() {
        if (this.m_DTMXRTreeFrag.getXPathContext() != null) {
            return new DTMNodeIterator(new NodeSetDTM(this.m_dtmRoot, this.m_DTMXRTreeFrag.getXPathContext().getDTMManager()));
        }
        return super.object();
    }

    public XRTreeFrag(Expression expr) {
        super(expr);
        this.m_dtmRoot = -1;
        this.m_allowRelease = false;
        this.m_xmlStr = null;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void allowDetachToRelease(boolean allowRelease) {
        this.m_allowRelease = allowRelease;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void detach() {
        if (this.m_allowRelease) {
            this.m_DTMXRTreeFrag.destruct();
            setObject(null);
        }
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public int getType() {
        return 5;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public String getTypeString() {
        return "#RTREEFRAG";
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public double num() throws TransformerException {
        XMLString s2 = xstr();
        return s2.toDouble();
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public boolean bool() {
        return true;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public XMLString xstr() {
        if (null == this.m_xmlStr) {
            this.m_xmlStr = this.m_DTMXRTreeFrag.getDTM().getStringValue(this.m_dtmRoot);
        }
        return this.m_xmlStr;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public void appendToFsb(FastStringBuffer fsb) {
        XString xstring = (XString) xstr();
        xstring.appendToFsb(fsb);
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public String str() {
        String str = this.m_DTMXRTreeFrag.getDTM().getStringValue(this.m_dtmRoot).toString();
        return null == str ? "" : str;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public int rtf() {
        return this.m_dtmRoot;
    }

    public DTMIterator asNodeIterator() {
        return new RTFIterator(this.m_dtmRoot, this.m_DTMXRTreeFrag.getXPathContext().getDTMManager());
    }

    public NodeList convertToNodeset() {
        if (this.m_obj instanceof NodeList) {
            return (NodeList) this.m_obj;
        }
        return new DTMNodeList(asNodeIterator());
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public boolean equals(XObject obj2) {
        try {
            if (4 == obj2.getType()) {
                return obj2.equals((XObject) this);
            }
            if (1 == obj2.getType()) {
                return bool() == obj2.bool();
            }
            if (2 == obj2.getType()) {
                return num() == obj2.num();
            }
            if (4 == obj2.getType()) {
                return xstr().equals(obj2.xstr());
            }
            if (3 == obj2.getType()) {
                return xstr().equals(obj2.xstr());
            }
            if (5 == obj2.getType()) {
                return xstr().equals(obj2.xstr());
            }
            return super.equals(obj2);
        } catch (TransformerException te) {
            throw new WrappedRuntimeException(te);
        }
    }
}
