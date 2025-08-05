package com.sun.org.apache.xalan.internal.xsltc.dom;

import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.DOMCache;
import com.sun.org.apache.xalan.internal.xsltc.DOMEnhancedForDTM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.dtm.DTMManager;
import com.sun.org.apache.xml.internal.dtm.ref.EmptyIterator;
import com.sun.org.apache.xml.internal.utils.SystemIDResolver;
import java.io.FileNotFoundException;
import javax.xml.transform.stream.StreamSource;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/LoadDocument.class */
public final class LoadDocument {
    private static final String NAMESPACE_FEATURE = "http://xml.org/sax/features/namespaces";

    public static DTMAxisIterator documentF(Object arg1, DTMAxisIterator arg2, String xslURI, AbstractTranslet translet, DOM dom) throws TransletException {
        int arg2FirstNode = arg2.next();
        if (arg2FirstNode == -1) {
            return EmptyIterator.getInstance();
        }
        String baseURI = dom.getDocumentURI(arg2FirstNode);
        if (!SystemIDResolver.isAbsoluteURI(baseURI)) {
            baseURI = SystemIDResolver.getAbsoluteURIFromRelative(baseURI);
        }
        try {
            if (arg1 instanceof String) {
                if (((String) arg1).length() == 0) {
                    return document(xslURI, "", translet, dom);
                }
                return document((String) arg1, baseURI, translet, dom);
            }
            if (arg1 instanceof DTMAxisIterator) {
                return document((DTMAxisIterator) arg1, baseURI, translet, dom);
            }
            String err = "document(" + arg1.toString() + ")";
            throw new IllegalArgumentException(err);
        } catch (Exception e2) {
            throw new TransletException(e2);
        }
    }

    public static DTMAxisIterator documentF(Object arg, String xslURI, AbstractTranslet translet, DOM dom) throws TransletException {
        try {
            if (arg instanceof String) {
                if (xslURI == null) {
                    xslURI = "";
                }
                String baseURI = xslURI;
                if (!SystemIDResolver.isAbsoluteURI(xslURI)) {
                    baseURI = SystemIDResolver.getAbsoluteURIFromRelative(xslURI);
                }
                String href = (String) arg;
                if (href.length() == 0) {
                    TemplatesImpl templates = (TemplatesImpl) translet.getTemplates();
                    DOM sdom = null;
                    if (templates != null) {
                        sdom = templates.getStylesheetDOM();
                    }
                    if (sdom != null) {
                        return document(sdom, translet, dom);
                    }
                    return document("", baseURI, translet, dom, true);
                }
                return document(href, baseURI, translet, dom);
            }
            if (arg instanceof DTMAxisIterator) {
                return document((DTMAxisIterator) arg, (String) null, translet, dom);
            }
            String err = "document(" + arg.toString() + ")";
            throw new IllegalArgumentException(err);
        } catch (Exception e2) {
            throw new TransletException(e2);
        }
    }

    private static DTMAxisIterator document(String uri, String base, AbstractTranslet translet, DOM dom) throws Exception {
        return document(uri, base, translet, dom, false);
    }

    private static DTMAxisIterator document(String uri, String base, AbstractTranslet translet, DOM dom, boolean cacheDOM) throws Exception {
        DOM newdom;
        TemplatesImpl templates;
        try {
            MultiDOM multiplexer = (MultiDOM) dom;
            if (base != null && !base.equals("")) {
                uri = SystemIDResolver.getAbsoluteURI(uri, base);
            }
            if (uri == null || uri.equals("")) {
                return EmptyIterator.getInstance();
            }
            int mask = multiplexer.getDocumentMask(uri);
            if (mask != -1) {
                DOM newDom = ((DOMAdapter) multiplexer.getDOMAdapter(uri)).getDOMImpl();
                if (newDom instanceof DOMEnhancedForDTM) {
                    return new SingletonIterator(((DOMEnhancedForDTM) newDom).getDocument(), true);
                }
            }
            DOMCache cache = translet.getDOMCache();
            multiplexer.nextMask();
            if (cache != null) {
                newdom = cache.retrieveDocument(base, uri, translet);
                if (newdom == null) {
                    if (translet.getAccessError() == null) {
                        Exception e2 = new FileNotFoundException(uri);
                        throw new TransletException(e2);
                    }
                    throw new Exception(translet.getAccessError());
                }
            } else {
                String accessError = SecuritySupport.checkAccess(uri, translet.getAllowedProtocols(), "all");
                if (accessError != null) {
                    ErrorMsg msg = new ErrorMsg(ErrorMsg.ACCESSING_XSLT_TARGET_ERR, SecuritySupport.sanitizePath(uri), accessError);
                    throw new Exception(msg.toString());
                }
                XSLTCDTMManager dtmManager = (XSLTCDTMManager) multiplexer.getDTMManager();
                DOMEnhancedForDTM enhancedDOM = (DOMEnhancedForDTM) dtmManager.getDTM(new StreamSource(uri), false, null, true, false, translet.hasIdCall(), cacheDOM);
                newdom = enhancedDOM;
                if (cacheDOM && (templates = (TemplatesImpl) translet.getTemplates()) != null) {
                    templates.setStylesheetDOM(enhancedDOM);
                }
                translet.prepassDocument(enhancedDOM);
                enhancedDOM.setDocumentURI(uri);
            }
            DOMAdapter domAdapter = translet.makeDOMAdapter(newdom);
            multiplexer.addDOMAdapter(domAdapter);
            translet.buildKeys(domAdapter, null, null, newdom.getDocument());
            return new SingletonIterator(newdom.getDocument(), true);
        } catch (Exception e3) {
            throw e3;
        }
    }

    private static DTMAxisIterator document(DTMAxisIterator arg1, String baseURI, AbstractTranslet translet, DOM dom) throws Exception {
        UnionIterator union = new UnionIterator(dom);
        while (true) {
            int node = arg1.next();
            if (node != -1) {
                String uri = dom.getStringValueX(node);
                if (baseURI == null) {
                    baseURI = dom.getDocumentURI(node);
                    if (!SystemIDResolver.isAbsoluteURI(baseURI)) {
                        baseURI = SystemIDResolver.getAbsoluteURIFromRelative(baseURI);
                    }
                }
                union.addIterator(document(uri, baseURI, translet, dom));
            } else {
                return union;
            }
        }
    }

    private static DTMAxisIterator document(DOM newdom, AbstractTranslet translet, DOM dom) throws Exception {
        DTMManager dtmManager = ((MultiDOM) dom).getDTMManager();
        if (dtmManager != null && (newdom instanceof DTM)) {
            ((DTM) newdom).migrateTo(dtmManager);
        }
        translet.prepassDocument(newdom);
        DOMAdapter domAdapter = translet.makeDOMAdapter(newdom);
        ((MultiDOM) dom).addDOMAdapter(domAdapter);
        translet.buildKeys(domAdapter, null, null, newdom.getDocument());
        return new SingletonIterator(newdom.getDocument(), true);
    }
}
