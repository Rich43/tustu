package com.sun.xml.internal.bind.v2.runtime.property;

import com.sun.xml.internal.bind.v2.model.core.ID;
import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
import com.sun.xml.internal.bind.v2.model.core.TypeInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeAttributePropertyInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeElementPropertyInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeValuePropertyInfo;
import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Collection;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/property/PropertyFactory.class */
public abstract class PropertyFactory {
    private static final Constructor<? extends Property>[] propImpls;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* JADX WARN: Multi-variable type inference failed */
    static {
        $assertionsDisabled = !PropertyFactory.class.desiredAssertionStatus();
        Class<? extends Property>[] implClasses = {SingleElementLeafProperty.class, null, null, ArrayElementLeafProperty.class, null, null, SingleElementNodeProperty.class, SingleReferenceNodeProperty.class, SingleMapNodeProperty.class, ArrayElementNodeProperty.class, ArrayReferenceNodeProperty.class, null};
        propImpls = new Constructor[implClasses.length];
        for (int i2 = 0; i2 < propImpls.length; i2++) {
            if (implClasses[i2] != null) {
                propImpls[i2] = implClasses[i2].getConstructors()[0];
            }
        }
    }

    private PropertyFactory() {
    }

    public static Property create(JAXBContextImpl grammar, RuntimePropertyInfo info) {
        PropertyKind kind = info.kind();
        switch (kind) {
            case ATTRIBUTE:
                return new AttributeProperty(grammar, (RuntimeAttributePropertyInfo) info);
            case VALUE:
                return new ValueProperty(grammar, (RuntimeValuePropertyInfo) info);
            case ELEMENT:
                if (((RuntimeElementPropertyInfo) info).isValueList()) {
                    return new ListElementProperty(grammar, (RuntimeElementPropertyInfo) info);
                }
                break;
            case REFERENCE:
            case MAP:
                break;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                break;
        }
        boolean isCollection = info.isCollection();
        boolean isLeaf = isLeaf(info);
        Constructor<? extends Property> c2 = propImpls[(isLeaf ? 0 : 6) + (isCollection ? 3 : 0) + kind.propertyIndex];
        try {
            return c2.newInstance(grammar, info);
        } catch (IllegalAccessException e2) {
            throw new IllegalAccessError(e2.getMessage());
        } catch (InstantiationException e3) {
            throw new InstantiationError(e3.getMessage());
        } catch (InvocationTargetException e4) {
            Throwable t2 = e4.getCause();
            if (t2 instanceof Error) {
                throw ((Error) t2);
            }
            if (t2 instanceof RuntimeException) {
                throw ((RuntimeException) t2);
            }
            throw new AssertionError(t2);
        }
    }

    static boolean isLeaf(RuntimePropertyInfo info) {
        Collection<? extends TypeInfo<Type, Class>> collectionRef2 = info.ref2();
        if (collectionRef2.size() != 1) {
            return false;
        }
        RuntimeTypeInfo rti = (RuntimeTypeInfo) collectionRef2.iterator().next();
        if (!(rti instanceof RuntimeNonElement)) {
            return false;
        }
        if (info.id() == ID.IDREF) {
            return true;
        }
        if (((RuntimeNonElement) rti).getTransducer() == null || !info.getIndividualType().equals(rti.getType2())) {
            return false;
        }
        return true;
    }
}
