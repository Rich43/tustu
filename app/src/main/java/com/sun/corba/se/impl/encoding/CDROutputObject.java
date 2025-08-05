package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.encoding.CodeSetComponentInfo;
import com.sun.corba.se.impl.encoding.CodeSetConversion;
import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry;
import com.sun.corba.se.impl.logging.OMGSystemException;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import com.sun.corba.se.pept.protocol.MessageMediator;
import com.sun.corba.se.spi.encoding.CorbaOutputObject;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
import com.sun.corba.se.spi.transport.CorbaConnection;
import java.io.IOException;
import org.omg.CORBA.portable.InputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CDROutputObject.class */
public class CDROutputObject extends CorbaOutputObject {
    private Message header;
    private ORB orb;
    private ORBUtilSystemException wrapper;
    private OMGSystemException omgWrapper;
    private CorbaConnection connection;

    private CDROutputObject(ORB orb, GIOPVersion gIOPVersion, Message message, BufferManagerWrite bufferManagerWrite, byte b2, CorbaMessageMediator corbaMessageMediator) {
        super(orb, gIOPVersion, message.getEncodingVersion(), false, bufferManagerWrite, b2, (corbaMessageMediator == null || corbaMessageMediator.getConnection() == null) ? false : ((CorbaConnection) corbaMessageMediator.getConnection()).shouldUseDirectByteBuffers());
        this.header = message;
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_ENCODING);
        this.omgWrapper = OMGSystemException.get(orb, CORBALogDomains.RPC_ENCODING);
        getBufferManager().setOutputObject(this);
        this.corbaMessageMediator = corbaMessageMediator;
    }

    public CDROutputObject(ORB orb, MessageMediator messageMediator, Message message, byte b2) {
        this(orb, ((CorbaMessageMediator) messageMediator).getGIOPVersion(), message, BufferManagerFactory.newBufferManagerWrite(((CorbaMessageMediator) messageMediator).getGIOPVersion(), message.getEncodingVersion(), orb), b2, (CorbaMessageMediator) messageMediator);
    }

    public CDROutputObject(ORB orb, MessageMediator messageMediator, Message message, byte b2, int i2) {
        this(orb, ((CorbaMessageMediator) messageMediator).getGIOPVersion(), message, BufferManagerFactory.newBufferManagerWrite(i2, message.getEncodingVersion(), orb), b2, (CorbaMessageMediator) messageMediator);
    }

    public CDROutputObject(ORB orb, CorbaMessageMediator corbaMessageMediator, GIOPVersion gIOPVersion, CorbaConnection corbaConnection, Message message, byte b2) {
        this(orb, gIOPVersion, message, BufferManagerFactory.newBufferManagerWrite(gIOPVersion, message.getEncodingVersion(), orb), b2, corbaMessageMediator);
        this.connection = corbaConnection;
    }

    public Message getMessageHeader() {
        return this.header;
    }

    public final void finishSendingMessage() {
        getBufferManager().sendMessage();
    }

    @Override // com.sun.corba.se.spi.encoding.CorbaOutputObject
    public void writeTo(CorbaConnection corbaConnection) throws IOException {
        ByteBufferWithInfo byteBufferWithInfo = getByteBufferWithInfo();
        getMessageHeader().setSize(byteBufferWithInfo.byteBuffer, byteBufferWithInfo.getSize());
        if (orb() != null) {
            if (((ORB) orb()).transportDebugFlag) {
                dprint(".writeTo: " + ((Object) corbaConnection));
            }
            if (((ORB) orb()).giopDebugFlag) {
                CDROutputStream_1_0.printBuffer(byteBufferWithInfo);
            }
        }
        byteBufferWithInfo.byteBuffer.position(0).limit(byteBufferWithInfo.getSize());
        corbaConnection.write(byteBufferWithInfo.byteBuffer);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStream, org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream
    public InputStream create_input_stream() {
        return null;
    }

    public CorbaConnection getConnection() {
        if (this.connection != null) {
            return this.connection;
        }
        return (CorbaConnection) this.corbaMessageMediator.getConnection();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStream
    public final ByteBufferWithInfo getByteBufferWithInfo() {
        return super.getByteBufferWithInfo();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStream
    public final void setByteBufferWithInfo(ByteBufferWithInfo byteBufferWithInfo) {
        super.setByteBufferWithInfo(byteBufferWithInfo);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStream
    protected CodeSetConversion.CTBConverter createCharCTBConverter() {
        CodeSetComponentInfo.CodeSetContext codeSets = getCodeSets();
        if (codeSets == null) {
            return super.createCharCTBConverter();
        }
        OSFCodeSetRegistry.Entry entryLookupEntry = OSFCodeSetRegistry.lookupEntry(codeSets.getCharCodeSet());
        if (entryLookupEntry == null) {
            throw this.wrapper.unknownCodeset(entryLookupEntry);
        }
        return CodeSetConversion.impl().getCTBConverter(entryLookupEntry, isLittleEndian(), false);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStream
    protected CodeSetConversion.CTBConverter createWCharCTBConverter() {
        CodeSetComponentInfo.CodeSetContext codeSets = getCodeSets();
        if (codeSets == null) {
            if (getConnection().isServer()) {
                throw this.omgWrapper.noClientWcharCodesetCtx();
            }
            throw this.omgWrapper.noServerWcharCodesetCmp();
        }
        OSFCodeSetRegistry.Entry entryLookupEntry = OSFCodeSetRegistry.lookupEntry(codeSets.getWCharCodeSet());
        if (entryLookupEntry == null) {
            throw this.wrapper.unknownCodeset(entryLookupEntry);
        }
        boolean zUseByteOrderMarkers = ((ORB) orb()).getORBData().useByteOrderMarkers();
        if (entryLookupEntry == OSFCodeSetRegistry.UTF_16) {
            if (getGIOPVersion().equals(GIOPVersion.V1_2)) {
                return CodeSetConversion.impl().getCTBConverter(entryLookupEntry, false, zUseByteOrderMarkers);
            }
            if (getGIOPVersion().equals(GIOPVersion.V1_1)) {
                return CodeSetConversion.impl().getCTBConverter(entryLookupEntry, isLittleEndian(), false);
            }
        }
        return CodeSetConversion.impl().getCTBConverter(entryLookupEntry, isLittleEndian(), zUseByteOrderMarkers);
    }

    private CodeSetComponentInfo.CodeSetContext getCodeSets() {
        if (getConnection() == null) {
            return CodeSetComponentInfo.LOCAL_CODE_SETS;
        }
        return getConnection().getCodeSetContext();
    }

    protected void dprint(String str) {
        ORBUtility.dprint("CDROutputObject", str);
    }
}
