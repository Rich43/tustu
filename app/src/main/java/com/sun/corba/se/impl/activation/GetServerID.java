package com.sun.corba.se.impl.activation;

import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.activation.RepositoryHelper;
import com.sun.corba.se.spi.activation.ServerNotRegistered;
import java.io.PrintStream;
import org.omg.CORBA.ORB;

/* compiled from: ServerTool.java */
/* loaded from: rt.jar:com/sun/corba/se/impl/activation/GetServerID.class */
class GetServerID implements CommandHandler {
    GetServerID() {
    }

    @Override // com.sun.corba.se.impl.activation.CommandHandler
    public String getCommandName() {
        return "getserverid";
    }

    @Override // com.sun.corba.se.impl.activation.CommandHandler
    public void printCommandHelp(PrintStream printStream, boolean z2) {
        if (!z2) {
            printStream.println(CorbaResourceUtil.getText("servertool.getserverid"));
        } else {
            printStream.println(CorbaResourceUtil.getText("servertool.getserverid1"));
        }
    }

    @Override // com.sun.corba.se.impl.activation.CommandHandler
    public boolean processCommand(String[] strArr, ORB orb, PrintStream printStream) {
        if (strArr.length == 2 && strArr[0].equals("-applicationName")) {
            String str = strArr[1];
            try {
                try {
                    int serverID = RepositoryHelper.narrow(orb.resolve_initial_references(ORBConstants.SERVER_REPOSITORY_NAME)).getServerID(str);
                    printStream.println();
                    printStream.println(CorbaResourceUtil.getText("servertool.getserverid2", str, Integer.toString(serverID)));
                    printStream.println();
                } catch (ServerNotRegistered e2) {
                    printStream.println(CorbaResourceUtil.getText("servertool.nosuchserver"));
                }
                return false;
            } catch (Exception e3) {
                e3.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
