package sun.security.action;

import java.io.File;
import java.io.FileInputStream;
import java.security.PrivilegedExceptionAction;

/* loaded from: rt.jar:sun/security/action/OpenFileInputStreamAction.class */
public class OpenFileInputStreamAction implements PrivilegedExceptionAction<FileInputStream> {
    private final File file;

    public OpenFileInputStreamAction(File file) {
        this.file = file;
    }

    public OpenFileInputStreamAction(String str) {
        this.file = new File(str);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.security.PrivilegedExceptionAction
    public FileInputStream run() throws Exception {
        return new FileInputStream(this.file);
    }
}
