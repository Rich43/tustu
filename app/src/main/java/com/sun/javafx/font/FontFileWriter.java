package com.sun.javafx.font;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import net.lingala.zip4j.util.InternalZipConstants;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: jfxrt.jar:com/sun/javafx/font/FontFileWriter.class */
class FontFileWriter implements FontConstants {
    byte[] header;
    int pos;
    int headerPos;
    int writtenBytes;
    FontTracker tracker;
    File file;
    RandomAccessFile raFile;

    public FontFileWriter() {
        if (!hasTempPermission()) {
            this.tracker = FontTracker.getTracker();
        }
    }

    protected void setLength(int size) throws IOException {
        if (this.raFile == null) {
            throw new IOException("File not open");
        }
        checkTracker(size);
        this.raFile.setLength(size);
    }

    public void seek(int pos) throws IOException {
        if (this.raFile == null) {
            throw new IOException("File not open");
        }
        if (pos != this.pos) {
            this.raFile.seek(pos);
            this.pos = pos;
        }
    }

    public File getFile() {
        return this.file;
    }

    public File openFile() throws PrivilegedActionException {
        this.pos = 0;
        this.writtenBytes = 0;
        this.file = (File) AccessController.doPrivileged(() -> {
            try {
                return Files.createTempFile("+JXF", ".tmp", new FileAttribute[0]).toFile();
            } catch (IOException e2) {
                throw new IOException("Unable to create temporary file");
            }
        });
        if (this.tracker != null) {
            this.tracker.add(this.file);
        }
        this.raFile = (RandomAccessFile) AccessController.doPrivileged(() -> {
            return new RandomAccessFile(this.file, InternalZipConstants.WRITE_MODE);
        });
        if (this.tracker != null) {
            this.tracker.set(this.file, this.raFile);
        }
        if (PrismFontFactory.debugFonts) {
            System.err.println("Temp file created: " + this.file.getPath());
        }
        return this.file;
    }

    public void closeFile() throws IOException {
        if (this.header != null) {
            this.raFile.seek(0L);
            this.raFile.write(this.header);
            this.header = null;
        }
        if (this.raFile != null) {
            this.raFile.close();
            this.raFile = null;
        }
        if (this.tracker != null) {
            this.tracker.remove(this.file);
        }
    }

    public void deleteFile() {
        if (this.file != null) {
            if (this.tracker != null) {
                this.tracker.subBytes(this.writtenBytes);
            }
            try {
                closeFile();
            } catch (Exception e2) {
            }
            try {
                AccessController.doPrivileged(() -> {
                    this.file.delete();
                    return null;
                });
                if (PrismFontFactory.debugFonts) {
                    System.err.println("Temp file delete: " + this.file.getPath());
                }
            } catch (Exception e3) {
            }
            this.file = null;
            this.raFile = null;
        }
    }

    public boolean isTracking() {
        return this.tracker != null;
    }

    private void checkTracker(int size) throws IOException {
        if (this.tracker != null) {
            if (size < 0 || this.pos > 33554432 - size) {
                throw new IOException("File too big.");
            }
            if (this.tracker.getNumBytes() > 335544320 - size) {
                throw new IOException("Total files too big.");
            }
        }
    }

    private void checkSize(int size) throws IOException {
        if (this.tracker != null) {
            checkTracker(size);
            this.tracker.addBytes(size);
            this.writtenBytes += size;
        }
    }

    private void setHeaderPos(int pos) {
        this.headerPos = pos;
    }

    public void writeHeader(int format, short numTables) throws IOException {
        int size = 12 + (16 * numTables);
        checkSize(size);
        this.header = new byte[size];
        short maxPower2 = (short) (numTables | (numTables >> 1));
        short maxPower22 = (short) (maxPower2 | (maxPower2 >> 2));
        short maxPower23 = (short) (maxPower22 | (maxPower22 >> 4));
        short maxPower24 = (short) (maxPower23 | (maxPower23 >> 8));
        short maxPower25 = (short) (maxPower24 & ((maxPower24 >> 1) ^ (-1)));
        short searchRange = (short) (maxPower25 * 16);
        short entrySelector = 0;
        while (maxPower25 > 1) {
            entrySelector = (short) (entrySelector + 1);
            maxPower25 = (short) (maxPower25 >> 1);
        }
        short rangeShift = (short) ((numTables * 16) - searchRange);
        setHeaderPos(0);
        writeInt(format);
        writeShort(numTables);
        writeShort(searchRange);
        writeShort(entrySelector);
        writeShort(rangeShift);
    }

