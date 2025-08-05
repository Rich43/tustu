package com.sun.corba.se.impl.ior.iiop;

import com.sun.corba.se.impl.encoding.CodeSetComponentInfo;
import com.sun.corba.se.impl.encoding.MarshalInputStream;
import com.sun.corba.se.impl.encoding.MarshalOutputStream;
import com.sun.corba.se.spi.ior.TaggedComponentBase;
import com.sun.corba.se.spi.ior.iiop.CodeSetsComponent;
import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/iiop/CodeSetsComponentImpl.class */
public class CodeSetsComponentImpl extends TaggedComponentBase implements CodeSetsComponent {
    CodeSetComponentInfo csci;

    public boolean equals(Object obj) {
        if (!(obj instanceof CodeSetsComponentImpl)) {
            return false;
        }
        return this.csci.equals(((CodeSetsComponentImpl) obj).csci);
    }

    public int hashCode() {
        return this.csci.hashCode();
    }

    public String toString() {
        return "CodeSetsComponentImpl[csci=" + ((Object) this.csci) + "]";
    }

    public CodeSetsComponentImpl() {
        this.csci = new CodeSetComponentInfo();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public CodeSetsComponentImpl(InputStream inputStream) {
        this.csci = new CodeSetComponentInfo();
        this.csci.read((MarshalInputStream) inputStream);
    }

    public CodeSetsComponentImpl(ORB orb) {
        if (orb == null) {
            this.csci = new CodeSetComponentInfo();
        } else {
            this.csci = orb.getORBData().getCodeSetComponentInfo();
        }
    }

    @Override // com.sun.corba.se.spi.ior.iiop.CodeSetsComponent
    public CodeSetComponentInfo getCodeSetComponentInfo() {
        return this.csci;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.corba.se.spi.ior.WriteContents
    public void writeContents(OutputStream outputStream) {
        this.csci.write((MarshalOutputStream) outputStream);
    }

    @Override // com.sun.corba.se.spi.ior.Identifiable
    public int getId() {
        return 1;
    }
}
