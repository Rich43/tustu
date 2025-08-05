package com.sun.corba.se.impl.io;

import com.sun.corba.se.impl.logging.OMGSystemException;
import com.sun.corba.se.impl.logging.UtilSystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORBVersion;
import com.sun.corba.se.spi.orb.ORBVersionFactory;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.HashMap;
import java.util.Map;
import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.ValueInputStream;
import org.omg.CORBA_2_3.portable.InputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/io/InputStreamHook.class */
public abstract class InputStreamHook extends ObjectInputStream {
    protected ReadObjectState readObjectState = DEFAULT_STATE;
    static final OMGSystemException omgWrapper = OMGSystemException.get(CORBALogDomains.RPC_ENCODING);
    static final UtilSystemException utilWrapper = UtilSystemException.get(CORBALogDomains.RPC_ENCODING);
    protected static final ReadObjectState DEFAULT_STATE = new DefaultState();
    protected static final ReadObjectState IN_READ_OBJECT_OPT_DATA = new InReadObjectOptionalDataState();
    protected static final ReadObjectState IN_READ_OBJECT_NO_MORE_OPT_DATA = new InReadObjectNoMoreOptionalDataState();
    protected static final ReadObjectState IN_READ_OBJECT_DEFAULTS_SENT = new InReadObjectDefaultsSentState();
    protected static final ReadObjectState NO_READ_OBJECT_DEFAULTS_SENT = new NoReadObjectDefaultsSentState();
    protected static final ReadObjectState IN_READ_OBJECT_REMOTE_NOT_CUSTOM_MARSHALED = new InReadObjectRemoteDidNotUseWriteObjectState();
    protected static final ReadObjectState IN_READ_OBJECT_PAST_DEFAULTS_REMOTE_NOT_CUSTOM = new InReadObjectPastDefaultsRemoteDidNotUseWOState();

    abstract void defaultReadObjectDelegate();

    abstract void readFields(Map map) throws ClassNotFoundException, IOException;

    protected abstract byte getStreamFormatVersion();

    abstract InputStream getOrbStream();

    /* loaded from: rt.jar:com/sun/corba/se/impl/io/InputStreamHook$HookGetFields.class */
    private class HookGetFields extends ObjectInputStream.GetField {
        private Map fields;

        HookGetFields(Map map) {
            this.fields = null;
            this.fields = map;
        }

        @Override // java.io.ObjectInputStream.GetField
        public java.io.ObjectStreamClass getObjectStreamClass() {
            return null;
        }

        @Override // java.io.ObjectInputStream.GetField
        public boolean defaulted(String str) throws IOException, IllegalArgumentException {
            return !this.fields.containsKey(str);
        }

        @Override // java.io.ObjectInputStream.GetField
        public boolean get(String str, boolean z2) throws IOException, IllegalArgumentException {
            if (defaulted(str)) {
                return z2;
            }
            return ((Boolean) this.fields.get(str)).booleanValue();
        }

        @Override // java.io.ObjectInputStream.GetField
        public char get(String str, char c2) throws IOException, IllegalArgumentException {
            if (defaulted(str)) {
                return c2;
            }
            return ((Character) this.fields.get(str)).charValue();
        }

        @Override // java.io.ObjectInputStream.GetField
        public byte get(String str, byte b2) throws IOException, IllegalArgumentException {
            if (defaulted(str)) {
                return b2;
            }
            return ((Byte) this.fields.get(str)).byteValue();
        }

        @Override // java.io.ObjectInputStream.GetField
        public short get(String str, short s2) throws IOException, IllegalArgumentException {
            if (defaulted(str)) {
                return s2;
            }
            return ((Short) this.fields.get(str)).shortValue();
        }

        @Override // java.io.ObjectInputStream.GetField
        public int get(String str, int i2) throws IOException, IllegalArgumentException {
            if (defaulted(str)) {
                return i2;
            }
            return ((Integer) this.fields.get(str)).intValue();
        }

        @Override // java.io.ObjectInputStream.GetField
        public long get(String str, long j2) throws IOException, IllegalArgumentException {
            if (defaulted(str)) {
                return j2;
            }
            return ((Long) this.fields.get(str)).longValue();
        }

        @Override // java.io.ObjectInputStream.GetField
        public float get(String str, float f2) throws IOException, IllegalArgumentException {
            if (defaulted(str)) {
                return f2;
            }
            return ((Float) this.fields.get(str)).floatValue();
        }

        @Override // java.io.ObjectInputStream.GetField
        public double get(String str, double d2) throws IOException, IllegalArgumentException {
            if (defaulted(str)) {
                return d2;
            }
            return ((Double) this.fields.get(str)).doubleValue();
        }

