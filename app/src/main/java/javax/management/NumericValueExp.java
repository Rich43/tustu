package javax.management;

import com.sun.jmx.mbeanserver.GetPropertyAction;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.security.AccessController;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:javax/management/NumericValueExp.class */
class NumericValueExp extends QueryEval implements ValueExp {
    private static final long oldSerialVersionUID = -6227876276058904000L;
    private static final long newSerialVersionUID = -4679739485102359104L;
    private static final ObjectStreamField[] oldSerialPersistentFields = {new ObjectStreamField("longVal", Long.TYPE), new ObjectStreamField("doubleVal", Double.TYPE), new ObjectStreamField("valIsLong", Boolean.TYPE)};
    private static final ObjectStreamField[] newSerialPersistentFields = {new ObjectStreamField("val", Number.class)};
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private Number val;
    private static boolean compat;

    static {
        compat = false;
        try {
            String str = (String) AccessController.doPrivileged(new GetPropertyAction("jmx.serial.form"));
            compat = str != null && str.equals("1.0");
        } catch (Exception e2) {
        }
        if (compat) {
            serialPersistentFields = oldSerialPersistentFields;
            serialVersionUID = oldSerialVersionUID;
        } else {
            serialPersistentFields = newSerialPersistentFields;
            serialVersionUID = newSerialVersionUID;
        }
    }

    public NumericValueExp() {
        this.val = Double.valueOf(0.0d);
    }

    NumericValueExp(Number number) {
        this.val = Double.valueOf(0.0d);
        this.val = number;
    }

    public double doubleValue() {
        if ((this.val instanceof Long) || (this.val instanceof Integer)) {
            return this.val.longValue();
        }
        return this.val.doubleValue();
    }

    public long longValue() {
        if ((this.val instanceof Long) || (this.val instanceof Integer)) {
            return this.val.longValue();
        }
        return (long) this.val.doubleValue();
    }

    public boolean isLong() {
        return (this.val instanceof Long) || (this.val instanceof Integer);
    }

    public String toString() {
        if (this.val == null) {
            return FXMLLoader.NULL_KEYWORD;
        }
        if ((this.val instanceof Long) || (this.val instanceof Integer)) {
            return Long.toString(this.val.longValue());
        }
        double dDoubleValue = this.val.doubleValue();
        if (Double.isInfinite(dDoubleValue)) {
            return dDoubleValue > 0.0d ? "(1.0 / 0.0)" : "(-1.0 / 0.0)";
        }
        if (Double.isNaN(dDoubleValue)) {
            return "(0.0 / 0.0)";
        }
        return Double.toString(dDoubleValue);
    }

    @Override // javax.management.ValueExp
    public ValueExp apply(ObjectName objectName) throws InvalidApplicationException, BadBinaryOpValueExpException, BadAttributeValueExpException, BadStringOperationException {
        return this;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        if (compat) {
            ObjectInputStream.GetField fields = objectInputStream.readFields();
            double d2 = fields.get("doubleVal", 0.0d);
            if (fields.defaulted("doubleVal")) {
                throw new NullPointerException("doubleVal");
            }
            long j2 = fields.get("longVal", 0L);
            if (fields.defaulted("longVal")) {
                throw new NullPointerException("longVal");
            }
            boolean z2 = fields.get("valIsLong", false);
            if (fields.defaulted("valIsLong")) {
                throw new NullPointerException("valIsLong");
            }
            if (z2) {
                this.val = Long.valueOf(j2);
                return;
            } else {
                this.val = Double.valueOf(d2);
                return;
            }
        }
        objectInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
            putFieldPutFields.put("doubleVal", doubleValue());
            putFieldPutFields.put("longVal", longValue());
            putFieldPutFields.put("valIsLong", isLong());
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }

    @Override // javax.management.QueryEval, javax.management.ValueExp
    @Deprecated
    public void setMBeanServer(MBeanServer mBeanServer) {
        super.setMBeanServer(mBeanServer);
    }
}
