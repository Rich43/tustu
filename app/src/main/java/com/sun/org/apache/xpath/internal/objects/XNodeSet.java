package com.sun.org.apache.xpath.internal.objects;

import com.sun.org.apache.xml.internal.dtm.DTMIterator;
import com.sun.org.apache.xml.internal.dtm.DTMManager;
import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeIterator;
import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeList;
import com.sun.org.apache.xml.internal.utils.FastStringBuffer;
import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
import com.sun.org.apache.xml.internal.utils.XMLString;
import com.sun.org.apache.xpath.internal.NodeSetDTM;
import com.sun.org.apache.xpath.internal.axes.NodeSequence;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/objects/XNodeSet.class */
public class XNodeSet extends NodeSequence {
    static final long serialVersionUID = 1916026368035639667L;
    static final LessThanComparator S_LT = new LessThanComparator();
    static final LessThanOrEqualComparator S_LTE = new LessThanOrEqualComparator();
    static final GreaterThanComparator S_GT = new GreaterThanComparator();
    static final GreaterThanOrEqualComparator S_GTE = new GreaterThanOrEqualComparator();
    static final EqualComparator S_EQ = new EqualComparator();
    static final NotEqualComparator S_NEQ = new NotEqualComparator();

    protected XNodeSet() {
    }

    public XNodeSet(DTMIterator val) {
        if (val instanceof XNodeSet) {
            XNodeSet nodeSet = (XNodeSet) val;
            setIter(nodeSet.m_iter);
            this.m_dtmMgr = nodeSet.m_dtmMgr;
            this.m_last = nodeSet.m_last;
            if (!nodeSet.hasCache()) {
                nodeSet.setShouldCacheNodes(true);
            }
            setObject(nodeSet.getIteratorCache());
            return;
        }
        setIter(val);
    }

    public XNodeSet(XNodeSet val) {
        setIter(val.m_iter);
        this.m_dtmMgr = val.m_dtmMgr;
        this.m_last = val.m_last;
        if (!val.hasCache()) {
            val.setShouldCacheNodes(true);
        }
        setObject(val.m_obj);
    }

    public XNodeSet(DTMManager dtmMgr) {
        this(-1, dtmMgr);
    }

