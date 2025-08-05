package sun.rmi.transport.proxy;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Hashtable;

/* loaded from: rt.jar:sun/rmi/transport/proxy/CGIHandler.class */
public final class CGIHandler {
    static int ContentLength;
    static String QueryString;
    static String RequestMethod;
    static String ServerName;
    static int ServerPort;
    private static CGICommandHandler[] commands;
    private static Hashtable<String, CGICommandHandler> commandLookup;

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.rmi.transport.proxy.CGIHandler.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Void run() {
                CGIHandler.ContentLength = Integer.getInteger("CONTENT_LENGTH", 0).intValue();
                CGIHandler.QueryString = System.getProperty("QUERY_STRING", "");
                CGIHandler.RequestMethod = System.getProperty("REQUEST_METHOD", "");
                CGIHandler.ServerName = System.getProperty("SERVER_NAME", "");
                CGIHandler.ServerPort = Integer.getInteger("SERVER_PORT", 0).intValue();
                return null;
            }
        });
        commands = new CGICommandHandler[]{new CGIForwardCommand(), new CGIGethostnameCommand(), new CGIPingCommand(), new CGITryHostnameCommand()};
        commandLookup = new Hashtable<>();
        for (int i2 = 0; i2 < commands.length; i2++) {
            commandLookup.put(commands[i2].getName(), commands[i2]);
        }
    }

    private CGIHandler() {
    }

    public static void main(String[] strArr) {
        String strSubstring;
        String strSubstring2;
        try {
            int iIndexOf = QueryString.indexOf("=");
            if (iIndexOf == -1) {
                strSubstring = QueryString;
                strSubstring2 = "";
            } else {
                strSubstring = QueryString.substring(0, iIndexOf);
                strSubstring2 = QueryString.substring(iIndexOf + 1);
            }
            CGICommandHandler cGICommandHandler = commandLookup.get(strSubstring);
            if (cGICommandHandler != null) {
                try {
                    cGICommandHandler.execute(strSubstring2);
                } catch (CGIClientException e2) {
                    e2.printStackTrace();
                    returnClientError(e2.getMessage());
                } catch (CGIServerException e3) {
                    e3.printStackTrace();
                    returnServerError(e3.getMessage());
                }
            } else {
                returnClientError("invalid command.");
            }
        } catch (Exception e4) {
            e4.printStackTrace();
            returnServerError("internal error: " + e4.getMessage());
        }
        System.exit(0);
    }

    private static void returnClientError(String str) {
        System.out.println("Status: 400 Bad Request: " + str);
        System.out.println("Content-type: text/html");
        System.out.println("");
        System.out.println("<HTML><HEAD><TITLE>Java RMI Client Error</TITLE></HEAD><BODY>");
        System.out.println("<H1>Java RMI Client Error</H1>");
        System.out.println("");
        System.out.println(str);
        System.out.println("</BODY></HTML>");
        System.exit(1);
    }

    private static void returnServerError(String str) {
        System.out.println("Status: 500 Server Error: " + str);
        System.out.println("Content-type: text/html");
        System.out.println("");
        System.out.println("<HTML><HEAD><TITLE>Java RMI Server Error</TITLE></HEAD><BODY>");
        System.out.println("<H1>Java RMI Server Error</H1>");
        System.out.println("");
        System.out.println(str);
        System.out.println("</BODY></HTML>");
        System.exit(1);
    }
}
