package com.sun.xml.internal.bind.v2.model.core;

import com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader;
import com.sun.xml.internal.bind.v2.model.nav.Navigator;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/core/Adapter.class */
public class Adapter<TypeT, ClassDeclT> {
    public final ClassDeclT adapterType;
    public final TypeT defaultType;
    public final TypeT customType;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Adapter.class.desiredAssertionStatus();
    }

    public Adapter(XmlJavaTypeAdapter spec, AnnotationReader<TypeT, ClassDeclT, ?, ?> reader, Navigator<TypeT, ClassDeclT, ?, ?> nav) {
        this(nav.asDecl((Navigator<TypeT, ClassDeclT, ?, ?>) reader.getClassValue(spec, "value")), nav);
    }

    public Adapter(ClassDeclT classdeclt, Navigator<TypeT, ClassDeclT, ?, ?> navigator) {
        this.adapterType = classdeclt;
        TypeT baseClass = navigator.getBaseClass(navigator.use(classdeclt), navigator.asDecl(XmlAdapter.class));
        if (!$assertionsDisabled && baseClass == null) {
            throw new AssertionError();
        }
        if (navigator.isParameterizedType(baseClass)) {
            this.defaultType = navigator.getTypeArgument(baseClass, 0);
        } else {
            this.defaultType = navigator.ref(Object.class);
        }
        if (navigator.isParameterizedType(baseClass)) {
            this.customType = navigator.getTypeArgument(baseClass, 1);
        } else {
            this.customType = navigator.ref(Object.class);
        }
    }
}
