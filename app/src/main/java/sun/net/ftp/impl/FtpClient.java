package sun.net.ftp.impl;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.nashorn.internal.runtime.ScriptingFunctions;
import org.icepdf.core.util.PdfOps;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import sun.net.TelnetInputStream;
import sun.net.TelnetOutputStream;
import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpDirEntry;
import sun.net.ftp.FtpDirParser;
import sun.net.ftp.FtpProtocolException;
import sun.net.ftp.FtpReplyCode;
import sun.net.util.IPAddressUtil;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/net/ftp/impl/FtpClient.class */
public class FtpClient extends sun.net.ftp.FtpClient {
    private static int defaultSoTimeout;
    private static int defaultConnectTimeout;
    private Proxy proxy;
    private Socket server;
    private PrintStream out;
    private InputStream in;
    private static String encoding;
    private InetSocketAddress serverAddr;
    private SSLSocketFactory sslFact;
    private Socket oldSocket;
    private String welcomeMsg;
    private String lastFileName;
    private static Pattern[] patterns;
    private static final boolean acceptPasvAddressVal;
    private static Pattern transPat;
    private static Pattern epsvPat;
    private static Pattern pasvPat;
    static final String ERROR_MSG = "Address should be the same as originating server";
    private static String[] MDTMformats;
    private static SimpleDateFormat[] dateFormats;
    private static final PlatformLogger logger = PlatformLogger.getLogger("sun.net.ftp.FtpClient");
    private static String[] patStrings = {"([\\-ld](?:[r\\-][w\\-][x\\-]){3})\\s*\\d+ (\\w+)\\s*(\\w+)\\s*(\\d+)\\s*([A-Z][a-z][a-z]\\s*\\d+)\\s*(\\d\\d:\\d\\d)\\s*(\\p{Print}*)", "([\\-ld](?:[r\\-][w\\-][x\\-]){3})\\s*\\d+ (\\w+)\\s*(\\w+)\\s*(\\d+)\\s*([A-Z][a-z][a-z]\\s*\\d+)\\s*(\\d{4})\\s*(\\p{Print}*)", "(\\d{2}/\\d{2}/\\d{4})\\s*(\\d{2}:\\d{2}[ap])\\s*((?:[0-9,]+)|(?:<DIR>))\\s*(\\p{Graph}*)", "(\\d{2}-\\d{2}-\\d{2})\\s*(\\d{2}:\\d{2}[AP]M)\\s*((?:[0-9,]+)|(?:<DIR>))\\s*(\\p{Graph}*)"};
    private static int[][] patternGroups = {new int[]{7, 4, 5, 6, 0, 1, 2, 3}, new int[]{7, 4, 5, 0, 6, 1, 2, 3}, new int[]{4, 3, 1, 2, 0, 0, 0, 0}, new int[]{4, 3, 1, 2, 0, 0, 0, 0}};
    private static Pattern linkp = Pattern.compile("(\\p{Print}+) \\-\\> (\\p{Print}+)$");
    private int readTimeout = -1;
    private int connectTimeout = -1;
    private boolean replyPending = false;
    private boolean loggedIn = false;
    private boolean useCrypto = false;
    private Vector<String> serverResponse = new Vector<>(1);
    private FtpReplyCode lastReplyCode = null;
    private final boolean passiveMode = true;
    private FtpClient.TransferType type = FtpClient.TransferType.BINARY;
    private long restartOffset = 0;
    private long lastTransSize = -1;
    private DateFormat df = DateFormat.getDateInstance(2, Locale.US);
    private FtpDirParser parser = new DefaultParser();
    private FtpDirParser mlsxParser = new MLSxParser();

