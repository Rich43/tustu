package com.sun.corba.se.impl.activation;

import com.sun.corba.se.impl.naming.pcosnaming.NameService;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.orb.ORB;
import java.io.File;

/* loaded from: rt.jar:com/sun/corba/se/impl/activation/NameServiceStartThread.class */
public class NameServiceStartThread extends Thread {
    private ORB orb;
    private File dbDir;

    public NameServiceStartThread(ORB orb, File file) {
        this.orb = orb;
        this.dbDir = file;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            this.orb.register_initial_reference(ORBConstants.PERSISTENT_NAME_SERVICE_NAME, new NameService(this.orb, this.dbDir).initialNamingContext());
        } catch (Exception e2) {
            System.err.println("NameService did not start successfully");
            e2.printStackTrace();
        }
    }
}
