package com.sun.corba.se.impl.activation;

import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.activation.EndPointInfo;
import com.sun.corba.se.spi.activation.InvalidORBid;
import com.sun.corba.se.spi.activation.LocatorHelper;
import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocationPerORB;
import com.sun.corba.se.spi.activation.ServerHeldDown;
import com.sun.corba.se.spi.activation.ServerNotRegistered;
import java.io.PrintStream;
import org.omg.CORBA.ORB;

/* compiled from: ServerTool.java */
/* loaded from: rt.jar:com/sun/corba/se/impl/activation/LocateServerForORB.class */
class LocateServerForORB implements CommandHandler {
    static final int illegalServerId = -1;

    LocateServerForORB() {
    }

    @Override // com.sun.corba.se.impl.activation.CommandHandler
    public String getCommandName() {
        return "locateperorb";
    }

    @Override // com.sun.corba.se.impl.activation.CommandHandler
    public void printCommandHelp(PrintStream printStream, boolean z2) {
        if (!z2) {
            printStream.println(CorbaResourceUtil.getText("servertool.locateorb"));
        } else {
            printStream.println(CorbaResourceUtil.getText("servertool.locateorb1"));
        }
    }

    @Override // com.sun.corba.se.impl.activation.CommandHandler
    public boolean processCommand(String[] strArr, ORB orb, PrintStream printStream) {
        int iIntValue = -1;
        String str = "";
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
                } else if (str2.equals("-orbid") && i2 < strArr.length) {
                    i2++;
                    str = strArr[i2];
                }
            } catch (InvalidORBid e2) {
                printStream.println(CorbaResourceUtil.getText("servertool.nosuchorb"));
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
        ServerLocationPerORB serverLocationPerORBLocateServerForORB = LocatorHelper.narrow(orb.resolve_initial_references(ORBConstants.SERVER_LOCATOR_NAME)).locateServerForORB(iIntValue, str);
        printStream.println(CorbaResourceUtil.getText("servertool.locateorb2", serverLocationPerORBLocateServerForORB.hostname));
        int length = serverLocationPerORBLocateServerForORB.ports.length;
        for (int i4 = 0; i4 < length; i4++) {
            EndPointInfo endPointInfo = serverLocationPerORBLocateServerForORB.ports[i4];
            printStream.println("\t\t" + endPointInfo.port + "\t\t" + endPointInfo.endpointType + "\t\t" + str);
        }
        return false;
    }
}
