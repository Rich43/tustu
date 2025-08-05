package java.util.zip;

import java.nio.file.attribute.FileTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/* loaded from: rt.jar:java/util/zip/ZipEntry.class */
public class ZipEntry implements ZipConstants, Cloneable {
    String name;
    long xdostime;
    FileTime mtime;
    FileTime atime;
    FileTime ctime;
    long crc;
    long size;
    long csize;
    int method;
    int flag;
    byte[] extra;
    String comment;
    public static final int STORED = 0;
    public static final int DEFLATED = 8;
    static final long DOSTIME_BEFORE_1980 = 2162688;
    private static final long UPPER_DOSTIME_BOUND = 4036608000000L;

    public ZipEntry(String str) {
        this.xdostime = -1L;
        this.crc = -1L;
        this.size = -1L;
        this.csize = -1L;
        this.method = -1;
        this.flag = 0;
        Objects.requireNonNull(str, "name");
        if (str.length() > 65535) {
            throw new IllegalArgumentException("entry name too long");
        }
        this.name = str;
    }

    public ZipEntry(ZipEntry zipEntry) {
        this.xdostime = -1L;
        this.crc = -1L;
        this.size = -1L;
        this.csize = -1L;
        this.method = -1;
        this.flag = 0;
        Objects.requireNonNull(zipEntry, "entry");
        this.name = zipEntry.name;
        this.xdostime = zipEntry.xdostime;
        this.mtime = zipEntry.mtime;
        this.atime = zipEntry.atime;
        this.ctime = zipEntry.ctime;
        this.crc = zipEntry.crc;
        this.size = zipEntry.size;
        this.csize = zipEntry.csize;
        this.method = zipEntry.method;
        this.flag = zipEntry.flag;
        this.extra = zipEntry.extra;
        this.comment = zipEntry.comment;
    }

    ZipEntry() {
        this.xdostime = -1L;
        this.crc = -1L;
        this.size = -1L;
        this.csize = -1L;
        this.method = -1;
        this.flag = 0;
    }

    public String getName() {
        return this.name;
    }

    public void setTime(long j2) {
        this.xdostime = ZipUtils.javaToExtendedDosTime(j2);
        if (this.xdostime != DOSTIME_BEFORE_1980 && j2 <= UPPER_DOSTIME_BOUND) {
            this.mtime = null;
        } else {
            this.mtime = FileTime.from(j2, TimeUnit.MILLISECONDS);
        }
    }

    public long getTime() {
        if (this.mtime != null) {
            return this.mtime.toMillis();
        }
        if (this.xdostime != -1) {
            return ZipUtils.extendedDosToJavaTime(this.xdostime);
        }
        return -1L;
    }

    public ZipEntry setLastModifiedTime(FileTime fileTime) {
        this.mtime = (FileTime) Objects.requireNonNull(fileTime, "lastModifiedTime");
        this.xdostime = ZipUtils.javaToExtendedDosTime(fileTime.to(TimeUnit.MILLISECONDS));
        return this;
    }

    public FileTime getLastModifiedTime() {
        if (this.mtime != null) {
            return this.mtime;
        }
        if (this.xdostime == -1) {
            return null;
        }
        return FileTime.from(getTime(), TimeUnit.MILLISECONDS);
    }

    public ZipEntry setLastAccessTime(FileTime fileTime) {
        this.atime = (FileTime) Objects.requireNonNull(fileTime, "lastAccessTime");
        return this;
    }

    public FileTime getLastAccessTime() {
        return this.atime;
    }

    public ZipEntry setCreationTime(FileTime fileTime) {
        this.ctime = (FileTime) Objects.requireNonNull(fileTime, "creationTime");
        return this;
    }

    public FileTime getCreationTime() {
        return this.ctime;
    }

    public void setSize(long j2) {
        if (j2 < 0) {
            throw new IllegalArgumentException("invalid entry size");
        }
        this.size = j2;
    }

    public long getSize() {
        return this.size;
    }

    public long getCompressedSize() {
        return this.csize;
    }

    public void setCompressedSize(long j2) {
        this.csize = j2;
    }

    public void setCrc(long j2) {
        if (j2 < 0 || j2 > 4294967295L) {
            throw new IllegalArgumentException("invalid entry crc-32");
        }
        this.crc = j2;
    }

    public long getCrc() {
        return this.crc;
    }

    public void setMethod(int i2) {
        if (i2 != 0 && i2 != 8) {
            throw new IllegalArgumentException("invalid compression method");
        }
        this.method = i2;
    }

    public int getMethod() {
        return this.method;
    }

    public void setExtra(byte[] bArr) {
        setExtra0(bArr, false);
    }

    void setExtra0(byte[] bArr, boolean z2) {
        if (bArr != null) {
            if (bArr.length > 65535) {
                throw new IllegalArgumentException("invalid extra field length");
            }
            int i2 = 0;
            int length = bArr.length;
            while (i2 + 4 < length) {
                int i3 = ZipUtils.get16(bArr, i2);
                int i4 = ZipUtils.get16(bArr, i2 + 2);
                int i5 = i2 + 4;
                if (i5 + i4 <= length) {
                    switch (i3) {
                        case 1:
                            if (z2 && i4 >= 16) {
                                this.size = ZipUtils.get64(bArr, i5);
                                this.csize = ZipUtils.get64(bArr, i5 + 8);
                                break;
                            } else {
                                break;
                            }
                            break;
                        case 10:
                            if (i4 >= 32) {
                                int i6 = i5 + 4;
                                if (ZipUtils.get16(bArr, i6) != 1 || ZipUtils.get16(bArr, i6 + 2) != 24) {
                                    break;
                                } else {
                                    this.mtime = ZipUtils.winTimeToFileTime(ZipUtils.get64(bArr, i6 + 4));
                                    this.atime = ZipUtils.winTimeToFileTime(ZipUtils.get64(bArr, i6 + 12));
                                    this.ctime = ZipUtils.winTimeToFileTime(ZipUtils.get64(bArr, i6 + 20));
                                    break;
                                }
                            } else {
                                break;
                            }
                            break;
                        case 21589:
                            int unsignedInt = Byte.toUnsignedInt(bArr[i5]);
                            int i7 = 1;
                            if ((unsignedInt & 1) != 0 && 1 + 4 <= i4) {
                                this.mtime = ZipUtils.unixTimeToFileTime(ZipUtils.get32(bArr, i5 + 1));
                                i7 = 1 + 4;
                            }
                            if ((unsignedInt & 2) != 0 && i7 + 4 <= i4) {
                                this.atime = ZipUtils.unixTimeToFileTime(ZipUtils.get32(bArr, i5 + i7));
                                i7 += 4;
                            }
                            if ((unsignedInt & 4) != 0 && i7 + 4 <= i4) {
                                this.ctime = ZipUtils.unixTimeToFileTime(ZipUtils.get32(bArr, i5 + i7));
                                int i8 = i7 + 4;
                                break;
                            } else {
                                break;
                            }
                            break;
                    }
                    i2 = i5 + i4;
                }
            }
        }
        this.extra = bArr;
    }

    public byte[] getExtra() {
        return this.extra;
    }

    public void setComment(String str) {
        this.comment = str;
    }

    public String getComment() {
        return this.comment;
    }

    public boolean isDirectory() {
        return this.name.endsWith("/");
    }

    public String toString() {
        return getName();
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public Object clone() {
        try {
            ZipEntry zipEntry = (ZipEntry) super.clone();
            zipEntry.extra = this.extra == null ? null : (byte[]) this.extra.clone();
            return zipEntry;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }
}
