package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.encoding.CodeSetComponentInfo;
import com.sun.corba.se.impl.encoding.CodeSetConversion;
import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry;
import com.sun.corba.se.impl.logging.OMGSystemException;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import com.sun.corba.se.pept.encoding.InputObject;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.transport.CorbaConnection;
import com.sun.org.omg.SendingContext.CodeBase;
import java.nio.ByteBuffer;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CDRInputObject.class */
public class CDRInputObject extends CDRInputStream implements InputObject {
    private CorbaConnection corbaConnection;
    private Message header;
    private boolean unmarshaledHeader;
    private ORB orb;
    private ORBUtilSystemException wrapper;
    private OMGSystemException omgWrapper;

    public CDRInputObject(ORB orb, CorbaConnection corbaConnection, ByteBuffer byteBuffer, Message message) {
        super(orb, byteBuffer, message.getSize(), message.isLittleEndian(), message.getGIOPVersion(), message.getEncodingVersion(), BufferManagerFactory.newBufferManagerRead(message.getGIOPVersion(), message.getEncodingVersion(), orb));
        this.corbaConnection = corbaConnection;
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_ENCODING);
        this.omgWrapper = OMGSystemException.get(orb, CORBALogDomains.RPC_ENCODING);
        if (orb.transportDebugFlag) {
            dprint(".CDRInputObject constructor:");
        }
        getBufferManager().init(message);
        this.header = message;
        this.unmarshaledHeader = false;
        setIndex(12);
        setBufferLength(message.getSize());
    }

    public final CorbaConnection getConnection() {
        return this.corbaConnection;
    }

    public Message getMessageHeader() {
        return this.header;
    }

    public void unmarshalHeader() {
        if (!this.unmarshaledHeader) {
            try {
                try {
                    if (((ORB) orb()).transportDebugFlag) {
                        dprint(".unmarshalHeader->: " + ((Object) getMessageHeader()));
                    }
                    getMessageHeader().read(this);
                    this.unmarshaledHeader = true;
                    if (((ORB) orb()).transportDebugFlag) {
                        dprint(".unmarshalHeader<-: " + ((Object) getMessageHeader()));
                    }
                } catch (RuntimeException e2) {
                    if (((ORB) orb()).transportDebugFlag) {
                        dprint(".unmarshalHeader: !!ERROR!!: " + ((Object) getMessageHeader()) + ": " + ((Object) e2));
                    }
                    throw e2;
                }
            } catch (Throwable th) {
                if (((ORB) orb()).transportDebugFlag) {
                    dprint(".unmarshalHeader<-: " + ((Object) getMessageHeader()));
                }
                throw th;
            }
        }
    }

    public final boolean unmarshaledHeader() {
        return this.unmarshaledHeader;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStream
    protected CodeSetConversion.BTCConverter createCharBTCConverter() {
        CodeSetComponentInfo.CodeSetContext codeSets = getCodeSets();
        if (codeSets == null) {
            return super.createCharBTCConverter();
        }
        OSFCodeSetRegistry.Entry entryLookupEntry = OSFCodeSetRegistry.lookupEntry(codeSets.getCharCodeSet());
        if (entryLookupEntry == null) {
            throw this.wrapper.unknownCodeset(entryLookupEntry);
        }
        return CodeSetConversion.impl().getBTCConverter(entryLookupEntry, isLittleEndian());
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStream
    protected CodeSetConversion.BTCConverter createWCharBTCConverter() {
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
        if (entryLookupEntry == OSFCodeSetRegistry.UTF_16 && getGIOPVersion().equals(GIOPVersion.V1_2)) {
            return CodeSetConversion.impl().getBTCConverter(entryLookupEntry, false);
        }
        return CodeSetConversion.impl().getBTCConverter(entryLookupEntry, isLittleEndian());
    }

    private CodeSetComponentInfo.CodeSetContext getCodeSets() {
        if (getConnection() == null) {
            return CodeSetComponentInfo.LOCAL_CODE_SETS;
        }
        return getConnection().getCodeSetContext();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStream
    public final CodeBase getCodeBase() {
        if (getConnection() == null) {
            return null;
        }
        return getConnection().getCodeBase();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStream
    public CDRInputStream dup() {
        return null;
    }

    protected void dprint(String str) {
        ORBUtility.dprint("CDRInputObject", str);
    }
}
