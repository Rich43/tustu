package javax.management;

import java.io.Serializable;

/* loaded from: rt.jar:javax/management/ValueExp.class */
public interface ValueExp extends Serializable {
    ValueExp apply(ObjectName objectName) throws InvalidApplicationException, BadBinaryOpValueExpException, BadAttributeValueExpException, BadStringOperationException;

    @Deprecated
    void setMBeanServer(MBeanServer mBeanServer);
}
