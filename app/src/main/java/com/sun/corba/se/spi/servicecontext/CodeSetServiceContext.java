package com.sun.corba.se.spi.servicecontext;

import com.sun.corba.se.impl.encoding.CodeSetComponentInfo;
import com.sun.corba.se.impl.encoding.MarshalInputStream;
import com.sun.corba.se.impl.encoding.MarshalOutputStream;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import org.omg.CORBA.SystemException;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/servicecontext/CodeSetServiceContext.class */
public class CodeSetServiceContext extends ServiceContext {
    public static final int SERVICE_CONTEXT_ID = 1;
    private CodeSetComponentInfo.CodeSetContext csc;

    public CodeSetServiceContext(CodeSetComponentInfo.CodeSetContext codeSetContext) {
        this.csc = codeSetContext;
    }

    public CodeSetServiceContext(InputStream inputStream, GIOPVersion gIOPVersion) {
        super(inputStream, gIOPVersion);
        this.csc = new CodeSetComponentInfo.CodeSetContext();
        this.csc.read((MarshalInputStream) this.in);
    }

    @Override // com.sun.corba.se.spi.servicecontext.ServiceContext
    public int getId() {
        return 1;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.corba.se.spi.servicecontext.ServiceContext
    public void writeData(OutputStream outputStream) throws SystemException {
        this.csc.write((MarshalOutputStream) outputStream);
    }

    public CodeSetComponentInfo.CodeSetContext getCodeSetContext() {
        return this.csc;
    }

    @Override // com.sun.corba.se.spi.servicecontext.ServiceContext
    public String toString() {
        return "CodeSetServiceContext[ csc=" + ((Object) this.csc) + " ]";
    }
}
