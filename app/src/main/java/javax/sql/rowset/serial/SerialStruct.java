package javax.sql.rowset.serial;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Ref;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.Arrays;
import java.util.Map;
import java.util.Vector;

/* loaded from: rt.jar:javax/sql/rowset/serial/SerialStruct.class */
public class SerialStruct implements Struct, Serializable, Cloneable {
    private String SQLTypeName;
    private Object[] attribs;
    static final long serialVersionUID = -8322445504027483372L;

    public SerialStruct(Struct struct, Map<String, Class<?>> map) throws SerialException {
        try {
            this.SQLTypeName = struct.getSQLTypeName();
            System.out.println("SQLTypeName: " + this.SQLTypeName);
            this.attribs = struct.getAttributes(map);
            mapToSerial(map);
        } catch (SQLException e2) {
            throw new SerialException(e2.getMessage());
        }
    }

    public SerialStruct(SQLData sQLData, Map<String, Class<?>> map) throws SerialException {
        try {
            this.SQLTypeName = sQLData.getSQLTypeName();
            Vector vector = new Vector();
            sQLData.writeSQL(new SQLOutputImpl(vector, map));
            this.attribs = vector.toArray();
        } catch (SQLException e2) {
            throw new SerialException(e2.getMessage());
        }
    }

    @Override // java.sql.Struct
    public String getSQLTypeName() throws SerialException {
        return this.SQLTypeName;
    }

    @Override // java.sql.Struct
    public Object[] getAttributes() throws SerialException {
        Object[] objArr = this.attribs;
        if (objArr == null) {
            return null;
        }
        return Arrays.copyOf(objArr, objArr.length);
    }

    @Override // java.sql.Struct
    public Object[] getAttributes(Map<String, Class<?>> map) throws SerialException {
        Object[] objArr = this.attribs;
        if (objArr == null) {
            return null;
        }
        return Arrays.copyOf(objArr, objArr.length);
    }

    private void mapToSerial(Map<String, Class<?>> map) throws SerialException {
        for (int i2 = 0; i2 < this.attribs.length; i2++) {
            try {
                if (this.attribs[i2] instanceof Struct) {
                    this.attribs[i2] = new SerialStruct((Struct) this.attribs[i2], map);
                } else if (this.attribs[i2] instanceof SQLData) {
                    this.attribs[i2] = new SerialStruct((SQLData) this.attribs[i2], map);
                } else if (this.attribs[i2] instanceof Blob) {
                    this.attribs[i2] = new SerialBlob((Blob) this.attribs[i2]);
                } else if (this.attribs[i2] instanceof Clob) {
                    this.attribs[i2] = new SerialClob((Clob) this.attribs[i2]);
                } else if (this.attribs[i2] instanceof Ref) {
                    this.attribs[i2] = new SerialRef((Ref) this.attribs[i2]);
                } else if (this.attribs[i2] instanceof Array) {
                    this.attribs[i2] = new SerialArray((Array) this.attribs[i2], map);
                }
            } catch (SQLException e2) {
                throw new SerialException(e2.getMessage());
            }
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof SerialStruct) {
            SerialStruct serialStruct = (SerialStruct) obj;
            return this.SQLTypeName.equals(serialStruct.SQLTypeName) && Arrays.equals(this.attribs, serialStruct.attribs);
        }
        return false;
    }

    public int hashCode() {
        return ((31 + Arrays.hashCode(this.attribs)) * 31 * 31) + this.SQLTypeName.hashCode();
    }

    public Object clone() {
        try {
            SerialStruct serialStruct = (SerialStruct) super.clone();
            serialStruct.attribs = Arrays.copyOf(this.attribs, this.attribs.length);
            return serialStruct;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError();
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        Object[] objArr = (Object[]) fields.get("attribs", (Object) null);
        this.attribs = objArr == null ? null : (Object[]) objArr.clone();
        this.SQLTypeName = (String) fields.get("SQLTypeName", (Object) null);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException, ClassNotFoundException {
        ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
        putFieldPutFields.put("attribs", this.attribs);
        putFieldPutFields.put("SQLTypeName", this.SQLTypeName);
        objectOutputStream.writeFields();
    }
}
