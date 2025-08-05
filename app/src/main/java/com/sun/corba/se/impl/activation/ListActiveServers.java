package com.sun.corba.se.impl.activation;

import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.activation.ActivatorHelper;
import com.sun.corba.se.spi.activation.Repository;
import com.sun.corba.se.spi.activation.RepositoryHelper;
import com.sun.corba.se.spi.activation.RepositoryPackage.ServerDef;
import com.sun.corba.se.spi.activation.ServerNotRegistered;
import java.io.PrintStream;
import org.omg.CORBA.ORB;

/* compiled from: ServerTool.java */
/* loaded from: rt.jar:com/sun/corba/se/impl/activation/ListActiveServers.class */
class ListActiveServers implements CommandHandler {
    ListActiveServers() {
    }

    @Override // com.sun.corba.se.impl.activation.CommandHandler
    public String getCommandName() {
        return "listactive";
    }

    @Override // com.sun.corba.se.impl.activation.CommandHandler
    public void printCommandHelp(PrintStream printStream, boolean z2) {
        if (!z2) {
            printStream.println(CorbaResourceUtil.getText("servertool.listactive"));
        } else {
            printStream.println(CorbaResourceUtil.getText("servertool.listactive1"));
        }
    }

    @Override // com.sun.corba.se.impl.activation.CommandHandler
    public boolean processCommand(String[] strArr, ORB orb, PrintStream printStream) {
        try {
            Repository repositoryNarrow = RepositoryHelper.narrow(orb.resolve_initial_references(ORBConstants.SERVER_REPOSITORY_NAME));
            int[] activeServers = ActivatorHelper.narrow(orb.resolve_initial_references(ORBConstants.SERVER_ACTIVATOR_NAME)).getActiveServers();
            printStream.println(CorbaResourceUtil.getText("servertool.list2"));
            ListServers.sortServers(activeServers);
            for (int i2 = 0; i2 < activeServers.length; i2++) {
                try {
                    ServerDef server = repositoryNarrow.getServer(activeServers[i2]);
                    printStream.println("\t   " + activeServers[i2] + "\t\t" + server.serverName + "\t\t" + server.applicationName);
                } catch (ServerNotRegistered e2) {
                }
            }
            return false;
        } catch (Exception e3) {
            e3.printStackTrace();
            return false;
        }
    }
}
