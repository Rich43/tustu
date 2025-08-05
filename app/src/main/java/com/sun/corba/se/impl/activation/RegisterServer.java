package com.sun.corba.se.impl.activation;

import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.activation.Activator;
import com.sun.corba.se.spi.activation.ActivatorHelper;
import com.sun.corba.se.spi.activation.BadServerDefinition;
import com.sun.corba.se.spi.activation.RepositoryHelper;
import com.sun.corba.se.spi.activation.RepositoryPackage.ServerDef;
import com.sun.corba.se.spi.activation.ServerAlreadyActive;
import com.sun.corba.se.spi.activation.ServerAlreadyRegistered;
import com.sun.corba.se.spi.activation.ServerHeldDown;
import com.sun.corba.se.spi.activation.ServerNotRegistered;
import java.io.PrintStream;
import org.omg.CORBA.ORB;

/* compiled from: ServerTool.java */
/* loaded from: rt.jar:com/sun/corba/se/impl/activation/RegisterServer.class */
class RegisterServer implements CommandHandler {
    RegisterServer() {
    }

    @Override // com.sun.corba.se.impl.activation.CommandHandler
    public String getCommandName() {
        return "register";
    }

    @Override // com.sun.corba.se.impl.activation.CommandHandler
    public void printCommandHelp(PrintStream printStream, boolean z2) {
        if (!z2) {
            printStream.println(CorbaResourceUtil.getText("servertool.register"));
        } else {
            printStream.println(CorbaResourceUtil.getText("servertool.register1"));
        }
    }

    @Override // com.sun.corba.se.impl.activation.CommandHandler
    public boolean processCommand(String[] strArr, ORB orb, PrintStream printStream) {
        int i2 = 0;
        String str = "";
        String str2 = "";
        String str3 = "";
        String str4 = "";
        String str5 = "";
        int iRegisterServer = 0;
        while (i2 < strArr.length) {
            int i3 = i2;
            i2++;
            String str6 = strArr[i3];
            if (str6.equals("-server")) {
                if (i2 >= strArr.length) {
                    return true;
                }
                i2++;
                str2 = strArr[i2];
            } else if (str6.equals("-applicationName")) {
                if (i2 >= strArr.length) {
                    return true;
                }
                i2++;
                str = strArr[i2];
            } else if (str6.equals("-classpath")) {
                if (i2 >= strArr.length) {
                    return true;
                }
                i2++;
                str3 = strArr[i2];
            } else if (str6.equals("-args")) {
                while (i2 < strArr.length && !strArr[i2].equals("-vmargs")) {
                    str4 = str4.equals("") ? strArr[i2] : str4 + " " + strArr[i2];
                    i2++;
                }
                if (str4.equals("")) {
                    return true;
                }
            } else {
                if (!str6.equals("-vmargs")) {
                    return true;
                }
                while (i2 < strArr.length && !strArr[i2].equals("-args")) {
                    str5 = str5.equals("") ? strArr[i2] : str5 + " " + strArr[i2];
                    i2++;
                }
                if (str5.equals("")) {
                    return true;
                }
            }
        }
        if (str2.equals("")) {
            return true;
        }
        try {
            iRegisterServer = RepositoryHelper.narrow(orb.resolve_initial_references(ORBConstants.SERVER_REPOSITORY_NAME)).registerServer(new ServerDef(str, str2, str3, str4, str5));
            Activator activatorNarrow = ActivatorHelper.narrow(orb.resolve_initial_references(ORBConstants.SERVER_ACTIVATOR_NAME));
            activatorNarrow.activate(iRegisterServer);
            activatorNarrow.install(iRegisterServer);
            printStream.println(CorbaResourceUtil.getText("servertool.register2", iRegisterServer));
            return false;
        } catch (BadServerDefinition e2) {
            printStream.println(CorbaResourceUtil.getText("servertool.baddef", e2.reason));
            return false;
        } catch (ServerAlreadyActive e3) {
            return false;
        } catch (ServerAlreadyRegistered e4) {
            printStream.println(CorbaResourceUtil.getText("servertool.register4", iRegisterServer));
            return false;
        } catch (ServerHeldDown e5) {
            printStream.println(CorbaResourceUtil.getText("servertool.register3", iRegisterServer));
            return false;
        } catch (ServerNotRegistered e6) {
            return false;
        } catch (Exception e7) {
            e7.printStackTrace();
            return false;
        }
    }
}
