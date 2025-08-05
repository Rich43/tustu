package com.sun.org.apache.xpath.internal.functions;

import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.objects.XObject;
import com.sun.org.apache.xpath.internal.objects.XString;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Properties;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/functions/FuncSystemProperty.class */
public class FuncSystemProperty extends FunctionOneArg {
    static final long serialVersionUID = 3694874980992204867L;
    static final String XSLT_PROPERTIES = "com/sun/org/apache/xalan/internal/res/XSLTInfo.properties";

    @Override // com.sun.org.apache.xpath.internal.functions.Function, com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt) throws TransformerException {
        String result;
        String fullName = this.m_arg0.execute(xctxt).str();
        int indexOfNSSep = fullName.indexOf(58);
        String propName = "";
        Properties xsltInfo = new Properties();
        loadPropertyFile(xsltInfo);
        if (indexOfNSSep > 0) {
            String prefix = indexOfNSSep >= 0 ? fullName.substring(0, indexOfNSSep) : "";
            String namespace = xctxt.getNamespaceContext().getNamespaceForPrefix(prefix);
            propName = indexOfNSSep < 0 ? fullName : fullName.substring(indexOfNSSep + 1);
            if (namespace.startsWith("http://www.w3.org/XSL/Transform") || namespace.equals("http://www.w3.org/1999/XSL/Transform")) {
                result = xsltInfo.getProperty(propName);
                if (null == result) {
                    warn(xctxt, "WG_PROPERTY_NOT_SUPPORTED", new Object[]{fullName});
                    return XString.EMPTYSTRING;
                }
            } else {
                warn(xctxt, "WG_DONT_DO_ANYTHING_WITH_NS", new Object[]{namespace, fullName});
                try {
                    result = SecuritySupport.getSystemProperty(propName);
                    if (null == result) {
                        return XString.EMPTYSTRING;
                    }
                } catch (SecurityException e2) {
                    warn(xctxt, "WG_SECURITY_EXCEPTION", new Object[]{fullName});
                    return XString.EMPTYSTRING;
                }
            }
        } else {
            try {
                result = SecuritySupport.getSystemProperty(fullName);
                if (null == result) {
                    return XString.EMPTYSTRING;
                }
            } catch (SecurityException e3) {
                warn(xctxt, "WG_SECURITY_EXCEPTION", new Object[]{fullName});
                return XString.EMPTYSTRING;
            }
        }
        if (propName.equals("version") && result.length() > 0) {
            try {
                return new XString("1.0");
            } catch (Exception e4) {
                return new XString(result);
            }
        }
        return new XString(result);
    }

    private void loadPropertyFile(Properties target) {
        try {
            InputStream is = SecuritySupport.getResourceAsStream(XSLT_PROPERTIES);
            BufferedInputStream bis = new BufferedInputStream(is);
            Throwable th = null;
            try {
                try {
                    target.load(bis);
                    if (bis != null) {
                        if (0 != 0) {
                            try {
                                bis.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        } else {
                            bis.close();
                        }
                    }
                } finally {
                }
            } finally {
            }
        } catch (Exception ex) {
            throw new WrappedRuntimeException(ex);
        }
    }
}
