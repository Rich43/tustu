package jdk.jfr.internal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.concurrent.Callable;

/* loaded from: jfr.jar:jdk/jfr/internal/WriteableUserPath.class */
public final class WriteableUserPath {
    private final AccessControlContext controlContext = AccessController.getContext();
    private final Path original;
    private final Path real;
    private final String realPathText;
    private final String originalText;
    private volatile boolean inPrivileged;

    public WriteableUserPath(Path path) throws IOException {
        if (Files.exists(path, new LinkOption[0]) && !Files.isWritable(path)) {
            throw new FileNotFoundException("Could not write to file: " + ((Object) path.toAbsolutePath()));
        }
        Files.newBufferedWriter(path, new OpenOption[0]).close();
        this.original = path;
        this.originalText = path.toString();
        this.real = path.toRealPath(new LinkOption[0]);
        this.realPathText = this.real.toString();
    }

    public Path getPotentiallyMaliciousOriginal() {
        return this.original;
    }

    public String getRealPathText() {
        return this.realPathText;
    }

    public String getOriginalText() {
        return this.originalText;
    }

    public Path getReal() {
        if (!this.inPrivileged) {
            throw new InternalError("A user path was accessed outside the context it was supplied in");
        }
        return this.real;
    }

    public void doPriviligedIO(final Callable<?> callable) throws IOException {
        IOException iOException;
        try {
            try {
                this.inPrivileged = true;
                AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: jdk.jfr.internal.WriteableUserPath.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public Void run() throws Exception {
                        callable.call();
                        return null;
                    }
                }, this.controlContext);
                this.inPrivileged = false;
            } finally {
            }
        } catch (Throwable th) {
            this.inPrivileged = false;
            throw th;
        }
    }
}
