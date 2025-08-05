package com.sun.corba.se.impl.io;

import java.io.IOException;
import java.io.NotActiveException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import org.omg.CORBA.portable.ValueOutputStream;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/io/OutputStreamHook.class */
public abstract class OutputStreamHook extends ObjectOutputStream {
    private HookPutFields putFields = null;
    protected byte streamFormatVersion = 1;
    protected WriteObjectState writeObjectState = NOT_IN_WRITE_OBJECT;
    protected static final WriteObjectState NOT_IN_WRITE_OBJECT = new DefaultState();
    protected static final WriteObjectState IN_WRITE_OBJECT = new InWriteObjectState();
    protected static final WriteObjectState WROTE_DEFAULT_DATA = new WroteDefaultDataState();
    protected static final WriteObjectState WROTE_CUSTOM_DATA = new WroteCustomDataState();

    abstract void writeField(ObjectStreamField objectStreamField, Object obj) throws IOException;

    public abstract void defaultWriteObjectDelegate();

    abstract ObjectStreamField[] getFieldsNoCopy();

    abstract OutputStream getOrbStream();

    protected abstract void beginOptionalCustomData();

    /* loaded from: rt.jar:com/sun/corba/se/impl/io/OutputStreamHook$HookPutFields.class */
    private class HookPutFields extends ObjectOutputStream.PutField {
        private Map<String, Object> fields;

        private HookPutFields() {
            this.fields = new HashMap();
        }

        @Override // java.io.ObjectOutputStream.PutField
        public void put(String str, boolean z2) {
            this.fields.put(str, new Boolean(z2));
        }

        @Override // java.io.ObjectOutputStream.PutField
        public void put(String str, char c2) {
            this.fields.put(str, new Character(c2));
        }

        @Override // java.io.ObjectOutputStream.PutField
        public void put(String str, byte b2) {
            this.fields.put(str, new Byte(b2));
        }

        @Override // java.io.ObjectOutputStream.PutField
        public void put(String str, short s2) {
            this.fields.put(str, new Short(s2));
        }

        @Override // java.io.ObjectOutputStream.PutField
        public void put(String str, int i2) {
            this.fields.put(str, new Integer(i2));
        }

        @Override // java.io.ObjectOutputStream.PutField
        public void put(String str, long j2) {
            this.fields.put(str, new Long(j2));
        }

        @Override // java.io.ObjectOutputStream.PutField
        public void put(String str, float f2) {
            this.fields.put(str, new Float(f2));
        }

        @Override // java.io.ObjectOutputStream.PutField
        public void put(String str, double d2) {
            this.fields.put(str, new Double(d2));
        }

        @Override // java.io.ObjectOutputStream.PutField
        public void put(String str, Object obj) {
            this.fields.put(str, obj);
        }

        @Override // java.io.ObjectOutputStream.PutField
        public void write(ObjectOutput objectOutput) throws IOException {
            OutputStreamHook outputStreamHook = (OutputStreamHook) objectOutput;
            ObjectStreamField[] fieldsNoCopy = outputStreamHook.getFieldsNoCopy();
            for (int i2 = 0; i2 < fieldsNoCopy.length; i2++) {
                outputStreamHook.writeField(fieldsNoCopy[i2], this.fields.get(fieldsNoCopy[i2].getName()));
            }
        }
    }

    @Override // java.io.ObjectOutputStream
    public void defaultWriteObject() throws IOException {
        this.writeObjectState.defaultWriteObject(this);
        defaultWriteObjectDelegate();
    }

    @Override // java.io.ObjectOutputStream
    public ObjectOutputStream.PutField putFields() throws IOException {
        if (this.putFields == null) {
            this.putFields = new HookPutFields();
        }
        return this.putFields;
    }

    public byte getStreamFormatVersion() {
        return this.streamFormatVersion;
    }

    @Override // java.io.ObjectOutputStream
    public void writeFields() throws IOException {
        this.writeObjectState.defaultWriteObject(this);
        if (this.putFields != null) {
            this.putFields.write(this);
            return;
        }
        throw new NotActiveException("no current PutField object");
    }

