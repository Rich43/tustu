package sun.nio.fs;

import java.io.IOException;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Map;
import java.util.Set;
import sun.nio.fs.AbstractBasicFileAttributeView;

/* loaded from: rt.jar:sun/nio/fs/WindowsFileAttributeViews.class */
class WindowsFileAttributeViews {
    WindowsFileAttributeViews() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: rt.jar:sun/nio/fs/WindowsFileAttributeViews$Basic.class */
    static class Basic extends AbstractBasicFileAttributeView {
        final WindowsPath file;
        final boolean followLinks;

        Basic(WindowsPath windowsPath, boolean z2) {
            this.file = windowsPath;
            this.followLinks = z2;
        }

        @Override // java.nio.file.attribute.BasicFileAttributeView
        public WindowsFileAttributes readAttributes() throws IOException {
            this.file.checkRead();
            try {
                return WindowsFileAttributes.get(this.file, this.followLinks);
            } catch (WindowsException e2) {
                e2.rethrowAsIOException(this.file);
                return null;
            }
        }

        private long adjustForFatEpoch(long j2) {
            if (j2 != -1 && j2 < 119600064000000000L) {
                return 119600064000000000L;
            }
            return j2;
        }

        void setFileTimes(long j2, long j3, long j4) throws IOException {
            long jCreateFile = -1;
            try {
                int i2 = 33554432;
                if (!this.followLinks && this.file.getFileSystem().supportsLinks()) {
                    i2 = 33554432 | 2097152;
                }
                jCreateFile = WindowsNativeDispatcher.CreateFile(this.file.getPathForWin32Calls(), 256, 7, 3, i2);
            } catch (WindowsException e2) {
                e2.rethrowAsIOException(this.file);
            }
            try {
                try {
                    WindowsNativeDispatcher.SetFileTime(jCreateFile, j2, j3, j4);
                    WindowsNativeDispatcher.CloseHandle(jCreateFile);
                } catch (WindowsException e3) {
                    e = e3;
                    if (this.followLinks && e.lastError() == 87) {
                        try {
                            if (WindowsFileStore.create(this.file).type().equals("FAT")) {
                                WindowsNativeDispatcher.SetFileTime(jCreateFile, adjustForFatEpoch(j2), adjustForFatEpoch(j3), adjustForFatEpoch(j4));
                                e = null;
                            }
                        } catch (IOException e4) {
                        } catch (SecurityException e5) {
                        } catch (WindowsException e6) {
                        }
                    }
                    if (e != null) {
                        e.rethrowAsIOException(this.file);
                    }
                    WindowsNativeDispatcher.CloseHandle(jCreateFile);
                }
            } catch (Throwable th) {
                WindowsNativeDispatcher.CloseHandle(jCreateFile);
                throw th;
            }
        }

        @Override // java.nio.file.attribute.BasicFileAttributeView
        public void setTimes(FileTime fileTime, FileTime fileTime2, FileTime fileTime3) throws IOException {
            if (fileTime == null && fileTime2 == null && fileTime3 == null) {
                return;
            }
            this.file.checkWrite();
            setFileTimes(fileTime3 == null ? -1L : WindowsFileAttributes.toWindowsTime(fileTime3), fileTime2 == null ? -1L : WindowsFileAttributes.toWindowsTime(fileTime2), fileTime == null ? -1L : WindowsFileAttributes.toWindowsTime(fileTime));
        }
    }

    /* loaded from: rt.jar:sun/nio/fs/WindowsFileAttributeViews$Dos.class */
    static class Dos extends Basic implements DosFileAttributeView {
        private static final String ARCHIVE_NAME = "archive";
        private static final String READONLY_NAME = "readonly";
        private static final String SYSTEM_NAME = "system";
        private static final String HIDDEN_NAME = "hidden";
        private static final String ATTRIBUTES_NAME = "attributes";
        static final Set<String> dosAttributeNames = Util.newSet(basicAttributeNames, READONLY_NAME, "archive", SYSTEM_NAME, HIDDEN_NAME, ATTRIBUTES_NAME);

        @Override // sun.nio.fs.WindowsFileAttributeViews.Basic, java.nio.file.attribute.BasicFileAttributeView
        public /* bridge */ /* synthetic */ DosFileAttributes readAttributes() throws IOException {
            return super.readAttributes();
        }

        Dos(WindowsPath windowsPath, boolean z2) {
            super(windowsPath, z2);
        }

        @Override // sun.nio.fs.AbstractBasicFileAttributeView, java.nio.file.attribute.BasicFileAttributeView, java.nio.file.attribute.AttributeView
        public String name() {
            return "dos";
        }

