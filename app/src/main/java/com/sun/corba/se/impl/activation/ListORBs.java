package com.sun.corba.se.impl.activation;

import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.activation.ActivatorHelper;
import com.sun.corba.se.spi.activation.ServerNotRegistered;
import java.io.PrintStream;
import org.omg.CORBA.ORB;

/* compiled from: ServerTool.java */
/* loaded from: rt.jar:com/sun/corba/se/impl/activation/ListORBs.class */
class ListORBs implements CommandHandler {
    static final int illegalServerId = -1;

    ListORBs() {
    }

    @Override // com.sun.corba.se.impl.activation.CommandHandler
    public String getCommandName() {
        return "orblist";
    }

    @Override // com.sun.corba.se.impl.activation.CommandHandler
    public void printCommandHelp(PrintStream printStream, boolean z2) {
        if (!z2) {
            printStream.println(CorbaResourceUtil.getText("servertool.orbidmap"));
        } else {
            printStream.println(CorbaResourceUtil.getText("servertool.orbidmap1"));
        }
    }

    @Override // com.sun.corba.se.impl.activation.CommandHandler
    public boolean processCommand(String[] strArr, ORB orb, PrintStream printStream) {
        int serverIdForAlias = -1;
        try {
            if (strArr.length == 2) {
                if (strArr[0].equals("-serverid")) {
                    serverIdForAlias = Integer.valueOf(strArr[1]).intValue();
                } else if (strArr[0].equals("-applicationName")) {
                    serverIdForAlias = ServerTool.getServerIdForAlias(orb, strArr[1]);
                }
            }
            if (serverIdForAlias == -1) {
                return true;
            }
            String[] oRBNames = ActivatorHelper.narrow(orb.resolve_initial_references(ORBConstants.SERVER_ACTIVATOR_NAME)).getORBNames(serverIdForAlias);
            printStream.println(CorbaResourceUtil.getText("servertool.orbidmap2"));
            for (String str : oRBNames) {
                printStream.println("\t " + str);
            }
            return false;
        } catch (ServerNotRegistered e2) {
            printStream.println("\tno such server found.");
            return false;
        } catch (Exception e3) {
            e3.printStackTrace();
            return false;
        }
    }
}
