package java.beans;

import com.sun.beans.finder.ClassFinder;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/* compiled from: Beans.java */
/* loaded from: rt.jar:java/beans/ObjectInputStreamWithLoader.class */
class ObjectInputStreamWithLoader extends ObjectInputStream {
    private ClassLoader loader;

    public ObjectInputStreamWithLoader(InputStream inputStream, ClassLoader classLoader) throws IOException {
        super(inputStream);
        if (classLoader == null) {
            throw new IllegalArgumentException("Illegal null argument to ObjectInputStreamWithLoader");
        }
        this.loader = classLoader;
    }

    @Override // java.io.ObjectInputStream
    protected Class resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
        return ClassFinder.resolveClass(objectStreamClass.getName(), this.loader);
    }
}
