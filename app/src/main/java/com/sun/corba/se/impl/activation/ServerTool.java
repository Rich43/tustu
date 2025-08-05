package com.sun.corba.se.impl.activation;

import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.activation.Repository;
import com.sun.corba.se.spi.activation.RepositoryHelper;
import com.sun.corba.se.spi.activation.ServerNotRegistered;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import org.omg.CORBA.ORB;

/* loaded from: rt.jar:com/sun/corba/se/impl/activation/ServerTool.class */
public class ServerTool {
    static final String helpCommand = "help";
    static final String toolName = "servertool";
    static final String commandArg = "-cmd";
    private static final boolean debug = false;
    ORB orb = null;
    static Vector handlers = new Vector();
    static int maxNameLen;

    static int getServerIdForAlias(ORB orb, String str) throws ServerNotRegistered {
        try {
            Repository repositoryNarrow = RepositoryHelper.narrow(orb.resolve_initial_references(ORBConstants.SERVER_REPOSITORY_NAME));
            repositoryNarrow.getServerID(str);
            return repositoryNarrow.getServerID(str);
        } catch (Exception e2) {
            throw new ServerNotRegistered();
        }
    }

    void run(String[] strArr) {
        String[] strArr2 = null;
        int i2 = 0;
        while (true) {
            if (i2 < strArr.length) {
                if (!strArr[i2].equals(commandArg)) {
                    i2++;
                } else {
                    int length = (strArr.length - i2) - 1;
                    strArr2 = new String[length];
                    for (int i3 = 0; i3 < length; i3++) {
                        i2++;
                        strArr2[i3] = strArr[i2];
                    }
                }
            }
        }
        try {
            Properties properties = System.getProperties();
            properties.put("org.omg.CORBA.ORBClass", "com.sun.corba.se.impl.orb.ORBImpl");
            this.orb = ORB.init(strArr, properties);
            if (strArr2 == null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                System.out.println(CorbaResourceUtil.getText("servertool.banner"));
                while (true) {
                    String[] command = readCommand(bufferedReader);
                    if (command != null) {
                        executeCommand(command);
                    } else {
                        printAvailableCommands();
                    }
                }
            } else {
                executeCommand(strArr2);
            }
        } catch (Exception e2) {
            System.out.println(CorbaResourceUtil.getText("servertool.usage", toolName));
            System.out.println();
            e2.printStackTrace();
        }
    }

    public static void main(String[] strArr) {
        new ServerTool().run(strArr);
    }

    String[] readCommand(BufferedReader bufferedReader) {
        System.out.print("servertool > ");
        try {
            int i2 = 0;
            String[] strArr = null;
            String line = bufferedReader.readLine();
            if (line != null) {
                StringTokenizer stringTokenizer = new StringTokenizer(line);
                if (stringTokenizer.countTokens() != 0) {
                    strArr = new String[stringTokenizer.countTokens()];
                    while (stringTokenizer.hasMoreTokens()) {
                        int i3 = i2;
                        i2++;
                        strArr[i3] = stringTokenizer.nextToken();
                    }
                }
            }
            return strArr;
        } catch (Exception e2) {
            System.out.println(CorbaResourceUtil.getText("servertool.usage", toolName));
            System.out.println();
            e2.printStackTrace();
            return null;
        }
    }

    void printAvailableCommands() {
        System.out.println(CorbaResourceUtil.getText("servertool.shorthelp"));
        for (int i2 = 0; i2 < handlers.size(); i2++) {
            CommandHandler commandHandler = (CommandHandler) handlers.elementAt(i2);
            System.out.print("\t" + commandHandler.getCommandName());
            for (int length = commandHandler.getCommandName().length(); length < maxNameLen; length++) {
                System.out.print(" ");
            }
            System.out.print(" - ");
            commandHandler.printCommandHelp(System.out, true);
        }
        System.out.println();
    }

    void executeCommand(String[] strArr) {
        if (strArr[0].equals(helpCommand)) {
            if (strArr.length != 1) {
                for (int i2 = 0; i2 < handlers.size(); i2++) {
                    CommandHandler commandHandler = (CommandHandler) handlers.elementAt(i2);
                    if (commandHandler.getCommandName().equals(strArr[1])) {
                        commandHandler.printCommandHelp(System.out, false);
                    }
                }
                return;
            }
            printAvailableCommands();
            return;
        }
        for (int i3 = 0; i3 < handlers.size(); i3++) {
            CommandHandler commandHandler2 = (CommandHandler) handlers.elementAt(i3);
            if (commandHandler2.getCommandName().equals(strArr[0])) {
                String[] strArr2 = new String[strArr.length - 1];
                for (int i4 = 0; i4 < strArr2.length; i4++) {
                    strArr2[i4] = strArr[i4 + 1];
                }
                try {
                    System.out.println();
                    if (commandHandler2.processCommand(strArr2, this.orb, System.out)) {
                        commandHandler2.printCommandHelp(System.out, false);
                    }
                    System.out.println();
                    return;
                } catch (Exception e2) {
                    return;
                }
            }
        }
        printAvailableCommands();
    }

    static {
        handlers.addElement(new RegisterServer());
        handlers.addElement(new UnRegisterServer());
        handlers.addElement(new GetServerID());
        handlers.addElement(new ListServers());
        handlers.addElement(new ListAliases());
        handlers.addElement(new ListActiveServers());
        handlers.addElement(new LocateServer());
        handlers.addElement(new LocateServerForORB());
        handlers.addElement(new ListORBs());
        handlers.addElement(new ShutdownServer());
        handlers.addElement(new StartServer());
        handlers.addElement(new Help());
        handlers.addElement(new Quit());
        maxNameLen = 0;
        for (int i2 = 0; i2 < handlers.size(); i2++) {
            int length = ((CommandHandler) handlers.elementAt(i2)).getCommandName().length();
            if (length > maxNameLen) {
                maxNameLen = length;
            }
        }
    }
}
