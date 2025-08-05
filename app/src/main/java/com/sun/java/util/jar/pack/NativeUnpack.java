package com.sun.java.util.jar.pack;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;

/* loaded from: rt.jar:com/sun/java/util/jar/pack/NativeUnpack.class */
class NativeUnpack {
    private long unpackerPtr;
    private BufferedInputStream in;
    private int _verbose;
    private long _byteCount;
    private int _segCount;
    private int _fileCount;
    private long _estByteLimit;
    private int _estSegLimit;
    private int _estFileLimit;
    private int _prevPercent = -1;
    private final CRC32 _crc32 = new CRC32();
    private byte[] _buf = new byte[16384];
    private UnpackerImpl _p200;
    private PropMap _props;
    static final /* synthetic */ boolean $assertionsDisabled;

    private static native synchronized void initIDs();

    private native synchronized long start(ByteBuffer byteBuffer, long j2);

    private native synchronized boolean getNextFile(Object[] objArr);

    private native synchronized ByteBuffer getUnusedInput();

    private native synchronized long finish();

    protected native synchronized boolean setOption(String str, String str2);

    protected native synchronized String getOption(String str);

    static {
        $assertionsDisabled = !NativeUnpack.class.desiredAssertionStatus();
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: com.sun.java.util.jar.pack.NativeUnpack.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                System.loadLibrary("unpack");
                return null;
            }
        });
        initIDs();
    }

    NativeUnpack(UnpackerImpl unpackerImpl) {
        this._p200 = unpackerImpl;
        this._props = unpackerImpl.props;
        unpackerImpl._nunp = this;
    }

    private static Object currentInstance() {
        UnpackerImpl unpackerImpl = (UnpackerImpl) Utils.getTLGlobals();
        if (unpackerImpl == null) {
            return null;
        }
        return unpackerImpl._nunp;
    }

    private synchronized long getUnpackerPtr() {
        return this.unpackerPtr;
    }

    private long readInputFn(ByteBuffer byteBuffer, long j2) throws IOException {
        if (this.in == null) {
            return 0L;
        }
        long jCapacity = byteBuffer.capacity() - byteBuffer.position();
        if (!$assertionsDisabled && j2 > jCapacity) {
            throw new AssertionError();
        }
        long j3 = 0;
        int i2 = 0;
        while (j3 < j2) {
            i2++;
            int length = this._buf.length;
            if (length > jCapacity - j3) {
                length = (int) (jCapacity - j3);
            }
            int i3 = this.in.read(this._buf, 0, length);
            if (i3 <= 0) {
                break;
            }
            j3 += i3;
            if (!$assertionsDisabled && j3 > jCapacity) {
                throw new AssertionError();
            }
            byteBuffer.put(this._buf, 0, i3);
        }
        if (this._verbose > 1) {
            Utils.log.fine("readInputFn(" + j2 + "," + jCapacity + ") => " + j3 + " steps=" + i2);
        }
        if (jCapacity > 100) {
            this._estByteLimit = this._byteCount + jCapacity;
        } else {
            this._estByteLimit = (this._byteCount + j3) * 20;
        }
        this._byteCount += j3;
        updateProgress();
        return j3;
    }

    private void updateProgress() {
        double d2 = this._segCount;
        if (this._estByteLimit > 0 && this._byteCount > 0) {
            d2 += this._byteCount / this._estByteLimit;
        }
        int iRound = (int) Math.round(100.0d * (((0.33d * d2) / Math.max(this._estSegLimit, 1)) + ((0.67d * this._fileCount) / Math.max(this._estFileLimit, 1))));
        if (iRound > 100) {
            iRound = 100;
        }
        if (iRound > this._prevPercent) {
            this._prevPercent = iRound;
            this._props.setInteger(Pack200.Unpacker.PROGRESS, iRound);
            if (this._verbose > 0) {
                Utils.log.info("progress = " + iRound);
            }
        }
    }

    private void copyInOption(String str) {
        String property = this._props.getProperty(str);
        if (this._verbose > 0) {
            Utils.log.info("set " + str + "=" + property);
        }
        if (property != null && !setOption(str, property)) {
            Utils.log.warning("Invalid option " + str + "=" + property);
        }
    }

    void run(InputStream inputStream, JarOutputStream jarOutputStream, ByteBuffer byteBuffer) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        this.in = bufferedInputStream;
        this._verbose = this._props.getInteger("com.sun.java.util.jar.pack.verbose");
        int time = "keep".equals(this._props.getProperty("com.sun.java.util.jar.pack.unpack.modification.time", "0")) ? 0 : this._props.getTime("com.sun.java.util.jar.pack.unpack.modification.time");
        copyInOption("com.sun.java.util.jar.pack.verbose");
        copyInOption(Pack200.Unpacker.DEFLATE_HINT);
        if (time == 0) {
            copyInOption("com.sun.java.util.jar.pack.unpack.modification.time");
        }
        updateProgress();
        while (true) {
            long jStart = start(byteBuffer, 0L);
            this._estByteLimit = 0L;
            this._byteCount = 0L;
            this._segCount++;
            this._estSegLimit = this._segCount + ((int) (jStart >>> 32));
            this._estFileLimit = (int) (((this._fileCount + ((int) (jStart >>> 0))) * this._estSegLimit) / this._segCount);
            int[] iArr = {0, 0, 0, 0};
            Object[] objArr = {iArr, null, null, null};
            while (getNextFile(objArr)) {
                writeEntry(jarOutputStream, (String) objArr[1], time != 0 ? time : iArr[2], (iArr[0] << 32) + ((iArr[1] << 32) >>> 32), iArr[3] != 0, (ByteBuffer) objArr[2], (ByteBuffer) objArr[3]);
                this._fileCount++;
                updateProgress();
            }
            byteBuffer = getUnusedInput();
            long jFinish = finish();
            if (this._verbose > 0) {
                Utils.log.info("bytes consumed = " + jFinish);
            }
            if (byteBuffer != null || Utils.isPackMagic(Utils.readMagic(bufferedInputStream))) {
                if (this._verbose > 0 && byteBuffer != null) {
                    Utils.log.info("unused input = " + ((Object) byteBuffer));
                }
            } else {
                return;
            }
        }
    }

    void run(InputStream inputStream, JarOutputStream jarOutputStream) throws IOException {
        run(inputStream, jarOutputStream, null);
    }

    void run(File file, JarOutputStream jarOutputStream) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        Throwable th = null;
        try {
            try {
                run(fileInputStream, jarOutputStream, null);
                if (fileInputStream != null) {
                    if (0 != 0) {
                        try {
                            fileInputStream.close();
                            return;
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                            return;
                        }
                    }
                    fileInputStream.close();
                }
            } catch (Throwable th3) {
                th = th3;
                throw th3;
            }
        } catch (Throwable th4) {
            if (fileInputStream != null) {
                if (th != null) {
                    try {
                        fileInputStream.close();
                    } catch (Throwable th5) {
                        th.addSuppressed(th5);
                    }
                } else {
                    fileInputStream.close();
                }
            }
            throw th4;
        }
    }

    private void writeEntry(JarOutputStream jarOutputStream, String str, long j2, long j3, boolean z2, ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws IOException {
        int i2 = (int) j3;
        if (i2 != j3) {
            throw new IOException("file too large: " + j3);
        }
        CRC32 crc32 = this._crc32;
        if (this._verbose > 1) {
            Utils.log.fine("Writing entry: " + str + " size=" + i2 + (z2 ? " deflated" : ""));
        }
        if (this._buf.length < i2) {
            int i3 = i2;
            while (true) {
                if (i3 >= this._buf.length) {
                    break;
                }
                i3 <<= 1;
                if (i3 <= 0) {
                    i3 = i2;
                    break;
                }
            }
            this._buf = new byte[i3];
        }
        if (!$assertionsDisabled && this._buf.length < i2) {
            throw new AssertionError();
        }
        int i4 = 0;
        if (byteBuffer != null) {
            int iCapacity = byteBuffer.capacity();
            byteBuffer.get(this._buf, 0, iCapacity);
            i4 = 0 + iCapacity;
        }
        if (byteBuffer2 != null) {
            int iCapacity2 = byteBuffer2.capacity();
            byteBuffer2.get(this._buf, i4, iCapacity2);
            i4 += iCapacity2;
        }
        while (i4 < i2) {
            int i5 = this.in.read(this._buf, i4, i2 - i4);
            if (i5 <= 0) {
                throw new IOException("EOF at end of archive");
            }
            i4 += i5;
        }
        ZipEntry zipEntry = new ZipEntry(str);
        zipEntry.setTime(j2 * 1000);
        if (i2 == 0) {
            zipEntry.setMethod(0);
            zipEntry.setSize(0L);
            zipEntry.setCrc(0L);
            zipEntry.setCompressedSize(0L);
        } else if (!z2) {
            zipEntry.setMethod(0);
            zipEntry.setSize(i2);
            zipEntry.setCompressedSize(i2);
            crc32.reset();
            crc32.update(this._buf, 0, i2);
            zipEntry.setCrc(crc32.getValue());
        } else {
            zipEntry.setMethod(8);
            zipEntry.setSize(i2);
        }
        jarOutputStream.putNextEntry(zipEntry);
        if (i2 > 0) {
            jarOutputStream.write(this._buf, 0, i2);
        }
        jarOutputStream.closeEntry();
        if (this._verbose > 0) {
            Utils.log.info("Writing " + Utils.zeString(zipEntry));
        }
    }
}
