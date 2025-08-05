package com.sun.org.apache.xpath.internal.functions;

import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.objects.XNumber;
import com.sun.org.apache.xpath.internal.objects.XObject;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/functions/FuncRound.class */
public class FuncRound extends FunctionOneArg {
    static final long serialVersionUID = -7970583902573826611L;

    @Override // com.sun.org.apache.xpath.internal.functions.Function, com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt) throws TransformerException {
        XObject obj = this.m_arg0.execute(xctxt);
        double val = obj.num();
        return (val < -0.5d || val >= 0.0d) ? val == 0.0d ? new XNumber(val) : new XNumber(Math.floor(val + 0.5d)) : new XNumber(-0.0d);
    }
}
