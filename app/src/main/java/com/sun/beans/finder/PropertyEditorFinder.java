package com.sun.beans.finder;

import com.sun.beans.WeakCache;
import com.sun.beans.editors.BooleanEditor;
import com.sun.beans.editors.ByteEditor;
import com.sun.beans.editors.DoubleEditor;
import com.sun.beans.editors.EnumEditor;
import com.sun.beans.editors.FloatEditor;
import com.sun.beans.editors.IntegerEditor;
import com.sun.beans.editors.LongEditor;
import com.sun.beans.editors.ShortEditor;
import java.beans.PropertyEditor;

/* loaded from: rt.jar:com/sun/beans/finder/PropertyEditorFinder.class */
public final class PropertyEditorFinder extends InstanceFinder<PropertyEditor> {
    private static final String DEFAULT = "sun.beans.editors";
    private static final String DEFAULT_NEW = "com.sun.beans.editors";
    private final WeakCache<Class<?>, Class<?>> registry;

    @Override // com.sun.beans.finder.InstanceFinder
    protected /* bridge */ /* synthetic */ PropertyEditor instantiate(Class cls, String str, String str2) {
        return instantiate((Class<?>) cls, str, str2);
    }

    @Override // com.sun.beans.finder.InstanceFinder
    public /* bridge */ /* synthetic */ PropertyEditor find(Class cls) {
        return find((Class<?>) cls);
    }

    @Override // com.sun.beans.finder.InstanceFinder
    public /* bridge */ /* synthetic */ void setPackages(String[] strArr) {
        super.setPackages(strArr);
    }

    @Override // com.sun.beans.finder.InstanceFinder
    public /* bridge */ /* synthetic */ String[] getPackages() {
        return super.getPackages();
    }

    public PropertyEditorFinder() {
        super(PropertyEditor.class, false, "Editor", DEFAULT);
        this.registry = new WeakCache<>();
        this.registry.put(Byte.TYPE, ByteEditor.class);
        this.registry.put(Short.TYPE, ShortEditor.class);
        this.registry.put(Integer.TYPE, IntegerEditor.class);
        this.registry.put(Long.TYPE, LongEditor.class);
        this.registry.put(Boolean.TYPE, BooleanEditor.class);
        this.registry.put(Float.TYPE, FloatEditor.class);
        this.registry.put(Double.TYPE, DoubleEditor.class);
    }

    public void register(Class<?> cls, Class<?> cls2) {
        synchronized (this.registry) {
            this.registry.put(cls, cls2);
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.beans.finder.InstanceFinder
    public PropertyEditor find(Class<?> cls) {
        Class<?> cls2;
        synchronized (this.registry) {
            cls2 = this.registry.get(cls);
        }
        PropertyEditor propertyEditorInstantiate = instantiate(cls2, null);
        if (propertyEditorInstantiate == null) {
            propertyEditorInstantiate = (PropertyEditor) super.find(cls);
            if (propertyEditorInstantiate == null && null != cls.getEnumConstants()) {
                propertyEditorInstantiate = new EnumEditor(cls);
            }
        }
        return propertyEditorInstantiate;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.beans.finder.InstanceFinder
    protected PropertyEditor instantiate(Class<?> cls, String str, String str2) {
        return (PropertyEditor) super.instantiate(cls, DEFAULT.equals(str) ? DEFAULT_NEW : str, str2);
    }
}
