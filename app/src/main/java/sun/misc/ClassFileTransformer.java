package sun.misc;

import java.util.ArrayList;
import java.util.List;

@Deprecated
/* loaded from: rt.jar:sun/misc/ClassFileTransformer.class */
public abstract class ClassFileTransformer {
    private static final List<ClassFileTransformer> transformers = new ArrayList();

    public abstract byte[] transform(byte[] bArr, int i2, int i3) throws ClassFormatError;

    public static void add(ClassFileTransformer classFileTransformer) {
        synchronized (transformers) {
            transformers.add(classFileTransformer);
        }
    }

    public static ClassFileTransformer[] getTransformers() {
        ClassFileTransformer[] classFileTransformerArr;
        synchronized (transformers) {
            classFileTransformerArr = (ClassFileTransformer[]) transformers.toArray(new ClassFileTransformer[transformers.size()]);
        }
        return classFileTransformerArr;
    }
}
