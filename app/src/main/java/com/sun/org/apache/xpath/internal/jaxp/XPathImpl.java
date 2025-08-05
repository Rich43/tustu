package com.sun.org.apache.xpath.internal.jaxp;

import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.objects.XObject;
import java.io.IOException;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFunctionException;
import javax.xml.xpath.XPathFunctionResolver;
import javax.xml.xpath.XPathVariableResolver;
import jdk.xml.internal.JdkXmlFeatures;
import jdk.xml.internal.JdkXmlUtils;
import jdk.xml.internal.XMLSecurityManager;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/jaxp/XPathImpl.class */
public class XPathImpl implements XPath {
    private XPathVariableResolver variableResolver;
    private XPathFunctionResolver functionResolver;
    private XPathVariableResolver origVariableResolver;
    private XPathFunctionResolver origFunctionResolver;
    private NamespaceContext namespaceContext;
    private JAXPPrefixResolver prefixResolver;
    private boolean featureSecureProcessing;
    private boolean overrideDefaultParser;
    private final JdkXmlFeatures featureManager;
    XMLSecurityManager xmlSecMgr;

    /* renamed from: d, reason: collision with root package name */
    private static Document f12012d = null;

    XPathImpl(XPathVariableResolver vr, XPathFunctionResolver fr) {
        this(vr, fr, false, new JdkXmlFeatures(false), new XMLSecurityManager(true));
    }

    XPathImpl(XPathVariableResolver vr, XPathFunctionResolver fr, boolean featureSecureProcessing, JdkXmlFeatures featureManager, XMLSecurityManager xmlSecMgr) {
        this.namespaceContext = null;
        this.featureSecureProcessing = false;
        this.overrideDefaultParser = true;
        this.variableResolver = vr;
        this.origVariableResolver = vr;
        this.functionResolver = fr;
        this.origFunctionResolver = fr;
        this.featureSecureProcessing = featureSecureProcessing;
        this.featureManager = featureManager;
        this.overrideDefaultParser = featureManager.getFeature(JdkXmlFeatures.XmlFeature.JDK_OVERRIDE_PARSER);
        this.xmlSecMgr = xmlSecMgr;
    }

