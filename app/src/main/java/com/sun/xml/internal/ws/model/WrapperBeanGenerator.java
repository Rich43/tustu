package com.sun.xml.internal.ws.model;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader;
import com.sun.xml.internal.bind.v2.model.annotation.RuntimeInlineAnnotationReader;
import com.sun.xml.internal.bind.v2.model.nav.Navigator;
import com.sun.xml.internal.ws.model.AbstractWrapperBeanGenerator;
import com.sun.xml.internal.ws.org.objectweb.asm.AnnotationVisitor;
import com.sun.xml.internal.ws.org.objectweb.asm.ClassWriter;
import com.sun.xml.internal.ws.org.objectweb.asm.FieldVisitor;
import com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlAttachmentRef;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;
import javax.xml.ws.Holder;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/WrapperBeanGenerator.class */
public class WrapperBeanGenerator {
    private static final Logger LOGGER;
    private static final FieldFactory FIELD_FACTORY;
    private static final AbstractWrapperBeanGenerator RUNTIME_GENERATOR;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !WrapperBeanGenerator.class.desiredAssertionStatus();
        LOGGER = Logger.getLogger(WrapperBeanGenerator.class.getName());
        FIELD_FACTORY = new FieldFactory();
        RUNTIME_GENERATOR = new RuntimeWrapperBeanGenerator(new RuntimeInlineAnnotationReader(), Utils.REFLECTION_NAVIGATOR, FIELD_FACTORY);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/model/WrapperBeanGenerator$RuntimeWrapperBeanGenerator.class */
    private static final class RuntimeWrapperBeanGenerator extends AbstractWrapperBeanGenerator<Type, Class, Method, Field> {
        protected RuntimeWrapperBeanGenerator(AnnotationReader<Type, Class, ?, Method> annReader, Navigator<Type, Class, ?, Method> nav, AbstractWrapperBeanGenerator.BeanMemberFactory<Type, Field> beanMemberFactory) {
            super(annReader, nav, beanMemberFactory);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.xml.internal.ws.model.AbstractWrapperBeanGenerator
        public Type getSafeType(Type type) {
            return type;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.xml.internal.ws.model.AbstractWrapperBeanGenerator
        public Type getHolderValueType(Type paramType) {
            if (paramType instanceof ParameterizedType) {
                ParameterizedType p2 = (ParameterizedType) paramType;
                if (p2.getRawType().equals(Holder.class)) {
                    return p2.getActualTypeArguments()[0];
                }
                return null;
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.xml.internal.ws.model.AbstractWrapperBeanGenerator
        public boolean isVoidType(Type type) {
            return type == Void.TYPE;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/model/WrapperBeanGenerator$FieldFactory.class */
    private static final class FieldFactory implements AbstractWrapperBeanGenerator.BeanMemberFactory<Type, Field> {
        private FieldFactory() {
        }

        @Override // com.sun.xml.internal.ws.model.AbstractWrapperBeanGenerator.BeanMemberFactory
        public /* bridge */ /* synthetic */ Field createWrapperBeanMember(Type type, String str, List list) {
            return createWrapperBeanMember2(type, str, (List<Annotation>) list);
        }

        /* renamed from: createWrapperBeanMember, reason: avoid collision after fix types in other method */
        public Field createWrapperBeanMember2(Type paramType, String paramName, List<Annotation> jaxb) {
            return new Field(paramName, paramType, WrapperBeanGenerator.getASMType(paramType), jaxb);
        }
    }

    private static byte[] createBeanImage(String className, String rootName, String rootNS, String typeName, String typeNS, Collection<Field> fields) throws Exception {
        ClassWriter cw = new ClassWriter(0);
        cw.visit(49, 33, replaceDotWithSlash(className), null, "java/lang/Object", null);
        AnnotationVisitor root = cw.visitAnnotation("Ljavax/xml/bind/annotation/XmlRootElement;", true);
        root.visit("name", rootName);
        root.visit(Constants.ATTRNAME_NAMESPACE, rootNS);
        root.visitEnd();
        AnnotationVisitor type = cw.visitAnnotation("Ljavax/xml/bind/annotation/XmlType;", true);
        type.visit("name", typeName);
        type.visit(Constants.ATTRNAME_NAMESPACE, typeNS);
        if (fields.size() > 1) {
            AnnotationVisitor propVisitor = type.visitArray("propOrder");
            Iterator<Field> it = fields.iterator();
            while (it.hasNext()) {
                propVisitor.visit("propOrder", it.next().fieldName);
            }
            propVisitor.visitEnd();
        }
        type.visitEnd();
        for (Field field : fields) {
            FieldVisitor fv = cw.visitField(1, field.fieldName, field.asmType.getDescriptor(), field.getSignature(), null);
            for (Annotation ann : field.jaxbAnnotations) {
                if (ann instanceof XmlMimeType) {
                    AnnotationVisitor mime = fv.visitAnnotation("Ljavax/xml/bind/annotation/XmlMimeType;", true);
                    mime.visit("value", ((XmlMimeType) ann).value());
                    mime.visitEnd();
                } else if (ann instanceof XmlJavaTypeAdapter) {
                    AnnotationVisitor ada = fv.visitAnnotation("Ljavax/xml/bind/annotation/adapters/XmlJavaTypeAdapter;", true);
                    ada.visit("value", getASMType(((XmlJavaTypeAdapter) ann).value()));
                    ada.visitEnd();
                } else if (ann instanceof XmlAttachmentRef) {
                    AnnotationVisitor att = fv.visitAnnotation("Ljavax/xml/bind/annotation/XmlAttachmentRef;", true);
                    att.visitEnd();
                } else if (ann instanceof XmlList) {
                    AnnotationVisitor list = fv.visitAnnotation("Ljavax/xml/bind/annotation/XmlList;", true);
                    list.visitEnd();
                } else if (ann instanceof XmlElement) {
                    AnnotationVisitor elem = fv.visitAnnotation("Ljavax/xml/bind/annotation/XmlElement;", true);
                    XmlElement xmlElem = (XmlElement) ann;
                    elem.visit("name", xmlElem.name());
                    elem.visit(Constants.ATTRNAME_NAMESPACE, xmlElem.namespace());
                    if (xmlElem.nillable()) {
                        elem.visit("nillable", true);
                    }
                    if (xmlElem.required()) {
                        elem.visit(SchemaSymbols.ATTVAL_REQUIRED, true);
                    }
                    elem.visitEnd();
                } else {
                    throw new WebServiceException("Unknown JAXB annotation " + ((Object) ann));
                }
            }
            fv.visitEnd();
        }
        MethodVisitor mv = cw.visitMethod(1, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(25, 0);
        mv.visitMethodInsn(183, "java/lang/Object", com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "()V");
        mv.visitInsn(177);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
        cw.visitEnd();
        if (LOGGER.isLoggable(Level.FINE)) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n");
            sb.append("@XmlRootElement(name=").append(rootName).append(", namespace=").append(rootNS).append(")");
            sb.append("\n");
            sb.append("@XmlType(name=").append(typeName).append(", namespace=").append(typeNS);
            if (fields.size() > 1) {
                sb.append(", propOrder={");
                for (Field field2 : fields) {
                    sb.append(" ");
                    sb.append(field2.fieldName);
                }
                sb.append(" }");
            }
            sb.append(")");
            sb.append("\n");
            sb.append("public class ").append(className).append(" {");
            for (Field field3 : fields) {
                sb.append("\n");
                for (Annotation ann2 : field3.jaxbAnnotations) {
                    sb.append("\n    ");
                    if (ann2 instanceof XmlMimeType) {
                        sb.append("@XmlMimeType(value=").append(((XmlMimeType) ann2).value()).append(")");
                    } else if (ann2 instanceof XmlJavaTypeAdapter) {
                        sb.append("@XmlJavaTypeAdapter(value=").append((Object) getASMType(((XmlJavaTypeAdapter) ann2).value())).append(")");
                    } else if (ann2 instanceof XmlAttachmentRef) {
                        sb.append("@XmlAttachmentRef");
                    } else if (ann2 instanceof XmlList) {
                        sb.append("@XmlList");
                    } else if (ann2 instanceof XmlElement) {
                        XmlElement xmlElem2 = (XmlElement) ann2;
                        sb.append("\n    ");
                        sb.append("@XmlElement(name=").append(xmlElem2.name()).append(", namespace=").append(xmlElem2.namespace());
                        if (xmlElem2.nillable()) {
                            sb.append(", nillable=true");
                        }
                        if (xmlElem2.required()) {
                            sb.append(", required=true");
                        }
                        sb.append(")");
                    } else {
                        throw new WebServiceException("Unknown JAXB annotation " + ((Object) ann2));
                    }
                }
                sb.append("\n    ");
                sb.append("public ");
                if (field3.getSignature() == null) {
                    sb.append(field3.asmType.getDescriptor());
                } else {
                    sb.append(field3.getSignature());
                }
                sb.append(" ");
                sb.append(field3.fieldName);
            }
            sb.append("\n\n}");
            LOGGER.fine(sb.toString());
        }
        return cw.toByteArray();
    }

    private static String replaceDotWithSlash(String name) {
        return name.replace('.', '/');
    }

    static Class createRequestWrapperBean(String className, Method method, QName reqElemName, ClassLoader cl) {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Request Wrapper Class : {0}", className);
        }
        List<Field> requestMembers = RUNTIME_GENERATOR.collectRequestBeanMembers(method);
        try {
            byte[] image = createBeanImage(className, reqElemName.getLocalPart(), reqElemName.getNamespaceURI(), reqElemName.getLocalPart(), reqElemName.getNamespaceURI(), requestMembers);
            return Injector.inject(cl, className, image);
        } catch (Exception e2) {
            throw new WebServiceException(e2);
        }
    }

    static Class createResponseWrapperBean(String className, Method method, QName resElemName, ClassLoader cl) {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Response Wrapper Class : {0}", className);
        }
        List<Field> responseMembers = RUNTIME_GENERATOR.collectResponseBeanMembers(method);
        try {
            byte[] image = createBeanImage(className, resElemName.getLocalPart(), resElemName.getNamespaceURI(), resElemName.getLocalPart(), resElemName.getNamespaceURI(), responseMembers);
            return Injector.inject(cl, className, image);
        } catch (Exception e2) {
            throw new WebServiceException(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static com.sun.xml.internal.ws.org.objectweb.asm.Type getASMType(Type t2) {
        if (!$assertionsDisabled && t2 == null) {
            throw new AssertionError();
        }
        if (t2 instanceof Class) {
            return com.sun.xml.internal.ws.org.objectweb.asm.Type.getType((Class) t2);
        }
        if (t2 instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) t2;
            if (pt.getRawType() instanceof Class) {
                return com.sun.xml.internal.ws.org.objectweb.asm.Type.getType((Class) pt.getRawType());
            }
        }
        if (t2 instanceof GenericArrayType) {
            return com.sun.xml.internal.ws.org.objectweb.asm.Type.getType(FieldSignature.vms(t2));
        }
        if (t2 instanceof WildcardType) {
            return com.sun.xml.internal.ws.org.objectweb.asm.Type.getType(FieldSignature.vms(t2));
        }
        if (t2 instanceof TypeVariable) {
            TypeVariable tv = (TypeVariable) t2;
            if (tv.getBounds()[0] instanceof Class) {
                return com.sun.xml.internal.ws.org.objectweb.asm.Type.getType((Class) tv.getBounds()[0]);
            }
        }
        throw new IllegalArgumentException("Not creating ASM Type for type = " + ((Object) t2));
    }

    static Class createExceptionBean(String className, Class exception, String typeNS, String elemName, String elemNS, ClassLoader cl) {
        return createExceptionBean(className, exception, typeNS, elemName, elemNS, cl, true);
    }

    static Class createExceptionBean(String className, Class exception, String typeNS, String elemName, String elemNS, ClassLoader cl, boolean decapitalizeExceptionBeanProperties) {
        Collection<Field> fields = RUNTIME_GENERATOR.collectExceptionBeanMembers(exception, decapitalizeExceptionBeanProperties);
        try {
            byte[] image = createBeanImage(className, elemName, elemNS, exception.getSimpleName(), typeNS, fields);
            return Injector.inject(cl, className, image);
        } catch (Exception e2) {
            throw new WebServiceException(e2);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/model/WrapperBeanGenerator$Field.class */
    private static class Field implements Comparable<Field> {
        private final Type reflectType;
        private final com.sun.xml.internal.ws.org.objectweb.asm.Type asmType;
        private final String fieldName;
        private final List<Annotation> jaxbAnnotations;

        Field(String paramName, Type paramType, com.sun.xml.internal.ws.org.objectweb.asm.Type asmType, List<Annotation> jaxbAnnotations) {
            this.reflectType = paramType;
            this.asmType = asmType;
            this.fieldName = paramName;
            this.jaxbAnnotations = jaxbAnnotations;
        }

        String getSignature() {
            if ((this.reflectType instanceof Class) || (this.reflectType instanceof TypeVariable)) {
                return null;
            }
            return FieldSignature.vms(this.reflectType);
        }

        @Override // java.lang.Comparable
        public int compareTo(Field o2) {
            return this.fieldName.compareTo(o2.fieldName);
        }
    }

    static void write(byte[] b2, String className) {
        try {
            FileOutputStream fo = new FileOutputStream(className.substring(className.lastIndexOf(".") + 1) + ".class");
            fo.write(b2);
            fo.flush();
            fo.close();
        } catch (IOException e2) {
            LOGGER.log(Level.INFO, "Error Writing class", (Throwable) e2);
        }
    }
}
