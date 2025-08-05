package com.sun.corba.se.impl.activation;

import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.activation.Repository;
import com.sun.corba.se.spi.activation.RepositoryHelper;
import com.sun.corba.se.spi.activation.RepositoryPackage.ServerDef;
import com.sun.corba.se.spi.activation.ServerNotRegistered;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.io.PrintStream;
import org.omg.CORBA.ORB;

/* compiled from: ServerTool.java */
/* loaded from: rt.jar:com/sun/corba/se/impl/activation/ListServers.class */
class ListServers implements CommandHandler {
    static final int illegalServerId = -1;

    ListServers() {
    }

    @Override // com.sun.corba.se.impl.activation.CommandHandler
    public String getCommandName() {
        return SchemaSymbols.ATTVAL_LIST;
    }

    @Override // com.sun.corba.se.impl.activation.CommandHandler
    public void printCommandHelp(PrintStream printStream, boolean z2) {
        if (!z2) {
            printStream.println(CorbaResourceUtil.getText("servertool.list"));
        } else {
            printStream.println(CorbaResourceUtil.getText("servertool.list1"));
        }
    }

    @Override // com.sun.corba.se.impl.activation.CommandHandler
    public boolean processCommand(String[] strArr, ORB orb, PrintStream printStream) {
        int iIntValue = -1;
        boolean z2 = strArr.length != 0;
        if (strArr.length == 2 && strArr[0].equals("-serverid")) {
            iIntValue = Integer.valueOf(strArr[1]).intValue();
        }
        if (iIntValue == -1 && z2) {
            return true;
        }
        try {
            Repository repositoryNarrow = RepositoryHelper.narrow(orb.resolve_initial_references(ORBConstants.SERVER_REPOSITORY_NAME));
            if (z2) {
                try {
                    ServerDef server = repositoryNarrow.getServer(iIntValue);
                    printStream.println();
                    printServerDef(server, iIntValue, printStream);
                    printStream.println();
                } catch (ServerNotRegistered e2) {
                    printStream.println(CorbaResourceUtil.getText("servertool.nosuchserver"));
                }
            } else {
                int[] iArrListRegisteredServers = repositoryNarrow.listRegisteredServers();
                printStream.println(CorbaResourceUtil.getText("servertool.list2"));
                sortServers(iArrListRegisteredServers);
                for (int i2 = 0; i2 < iArrListRegisteredServers.length; i2++) {
                    try {
                        ServerDef server2 = repositoryNarrow.getServer(iArrListRegisteredServers[i2]);
                        printStream.println("\t   " + iArrListRegisteredServers[i2] + "\t\t" + server2.serverName + "\t\t" + server2.applicationName);
                    } catch (ServerNotRegistered e3) {
                    }
                }
            }
            return false;
        } catch (Exception e4) {
            e4.printStackTrace();
            return false;
        }
    }

    static void printServerDef(ServerDef serverDef, int i2, PrintStream printStream) {
        printStream.println(CorbaResourceUtil.getText("servertool.appname", serverDef.applicationName));
        printStream.println(CorbaResourceUtil.getText("servertool.name", serverDef.serverName));
        printStream.println(CorbaResourceUtil.getText("servertool.classpath", serverDef.serverClassPath));
        printStream.println(CorbaResourceUtil.getText("servertool.args", serverDef.serverArgs));
        printStream.println(CorbaResourceUtil.getText("servertool.vmargs", serverDef.serverVmArgs));
        printStream.println(CorbaResourceUtil.getText("servertool.serverid", i2));
    }

    static void sortServers(int[] iArr) {
        int length = iArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = i2;
            for (int i4 = i2 + 1; i4 < length; i4++) {
                if (iArr[i4] < iArr[i3]) {
                    i3 = i4;
                }
            }
            if (i3 != i2) {
                int i5 = iArr[i2];
                iArr[i2] = iArr[i3];
                iArr[i3] = i5;
            }
        }
    }
}