    /* JADX WARN: Type inference failed for: r0v6, types: [int[], int[][]] */
    static {
        encoding = "ISO8859_1";
        final int[] iArr = {0, 0};
        final String[] strArr = {null};
        final String[] strArr2 = {null};
        AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: sun.net.ftp.impl.FtpClient.1
            @Override // java.security.PrivilegedAction
            public Object run() {
                strArr2[0] = System.getProperty("jdk.net.ftp.trustPasvAddress", "false");
                iArr[0] = Integer.getInteger("sun.net.client.defaultReadTimeout", 300000).intValue();
                iArr[1] = Integer.getInteger("sun.net.client.defaultConnectTimeout", 300000).intValue();
                strArr[0] = System.getProperty("file.encoding", "ISO8859_1");
                return null;
            }
        });
        if (iArr[0] == 0) {
            defaultSoTimeout = -1;
        } else {
            defaultSoTimeout = iArr[0];
        }
        if (iArr[1] == 0) {
            defaultConnectTimeout = -1;
        } else {
            defaultConnectTimeout = iArr[1];
        }
        encoding = strArr[0];
        try {
            if (!isASCIISuperset(encoding)) {
                encoding = "ISO8859_1";
            }
        } catch (Exception e2) {
            encoding = "ISO8859_1";
        }
        patterns = new Pattern[patStrings.length];
        for (int i2 = 0; i2 < patStrings.length; i2++) {
            patterns[i2] = Pattern.compile(patStrings[i2]);
        }
        acceptPasvAddressVal = Boolean.parseBoolean(strArr2[0]);
        transPat = null;
        epsvPat = null;
        pasvPat = null;
        MDTMformats = new String[]{"yyyyMMddHHmmss.SSS", "yyyyMMddHHmmss"};
        dateFormats = new SimpleDateFormat[MDTMformats.length];
        for (int i3 = 0; i3 < MDTMformats.length; i3++) {
            dateFormats[i3] = new SimpleDateFormat(MDTMformats[i3]);
            dateFormats[i3].setTimeZone(TimeZone.getTimeZone("GMT"));
        }
    }

    private static boolean isASCIISuperset(String str) throws Exception {
        return Arrays.equals("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_.!~*'();/?:@&=+$,".getBytes(str), new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 45, 95, 46, 33, 126, 42, 39, 40, 41, 59, 47, 63, 58, 64, 38, 61, 43, 36, 44});
    }

    /* loaded from: rt.jar:sun/net/ftp/impl/FtpClient$DefaultParser.class */
    private class DefaultParser implements FtpDirParser {
        private DefaultParser() {
        }

        @Override // sun.net.ftp.FtpDirParser
        public FtpDirEntry parseLine(String str) {
            Date time;
            String strGroup = null;
            String strGroup2 = null;
            String strGroup3 = null;
            String strGroup4 = null;
            String strGroup5 = null;
            String strGroup6 = null;
            String strGroup7 = null;
            boolean zStartsWith = false;
            Calendar calendar = Calendar.getInstance();
            int i2 = calendar.get(1);
            for (int i3 = 0; i3 < FtpClient.patterns.length; i3++) {
                Matcher matcher = FtpClient.patterns[i3].matcher(str);
                if (matcher.find()) {
                    strGroup4 = matcher.group(FtpClient.patternGroups[i3][0]);
                    strGroup2 = matcher.group(FtpClient.patternGroups[i3][1]);
                    strGroup = matcher.group(FtpClient.patternGroups[i3][2]);
                    if (FtpClient.patternGroups[i3][4] > 0) {
                        strGroup = strGroup + ", " + matcher.group(FtpClient.patternGroups[i3][4]);
                    } else if (FtpClient.patternGroups[i3][3] > 0) {
                        strGroup = strGroup + ", " + String.valueOf(i2);
                    }
                    if (FtpClient.patternGroups[i3][3] > 0) {
                        strGroup3 = matcher.group(FtpClient.patternGroups[i3][3]);
                    }
                    if (FtpClient.patternGroups[i3][5] > 0) {
                        strGroup5 = matcher.group(FtpClient.patternGroups[i3][5]);
                        zStartsWith = strGroup5.startsWith(PdfOps.d_TOKEN);
                    }
                    if (FtpClient.patternGroups[i3][6] > 0) {
                        strGroup6 = matcher.group(FtpClient.patternGroups[i3][6]);
                    }
                    if (FtpClient.patternGroups[i3][7] > 0) {
                        strGroup7 = matcher.group(FtpClient.patternGroups[i3][7]);
                    }
                    if ("<DIR>".equals(strGroup2)) {
                        zStartsWith = true;
                        strGroup2 = null;
                    }
                }
            }
            if (strGroup4 != null) {
                try {
                    time = FtpClient.this.df.parse(strGroup);
                } catch (Exception e2) {
                    time = null;
                }
                if (time != null && strGroup3 != null) {
                    int iIndexOf = strGroup3.indexOf(CallSiteDescriptor.TOKEN_DELIMITER);
                    calendar.setTime(time);
                    calendar.set(10, Integer.parseInt(strGroup3.substring(0, iIndexOf)));
                    calendar.set(12, Integer.parseInt(strGroup3.substring(iIndexOf + 1)));
                    time = calendar.getTime();
                }
                Matcher matcher2 = FtpClient.linkp.matcher(strGroup4);
                if (matcher2.find()) {
                    strGroup4 = matcher2.group(1);
                }
                boolean[][] zArr = new boolean[3][3];
                for (int i4 = 0; i4 < 3; i4++) {
                    for (int i5 = 0; i5 < 3; i5++) {
                        zArr[i4][i5] = strGroup5.charAt((i4 * 3) + i5) != '-';
                    }
                }
                FtpDirEntry ftpDirEntry = new FtpDirEntry(strGroup4);
                ftpDirEntry.setUser(strGroup6).setGroup(strGroup7);
                ftpDirEntry.setSize(Long.parseLong(strGroup2)).setLastModified(time);
                ftpDirEntry.setPermissions(zArr);
                ftpDirEntry.setType(zStartsWith ? FtpDirEntry.Type.DIR : str.charAt(0) == 'l' ? FtpDirEntry.Type.LINK : FtpDirEntry.Type.FILE);
                return ftpDirEntry;
            }
            return null;
        }
    }

    /* loaded from: rt.jar:sun/net/ftp/impl/FtpClient$MLSxParser.class */
    private class MLSxParser implements FtpDirParser {
        private SimpleDateFormat df;

        private MLSxParser() {
            this.df = new SimpleDateFormat("yyyyMMddhhmmss");
        }

        @Override // sun.net.ftp.FtpDirParser
        public FtpDirEntry parseLine(String str) {
            String strTrim;
            String strSubstring;
            String strSubstring2;
            int iLastIndexOf = str.lastIndexOf(";");
            if (iLastIndexOf > 0) {
                strTrim = str.substring(iLastIndexOf + 1).trim();
                strSubstring = str.substring(0, iLastIndexOf);
            } else {
                strTrim = str.trim();
                strSubstring = "";
            }
            FtpDirEntry ftpDirEntry = new FtpDirEntry(strTrim);
            while (!strSubstring.isEmpty()) {
                int iIndexOf = strSubstring.indexOf(";");
                if (iIndexOf > 0) {
                    strSubstring2 = strSubstring.substring(0, iIndexOf);
                    strSubstring = strSubstring.substring(iIndexOf + 1);
                } else {
                    strSubstring2 = strSubstring;
                    strSubstring = "";
                }
                int iIndexOf2 = strSubstring2.indexOf("=");
                if (iIndexOf2 > 0) {
                    ftpDirEntry.addFact(strSubstring2.substring(0, iIndexOf2), strSubstring2.substring(iIndexOf2 + 1));
                }
            }
            String fact = ftpDirEntry.getFact("Size");
            if (fact != null) {
                ftpDirEntry.setSize(Long.parseLong(fact));
            }
            String fact2 = ftpDirEntry.getFact("Modify");
            if (fact2 != null) {
                Date date = null;
                try {
                    date = this.df.parse(fact2);
                } catch (ParseException e2) {
                }
                if (date != null) {
                    ftpDirEntry.setLastModified(date);
                }
            }
            String fact3 = ftpDirEntry.getFact("Create");
            if (fact3 != null) {
                Date date2 = null;
                try {
                    date2 = this.df.parse(fact3);
                } catch (ParseException e3) {
                }
                if (date2 != null) {
                    ftpDirEntry.setCreated(date2);
                }
            }
            String fact4 = ftpDirEntry.getFact(Constants._ATT_TYPE);
            if (fact4 != null) {
                if (fact4.equalsIgnoreCase(DeploymentDescriptorParser.ATTR_FILE)) {
                    ftpDirEntry.setType(FtpDirEntry.Type.FILE);
                }
                if (fact4.equalsIgnoreCase("dir")) {
                    ftpDirEntry.setType(FtpDirEntry.Type.DIR);
                }
                if (fact4.equalsIgnoreCase("cdir")) {
                    ftpDirEntry.setType(FtpDirEntry.Type.CDIR);
                }
                if (fact4.equalsIgnoreCase("pdir")) {
                    ftpDirEntry.setType(FtpDirEntry.Type.PDIR);
                }
            }
            return ftpDirEntry;
        }
    }

    private void getTransferSize() {
        this.lastTransSize = -1L;
        String lastResponseString = getLastResponseString();
        if (transPat == null) {
            transPat = Pattern.compile("150 Opening .*\\((\\d+) bytes\\).");
        }
        Matcher matcher = transPat.matcher(lastResponseString);
        if (matcher.find()) {
            this.lastTransSize = Long.parseLong(matcher.group(1));
        }
    }

    private void getTransferName() {
        this.lastFileName = null;
        String lastResponseString = getLastResponseString();
        int iIndexOf = lastResponseString.indexOf("unique file name:");
        int iLastIndexOf = lastResponseString.lastIndexOf(41);
        if (iIndexOf >= 0) {
            this.lastFileName = lastResponseString.substring(iIndexOf + 17, iLastIndexOf);
        }
    }

    private int readServerResponse() throws IOException {
        int i2;
        StringBuffer stringBuffer = new StringBuffer(32);
        int i3 = -1;
        this.serverResponse.setSize(0);
        while (true) {
            int i4 = this.in.read();
            int i5 = i4;
            if (i4 != -1) {
                if (i5 == 13) {
                    int i6 = this.in.read();
                    i5 = i6;
                    if (i6 != 10) {
                        stringBuffer.append('\r');
                    }
                }
                stringBuffer.append((char) i5);
                if (i5 != 10) {
                    continue;
                }
            }
            String string = stringBuffer.toString();
            stringBuffer.setLength(0);
            if (logger.isLoggable(PlatformLogger.Level.FINEST)) {
                logger.finest("Server [" + ((Object) this.serverAddr) + "] --> " + string);
            }
            if (string.length() == 0) {
                i2 = -1;
            } else {
                try {
                    i2 = Integer.parseInt(string.substring(0, 3));
                } catch (NumberFormatException e2) {
                    i2 = -1;
                } catch (StringIndexOutOfBoundsException e3) {
                }
            }
            this.serverResponse.addElement(string);
            if (i3 != -1) {
                if (i2 == i3 && (string.length() < 4 || string.charAt(3) != '-')) {
                    break;
                }
            } else {
                if (string.length() < 4 || string.charAt(3) != '-') {
                    break;
                }
                i3 = i2;
            }
        }
        return i2;
    }

    private void sendServer(String str) {
        this.out.print(str);
        if (logger.isLoggable(PlatformLogger.Level.FINEST)) {
            logger.finest("Server [" + ((Object) this.serverAddr) + "] <-- " + str);
        }
    }

    private String getResponseString() {
        return this.serverResponse.elementAt(0);
    }

    private Vector<String> getResponseStrings() {
        return this.serverResponse;
    }

    private boolean readReply() throws IOException {
        this.lastReplyCode = FtpReplyCode.find(readServerResponse());
        if (this.lastReplyCode.isPositivePreliminary()) {
            this.replyPending = true;
            return true;
        }
        if (this.lastReplyCode.isPositiveCompletion() || this.lastReplyCode.isPositiveIntermediate()) {
            if (this.lastReplyCode == FtpReplyCode.CLOSING_DATA_CONNECTION) {
                getTransferName();
                return true;
            }
            return true;
        }
        return false;
    }

    private boolean issueCommand(String str) throws FtpProtocolException, IOException {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected");
        }
        if (this.replyPending) {
            try {
                completePending();
            } catch (FtpProtocolException e2) {
            }
        }
        if (str.indexOf(10) != -1) {
            FtpProtocolException ftpProtocolException = new FtpProtocolException("Illegal FTP command");
            ftpProtocolException.initCause(new IllegalArgumentException("Illegal carriage return"));
            throw ftpProtocolException;
        }
        sendServer(str + "\r\n");
        return readReply();
    }

    private void issueCommandCheck(String str) throws FtpProtocolException, IOException {
        if (!issueCommand(str)) {
            throw new FtpProtocolException(str + CallSiteDescriptor.TOKEN_DELIMITER + getResponseString(), getLastReplyCode());
        }
    }

    private Socket openPassiveDataConnection(String str) throws FtpProtocolException, IOException, NumberFormatException {
        InetSocketAddress inetSocketAddressValidatePasvAddress;
        Socket socket;
        if (issueCommand("EPSV ALL")) {
            issueCommandCheck("EPSV");
            String responseString = getResponseString();
            if (epsvPat == null) {
                epsvPat = Pattern.compile("^229 .* \\(\\|\\|\\|(\\d+)\\|\\)");
            }
            Matcher matcher = epsvPat.matcher(responseString);
            if (!matcher.find()) {
                throw new FtpProtocolException("EPSV failed : " + responseString);
            }
            int i2 = Integer.parseInt(matcher.group(1));
            InetAddress inetAddress = this.server.getInetAddress();
            if (inetAddress != null) {
                inetSocketAddressValidatePasvAddress = new InetSocketAddress(inetAddress, i2);
            } else {
                inetSocketAddressValidatePasvAddress = InetSocketAddress.createUnresolved(this.serverAddr.getHostName(), i2);
            }
        } else {
            issueCommandCheck("PASV");
            String responseString2 = getResponseString();
            if (pasvPat == null) {
                pasvPat = Pattern.compile("227 .* \\(?(\\d{1,3},\\d{1,3},\\d{1,3},\\d{1,3}),(\\d{1,3}),(\\d{1,3})\\)?");
            }
            Matcher matcher2 = pasvPat.matcher(responseString2);
            if (!matcher2.find()) {
                throw new FtpProtocolException("PASV failed : " + responseString2);
            }
            int i3 = Integer.parseInt(matcher2.group(3)) + (Integer.parseInt(matcher2.group(2)) << 8);
            String strReplace = matcher2.group(1).replace(',', '.');
            if (!IPAddressUtil.isIPv4LiteralAddress(strReplace)) {
                throw new FtpProtocolException("PASV failed : " + responseString2);
            }
            if (acceptPasvAddressVal) {
                inetSocketAddressValidatePasvAddress = new InetSocketAddress(strReplace, i3);
            } else {
                inetSocketAddressValidatePasvAddress = validatePasvAddress(i3, strReplace, this.server.getInetAddress());
            }
        }
        if (this.proxy != null) {
            if (this.proxy.type() == Proxy.Type.SOCKS) {
                socket = (Socket) AccessController.doPrivileged(new PrivilegedAction<Socket>() { // from class: sun.net.ftp.impl.FtpClient.2
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    public Socket run() {
                        return new Socket(FtpClient.this.proxy);
                    }
                });
            } else {
                socket = new Socket(Proxy.NO_PROXY);
            }
        } else {
            socket = new Socket();
        }
        socket.bind(new InetSocketAddress((InetAddress) AccessController.doPrivileged(new PrivilegedAction<InetAddress>() { // from class: sun.net.ftp.impl.FtpClient.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public InetAddress run() {
                return FtpClient.this.server.getLocalAddress();
            }
        }), 0));
        if (this.connectTimeout >= 0) {
            socket.connect(inetSocketAddressValidatePasvAddress, this.connectTimeout);
        } else if (defaultConnectTimeout > 0) {
            socket.connect(inetSocketAddressValidatePasvAddress, defaultConnectTimeout);
        } else {
            socket.connect(inetSocketAddressValidatePasvAddress);
        }
        if (this.readTimeout >= 0) {
            socket.setSoTimeout(this.readTimeout);
        } else if (defaultSoTimeout > 0) {
            socket.setSoTimeout(defaultSoTimeout);
        }
        if (this.useCrypto) {
            try {
                socket = this.sslFact.createSocket(socket, inetSocketAddressValidatePasvAddress.getHostName(), inetSocketAddressValidatePasvAddress.getPort(), true);
            } catch (Exception e2) {
                throw new FtpProtocolException("Can't open secure data channel: " + ((Object) e2));
            }
        }
        if (!issueCommand(str)) {
            socket.close();
            if (getLastReplyCode() == FtpReplyCode.FILE_UNAVAILABLE) {
                throw new FileNotFoundException(str);
            }
            throw new FtpProtocolException(str + CallSiteDescriptor.TOKEN_DELIMITER + getResponseString(), getLastReplyCode());
        }
        return socket;
    }

    private InetSocketAddress validatePasvAddress(int i2, String str, InetAddress inetAddress) throws FtpProtocolException {
        if (inetAddress == null) {
            return InetSocketAddress.createUnresolved(this.serverAddr.getHostName(), i2);
        }
        if (inetAddress.getHostAddress().equals(str)) {
            return new InetSocketAddress(str, i2);
        }
        if (inetAddress.isLoopbackAddress() && str.startsWith("127.")) {
            return new InetSocketAddress(str, i2);
        }
        if (inetAddress.isLoopbackAddress()) {
            if (privilegedLocalHost().getHostAddress().equals(str)) {
                return new InetSocketAddress(str, i2);
            }
            throw new FtpProtocolException(ERROR_MSG);
        }
        if (str.startsWith("127.")) {
            if (privilegedLocalHost().equals(inetAddress)) {
                return new InetSocketAddress(str, i2);
            }
            throw new FtpProtocolException(ERROR_MSG);
        }
        String hostName = inetAddress.getHostName();
        if (!IPAddressUtil.isIPv4LiteralAddress(hostName) && !IPAddressUtil.isIPv6LiteralAddress(hostName)) {
            Stream map = Arrays.stream(privilegedGetAllByName(hostName)).map((v0) -> {
                return v0.getHostAddress();
            });
            str.getClass();
            if (((String) map.filter(str::equalsIgnoreCase).findFirst().orElse(null)) != null) {
                return new InetSocketAddress(str, i2);
            }
        }
        throw new FtpProtocolException(ERROR_MSG);
    }

    private static InetAddress privilegedLocalHost() throws FtpProtocolException {
        try {
            return (InetAddress) AccessController.doPrivileged(InetAddress::getLocalHost);
        } catch (Exception e2) {
            FtpProtocolException ftpProtocolException = new FtpProtocolException(ERROR_MSG);
            ftpProtocolException.initCause(e2);
            throw ftpProtocolException;
        }
    }

    private static InetAddress[] privilegedGetAllByName(String str) throws FtpProtocolException {
        try {
            return (InetAddress[]) AccessController.doPrivileged(() -> {
                return InetAddress.getAllByName(str);
            });
        } catch (Exception e2) {
            FtpProtocolException ftpProtocolException = new FtpProtocolException(ERROR_MSG);
            ftpProtocolException.initCause(e2);
            throw ftpProtocolException;
        }
    }

    private Socket openDataConnection(String str) throws FtpProtocolException, IOException {
        try {
            return openPassiveDataConnection(str);
        } catch (FtpProtocolException e2) {
            String message = e2.getMessage();
            if (!message.startsWith("PASV") && !message.startsWith("EPSV")) {
                throw e2;
            }
            if (this.proxy != null && this.proxy.type() == Proxy.Type.SOCKS) {
                throw new FtpProtocolException("Passive mode failed");
            }
            ServerSocket serverSocket = new ServerSocket(0, 1, this.server.getLocalAddress());
            try {
                InetAddress inetAddress = serverSocket.getInetAddress();
                if (inetAddress.isAnyLocalAddress()) {
                    inetAddress = this.server.getLocalAddress();
                }
                if (!issueCommand("EPRT |" + (inetAddress instanceof Inet6Address ? "2" : "1") + CallSiteDescriptor.OPERATOR_DELIMITER + inetAddress.getHostAddress() + CallSiteDescriptor.OPERATOR_DELIMITER + serverSocket.getLocalPort() + CallSiteDescriptor.OPERATOR_DELIMITER) || !issueCommand(str)) {
                    String str2 = "PORT ";
                    for (byte b2 : inetAddress.getAddress()) {
                        str2 = str2 + (b2 & 255) + ",";
                    }
                    issueCommandCheck(str2 + ((serverSocket.getLocalPort() >>> 8) & 255) + "," + (serverSocket.getLocalPort() & 255));
                    issueCommandCheck(str);
                }
                if (this.connectTimeout >= 0) {
                    serverSocket.setSoTimeout(this.connectTimeout);
                } else if (defaultConnectTimeout > 0) {
                    serverSocket.setSoTimeout(defaultConnectTimeout);
                }
                Socket socketAccept = serverSocket.accept();
                if (this.readTimeout >= 0) {
                    socketAccept.setSoTimeout(this.readTimeout);
                } else if (defaultSoTimeout > 0) {
                    socketAccept.setSoTimeout(defaultSoTimeout);
                }
                if (this.useCrypto) {
                    try {
                        socketAccept = this.sslFact.createSocket(socketAccept, this.serverAddr.getHostName(), this.serverAddr.getPort(), true);
                    } catch (Exception e3) {
                        throw new IOException(e3.getLocalizedMessage());
                    }
                }
                return socketAccept;
            } finally {
                serverSocket.close();
            }
        }
    }

    private InputStream createInputStream(InputStream inputStream) {
        if (this.type == FtpClient.TransferType.ASCII) {
            return new TelnetInputStream(inputStream, false);
        }
        return inputStream;
    }

    private OutputStream createOutputStream(OutputStream outputStream) {
        if (this.type == FtpClient.TransferType.ASCII) {
            return new TelnetOutputStream(outputStream, false);
        }
        return outputStream;
    }

    protected FtpClient() {
    }

    public static sun.net.ftp.FtpClient create() {
        return new FtpClient();
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient enablePassiveMode(boolean z2) {
        return this;
    }

    @Override // sun.net.ftp.FtpClient
    public boolean isPassiveModeEnabled() {
        return true;
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient setConnectTimeout(int i2) {
        this.connectTimeout = i2;
        return this;
    }

    @Override // sun.net.ftp.FtpClient
    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient setReadTimeout(int i2) {
        this.readTimeout = i2;
        return this;
    }

    @Override // sun.net.ftp.FtpClient
    public int getReadTimeout() {
        return this.readTimeout;
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient setProxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    @Override // sun.net.ftp.FtpClient
    public Proxy getProxy() {
        return this.proxy;
    }

    private void tryConnect(InetSocketAddress inetSocketAddress, int i2) throws IOException {
        if (isConnected()) {
            disconnect();
        }
        this.server = doConnect(inetSocketAddress, i2);
        try {
            this.out = new PrintStream((OutputStream) new BufferedOutputStream(this.server.getOutputStream()), true, encoding);
            this.in = new BufferedInputStream(this.server.getInputStream());
        } catch (UnsupportedEncodingException e2) {
            throw new InternalError(encoding + "encoding not found", e2);
        }
    }

    private Socket doConnect(InetSocketAddress inetSocketAddress, int i2) throws IOException {
        Socket socket;
        if (this.proxy != null) {
            if (this.proxy.type() == Proxy.Type.SOCKS) {
                socket = (Socket) AccessController.doPrivileged(new PrivilegedAction<Socket>() { // from class: sun.net.ftp.impl.FtpClient.4
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    public Socket run() {
                        return new Socket(FtpClient.this.proxy);
                    }
                });
            } else {
                socket = new Socket(Proxy.NO_PROXY);
            }
        } else {
            socket = new Socket();
        }
        if (i2 >= 0) {
            socket.connect(inetSocketAddress, i2);
        } else if (this.connectTimeout >= 0) {
            socket.connect(inetSocketAddress, this.connectTimeout);
        } else if (defaultConnectTimeout > 0) {
            socket.connect(inetSocketAddress, defaultConnectTimeout);
        } else {
            socket.connect(inetSocketAddress);
        }
        if (this.readTimeout >= 0) {
            socket.setSoTimeout(this.readTimeout);
        } else if (defaultSoTimeout > 0) {
            socket.setSoTimeout(defaultSoTimeout);
        }
        return socket;
    }

    private void disconnect() throws IOException {
        if (isConnected()) {
            this.server.close();
        }
        this.server = null;
        this.in = null;
        this.out = null;
        this.lastTransSize = -1L;
        this.lastFileName = null;
        this.restartOffset = 0L;
        this.welcomeMsg = null;
        this.lastReplyCode = null;
        this.serverResponse.setSize(0);
    }

    @Override // sun.net.ftp.FtpClient
    public boolean isConnected() {
        return this.server != null;
    }

    @Override // sun.net.ftp.FtpClient
    public SocketAddress getServerAddress() {
        if (this.server == null) {
            return null;
        }
        return this.server.getRemoteSocketAddress();
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient connect(SocketAddress socketAddress) throws FtpProtocolException, IOException {
        return connect(socketAddress, -1);
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient connect(SocketAddress socketAddress, int i2) throws FtpProtocolException, IOException {
        if (!(socketAddress instanceof InetSocketAddress)) {
            throw new IllegalArgumentException("Wrong address type");
        }
        this.serverAddr = (InetSocketAddress) socketAddress;
        tryConnect(this.serverAddr, i2);
        if (!readReply()) {
            throw new FtpProtocolException("Welcome message: " + getResponseString(), this.lastReplyCode);
        }
        this.welcomeMsg = getResponseString().substring(4);
        return this;
    }

    private void tryLogin(String str, char[] cArr) throws FtpProtocolException, IOException {
        issueCommandCheck("USER " + str);
        if (this.lastReplyCode == FtpReplyCode.NEED_PASSWORD && cArr != null && cArr.length > 0) {
            issueCommandCheck("PASS " + String.valueOf(cArr));
        }
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient login(String str, char[] cArr) throws FtpProtocolException, IOException {
        if (!isConnected()) {
            throw new FtpProtocolException("Not connected yet", FtpReplyCode.BAD_SEQUENCE);
        }
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("User name can't be null or empty");
        }
        tryLogin(str, cArr);
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < this.serverResponse.size(); i2++) {
            String strElementAt = this.serverResponse.elementAt(i2);
            if (strElementAt != null) {
                if (strElementAt.length() >= 4 && strElementAt.startsWith("230")) {
                    strElementAt = strElementAt.substring(4);
                }
                stringBuffer.append(strElementAt);
            }
        }
        this.welcomeMsg = stringBuffer.toString();
        this.loggedIn = true;
        return this;
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient login(String str, char[] cArr, String str2) throws FtpProtocolException, IOException {
        if (!isConnected()) {
            throw new FtpProtocolException("Not connected yet", FtpReplyCode.BAD_SEQUENCE);
        }
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("User name can't be null or empty");
        }
        tryLogin(str, cArr);
        if (this.lastReplyCode == FtpReplyCode.NEED_ACCOUNT) {
            issueCommandCheck("ACCT " + str2);
        }
        StringBuffer stringBuffer = new StringBuffer();
        if (this.serverResponse != null) {
            Iterator<String> it = this.serverResponse.iterator();
            while (it.hasNext()) {
                String next = it.next();
                if (next != null) {
                    if (next.length() >= 4 && next.startsWith("230")) {
                        next = next.substring(4);
                    }
                    stringBuffer.append(next);
                }
            }
        }
        this.welcomeMsg = stringBuffer.toString();
        this.loggedIn = true;
        return this;
    }

    @Override // sun.net.ftp.FtpClient, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (isConnected()) {
            try {
                issueCommand("QUIT");
            } catch (FtpProtocolException e2) {
            }
            this.loggedIn = false;
        }
        disconnect();
    }

    @Override // sun.net.ftp.FtpClient
    public boolean isLoggedIn() {
        return this.loggedIn;
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient changeDirectory(String str) throws FtpProtocolException, IOException {
        if (str == null || "".equals(str)) {
            throw new IllegalArgumentException("directory can't be null or empty");
        }
        issueCommandCheck("CWD " + str);
        return this;
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient changeToParentDirectory() throws FtpProtocolException, IOException {
        issueCommandCheck("CDUP");
        return this;
    }

    @Override // sun.net.ftp.FtpClient
    public String getWorkingDirectory() throws FtpProtocolException, IOException {
        issueCommandCheck(ScriptingFunctions.PWD_NAME);
        String responseString = getResponseString();
        if (!responseString.startsWith("257")) {
            return null;
        }
        return responseString.substring(5, responseString.lastIndexOf(34));
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient setRestartOffset(long j2) {
        if (j2 < 0) {
            throw new IllegalArgumentException("offset can't be negative");
        }
        this.restartOffset = j2;
        return this;
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient getFile(String str, OutputStream outputStream) throws FtpProtocolException, IOException {
        if (this.restartOffset > 0) {
            try {
                Socket socketOpenDataConnection = openDataConnection("REST " + this.restartOffset);
                this.restartOffset = 0L;
                issueCommandCheck("RETR " + str);
                getTransferSize();
                InputStream inputStreamCreateInputStream = createInputStream(socketOpenDataConnection.getInputStream());
                byte[] bArr = new byte[1500 * 10];
                while (true) {
                    int i2 = inputStreamCreateInputStream.read(bArr);
                    if (i2 < 0) {
                        break;
                    }
                    if (i2 > 0) {
                        outputStream.write(bArr, 0, i2);
                    }
                }
                inputStreamCreateInputStream.close();
            } catch (Throwable th) {
                this.restartOffset = 0L;
                throw th;
            }
        } else {
            Socket socketOpenDataConnection2 = openDataConnection("RETR " + str);
            getTransferSize();
            InputStream inputStreamCreateInputStream2 = createInputStream(socketOpenDataConnection2.getInputStream());
            byte[] bArr2 = new byte[1500 * 10];
            while (true) {
                int i3 = inputStreamCreateInputStream2.read(bArr2);
                if (i3 < 0) {
                    break;
                }
                if (i3 > 0) {
                    outputStream.write(bArr2, 0, i3);
                }
            }
            inputStreamCreateInputStream2.close();
        }
        return completePending();
    }

    @Override // sun.net.ftp.FtpClient
    public InputStream getFileStream(String str) throws FtpProtocolException, IOException {
        if (this.restartOffset > 0) {
            try {
                Socket socketOpenDataConnection = openDataConnection("REST " + this.restartOffset);
                if (socketOpenDataConnection == null) {
                    return null;
                }
                issueCommandCheck("RETR " + str);
                getTransferSize();
                return createInputStream(socketOpenDataConnection.getInputStream());
            } finally {
                this.restartOffset = 0L;
            }
        }
        Socket socketOpenDataConnection2 = openDataConnection("RETR " + str);
        if (socketOpenDataConnection2 == null) {
            return null;
        }
        getTransferSize();
        return createInputStream(socketOpenDataConnection2.getInputStream());
    }

    @Override // sun.net.ftp.FtpClient
    public OutputStream putFileStream(String str, boolean z2) throws FtpProtocolException, IOException {
        Socket socketOpenDataConnection = openDataConnection((z2 ? "STOU " : "STOR ") + str);
        if (socketOpenDataConnection == null) {
            return null;
        }
        return new TelnetOutputStream(socketOpenDataConnection.getOutputStream(), this.type == FtpClient.TransferType.BINARY);
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient putFile(String str, InputStream inputStream, boolean z2) throws FtpProtocolException, IOException {
        String str2 = z2 ? "STOU " : "STOR ";
        if (this.type == FtpClient.TransferType.BINARY) {
            OutputStream outputStreamCreateOutputStream = createOutputStream(openDataConnection(str2 + str).getOutputStream());
            byte[] bArr = new byte[1500 * 10];
            while (true) {
                int i2 = inputStream.read(bArr);
                if (i2 < 0) {
                    break;
                }
                if (i2 > 0) {
                    outputStreamCreateOutputStream.write(bArr, 0, i2);
                }
            }
            outputStreamCreateOutputStream.close();
        }
        return completePending();
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient appendFile(String str, InputStream inputStream) throws FtpProtocolException, IOException {
        OutputStream outputStreamCreateOutputStream = createOutputStream(openDataConnection("APPE " + str).getOutputStream());
        byte[] bArr = new byte[1500 * 10];
        while (true) {
            int i2 = inputStream.read(bArr);
            if (i2 >= 0) {
                if (i2 > 0) {
                    outputStreamCreateOutputStream.write(bArr, 0, i2);
                }
            } else {
                outputStreamCreateOutputStream.close();
                return completePending();
            }
        }
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient rename(String str, String str2) throws FtpProtocolException, IOException {
        issueCommandCheck("RNFR " + str);
        issueCommandCheck("RNTO " + str2);
        return this;
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient deleteFile(String str) throws FtpProtocolException, IOException {
        issueCommandCheck("DELE " + str);
        return this;
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient makeDirectory(String str) throws FtpProtocolException, IOException {
        issueCommandCheck("MKD " + str);
        return this;
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient removeDirectory(String str) throws FtpProtocolException, IOException {
        issueCommandCheck("RMD " + str);
        return this;
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient noop() throws FtpProtocolException, IOException {
        issueCommandCheck("NOOP");
        return this;
    }

    @Override // sun.net.ftp.FtpClient
    public String getStatus(String str) throws FtpProtocolException, IOException {
        issueCommandCheck(str == null ? "STAT" : "STAT " + str);
        Vector<String> responseStrings = getResponseStrings();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 1; i2 < responseStrings.size() - 1; i2++) {
            stringBuffer.append(responseStrings.get(i2));
        }
        return stringBuffer.toString();
    }

    @Override // sun.net.ftp.FtpClient
    public List<String> getFeatures() throws FtpProtocolException, IOException {
        ArrayList arrayList = new ArrayList();
        issueCommandCheck("FEAT");
        Vector<String> responseStrings = getResponseStrings();
        for (int i2 = 1; i2 < responseStrings.size() - 1; i2++) {
            String str = responseStrings.get(i2);
            arrayList.add(str.substring(1, str.length() - 1));
        }
        return arrayList;
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient abort() throws FtpProtocolException, IOException {
        issueCommandCheck("ABOR");
        return this;
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient completePending() throws FtpProtocolException, IOException {
        while (this.replyPending) {
            this.replyPending = false;
            if (!readReply()) {
                throw new FtpProtocolException(getLastResponseString(), this.lastReplyCode);
            }
        }
        return this;
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient reInit() throws FtpProtocolException, IOException {
        issueCommandCheck("REIN");
        this.loggedIn = false;
        if (this.useCrypto && (this.server instanceof SSLSocket)) {
            ((SSLSocket) this.server).getSession().invalidate();
            this.server = this.oldSocket;
            this.oldSocket = null;
            try {
                this.out = new PrintStream((OutputStream) new BufferedOutputStream(this.server.getOutputStream()), true, encoding);
                this.in = new BufferedInputStream(this.server.getInputStream());
            } catch (UnsupportedEncodingException e2) {
                throw new InternalError(encoding + "encoding not found", e2);
            }
        }
        this.useCrypto = false;
        return this;
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient setType(FtpClient.TransferType transferType) throws FtpProtocolException, IOException {
        String str = "NOOP";
        this.type = transferType;
        if (transferType == FtpClient.TransferType.ASCII) {
            str = "TYPE A";
        }
        if (transferType == FtpClient.TransferType.BINARY) {
            str = "TYPE I";
        }
        if (transferType == FtpClient.TransferType.EBCDIC) {
            str = "TYPE E";
        }
        issueCommandCheck(str);
        return this;
    }

    @Override // sun.net.ftp.FtpClient
    public InputStream list(String str) throws FtpProtocolException, IOException {
        Socket socketOpenDataConnection = openDataConnection(str == null ? "LIST" : "LIST " + str);
        if (socketOpenDataConnection != null) {
            return createInputStream(socketOpenDataConnection.getInputStream());
        }
        return null;
    }

    @Override // sun.net.ftp.FtpClient
    public InputStream nameList(String str) throws FtpProtocolException, IOException {
        Socket socketOpenDataConnection = openDataConnection(str == null ? "NLST" : "NLST " + str);
        if (socketOpenDataConnection != null) {
            return createInputStream(socketOpenDataConnection.getInputStream());
        }
        return null;
    }

    @Override // sun.net.ftp.FtpClient
    public long getSize(String str) throws FtpProtocolException, IOException {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("path can't be null or empty");
        }
        issueCommandCheck("SIZE " + str);
        if (this.lastReplyCode == FtpReplyCode.FILE_STATUS) {
            String responseString = getResponseString();
            return Long.parseLong(responseString.substring(4, responseString.length() - 1));
        }
        return -1L;
    }

    @Override // sun.net.ftp.FtpClient
    public Date getLastModified(String str) throws FtpProtocolException, IOException {
        issueCommandCheck("MDTM " + str);
        if (this.lastReplyCode == FtpReplyCode.FILE_STATUS) {
            String strSubstring = getResponseString().substring(4);
            Date date = null;
            for (SimpleDateFormat simpleDateFormat : dateFormats) {
                try {
                    date = simpleDateFormat.parse(strSubstring);
                } catch (ParseException e2) {
                }
                if (date != null) {
                    return date;
                }
            }
            return null;
        }
        return null;
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient setDirParser(FtpDirParser ftpDirParser) {
        this.parser = ftpDirParser;
        return this;
    }

    /* loaded from: rt.jar:sun/net/ftp/impl/FtpClient$FtpFileIterator.class */
    private class FtpFileIterator implements Iterator<FtpDirEntry>, Closeable {
        private BufferedReader in;
        private FtpDirParser fparser;
        private FtpDirEntry nextFile = null;
        private boolean eof = false;

        public FtpFileIterator(FtpDirParser ftpDirParser, BufferedReader bufferedReader) {
            this.in = null;
            this.fparser = null;
            this.in = bufferedReader;
            this.fparser = ftpDirParser;
            readNext();
        }

        private void readNext() {
            String line;
            this.nextFile = null;
            if (this.eof) {
                return;
            }
            do {
                try {
                    line = this.in.readLine();
                    if (line != null) {
                        this.nextFile = this.fparser.parseLine(line);
                        if (this.nextFile != null) {
                            return;
                        }
                    }
                } catch (IOException e2) {
                }
            } while (line != null);
            this.in.close();
            this.eof = true;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.nextFile != null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public FtpDirEntry next() {
            FtpDirEntry ftpDirEntry = this.nextFile;
            readNext();
            return ftpDirEntry;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (this.in != null && !this.eof) {
                this.in.close();
            }
            this.eof = true;
            this.nextFile = null;
        }
    }

    @Override // sun.net.ftp.FtpClient
    public Iterator<FtpDirEntry> listFiles(String str) throws FtpProtocolException, IOException {
        String str2;
        Socket socketOpenDataConnection = null;
        if (str == null) {
            str2 = "MLSD";
        } else {
            try {
                str2 = "MLSD " + str;
            } catch (FtpProtocolException e2) {
            }
        }
        socketOpenDataConnection = openDataConnection(str2);
        if (socketOpenDataConnection != null) {
            return new FtpFileIterator(this.mlsxParser, new BufferedReader(new InputStreamReader(socketOpenDataConnection.getInputStream())));
        }
        Socket socketOpenDataConnection2 = openDataConnection(str == null ? "LIST" : "LIST " + str);
        if (socketOpenDataConnection2 != null) {
            return new FtpFileIterator(this.parser, new BufferedReader(new InputStreamReader(socketOpenDataConnection2.getInputStream())));
        }
        return null;
    }

    private boolean sendSecurityData(byte[] bArr) throws FtpProtocolException, IOException {
        return issueCommand("ADAT " + new BASE64Encoder().encode(bArr));
    }

    private byte[] getSecurityData() {
        String lastResponseString = getLastResponseString();
        if (lastResponseString.substring(4, 9).equalsIgnoreCase("ADAT=")) {
            try {
                return new BASE64Decoder().decodeBuffer(lastResponseString.substring(9, lastResponseString.length() - 1));
            } catch (IOException e2) {
                return null;
            }
        }
        return null;
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient useKerberos() throws FtpProtocolException, IOException {
        return this;
    }

    @Override // sun.net.ftp.FtpClient
    public String getWelcomeMsg() {
        return this.welcomeMsg;
    }

    @Override // sun.net.ftp.FtpClient
    public FtpReplyCode getLastReplyCode() {
        return this.lastReplyCode;
    }

    @Override // sun.net.ftp.FtpClient
    public String getLastResponseString() {
        StringBuffer stringBuffer = new StringBuffer();
        if (this.serverResponse != null) {
            Iterator<String> it = this.serverResponse.iterator();
            while (it.hasNext()) {
                String next = it.next();
                if (next != null) {
                    stringBuffer.append(next);
                }
            }
        }
        return stringBuffer.toString();
    }

    @Override // sun.net.ftp.FtpClient
    public long getLastTransferSize() {
        return this.lastTransSize;
    }

    @Override // sun.net.ftp.FtpClient
    public String getLastFileName() {
        return this.lastFileName;
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient startSecureSession() throws FtpProtocolException, IOException {
        if (!isConnected()) {
            throw new FtpProtocolException("Not connected yet", FtpReplyCode.BAD_SEQUENCE);
        }
        if (this.sslFact == null) {
            try {
                this.sslFact = (SSLSocketFactory) SSLSocketFactory.getDefault();
            } catch (Exception e2) {
                throw new IOException(e2.getLocalizedMessage());
            }
        }
        issueCommandCheck("AUTH TLS");
        try {
            Socket socketCreateSocket = this.sslFact.createSocket(this.server, this.serverAddr.getHostName(), this.serverAddr.getPort(), true);
            this.oldSocket = this.server;
            this.server = socketCreateSocket;
            try {
                this.out = new PrintStream((OutputStream) new BufferedOutputStream(this.server.getOutputStream()), true, encoding);
                this.in = new BufferedInputStream(this.server.getInputStream());
                issueCommandCheck("PBSZ 0");
                issueCommandCheck("PROT P");
                this.useCrypto = true;
                return this;
            } catch (UnsupportedEncodingException e3) {
                throw new InternalError(encoding + "encoding not found", e3);
            }
        } catch (SSLException e4) {
            try {
                disconnect();
            } catch (Exception e5) {
            }
            throw e4;
        }
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient endSecureSession() throws FtpProtocolException, IOException {
        if (!this.useCrypto) {
            return this;
        }
        issueCommandCheck("CCC");
        issueCommandCheck("PROT C");
        this.useCrypto = false;
        this.server = this.oldSocket;
        this.oldSocket = null;
        try {
            this.out = new PrintStream((OutputStream) new BufferedOutputStream(this.server.getOutputStream()), true, encoding);
            this.in = new BufferedInputStream(this.server.getInputStream());
            return this;
        } catch (UnsupportedEncodingException e2) {
            throw new InternalError(encoding + "encoding not found", e2);
        }
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient allocate(long j2) throws FtpProtocolException, IOException {
        issueCommandCheck("ALLO " + j2);
        return this;
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient structureMount(String str) throws FtpProtocolException, IOException {
        issueCommandCheck("SMNT " + str);
        return this;
    }

    @Override // sun.net.ftp.FtpClient
    public String getSystem() throws FtpProtocolException, IOException {
        issueCommandCheck("SYST");
        return getResponseString().substring(4);
    }

    @Override // sun.net.ftp.FtpClient
    public String getHelp(String str) throws FtpProtocolException, IOException {
        issueCommandCheck("HELP " + str);
        Vector<String> responseStrings = getResponseStrings();
        if (responseStrings.size() == 1) {
            return responseStrings.get(0).substring(4);
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 1; i2 < responseStrings.size() - 1; i2++) {
            stringBuffer.append(responseStrings.get(i2).substring(3));
        }
        return stringBuffer.toString();
    }

    @Override // sun.net.ftp.FtpClient
    public sun.net.ftp.FtpClient siteCmd(String str) throws FtpProtocolException, IOException {
        issueCommandCheck("SITE " + str);
        return this;
    }
}
