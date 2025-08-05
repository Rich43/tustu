package jdk.jfr.internal;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import jdk.internal.org.objectweb.asm.AnnotationVisitor;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.commons.GeneratorAdapter;
import jdk.internal.org.objectweb.asm.commons.Method;
import jdk.jfr.AnnotationElement;
import jdk.jfr.Event;
import jdk.jfr.ValueDescriptor;

/* loaded from: jfr.jar:jdk/jfr/internal/EventClassBuilder.class */
public final class EventClassBuilder {
    private static final jdk.internal.org.objectweb.asm.Type TYPE_EVENT = jdk.internal.org.objectweb.asm.Type.getType((Class<?>) Event.class);
    private static final jdk.internal.org.objectweb.asm.Type TYPE_IOBE = jdk.internal.org.objectweb.asm.Type.getType((Class<?>) IndexOutOfBoundsException.class);
    private static final Method DEFAULT_CONSTRUCTOR = Method.getMethod("void <init> ()");
    private static final Method SET_METHOD = Method.getMethod("void set (int, java.lang.Object)");
    private static final AtomicLong idCounter = new AtomicLong();
    private final ClassWriter classWriter = new ClassWriter(3);
    private final String fullClassName = "jdk.jfr.DynamicEvent" + idCounter.incrementAndGet();
    private final jdk.internal.org.objectweb.asm.Type type = jdk.internal.org.objectweb.asm.Type.getType(this.fullClassName.replace(".", "/"));
    private final List<ValueDescriptor> fields;
    private final List<AnnotationElement> annotationElements;

    public EventClassBuilder(List<AnnotationElement> list, List<ValueDescriptor> list2) {
        this.fields = list2;
        this.annotationElements = list;
    }

    public Class<? extends Event> build() {
        buildClassInfo();
        buildConstructor();
        buildFields();
        buildSetMethod();
        endClass();
        byte[] byteArray = this.classWriter.toByteArray();
        ASMToolkit.logASM(this.fullClassName, byteArray);
        return SecuritySupport.defineClass(this.type.getInternalName(), byteArray, Event.class.getClassLoader()).asSubclass(Event.class);
    }

    private void endClass() {
        this.classWriter.visitEnd();
    }

    private void buildSetMethod() {
        GeneratorAdapter generatorAdapter = new GeneratorAdapter(1, SET_METHOD, (String) null, (jdk.internal.org.objectweb.asm.Type[]) null, this.classWriter);
        int i2 = 0;
        for (ValueDescriptor valueDescriptor : this.fields) {
            generatorAdapter.loadArg(0);
            generatorAdapter.visitLdcInsn(Integer.valueOf(i2));
            Label label = new Label();
            generatorAdapter.ifICmp(154, label);
            generatorAdapter.loadThis();
            generatorAdapter.loadArg(1);
            jdk.internal.org.objectweb.asm.Type type = ASMToolkit.toType(valueDescriptor);
            generatorAdapter.unbox(ASMToolkit.toType(valueDescriptor));
            generatorAdapter.putField(this.type, valueDescriptor.getName(), type);
            generatorAdapter.visitInsn(177);
            generatorAdapter.visitLabel(label);
            i2++;
        }
        generatorAdapter.throwException(TYPE_IOBE, "Index must between 0 and " + this.fields.size());
        generatorAdapter.endMethod();
    }

    private void buildConstructor() {
        MethodVisitor methodVisitorVisitMethod = this.classWriter.visitMethod(1, DEFAULT_CONSTRUCTOR.getName(), DEFAULT_CONSTRUCTOR.getDescriptor(), null, null);
        methodVisitorVisitMethod.visitIntInsn(25, 0);
        ASMToolkit.invokeSpecial(methodVisitorVisitMethod, TYPE_EVENT.getInternalName(), DEFAULT_CONSTRUCTOR);
        methodVisitorVisitMethod.visitInsn(177);
        methodVisitorVisitMethod.visitMaxs(0, 0);
    }

    private void buildClassInfo() {
        this.classWriter.visit(52, 49, this.type.getInternalName(), null, ASMToolkit.getInternalName(Event.class.getName()), null);
        for (AnnotationElement annotationElement : this.annotationElements) {
            AnnotationVisitor annotationVisitorVisitAnnotation = this.classWriter.visitAnnotation(ASMToolkit.getDescriptor(annotationElement.getTypeName()), true);
            for (ValueDescriptor valueDescriptor : annotationElement.getValueDescriptors()) {
                Object value = annotationElement.getValue(valueDescriptor.getName());
                String name = valueDescriptor.getName();
                if (valueDescriptor.isArray()) {
                    AnnotationVisitor annotationVisitorVisitArray = annotationVisitorVisitAnnotation.visitArray(name);
                    for (Object obj : (Object[]) value) {
                        annotationVisitorVisitArray.visit(null, obj);
                    }
                    annotationVisitorVisitArray.visitEnd();
                } else {
                    annotationVisitorVisitAnnotation.visit(name, value);
                }
            }
            annotationVisitorVisitAnnotation.visitEnd();
        }
    }

    private void buildFields() {
        for (ValueDescriptor valueDescriptor : this.fields) {
            this.classWriter.visitField(2, valueDescriptor.getName(), ASMToolkit.getDescriptor(valueDescriptor.getTypeName()), null, null);
        }
    }
}
