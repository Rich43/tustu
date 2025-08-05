package com.sun.org.apache.xpath.internal.jaxp;

import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xpath.internal.XPath;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.objects.XObject;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFunctionException;
import javax.xml.xpath.XPathFunctionResolver;
import javax.xml.xpath.XPathVariableResolver;
import jdk.xml.internal.JdkXmlFeatures;
import jdk.xml.internal.JdkXmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.InputSource;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/jaxp/XPathExpressionImpl.class */
public class XPathExpressionImpl implements XPathExpression {
    private XPathFunctionResolver functionResolver;
    private XPathVariableResolver variableResolver;
    private JAXPPrefixResolver prefixResolver;
    private XPath xpath;
    private boolean featureSecureProcessing;
    boolean overrideDefaultParser;
    private final JdkXmlFeatures featureManager;
    static DocumentBuilderFactory dbf = null;
    static DocumentBuilder db = null;

    /* renamed from: d, reason: collision with root package name */
    static Document f12011d = null;

    protected XPathExpressionImpl() {
        this(null, null, null, null, false, new JdkXmlFeatures(false));
    }

    protected XPathExpressionImpl(XPath xpath, JAXPPrefixResolver prefixResolver, XPathFunctionResolver functionResolver, XPathVariableResolver variableResolver) {
        this(xpath, prefixResolver, functionResolver, variableResolver, false, new JdkXmlFeatures(false));
    }

    protected XPathExpressionImpl(XPath xpath, JAXPPrefixResolver prefixResolver, XPathFunctionResolver functionResolver, XPathVariableResolver variableResolver, boolean featureSecureProcessing, JdkXmlFeatures featureManager) {
        this.featureSecureProcessing = false;
        this.xpath = xpath;
        this.prefixResolver = prefixResolver;
        this.functionResolver = functionResolver;
        this.variableResolver = variableResolver;
        this.featureSecureProcessing = featureSecureProcessing;
        this.featureManager = featureManager;
        this.overrideDefaultParser = featureManager.getFeature(JdkXmlFeatures.XmlFeature.JDK_OVERRIDE_PARSER);
    }

    public void setXPath(XPath xpath) {
        this.xpath = xpath;
    }

    public Object eval(Object item, QName returnType) throws TransformerException {
        XObject resultObject = eval(item);
        return getResultAsType(resultObject, returnType);
    }

    private XObject eval(Object contextItem) throws TransformerException {
        XPathContext xpathSupport;
        XObject xobj;
        if (this.functionResolver != null) {
            JAXPExtensionsProvider jep = new JAXPExtensionsProvider(this.functionResolver, this.featureSecureProcessing, this.featureManager);
            xpathSupport = new XPathContext(jep);
        } else {
            xpathSupport = new XPathContext();
        }
        xpathSupport.setVarStack(new JAXPVariableStack(this.variableResolver));
        Node contextNode = (Node) contextItem;
        if (contextNode == null) {
            xobj = this.xpath.execute(xpathSupport, -1, this.prefixResolver);
        } else {
            xobj = this.xpath.execute(xpathSupport, contextNode, this.prefixResolver);
        }
        return xobj;
    }

    @Override // javax.xml.xpath.XPathExpression
    public Object evaluate(Object item, QName returnType) throws XPathExpressionException {
        if (returnType == null) {
            String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[]{"returnType"});
            throw new NullPointerException(fmsg);
        }
        if (!isSupported(returnType)) {
            String fmsg2 = XSLMessages.createXPATHMessage("ER_UNSUPPORTED_RETURN_TYPE", new Object[]{returnType.toString()});
            throw new IllegalArgumentException(fmsg2);
        }
        try {
            return eval(item, returnType);
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

    @Override // javax.xml.xpath.XPathExpression
    public String evaluate(Object item) throws XPathExpressionException {
        return (String) evaluate(item, XPathConstants.STRING);
    }

    @Override // javax.xml.xpath.XPathExpression
    public Object evaluate(InputSource source, QName returnType) throws XPathExpressionException {
        if (source == null || returnType == null) {
            String fmsg = XSLMessages.createXPATHMessage("ER_SOURCE_RETURN_TYPE_CANNOT_BE_NULL", null);
            throw new NullPointerException(fmsg);
        }
        if (!isSupported(returnType)) {
            String fmsg2 = XSLMessages.createXPATHMessage("ER_UNSUPPORTED_RETURN_TYPE", new Object[]{returnType.toString()});
            throw new IllegalArgumentException(fmsg2);
        }
        try {
            if (dbf == null) {
                dbf = JdkXmlUtils.getDOMFactory(this.overrideDefaultParser);
            }
            db = dbf.newDocumentBuilder();
            Document document = db.parse(source);
            return eval(document, returnType);
        } catch (Exception e2) {
            throw new XPathExpressionException(e2);
        }
    }

    @Override // javax.xml.xpath.XPathExpression
    public String evaluate(InputSource source) throws XPathExpressionException {
        return (String) evaluate(source, XPathConstants.STRING);
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
}
