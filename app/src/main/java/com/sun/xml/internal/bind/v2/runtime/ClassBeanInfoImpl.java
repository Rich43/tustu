package com.sun.xml.internal.bind.v2.runtime;

import com.sun.istack.internal.FinalArrayList;
import com.sun.xml.internal.bind.Util;
import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.ClassFactory;
import com.sun.xml.internal.bind.v2.model.core.ID;
import com.sun.xml.internal.bind.v2.model.core.PropertyInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeClassInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfo;
import com.sun.xml.internal.bind.v2.runtime.property.AttributeProperty;
import com.sun.xml.internal.bind.v2.runtime.property.Property;
import com.sun.xml.internal.bind.v2.runtime.property.PropertyFactory;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.StructureLoader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiTypeLoader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.helpers.ValidationEventImpl;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.LocatorImpl;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/ClassBeanInfoImpl.class */
public final class ClassBeanInfoImpl<BeanT> extends JaxBeanInfo<BeanT> implements AttributeAccessor<BeanT> {
    public final Property<BeanT>[] properties;
    private Property<? super BeanT> idProperty;
    private Loader loader;
    private Loader loaderWithTypeSubst;
    private RuntimeClassInfo ci;
    private final Accessor<? super BeanT, Map<QName, String>> inheritedAttWildcard;
    private final Transducer<BeanT> xducer;
    public final ClassBeanInfoImpl<? super BeanT> superClazz;
    private final Accessor<? super BeanT, Locator> xmlLocatorField;
    private final Name tagName;
    private boolean retainPropertyInfo;
    private AttributeProperty<BeanT>[] attributeProperties;
    private Property<BeanT>[] uriProperties;
    private final Method factoryMethod;
    private static final AttributeProperty[] EMPTY_PROPERTIES = new AttributeProperty[0];
    private static final Logger logger = Util.getClassLogger();

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v2, types: [com.sun.xml.internal.bind.v2.model.runtime.RuntimeClassInfo] */
    ClassBeanInfoImpl(JAXBContextImpl jAXBContextImpl, RuntimeClassInfo runtimeClassInfo) {
        super(jAXBContextImpl, (RuntimeTypeInfo) runtimeClassInfo, runtimeClassInfo.getClazz(), runtimeClassInfo.getTypeName(), runtimeClassInfo.isElement(), false, true);
        this.retainPropertyInfo = false;
        this.ci = runtimeClassInfo;
        this.inheritedAttWildcard = runtimeClassInfo.getAttributeWildcard();
        this.xducer = runtimeClassInfo.getTransducer();
        this.factoryMethod = runtimeClassInfo.getFactoryMethod();
        this.retainPropertyInfo = jAXBContextImpl.retainPropertyInfo;
        if (this.factoryMethod != null && (!Modifier.isPublic(this.factoryMethod.getDeclaringClass().getModifiers()) || !Modifier.isPublic(this.factoryMethod.getModifiers()))) {
            try {
                this.factoryMethod.setAccessible(true);
            } catch (SecurityException e2) {
                logger.log(Level.FINE, "Unable to make the method of " + ((Object) this.factoryMethod) + " accessible", (Throwable) e2);
                throw e2;
            }
        }
        if (runtimeClassInfo.getBaseClass2() == null) {
            this.superClazz = null;
        } else {
            this.superClazz = jAXBContextImpl.getOrCreate((RuntimeClassInfo) runtimeClassInfo.getBaseClass2());
        }
        if (this.superClazz != null && this.superClazz.xmlLocatorField != null) {
            this.xmlLocatorField = this.superClazz.xmlLocatorField;
        } else {
            this.xmlLocatorField = runtimeClassInfo.getLocatorField();
        }
        List<? extends PropertyInfo<Type, Class>> properties = runtimeClassInfo.getProperties();
        this.properties = new Property[properties.size()];
        int i2 = 0;
        boolean zElementOnlyContent = true;
        Iterator<? extends PropertyInfo<Type, Class>> it = properties.iterator();
        while (it.hasNext()) {
            RuntimePropertyInfo runtimePropertyInfo = (RuntimePropertyInfo) it.next();
            Property<? super BeanT> propertyCreate = PropertyFactory.create(jAXBContextImpl, runtimePropertyInfo);
            if (runtimePropertyInfo.id() == ID.ID) {
                this.idProperty = propertyCreate;
            }
            int i3 = i2;
            i2++;
            this.properties[i3] = propertyCreate;
            zElementOnlyContent &= runtimePropertyInfo.elementOnlyContent();
            checkOverrideProperties(propertyCreate);
        }
        hasElementOnlyContentModel(zElementOnlyContent);
        if (runtimeClassInfo.isElement()) {
            this.tagName = jAXBContextImpl.nameBuilder.createElementName(runtimeClassInfo.getElementName());
        } else {
            this.tagName = null;
        }
        setLifecycleFlags();
    }

