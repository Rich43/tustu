package sun.net.ftp;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/* loaded from: rt.jar:sun/net/ftp/FtpClient.class */
public abstract class FtpClient implements Closeable {
    private static final int FTP_PORT = 21;

    /* loaded from: rt.jar:sun/net/ftp/FtpClient$TransferType.class */
    public enum TransferType {
        ASCII,
        BINARY,
        EBCDIC
    }

    public abstract FtpClient enablePassiveMode(boolean z2);

    public abstract boolean isPassiveModeEnabled();

    public abstract FtpClient setConnectTimeout(int i2);

    public abstract int getConnectTimeout();

    public abstract FtpClient setReadTimeout(int i2);

    public abstract int getReadTimeout();

    public abstract FtpClient setProxy(Proxy proxy);

    public abstract Proxy getProxy();

    public abstract boolean isConnected();

    public abstract FtpClient connect(SocketAddress socketAddress) throws FtpProtocolException, IOException;

    public abstract FtpClient connect(SocketAddress socketAddress, int i2) throws FtpProtocolException, IOException;

    public abstract SocketAddress getServerAddress();

    public abstract FtpClient login(String str, char[] cArr) throws FtpProtocolException, IOException;

    public abstract FtpClient login(String str, char[] cArr, String str2) throws FtpProtocolException, IOException;

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public abstract void close() throws IOException;

    public abstract boolean isLoggedIn();

    public abstract FtpClient changeDirectory(String str) throws FtpProtocolException, IOException;

    public abstract FtpClient changeToParentDirectory() throws FtpProtocolException, IOException;

    public abstract String getWorkingDirectory() throws FtpProtocolException, IOException;

    public abstract FtpClient setRestartOffset(long j2);

    public abstract FtpClient getFile(String str, OutputStream outputStream) throws FtpProtocolException, IOException;

    public abstract InputStream getFileStream(String str) throws FtpProtocolException, IOException;

    public abstract OutputStream putFileStream(String str, boolean z2) throws FtpProtocolException, IOException;

    public abstract FtpClient putFile(String str, InputStream inputStream, boolean z2) throws FtpProtocolException, IOException;

    public abstract FtpClient appendFile(String str, InputStream inputStream) throws FtpProtocolException, IOException;

    public abstract FtpClient rename(String str, String str2) throws FtpProtocolException, IOException;

    public abstract FtpClient deleteFile(String str) throws FtpProtocolException, IOException;

    public abstract FtpClient makeDirectory(String str) throws FtpProtocolException, IOException;

    public abstract FtpClient removeDirectory(String str) throws FtpProtocolException, IOException;

    public abstract FtpClient noop() throws FtpProtocolException, IOException;

    public abstract String getStatus(String str) throws FtpProtocolException, IOException;

    public abstract List<String> getFeatures() throws FtpProtocolException, IOException;

    public abstract FtpClient abort() throws FtpProtocolException, IOException;

    public abstract FtpClient completePending() throws FtpProtocolException, IOException;

    public abstract FtpClient reInit() throws FtpProtocolException, IOException;

    public abstract FtpClient setType(TransferType transferType) throws FtpProtocolException, IOException;

    public abstract InputStream list(String str) throws FtpProtocolException, IOException;

    public abstract InputStream nameList(String str) throws FtpProtocolException, IOException;

    public abstract long getSize(String str) throws FtpProtocolException, IOException;

    public abstract Date getLastModified(String str) throws FtpProtocolException, IOException;

    public abstract FtpClient setDirParser(FtpDirParser ftpDirParser);

    public abstract Iterator<FtpDirEntry> listFiles(String str) throws FtpProtocolException, IOException;

    public abstract FtpClient useKerberos() throws FtpProtocolException, IOException;

    public abstract String getWelcomeMsg();

    public abstract FtpReplyCode getLastReplyCode();

    public abstract String getLastResponseString();

    public abstract long getLastTransferSize();

    public abstract String getLastFileName();

    public abstract FtpClient startSecureSession() throws FtpProtocolException, IOException;

    public abstract FtpClient endSecureSession() throws FtpProtocolException, IOException;

    public abstract FtpClient allocate(long j2) throws FtpProtocolException, IOException;

    public abstract FtpClient structureMount(String str) throws FtpProtocolException, IOException;

    public abstract String getSystem() throws FtpProtocolException, IOException;

    public abstract String getHelp(String str) throws FtpProtocolException, IOException;

    public abstract FtpClient siteCmd(String str) throws FtpProtocolException, IOException;

    public static final int defaultPort() {
        return 21;
    }

    protected FtpClient() {
    }

    public static FtpClient create() {
        return FtpClientProvider.provider().createFtpClient();
    }

    public static FtpClient create(InetSocketAddress inetSocketAddress) throws FtpProtocolException, IOException {
        FtpClient ftpClientCreate = create();
        if (inetSocketAddress != null) {
            ftpClientCreate.connect(inetSocketAddress);
        }
        return ftpClientCreate;
    }

    public static FtpClient create(String str) throws FtpProtocolException, IOException {
        return create(new InetSocketAddress(str, 21));
    }

    public OutputStream putFileStream(String str) throws FtpProtocolException, IOException {
        return putFileStream(str, false);
    }

    public FtpClient putFile(String str, InputStream inputStream) throws FtpProtocolException, IOException {
        return putFile(str, inputStream, false);
    }

    public FtpClient setBinaryType() throws FtpProtocolException, IOException {
        setType(TransferType.BINARY);
        return this;
    }

    public FtpClient setAsciiType() throws FtpProtocolException, IOException {
        setType(TransferType.ASCII);
        return this;
    }
}
