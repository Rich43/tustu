package com.sun.corba.se.impl.orb;

import com.sun.corba.se.impl.encoding.CodeSetComponentInfo;
import com.sun.corba.se.impl.legacy.connection.USLPort;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.pept.transport.Acceptor;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.legacy.connection.ORBSocketFactory;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.DataCollector;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orb.ORBData;
import com.sun.corba.se.spi.orb.ParserImplTableBase;
import com.sun.corba.se.spi.orb.StringPair;
import com.sun.corba.se.spi.transport.CorbaContactInfoListFactory;
import com.sun.corba.se.spi.transport.IIOPPrimaryToContactInfo;
import com.sun.corba.se.spi.transport.IORToSocketInfo;
import com.sun.corba.se.spi.transport.ReadTimeouts;
import java.net.URL;
import org.omg.CORBA.CompletionStatus;
import org.omg.PortableInterceptor.ORBInitializer;

/* loaded from: rt.jar:com/sun/corba/se/impl/orb/ORBDataParserImpl.class */
public class ORBDataParserImpl extends ParserImplTableBase implements ORBData {
    private ORB orb;
    private ORBUtilSystemException wrapper;
    private String ORBInitialHost;
    private int ORBInitialPort;
    private String ORBServerHost;
    private int ORBServerPort;
    private String listenOnAllInterfaces;
    private ORBSocketFactory legacySocketFactory;
    private com.sun.corba.se.spi.transport.ORBSocketFactory socketFactory;
    private USLPort[] userSpecifiedListenPorts;
    private IORToSocketInfo iorToSocketInfo;
    private IIOPPrimaryToContactInfo iiopPrimaryToContactInfo;
    private String orbId;
    private boolean orbServerIdPropertySpecified;
    private URL servicesURL;
    private String propertyInitRef;
    private boolean allowLocalOptimization;
    private GIOPVersion giopVersion;
    private int highWaterMark;
    private int lowWaterMark;
    private int numberToReclaim;
    private int giopFragmentSize;
    private int giopBufferSize;
    private int giop11BuffMgr;
    private int giop12BuffMgr;
    private short giopTargetAddressPreference;
    private short giopAddressDisposition;
    private boolean useByteOrderMarkers;
    private boolean useByteOrderMarkersInEncaps;
    private boolean alwaysSendCodeSetCtx;
    private boolean persistentPortInitialized;
    private int persistentServerPort;
    private boolean persistentServerIdInitialized;
    private int persistentServerId;
    private boolean serverIsORBActivated;
    private Class badServerIdHandlerClass;
    private CodeSetComponentInfo.CodeSetComponent charData;
    private CodeSetComponentInfo.CodeSetComponent wcharData;
    private ORBInitializer[] orbInitializers;
    private StringPair[] orbInitialReferences;
    private String defaultInitRef;
    private String[] debugFlags;
    private Acceptor[] acceptors;
    private CorbaContactInfoListFactory corbaContactInfoListFactory;
    private String acceptorSocketType;
    private boolean acceptorSocketUseSelectThreadToWait;
    private boolean acceptorSocketUseWorkerThreadForEvent;
    private String connectionSocketType;
    private boolean connectionSocketUseSelectThreadToWait;
    private boolean connectionSocketUseWorkerThreadForEvent;
    private ReadTimeouts readTimeouts;
    private boolean disableDirectByteBufferUse;
    private boolean enableJavaSerialization;
    private boolean useRepId;
    private CodeSetComponentInfo codesets;

