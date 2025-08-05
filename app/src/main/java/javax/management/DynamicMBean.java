package javax.management;

/* loaded from: rt.jar:javax/management/DynamicMBean.class */
public interface DynamicMBean {
    Object getAttribute(String str) throws MBeanException, AttributeNotFoundException, ReflectionException;

    void setAttribute(Attribute attribute) throws InvalidAttributeValueException, MBeanException, AttributeNotFoundException, ReflectionException;

    AttributeList getAttributes(String[] strArr);

    AttributeList setAttributes(AttributeList attributeList);

    Object invoke(String str, Object[] objArr, String[] strArr) throws MBeanException, ReflectionException;

    MBeanInfo getMBeanInfo();
}