    @Override // javax.xml.xpath.XPath
    public void setXPathVariableResolver(XPathVariableResolver resolver) {
        if (resolver == null) {
            String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[]{"XPathVariableResolver"});
            throw new NullPointerException(fmsg);
        }
        this.variableResolver = resolver;
    }

    @Override // javax.xml.xpath.XPath
    public XPathVariableResolver getXPathVariableResolver() {
        return this.variableResolver;
    }

    @Override // javax.xml.xpath.XPath
    public void setXPathFunctionResolver(XPathFunctionResolver resolver) {
        if (resolver == null) {
            String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[]{"XPathFunctionResolver"});
            throw new NullPointerException(fmsg);
        }
        this.functionResolver = resolver;
    }

    @Override // javax.xml.xpath.XPath
    public XPathFunctionResolver getXPathFunctionResolver() {
        return this.functionResolver;
    }

    @Override // javax.xml.xpath.XPath
    public void setNamespaceContext(NamespaceContext nsContext) {
        if (nsContext == null) {
            String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[]{"NamespaceContext"});
            throw new NullPointerException(fmsg);
        }
        this.namespaceContext = nsContext;
        this.prefixResolver = new JAXPPrefixResolver(nsContext);
    }

    @Override // javax.xml.xpath.XPath
    public NamespaceContext getNamespaceContext() {
        return this.namespaceContext;
    }

    private DocumentBuilder getParser() {
        try {
            DocumentBuilderFactory dbf = JdkXmlUtils.getDOMFactory(this.overrideDefaultParser);
            return dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e2) {
            throw new Error(e2);
        }
    }

    private XObject eval(String expression, Object contextItem) throws TransformerException {
        XPathContext xpathSupport;
        XObject xobj;
        com.sun.org.apache.xpath.internal.XPath xpath = new com.sun.org.apache.xpath.internal.XPath(expression, null, this.prefixResolver, 0, null, null, this.xmlSecMgr);
        if (this.functionResolver != null) {
            JAXPExtensionsProvider jep = new JAXPExtensionsProvider(this.functionResolver, this.featureSecureProcessing, this.featureManager);
            xpathSupport = new XPathContext(jep);
        } else {
            xpathSupport = new XPathContext();
        }
        xpathSupport.setVarStack(new JAXPVariableStack(this.variableResolver));
        if (contextItem instanceof Node) {
            xobj = xpath.execute(xpathSupport, (Node) contextItem, this.prefixResolver);
        } else {
            xobj = xpath.execute(xpathSupport, -1, this.prefixResolver);
        }
        return xobj;
    }

    @Override // javax.xml.xpath.XPath
    public Object evaluate(String expression, Object item, QName returnType) throws XPathExpressionException {
        if (expression == null) {
            String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[]{"XPath expression"});
            throw new NullPointerException(fmsg);
        }
        if (returnType == null) {
            String fmsg2 = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[]{"returnType"});
            throw new NullPointerException(fmsg2);
        }
        if (!isSupported(returnType)) {
            String fmsg3 = XSLMessages.createXPATHMessage("ER_UNSUPPORTED_RETURN_TYPE", new Object[]{returnType.toString()});
            throw new IllegalArgumentException(fmsg3);
        }
        try {
            XObject resultObject = eval(expression, item);
            return getResultAsType(resultObject, returnType);
        } catch (NullPointerException npe) {
            throw new XPathExpressionException(npe);
        } catch (TransformerException te) {
            Throwable nestedException = te.getException();
            if (nestedException instanceof XPathFunctionException) {
                throw ((XPathFunctionException) nestedException);
            }
            throw new XPathExpressionException(te);
        }
    }

    private boolean isSupported(QName returnType) {
        if (returnType.equals(XPathConstants.STRING) || returnType.equals(XPathConstants.NUMBER) || returnType.equals(XPathConstants.BOOLEAN) || returnType.equals(XPathConstants.NODE) || returnType.equals(XPathConstants.NODESET)) {
            return true;
        }
        return false;
    }

    private Object getResultAsType(XObject resultObject, QName returnType) throws TransformerException {
        if (returnType.equals(XPathConstants.STRING)) {
            return resultObject.str();
        }
        if (returnType.equals(XPathConstants.NUMBER)) {
            return new Double(resultObject.num());
        }
        if (returnType.equals(XPathConstants.BOOLEAN)) {
            return new Boolean(resultObject.bool());
        }
        if (returnType.equals(XPathConstants.NODESET)) {
            return resultObject.nodelist();
        }
        if (returnType.equals(XPathConstants.NODE)) {
            NodeIterator ni = resultObject.nodeset();
            return ni.nextNode();
        }
        String fmsg = XSLMessages.createXPATHMessage("ER_UNSUPPORTED_RETURN_TYPE", new Object[]{returnType.toString()});
        throw new IllegalArgumentException(fmsg);
    }

    @Override // javax.xml.xpath.XPath
    public String evaluate(String expression, Object item) throws XPathExpressionException {
        return (String) evaluate(expression, item, XPathConstants.STRING);
    }

    @Override // javax.xml.xpath.XPath
    public XPathExpression compile(String expression) throws XPathExpressionException {
        if (expression == null) {
            String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[]{"XPath expression"});
            throw new NullPointerException(fmsg);
        }
        try {
            com.sun.org.apache.xpath.internal.XPath xpath = new com.sun.org.apache.xpath.internal.XPath(expression, null, this.prefixResolver, 0, null, null, this.xmlSecMgr);
            XPathExpressionImpl ximpl = new XPathExpressionImpl(xpath, this.prefixResolver, this.functionResolver, this.variableResolver, this.featureSecureProcessing, this.featureManager);
            return ximpl;
        } catch (TransformerException te) {
            throw new XPathExpressionException(te);
        }
    }

    @Override // javax.xml.xpath.XPath
    public Object evaluate(String expression, InputSource source, QName returnType) throws XPathExpressionException {
        if (source == null) {
            String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[]{"source"});
            throw new NullPointerException(fmsg);
        }
        if (expression == null) {
            String fmsg2 = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[]{"XPath expression"});
            throw new NullPointerException(fmsg2);
        }
        if (returnType == null) {
            String fmsg3 = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[]{"returnType"});
            throw new NullPointerException(fmsg3);
        }
        if (!isSupported(returnType)) {
            String fmsg4 = XSLMessages.createXPATHMessage("ER_UNSUPPORTED_RETURN_TYPE", new Object[]{returnType.toString()});
            throw new IllegalArgumentException(fmsg4);
        }
        try {
            Document document = getParser().parse(source);
            XObject resultObject = eval(expression, document);
            return getResultAsType(resultObject, returnType);
        } catch (IOException e2) {
            throw new XPathExpressionException(e2);
        } catch (TransformerException te) {
            Throwable nestedException = te.getException();
            if (nestedException instanceof XPathFunctionException) {
                throw ((XPathFunctionException) nestedException);
            }
            throw new XPathExpressionException(te);
        } catch (SAXException e3) {
            throw new XPathExpressionException(e3);
        }
    }

    @Override // javax.xml.xpath.XPath
    public String evaluate(String expression, InputSource source) throws XPathExpressionException {
        return (String) evaluate(expression, source, XPathConstants.STRING);
    }

    @Override // javax.xml.xpath.XPath
    public void reset() {
        this.variableResolver = this.origVariableResolver;
        this.functionResolver = this.origFunctionResolver;
        this.namespaceContext = null;
    }
}