        @Override // java.io.ObjectInputStream.GetField
        public Object get(String str, Object obj) throws IOException, IllegalArgumentException {
            if (defaulted(str)) {
                return obj;
            }
            return this.fields.get(str);
        }

        public String toString() {
            return this.fields.toString();
        }
    }

    @Override // java.io.ObjectInputStream
    public void defaultReadObject() throws IOException, ClassNotFoundException {
        this.readObjectState.beginDefaultReadObject(this);
        defaultReadObjectDelegate();
        this.readObjectState.endDefaultReadObject(this);
    }

    @Override // java.io.ObjectInputStream
    public ObjectInputStream.GetField readFields() throws ClassNotFoundException, IOException {
        HashMap map = new HashMap();
        readFields(map);
        this.readObjectState.endDefaultReadObject(this);
        return new HookGetFields(map);
    }

    protected void setState(ReadObjectState readObjectState) {
        this.readObjectState = readObjectState;
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/io/InputStreamHook$ReadObjectState.class */
    protected static class ReadObjectState {
        protected ReadObjectState() {
        }

        public void beginUnmarshalCustomValue(InputStreamHook inputStreamHook, boolean z2, boolean z3) throws IOException {
        }

        public void endUnmarshalCustomValue(InputStreamHook inputStreamHook) throws IOException {
        }

        public void beginDefaultReadObject(InputStreamHook inputStreamHook) throws IOException {
        }

        public void endDefaultReadObject(InputStreamHook inputStreamHook) throws IOException {
        }

        public void readData(InputStreamHook inputStreamHook) throws IOException {
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/io/InputStreamHook$DefaultState.class */
    protected static class DefaultState extends ReadObjectState {
        protected DefaultState() {
        }

        @Override // com.sun.corba.se.impl.io.InputStreamHook.ReadObjectState
        public void beginUnmarshalCustomValue(InputStreamHook inputStreamHook, boolean z2, boolean z3) throws IOException {
            if (z3) {
                if (z2) {
                    inputStreamHook.setState(InputStreamHook.IN_READ_OBJECT_DEFAULTS_SENT);
                    return;
                }
                try {
                    if (inputStreamHook.getStreamFormatVersion() == 2) {
                        ((ValueInputStream) inputStreamHook.getOrbStream()).start_value();
                    }
                } catch (Exception e2) {
                }
                inputStreamHook.setState(InputStreamHook.IN_READ_OBJECT_OPT_DATA);
                return;
            }
            if (z2) {
                inputStreamHook.setState(InputStreamHook.NO_READ_OBJECT_DEFAULTS_SENT);
                return;
            }
            throw new StreamCorruptedException("No default data sent");
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/io/InputStreamHook$InReadObjectRemoteDidNotUseWriteObjectState.class */
    protected static class InReadObjectRemoteDidNotUseWriteObjectState extends ReadObjectState {
        protected InReadObjectRemoteDidNotUseWriteObjectState() {
        }

        @Override // com.sun.corba.se.impl.io.InputStreamHook.ReadObjectState
        public void beginUnmarshalCustomValue(InputStreamHook inputStreamHook, boolean z2, boolean z3) {
            throw InputStreamHook.utilWrapper.badBeginUnmarshalCustomValue();
        }

        @Override // com.sun.corba.se.impl.io.InputStreamHook.ReadObjectState
        public void endDefaultReadObject(InputStreamHook inputStreamHook) {
            inputStreamHook.setState(InputStreamHook.IN_READ_OBJECT_PAST_DEFAULTS_REMOTE_NOT_CUSTOM);
        }

        @Override // com.sun.corba.se.impl.io.InputStreamHook.ReadObjectState
        public void readData(InputStreamHook inputStreamHook) {
            inputStreamHook.throwOptionalDataIncompatibleException();
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/io/InputStreamHook$InReadObjectPastDefaultsRemoteDidNotUseWOState.class */
    protected static class InReadObjectPastDefaultsRemoteDidNotUseWOState extends ReadObjectState {
        protected InReadObjectPastDefaultsRemoteDidNotUseWOState() {
        }

        @Override // com.sun.corba.se.impl.io.InputStreamHook.ReadObjectState
        public void beginUnmarshalCustomValue(InputStreamHook inputStreamHook, boolean z2, boolean z3) {
            throw InputStreamHook.utilWrapper.badBeginUnmarshalCustomValue();
        }

        @Override // com.sun.corba.se.impl.io.InputStreamHook.ReadObjectState
        public void beginDefaultReadObject(InputStreamHook inputStreamHook) throws IOException {
            throw new StreamCorruptedException("Default data already read");
        }

        @Override // com.sun.corba.se.impl.io.InputStreamHook.ReadObjectState
        public void readData(InputStreamHook inputStreamHook) {
            inputStreamHook.throwOptionalDataIncompatibleException();
        }
    }

    protected void throwOptionalDataIncompatibleException() {
        throw omgWrapper.rmiiiopOptionalDataIncompatible2();
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/io/InputStreamHook$InReadObjectDefaultsSentState.class */
    protected static class InReadObjectDefaultsSentState extends ReadObjectState {
        protected InReadObjectDefaultsSentState() {
        }

        @Override // com.sun.corba.se.impl.io.InputStreamHook.ReadObjectState
        public void beginUnmarshalCustomValue(InputStreamHook inputStreamHook, boolean z2, boolean z3) {
            throw InputStreamHook.utilWrapper.badBeginUnmarshalCustomValue();
        }

        @Override // com.sun.corba.se.impl.io.InputStreamHook.ReadObjectState
        public void endUnmarshalCustomValue(InputStreamHook inputStreamHook) {
            if (inputStreamHook.getStreamFormatVersion() == 2) {
                ((ValueInputStream) inputStreamHook.getOrbStream()).start_value();
                ((ValueInputStream) inputStreamHook.getOrbStream()).end_value();
            }
            inputStreamHook.setState(InputStreamHook.DEFAULT_STATE);
        }

        @Override // com.sun.corba.se.impl.io.InputStreamHook.ReadObjectState
        public void endDefaultReadObject(InputStreamHook inputStreamHook) throws IOException {
            if (inputStreamHook.getStreamFormatVersion() == 2) {
                ((ValueInputStream) inputStreamHook.getOrbStream()).start_value();
            }
            inputStreamHook.setState(InputStreamHook.IN_READ_OBJECT_OPT_DATA);
        }

        @Override // com.sun.corba.se.impl.io.InputStreamHook.ReadObjectState
        public void readData(InputStreamHook inputStreamHook) throws IOException {
            ORB orb = inputStreamHook.getOrbStream().orb();
            if (orb == null || !(orb instanceof com.sun.corba.se.spi.orb.ORB)) {
                throw new StreamCorruptedException("Default data must be read first");
            }
            ORBVersion oRBVersion = ((com.sun.corba.se.spi.orb.ORB) orb).getORBVersion();
            if (ORBVersionFactory.getPEORB().compareTo(oRBVersion) <= 0 || oRBVersion.equals(ORBVersionFactory.getFOREIGN())) {
                throw new StreamCorruptedException("Default data must be read first");
            }
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/io/InputStreamHook$InReadObjectOptionalDataState.class */
    protected static class InReadObjectOptionalDataState extends ReadObjectState {
        protected InReadObjectOptionalDataState() {
        }

        @Override // com.sun.corba.se.impl.io.InputStreamHook.ReadObjectState
        public void beginUnmarshalCustomValue(InputStreamHook inputStreamHook, boolean z2, boolean z3) {
            throw InputStreamHook.utilWrapper.badBeginUnmarshalCustomValue();
        }

        @Override // com.sun.corba.se.impl.io.InputStreamHook.ReadObjectState
        public void endUnmarshalCustomValue(InputStreamHook inputStreamHook) throws IOException {
            if (inputStreamHook.getStreamFormatVersion() == 2) {
                ((ValueInputStream) inputStreamHook.getOrbStream()).end_value();
            }
            inputStreamHook.setState(InputStreamHook.DEFAULT_STATE);
        }

        @Override // com.sun.corba.se.impl.io.InputStreamHook.ReadObjectState
        public void beginDefaultReadObject(InputStreamHook inputStreamHook) throws IOException {
            throw new StreamCorruptedException("Default data not sent or already read/passed");
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/io/InputStreamHook$InReadObjectNoMoreOptionalDataState.class */
    protected static class InReadObjectNoMoreOptionalDataState extends InReadObjectOptionalDataState {
        protected InReadObjectNoMoreOptionalDataState() {
        }

        @Override // com.sun.corba.se.impl.io.InputStreamHook.ReadObjectState
        public void readData(InputStreamHook inputStreamHook) throws IOException {
            inputStreamHook.throwOptionalDataIncompatibleException();
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/io/InputStreamHook$NoReadObjectDefaultsSentState.class */
    protected static class NoReadObjectDefaultsSentState extends ReadObjectState {
        protected NoReadObjectDefaultsSentState() {
        }

        @Override // com.sun.corba.se.impl.io.InputStreamHook.ReadObjectState
        public void endUnmarshalCustomValue(InputStreamHook inputStreamHook) throws IOException {
            if (inputStreamHook.getStreamFormatVersion() == 2) {
                ((ValueInputStream) inputStreamHook.getOrbStream()).start_value();
                ((ValueInputStream) inputStreamHook.getOrbStream()).end_value();
            }
            inputStreamHook.setState(InputStreamHook.DEFAULT_STATE);
        }
    }
}
