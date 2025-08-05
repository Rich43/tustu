package com.sun.org.apache.xpath.internal.jaxp;

import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xml.internal.utils.QName;
import com.sun.org.apache.xpath.internal.VariableStack;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.objects.XObject;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathVariableResolver;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/jaxp/JAXPVariableStack.class */
public class JAXPVariableStack extends VariableStack {
    private final XPathVariableResolver resolver;

    public JAXPVariableStack(XPathVariableResolver resolver) {
        this.resolver = resolver;
    }

    @Override // com.sun.org.apache.xpath.internal.VariableStack
    public XObject getVariableOrParam(XPathContext xctxt, QName qname) throws TransformerException, IllegalArgumentException {
        if (qname == null) {
            String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[]{"Variable qname"});
            throw new IllegalArgumentException(fmsg);
        }
        javax.xml.namespace.QName name = new javax.xml.namespace.QName(qname.getNamespace(), qname.getLocalPart());
        Object varValue = this.resolver.resolveVariable(name);
        if (varValue == null) {
            String fmsg2 = XSLMessages.createXPATHMessage("ER_RESOLVE_VARIABLE_RETURNS_NULL", new Object[]{name.toString()});
            throw new TransformerException(fmsg2);
        }
        return XObject.create(varValue, xctxt);
    }
}