    public void writeDirectoryEntry(int index, int tag, int checksum, int offset, int length) throws IOException {
        setHeaderPos(12 + (16 * index));
        writeInt(tag);
        writeInt(checksum);
        writeInt(offset);
        writeInt(length);
    }

    private void writeInt(int value) throws IOException {
        byte[] bArr = this.header;
        int i2 = this.headerPos;
        this.headerPos = i2 + 1;
        bArr[i2] = (byte) ((value & (-16777216)) >> 24);
        byte[] bArr2 = this.header;
        int i3 = this.headerPos;
        this.headerPos = i3 + 1;
        bArr2[i3] = (byte) ((value & 16711680) >> 16);
        byte[] bArr3 = this.header;
        int i4 = this.headerPos;
        this.headerPos = i4 + 1;
        bArr3[i4] = (byte) ((value & NormalizerImpl.CC_MASK) >> 8);
        byte[] bArr4 = this.header;
        int i5 = this.headerPos;
        this.headerPos = i5 + 1;
        bArr4[i5] = (byte) (value & 255);
    }

    private void writeShort(short value) throws IOException {
        byte[] bArr = this.header;
        int i2 = this.headerPos;
        this.headerPos = i2 + 1;
        bArr[i2] = (byte) ((value & 65280) >> 8);
        byte[] bArr2 = this.header;
        int i3 = this.headerPos;
        this.headerPos = i3 + 1;
        bArr2[i3] = (byte) (value & 255);
    }

    public void writeBytes(byte[] buffer) throws IOException {
        writeBytes(buffer, 0, buffer.length);
    }

    public void writeBytes(byte[] buffer, int startPos, int length) throws IOException {
        checkSize(length);
        this.raFile.write(buffer, startPos, length);
        this.pos += length;
    }

    static boolean hasTempPermission() {
        if (System.getSecurityManager() == null) {
            return true;
        }
        boolean hasPerm = false;
        try {
            File f2 = Files.createTempFile("+JXF", ".tmp", new FileAttribute[0]).toFile();
            f2.delete();
            hasPerm = true;
        } catch (Throwable th) {
        }
        return hasPerm;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/font/FontFileWriter$FontTracker.class */
    static class FontTracker {
        public static final int MAX_FILE_SIZE = 33554432;
        public static final int MAX_TOTAL_BYTES = 335544320;
        static int numBytes;
        static FontTracker tracker;
        private static Semaphore cs = null;

        FontTracker() {
        }

        public static synchronized FontTracker getTracker() {
            if (tracker == null) {
                tracker = new FontTracker();
            }
            return tracker;
        }

        public synchronized int getNumBytes() {
            return numBytes;
        }

        public synchronized void addBytes(int sz) {
            numBytes += sz;
        }

        public synchronized void subBytes(int sz) {
            numBytes -= sz;
        }

        private static synchronized Semaphore getCS() {
            if (cs == null) {
                cs = new Semaphore(5, true);
            }
            return cs;
        }

        public boolean acquirePermit() throws InterruptedException {
            return getCS().tryAcquire(120L, TimeUnit.SECONDS);
        }

        public void releasePermit() {
            getCS().release();
        }

        public void add(File file) {
            TempFileDeletionHook.add(file);
        }

        public void set(File file, RandomAccessFile raf) {
            TempFileDeletionHook.set(file, raf);
        }

        public void remove(File file) {
            TempFileDeletionHook.remove(file);
        }

        /* loaded from: jfxrt.jar:com/sun/javafx/font/FontFileWriter$FontTracker$TempFileDeletionHook.class */
        private static class TempFileDeletionHook {
            private static HashMap<File, RandomAccessFile> files = new HashMap<>();

            /* renamed from: t, reason: collision with root package name */
            private static Thread f11879t = null;

            static void init() {
                if (f11879t == null) {
                    AccessController.doPrivileged(() -> {
                        f11879t = new Thread(() -> {
                            runHooks();
                        });
                        Runtime.getRuntime().addShutdownHook(f11879t);
                        return null;
                    });
                }
            }

            private TempFileDeletionHook() {
            }

            static synchronized void add(File file) {
                init();
                files.put(file, null);
            }

            static synchronized void set(File file, RandomAccessFile raf) {
                files.put(file, raf);
            }

            static synchronized void remove(File file) {
                files.remove(file);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public static synchronized void runHooks() {
                if (files.isEmpty()) {
                    return;
                }
                for (Map.Entry<File, RandomAccessFile> entry : files.entrySet()) {
                    try {
                        if (entry.getValue() != null) {
                            entry.getValue().close();
                        }
                    } catch (Exception e2) {
                    }
                    entry.getKey().delete();
                }
            }
        }
    }
}
