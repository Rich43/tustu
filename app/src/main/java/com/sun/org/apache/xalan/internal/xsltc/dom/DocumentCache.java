package com.sun.org.apache.xalan.internal.xsltc.dom;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.DOMCache;
import com.sun.org.apache.xalan.internal.xsltc.DOMEnhancedForDTM;
import com.sun.org.apache.xalan.internal.xsltc.Translet;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
import com.sun.org.apache.xml.internal.utils.SystemIDResolver;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXSource;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/DocumentCache.class */
public final class DocumentCache implements DOMCache {
    private int _size;
    private Map<String, CachedDocument> _references;
    private String[] _URIs;
    private int _count;
    private int _current;
    private SAXParser _parser;
    private XMLReader _reader;
    private XSLTCDTMManager _dtmManager;
    private static final int REFRESH_INTERVAL = 1000;

    /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/DocumentCache$CachedDocument.class */
    public final class CachedDocument {
        private long _firstReferenced;
        private long _lastReferenced;
        private long _accessCount;
        private long _lastModified;
        private long _lastChecked;
        private long _buildTime;
        private DOMEnhancedForDTM _dom = null;

        public CachedDocument(String uri) {
            long stamp = System.currentTimeMillis();
            this._firstReferenced = stamp;
            this._lastReferenced = stamp;
            this._accessCount = 0L;
            loadDocument(uri);
            this._buildTime = System.currentTimeMillis() - stamp;
        }

        public void loadDocument(String uri) {
            try {
                long stamp = System.currentTimeMillis();
                this._dom = (DOMEnhancedForDTM) DocumentCache.this._dtmManager.getDTM(new SAXSource(DocumentCache.this._reader, new InputSource(uri)), false, null, true, false);
                this._dom.setDocumentURI(uri);
                long thisTime = System.currentTimeMillis() - stamp;
                if (this._buildTime > 0) {
                    this._buildTime = (this._buildTime + thisTime) >>> 1;
                } else {
                    this._buildTime = thisTime;
                }
            } catch (Exception e2) {
                this._dom = null;
            }
        }

        public DOM getDocument() {
            return this._dom;
        }

        public long getFirstReferenced() {
            return this._firstReferenced;
        }

        public long getLastReferenced() {
            return this._lastReferenced;
        }

        public long getAccessCount() {
            return this._accessCount;
        }

        public void incAccessCount() {
            this._accessCount++;
        }

        public long getLastModified() {
            return this._lastModified;
        }

        public void setLastModified(long t2) {
            this._lastModified = t2;
        }

        public long getLatency() {
            return this._buildTime;
        }

        public long getLastChecked() {
            return this._lastChecked;
        }

        public void setLastChecked(long t2) {
            this._lastChecked = t2;
        }

        public long getEstimatedSize() {
            if (this._dom != null) {
                return this._dom.getSize() << 5;
            }
            return 0L;
        }
    }

    public DocumentCache(int size) throws SAXException {
        this(size, null);
        try {
            this._dtmManager = XSLTCDTMManager.createNewDTMManagerInstance();
        } catch (Exception e2) {
            throw new SAXException(e2);
        }
    }

    public DocumentCache(int size, XSLTCDTMManager dtmManager) throws SAXException {
        this._dtmManager = dtmManager;
        this._count = 0;
        this._current = 0;
        this._size = size;
        this._references = new HashMap(this._size + 2);
        this._URIs = new String[this._size];
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            try {
                factory.setFeature("http://xml.org/sax/features/namespaces", true);
            } catch (Exception e2) {
                factory.setNamespaceAware(true);
            }
            this._parser = factory.newSAXParser();
            this._reader = this._parser.getXMLReader();
        } catch (ParserConfigurationException e3) {
            BasisLibrary.runTimeError(BasisLibrary.NAMESPACES_SUPPORT_ERR);
        }
    }

    private final long getLastModified(String uri) {
        try {
            URL url = new URL(uri);
            URLConnection connection = url.openConnection();
            long timestamp = connection.getLastModified();
            if (timestamp == 0 && DeploymentDescriptorParser.ATTR_FILE.equals(url.getProtocol())) {
                File localfile = new File(URLDecoder.decode(url.getFile()));
                timestamp = localfile.lastModified();
            }
            return timestamp;
        } catch (Exception e2) {
            return System.currentTimeMillis();
        }
    }

    private CachedDocument lookupDocument(String uri) {
        return this._references.get(uri);
    }

    private synchronized void insertDocument(String uri, CachedDocument doc) {
        if (this._count < this._size) {
            String[] strArr = this._URIs;
            int i2 = this._count;
            this._count = i2 + 1;
            strArr[i2] = uri;
            this._current = 0;
        } else {
            this._references.remove(this._URIs[this._current]);
            this._URIs[this._current] = uri;
            int i3 = this._current + 1;
            this._current = i3;
            if (i3 >= this._size) {
                this._current = 0;
            }
        }
        this._references.put(uri, doc);
    }

    private synchronized void replaceDocument(String uri, CachedDocument doc) {
        if (doc == null) {
            insertDocument(uri, doc);
        } else {
            this._references.put(uri, doc);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOMCache
    public DOM retrieveDocument(String baseURI, String href, Translet trs) {
        String uri = href;
        if (baseURI != null && !baseURI.equals("")) {
            try {
                uri = SystemIDResolver.getAbsoluteURI(uri, baseURI);
            } catch (TransformerException e2) {
            }
        }
        CachedDocument cachedDocumentLookupDocument = lookupDocument(uri);
        CachedDocument doc = cachedDocumentLookupDocument;
        if (cachedDocumentLookupDocument == null) {
            doc = new CachedDocument(uri);
            if (doc == null) {
                return null;
            }
            doc.setLastModified(getLastModified(uri));
            insertDocument(uri, doc);
        } else {
            long now = System.currentTimeMillis();
            long chk = doc.getLastChecked();
            doc.setLastChecked(now);
            if (now > chk + 1000) {
                doc.setLastChecked(now);
                long last = getLastModified(uri);
                if (last > doc.getLastModified()) {
                    doc = new CachedDocument(uri);
                    if (doc == null) {
                        return null;
                    }
                    doc.setLastModified(getLastModified(uri));
                    replaceDocument(uri, doc);
                }
            }
        }
        DOM dom = doc.getDocument();
        if (dom == null) {
            return null;
        }
        doc.incAccessCount();
        AbstractTranslet translet = (AbstractTranslet) trs;
        translet.prepassDocument(dom);
        return doc.getDocument();
    }

    public void getStatistics(PrintWriter out) {
        out.println("<h2>DOM cache statistics</h2><center><table border=\"2\"><tr><td><b>Document URI</b></td><td><center><b>Build time</b></center></td><td><center><b>Access count</b></center></td><td><center><b>Last accessed</b></center></td><td><center><b>Last modified</b></center></td></tr>");
        for (int i2 = 0; i2 < this._count; i2++) {
            CachedDocument doc = this._references.get(this._URIs[i2]);
            out.print("<tr><td><a href=\"" + this._URIs[i2] + "\"><font size=-1>" + this._URIs[i2] + "</font></a></td>");
            out.print("<td><center>" + doc.getLatency() + "ms</center></td>");
            out.print("<td><center>" + doc.getAccessCount() + "</center></td>");
            out.print("<td><center>" + ((Object) new Date(doc.getLastReferenced())) + "</center></td>");
            out.print("<td><center>" + ((Object) new Date(doc.getLastModified())) + "</center></td>");
            out.println("</tr>");
        }
        out.println("</table></center>");
    }
}
