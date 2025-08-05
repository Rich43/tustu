package com.sun.jmx.mbeanserver;

import com.sun.jmx.defaults.JmxProperties;
import java.util.logging.Level;
import javafx.fxml.FXMLLoader;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.JMRuntimeException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.MBeanServerDelegate;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/MBeanServerDelegateImpl.class */
final class MBeanServerDelegateImpl extends MBeanServerDelegate implements DynamicMBean, MBeanRegistration {
    private static final String[] attributeNames = {"MBeanServerId", "SpecificationName", "SpecificationVersion", "SpecificationVendor", "ImplementationName", "ImplementationVersion", "ImplementationVendor"};
    private static final MBeanAttributeInfo[] attributeInfos = {new MBeanAttributeInfo("MBeanServerId", "java.lang.String", "The MBean server agent identification", true, false, false), new MBeanAttributeInfo("SpecificationName", "java.lang.String", "The full name of the JMX specification implemented by this product.", true, false, false), new MBeanAttributeInfo("SpecificationVersion", "java.lang.String", "The version of the JMX specification implemented by this product.", true, false, false), new MBeanAttributeInfo("SpecificationVendor", "java.lang.String", "The vendor of the JMX specification implemented by this product.", true, false, false), new MBeanAttributeInfo("ImplementationName", "java.lang.String", "The JMX implementation name (the name of this product)", true, false, false), new MBeanAttributeInfo("ImplementationVersion", "java.lang.String", "The JMX implementation version (the version of this product).", true, false, false), new MBeanAttributeInfo("ImplementationVendor", "java.lang.String", "the JMX implementation vendor (the vendor of this product).", true, false, false)};
    private final MBeanInfo delegateInfo = new MBeanInfo("javax.management.MBeanServerDelegate", "Represents  the MBean server from the management point of view.", attributeInfos, null, null, getNotificationInfo());

    @Override // javax.management.MBeanRegistration
    public final ObjectName preRegister(MBeanServer mBeanServer, ObjectName objectName) throws Exception {
        return objectName == null ? DELEGATE_NAME : objectName;
    }

    @Override // javax.management.MBeanRegistration
    public final void postRegister(Boolean bool) {
    }

    @Override // javax.management.MBeanRegistration
    public final void preDeregister() throws Exception {
        throw new IllegalArgumentException("The MBeanServerDelegate MBean cannot be unregistered");
    }

    @Override // javax.management.MBeanRegistration
    public final void postDeregister() {
    }

    @Override // javax.management.DynamicMBean
    public Object getAttribute(String str) throws MBeanException, AttributeNotFoundException, ReflectionException {
        try {
            if (str == null) {
                throw new AttributeNotFoundException(FXMLLoader.NULL_KEYWORD);
            }
            if (str.equals("MBeanServerId")) {
                return getMBeanServerId();
            }
            if (str.equals("SpecificationName")) {
                return getSpecificationName();
            }
            if (str.equals("SpecificationVersion")) {
                return getSpecificationVersion();
            }
            if (str.equals("SpecificationVendor")) {
                return getSpecificationVendor();
            }
            if (str.equals("ImplementationName")) {
                return getImplementationName();
            }
            if (str.equals("ImplementationVersion")) {
                return getImplementationVersion();
            }
            if (str.equals("ImplementationVendor")) {
                return getImplementationVendor();
            }
            throw new AttributeNotFoundException(FXMLLoader.NULL_KEYWORD);
        } catch (SecurityException e2) {
            throw e2;
        } catch (AttributeNotFoundException e3) {
            throw e3;
        } catch (JMRuntimeException e4) {
            throw e4;
        } catch (Exception e5) {
            throw new MBeanException(e5, "Failed to get " + str);
        }
    }

    @Override // javax.management.DynamicMBean
    public void setAttribute(Attribute attribute) throws MBeanException, InvalidAttributeValueException, AttributeNotFoundException, ReflectionException {
        String name = attribute == null ? null : attribute.getName();
        if (name == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), "Exception occurred trying to invoke the setter on the MBean");
        }
        getAttribute(name);
        throw new AttributeNotFoundException(name + " not accessible");
    }

    @Override // javax.management.DynamicMBean
    public AttributeList getAttributes(String[] strArr) {
        String[] strArr2 = strArr == null ? attributeNames : strArr;
        int length = strArr2.length;
        AttributeList attributeList = new AttributeList(length);
        for (int i2 = 0; i2 < length; i2++) {
            try {
                attributeList.add(new Attribute(strArr2[i2], getAttribute(strArr2[i2])));
            } catch (Exception e2) {
                if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINEST)) {
                    JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINEST, MBeanServerDelegateImpl.class.getName(), "getAttributes", "Attribute " + strArr2[i2] + " not found");
                }
            }
        }
        return attributeList;
    }

    @Override // javax.management.DynamicMBean
    public AttributeList setAttributes(AttributeList attributeList) {
        return new AttributeList(0);
    }

    @Override // javax.management.DynamicMBean
    public Object invoke(String str, Object[] objArr, String[] strArr) throws MBeanException, ReflectionException {
        if (str == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Operation name  cannot be null"), "Exception occurred trying to invoke the operation on the MBean");
        }
        throw new ReflectionException(new NoSuchMethodException(str), "The operation with name " + str + " could not be found");
    }

    @Override // javax.management.DynamicMBean
    public MBeanInfo getMBeanInfo() {
        return this.delegateInfo;
    }
}
