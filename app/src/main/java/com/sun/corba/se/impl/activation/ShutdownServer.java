package com.sun.corba.se.impl.activation;

import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.activation.ActivatorHelper;
import com.sun.corba.se.spi.activation.ServerNotActive;
import com.sun.corba.se.spi.activation.ServerNotRegistered;
import java.io.PrintStream;
import org.omg.CORBA.ORB;

/* compiled from: ServerTool.java */
/* loaded from: rt.jar:com/sun/corba/se/impl/activation/ShutdownServer.class */
class ShutdownServer implements CommandHandler {
    static final int illegalServerId = -1;

    ShutdownServer() {
    }

    @Override // com.sun.corba.se.impl.activation.CommandHandler
    public String getCommandName() {
        return "shutdown";
    }

    @Override // com.sun.corba.se.impl.activation.CommandHandler
    public void printCommandHelp(PrintStream printStream, boolean z2) {
        if (!z2) {
            printStream.println(CorbaResourceUtil.getText("servertool.shutdown"));
        } else {
            printStream.println(CorbaResourceUtil.getText("servertool.shutdown1"));
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
            ActivatorHelper.narrow(orb.resolve_initial_references(ORBConstants.SERVER_ACTIVATOR_NAME)).shutdown(serverIdForAlias);
            printStream.println(CorbaResourceUtil.getText("servertool.shutdown2"));
            return false;
        } catch (ServerNotActive e2) {
            printStream.println(CorbaResourceUtil.getText("servertool.servernotrunning"));
            return false;
        } catch (ServerNotRegistered e3) {
            printStream.println(CorbaResourceUtil.getText("servertool.nosuchserver"));
            return false;
        } catch (Exception e4) {
            e4.printStackTrace();
            return false;
        }
    }
}
