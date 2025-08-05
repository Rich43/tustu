package sun.net.ftp.impl;

import sun.net.ftp.FtpClientProvider;

/* loaded from: rt.jar:sun/net/ftp/impl/DefaultFtpClientProvider.class */
public class DefaultFtpClientProvider extends FtpClientProvider {
    @Override // sun.net.ftp.FtpClientProvider
    public sun.net.ftp.FtpClient createFtpClient() {
        return FtpClient.create();
    }
}
