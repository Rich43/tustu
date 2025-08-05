package com.sun.nio.zipfs;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileStoreAttributeView;

/* loaded from: zipfs.jar:com/sun/nio/zipfs/ZipFileStore.class */
public class ZipFileStore extends FileStore {
    private final ZipFileSystem zfs;

    ZipFileStore(ZipPath zipPath) {
        this.zfs = zipPath.getFileSystem();
    }

    @Override // java.nio.file.FileStore
    public String name() {
        return this.zfs.toString() + "/";
    }

    @Override // java.nio.file.FileStore
    public String type() {
        return "zipfs";
    }

    @Override // java.nio.file.FileStore
    public boolean isReadOnly() {
        return this.zfs.isReadOnly();
    }

    @Override // java.nio.file.FileStore
    public boolean supportsFileAttributeView(Class<? extends FileAttributeView> cls) {
        return cls == BasicFileAttributeView.class || cls == ZipFileAttributeView.class;
    }

    @Override // java.nio.file.FileStore
    public boolean supportsFileAttributeView(String str) {
        return str.equals("basic") || str.equals("zip");
    }

    @Override // java.nio.file.FileStore
    public <V extends FileStoreAttributeView> V getFileStoreAttributeView(Class<V> cls) {
        if (cls == null) {
            throw new NullPointerException();
        }
        return (V) null;
    }

    @Override // java.nio.file.FileStore
    public long getTotalSpace() throws IOException {
        return new ZipFileStoreAttributes(this).totalSpace();
    }

    @Override // java.nio.file.FileStore
    public long getUsableSpace() throws IOException {
        return new ZipFileStoreAttributes(this).usableSpace();
    }

    @Override // java.nio.file.FileStore
    public long getUnallocatedSpace() throws IOException {
        return new ZipFileStoreAttributes(this).unallocatedSpace();
    }

    @Override // java.nio.file.FileStore
    public Object getAttribute(String str) throws IOException {
        if (str.equals("totalSpace")) {
            return Long.valueOf(getTotalSpace());
        }
        if (str.equals("usableSpace")) {
            return Long.valueOf(getUsableSpace());
        }
        if (str.equals("unallocatedSpace")) {
            return Long.valueOf(getUnallocatedSpace());
        }
        throw new UnsupportedOperationException("does not support the given attribute");
    }

    /* loaded from: zipfs.jar:com/sun/nio/zipfs/ZipFileStore$ZipFileStoreAttributes.class */
    private static class ZipFileStoreAttributes {
        final FileStore fstore;
        final long size;

        public ZipFileStoreAttributes(ZipFileStore zipFileStore) throws IOException {
            Path path = FileSystems.getDefault().getPath(zipFileStore.name(), new String[0]);
            this.size = Files.size(path);
            this.fstore = Files.getFileStore(path);
        }

        public long totalSpace() {
            return this.size;
        }

        public long usableSpace() throws IOException {
            if (!this.fstore.isReadOnly()) {
                return this.fstore.getUsableSpace();
            }
            return 0L;
        }

        public long unallocatedSpace() throws IOException {
            if (!this.fstore.isReadOnly()) {
                return this.fstore.getUnallocatedSpace();
            }
            return 0L;
        }
    }
}
