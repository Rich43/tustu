package com.sun.corba.se.spi.servicecontext;

import com.sun.corba.se.impl.encoding.CDRInputStream;
import com.sun.corba.se.impl.encoding.EncapsInputStream;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.impl.util.Utility;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.org.omg.SendingContext.CodeBase;
import java.util.HashMap;
import java.util.Map;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.OctetSeqHelper;
import org.omg.CORBA.SystemException;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;
import sun.corba.EncapsInputStreamFactory;

/* loaded from: rt.jar:com/sun/corba/se/spi/servicecontext/ServiceContexts.class */
public class ServiceContexts {
    private static final int JAVAIDL_ALIGN_SERVICE_ID = -1106033203;
    private ORB orb;
    private Map scMap;
    private boolean addAlignmentOnWrite;
    private CodeBase codeBase;
    private GIOPVersion giopVersion;
    private ORBUtilSystemException wrapper;

    private static boolean isDebugging(OutputStream outputStream) {
        ORB orb = (ORB) outputStream.orb();
        if (orb == null) {
            return false;
        }
        return orb.serviceContextDebugFlag;
    }

    private static boolean isDebugging(InputStream inputStream) {
        ORB orb = (ORB) inputStream.orb();
        if (orb == null) {
            return false;
        }
        return orb.serviceContextDebugFlag;
    }

    private void dprint(String str) {
        ORBUtility.dprint(this, str);
    }

    public static void writeNullServiceContext(OutputStream outputStream) {
        if (isDebugging(outputStream)) {
            ORBUtility.dprint("ServiceContexts", "Writing null service context");
        }
        outputStream.write_long(0);
    }

    private void createMapFromInputStream(InputStream inputStream) {
        this.orb = (ORB) inputStream.orb();
        if (this.orb.serviceContextDebugFlag) {
            dprint("Constructing ServiceContexts from input stream");
        }
        int i2 = inputStream.read_long();
        if (this.orb.serviceContextDebugFlag) {
            dprint("Number of service contexts = " + i2);
        }
        for (int i3 = 0; i3 < i2; i3++) {
            int i4 = inputStream.read_long();
            if (this.orb.serviceContextDebugFlag) {
                dprint("Reading service context id " + i4);
            }
            byte[] bArr = OctetSeqHelper.read(inputStream);
            if (this.orb.serviceContextDebugFlag) {
                dprint("Service context" + i4 + " length: " + bArr.length);
            }
            this.scMap.put(new Integer(i4), bArr);
        }
    }

    public ServiceContexts(ORB orb) {
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
        this.addAlignmentOnWrite = false;
        this.scMap = new HashMap();
        this.giopVersion = orb.getORBData().getGIOPVersion();
        this.codeBase = null;
    }

    public ServiceContexts(InputStream inputStream) {
        this((ORB) inputStream.orb());
        this.codeBase = ((CDRInputStream) inputStream).getCodeBase();
        createMapFromInputStream(inputStream);
        this.giopVersion = ((CDRInputStream) inputStream).getGIOPVersion();
    }

    private ServiceContext unmarshal(Integer num, byte[] bArr) {
        ServiceContext serviceContextMakeServiceContext;
        ServiceContextData serviceContextDataFindServiceContextData = this.orb.getServiceContextRegistry().findServiceContextData(num.intValue());
        if (serviceContextDataFindServiceContextData == null) {
            if (this.orb.serviceContextDebugFlag) {
                dprint("Could not find ServiceContextData for " + ((Object) num) + " using UnknownServiceContext");
            }
            serviceContextMakeServiceContext = new UnknownServiceContext(num.intValue(), bArr);
        } else {
            if (this.orb.serviceContextDebugFlag) {
                dprint("Found " + ((Object) serviceContextDataFindServiceContextData));
            }
            EncapsInputStream encapsInputStreamNewEncapsInputStream = EncapsInputStreamFactory.newEncapsInputStream(this.orb, bArr, bArr.length, this.giopVersion, this.codeBase);
            encapsInputStreamNewEncapsInputStream.consumeEndian();
            serviceContextMakeServiceContext = serviceContextDataFindServiceContextData.makeServiceContext(encapsInputStreamNewEncapsInputStream, this.giopVersion);
            if (serviceContextMakeServiceContext == null) {
                throw this.wrapper.svcctxUnmarshalError(CompletionStatus.COMPLETED_MAYBE);
            }
        }
        return serviceContextMakeServiceContext;
    }

    public void addAlignmentPadding() {
        this.addAlignmentOnWrite = true;
    }

    public void write(OutputStream outputStream, GIOPVersion gIOPVersion) throws SystemException {
        if (isDebugging(outputStream)) {
            dprint("Writing service contexts to output stream");
            Utility.printStackTrace();
        }
        int size = this.scMap.size();
        if (this.addAlignmentOnWrite) {
            if (isDebugging(outputStream)) {
                dprint("Adding alignment padding");
            }
            size++;
        }
        if (isDebugging(outputStream)) {
            dprint("Service context has " + size + " components");
        }
        outputStream.write_long(size);
        writeServiceContextsInOrder(outputStream, gIOPVersion);
        if (this.addAlignmentOnWrite) {
            if (isDebugging(outputStream)) {
                dprint("Writing alignment padding");
            }
            outputStream.write_long(JAVAIDL_ALIGN_SERVICE_ID);
            outputStream.write_long(4);
            outputStream.write_octet((byte) 0);
            outputStream.write_octet((byte) 0);
            outputStream.write_octet((byte) 0);
            outputStream.write_octet((byte) 0);
        }
        if (isDebugging(outputStream)) {
            dprint("Service context writing complete");
        }
    }

    private void writeServiceContextsInOrder(OutputStream outputStream, GIOPVersion gIOPVersion) throws SystemException {
        Integer num = new Integer(9);
        Object objRemove = this.scMap.remove(num);
        for (Integer num2 : this.scMap.keySet()) {
            writeMapEntry(outputStream, num2, this.scMap.get(num2), gIOPVersion);
        }
        if (objRemove != null) {
            writeMapEntry(outputStream, num, objRemove, gIOPVersion);
            this.scMap.put(num, objRemove);
        }
    }

    private void writeMapEntry(OutputStream outputStream, Integer num, Object obj, GIOPVersion gIOPVersion) throws SystemException {
        if (obj instanceof byte[]) {
            if (isDebugging(outputStream)) {
                dprint("Writing service context bytes for id " + ((Object) num));
            }
            OctetSeqHelper.write(outputStream, (byte[]) obj);
        } else {
            ServiceContext serviceContext = (ServiceContext) obj;
            if (isDebugging(outputStream)) {
                dprint("Writing service context " + ((Object) serviceContext));
            }
            serviceContext.write(outputStream, gIOPVersion);
        }
    }

    public void put(ServiceContext serviceContext) {
        this.scMap.put(new Integer(serviceContext.getId()), serviceContext);
    }

    public void delete(int i2) {
        delete(new Integer(i2));
    }

    public void delete(Integer num) {
        this.scMap.remove(num);
    }

    public ServiceContext get(int i2) {
        return get(new Integer(i2));
    }

    public ServiceContext get(Integer num) {
        Object obj = this.scMap.get(num);
        if (obj == null) {
            return null;
        }
        if (obj instanceof byte[]) {
            ServiceContext serviceContextUnmarshal = unmarshal(num, (byte[]) obj);
            this.scMap.put(num, serviceContextUnmarshal);
            return serviceContextUnmarshal;
        }
        return (ServiceContext) obj;
    }
}
