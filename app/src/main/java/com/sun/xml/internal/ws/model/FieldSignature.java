package com.sun.xml.internal.ws.model;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import org.slf4j.Marker;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/FieldSignature.class */
final class FieldSignature {
    static final /* synthetic */ boolean $assertionsDisabled;

    FieldSignature() {
    }

    static {
        $assertionsDisabled = !FieldSignature.class.desiredAssertionStatus();
    }

    static String vms(Type t2) {
        if ((t2 instanceof Class) && ((Class) t2).isPrimitive()) {
            Class c2 = (Class) t2;
            if (c2 == Integer.TYPE) {
                return "I";
            }
            if (c2 == Void.TYPE) {
                return "V";
            }
            if (c2 == Boolean.TYPE) {
                return Constants.HASIDCALL_INDEX_SIG;
            }
            if (c2 == Byte.TYPE) {
                return PdfOps.B_TOKEN;
            }
            if (c2 == Character.TYPE) {
                return "C";
            }
            if (c2 == Short.TYPE) {
                return PdfOps.S_TOKEN;
            }
            if (c2 == Double.TYPE) {
                return PdfOps.D_TOKEN;
            }
            if (c2 == Float.TYPE) {
                return PdfOps.F_TOKEN;
            }
            if (c2 == Long.TYPE) {
                return "J";
            }
        } else {
            if ((t2 instanceof Class) && ((Class) t2).isArray()) {
                return "[" + vms(((Class) t2).getComponentType());
            }
            if ((t2 instanceof Class) || (t2 instanceof ParameterizedType)) {
                return "L" + fqcn(t2) + ";";
            }
            if (t2 instanceof GenericArrayType) {
                return "[" + vms(((GenericArrayType) t2).getGenericComponentType());
            }
            if (t2 instanceof TypeVariable) {
                return Constants.OBJECT_SIG;
            }
            if (t2 instanceof WildcardType) {
                WildcardType w2 = (WildcardType) t2;
                if (w2.getLowerBounds().length > 0) {
                    return LanguageTag.SEP + vms(w2.getLowerBounds()[0]);
                }
                if (w2.getUpperBounds().length > 0) {
                    Type wt = w2.getUpperBounds()[0];
                    if (wt.equals(Object.class)) {
                        return "*";
                    }
                    return Marker.ANY_NON_NULL_MARKER + vms(wt);
                }
            }
        }
        throw new IllegalArgumentException("Illegal vms arg " + ((Object) t2));
    }

    private static String fqcn(Type t2) {
        if (t2 instanceof Class) {
            Class c2 = (Class) t2;
            if (c2.getDeclaringClass() == null) {
                return c2.getName().replace('.', '/');
            }
            return fqcn(c2.getDeclaringClass()) + FXMLLoader.EXPRESSION_PREFIX + c2.getSimpleName();
        }
        if (t2 instanceof ParameterizedType) {
            ParameterizedType p2 = (ParameterizedType) t2;
            if (p2.getOwnerType() == null) {
                return fqcn(p2.getRawType()) + args(p2);
            }
            if ($assertionsDisabled || (p2.getRawType() instanceof Class)) {
                return fqcn(p2.getOwnerType()) + "." + ((Class) p2.getRawType()).getSimpleName() + args(p2);
            }
            throw new AssertionError();
        }
        throw new IllegalArgumentException("Illegal fqcn arg = " + ((Object) t2));
    }

    private static String args(ParameterizedType p2) {
        StringBuilder sig = new StringBuilder("<");
        for (Type t2 : p2.getActualTypeArguments()) {
            sig.append(vms(t2));
        }
        return sig.append(">").toString();
    }
}
