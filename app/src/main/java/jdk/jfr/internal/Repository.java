package jdk.jfr.internal;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import jdk.jfr.internal.SecuritySupport;

/* loaded from: jfr.jar:jdk/jfr/internal/Repository.class */
public final class Repository {
    private static final int MAX_REPO_CREATION_RETRIES = 1000;
    private static final JVM jvm = JVM.getJVM();
    private static final Repository instance = new Repository();
    public static final DateTimeFormatter REPO_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
    private final Set<SecuritySupport.SafePath> cleanupDirectories = new HashSet();
    private SecuritySupport.SafePath baseLocation;
    private SecuritySupport.SafePath repository;

    private Repository() {
    }

    public static Repository getRepository() {
        return instance;
    }

    public synchronized void setBasePath(SecuritySupport.SafePath safePath) throws Exception {
        this.repository = createRepository(safePath);
        try {
            SecuritySupport.delete(this.repository);
        } catch (IOException e2) {
            Logger.log(LogTag.JFR, LogLevel.INFO, "Could not delete disk repository " + ((Object) this.repository));
        }
        this.baseLocation = safePath;
    }

    synchronized void ensureRepository() throws Exception {
        if (this.baseLocation == null) {
            setBasePath(SecuritySupport.JAVA_IO_TMPDIR);
        }
    }

    synchronized RepositoryChunk newChunk(Instant instant) {
        try {
            if (!SecuritySupport.existDirectory(this.repository)) {
                this.repository = createRepository(this.baseLocation);
                jvm.setRepositoryLocation(this.repository.toString());
                this.cleanupDirectories.add(this.repository);
            }
            return new RepositoryChunk(this.repository, instant);
        } catch (Exception e2) {
            String str = String.format("Could not create chunk in repository %s, %s", this.repository, e2.getMessage());
            Logger.log(LogTag.JFR, LogLevel.ERROR, str);
            jvm.abort(str);
            throw new InternalError("Could not abort after JFR disk creation error");
        }
    }

    private static SecuritySupport.SafePath createRepository(SecuritySupport.SafePath safePath) throws Exception {
        SecuritySupport.SafePath safePathCreateRealBasePath = createRealBasePath(safePath);
        SecuritySupport.SafePath safePath2 = null;
        String str = REPO_DATE_FORMAT.format(LocalDateTime.now()) + "_" + JVM.getJVM().getPid();
        String str2 = str;
        int i2 = 0;
        while (i2 < 1000) {
            safePath2 = new SecuritySupport.SafePath(safePathCreateRealBasePath.toPath().resolve(str2));
            if (tryToUseAsRepository(safePath2)) {
                break;
            }
            str2 = str + "_" + i2;
            i2++;
        }
        if (i2 == 1000) {
            throw new Exception("Unable to create JFR repository directory using base location (" + ((Object) safePath) + ")");
        }
        return SecuritySupport.toRealPath(safePath2);
    }

    private static SecuritySupport.SafePath createRealBasePath(SecuritySupport.SafePath safePath) throws Exception {
        if (SecuritySupport.exists(safePath)) {
            if (!SecuritySupport.isWritable(safePath)) {
                throw new IOException("JFR repository directory (" + safePath.toString() + ") exists, but isn't writable");
            }
            return SecuritySupport.toRealPath(safePath);
        }
        return SecuritySupport.toRealPath(SecuritySupport.createDirectories(safePath));
    }

    private static boolean tryToUseAsRepository(SecuritySupport.SafePath safePath) {
        if (safePath.toPath().getParent() == null) {
            return false;
        }
        try {
            try {
                SecuritySupport.createDirectories(safePath);
            } catch (IOException e2) {
                return false;
            }
        } catch (Exception e3) {
        }
        if (!SecuritySupport.exists(safePath)) {
            return false;
        }
        if (!SecuritySupport.isDirectory(safePath)) {
            return false;
        }
        return true;
    }

    synchronized void clear() {
        for (SecuritySupport.SafePath safePath : this.cleanupDirectories) {
            try {
                SecuritySupport.clearDirectory(safePath);
                Logger.log(LogTag.JFR, LogLevel.INFO, "Removed repository " + ((Object) safePath));
            } catch (IOException e2) {
                Logger.log(LogTag.JFR, LogLevel.ERROR, "Repository " + ((Object) safePath) + " could not be removed at shutdown: " + e2.getMessage());
            }
        }
    }

    public synchronized SecuritySupport.SafePath getRepositoryPath() {
        return this.repository;
    }
}
