package javax.management;

import java.io.Serializable;

/* loaded from: rt.jar:javax/management/QueryExp.class */
public interface QueryExp extends Serializable {
    boolean apply(ObjectName objectName) throws InvalidApplicationException, BadBinaryOpValueExpException, BadAttributeValueExpException, BadStringOperationException;

    void setMBeanServer(MBeanServer mBeanServer);
}
