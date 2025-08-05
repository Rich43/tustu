package java.beans;

import java.lang.ref.Reference;

/* loaded from: rt.jar:java/beans/BeanDescriptor.class */
public class BeanDescriptor extends FeatureDescriptor {
    private Reference<? extends Class<?>> beanClassRef;
    private Reference<? extends Class<?>> customizerClassRef;

    public BeanDescriptor(Class<?> cls) {
        this(cls, null);
    }

    public BeanDescriptor(Class<?> cls, Class<?> cls2) {
        this.beanClassRef = getWeakReference(cls);
        this.customizerClassRef = getWeakReference(cls2);
        String name = cls.getName();
        while (true) {
            String str = name;
            if (str.indexOf(46) >= 0) {
                name = str.substring(str.indexOf(46) + 1);
            } else {
                setName(str);
                return;
            }
        }
    }

    public Class<?> getBeanClass() {
        if (this.beanClassRef != null) {
            return this.beanClassRef.get();
        }
        return null;
    }

    public Class<?> getCustomizerClass() {
        if (this.customizerClassRef != null) {
            return this.customizerClassRef.get();
        }
        return null;
    }

    BeanDescriptor(BeanDescriptor beanDescriptor) {
        super(beanDescriptor);
        this.beanClassRef = beanDescriptor.beanClassRef;
        this.customizerClassRef = beanDescriptor.customizerClassRef;
    }

    @Override // java.beans.FeatureDescriptor
    void appendTo(StringBuilder sb) {
        appendTo(sb, "beanClass", (Reference<?>) this.beanClassRef);
        appendTo(sb, "customizerClass", (Reference<?>) this.customizerClassRef);
    }
}
