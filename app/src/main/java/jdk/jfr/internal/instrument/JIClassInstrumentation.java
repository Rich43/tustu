package jdk.jfr.internal.instrument;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.jfr.internal.SecuritySupport;
import jdk.jfr.internal.Utils;

@Deprecated
/* loaded from: jfr.jar:jdk/jfr/internal/instrument/JIClassInstrumentation.class */
final class JIClassInstrumentation {
    private final Class<?> instrumentor;
    private final String targetName;
    private final String instrumentorName;
    private final byte[] newBytes = makeBytecode();
    private final ClassReader targetClassReader;
    private final ClassReader instrClassReader;

    JIClassInstrumentation(Class<?> cls, Class<?> cls2, byte[] bArr) throws ClassNotFoundException, IOException {
        this.instrumentorName = cls.getName();
        this.targetName = cls2.getName();
        this.instrumentor = cls;
        this.targetClassReader = new ClassReader(bArr);
        this.instrClassReader = new ClassReader(getOriginalClassBytes(cls));
        Utils.writeGeneratedASM(cls2.getName(), this.newBytes);
    }

    private static byte[] getOriginalClassBytes(Class<?> cls) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        InputStream resourceAsStream = SecuritySupport.getResourceAsStream("/" + cls.getName().replace(".", "/") + ".class");
        byte[] bArr = new byte[16384];
        while (true) {
            int i2 = resourceAsStream.read(bArr, 0, bArr.length);
            if (i2 != -1) {
                byteArrayOutputStream.write(bArr, 0, i2);
            } else {
                byteArrayOutputStream.flush();
                resourceAsStream.close();
                return byteArrayOutputStream.toByteArray();
            }
        }
    }

    private byte[] makeBytecode() throws SecurityException, IOException, ClassNotFoundException {
        ArrayList arrayList = new ArrayList();
        for (Method method : this.instrumentor.getDeclaredMethods()) {
            if (((JIInstrumentationMethod) method.getAnnotation(JIInstrumentationMethod.class)) != null) {
                arrayList.add(method);
            }
        }
        ClassNode classNode = new ClassNode();
        this.instrClassReader.accept(new JIInliner(Opcodes.ASM5, classNode, this.targetName, this.instrumentorName, this.targetClassReader, arrayList), 8);
        ClassWriter classWriter = new ClassWriter(2);
        this.targetClassReader.accept(new JIMethodMergeAdapter(classWriter, classNode, arrayList, (JITypeMapping[]) this.instrumentor.getAnnotationsByType(JITypeMapping.class)), 8);
        return classWriter.toByteArray();
    }

    public byte[] getNewBytes() {
        return (byte[]) this.newBytes.clone();
    }
}
