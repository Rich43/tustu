package com.sun.org.apache.xpath.internal.functions;

import com.sun.org.apache.xpath.internal.ExtensionsProvider;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.compiler.FunctionTable;
import com.sun.org.apache.xpath.internal.objects.XBoolean;
import com.sun.org.apache.xpath.internal.objects.XObject;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/functions/FuncExtFunctionAvailable.class */
public class FuncExtFunctionAvailable extends FunctionOneArg {
    static final long serialVersionUID = 5118814314918592241L;
    private transient FunctionTable m_functionTable = null;

    @Override // com.sun.org.apache.xpath.internal.functions.Function, com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt) throws TransformerException {
        String namespace;
        String methName;
        String fullName = this.m_arg0.execute(xctxt).str();
        int indexOfNSSep = fullName.indexOf(58);
        if (indexOfNSSep < 0) {
            namespace = "http://www.w3.org/1999/XSL/Transform";
            methName = fullName;
        } else {
            String prefix = fullName.substring(0, indexOfNSSep);
            namespace = xctxt.getNamespaceContext().getNamespaceForPrefix(prefix);
            if (null == namespace) {
                return XBoolean.S_FALSE;
            }
            methName = fullName.substring(indexOfNSSep + 1);
        }
        if (namespace.equals("http://www.w3.org/1999/XSL/Transform")) {
            try {
                if (null == this.m_functionTable) {
                    this.m_functionTable = new FunctionTable();
                }
                return this.m_functionTable.functionAvailable(methName) ? XBoolean.S_TRUE : XBoolean.S_FALSE;
            } catch (Exception e2) {
                return XBoolean.S_FALSE;
            }
        }
        ExtensionsProvider extProvider = (ExtensionsProvider) xctxt.getOwnerObject();
        return extProvider.functionAvailable(namespace, methName) ? XBoolean.S_TRUE : XBoolean.S_FALSE;
    }

    public void setFunctionTable(FunctionTable aTable) {
        this.m_functionTable = aTable;
    }
}
