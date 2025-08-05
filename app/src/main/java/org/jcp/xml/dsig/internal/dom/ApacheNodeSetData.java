package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.signature.NodeFilter;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.xml.crypto.NodeSetData;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/ApacheNodeSetData.class */
public class ApacheNodeSetData implements ApacheData, NodeSetData {
    private XMLSignatureInput xi;

    public ApacheNodeSetData(XMLSignatureInput xMLSignatureInput) {
        this.xi = xMLSignatureInput;
    }

    @Override // javax.xml.crypto.NodeSetData
    public Iterator<Node> iterator() {
        if (this.xi.getNodeFilters() != null && !this.xi.getNodeFilters().isEmpty()) {
            return Collections.unmodifiableSet(getNodeSet(this.xi.getNodeFilters())).iterator();
        }
        try {
            return Collections.unmodifiableSet(this.xi.getNodeSet()).iterator();
        } catch (Exception e2) {
            throw new RuntimeException("unrecoverable error retrieving nodeset", e2);
        }
    }

    @Override // org.jcp.xml.dsig.internal.dom.ApacheData
    public XMLSignatureInput getXMLSignatureInput() {
        return this.xi;
    }

    private Set<Node> getNodeSet(List<NodeFilter> list) throws DOMException {
        if (this.xi.isNeedsToBeExpanded()) {
            XMLUtils.circumventBug2650(XMLUtils.getOwnerDocument(this.xi.getSubNode()));
        }
        LinkedHashSet<Node> linkedHashSet = new LinkedHashSet();
        XMLUtils.getSet(this.xi.getSubNode(), linkedHashSet, null, !this.xi.isExcludeComments());
        LinkedHashSet linkedHashSet2 = new LinkedHashSet();
        for (Node node : linkedHashSet) {
            Iterator<NodeFilter> it = list.iterator();
            boolean z2 = false;
            while (it.hasNext() && !z2) {
                if (it.next().isNodeInclude(node) != 1) {
                    z2 = true;
                }
            }
            if (!z2) {
                linkedHashSet2.add(node);
            }
        }
        return linkedHashSet2;
    }
}
