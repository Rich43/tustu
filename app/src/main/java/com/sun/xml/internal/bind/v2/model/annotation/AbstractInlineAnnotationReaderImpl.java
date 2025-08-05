package com.sun.xml.internal.bind.v2.model.annotation;

import com.sun.xml.internal.bind.v2.model.core.ErrorHandler;
import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;
import java.lang.annotation.Annotation;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/annotation/AbstractInlineAnnotationReaderImpl.class */
public abstract class AbstractInlineAnnotationReaderImpl<T, C, F, M> implements AnnotationReader<T, C, F, M> {
    private ErrorHandler errorHandler;
    static final /* synthetic */ boolean $assertionsDisabled;

    protected abstract String fullName(M m2);

    static {
        $assertionsDisabled = !AbstractInlineAnnotationReaderImpl.class.desiredAssertionStatus();
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader
    public void setErrorHandler(ErrorHandler errorHandler) {
        if (errorHandler == null) {
            throw new IllegalArgumentException();
        }
        this.errorHandler = errorHandler;
    }

    public final ErrorHandler getErrorHandler() {
        if ($assertionsDisabled || this.errorHandler != null) {
            return this.errorHandler;
        }
        throw new AssertionError((Object) "error handler must be set before use");
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader
    public final <A extends Annotation> A getMethodAnnotation(Class<A> cls, M m2, M m3, Locatable locatable) {
        A a2 = (A) (m2 == null ? null : getMethodAnnotation(cls, m2, locatable));
        A a3 = (A) (m3 == null ? null : getMethodAnnotation(cls, m3, locatable));
        if (a2 == null) {
            if (a3 == null) {
                return null;
            }
            return a3;
        }
        if (a3 == null) {
            return a2;
        }
        getErrorHandler().error(new IllegalAnnotationException(Messages.DUPLICATE_ANNOTATIONS.format(cls.getName(), fullName(m2), fullName(m3)), a2, a3));
        return a2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader
    public boolean hasMethodAnnotation(Class<? extends Annotation> cls, String propertyName, M getter, M setter, Locatable srcPos) {
        boolean x2 = getter != null && hasMethodAnnotation(cls, getter);
        boolean y2 = setter != null && hasMethodAnnotation(cls, setter);
        if (x2 && y2) {
            getMethodAnnotation(cls, getter, setter, srcPos);
        }
        return x2 || y2;
    }
}
