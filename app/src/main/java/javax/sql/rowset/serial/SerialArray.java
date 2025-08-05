package javax.sql.rowset.serial;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Struct;
import java.sql.Types;
import java.util.Arrays;
import java.util.Map;

/* loaded from: rt.jar:javax/sql/rowset/serial/SerialArray.class */
public class SerialArray implements Array, Serializable, Cloneable {
    private Object[] elements;
    private int baseType;
    private String baseTypeName;
    private int len;
    static final long serialVersionUID = -8466174297270688520L;

    public SerialArray(Array array, Map<String, Class<?>> map) throws SQLException {
        if (array == null || map == null) {
            throw new SQLException("Cannot instantiate a SerialArray object with null parameters");
        }
        Object[] objArr = (Object[]) array.getArray();
        this.elements = objArr;
        if (objArr == null) {
            throw new SQLException("Invalid Array object. Calls to Array.getArray() return null value which cannot be serialized");
        }
        this.elements = (Object[]) array.getArray(map);
        this.baseType = array.getBaseType();
        this.baseTypeName = array.getBaseTypeName();
        this.len = this.elements.length;
        switch (this.baseType) {
            case 70:
                for (int i2 = 0; i2 < this.len; i2++) {
                    this.elements[i2] = new SerialDatalink((URL) this.elements[i2]);
                }
                return;
            case 2000:
                for (int i3 = 0; i3 < this.len; i3++) {
                    this.elements[i3] = new SerialJavaObject(this.elements[i3]);
                }
                return;
            case Types.STRUCT /* 2002 */:
                for (int i4 = 0; i4 < this.len; i4++) {
                    this.elements[i4] = new SerialStruct((Struct) this.elements[i4], map);
                }
                return;
            case Types.ARRAY /* 2003 */:
                for (int i5 = 0; i5 < this.len; i5++) {
                    this.elements[i5] = new SerialArray((Array) this.elements[i5], map);
                }
                return;
            case Types.BLOB /* 2004 */:
                for (int i6 = 0; i6 < this.len; i6++) {
                    this.elements[i6] = new SerialBlob((Blob) this.elements[i6]);
                }
                return;
            case Types.CLOB /* 2005 */:
                for (int i7 = 0; i7 < this.len; i7++) {
                    this.elements[i7] = new SerialClob((Clob) this.elements[i7]);
                }
                return;
            default:
                return;
        }
    }

    @Override // java.sql.Array
    public void free() throws SQLException {
        if (this.elements != null) {
            this.elements = null;
            this.baseTypeName = null;
        }
    }

    public SerialArray(Array array) throws SQLException {
        if (array == null) {
            throw new SQLException("Cannot instantiate a SerialArray object with a null Array object");
        }
        Object[] objArr = (Object[]) array.getArray();
        this.elements = objArr;
        if (objArr == null) {
            throw new SQLException("Invalid Array object. Calls to Array.getArray() return null value which cannot be serialized");
        }
        this.baseType = array.getBaseType();
        this.baseTypeName = array.getBaseTypeName();
        this.len = this.elements.length;
        switch (this.baseType) {
            case 70:
                for (int i2 = 0; i2 < this.len; i2++) {
                    this.elements[i2] = new SerialDatalink((URL) this.elements[i2]);
                }
                return;
            case 2000:
                for (int i3 = 0; i3 < this.len; i3++) {
                    this.elements[i3] = new SerialJavaObject(this.elements[i3]);
                }
                return;
            case Types.BLOB /* 2004 */:
                for (int i4 = 0; i4 < this.len; i4++) {
                    this.elements[i4] = new SerialBlob((Blob) this.elements[i4]);
                }
                return;
            case Types.CLOB /* 2005 */:
                for (int i5 = 0; i5 < this.len; i5++) {
                    this.elements[i5] = new SerialClob((Clob) this.elements[i5]);
                }
                return;
            default:
                return;
        }
    }

