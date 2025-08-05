package sun.net.smtp;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.security.AccessController;
import org.apache.commons.net.smtp.SMTPReply;
import sun.net.TransferProtocolClient;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/net/smtp/SmtpClient.class */
public class SmtpClient extends TransferProtocolClient {
    private static int DEFAULT_SMTP_PORT = 25;
    String mailhost;
    SmtpPrintStream message;

    @Override // sun.net.NetworkClient
    public void closeServer() throws IOException {
        if (serverIsOpen()) {
            closeMessage();
            issueCommand("QUIT\r\n", 221);
            super.closeServer();
        }
    }

    void issueCommand(String str, int i2) throws IOException {
        int serverResponse;
        sendServer(str);
        do {
            serverResponse = readServerResponse();
            if (serverResponse == i2) {
                return;
            }
        } while (serverResponse == 220);
        throw new SmtpProtocolException(getResponseString());
    }

    private void toCanonical(String str) throws IOException {
        if (str.startsWith("<")) {
            issueCommand("rcpt to: " + str + "\r\n", 250);
        } else {
            issueCommand("rcpt to: <" + str + ">\r\n", 250);
        }
    }

    public void to(String str) throws IOException {
        if (str.indexOf(10) != -1) {
            throw new IOException("Illegal SMTP command", new IllegalArgumentException("Illegal carriage return"));
        }
        int i2 = 0;
        int length = str.length();
        int i3 = 0;
        int i4 = 0;
        boolean z2 = false;
        for (int i5 = 0; i5 < length; i5++) {
            char cCharAt = str.charAt(i5);
            if (i4 > 0) {
                if (cCharAt == '(') {
                    i4++;
                } else if (cCharAt == ')') {
                    i4--;
                }
                if (i4 == 0) {
                    if (i3 > i2) {
                        z2 = true;
                    } else {
                        i2 = i5 + 1;
                    }
                }
            } else if (cCharAt == '(') {
                i4++;
            } else if (cCharAt == '<') {
                int i6 = i5 + 1;
                i3 = i6;
                i2 = i6;
            } else if (cCharAt == '>') {
                z2 = true;
            } else if (cCharAt == ',') {
                if (i3 > i2) {
                    toCanonical(str.substring(i2, i3));
                }
                i2 = i5 + 1;
                z2 = false;
            } else if (cCharAt > ' ' && !z2) {
                i3 = i5 + 1;
            } else if (i2 == i5) {
                i2++;
            }
        }
        if (i3 > i2) {
            toCanonical(str.substring(i2, i3));
        }
    }

    public void from(String str) throws IOException {
        if (str.indexOf(10) != -1) {
            throw new IOException("Illegal SMTP command", new IllegalArgumentException("Illegal carriage return"));
        }
        if (str.startsWith("<")) {
            issueCommand("mail from: " + str + "\r\n", 250);
        } else {
            issueCommand("mail from: <" + str + ">\r\n", 250);
        }
    }

    private void openServer(String str) throws IOException {
        this.mailhost = str;
        openServer(this.mailhost, DEFAULT_SMTP_PORT);
        issueCommand("helo " + InetAddress.getLocalHost().getHostName() + "\r\n", 250);
    }

    public PrintStream startMessage() throws IOException {
        issueCommand("data\r\n", SMTPReply.START_MAIL_INPUT);
        try {
            this.message = new SmtpPrintStream(this.serverOutput, this);
            return this.message;
        } catch (UnsupportedEncodingException e2) {
            throw new InternalError(encoding + " encoding not found", e2);
        }
    }

    void closeMessage() throws IOException {
        if (this.message != null) {
            this.message.close();
        }
    }

    public SmtpClient(String str) throws IOException {
        if (str != null) {
            try {
                openServer(str);
                this.mailhost = str;
                return;
            } catch (Exception e2) {
            }
        }
        try {
            this.mailhost = (String) AccessController.doPrivileged(new GetPropertyAction("mail.host"));
            if (this.mailhost != null) {
                openServer(this.mailhost);
                return;
            }
        } catch (Exception e3) {
        }
        try {
            this.mailhost = "localhost";
            openServer(this.mailhost);
        } catch (Exception e4) {
            this.mailhost = "mailhost";
            openServer(this.mailhost);
        }
    }

    public SmtpClient() throws IOException {
        this((String) null);
    }

    public SmtpClient(int i2) throws IOException {
        setConnectTimeout(i2);
        try {
            this.mailhost = (String) AccessController.doPrivileged(new GetPropertyAction("mail.host"));
            if (this.mailhost != null) {
                openServer(this.mailhost);
                return;
            }
        } catch (Exception e2) {
        }
        try {
            this.mailhost = "localhost";
            openServer(this.mailhost);
        } catch (Exception e3) {
            this.mailhost = "mailhost";
            openServer(this.mailhost);
        }
    }

    public String getMailHost() {
        return this.mailhost;
    }

    String getEncoding() {
        return encoding;
    }
}