    public XNodeSet(int n2, DTMManager dtmMgr) {
        super(new NodeSetDTM(dtmMgr));
        this.m_dtmMgr = dtmMgr;
        if (-1 != n2) {
            ((NodeSetDTM) this.m_obj).addNode(n2);
            this.m_last = 1;
        } else {
            this.m_last = 0;
        }
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public int getType() {
        return 4;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public String getTypeString() {
        return "#NODESET";
    }

    public double getNumberFromNode(int n2) {
        XMLString xstr = this.m_dtmMgr.getDTM(n2).getStringValue(n2);
        return xstr.toDouble();
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public double num() {
        int node = item(0);
        if (node != -1) {
            return getNumberFromNode(node);
        }
        return Double.NaN;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public double numWithSideEffects() {
        int node = nextNode();
        if (node != -1) {
            return getNumberFromNode(node);
        }
        return Double.NaN;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public boolean bool() {
        return item(0) != -1;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public boolean boolWithSideEffects() {
        return nextNode() != -1;
    }

    public XMLString getStringFromNode(int n2) {
        if (-1 != n2) {
            return this.m_dtmMgr.getDTM(n2).getStringValue(n2);
        }
        return XString.EMPTYSTRING;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public void dispatchCharactersEvents(ContentHandler ch) throws SAXException {
        int node = item(0);
        if (node != -1) {
            this.m_dtmMgr.getDTM(node).dispatchCharactersEvents(node, ch, false);
        }
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public XMLString xstr() {
        int node = item(0);
        return node != -1 ? getStringFromNode(node) : XString.EMPTYSTRING;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public void appendToFsb(FastStringBuffer fsb) {
        XString xstring = (XString) xstr();
        xstring.appendToFsb(fsb);
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public String str() {
        int node = item(0);
        return node != -1 ? getStringFromNode(node).toString() : "";
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public Object object() {
        if (null == this.m_obj) {
            return this;
        }
        return this.m_obj;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public NodeIterator nodeset() throws TransformerException {
        return new DTMNodeIterator(iter());
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public NodeList nodelist() throws TransformerException {
        DTMNodeList nodelist = new DTMNodeList(this);
        XNodeSet clone = (XNodeSet) nodelist.getDTMIterator();
        SetVector(clone.getVector());
        return nodelist;
    }

    public DTMIterator iterRaw() {
        return this;
    }

    public void release(DTMIterator iter) {
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public DTMIterator iter() {
        try {
            if (hasCache()) {
                return cloneWithReset();
            }
            return this;
        } catch (CloneNotSupportedException cnse) {
            throw new RuntimeException(cnse.getMessage());
        }
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public XObject getFresh() {
        try {
            if (hasCache()) {
                return (XObject) cloneWithReset();
            }
            return this;
        } catch (CloneNotSupportedException cnse) {
            throw new RuntimeException(cnse.getMessage());
        }
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public NodeSetDTM mutableNodeset() {
        NodeSetDTM mnl;
        if (this.m_obj instanceof NodeSetDTM) {
            mnl = (NodeSetDTM) this.m_obj;
        } else {
            mnl = new NodeSetDTM(iter());
            setObject(mnl);
            setCurrentPos(0);
        }
        return mnl;
    }

    public boolean compare(XObject obj2, Comparator comparator) throws TransformerException {
        boolean result = false;
        int type = obj2.getType();
        if (4 == type) {
            DTMIterator list1 = iterRaw();
            DTMIterator list2 = ((XNodeSet) obj2).iterRaw();
            Vector node2Strings = null;
            while (true) {
                int node1 = list1.nextNode();
                if (-1 == node1) {
                    break;
                }
                XMLString s1 = getStringFromNode(node1);
                if (null == node2Strings) {
                    while (true) {
                        int node2 = list2.nextNode();
                        if (-1 != node2) {
                            XMLString s2 = getStringFromNode(node2);
                            if (comparator.compareStrings(s1, s2)) {
                                result = true;
                                break;
                            }
                            if (null == node2Strings) {
                                node2Strings = new Vector();
                            }
                            node2Strings.addElement(s2);
                        }
                    }
                } else {
                    int n2 = node2Strings.size();
                    int i2 = 0;
                    while (true) {
                        if (i2 >= n2) {
                            break;
                        }
                        if (!comparator.compareStrings(s1, (XMLString) node2Strings.elementAt(i2))) {
                            i2++;
                        } else {
                            result = true;
                            break;
                        }
                    }
                }
            }
            list1.reset();
            list2.reset();
        } else if (1 == type) {
            double num1 = bool() ? 1.0d : 0.0d;
            double num2 = obj2.num();
            result = comparator.compareNumbers(num1, num2);
        } else if (2 == type) {
            DTMIterator list12 = iterRaw();
            double num22 = obj2.num();
            while (true) {
                int node = list12.nextNode();
                if (-1 == node) {
                    break;
                }
                double num12 = getNumberFromNode(node);
                if (comparator.compareNumbers(num12, num22)) {
                    result = true;
                    break;
                }
            }
            list12.reset();
        } else if (5 == type) {
            XMLString s22 = obj2.xstr();
            DTMIterator list13 = iterRaw();
            while (true) {
                int node3 = list13.nextNode();
                if (-1 == node3) {
                    break;
                }
                XMLString s12 = getStringFromNode(node3);
                if (comparator.compareStrings(s12, s22)) {
                    result = true;
                    break;
                }
            }
            list13.reset();
        } else if (3 == type) {
            XMLString s23 = obj2.xstr();
            DTMIterator list14 = iterRaw();
            while (true) {
                int node4 = list14.nextNode();
                if (-1 == node4) {
                    break;
                }
                XMLString s13 = getStringFromNode(node4);
                if (comparator.compareStrings(s13, s23)) {
                    result = true;
                    break;
                }
            }
            list14.reset();
        } else {
            result = comparator.compareNumbers(num(), obj2.num());
        }
        return result;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public boolean lessThan(XObject obj2) throws TransformerException {
        return compare(obj2, S_LT);
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public boolean lessThanOrEqual(XObject obj2) throws TransformerException {
        return compare(obj2, S_LTE);
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public boolean greaterThan(XObject obj2) throws TransformerException {
        return compare(obj2, S_GT);
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public boolean greaterThanOrEqual(XObject obj2) throws TransformerException {
        return compare(obj2, S_GTE);
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public boolean equals(XObject obj2) {
        try {
            return compare(obj2, S_EQ);
        } catch (TransformerException te) {
            throw new WrappedRuntimeException(te);
        }
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public boolean notEquals(XObject obj2) throws TransformerException {
        return compare(obj2, S_NEQ);
    }
}
