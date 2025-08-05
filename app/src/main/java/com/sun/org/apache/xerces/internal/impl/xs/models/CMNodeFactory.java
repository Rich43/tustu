package com.sun.org.apache.xerces.internal.impl.xs.models;

import com.sun.org.apache.xerces.internal.impl.Constants;
import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import com.sun.org.apache.xerces.internal.impl.dtd.models.CMNode;
import com.sun.org.apache.xerces.internal.impl.xs.XSMessageFormatter;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/models/CMNodeFactory.class */
public class CMNodeFactory {
    private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
    private static final boolean DEBUG = false;
    private static final int MULTIPLICITY = 1;
    private int maxNodeLimit;
    private XMLErrorReporter fErrorReporter;
    private int nodeCount = 0;
    private XMLSecurityManager fSecurityManager = null;

    public void reset(XMLComponentManager componentManager) {
        this.fErrorReporter = (XMLErrorReporter) componentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
        try {
            this.fSecurityManager = (XMLSecurityManager) componentManager.getProperty("http://apache.org/xml/properties/security-manager");
            if (this.fSecurityManager != null) {
                this.maxNodeLimit = this.fSecurityManager.getLimit(XMLSecurityManager.Limit.MAX_OCCUR_NODE_LIMIT) * 1;
            }
        } catch (XMLConfigurationException e2) {
            this.fSecurityManager = null;
        }
    }

    public CMNode getCMLeafNode(int type, Object leaf, int id, int position) {
        return new XSCMLeaf(type, leaf, id, position);
    }

    public CMNode getCMRepeatingLeafNode(int type, Object leaf, int minOccurs, int maxOccurs, int id, int position) throws XNIException {
        nodeCountCheck();
        return new XSCMRepeatingLeaf(type, leaf, minOccurs, maxOccurs, id, position);
    }

    public CMNode getCMUniOpNode(int type, CMNode childNode) throws XNIException {
        nodeCountCheck();
        return new XSCMUniOp(type, childNode);
    }

    public CMNode getCMBinOpNode(int type, CMNode leftNode, CMNode rightNode) {
        return new XSCMBinOp(type, leftNode, rightNode);
    }

    public void nodeCountCheck() throws XNIException {
        if (this.fSecurityManager == null || this.fSecurityManager.isNoLimit(this.maxNodeLimit)) {
            return;
        }
        int i2 = this.nodeCount;
        this.nodeCount = i2 + 1;
        if (i2 > this.maxNodeLimit) {
            this.fErrorReporter.reportError(XSMessageFormatter.SCHEMA_DOMAIN, "MaxOccurLimit", new Object[]{new Integer(this.maxNodeLimit)}, (short) 2);
            this.nodeCount = 0;
        }
    }

    public void resetNodeCount() {
        this.nodeCount = 0;
    }

    public void setProperty(String propertyId, Object value) throws XMLConfigurationException {
        if (propertyId.startsWith(Constants.XERCES_PROPERTY_PREFIX)) {
            int suffixLength = propertyId.length() - Constants.XERCES_PROPERTY_PREFIX.length();
            if (suffixLength == Constants.SECURITY_MANAGER_PROPERTY.length() && propertyId.endsWith(Constants.SECURITY_MANAGER_PROPERTY)) {
                this.fSecurityManager = (XMLSecurityManager) value;
                this.maxNodeLimit = this.fSecurityManager != null ? this.fSecurityManager.getLimit(XMLSecurityManager.Limit.MAX_OCCUR_NODE_LIMIT) * 1 : 0;
            } else if (suffixLength == Constants.ERROR_REPORTER_PROPERTY.length() && propertyId.endsWith(Constants.ERROR_REPORTER_PROPERTY)) {
                this.fErrorReporter = (XMLErrorReporter) value;
            }
        }
    }
}