    @Override // com.sun.corba.se.spi.orb.ORBData
    public String getORBInitialHost() {
        return this.ORBInitialHost;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public int getORBInitialPort() {
        return this.ORBInitialPort;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public String getORBServerHost() {
        return this.ORBServerHost;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public String getListenOnAllInterfaces() {
        return this.listenOnAllInterfaces;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public int getORBServerPort() {
        return this.ORBServerPort;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public ORBSocketFactory getLegacySocketFactory() {
        return this.legacySocketFactory;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public com.sun.corba.se.spi.transport.ORBSocketFactory getSocketFactory() {
        return this.socketFactory;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public USLPort[] getUserSpecifiedListenPorts() {
        return this.userSpecifiedListenPorts;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public IORToSocketInfo getIORToSocketInfo() {
        return this.iorToSocketInfo;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public IIOPPrimaryToContactInfo getIIOPPrimaryToContactInfo() {
        return this.iiopPrimaryToContactInfo;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public String getORBId() {
        return this.orbId;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public boolean getORBServerIdPropertySpecified() {
        return this.orbServerIdPropertySpecified;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public boolean isLocalOptimizationAllowed() {
        return this.allowLocalOptimization;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public GIOPVersion getGIOPVersion() {
        return this.giopVersion;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public int getHighWaterMark() {
        return this.highWaterMark;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public int getLowWaterMark() {
        return this.lowWaterMark;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public int getNumberToReclaim() {
        return this.numberToReclaim;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public int getGIOPFragmentSize() {
        return this.giopFragmentSize;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public int getGIOPBufferSize() {
        return this.giopBufferSize;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public int getGIOPBuffMgrStrategy(GIOPVersion gIOPVersion) {
        if (gIOPVersion == null || gIOPVersion.equals(GIOPVersion.V1_0)) {
            return 0;
        }
        if (gIOPVersion.equals(GIOPVersion.V1_1)) {
            return this.giop11BuffMgr;
        }
        if (gIOPVersion.equals(GIOPVersion.V1_2)) {
            return this.giop12BuffMgr;
        }
        return 0;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public short getGIOPTargetAddressPreference() {
        return this.giopTargetAddressPreference;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public short getGIOPAddressDisposition() {
        return this.giopAddressDisposition;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public boolean useByteOrderMarkers() {
        return this.useByteOrderMarkers;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public boolean useByteOrderMarkersInEncapsulations() {
        return this.useByteOrderMarkersInEncaps;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public boolean alwaysSendCodeSetServiceContext() {
        return this.alwaysSendCodeSetCtx;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public boolean getPersistentPortInitialized() {
        return this.persistentPortInitialized;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public int getPersistentServerPort() {
        if (this.persistentPortInitialized) {
            return this.persistentServerPort;
        }
        throw this.wrapper.persistentServerportNotSet(CompletionStatus.COMPLETED_MAYBE);
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public boolean getPersistentServerIdInitialized() {
        return this.persistentServerIdInitialized;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public int getPersistentServerId() {
        if (this.persistentServerIdInitialized) {
            return this.persistentServerId;
        }
        throw this.wrapper.persistentServeridNotSet(CompletionStatus.COMPLETED_MAYBE);
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public boolean getServerIsORBActivated() {
        return this.serverIsORBActivated;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public Class getBadServerIdHandler() {
        return this.badServerIdHandlerClass;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public CodeSetComponentInfo getCodeSetComponentInfo() {
        return this.codesets;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public ORBInitializer[] getORBInitializers() {
        return this.orbInitializers;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public StringPair[] getORBInitialReferences() {
        return this.orbInitialReferences;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public String getORBDefaultInitialReference() {
        return this.defaultInitRef;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public String[] getORBDebugFlags() {
        return this.debugFlags;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public Acceptor[] getAcceptors() {
        return this.acceptors;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public CorbaContactInfoListFactory getCorbaContactInfoListFactory() {
        return this.corbaContactInfoListFactory;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public String acceptorSocketType() {
        return this.acceptorSocketType;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public boolean acceptorSocketUseSelectThreadToWait() {
        return this.acceptorSocketUseSelectThreadToWait;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public boolean acceptorSocketUseWorkerThreadForEvent() {
        return this.acceptorSocketUseWorkerThreadForEvent;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public String connectionSocketType() {
        return this.connectionSocketType;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public boolean connectionSocketUseSelectThreadToWait() {
        return this.connectionSocketUseSelectThreadToWait;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public boolean connectionSocketUseWorkerThreadForEvent() {
        return this.connectionSocketUseWorkerThreadForEvent;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public boolean isJavaSerializationEnabled() {
        return this.enableJavaSerialization;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public ReadTimeouts getTransportTCPReadTimeouts() {
        return this.readTimeouts;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public boolean disableDirectByteBufferUse() {
        return this.disableDirectByteBufferUse;
    }

    @Override // com.sun.corba.se.spi.orb.ORBData
    public boolean useRepId() {
        return this.useRepId;
    }

    public ORBDataParserImpl(ORB orb, DataCollector dataCollector) {
        super(ParserTable.get().getParserData());
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.ORB_LIFECYCLE);
        init(dataCollector);
        complete();
    }

    @Override // com.sun.corba.se.spi.orb.ParserImplBase
    public void complete() {
        this.codesets = new CodeSetComponentInfo(this.charData, this.wcharData);
    }
}
