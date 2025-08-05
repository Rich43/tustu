package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dsig.Manifest;
import javax.xml.crypto.dsig.Reference;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMManifest.class */
public final class DOMManifest extends DOMStructure implements Manifest {
    private final List<Reference> references;
    private final String id;

    public DOMManifest(List<? extends Reference> list, String str) {
        if (list == null) {
            throw new NullPointerException("references cannot be null");
        }
        this.references = Collections.unmodifiableList(new ArrayList(list));
        if (this.references.isEmpty()) {
            throw new IllegalArgumentException("list of references must contain at least one entry");
        }
        int size = this.references.size();
        for (int i2 = 0; i2 < size; i2++) {
            if (!(this.references.get(i2) instanceof Reference)) {
                throw new ClassCastException("references[" + i2 + "] is not a valid type");
            }
        }
        this.id = str;
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0096, code lost:
    
        throw new javax.xml.crypto.MarshalException("Invalid element name: " + r0 + jdk.internal.dynalink.CallSiteDescriptor.TOKEN_DELIMITER + r0 + ", expected Reference");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public DOMManifest(org.w3c.dom.Element r8, javax.xml.crypto.XMLCryptoContext r9, java.security.Provider r10) throws javax.xml.crypto.MarshalException {
        /*
            Method dump skipped, instructions count: 246
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jcp.xml.dsig.internal.dom.DOMManifest.<init>(org.w3c.dom.Element, javax.xml.crypto.XMLCryptoContext, java.security.Provider):void");
    }

    @Override // javax.xml.crypto.dsig.Manifest
    public String getId() {
        return this.id;
    }

    public static List<Reference> getManifestReferences(Manifest manifest) {
        return manifest.getReferences();
    }

    @Override // javax.xml.crypto.dsig.Manifest
    public List<Reference> getReferences() {
        return this.references;
    }

    @Override // org.jcp.xml.dsig.internal.dom.DOMStructure
    public void marshal(Node node, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException, DOMException {
        Element elementCreateElement = DOMUtils.createElement(DOMUtils.getOwnerDocument(node), Constants._TAG_MANIFEST, "http://www.w3.org/2000/09/xmldsig#", str);
        DOMUtils.setAttributeID(elementCreateElement, Constants._ATT_ID, this.id);
        Iterator<Reference> it = this.references.iterator();
        while (it.hasNext()) {
            ((DOMReference) it.next()).marshal(elementCreateElement, str, dOMCryptoContext);
        }
        node.appendChild(elementCreateElement);
    }

    public boolean equals(Object obj) {
        boolean zEquals;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Manifest)) {
            return false;
        }
        Manifest manifest = (Manifest) obj;
        if (this.id == null) {
            zEquals = manifest.getId() == null;
        } else {
            zEquals = this.id.equals(manifest.getId());
        }
        return zEquals && this.references.equals(manifest.getReferences());
    }

    public int hashCode() {
        int iHashCode = 17;
        if (this.id != null) {
            iHashCode = (31 * 17) + this.id.hashCode();
        }
        return (31 * iHashCode) + this.references.hashCode();
    }
}
