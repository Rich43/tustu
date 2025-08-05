package javax.management;

import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:javax/management/StringValueExp.class */
public class StringValueExp implements ValueExp {
    private static final long serialVersionUID = -3256390509806284044L;
    private String val;

    public StringValueExp() {
    }

    public StringValueExp(String str) {
        this.val = str;
    }

    public String getValue() {
        return this.val;
    }

    public String toString() {
        return PdfOps.SINGLE_QUOTE_TOKEN + this.val.replace(PdfOps.SINGLE_QUOTE_TOKEN, "''") + PdfOps.SINGLE_QUOTE_TOKEN;
    }

    @Override // javax.management.ValueExp
    @Deprecated
    public void setMBeanServer(MBeanServer mBeanServer) {
    }

    @Override // javax.management.ValueExp
    public ValueExp apply(ObjectName objectName) throws InvalidApplicationException, BadBinaryOpValueExpException, BadAttributeValueExpException, BadStringOperationException {
        return this;
    }
}
