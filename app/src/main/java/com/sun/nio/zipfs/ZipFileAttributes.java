package com.sun.nio.zipfs;

import com.sun.nio.zipfs.ZipFileSystem;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.Formatter;

/* loaded from: zipfs.jar:com/sun/nio/zipfs/ZipFileAttributes.class */
public class ZipFileAttributes implements BasicFileAttributes {

    /* renamed from: e, reason: collision with root package name */
    private final ZipFileSystem.Entry f11986e;

    ZipFileAttributes(ZipFileSystem.Entry entry) {
        this.f11986e = entry;
    }

    @Override // java.nio.file.attribute.BasicFileAttributes
    public FileTime creationTime() {
        if (this.f11986e.ctime != -1) {
            return FileTime.fromMillis(this.f11986e.ctime);
        }
        return null;
    }

    @Override // java.nio.file.attribute.BasicFileAttributes
    public boolean isDirectory() {
        return this.f11986e.isDir();
    }

    @Override // java.nio.file.attribute.BasicFileAttributes
    public boolean isOther() {
        return false;
    }

    @Override // java.nio.file.attribute.BasicFileAttributes
    public boolean isRegularFile() {
        return !this.f11986e.isDir();
    }

    @Override // java.nio.file.attribute.BasicFileAttributes
    public FileTime lastAccessTime() {
        if (this.f11986e.atime != -1) {
            return FileTime.fromMillis(this.f11986e.atime);
        }
        return null;
    }

    @Override // java.nio.file.attribute.BasicFileAttributes
    public FileTime lastModifiedTime() {
        return FileTime.fromMillis(this.f11986e.mtime);
    }

    @Override // java.nio.file.attribute.BasicFileAttributes
    public long size() {
        return this.f11986e.size;
    }

    @Override // java.nio.file.attribute.BasicFileAttributes
    public boolean isSymbolicLink() {
        return false;
    }

    @Override // java.nio.file.attribute.BasicFileAttributes
    public Object fileKey() {
        return null;
    }

    public long compressedSize() {
        return this.f11986e.csize;
    }

    public long crc() {
        return this.f11986e.crc;
    }

    public int method() {
        return this.f11986e.method;
    }

    public byte[] extra() {
        if (this.f11986e.extra != null) {
            return Arrays.copyOf(this.f11986e.extra, this.f11986e.extra.length);
        }
        return null;
    }

    public byte[] comment() {
        if (this.f11986e.comment != null) {
            return Arrays.copyOf(this.f11986e.comment, this.f11986e.comment.length);
        }
        return null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(1024);
        Formatter formatter = new Formatter(sb);
        if (creationTime() != null) {
            formatter.format("    creationTime    : %tc%n", Long.valueOf(creationTime().toMillis()));
        } else {
            formatter.format("    creationTime    : null%n", new Object[0]);
        }
        if (lastAccessTime() != null) {
            formatter.format("    lastAccessTime  : %tc%n", Long.valueOf(lastAccessTime().toMillis()));
        } else {
            formatter.format("    lastAccessTime  : null%n", new Object[0]);
        }
        formatter.format("    lastModifiedTime: %tc%n", Long.valueOf(lastModifiedTime().toMillis()));
        formatter.format("    isRegularFile   : %b%n", Boolean.valueOf(isRegularFile()));
        formatter.format("    isDirectory     : %b%n", Boolean.valueOf(isDirectory()));
        formatter.format("    isSymbolicLink  : %b%n", Boolean.valueOf(isSymbolicLink()));
        formatter.format("    isOther         : %b%n", Boolean.valueOf(isOther()));
        formatter.format("    fileKey         : %s%n", fileKey());
        formatter.format("    size            : %d%n", Long.valueOf(size()));
        formatter.format("    compressedSize  : %d%n", Long.valueOf(compressedSize()));
        formatter.format("    crc             : %x%n", Long.valueOf(crc()));
        formatter.format("    method          : %d%n", Integer.valueOf(method()));
        formatter.close();
        return sb.toString();
    }
}
