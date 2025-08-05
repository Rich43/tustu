package com.sun.xml.internal.org.jvnet.mimepull;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/WeakDataFile.class */
final class WeakDataFile extends WeakReference<DataFile> {
    private static final Logger LOGGER = Logger.getLogger(WeakDataFile.class.getName());
    private static ReferenceQueue<DataFile> refQueue = new ReferenceQueue<>();
    private static List<WeakDataFile> refList = new ArrayList();
    private final File file;
    private final RandomAccessFile raf;
    private static boolean hasCleanUpExecutor;

    static {
        hasCleanUpExecutor = false;
        CleanUpExecutorFactory executorFactory = CleanUpExecutorFactory.newInstance();
        if (executorFactory != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Initializing clean up executor for MIMEPULL: {0}", executorFactory.getClass().getName());
            }
            Executor executor = executorFactory.getExecutor();
            executor.execute(new Runnable() { // from class: com.sun.xml.internal.org.jvnet.mimepull.WeakDataFile.1
                @Override // java.lang.Runnable
                public void run() {
                    while (true) {
                        try {
                            WeakDataFile weak = (WeakDataFile) WeakDataFile.refQueue.remove();
                            if (WeakDataFile.LOGGER.isLoggable(Level.FINE)) {
                                WeakDataFile.LOGGER.log(Level.FINE, "Cleaning file = {0} from reference queue.", weak.file);
                            }
                            weak.close();
                        } catch (InterruptedException e2) {
                        }
                    }
                }
            });
            hasCleanUpExecutor = true;
        }
    }

    WeakDataFile(DataFile df, File file) {
        super(df, refQueue);
        refList.add(this);
        this.file = file;
        try {
            this.raf = new RandomAccessFile(file, InternalZipConstants.WRITE_MODE);
            if (!hasCleanUpExecutor) {
                drainRefQueueBounded();
            }
        } catch (IOException ioe) {
            throw new MIMEParsingException(ioe);
        }
    }

    synchronized void read(long pointer, byte[] buf, int offset, int length) {
        try {
            this.raf.seek(pointer);
            this.raf.readFully(buf, offset, length);
        } catch (IOException ioe) {
            throw new MIMEParsingException(ioe);
        }
    }

    synchronized long writeTo(long pointer, byte[] data, int offset, int length) {
        try {
            this.raf.seek(pointer);
            this.raf.write(data, offset, length);
            return this.raf.getFilePointer();
        } catch (IOException ioe) {
            throw new MIMEParsingException(ioe);
        }
    }

    void close() {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Deleting file = {0}", this.file.getName());
        }
        refList.remove(this);
        try {
            this.raf.close();
            boolean deleted = this.file.delete();
            if (!deleted && LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "File {0} was not deleted", this.file.getAbsolutePath());
            }
        } catch (IOException ioe) {
            throw new MIMEParsingException(ioe);
        }
    }

    void renameTo(File f2) {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Moving file={0} to={1}", new Object[]{this.file, f2});
        }
        refList.remove(this);
        try {
            this.raf.close();
            boolean renamed = this.file.renameTo(f2);
            if (!renamed && LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "File {0} was not moved to {1}", new Object[]{this.file.getAbsolutePath(), f2.getAbsolutePath()});
            }
        } catch (IOException ioe) {
            throw new MIMEParsingException(ioe);
        }
    }

    static void drainRefQueueBounded() {
        while (true) {
            WeakDataFile weak = (WeakDataFile) refQueue.poll();
            if (weak != null) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "Cleaning file = {0} from reference queue.", weak.file);
                }
                weak.close();
            } else {
                return;
            }
        }
    }
}
