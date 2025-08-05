package org.apache.commons.net;

import java.io.PrintStream;
import java.io.PrintWriter;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/PrintCommandListener.class */
public class PrintCommandListener implements ProtocolCommandListener {
    private final PrintWriter __writer;
    private final boolean __nologin;
    private final char __eolMarker;
    private final boolean __directionMarker;

    public PrintCommandListener(PrintStream stream) {
        this(new PrintWriter(stream));
    }

    public PrintCommandListener(PrintStream stream, boolean suppressLogin) {
        this(new PrintWriter(stream), suppressLogin);
    }

    public PrintCommandListener(PrintStream stream, boolean suppressLogin, char eolMarker) {
        this(new PrintWriter(stream), suppressLogin, eolMarker);
    }

    public PrintCommandListener(PrintStream stream, boolean suppressLogin, char eolMarker, boolean showDirection) {
        this(new PrintWriter(stream), suppressLogin, eolMarker, showDirection);
    }

    public PrintCommandListener(PrintWriter writer) {
        this(writer, false);
    }

    public PrintCommandListener(PrintWriter writer, boolean suppressLogin) {
        this(writer, suppressLogin, (char) 0);
    }

    public PrintCommandListener(PrintWriter writer, boolean suppressLogin, char eolMarker) {
        this(writer, suppressLogin, eolMarker, false);
    }

    public PrintCommandListener(PrintWriter writer, boolean suppressLogin, char eolMarker, boolean showDirection) {
        this.__writer = writer;
        this.__nologin = suppressLogin;
        this.__eolMarker = eolMarker;
        this.__directionMarker = showDirection;
    }

    @Override // org.apache.commons.net.ProtocolCommandListener
    public void protocolCommandSent(ProtocolCommandEvent event) {
        if (this.__directionMarker) {
            this.__writer.print("> ");
        }
        if (this.__nologin) {
            String cmd = event.getCommand();
            if ("PASS".equalsIgnoreCase(cmd) || "USER".equalsIgnoreCase(cmd)) {
                this.__writer.print(cmd);
                this.__writer.println(" *******");
            } else if ("LOGIN".equalsIgnoreCase(cmd)) {
                String msg = event.getMessage();
                this.__writer.print(msg.substring(0, msg.indexOf("LOGIN") + "LOGIN".length()));
                this.__writer.println(" *******");
            } else {
                this.__writer.print(getPrintableString(event.getMessage()));
            }
        } else {
            this.__writer.print(getPrintableString(event.getMessage()));
        }
        this.__writer.flush();
    }

    private String getPrintableString(String msg) {
        if (this.__eolMarker == 0) {
            return msg;
        }
        int pos = msg.indexOf("\r\n");
        if (pos > 0) {
            return msg.substring(0, pos) + this.__eolMarker + msg.substring(pos);
        }
        return msg;
    }

    @Override // org.apache.commons.net.ProtocolCommandListener
    public void protocolReplyReceived(ProtocolCommandEvent event) {
        if (this.__directionMarker) {
            this.__writer.print("< ");
        }
        this.__writer.print(event.getMessage());
        this.__writer.flush();
    }
}
