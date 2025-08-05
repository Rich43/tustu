package javax.management;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.Objects;

/* loaded from: rt.jar:javax/management/MBeanFeatureInfo.class */
public class MBeanFeatureInfo implements Serializable, DescriptorRead {
    static final long serialVersionUID = 3952882688968447265L;
    protected String name;
    protected String description;
    private transient Descriptor descriptor;

    public MBeanFeatureInfo(String str, String str2) {
        this(str, str2, null);
    }

    public MBeanFeatureInfo(String str, String str2, Descriptor descriptor) {
        this.name = str;
        this.description = str2;
        this.descriptor = descriptor;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    @Override // javax.management.DescriptorRead
    public Descriptor getDescriptor() {
        return (Descriptor) ImmutableDescriptor.nonNullDescriptor(this.descriptor).clone();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MBeanFeatureInfo)) {
            return false;
        }
        MBeanFeatureInfo mBeanFeatureInfo = (MBeanFeatureInfo) obj;
        return Objects.equals(mBeanFeatureInfo.getName(), getName()) && Objects.equals(mBeanFeatureInfo.getDescription(), getDescription()) && Objects.equals(mBeanFeatureInfo.getDescriptor(), getDescriptor());
    }

    public int hashCode() {
        return (getName().hashCode() ^ getDescription().hashCode()) ^ getDescriptor().hashCode();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        if (this.descriptor != null && this.descriptor.getClass() == ImmutableDescriptor.class) {
            objectOutputStream.write(1);
            String[] fieldNames = this.descriptor.getFieldNames();
            objectOutputStream.writeObject(fieldNames);
            objectOutputStream.writeObject(this.descriptor.getFieldValues(fieldNames));
            return;
        }
        objectOutputStream.write(0);
        objectOutputStream.writeObject(this.descriptor);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        switch (objectInputStream.read()) {
            case -1:
                this.descriptor = ImmutableDescriptor.EMPTY_DESCRIPTOR;
                return;
            case 0:
                this.descriptor = (Descriptor) objectInputStream.readObject();
                if (this.descriptor == null) {
                    this.descriptor = ImmutableDescriptor.EMPTY_DESCRIPTOR;
                    return;
                }
                return;
            case 1:
                String[] strArr = (String[]) objectInputStream.readObject();
                this.descriptor = strArr.length == 0 ? ImmutableDescriptor.EMPTY_DESCRIPTOR : new ImmutableDescriptor(strArr, (Object[]) objectInputStream.readObject());
                return;
            default:
                throw new StreamCorruptedException("Got unexpected byte.");
        }
    }
}
