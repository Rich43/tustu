package javax.sql.rowset.serial;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Ref;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:javax/sql/rowset/serial/SerialRef.class */
public class SerialRef implements Ref, Serializable, Cloneable {
    private String baseTypeName;
    private Object object;
    private Ref reference;
    static final long serialVersionUID = -4727123500609662274L;

    public SerialRef(Ref ref) throws SQLException {
        if (ref == null) {
            throw new SQLException("Cannot instantiate a SerialRef object with a null Ref object");
        }
        this.reference = ref;
        this.object = ref;
        if (ref.getBaseTypeName() == null) {
            throw new SQLException("Cannot instantiate a SerialRef object that returns a null base type name");
        }
        this.baseTypeName = ref.getBaseTypeName();
    }

    @Override // java.sql.Ref
    public String getBaseTypeName() throws SerialException {
        return this.baseTypeName;
    }

    @Override // java.sql.Ref
    public Object getObject(Map<String, Class<?>> map) throws SerialException {
        Hashtable hashtable = new Hashtable(map);
        if (this.object != null) {
            return hashtable.get(this.object);
        }
        throw new SerialException("The object is not set");
    }

    @Override // java.sql.Ref
    public Object getObject() throws SerialException {
        if (this.reference != null) {
            try {
                return this.reference.getObject();
            } catch (SQLException e2) {
                throw new SerialException("SQLException: " + e2.getMessage());
            }
        }
        if (this.object != null) {
            return this.object;
        }
        throw new SerialException("The object is not set");
    }

    @Override // java.sql.Ref
    public void setObject(Object obj) throws SerialException {
        try {
            this.reference.setObject(obj);
            this.object = obj;
        } catch (SQLException e2) {
            throw new SerialException("SQLException: " + e2.getMessage());
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof SerialRef) {
            SerialRef serialRef = (SerialRef) obj;
            return this.baseTypeName.equals(serialRef.baseTypeName) && this.object.equals(serialRef.object);
        }
        return false;
    }

    public int hashCode() {
        return ((31 + this.object.hashCode()) * 31) + this.baseTypeName.hashCode();
    }

    public Object clone() {
        try {
            SerialRef serialRef = (SerialRef) super.clone();
            serialRef.reference = null;
            return serialRef;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError();
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        this.object = fields.get("object", (Object) null);
        this.baseTypeName = (String) fields.get("baseTypeName", (Object) null);
        this.reference = (Ref) fields.get(FXMLLoader.REFERENCE_TAG, (Object) null);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException, ClassNotFoundException {
        ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
        putFieldPutFields.put("baseTypeName", this.baseTypeName);
        putFieldPutFields.put("object", this.object);
        putFieldPutFields.put(FXMLLoader.REFERENCE_TAG, this.reference instanceof Serializable ? this.reference : null);
        objectOutputStream.writeFields();
    }
}