    @Override // java.sql.Array
    public Object getArray() throws SerialException {
        isValid();
        Object[] objArr = new Object[this.len];
        System.arraycopy(this.elements, 0, objArr, 0, this.len);
        return objArr;
    }

    @Override // java.sql.Array
    public Object getArray(Map<String, Class<?>> map) throws SerialException {
        isValid();
        Object[] objArr = new Object[this.len];
        System.arraycopy(this.elements, 0, objArr, 0, this.len);
        return objArr;
    }

    @Override // java.sql.Array
    public Object getArray(long j2, int i2) throws SerialException {
        isValid();
        Object[] objArr = new Object[i2];
        System.arraycopy(this.elements, (int) j2, objArr, 0, i2);
        return objArr;
    }

    @Override // java.sql.Array
    public Object getArray(long j2, int i2, Map<String, Class<?>> map) throws SerialException {
        isValid();
        Object[] objArr = new Object[i2];
        System.arraycopy(this.elements, (int) j2, objArr, 0, i2);
        return objArr;
    }

    @Override // java.sql.Array
    public int getBaseType() throws SerialException {
        isValid();
        return this.baseType;
    }

    @Override // java.sql.Array
    public String getBaseTypeName() throws SerialException {
        isValid();
        return this.baseTypeName;
    }

    @Override // java.sql.Array
    public ResultSet getResultSet(long j2, int i2) throws SerialException {
        SerialException serialException = new SerialException();
        serialException.initCause(new UnsupportedOperationException());
        throw serialException;
    }

    @Override // java.sql.Array
    public ResultSet getResultSet(Map<String, Class<?>> map) throws SerialException {
        SerialException serialException = new SerialException();
        serialException.initCause(new UnsupportedOperationException());
        throw serialException;
    }

    @Override // java.sql.Array
    public ResultSet getResultSet() throws SerialException {
        SerialException serialException = new SerialException();
        serialException.initCause(new UnsupportedOperationException());
        throw serialException;
    }

    @Override // java.sql.Array
    public ResultSet getResultSet(long j2, int i2, Map<String, Class<?>> map) throws SerialException {
        SerialException serialException = new SerialException();
        serialException.initCause(new UnsupportedOperationException());
        throw serialException;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof SerialArray) {
            SerialArray serialArray = (SerialArray) obj;
            return this.baseType == serialArray.baseType && this.baseTypeName.equals(serialArray.baseTypeName) && Arrays.equals(this.elements, serialArray.elements);
        }
        return false;
    }

    public int hashCode() {
        return ((((((31 + Arrays.hashCode(this.elements)) * 31) + this.len) * 31) + this.baseType) * 31) + this.baseTypeName.hashCode();
    }

    public Object clone() {
        try {
            SerialArray serialArray = (SerialArray) super.clone();
            serialArray.elements = this.elements != null ? Arrays.copyOf(this.elements, this.len) : null;
            return serialArray;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError();
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        Object[] objArr = (Object[]) fields.get(Constants.ATTRNAME_ELEMENTS, (Object) null);
        if (objArr == null) {
            throw new InvalidObjectException("elements is null and should not be!");
        }
        this.elements = (Object[]) objArr.clone();
        this.len = fields.get("len", 0);
        if (this.elements.length != this.len) {
            throw new InvalidObjectException("elements is not the expected size");
        }
        this.baseType = fields.get("baseType", 0);
        this.baseTypeName = (String) fields.get("baseTypeName", (Object) null);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException, ClassNotFoundException {
        ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
        putFieldPutFields.put(Constants.ATTRNAME_ELEMENTS, this.elements);
        putFieldPutFields.put("len", this.len);
        putFieldPutFields.put("baseType", this.baseType);
        putFieldPutFields.put("baseTypeName", this.baseTypeName);
        objectOutputStream.writeFields();
    }

    private void isValid() throws SerialException {
        if (this.elements == null) {
            throw new SerialException("Error: You cannot call a method on a SerialArray instance once free() has been called.");
        }
    }
}
