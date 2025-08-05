package javax.swing.text.html;

import java.awt.Color;
import java.awt.Component;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import javax.swing.JLabel;
import javax.swing.text.AttributeSet;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import sun.reflect.misc.MethodUtil;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:javax/swing/text/html/ObjectView.class */
public class ObjectView extends ComponentView {
    private boolean createComp;

    public ObjectView(Element element) {
        super(element);
        this.createComp = true;
    }

    ObjectView(Element element, boolean z2) {
        super(element);
        this.createComp = true;
        this.createComp = z2;
    }

    @Override // javax.swing.text.ComponentView
    protected Component createComponent() {
        if (!this.createComp) {
            return getUnloadableRepresentation();
        }
        AttributeSet attributes = getElement().getAttributes();
        String str = (String) attributes.getAttribute(HTML.Attribute.CLASSID);
        try {
            ReflectUtil.checkPackageAccess(str);
            Class<?> cls = Class.forName(str, false, Thread.currentThread().getContextClassLoader());
            if (Component.class.isAssignableFrom(cls)) {
                Object objNewInstance = cls.newInstance();
                if (objNewInstance instanceof Component) {
                    Component component = (Component) objNewInstance;
                    setParameters(component, attributes);
                    return component;
                }
            }
        } catch (Throwable th) {
        }
        return getUnloadableRepresentation();
    }

    Component getUnloadableRepresentation() {
        JLabel jLabel = new JLabel("??");
        jLabel.setForeground(Color.red);
        return jLabel;
    }

    private void setParameters(Component component, AttributeSet attributeSet) {
        try {
            PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(component.getClass()).getPropertyDescriptors();
            for (int i2 = 0; i2 < propertyDescriptors.length; i2++) {
                Object attribute = attributeSet.getAttribute(propertyDescriptors[i2].getName());
                if (attribute instanceof String) {
                    String str = (String) attribute;
                    Method writeMethod = propertyDescriptors[i2].getWriteMethod();
                    if (writeMethod == null || writeMethod.getParameterTypes().length != 1) {
                        return;
                    }
                    try {
                        MethodUtil.invoke(writeMethod, component, new Object[]{str});
                    } catch (Exception e2) {
                        System.err.println("Invocation failed");
                    }
                }
            }
        } catch (IntrospectionException e3) {
            System.err.println("introspector failed, ex: " + ((Object) e3));
        }
    }
}