    protected void setState(WriteObjectState writeObjectState) {
        this.writeObjectState = writeObjectState;
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/io/OutputStreamHook$WriteObjectState.class */
    protected static class WriteObjectState {
        protected WriteObjectState() {
        }

        public void enterWriteObject(OutputStreamHook outputStreamHook) throws IOException {
        }

        public void exitWriteObject(OutputStreamHook outputStreamHook) throws IOException {
        }

        public void defaultWriteObject(OutputStreamHook outputStreamHook) throws IOException {
        }

        public void writeData(OutputStreamHook outputStreamHook) throws IOException {
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/io/OutputStreamHook$DefaultState.class */
    protected static class DefaultState extends WriteObjectState {
        protected DefaultState() {
        }

        @Override // com.sun.corba.se.impl.io.OutputStreamHook.WriteObjectState
        public void enterWriteObject(OutputStreamHook outputStreamHook) throws IOException {
            outputStreamHook.setState(OutputStreamHook.IN_WRITE_OBJECT);
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/io/OutputStreamHook$InWriteObjectState.class */
    protected static class InWriteObjectState extends WriteObjectState {
        protected InWriteObjectState() {
        }

        @Override // com.sun.corba.se.impl.io.OutputStreamHook.WriteObjectState
        public void enterWriteObject(OutputStreamHook outputStreamHook) throws IOException {
            throw new IOException("Internal state failure: Entered writeObject twice");
        }

        @Override // com.sun.corba.se.impl.io.OutputStreamHook.WriteObjectState
        public void exitWriteObject(OutputStreamHook outputStreamHook) throws IOException {
            outputStreamHook.getOrbStream().write_boolean(false);
            if (outputStreamHook.getStreamFormatVersion() == 2) {
                outputStreamHook.getOrbStream().write_long(0);
            }
            outputStreamHook.setState(OutputStreamHook.NOT_IN_WRITE_OBJECT);
        }

        @Override // com.sun.corba.se.impl.io.OutputStreamHook.WriteObjectState
        public void defaultWriteObject(OutputStreamHook outputStreamHook) throws IOException {
            outputStreamHook.getOrbStream().write_boolean(true);
            outputStreamHook.setState(OutputStreamHook.WROTE_DEFAULT_DATA);
        }

        @Override // com.sun.corba.se.impl.io.OutputStreamHook.WriteObjectState
        public void writeData(OutputStreamHook outputStreamHook) throws IOException {
            outputStreamHook.getOrbStream().write_boolean(false);
            outputStreamHook.beginOptionalCustomData();
            outputStreamHook.setState(OutputStreamHook.WROTE_CUSTOM_DATA);
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/io/OutputStreamHook$WroteDefaultDataState.class */
    protected static class WroteDefaultDataState extends InWriteObjectState {
        protected WroteDefaultDataState() {
        }

        @Override // com.sun.corba.se.impl.io.OutputStreamHook.InWriteObjectState, com.sun.corba.se.impl.io.OutputStreamHook.WriteObjectState
        public void exitWriteObject(OutputStreamHook outputStreamHook) throws IOException {
            if (outputStreamHook.getStreamFormatVersion() == 2) {
                outputStreamHook.getOrbStream().write_long(0);
            }
            outputStreamHook.setState(OutputStreamHook.NOT_IN_WRITE_OBJECT);
        }

        @Override // com.sun.corba.se.impl.io.OutputStreamHook.InWriteObjectState, com.sun.corba.se.impl.io.OutputStreamHook.WriteObjectState
        public void defaultWriteObject(OutputStreamHook outputStreamHook) throws IOException {
            throw new IOException("Called defaultWriteObject/writeFields twice");
        }

        @Override // com.sun.corba.se.impl.io.OutputStreamHook.InWriteObjectState, com.sun.corba.se.impl.io.OutputStreamHook.WriteObjectState
        public void writeData(OutputStreamHook outputStreamHook) throws IOException {
            outputStreamHook.beginOptionalCustomData();
            outputStreamHook.setState(OutputStreamHook.WROTE_CUSTOM_DATA);
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/io/OutputStreamHook$WroteCustomDataState.class */
    protected static class WroteCustomDataState extends InWriteObjectState {
        protected WroteCustomDataState() {
        }

        @Override // com.sun.corba.se.impl.io.OutputStreamHook.InWriteObjectState, com.sun.corba.se.impl.io.OutputStreamHook.WriteObjectState
        public void exitWriteObject(OutputStreamHook outputStreamHook) throws IOException {
            if (outputStreamHook.getStreamFormatVersion() == 2) {
                ((ValueOutputStream) outputStreamHook.getOrbStream()).end_value();
            }
            outputStreamHook.setState(OutputStreamHook.NOT_IN_WRITE_OBJECT);
        }

        @Override // com.sun.corba.se.impl.io.OutputStreamHook.InWriteObjectState, com.sun.corba.se.impl.io.OutputStreamHook.WriteObjectState
        public void defaultWriteObject(OutputStreamHook outputStreamHook) throws IOException {
            throw new IOException("Cannot call defaultWriteObject/writeFields after writing custom data in RMI-IIOP");
        }

        @Override // com.sun.corba.se.impl.io.OutputStreamHook.InWriteObjectState, com.sun.corba.se.impl.io.OutputStreamHook.WriteObjectState
        public void writeData(OutputStreamHook outputStreamHook) throws IOException {
        }
    }
}
