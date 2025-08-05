package com.sun.org.apache.xpath.internal.jaxp;

import com.sun.org.apache.xalan.internal.res.XSLMessages;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;
import javax.xml.xpath.XPathFunctionResolver;
import javax.xml.xpath.XPathVariableResolver;
import jdk.xml.internal.JdkXmlFeatures;
import jdk.xml.internal.XMLSecurityManager;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/jaxp/XPathFactoryImpl.class */
public class XPathFactoryImpl extends XPathFactory {
    private static final String CLASS_NAME = "XPathFactoryImpl";
    private XPathFunctionResolver xPathFunctionResolver = null;
    private XPathVariableResolver xPathVariableResolver = null;
    private boolean _isNotSecureProcessing;
    private boolean _isSecureMode;
    private final JdkXmlFeatures _featureManager;
    private XMLSecurityManager _xmlSecMgr;

    public XPathFactoryImpl() {
        this._isNotSecureProcessing = true;
        this._isSecureMode = false;
        if (System.getSecurityManager() != null) {
            this._isSecureMode = true;
            this._isNotSecureProcessing = false;
        }
        this._featureManager = new JdkXmlFeatures(!this._isNotSecureProcessing);
        this._xmlSecMgr = new XMLSecurityManager(true);
    }

    @Override // javax.xml.xpath.XPathFactory
    public boolean isObjectModelSupported(String objectModel) {
        if (objectModel == null) {
            String fmsg = XSLMessages.createXPATHMessage("ER_OBJECT_MODEL_NULL", new Object[]{getClass().getName()});
            throw new NullPointerException(fmsg);
        }
        if (objectModel.length() == 0) {
            String fmsg2 = XSLMessages.createXPATHMessage("ER_OBJECT_MODEL_EMPTY", new Object[]{getClass().getName()});
            throw new IllegalArgumentException(fmsg2);
        }
        if (objectModel.equals("http://java.sun.com/jaxp/xpath/dom")) {
            return true;
        }
        return false;
    }

    @Override // javax.xml.xpath.XPathFactory
    public XPath newXPath() {
        return new XPathImpl(this.xPathVariableResolver, this.xPathFunctionResolver, !this._isNotSecureProcessing, this._featureManager, this._xmlSecMgr);
    }

    @Override // javax.xml.xpath.XPathFactory
    public void setFeature(String name, boolean value) throws XPathFactoryConfigurationException {
        if (name == null) {
            String fmsg = XSLMessages.createXPATHMessage("ER_FEATURE_NAME_NULL", new Object[]{CLASS_NAME, new Boolean(value)});
            throw new NullPointerException(fmsg);
        }
        if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
            if (this._isSecureMode && !value) {
                String fmsg2 = XSLMessages.createXPATHMessage("ER_SECUREPROCESSING_FEATURE", new Object[]{name, CLASS_NAME, new Boolean(value)});
                throw new XPathFactoryConfigurationException(fmsg2);
            }
            this._isNotSecureProcessing = !value;
            if (value && this._featureManager != null) {
                this._featureManager.setFeature(JdkXmlFeatures.XmlFeature.ENABLE_EXTENSION_FUNCTION, JdkXmlFeatures.State.FSP, false);
                return;
            }
            return;
        }
        if (name.equals("http://www.oracle.com/feature/use-service-mechanism") && this._isSecureMode) {
            return;
        }
        if (this._featureManager != null && this._featureManager.setFeature(name, JdkXmlFeatures.State.APIPROPERTY, Boolean.valueOf(value))) {
            return;
        }
        String fmsg3 = XSLMessages.createXPATHMessage("ER_FEATURE_UNKNOWN", new Object[]{name, CLASS_NAME, Boolean.valueOf(value)});
        throw new XPathFactoryConfigurationException(fmsg3);
    }

    @Override // javax.xml.xpath.XPathFactory
    public boolean getFeature(String name) throws XPathFactoryConfigurationException {
        if (name == null) {
            String fmsg = XSLMessages.createXPATHMessage("ER_GETTING_NULL_FEATURE", new Object[]{CLASS_NAME});
            throw new NullPointerException(fmsg);
        }
        if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
            return !this._isNotSecureProcessing;
        }
        int index = this._featureManager.getIndex(name);
        if (index > -1) {
            return this._featureManager.getFeature(index);
        }
        String fmsg2 = XSLMessages.createXPATHMessage("ER_GETTING_UNKNOWN_FEATURE", new Object[]{name, CLASS_NAME});
        throw new XPathFactoryConfigurationException(fmsg2);
    }

    @Override // javax.xml.xpath.XPathFactory
    public void setXPathFunctionResolver(XPathFunctionResolver resolver) {
        if (resolver == null) {
            String fmsg = XSLMessages.createXPATHMessage("ER_NULL_XPATH_FUNCTION_RESOLVER", new Object[]{CLASS_NAME});
            throw new NullPointerException(fmsg);
        }
        this.xPathFunctionResolver = resolver;
    }

    @Override // javax.xml.xpath.XPathFactory
    public void setXPathVariableResolver(XPathVariableResolver resolver) {
        if (resolver == null) {
            String fmsg = XSLMessages.createXPATHMessage("ER_NULL_XPATH_VARIABLE_RESOLVER", new Object[]{CLASS_NAME});
            throw new NullPointerException(fmsg);
        }
        this.xPathVariableResolver = resolver;
    }
}
