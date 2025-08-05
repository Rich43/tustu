package jdk.jfr.internal;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Supplier;
import jdk.jfr.internal.SecuritySupport;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: jfr.jar:jdk/jfr/internal/RepositoryChunk.class */
public final class RepositoryChunk {
    private static final int MAX_CHUNK_NAMES = 100;
    static final Comparator<RepositoryChunk> END_TIME_COMPARATOR = new Comparator<RepositoryChunk>() { // from class: jdk.jfr.internal.RepositoryChunk.1
        @Override // java.util.Comparator
        public int compare(RepositoryChunk repositoryChunk, RepositoryChunk repositoryChunk2) {
            return repositoryChunk.endTime.compareTo(repositoryChunk2.endTime);
        }
    };
    private final SecuritySupport.SafePath repositoryPath;
    private final SecuritySupport.SafePath unFinishedFile;
    private final SecuritySupport.SafePath file;
    private final Instant startTime;
    private final RandomAccessFile unFinishedRAF;
    private Instant endTime = null;
    private int refCount = 0;
    private long size;

    RepositoryChunk(SecuritySupport.SafePath safePath, Instant instant) throws Exception {
        String str = Repository.REPO_DATE_FORMAT.format(LocalDateTime.ofInstant(instant, ZonedDateTime.now().getZone()));
        this.startTime = instant;
        this.repositoryPath = safePath;
        this.unFinishedFile = findFileName(this.repositoryPath, str, ".part");
        this.file = findFileName(this.repositoryPath, str, ".jfr");
        this.unFinishedRAF = SecuritySupport.createRandomAccessFile(this.unFinishedFile);
        SecuritySupport.touch(this.file);
    }

    private static SecuritySupport.SafePath findFileName(SecuritySupport.SafePath safePath, String str, String str2) throws Exception {
        Path pathResolve = safePath.toPath().resolve(str + str2);
        for (int i2 = 1; i2 < 100; i2++) {
            SecuritySupport.SafePath safePath2 = new SecuritySupport.SafePath(pathResolve);
            if (!SecuritySupport.exists(safePath2)) {
                return safePath2;
            }
            pathResolve = safePath.toPath().resolve(String.format("%s_%02d%s", str, Integer.valueOf(i2), str2));
        }
        return SecuritySupport.toRealPath(new SecuritySupport.SafePath(safePath.toPath().resolve(str + "_" + System.currentTimeMillis() + str2)));
    }

    public SecuritySupport.SafePath getUnfishedFile() {
        return this.unFinishedFile;
    }

    void finish(Instant instant) {
        try {
            finishWithException(instant);
        } catch (IOException e2) {
            Logger.log(LogTag.JFR, LogLevel.ERROR, "Could not finish chunk. " + e2.getMessage());
        }
    }

    private void finishWithException(Instant instant) throws IOException {
        this.unFinishedRAF.close();
        this.size = finish(this.unFinishedFile, this.file);
        this.endTime = instant;
        Logger.log(LogTag.JFR_SYSTEM, LogLevel.DEBUG, (Supplier<String>) () -> {
            return "Chunk finished: " + ((Object) this.file);
        });
    }

    private static long finish(SecuritySupport.SafePath safePath, SecuritySupport.SafePath safePath2) throws IOException {
        Objects.requireNonNull(safePath);
        Objects.requireNonNull(safePath2);
        SecuritySupport.delete(safePath2);
        SecuritySupport.moveReplace(safePath, safePath2);
        return SecuritySupport.getFileSize(safePath2);
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public Instant getEndTime() {
        return this.endTime;
    }

    private void delete(SecuritySupport.SafePath safePath) {
        try {
            SecuritySupport.delete(safePath);
            Logger.log(LogTag.JFR, LogLevel.DEBUG, (Supplier<String>) () -> {
                return "Repository chunk " + ((Object) safePath) + " deleted";
            });
        } catch (IOException e2) {
            Logger.log(LogTag.JFR, LogLevel.ERROR, (Supplier<String>) () -> {
                return "Repository chunk " + ((Object) safePath) + " could not be deleted: " + e2.getMessage();
            });
            if (safePath != null) {
                SecuritySupport.deleteOnExit(safePath);
            }
        }
    }

    private void destroy() {
        if (!isFinished()) {
            finish(Instant.MIN);
        }
        if (this.file != null) {
            delete(this.file);
        }
        try {
            this.unFinishedRAF.close();
        } catch (IOException e2) {
            Logger.log(LogTag.JFR, LogLevel.ERROR, (Supplier<String>) () -> {
                return "Could not close random access file: " + this.unFinishedFile.toString() + ". File will not be deleted due to: " + e2.getMessage();
            });
        }
    }

    public synchronized void use() {
        this.refCount++;
        Logger.log(LogTag.JFR_SYSTEM, LogLevel.DEBUG, (Supplier<String>) () -> {
            return "Use chunk " + toString() + " ref count now " + this.refCount;
        });
    }

    public synchronized void release() {
        this.refCount--;
        Logger.log(LogTag.JFR_SYSTEM, LogLevel.DEBUG, (Supplier<String>) () -> {
            return "Release chunk " + toString() + " ref count now " + this.refCount;
        });
        if (this.refCount == 0) {
            destroy();
        }
    }

    protected void finalize() {
        boolean z2 = false;
        synchronized (this) {
            if (this.refCount > 0) {
                z2 = true;
            }
        }
        if (z2) {
            destroy();
        }
    }

    public long getSize() {
        return this.size;
    }

    public boolean isFinished() {
        return this.endTime != null;
    }

    public String toString() {
        if (isFinished()) {
            return this.file.toString();
        }
        return this.unFinishedFile.toString();
    }

    ReadableByteChannel newChannel() throws IOException {
        if (!isFinished()) {
            throw new IOException("Chunk not finished");
        }
        return SecuritySupport.newFileChannelToRead(this.file);
    }

    public boolean inInterval(Instant instant, Instant instant2) {
        if (instant != null && getEndTime().isBefore(instant)) {
            return false;
        }
        if (instant2 != null && getStartTime().isAfter(instant2)) {
            return false;
        }
        return true;
    }

    public SecuritySupport.SafePath getFile() {
        return this.file;
    }
}
