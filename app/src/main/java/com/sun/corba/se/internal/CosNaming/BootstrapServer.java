package com.sun.corba.se.internal.CosNaming;

import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.resolver.LocalResolver;
import com.sun.corba.se.spi.resolver.ResolverDefault;
import java.io.File;
import java.util.Properties;
import org.omg.CORBA.ORBPackage.InvalidName;

/* loaded from: rt.jar:com/sun/corba/se/internal/CosNaming/BootstrapServer.class */
public class BootstrapServer {
    private ORB orb;

    public static final void main(String[] strArr) throws NumberFormatException {
        String str = null;
        int i2 = 900;
        for (int i3 = 0; i3 < strArr.length; i3++) {
            if (strArr[i3].equals("-InitialServicesFile") && i3 < strArr.length - 1) {
                str = strArr[i3 + 1];
            }
            if (strArr[i3].equals("-ORBInitialPort") && i3 < strArr.length - 1) {
                i2 = Integer.parseInt(strArr[i3 + 1]);
            }
        }
        if (str == null) {
            System.out.println(CorbaResourceUtil.getText("bootstrap.usage", "BootstrapServer"));
            return;
        }
        File file = new File(str);
        if (file.exists() && !file.canRead()) {
            System.err.println(CorbaResourceUtil.getText("bootstrap.filenotreadable", file.getAbsolutePath()));
            return;
        }
        System.out.println(CorbaResourceUtil.getText("bootstrap.success", Integer.toString(i2), file.getAbsolutePath()));
        Properties properties = new Properties();
        properties.put(ORBConstants.SERVER_PORT_PROPERTY, Integer.toString(i2));
        ORB orb = (ORB) org.omg.CORBA.ORB.init(strArr, properties);
        LocalResolver localResolver = orb.getLocalResolver();
        orb.setLocalResolver(ResolverDefault.makeSplitLocalResolver(ResolverDefault.makeCompositeResolver(ResolverDefault.makeFileResolver(orb, file), localResolver), localResolver));
        try {
            orb.resolve_initial_references(ORBConstants.ROOT_POA_NAME);
            orb.run();
        } catch (InvalidName e2) {
            RuntimeException runtimeException = new RuntimeException("This should not happen");
            runtimeException.initCause(e2);
            throw runtimeException;
        }
    }
}
