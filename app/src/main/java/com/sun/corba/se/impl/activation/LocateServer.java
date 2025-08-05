package com.sun.corba.se.impl.activation;

import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.activation.LocatorHelper;
import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocation;
import com.sun.corba.se.spi.activation.NoSuchEndPoint;
import com.sun.corba.se.spi.activation.ORBPortInfo;
import com.sun.corba.se.spi.activation.ServerHeldDown;
import com.sun.corba.se.spi.activation.ServerNotRegistered;
import java.io.PrintStream;
import org.omg.CORBA.ORB;

/* compiled from: ServerTool.java */
/* loaded from: rt.jar:com/sun/corba/se/impl/activation/LocateServer.class */
class LocateServer implements CommandHandler {
    static final int illegalServerId = -1;

    LocateServer() {
    }

    @Override // com.sun.corba.se.impl.activation.CommandHandler
    public String getCommandName() {
        return "locate";
    }

    @Override // com.sun.corba.se.impl.activation.CommandHandler
    public void printCommandHelp(PrintStream printStream, boolean z2) {
        if (!z2) {
            printStream.println(CorbaResourceUtil.getText("servertool.locate"));
        } else {
            printStream.println(CorbaResourceUtil.getText("servertool.locate1"));
        }
    }

    @Override // com.sun.corba.se.impl.activation.CommandHandler
    public boolean processCommand(String[] strArr, ORB orb, PrintStream printStream) {
        int iIntValue = -1;
        String str = "IIOP_CLEAR_TEXT";
        int i2 = 0;
        while (i2 < strArr.length) {
            try {
                int i3 = i2;
                i2++;
                String str2 = strArr[i3];
                if (str2.equals("-serverid")) {
                    if (i2 >= strArr.length) {
                        return true;
                    }
                    i2++;
                    iIntValue = Integer.valueOf(strArr[i2]).intValue();
                } else if (str2.equals("-applicationName")) {
                    if (i2 >= strArr.length) {
                        return true;
                    }
                    i2++;
                    iIntValue = ServerTool.getServerIdForAlias(orb, strArr[i2]);
                } else if (str2.equals("-endpointType") && i2 < strArr.length) {
                    i2++;
                    str = strArr[i2];
                }
            } catch (NoSuchEndPoint e2) {
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
        if (iIntValue == -1) {
            return true;
        }
        ServerLocation serverLocationLocateServer = LocatorHelper.narrow(orb.resolve_initial_references(ORBConstants.SERVER_LOCATOR_NAME)).locateServer(iIntValue, str);
        printStream.println(CorbaResourceUtil.getText("servertool.locate2", serverLocationLocateServer.hostname));
        int length = serverLocationLocateServer.ports.length;
        for (int i4 = 0; i4 < length; i4++) {
            ORBPortInfo oRBPortInfo = serverLocationLocateServer.ports[i4];
            printStream.println("\t\t" + oRBPortInfo.port + "\t\t" + str + "\t\t" + oRBPortInfo.orbId);
        }
        return false;
    }
}
