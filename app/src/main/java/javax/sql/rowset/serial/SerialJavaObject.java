package javax.sql.rowset.serial;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Vector;
import javax.sql.rowset.RowSetWarning;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:javax/sql/rowset/serial/SerialJavaObject.class */
public class SerialJavaObject implements Serializable, Cloneable {
    private Object obj;
    private transient Field[] fields;
    static final long serialVersionUID = -1465795139032831023L;
    Vector<RowSetWarning> chain;

    public SerialJavaObject(Object obj) throws SerialException {
        Class<?> cls = obj.getClass();
        if (!(obj instanceof Serializable)) {
            setWarning(new RowSetWarning("Warning, the object passed to the constructor does not implement Serializable"));
        }
        this.fields = cls.getFields();
        if (hasStaticFields(this.fields)) {
            throw new SerialException("Located static fields in object instance. Cannot serialize");
        }
        this.obj = obj;
    }

    public Object getObject() throws SerialException {
        return this.obj;
    }

    @CallerSensitive
    public Field[] getFields() throws SerialException {
        if (this.fields != null) {
            Class<?> cls = this.obj.getClass();
            if (System.getSecurityManager() != null && ReflectUtil.needsPackageAccessCheck(Reflection.getCallerClass().getClassLoader(), cls.getClassLoader())) {
                ReflectUtil.checkPackageAccess(cls);
            }
            return cls.getFields();
        }
        throw new SerialException("SerialJavaObject does not contain a serialized object instance");
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof SerialJavaObject) {
            return this.obj.equals(((SerialJavaObject) obj).obj);
        }
        return false;
    }

    public int hashCode() {
        return 31 + this.obj.hashCode();
    }

    public Object clone() {
        try {
            SerialJavaObject serialJavaObject = (SerialJavaObject) super.clone();
            serialJavaObject.fields = (Field[]) Arrays.copyOf(this.fields, this.fields.length);
            if (this.chain != null) {
                serialJavaObject.chain = new Vector<>(this.chain);
            }
            return serialJavaObject;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError();
        }
    }

    private void setWarning(RowSetWarning rowSetWarning) {
        if (this.chain == null) {
            this.chain = new Vector<>();
        }
        this.chain.add(rowSetWarning);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        Vector vector = (Vector) fields.get("chain", (Object) null);
        if (vector != null) {
            this.chain = new Vector<>(vector);
        }
        this.obj = fields.get("obj", (Object) null);
        if (this.obj != null) {
            this.fields = this.obj.getClass().getFields();
            if (hasStaticFields(this.fields)) {
                throw new IOException("Located static fields in object instance. Cannot serialize");
            }
            return;
        }
        throw new IOException("Object cannot be null!");
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
        putFieldPutFields.put("obj", this.obj);
        putFieldPutFields.put("chain", this.chain);
        objectOutputStream.writeFields();
    }

    private static boolean hasStaticFields(Field[] fieldArr) {
        for (Field field : fieldArr) {
            if (field.getModifiers() == 8) {
                return true;
            }
        }
        return false;
    }
}
