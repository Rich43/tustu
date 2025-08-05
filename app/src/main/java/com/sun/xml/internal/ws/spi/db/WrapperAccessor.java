package com.sun.xml.internal.ws.spi.db;

import java.util.Map;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/spi/db/WrapperAccessor.class */
public abstract class WrapperAccessor {
    protected Map<Object, PropertySetter> propertySetters;
    protected Map<Object, PropertyGetter> propertyGetters;
    protected boolean elementLocalNameCollision;

    protected PropertySetter getPropertySetter(QName name) {
        Object key = this.elementLocalNameCollision ? name : name.getLocalPart();
        return this.propertySetters.get(key);
    }

    protected PropertyGetter getPropertyGetter(QName name) {
        Object key = this.elementLocalNameCollision ? name : name.getLocalPart();
        return this.propertyGetters.get(key);
    }

    public PropertyAccessor getPropertyAccessor(String ns, String name) {
        QName n2 = new QName(ns, name);
        final PropertySetter setter = getPropertySetter(n2);
        final PropertyGetter getter = getPropertyGetter(n2);
        return new PropertyAccessor() { // from class: com.sun.xml.internal.ws.spi.db.WrapperAccessor.1
            @Override // com.sun.xml.internal.ws.spi.db.PropertyAccessor
            public Object get(Object bean) throws DatabindingException {
                return getter.get(bean);
            }

            @Override // com.sun.xml.internal.ws.spi.db.PropertyAccessor
            public void set(Object bean, Object value) throws DatabindingException {
                setter.set(bean, value);
            }
        };
    }
}
