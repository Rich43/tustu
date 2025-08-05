package com.sun.corba.se.impl.naming.namingutil;

import com.sun.corba.se.impl.orbutil.ORBConstants;
import java.util.ArrayList;
import java.util.List;

/* loaded from: rt.jar:com/sun/corba/se/impl/naming/namingutil/INSURLBase.class */
public abstract class INSURLBase implements INSURL {
    protected boolean rirFlag = false;
    protected ArrayList theEndpointInfo = null;
    protected String theKeyString = ORBConstants.PERSISTENT_NAME_SERVICE_NAME;
    protected String theStringifiedName = null;

    @Override // com.sun.corba.se.impl.naming.namingutil.INSURL
    public abstract boolean isCorbanameURL();

    @Override // com.sun.corba.se.impl.naming.namingutil.INSURL
    public boolean getRIRFlag() {
        return this.rirFlag;
    }

    @Override // com.sun.corba.se.impl.naming.namingutil.INSURL
    public List getEndpointInfo() {
        return this.theEndpointInfo;
    }

    @Override // com.sun.corba.se.impl.naming.namingutil.INSURL
    public String getKeyString() {
        return this.theKeyString;
    }

    @Override // com.sun.corba.se.impl.naming.namingutil.INSURL
    public String getStringifiedName() {
        return this.theStringifiedName;
    }

    @Override // com.sun.corba.se.impl.naming.namingutil.INSURL
    public void dPrint() {
        System.out.println("URL Dump...");
        System.out.println("Key String = " + getKeyString());
        System.out.println("RIR Flag = " + getRIRFlag());
        System.out.println("isCorbanameURL = " + isCorbanameURL());
        for (int i2 = 0; i2 < this.theEndpointInfo.size(); i2++) {
            ((IIOPEndpointInfo) this.theEndpointInfo.get(i2)).dump();
        }
        if (isCorbanameURL()) {
            System.out.println("Stringified Name = " + getStringifiedName());
        }
    }
}