    private void checkOverrideProperties(Property p2) {
        Property[] props;
        String spName;
        ClassBeanInfoImpl bi2 = this;
        while (true) {
            ClassBeanInfoImpl classBeanInfoImpl = bi2.superClazz;
            bi2 = classBeanInfoImpl;
            if (classBeanInfoImpl == null || (props = bi2.properties) == null) {
                return;
            }
            for (Property superProperty : props) {
                if (superProperty != null && (spName = superProperty.getFieldName()) != null && spName.equals(p2.getFieldName())) {
                    superProperty.setHiddenByOverride(true);
                }
            }
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    protected void link(JAXBContextImpl jAXBContextImpl) {
        if (this.uriProperties != null) {
            return;
        }
        super.link(jAXBContextImpl);
        if (this.superClazz != null) {
            this.superClazz.link(jAXBContextImpl);
        }
        getLoader(jAXBContextImpl, true);
        if (this.superClazz != null) {
            if (this.idProperty == null) {
                this.idProperty = this.superClazz.idProperty;
            }
            if (!this.superClazz.hasElementOnlyContentModel()) {
                hasElementOnlyContentModel(false);
            }
        }
        FinalArrayList finalArrayList = new FinalArrayList();
        FinalArrayList finalArrayList2 = new FinalArrayList();
        ClassBeanInfoImpl classBeanInfoImpl = this;
        while (true) {
            ClassBeanInfoImpl classBeanInfoImpl2 = classBeanInfoImpl;
            if (classBeanInfoImpl2 == null) {
                break;
            }
            for (int i2 = 0; i2 < classBeanInfoImpl2.properties.length; i2++) {
                Property<BeanT> property = classBeanInfoImpl2.properties[i2];
                if (property instanceof AttributeProperty) {
                    finalArrayList.add((AttributeProperty) property);
                }
                if (property.hasSerializeURIAction()) {
                    finalArrayList2.add(property);
                }
            }
            classBeanInfoImpl = classBeanInfoImpl2.superClazz;
        }
        if (jAXBContextImpl.c14nSupport) {
            Collections.sort(finalArrayList);
        }
        if (finalArrayList.isEmpty()) {
            this.attributeProperties = EMPTY_PROPERTIES;
        } else {
            this.attributeProperties = (AttributeProperty[]) finalArrayList.toArray(new AttributeProperty[finalArrayList.size()]);
        }
        if (finalArrayList2.isEmpty()) {
            this.uriProperties = EMPTY_PROPERTIES;
        } else {
            this.uriProperties = (Property[]) finalArrayList2.toArray(new Property[finalArrayList2.size()]);
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public void wrapUp() {
        for (Property p2 : this.properties) {
            p2.wrapUp();
        }
        this.ci = null;
        super.wrapUp();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public String getElementNamespaceURI(BeanT bean) {
        return this.tagName.nsUri;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public String getElementLocalName(BeanT bean) {
        return this.tagName.localName;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public BeanT createInstance(UnmarshallingContext unmarshallingContext) throws IllegalAccessException, SAXException, InstantiationException, SecurityException, InvocationTargetException {
        Object objCreate0;
        if (this.factoryMethod == null) {
            objCreate0 = ClassFactory.create0(this.jaxbType);
        } else {
            Object objCreate = ClassFactory.create(this.factoryMethod);
            if (this.jaxbType.isInstance(objCreate)) {
                objCreate0 = objCreate;
            } else {
                throw new InstantiationException("The factory method didn't return a correct object");
            }
        }
        if (this.xmlLocatorField != null) {
            try {
                this.xmlLocatorField.set(objCreate0, new LocatorImpl(unmarshallingContext.getLocator()));
            } catch (AccessorException e2) {
                unmarshallingContext.handleError(e2);
            }
        }
        return (BeanT) objCreate0;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public boolean reset(BeanT bean, UnmarshallingContext context) throws SAXException {
        try {
            if (this.superClazz != null) {
                this.superClazz.reset(bean, context);
            }
            for (Property<BeanT> p2 : this.properties) {
                p2.reset(bean);
            }
            return true;
        } catch (AccessorException e2) {
            context.handleError(e2);
            return false;
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public String getId(BeanT bean, XMLSerializer target) throws SAXException {
        if (this.idProperty != null) {
            try {
                return this.idProperty.getIdValue(bean);
            } catch (AccessorException e2) {
                target.reportError(null, e2);
                return null;
            }
        }
        return null;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public void serializeRoot(BeanT bean, XMLSerializer target) throws SAXException, XMLStreamException, IOException {
        String message;
        if (this.tagName == null) {
            Class beanClass = bean.getClass();
            if (beanClass.isAnnotationPresent(XmlRootElement.class)) {
                message = Messages.UNABLE_TO_MARSHAL_UNBOUND_CLASS.format(beanClass.getName());
            } else {
                message = Messages.UNABLE_TO_MARSHAL_NON_ELEMENT.format(beanClass.getName());
            }
            target.reportError(new ValidationEventImpl(1, message, null, null));
            return;
        }
        target.startElement(this.tagName, bean);
        target.childAsSoleContent(bean, null);
        target.endElement();
        if (this.retainPropertyInfo) {
            target.currentProperty.remove();
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public void serializeBody(BeanT bean, XMLSerializer target) throws SAXException, XMLStreamException, IOException {
        if (this.superClazz != null) {
            this.superClazz.serializeBody(bean, target);
        }
        try {
            for (Property<BeanT> p2 : this.properties) {
                if (this.retainPropertyInfo) {
                    target.currentProperty.set(p2);
                }
                boolean isThereAnOverridingProperty = p2.isHiddenByOverride();
                if (!isThereAnOverridingProperty || bean.getClass().equals(this.jaxbType)) {
                    p2.serializeBody(bean, target, null);
                } else if (isThereAnOverridingProperty) {
                    Class beanClass = bean.getClass();
                    if (Utils.REFLECTION_NAVIGATOR.getDeclaredField(beanClass, p2.getFieldName()) == null) {
                        p2.serializeBody(bean, target, null);
                    }
                }
            }
        } catch (AccessorException e2) {
            target.reportError(null, e2);
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public void serializeAttributes(BeanT bean, XMLSerializer target) throws SAXException, XMLStreamException, IOException {
        for (AttributeProperty<BeanT> p2 : this.attributeProperties) {
            try {
                if (this.retainPropertyInfo) {
                    Property parentProperty = target.getCurrentProperty();
                    target.currentProperty.set(p2);
                    p2.serializeAttributes(bean, target);
                    target.currentProperty.set(parentProperty);
                } else {
                    p2.serializeAttributes(bean, target);
                }
                if (p2.attName.equals("http://www.w3.org/2001/XMLSchema-instance", "nil")) {
                    this.isNilIncluded = true;
                }
            } catch (AccessorException e2) {
                target.reportError(null, e2);
            }
        }
        try {
            if (this.inheritedAttWildcard != null) {
                Map<QName, String> map = this.inheritedAttWildcard.get(bean);
                target.attWildcardAsAttributes(map, null);
            }
        } catch (AccessorException e3) {
            target.reportError(null, e3);
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public void serializeURIs(BeanT bean, XMLSerializer target) throws SAXException {
        try {
            if (this.retainPropertyInfo) {
                Property parentProperty = target.getCurrentProperty();
                for (Property<BeanT> p2 : this.uriProperties) {
                    target.currentProperty.set(p2);
                    p2.serializeURIs(bean, target);
                }
                target.currentProperty.set(parentProperty);
            } else {
                for (Property<BeanT> property : this.uriProperties) {
                    property.serializeURIs(bean, target);
                }
            }
            if (this.inheritedAttWildcard != null) {
                Map<QName, String> map = this.inheritedAttWildcard.get(bean);
                target.attWildcardAsURIs(map, null);
            }
        } catch (AccessorException e2) {
            target.reportError(null, e2);
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public Loader getLoader(JAXBContextImpl context, boolean typeSubstitutionCapable) {
        if (this.loader == null) {
            StructureLoader sl = new StructureLoader(this);
            this.loader = sl;
            if (this.ci.hasSubClasses()) {
                this.loaderWithTypeSubst = new XsiTypeLoader(this);
            } else {
                this.loaderWithTypeSubst = this.loader;
            }
            sl.init(context, this, this.ci.getAttributeWildcard());
        }
        if (typeSubstitutionCapable) {
            return this.loaderWithTypeSubst;
        }
        return this.loader;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public Transducer<BeanT> getTransducer() {
        return this.xducer;
    }
}
