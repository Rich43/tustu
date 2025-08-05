package com.sun.org.apache.xalan.internal.xsltc.trax;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.StripFilter;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.dom.DOMWSFilter;
import com.sun.org.apache.xalan.internal.xsltc.dom.SAXImpl;
import com.sun.org.apache.xalan.internal.xsltc.dom.XSLTCDTMManager;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/trax/XSLTCSource.class */
public final class XSLTCSource implements Source {
    private String _systemId;
    private Source _source;
    private ThreadLocal _dom;

    public XSLTCSource(String systemId) {
        this._systemId = null;
        this._source = null;
        this._dom = new ThreadLocal();
        this._systemId = systemId;
    }

    public XSLTCSource(Source source) {
        this._systemId = null;
        this._source = null;
        this._dom = new ThreadLocal();
        this._source = source;
    }

    @Override // javax.xml.transform.Source
    public void setSystemId(String systemId) {
        this._systemId = systemId;
        if (this._source != null) {
            this._source.setSystemId(systemId);
        }
    }

    @Override // javax.xml.transform.Source
    public String getSystemId() {
        if (this._source != null) {
            return this._source.getSystemId();
        }
        return this._systemId;
    }

    protected DOM getDOM(XSLTCDTMManager dtmManager, AbstractTranslet translet) throws SAXException {
        SAXImpl idom = (SAXImpl) this._dom.get();
        if (idom != null) {
            if (dtmManager != null) {
                idom.migrateTo(dtmManager);
            }
        } else {
            Source source = this._source;
            if (source == null) {
                if (this._systemId != null && this._systemId.length() > 0) {
                    source = new StreamSource(this._systemId);
                } else {
                    ErrorMsg err = new ErrorMsg(ErrorMsg.XSLTC_SOURCE_ERR);
                    throw new SAXException(err.toString());
                }
            }
            DOMWSFilter wsfilter = null;
            if (translet != null && (translet instanceof StripFilter)) {
                wsfilter = new DOMWSFilter(translet);
            }
            boolean hasIdCall = translet != null ? translet.hasIdCall() : false;
            if (dtmManager == null) {
                dtmManager = XSLTCDTMManager.newInstance();
            }
            idom = (SAXImpl) dtmManager.getDTM(source, true, wsfilter, false, false, hasIdCall);
            String systemId = getSystemId();
            if (systemId != null) {
                idom.setDocumentURI(systemId);
            }
            this._dom.set(idom);
        }
        return idom;
    }
}
