package sun.net.www.protocol.file;

import java.io.File;
import java.io.FilePermission;
import java.net.URL;
import java.security.Permission;

/* loaded from: rt.jar:sun/net/www/protocol/file/UNCFileURLConnection.class */
final class UNCFileURLConnection extends FileURLConnection {
    private final String effectivePath;
    private volatile Permission permission;

    UNCFileURLConnection(URL url, File file, String str) {
        super(url, file);
        this.effectivePath = str;
    }

    @Override // sun.net.www.protocol.file.FileURLConnection, java.net.URLConnection
    public Permission getPermission() {
        Permission permission = this.permission;
        if (permission == null) {
            FilePermission filePermission = new FilePermission(this.effectivePath, "read");
            permission = filePermission;
            this.permission = filePermission;
        }
        return permission;
    }
}
