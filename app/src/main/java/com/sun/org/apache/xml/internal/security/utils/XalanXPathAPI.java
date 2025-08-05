package com.sun.org.apache.xml.internal.security.utils;

import com.sun.org.apache.xml.internal.security.transforms.implementations.FuncHere;
import com.sun.org.apache.xml.internal.utils.PrefixResolver;
import com.sun.org.apache.xml.internal.utils.PrefixResolverDefault;
import com.sun.org.apache.xpath.internal.Expression;
import com.sun.org.apache.xpath.internal.XPath;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.compiler.FunctionTable;
import com.sun.org.apache.xpath.internal.compiler.Keywords;
import com.sun.org.apache.xpath.internal.objects.XObject;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.lang.reflect.Method;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/XalanXPathAPI.class */
public class XalanXPathAPI implements XPathAPI {
    private static final Logger LOG = LoggerFactory.getLogger(XalanXPathAPI.class);
    private String xpathStr;
    private XPath xpath;
    private static FunctionTable funcTable;
    private static boolean installed;
    private XPathContext context;

    static {
        fixupFunctionTable();
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.XPathAPI
    public NodeList selectNodeList(Node node, Node node2, String str, Node node3) throws TransformerException {
        return eval(node, node2, str, node3).nodelist();
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.XPathAPI
    public boolean evaluate(Node node, Node node2, String str, Node node3) throws TransformerException {
        return eval(node, node2, str, node3).bool();
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.XPathAPI
    public void clear() {
        this.xpathStr = null;
        this.xpath = null;
        this.context = null;
    }

    public static synchronized boolean isInstalled() {
        return installed;
    }

    private XObject eval(Node node, Node node2, String str, Node node3) throws TransformerException {
        if (this.context == null) {
            this.context = new XPathContext(node2);
            this.context.setSecureProcessing(true);
        }
        PrefixResolverDefault prefixResolverDefault = new PrefixResolverDefault(node3.getNodeType() == 9 ? ((Document) node3).getDocumentElement() : node3);
        if (!str.equals(this.xpathStr)) {
            if (str.indexOf("here()") > 0) {
                this.context.reset();
            }
            this.xpath = createXPath(str, prefixResolverDefault);
            this.xpathStr = str;
        }
        return this.xpath.execute(this.context, this.context.getDTMHandleFromNode(node), prefixResolverDefault);
    }

    private XPath createXPath(String str, PrefixResolver prefixResolver) throws TransformerException {
        XPath xPath = null;
        try {
            xPath = (XPath) XPath.class.getConstructor(String.class, SourceLocator.class, PrefixResolver.class, Integer.TYPE, ErrorListener.class, FunctionTable.class).newInstance(str, null, prefixResolver, 0, null, funcTable);
        } catch (Exception e2) {
            LOG.debug(e2.getMessage(), e2);
        }
        if (xPath == null) {
            xPath = new XPath(str, null, prefixResolver, 0, null);
        }
        return xPath;
    }

    private static synchronized void fixupFunctionTable() {
        installed = false;
        if (new FunctionTable().functionAvailable(Keywords.FUNC_HERE_STRING)) {
            LOG.debug("Here function already registered");
            installed = true;
            return;
        }
        LOG.debug("Registering Here function");
        try {
            Method method = FunctionTable.class.getMethod("installFunction", String.class, Expression.class);
            if ((method.getModifiers() & 8) != 0) {
                method.invoke(null, Keywords.FUNC_HERE_STRING, new FuncHere());
                installed = true;
            }
        } catch (Exception e2) {
            LOG.debug("Error installing function using the static installFunction method", e2);
        }
        if (!installed) {
            try {
                funcTable = new FunctionTable();
                FunctionTable.class.getMethod("installFunction", String.class, Class.class).invoke(funcTable, Keywords.FUNC_HERE_STRING, FuncHere.class);
                installed = true;
            } catch (Exception e3) {
                LOG.debug("Error installing function using the static installFunction method", e3);
            }
        }
        if (installed) {
            LOG.debug("Registered class {} for XPath function 'here()' function in internal table", FuncHere.class.getName());
        } else {
            LOG.debug("Unable to register class {} for XPath function 'here()' function in internal table", FuncHere.class.getName());
        }
    }
}
