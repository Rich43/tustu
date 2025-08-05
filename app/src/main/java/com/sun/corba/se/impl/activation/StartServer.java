package com.sun.corba.se.impl.activation;

import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.activation.ActivatorHelper;
import com.sun.corba.se.spi.activation.ServerAlreadyActive;
import com.sun.corba.se.spi.activation.ServerHeldDown;
import com.sun.corba.se.spi.activation.ServerNotRegistered;
import java.io.PrintStream;
import org.omg.CORBA.ORB;

/* compiled from: ServerTool.java */
/* loaded from: rt.jar:com/sun/corba/se/impl/activation/StartServer.class */
class StartServer implements CommandHandler {
    static final int illegalServerId = -1;

    StartServer() {
    }

    @Override // com.sun.corba.se.impl.activation.CommandHandler
    public String getCommandName() {
        return "startup";
    }

    @Override // com.sun.corba.se.impl.activation.CommandHandler
    public void printCommandHelp(PrintStream printStream, boolean z2) {
        if (!z2) {
            printStream.println(CorbaResourceUtil.getText("servertool.startserver"));
        } else {
            printStream.println(CorbaResourceUtil.getText("servertool.startserver1"));
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
            ActivatorHelper.narrow(orb.resolve_initial_references(ORBConstants.SERVER_ACTIVATOR_NAME)).activate(serverIdForAlias);
            printStream.println(CorbaResourceUtil.getText("servertool.startserver2"));
            return false;
        } catch (ServerAlreadyActive e2) {
            printStream.println(CorbaResourceUtil.getText("servertool.serverup"));
            return false;
        } catch (ServerHeldDown e3) {
            printStream.println(CorbaResourceUtil.getText("servertool.helddown"));
            return false;
        } catch (ServerNotRegistered e4) {
            printStream.println(CorbaResourceUtil.getText("servertool.nosuchserver"));
            return false;
        } catch (Exception e5) {
            e5.printStackTrace();
            return false;
        }
    }
}