        @Override // sun.nio.fs.AbstractBasicFileAttributeView, sun.nio.fs.DynamicFileAttributeView
        public void setAttribute(String str, Object obj) throws IOException {
            if (str.equals(READONLY_NAME)) {
                setReadOnly(((Boolean) obj).booleanValue());
                return;
            }
            if (str.equals("archive")) {
                setArchive(((Boolean) obj).booleanValue());
                return;
            }
            if (str.equals(SYSTEM_NAME)) {
                setSystem(((Boolean) obj).booleanValue());
            } else if (str.equals(HIDDEN_NAME)) {
                setHidden(((Boolean) obj).booleanValue());
            } else {
                super.setAttribute(str, obj);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // sun.nio.fs.AbstractBasicFileAttributeView, sun.nio.fs.DynamicFileAttributeView
        public Map<String, Object> readAttributes(String[] strArr) throws IOException {
            AbstractBasicFileAttributeView.AttributesBuilder attributesBuilderCreate = AbstractBasicFileAttributeView.AttributesBuilder.create(dosAttributeNames, strArr);
            WindowsFileAttributes attributes = readAttributes();
            addRequestedBasicAttributes(attributes, attributesBuilderCreate);
            if (attributesBuilderCreate.match(READONLY_NAME)) {
                attributesBuilderCreate.add(READONLY_NAME, Boolean.valueOf(attributes.isReadOnly()));
            }
            if (attributesBuilderCreate.match("archive")) {
                attributesBuilderCreate.add("archive", Boolean.valueOf(attributes.isArchive()));
            }
            if (attributesBuilderCreate.match(SYSTEM_NAME)) {
                attributesBuilderCreate.add(SYSTEM_NAME, Boolean.valueOf(attributes.isSystem()));
            }
            if (attributesBuilderCreate.match(HIDDEN_NAME)) {
                attributesBuilderCreate.add(HIDDEN_NAME, Boolean.valueOf(attributes.isHidden()));
            }
            if (attributesBuilderCreate.match(ATTRIBUTES_NAME)) {
                attributesBuilderCreate.add(ATTRIBUTES_NAME, Integer.valueOf(attributes.attributes()));
            }
            return attributesBuilderCreate.unmodifiableMap();
        }

        private void updateAttributes(int i2, boolean z2) throws IOException {
            int i3;
            this.file.checkWrite();
            String finalPath = WindowsLinkSupport.getFinalPath(this.file, this.followLinks);
            try {
                int iGetFileAttributes = WindowsNativeDispatcher.GetFileAttributes(finalPath);
                if (z2) {
                    i3 = iGetFileAttributes | i2;
                } else {
                    i3 = iGetFileAttributes & (i2 ^ (-1));
                }
                if (i3 != iGetFileAttributes) {
                    WindowsNativeDispatcher.SetFileAttributes(finalPath, i3);
                }
            } catch (WindowsException e2) {
                e2.rethrowAsIOException(this.file);
            }
        }

        @Override // java.nio.file.attribute.DosFileAttributeView
        public void setReadOnly(boolean z2) throws IOException {
            updateAttributes(1, z2);
        }

        @Override // java.nio.file.attribute.DosFileAttributeView
        public void setHidden(boolean z2) throws IOException {
            updateAttributes(2, z2);
        }

        @Override // java.nio.file.attribute.DosFileAttributeView
        public void setArchive(boolean z2) throws IOException {
            updateAttributes(32, z2);
        }

        @Override // java.nio.file.attribute.DosFileAttributeView
        public void setSystem(boolean z2) throws IOException {
            updateAttributes(4, z2);
        }

        void setAttributes(WindowsFileAttributes windowsFileAttributes) throws IOException {
            int i2 = 0;
            if (windowsFileAttributes.isReadOnly()) {
                i2 = 0 | 1;
            }
            if (windowsFileAttributes.isHidden()) {
                i2 |= 2;
            }
            if (windowsFileAttributes.isArchive()) {
                i2 |= 32;
            }
            if (windowsFileAttributes.isSystem()) {
                i2 |= 4;
            }
            updateAttributes(i2, true);
            setFileTimes(WindowsFileAttributes.toWindowsTime(windowsFileAttributes.creationTime()), WindowsFileAttributes.toWindowsTime(windowsFileAttributes.lastModifiedTime()), WindowsFileAttributes.toWindowsTime(windowsFileAttributes.lastAccessTime()));
        }
    }

    static Basic createBasicView(WindowsPath windowsPath, boolean z2) {
        return new Basic(windowsPath, z2);
    }

    static Dos createDosView(WindowsPath windowsPath, boolean z2) {
        return new Dos(windowsPath, z2);
    }
}
