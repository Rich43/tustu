package com.sun.nio.zipfs;

import java.io.IOException;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.icepdf.core.util.PdfOps;

/* loaded from: zipfs.jar:com/sun/nio/zipfs/ZipFileAttributeView.class */
public class ZipFileAttributeView implements BasicFileAttributeView {
    private final ZipPath path;
    private final boolean isZipView;

    /* loaded from: zipfs.jar:com/sun/nio/zipfs/ZipFileAttributeView$AttrID.class */
    private enum AttrID {
        size,
        creationTime,
        lastAccessTime,
        lastModifiedTime,
        isDirectory,
        isRegularFile,
        isSymbolicLink,
        isOther,
        fileKey,
        compressedSize,
        crc,
        method
    }

    private ZipFileAttributeView(ZipPath zipPath, boolean z2) {
        this.path = zipPath;
        this.isZipView = z2;
    }

    static <V extends FileAttributeView> V get(ZipPath zipPath, Class<V> cls) {
        if (cls == null) {
            throw new NullPointerException();
        }
        if (cls == BasicFileAttributeView.class) {
            return new ZipFileAttributeView(zipPath, false);
        }
        if (cls == ZipFileAttributeView.class) {
            return new ZipFileAttributeView(zipPath, true);
        }
        return null;
    }

    static ZipFileAttributeView get(ZipPath zipPath, String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        if (str.equals("basic")) {
            return new ZipFileAttributeView(zipPath, false);
        }
        if (str.equals("zip")) {
            return new ZipFileAttributeView(zipPath, true);
        }
        return null;
    }

    @Override // java.nio.file.attribute.BasicFileAttributeView, java.nio.file.attribute.AttributeView
    public String name() {
        return this.isZipView ? "zip" : "basic";
    }

    @Override // java.nio.file.attribute.BasicFileAttributeView
    public ZipFileAttributes readAttributes() throws IOException {
        return this.path.getAttributes();
    }

    @Override // java.nio.file.attribute.BasicFileAttributeView
    public void setTimes(FileTime fileTime, FileTime fileTime2, FileTime fileTime3) throws IOException {
        this.path.setTimes(fileTime, fileTime2, fileTime3);
    }

    void setAttribute(String str, Object obj) throws IOException {
        try {
            if (AttrID.valueOf(str) == AttrID.lastModifiedTime) {
                setTimes((FileTime) obj, null, null);
            }
            if (AttrID.valueOf(str) == AttrID.lastAccessTime) {
                setTimes(null, (FileTime) obj, null);
            }
            if (AttrID.valueOf(str) == AttrID.creationTime) {
                setTimes(null, null, (FileTime) obj);
            }
        } catch (IllegalArgumentException e2) {
            throw new UnsupportedOperationException(PdfOps.SINGLE_QUOTE_TOKEN + str + "' is unknown or read-only attribute");
        }
    }

    Map<String, Object> readAttributes(String str) throws IOException {
        ZipFileAttributes attributes = readAttributes();
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        if ("*".equals(str)) {
            for (AttrID attrID : AttrID.values()) {
                try {
                    linkedHashMap.put(attrID.name(), attribute(attrID, attributes));
                } catch (IllegalArgumentException e2) {
                }
            }
        } else {
            for (String str2 : str.split(",")) {
                try {
                    linkedHashMap.put(str2, attribute(AttrID.valueOf(str2), attributes));
                } catch (IllegalArgumentException e3) {
                }
            }
        }
        return linkedHashMap;
    }

    Object attribute(AttrID attrID, ZipFileAttributes zipFileAttributes) {
        switch (attrID) {
            case size:
                return Long.valueOf(zipFileAttributes.size());
            case creationTime:
                return zipFileAttributes.creationTime();
            case lastAccessTime:
                return zipFileAttributes.lastAccessTime();
            case lastModifiedTime:
                return zipFileAttributes.lastModifiedTime();
            case isDirectory:
                return Boolean.valueOf(zipFileAttributes.isDirectory());
            case isRegularFile:
                return Boolean.valueOf(zipFileAttributes.isRegularFile());
            case isSymbolicLink:
                return Boolean.valueOf(zipFileAttributes.isSymbolicLink());
            case isOther:
                return Boolean.valueOf(zipFileAttributes.isOther());
            case fileKey:
                return zipFileAttributes.fileKey();
            case compressedSize:
                if (this.isZipView) {
                    return Long.valueOf(zipFileAttributes.compressedSize());
                }
                return null;
            case crc:
                if (this.isZipView) {
                    return Long.valueOf(zipFileAttributes.crc());
                }
                return null;
            case method:
                if (this.isZipView) {
                    return Integer.valueOf(zipFileAttributes.method());
                }
                return null;
            default:
                return null;
        }
    }
}
