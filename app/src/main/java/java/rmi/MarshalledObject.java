package java.rmi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.misc.ObjectInputFilter;
import sun.rmi.server.MarshalInputStream;
import sun.rmi.server.MarshalOutputStream;

/* loaded from: rt.jar:java/rmi/MarshalledObject.class */
public final class MarshalledObject<T> implements Serializable {
    private byte[] objBytes;
    private byte[] locBytes;
    private int hash;
    private transient ObjectInputFilter objectInputFilter = null;
    private static final long serialVersionUID = 8988374069173025854L;

    public MarshalledObject(T t2) throws IOException {
        this.objBytes = null;
        this.locBytes = null;
        if (t2 == null) {
            this.hash = 13;
            return;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
        MarshalledObjectOutputStream marshalledObjectOutputStream = new MarshalledObjectOutputStream(byteArrayOutputStream, byteArrayOutputStream2);
        marshalledObjectOutputStream.writeObject(t2);
        marshalledObjectOutputStream.flush();
        this.objBytes = byteArrayOutputStream.toByteArray();
        this.locBytes = marshalledObjectOutputStream.hadAnnotations() ? byteArrayOutputStream2.toByteArray() : null;
        int i2 = 0;
        for (int i3 = 0; i3 < this.objBytes.length; i3++) {
            i2 = (31 * i2) + this.objBytes[i3];
        }
        this.hash = i2;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.objectInputFilter = ObjectInputFilter.Config.getObjectInputFilter(objectInputStream);
    }

    public T get() throws IOException, ClassNotFoundException {
        if (this.objBytes == null) {
            return null;
        }
        MarshalledObjectInputStream marshalledObjectInputStream = new MarshalledObjectInputStream(new ByteArrayInputStream(this.objBytes), this.locBytes == null ? null : new ByteArrayInputStream(this.locBytes), this.objectInputFilter);
        T t2 = (T) marshalledObjectInputStream.readObject();
        marshalledObjectInputStream.close();
        return t2;
    }

    public int hashCode() {
        return this.hash;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj != null && (obj instanceof MarshalledObject)) {
            MarshalledObject marshalledObject = (MarshalledObject) obj;
            if (this.objBytes == null || marshalledObject.objBytes == null) {
                return this.objBytes == marshalledObject.objBytes;
            }
            if (this.objBytes.length != marshalledObject.objBytes.length) {
                return false;
            }
            for (int i2 = 0; i2 < this.objBytes.length; i2++) {
                if (this.objBytes[i2] != marshalledObject.objBytes[i2]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /* loaded from: rt.jar:java/rmi/MarshalledObject$MarshalledObjectOutputStream.class */
    private static class MarshalledObjectOutputStream extends MarshalOutputStream {
        private ObjectOutputStream locOut;
        private boolean hadAnnotations;

        MarshalledObjectOutputStream(OutputStream outputStream, OutputStream outputStream2) throws IOException {
            super(outputStream);
            useProtocolVersion(2);
            this.locOut = new ObjectOutputStream(outputStream2);
            this.hadAnnotations = false;
        }

        boolean hadAnnotations() {
            return this.hadAnnotations;
        }

        @Override // sun.rmi.server.MarshalOutputStream
        protected void writeLocation(String str) throws IOException {
            this.hadAnnotations |= str != null;
            this.locOut.writeObject(str);
        }

        @Override // java.io.ObjectOutputStream, java.io.OutputStream, java.io.Flushable
        public void flush() throws IOException {
            super.flush();
            this.locOut.flush();
        }
    }

    /* loaded from: rt.jar:java/rmi/MarshalledObject$MarshalledObjectInputStream.class */
    private static class MarshalledObjectInputStream extends MarshalInputStream {
        private ObjectInputStream locIn;

        MarshalledObjectInputStream(InputStream inputStream, InputStream inputStream2, final ObjectInputFilter objectInputFilter) throws IOException {
            super(inputStream);
            this.locIn = inputStream2 == null ? null : new ObjectInputStream(inputStream2);
            if (objectInputFilter != null) {
                AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.rmi.MarshalledObject.MarshalledObjectInputStream.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    public Void run() {
                        ObjectInputFilter.Config.setObjectInputFilter(MarshalledObjectInputStream.this, objectInputFilter);
                        if (MarshalledObjectInputStream.this.locIn != null) {
                            ObjectInputFilter.Config.setObjectInputFilter(MarshalledObjectInputStream.this.locIn, objectInputFilter);
                            return null;
                        }
                        return null;
                    }
                });
            }
        }

        @Override // sun.rmi.server.MarshalInputStream
        protected Object readLocation() throws IOException, ClassNotFoundException {
            if (this.locIn == null) {
                return null;
            }
            return this.locIn.readObject();
        }
    }
}
