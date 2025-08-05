package com.sun.corba.se.impl.naming.pcosnaming;

import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.activation.InitialNameServiceHelper;
import com.sun.corba.se.spi.orb.ORB;
import java.io.File;
import java.util.Properties;

/* loaded from: rt.jar:com/sun/corba/se/impl/naming/pcosnaming/NameServer.class */
public class NameServer {
    private ORB orb;
    private File dbDir;
    private static final String dbName = "names.db";

    public static void main(String[] strArr) {
        new NameServer(strArr).run();
    }

    protected NameServer(String[] strArr) {
        Properties properties = System.getProperties();
        properties.put(ORBConstants.SERVER_ID_PROPERTY, "1000");
        properties.put("org.omg.CORBA.ORBClass", "com.sun.corba.se.impl.orb.ORBImpl");
        this.orb = (ORB) org.omg.CORBA.ORB.init(strArr, properties);
        this.dbDir = new File(properties.getProperty(ORBConstants.DB_DIR_PROPERTY) + properties.getProperty("file.separator") + dbName + properties.getProperty("file.separator"));
        if (!this.dbDir.exists()) {
            this.dbDir.mkdir();
        }
    }

    protected void run() {
        try {
            InitialNameServiceHelper.narrow(this.orb.resolve_initial_references(ORBConstants.INITIAL_NAME_SERVICE_NAME)).bind(ORBConstants.PERSISTENT_NAME_SERVICE_NAME, new NameService(this.orb, this.dbDir).initialNamingContext(), true);
            System.out.println(CorbaResourceUtil.getText("pnameserv.success"));
            this.orb.run();
        } catch (Exception e2) {
            e2.printStackTrace(System.err);
        }
    }
}
