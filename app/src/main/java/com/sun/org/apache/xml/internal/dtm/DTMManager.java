package com.sun.org.apache.xml.internal.dtm;

import com.sun.org.apache.xml.internal.dtm.ref.DTMManagerDefault;
import com.sun.org.apache.xml.internal.utils.PrefixResolver;
import com.sun.org.apache.xml.internal.utils.XMLStringFactory;
import javax.xml.transform.Source;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/DTMManager.class */
public abstract class DTMManager {
    private boolean _overrideDefaultParser;
    public static final int IDENT_DTM_NODE_BITS = 16;
    public static final int IDENT_NODE_DEFAULT = 65535;
    public static final int IDENT_DTM_DEFAULT = -65536;
    public static final int IDENT_MAX_DTMS = 65536;
    protected XMLStringFactory m_xsf = null;
    public boolean m_incremental = false;
    public boolean m_source_location = false;

    public abstract DTM getDTM(Source source, boolean z2, DTMWSFilter dTMWSFilter, boolean z3, boolean z4);

    public abstract DTM getDTM(int i2);

    public abstract int getDTMHandleFromNode(Node node);

    public abstract DTM createDocumentFragment();

    public abstract boolean release(DTM dtm, boolean z2);

    public abstract DTMIterator createDTMIterator(Object obj, int i2);

    public abstract DTMIterator createDTMIterator(String str, PrefixResolver prefixResolver);

    public abstract DTMIterator createDTMIterator(int i2, DTMFilter dTMFilter, boolean z2);

    public abstract DTMIterator createDTMIterator(int i2);

    public abstract int getDTMIdentity(DTM dtm);

    protected DTMManager() {
    }

    public XMLStringFactory getXMLStringFactory() {
        return this.m_xsf;
    }

    public void setXMLStringFactory(XMLStringFactory xsf) {
        this.m_xsf = xsf;
    }

    public static DTMManager newInstance(XMLStringFactory xsf) throws DTMException {
        DTMManager factoryImpl = new DTMManagerDefault();
        factoryImpl.setXMLStringFactory(xsf);
        return factoryImpl;
    }

    public boolean getIncremental() {
        return this.m_incremental;
    }

    public void setIncremental(boolean incremental) {
        this.m_incremental = incremental;
    }

    public boolean getSource_location() {
        return this.m_source_location;
    }

    public void setSource_location(boolean sourceLocation) {
        this.m_source_location = sourceLocation;
    }

    public boolean overrideDefaultParser() {
        return this._overrideDefaultParser;
    }

    public void setOverrideDefaultParser(boolean flag) {
        this._overrideDefaultParser = flag;
    }

    public int getDTMIdentityMask() {
        return IDENT_DTM_DEFAULT;
    }

    public int getNodeIdentityMask() {
        return 65535;
    }
}
