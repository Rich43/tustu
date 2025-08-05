package com.sun.org.apache.xalan.internal.xsltc.trax;

import com.sun.org.apache.xalan.internal.utils.ConfigurationError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.CompilerException;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Parser;
import com.sun.org.apache.xalan.internal.xsltc.compiler.SourceLoader;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Stylesheet;
import com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode;
import com.sun.org.apache.xalan.internal.xsltc.compiler.XSLTC;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import java.util.ArrayList;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.TemplatesHandler;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/trax/TemplatesHandlerImpl.class */
public class TemplatesHandlerImpl implements ContentHandler, TemplatesHandler, SourceLoader {
    private String _systemId;
    private int _indentNumber;
    private TransformerFactoryImpl _tfactory;
    private Parser _parser;
    private URIResolver _uriResolver = null;
    private TemplatesImpl _templates = null;

    protected TemplatesHandlerImpl(int indentNumber, TransformerFactoryImpl tfactory) {
        this._tfactory = null;
        this._parser = null;
        this._indentNumber = indentNumber;
        this._tfactory = tfactory;
        XSLTC xsltc = new XSLTC(tfactory.getJdkXmlFeatures());
        if (tfactory.getFeature("http://javax.xml.XMLConstants/feature/secure-processing")) {
            xsltc.setSecureProcessing(true);
        }
        xsltc.setProperty(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, (String) tfactory.getAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET));
        xsltc.setProperty("http://javax.xml.XMLConstants/property/accessExternalDTD", (String) tfactory.getAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD"));
        xsltc.setProperty("http://apache.org/xml/properties/security-manager", tfactory.getAttribute("http://apache.org/xml/properties/security-manager"));
        if ("true".equals(tfactory.getAttribute(TransformerFactoryImpl.ENABLE_INLINING))) {
            xsltc.setTemplateInlining(true);
        } else {
            xsltc.setTemplateInlining(false);
        }
        this._parser = xsltc.getParser();
    }

    @Override // javax.xml.transform.sax.TemplatesHandler
    public String getSystemId() {
        return this._systemId;
    }

    @Override // javax.xml.transform.sax.TemplatesHandler
    public void setSystemId(String id) {
        this._systemId = id;
    }

    public void setURIResolver(URIResolver resolver) {
        this._uriResolver = resolver;
    }

    @Override // javax.xml.transform.sax.TemplatesHandler
    public Templates getTemplates() {
        return this._templates;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SourceLoader
    public InputSource loadSource(String href, String context, XSLTC xsltc) {
        try {
            Source source = this._uriResolver.resolve(href, context);
            if (source != null) {
                return Util.getInputSource(xsltc, source);
            }
            return null;
        } catch (TransformerException e2) {
            return null;
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void startDocument() {
        XSLTC xsltc = this._parser.getXSLTC();
        xsltc.init();
        xsltc.setOutputType(2);
        this._parser.startDocument();
    }

    @Override // org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        String transletName;
        this._parser.endDocument();
        try {
            XSLTC xsltc = this._parser.getXSLTC();
            if (this._systemId != null) {
                transletName = Util.baseName(this._systemId);
            } else {
                transletName = (String) this._tfactory.getAttribute(TransformerFactoryImpl.TRANSLET_NAME);
            }
            xsltc.setClassName(transletName);
            String transletName2 = xsltc.getClassName();
            Stylesheet stylesheet = null;
            SyntaxTreeNode root = this._parser.getDocumentRoot();
            if (!this._parser.errorsFound() && root != null) {
                stylesheet = this._parser.makeStylesheet(root);
                stylesheet.setSystemId(this._systemId);
                stylesheet.setParentStylesheet(null);
                if (xsltc.getTemplateInlining()) {
                    stylesheet.setTemplateInlining(true);
                } else {
                    stylesheet.setTemplateInlining(false);
                }
                if (this._uriResolver != null) {
                    stylesheet.setSourceLoader(this);
                }
                this._parser.setCurrentStylesheet(stylesheet);
                xsltc.setStylesheet(stylesheet);
                this._parser.createAST(stylesheet);
            }
            if (!this._parser.errorsFound() && stylesheet != null) {
                stylesheet.setMultiDocument(xsltc.isMultiDocument());
                stylesheet.setHasIdCall(xsltc.hasIdCall());
                synchronized (xsltc.getClass()) {
                    stylesheet.translate();
                }
            }
            if (!this._parser.errorsFound()) {
                byte[][] bytecodes = xsltc.getBytecodes();
                if (bytecodes != null) {
                    this._templates = new TemplatesImpl(xsltc.getBytecodes(), transletName2, this._parser.getOutputProperties(), this._indentNumber, this._tfactory);
                    if (this._uriResolver != null) {
                        this._templates.setURIResolver(this._uriResolver);
                    }
                }
                return;
            }
            StringBuilder errorMessage = new StringBuilder();
            ArrayList<ErrorMsg> errors = this._parser.getErrors();
            int count = errors.size();
            for (int i2 = 0; i2 < count; i2++) {
                if (errorMessage.length() > 0) {
                    errorMessage.append('\n');
                }
                errorMessage.append(errors.get(i2).toString());
            }
            throw new SAXException(ErrorMsg.JAXP_COMPILE_ERR, new TransformerException(errorMessage.toString()));
        } catch (CompilerException e2) {
            throw new SAXException(ErrorMsg.JAXP_COMPILE_ERR, e2);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) {
        this._parser.startPrefixMapping(prefix, uri);
    }

    @Override // org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) {
        this._parser.endPrefixMapping(prefix);
    }

    @Override // org.xml.sax.ContentHandler
    public void startElement(String uri, String localname, String qname, Attributes attributes) throws SAXException, ConfigurationError {
        this._parser.startElement(uri, localname, qname, attributes);
    }

    @Override // org.xml.sax.ContentHandler
    public void endElement(String uri, String localname, String qname) {
        this._parser.endElement(uri, localname, qname);
    }

    @Override // org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) {
        this._parser.characters(ch, start, length);
    }

    @Override // org.xml.sax.ContentHandler
    public void processingInstruction(String name, String value) {
        this._parser.processingInstruction(name, value);
    }

    @Override // org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) {
        this._parser.ignorableWhitespace(ch, start, length);
    }

    @Override // org.xml.sax.ContentHandler
    public void skippedEntity(String name) {
        this._parser.skippedEntity(name);
    }

    @Override // org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
        setSystemId(locator.getSystemId());
        this._parser.setDocumentLocator(locator);
    }
}
