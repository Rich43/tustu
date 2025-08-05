package com.sun.org.apache.xpath.internal.jaxp;

import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
import com.sun.org.apache.xpath.internal.ExtensionsProvider;
import com.sun.org.apache.xpath.internal.functions.FuncExtFunction;
import com.sun.org.apache.xpath.internal.objects.XNodeSet;
import com.sun.org.apache.xpath.internal.objects.XObject;
import java.util.ArrayList;
import java.util.Vector;
import javax.xml.namespace.QName;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;
import javax.xml.xpath.XPathFunctionResolver;
import jdk.xml.internal.JdkXmlFeatures;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/jaxp/JAXPExtensionsProvider.class */
public class JAXPExtensionsProvider implements ExtensionsProvider {
    private final XPathFunctionResolver resolver;
    private boolean extensionInvocationDisabled;

    public JAXPExtensionsProvider(XPathFunctionResolver resolver) {
        this.extensionInvocationDisabled = false;
        this.resolver = resolver;
        this.extensionInvocationDisabled = false;
    }

    public JAXPExtensionsProvider(XPathFunctionResolver resolver, boolean featureSecureProcessing, JdkXmlFeatures featureManager) {
        this.extensionInvocationDisabled = false;
        this.resolver = resolver;
        if (featureSecureProcessing && !featureManager.getFeature(JdkXmlFeatures.XmlFeature.ENABLE_EXTENSION_FUNCTION)) {
            this.extensionInvocationDisabled = true;
        }
    }

    @Override // com.sun.org.apache.xpath.internal.ExtensionsProvider
    public boolean functionAvailable(String ns, String funcName) throws TransformerException {
        try {
            if (funcName == null) {
                String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[]{"Function Name"});
                throw new NullPointerException(fmsg);
            }
            QName myQName = new QName(ns, funcName);
            XPathFunction xpathFunction = this.resolver.resolveFunction(myQName, 0);
            if (xpathFunction == null) {
                return false;
            }
            return true;
        } catch (Exception e2) {
            return false;
        }
    }

    @Override // com.sun.org.apache.xpath.internal.ExtensionsProvider
    public boolean elementAvailable(String ns, String elemName) throws TransformerException {
        return false;
    }

    @Override // com.sun.org.apache.xpath.internal.ExtensionsProvider
    public Object extFunction(String ns, String funcName, Vector argVec, Object methodKey) throws TransformerException, XPathFunctionException {
        try {
            if (funcName == null) {
                String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[]{"Function Name"});
                throw new NullPointerException(fmsg);
            }
            QName myQName = new QName(ns, funcName);
            if (this.extensionInvocationDisabled) {
                String fmsg2 = XSLMessages.createXPATHMessage("ER_EXTENSION_FUNCTION_CANNOT_BE_INVOKED", new Object[]{myQName.toString()});
                throw new XPathFunctionException(fmsg2);
            }
            int arity = argVec.size();
            XPathFunction xpathFunction = this.resolver.resolveFunction(myQName, arity);
            ArrayList argList = new ArrayList(arity);
            for (int i2 = 0; i2 < arity; i2++) {
                Object argument = argVec.elementAt(i2);
                if (argument instanceof XNodeSet) {
                    argList.add(i2, ((XNodeSet) argument).nodelist());
                } else if (argument instanceof XObject) {
                    Object passedArgument = ((XObject) argument).object();
                    argList.add(i2, passedArgument);
                } else {
                    argList.add(i2, argument);
                }
            }
            return xpathFunction.evaluate(argList);
        } catch (XPathFunctionException xfe) {
            throw new WrappedRuntimeException(xfe);
        } catch (Exception e2) {
            throw new TransformerException(e2);
        }
    }

    @Override // com.sun.org.apache.xpath.internal.ExtensionsProvider
    public Object extFunction(FuncExtFunction extFunction, Vector argVec) throws TransformerException, XPathFunctionException {
        try {
            String namespace = extFunction.getNamespace();
            String functionName = extFunction.getFunctionName();
            int arity = extFunction.getArgCount();
            QName myQName = new QName(namespace, functionName);
            if (this.extensionInvocationDisabled) {
                String fmsg = XSLMessages.createXPATHMessage("ER_EXTENSION_FUNCTION_CANNOT_BE_INVOKED", new Object[]{myQName.toString()});
                throw new XPathFunctionException(fmsg);
            }
            XPathFunction xpathFunction = this.resolver.resolveFunction(myQName, arity);
            ArrayList argList = new ArrayList(arity);
            for (int i2 = 0; i2 < arity; i2++) {
                Object argument = argVec.elementAt(i2);
                if (argument instanceof XNodeSet) {
                    argList.add(i2, ((XNodeSet) argument).nodelist());
                } else if (argument instanceof XObject) {
                    Object passedArgument = ((XObject) argument).object();
                    argList.add(i2, passedArgument);
                } else {
                    argList.add(i2, argument);
                }
            }
            return xpathFunction.evaluate(argList);
        } catch (XPathFunctionException xfe) {
            throw new WrappedRuntimeException(xfe);
        } catch (Exception e2) {
            throw new TransformerException(e2);
        }
    }
}
