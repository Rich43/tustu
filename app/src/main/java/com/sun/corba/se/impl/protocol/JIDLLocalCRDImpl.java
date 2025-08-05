package com.sun.corba.se.impl.protocol;

import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.orb.ORB;
import javax.rmi.CORBA.Tie;
import org.omg.CORBA.Object;
import org.omg.CORBA.portable.ServantObject;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/JIDLLocalCRDImpl.class */
public class JIDLLocalCRDImpl extends LocalClientRequestDispatcherBase {
    protected ServantObject servant;

    public JIDLLocalCRDImpl(ORB orb, int i2, IOR ior) {
        super(orb, i2, ior);
    }

    @Override // com.sun.corba.se.spi.protocol.LocalClientRequestDispatcher
    public ServantObject servant_preinvoke(Object object, String str, Class cls) {
        if (!checkForCompatibleServant(this.servant, cls)) {
            return null;
        }
        return this.servant;
    }

    @Override // com.sun.corba.se.spi.protocol.LocalClientRequestDispatcher
    public void servant_postinvoke(Object object, ServantObject servantObject) {
    }

    public void setServant(Object obj) {
        if (obj != null && (obj instanceof Tie)) {
            this.servant = new ServantObject();
            this.servant.servant = ((Tie) obj).getTarget();
            return;
        }
        this.servant = null;
    }

    public void unexport() {
        this.servant = null;
    }
}
