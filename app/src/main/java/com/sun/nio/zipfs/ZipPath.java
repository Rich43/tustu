package com.sun.nio.zipfs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessDeniedException;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.ProviderMismatchException;
import java.nio.file.ReadOnlyFileSystemException;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.icepdf.core.util.PdfOps;

/* loaded from: zipfs.jar:com/sun/nio/zipfs/ZipPath.class */
public class ZipPath implements Path {
    private final ZipFileSystem zfs;
    private final byte[] path;
    private volatile int[] offsets;
    private int hashcode;
    private volatile byte[] resolved;

    ZipPath(ZipFileSystem zipFileSystem, byte[] bArr) {
        this(zipFileSystem, bArr, false);
    }

    ZipPath(ZipFileSystem zipFileSystem, byte[] bArr, boolean z2) {
        this.hashcode = 0;
        this.resolved = null;
        this.zfs = zipFileSystem;
        if (z2) {
            this.path = bArr;
        } else if (zipFileSystem.zc.isUTF8()) {
            this.path = normalize(bArr);
        } else {
            this.path = normalize(zipFileSystem.getString(bArr));
        }
    }

    ZipPath(ZipFileSystem zipFileSystem, String str) {
        this.hashcode = 0;
        this.resolved = null;
        this.zfs = zipFileSystem;
        if (zipFileSystem.zc.isUTF8()) {
            this.path = normalize(zipFileSystem.getBytes(str));
        } else {
            this.path = normalize(str);
        }
    }

    @Override // java.nio.file.Path
    public ZipPath getRoot() {
        if (isAbsolute()) {
            return new ZipPath(this.zfs, new byte[]{this.path[0]});
        }
        return null;
    }

    @Override // java.nio.file.Path
    public Path getFileName() {
        initOffsets();
        int length = this.offsets.length;
        if (length == 0) {
            return null;
        }
        if (length == 1 && this.path[0] != 47) {
            return this;
        }
        int i2 = this.offsets[length - 1];
        int length2 = this.path.length - i2;
        byte[] bArr = new byte[length2];
        System.arraycopy(this.path, i2, bArr, 0, length2);
        return new ZipPath(this.zfs, bArr);
    }

    @Override // java.nio.file.Path
    public ZipPath getParent() {
        initOffsets();
        int length = this.offsets.length;
        if (length == 0) {
            return null;
        }
        int i2 = this.offsets[length - 1] - 1;
        if (i2 <= 0) {
            return getRoot();
        }
        byte[] bArr = new byte[i2];
        System.arraycopy(this.path, 0, bArr, 0, i2);
        return new ZipPath(this.zfs, bArr);
    }

    @Override // java.nio.file.Path
    public int getNameCount() {
        initOffsets();
        return this.offsets.length;
    }

    @Override // java.nio.file.Path
    public ZipPath getName(int i2) {
        int length;
        initOffsets();
        if (i2 < 0 || i2 >= this.offsets.length) {
            throw new IllegalArgumentException();
        }
        int i3 = this.offsets[i2];
        if (i2 == this.offsets.length - 1) {
            length = this.path.length - i3;
        } else {
            length = (this.offsets[i2 + 1] - i3) - 1;
        }
        byte[] bArr = new byte[length];
        System.arraycopy(this.path, i3, bArr, 0, length);
        return new ZipPath(this.zfs, bArr);
    }

    @Override // java.nio.file.Path
    public ZipPath subpath(int i2, int i3) {
        int length;
        initOffsets();
        if (i2 < 0 || i2 >= this.offsets.length || i3 > this.offsets.length || i2 >= i3) {
            throw new IllegalArgumentException();
        }
        int i4 = this.offsets[i2];
        if (i3 == this.offsets.length) {
            length = this.path.length - i4;
        } else {
            length = (this.offsets[i3] - i4) - 1;
        }
        byte[] bArr = new byte[length];
        System.arraycopy(this.path, i4, bArr, 0, length);
        return new ZipPath(this.zfs, bArr);
    }

    @Override // java.nio.file.Path
    public ZipPath toRealPath(LinkOption... linkOptionArr) throws IOException {
        ZipPath absolutePath = new ZipPath(this.zfs, getResolvedPath()).toAbsolutePath();
        absolutePath.checkAccess(new AccessMode[0]);
        return absolutePath;
    }

    boolean isHidden() {
        return false;
    }

    @Override // java.nio.file.Path
    public ZipPath toAbsolutePath() {
        byte[] bArr;
        if (isAbsolute()) {
            return this;
        }
        byte[] bArr2 = this.zfs.getDefaultDir().path;
        int length = bArr2.length;
        boolean z2 = bArr2[length - 1] == 47;
        if (z2) {
            bArr = new byte[length + this.path.length];
        } else {
            bArr = new byte[length + 1 + this.path.length];
        }
        System.arraycopy(bArr2, 0, bArr, 0, length);
        if (!z2) {
            length++;
            bArr[length] = 47;
        }
        System.arraycopy(this.path, 0, bArr, length, this.path.length);
        return new ZipPath(this.zfs, bArr, true);
    }

    @Override // java.nio.file.Path
    public URI toUri() {
        try {
            return new URI("jar", ((Object) this.zfs.getZipFile().toUri()) + "!" + this.zfs.getString(toAbsolutePath().path), null);
        } catch (Exception e2) {
            throw new AssertionError(e2);
        }
    }

    private boolean equalsNameAt(ZipPath zipPath, int i2) {
        int length;
        int length2;
        int i3 = this.offsets[i2];
        if (i2 == this.offsets.length - 1) {
            length = this.path.length - i3;
        } else {
            length = (this.offsets[i2 + 1] - i3) - 1;
        }
        int i4 = zipPath.offsets[i2];
        if (i2 == zipPath.offsets.length - 1) {
            length2 = zipPath.path.length - i4;
        } else {
            length2 = (zipPath.offsets[i2 + 1] - i4) - 1;
        }
        if (length != length2) {
            return false;
        }
        for (int i5 = 0; i5 < length; i5++) {
            if (this.path[i3 + i5] != zipPath.path[i4 + i5]) {
                return false;
            }
        }
        return true;
    }

    @Override // java.nio.file.Path
    public Path relativize(Path path) {
        ZipPath zipPathCheckPath = checkPath(path);
        if (zipPathCheckPath.equals(this)) {
            return new ZipPath(getFileSystem(), new byte[0], true);
        }
        if (isAbsolute() != zipPathCheckPath.isAbsolute()) {
            throw new IllegalArgumentException();
        }
        int nameCount = getNameCount();
        int nameCount2 = zipPathCheckPath.getNameCount();
        int iMin = Math.min(nameCount, nameCount2);
        int i2 = 0;
        while (i2 < iMin && equalsNameAt(zipPathCheckPath, i2)) {
            i2++;
        }
        int i3 = nameCount - i2;
        int length = (i3 * 3) - 1;
        if (i2 < nameCount2) {
            length += (zipPathCheckPath.path.length - zipPathCheckPath.offsets[i2]) + 1;
        }
        byte[] bArr = new byte[length];
        int i4 = 0;
        while (i3 > 0) {
            int i5 = i4;
            int i6 = i4 + 1;
            bArr[i5] = 46;
            i4 = i6 + 1;
            bArr[i6] = 46;
            if (i4 < length) {
                i4++;
                bArr[i4] = 47;
            }
            i3--;
        }
        if (i2 < nameCount2) {
            System.arraycopy(zipPathCheckPath.path, zipPathCheckPath.offsets[i2], bArr, i4, zipPathCheckPath.path.length - zipPathCheckPath.offsets[i2]);
        }
        return new ZipPath(getFileSystem(), bArr);
    }

    @Override // java.nio.file.Path
    public ZipFileSystem getFileSystem() {
        return this.zfs;
    }

    @Override // java.nio.file.Path
    public boolean isAbsolute() {
        return this.path.length > 0 && this.path[0] == 47;
    }

    @Override // java.nio.file.Path
    public ZipPath resolve(Path path) {
        byte[] bArr;
        ZipPath zipPathCheckPath = checkPath(path);
        if (zipPathCheckPath.isAbsolute()) {
            return zipPathCheckPath;
        }
        if (this.path[this.path.length - 1] == 47) {
            bArr = new byte[this.path.length + zipPathCheckPath.path.length];
            System.arraycopy(this.path, 0, bArr, 0, this.path.length);
            System.arraycopy(zipPathCheckPath.path, 0, bArr, this.path.length, zipPathCheckPath.path.length);
        } else {
            bArr = new byte[this.path.length + 1 + zipPathCheckPath.path.length];
            System.arraycopy(this.path, 0, bArr, 0, this.path.length);
            bArr[this.path.length] = 47;
            System.arraycopy(zipPathCheckPath.path, 0, bArr, this.path.length + 1, zipPathCheckPath.path.length);
        }
        return new ZipPath(this.zfs, bArr);
    }

    @Override // java.nio.file.Path
    public Path resolveSibling(Path path) {
        if (path == null) {
            throw new NullPointerException();
        }
        ZipPath parent = getParent();
        return parent == null ? path : parent.resolve(path);
    }

    @Override // java.nio.file.Path
    public boolean startsWith(Path path) {
        ZipPath zipPathCheckPath = checkPath(path);
        if (zipPathCheckPath.isAbsolute() != isAbsolute() || zipPathCheckPath.path.length > this.path.length) {
            return false;
        }
        int length = zipPathCheckPath.path.length;
        for (int i2 = 0; i2 < length; i2++) {
            if (zipPathCheckPath.path[i2] != this.path[i2]) {
                return false;
            }
        }
        int i3 = length - 1;
        return zipPathCheckPath.path.length == this.path.length || zipPathCheckPath.path[i3] == 47 || this.path[i3 + 1] == 47;
    }

    @Override // java.nio.file.Path
    public boolean endsWith(Path path) {
        ZipPath zipPathCheckPath = checkPath(path);
        int length = zipPathCheckPath.path.length - 1;
        if (length > 0 && zipPathCheckPath.path[length] == 47) {
            length--;
        }
        int length2 = this.path.length - 1;
        if (length2 > 0 && this.path[length2] == 47) {
            length2--;
        }
        if (length == -1) {
            return length2 == -1;
        }
        if ((zipPathCheckPath.isAbsolute() && (!isAbsolute() || length != length2)) || length2 < length) {
            return false;
        }
        while (length >= 0) {
            if (zipPathCheckPath.path[length] == this.path[length2]) {
                length--;
                length2--;
            } else {
                return false;
            }
        }
        return zipPathCheckPath.path[length + 1] == 47 || length2 == -1 || this.path[length2] == 47;
    }

    @Override // java.nio.file.Path
    public ZipPath resolve(String str) {
        return resolve((Path) getFileSystem().getPath(str, new String[0]));
    }

    @Override // java.nio.file.Path
    public final Path resolveSibling(String str) {
        return resolveSibling(getFileSystem().getPath(str, new String[0]));
    }

    @Override // java.nio.file.Path
    public final boolean startsWith(String str) {
        return startsWith(getFileSystem().getPath(str, new String[0]));
    }

    @Override // java.nio.file.Path
    public final boolean endsWith(String str) {
        return endsWith(getFileSystem().getPath(str, new String[0]));
    }

    @Override // java.nio.file.Path
    public Path normalize() {
        byte[] resolved = getResolved();
        if (resolved == this.path) {
            return this;
        }
        return new ZipPath(this.zfs, resolved, true);
    }

    private ZipPath checkPath(Path path) {
        if (path == null) {
            throw new NullPointerException();
        }
        if (!(path instanceof ZipPath)) {
            throw new ProviderMismatchException();
        }
        return (ZipPath) path;
    }

    private void initOffsets() {
        if (this.offsets == null) {
            int i2 = 0;
            int i3 = 0;
            while (i3 < this.path.length) {
                int i4 = i3;
                i3++;
                if (this.path[i4] != 47) {
                    i2++;
                    while (i3 < this.path.length && this.path[i3] != 47) {
                        i3++;
                    }
                }
            }
            int[] iArr = new int[i2];
            int i5 = 0;
            int i6 = 0;
            while (i6 < this.path.length) {
                if (this.path[i6] == 47) {
                    i6++;
                } else {
                    int i7 = i5;
                    i5++;
                    int i8 = i6;
                    i6++;
                    iArr[i7] = i8;
                    while (i6 < this.path.length && this.path[i6] != 47) {
                        i6++;
                    }
                }
            }
            synchronized (this) {
                if (this.offsets == null) {
                    this.offsets = iArr;
                }
            }
        }
    }

    byte[] getResolvedPath() {
        byte[] resolvedPath;
        if (this.resolved == null) {
            if (isAbsolute()) {
                resolvedPath = getResolved();
            } else {
                resolvedPath = toAbsolutePath().getResolvedPath();
            }
            if (resolvedPath[0] == 47) {
                resolvedPath = Arrays.copyOfRange(resolvedPath, 1, resolvedPath.length);
            }
            this.resolved = resolvedPath;
        }
        return this.resolved;
    }

    private byte[] normalize(byte[] bArr) {
        if (bArr.length == 0) {
            return bArr;
        }
        byte b2 = 0;
        for (int i2 = 0; i2 < bArr.length; i2++) {
            byte b3 = bArr[i2];
            if (b3 == 92) {
                return normalize(bArr, i2);
            }
            if (b3 == 47 && b2 == 47) {
                return normalize(bArr, i2 - 1);
            }
            if (b3 == 0) {
                throw new InvalidPathException(this.zfs.getString(bArr), "Path: nul character not allowed");
            }
            b2 = b3;
        }
        return bArr;
    }

    private byte[] normalize(byte[] bArr, int i2) {
        byte[] bArr2 = new byte[bArr.length];
        int i3 = 0;
        while (i3 < i2) {
            bArr2[i3] = bArr[i3];
            i3++;
        }
        int i4 = i3;
        byte b2 = 0;
        while (i3 < bArr.length) {
            int i5 = i3;
            i3++;
            byte b3 = bArr[i5];
            if (b3 == 92) {
                b3 = 47;
            }
            if (b3 != 47 || b2 != 47) {
                if (b3 == 0) {
                    throw new InvalidPathException(this.zfs.getString(bArr), "Path: nul character not allowed");
                }
                int i6 = i4;
                i4++;
                bArr2[i6] = b3;
                b2 = b3;
            }
        }
        if (i4 > 1 && bArr2[i4 - 1] == 47) {
            i4--;
        }
        return i4 == bArr2.length ? bArr2 : Arrays.copyOf(bArr2, i4);
    }

    private byte[] normalize(String str) {
        int length = str.length();
        if (length == 0) {
            return new byte[0];
        }
        char c2 = 0;
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = str.charAt(i2);
            if (cCharAt == '\\' || cCharAt == 0) {
                return normalize(str, i2, length);
            }
            if (cCharAt == '/' && c2 == '/') {
                return normalize(str, i2 - 1, length);
            }
            c2 = cCharAt;
        }
        if (length > 1 && c2 == '/') {
            str = str.substring(0, length - 1);
        }
        return this.zfs.getBytes(str);
    }

    private byte[] normalize(String str, int i2, int i3) {
        StringBuilder sb = new StringBuilder(i3);
        sb.append((CharSequence) str, 0, i2);
        char c2 = 0;
        while (i2 < i3) {
            int i4 = i2;
            i2++;
            char cCharAt = str.charAt(i4);
            if (cCharAt == '\\') {
                cCharAt = '/';
            }
            if (cCharAt != '/' || c2 != '/') {
                if (cCharAt == 0) {
                    throw new InvalidPathException(str, "Path: nul character not allowed");
                }
                sb.append(cCharAt);
                c2 = cCharAt;
            }
        }
        int length = sb.length();
        if (length > 1 && c2 == '/') {
            sb.delete(length - 1, length);
        }
        return this.zfs.getBytes(sb.toString());
    }

    private byte[] getResolved() {
        if (this.path.length == 0) {
            return this.path;
        }
        for (int i2 = 0; i2 < this.path.length; i2++) {
            if (this.path[i2] == 46) {
                return resolve0();
            }
        }
        return this.path;
    }

    private byte[] resolve0() {
        byte[] bArr = new byte[this.path.length];
        int nameCount = getNameCount();
        int[] iArr = new int[nameCount];
        int i2 = -1;
        int i3 = 0;
        int i4 = 0;
        while (i4 < nameCount) {
            int i5 = this.offsets[i4];
            int length = i4 == this.offsets.length - 1 ? this.path.length - i5 : (this.offsets[i4 + 1] - i5) - 1;
            if (length == 1 && this.path[i5] == 46) {
                if (i3 == 0 && this.path[0] == 47) {
                    int i6 = i3;
                    i3++;
                    bArr[i6] = 47;
                }
            } else if (length == 2 && this.path[i5] == 46 && this.path[i5 + 1] == 46) {
                if (i2 >= 0) {
                    int i7 = i2;
                    i2--;
                    i3 = iArr[i7];
                } else if (this.path[0] == 47) {
                    if (i3 == 0) {
                        int i8 = i3;
                        i3++;
                        bArr[i8] = 47;
                    }
                } else {
                    if (i3 != 0 && bArr[i3 - 1] != 47) {
                        int i9 = i3;
                        i3++;
                        bArr[i9] = 47;
                    }
                    while (true) {
                        int i10 = length;
                        length--;
                        if (i10 > 0) {
                            int i11 = i3;
                            i3++;
                            int i12 = i5;
                            i5++;
                            bArr[i11] = this.path[i12];
                        }
                    }
                }
            } else {
                if ((i3 == 0 && this.path[0] == 47) || (i3 != 0 && bArr[i3 - 1] != 47)) {
                    int i13 = i3;
                    i3++;
                    bArr[i13] = 47;
                }
                i2++;
                iArr[i2] = i3;
                while (true) {
                    int i14 = length;
                    length--;
                    if (i14 > 0) {
                        int i15 = i3;
                        i3++;
                        int i16 = i5;
                        i5++;
                        bArr[i15] = this.path[i16];
                    }
                }
            }
            i4++;
        }
        if (i3 > 1 && bArr[i3 - 1] == 47) {
            i3--;
        }
        return i3 == bArr.length ? bArr : Arrays.copyOf(bArr, i3);
    }

    @Override // java.nio.file.Path
    public String toString() {
        return this.zfs.getString(this.path);
    }

    @Override // java.nio.file.Path
    public int hashCode() {
        int i2 = this.hashcode;
        if (i2 == 0) {
            int iHashCode = Arrays.hashCode(this.path);
            i2 = iHashCode;
            this.hashcode = iHashCode;
        }
        return i2;
    }

    @Override // java.nio.file.Path
    public boolean equals(Object obj) {
        return obj != null && (obj instanceof ZipPath) && this.zfs == ((ZipPath) obj).zfs && compareTo((Path) obj) == 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.nio.file.Path, java.lang.Comparable
    public int compareTo(Path path) {
        ZipPath zipPathCheckPath = checkPath(path);
        int length = this.path.length;
        int length2 = zipPathCheckPath.path.length;
        int iMin = Math.min(length, length2);
        byte[] bArr = this.path;
        byte[] bArr2 = zipPathCheckPath.path;
        for (int i2 = 0; i2 < iMin; i2++) {
            int i3 = bArr[i2] & 255;
            int i4 = bArr2[i2] & 255;
            if (i3 != i4) {
                return i3 - i4;
            }
        }
        return length - length2;
    }

    @Override // java.nio.file.Path, java.nio.file.Watchable
    public WatchKey register(WatchService watchService, WatchEvent.Kind<?>[] kindArr, WatchEvent.Modifier... modifierArr) {
        if (watchService == null || kindArr == null || modifierArr == null) {
            throw new NullPointerException();
        }
        throw new UnsupportedOperationException();
    }

    @Override // java.nio.file.Path, java.nio.file.Watchable
    public WatchKey register(WatchService watchService, WatchEvent.Kind<?>... kindArr) {
        return register(watchService, kindArr, new WatchEvent.Modifier[0]);
    }

    @Override // java.nio.file.Path
    public final File toFile() {
        throw new UnsupportedOperationException();
    }

    @Override // java.nio.file.Path, java.lang.Iterable, java.util.List
    public Iterator<Path> iterator() {
        return new Iterator<Path>() { // from class: com.sun.nio.zipfs.ZipPath.1

            /* renamed from: i, reason: collision with root package name */
            private int f11988i = 0;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.f11988i < ZipPath.this.getNameCount();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public Path next() {
                if (this.f11988i < ZipPath.this.getNameCount()) {
                    ZipPath name = ZipPath.this.getName(this.f11988i);
                    this.f11988i++;
                    return name;
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new ReadOnlyFileSystemException();
            }
        };
    }

    void createDirectory(FileAttribute<?>... fileAttributeArr) throws IOException {
        this.zfs.createDirectory(getResolvedPath(), fileAttributeArr);
    }

    InputStream newInputStream(OpenOption... openOptionArr) throws IOException {
        if (openOptionArr.length > 0) {
            for (OpenOption openOption : openOptionArr) {
                if (openOption != StandardOpenOption.READ) {
                    throw new UnsupportedOperationException(PdfOps.SINGLE_QUOTE_TOKEN + ((Object) openOption) + "' not allowed");
                }
            }
        }
        return this.zfs.newInputStream(getResolvedPath());
    }

    DirectoryStream<Path> newDirectoryStream(DirectoryStream.Filter<? super Path> filter) throws IOException {
        return new ZipDirectoryStream(this, filter);
    }

    void delete() throws IOException {
        this.zfs.deleteFile(getResolvedPath(), true);
    }

    void deleteIfExists() throws IOException {
        this.zfs.deleteFile(getResolvedPath(), false);
    }

    ZipFileAttributes getAttributes() throws IOException {
        ZipFileAttributes fileAttributes = this.zfs.getFileAttributes(getResolvedPath());
        if (fileAttributes == null) {
            throw new NoSuchFileException(toString());
        }
        return fileAttributes;
    }

    void setAttribute(String str, Object obj, LinkOption... linkOptionArr) throws IOException {
        String strSubstring;
        String strSubstring2;
        int iIndexOf = str.indexOf(58);
        if (iIndexOf == -1) {
            strSubstring = "basic";
            strSubstring2 = str;
        } else {
            strSubstring = str.substring(0, iIndexOf);
            strSubstring2 = str.substring(iIndexOf + 1);
        }
        ZipFileAttributeView zipFileAttributeView = ZipFileAttributeView.get(this, strSubstring);
        if (zipFileAttributeView == null) {
            throw new UnsupportedOperationException("view <" + ((Object) zipFileAttributeView) + "> is not supported");
        }
        zipFileAttributeView.setAttribute(strSubstring2, obj);
    }

    void setTimes(FileTime fileTime, FileTime fileTime2, FileTime fileTime3) throws IOException {
        this.zfs.setTimes(getResolvedPath(), fileTime, fileTime2, fileTime3);
    }

    Map<String, Object> readAttributes(String str, LinkOption... linkOptionArr) throws IOException {
        String strSubstring;
        String strSubstring2;
        int iIndexOf = str.indexOf(58);
        if (iIndexOf == -1) {
            strSubstring = "basic";
            strSubstring2 = str;
        } else {
            strSubstring = str.substring(0, iIndexOf);
            strSubstring2 = str.substring(iIndexOf + 1);
        }
        ZipFileAttributeView zipFileAttributeView = ZipFileAttributeView.get(this, strSubstring);
        if (zipFileAttributeView == null) {
            throw new UnsupportedOperationException("view not supported");
        }
        return zipFileAttributeView.readAttributes(strSubstring2);
    }

    FileStore getFileStore() throws IOException {
        if (exists()) {
            return this.zfs.getFileStore(this);
        }
        throw new NoSuchFileException(this.zfs.getString(this.path));
    }

    boolean isSameFile(Path path) throws IOException {
        if (equals(path)) {
            return true;
        }
        if (path == null || getFileSystem() != path.getFileSystem()) {
            return false;
        }
        checkAccess(new AccessMode[0]);
        ((ZipPath) path).checkAccess(new AccessMode[0]);
        return Arrays.equals(getResolvedPath(), ((ZipPath) path).getResolvedPath());
    }

    SeekableByteChannel newByteChannel(Set<? extends OpenOption> set, FileAttribute<?>... fileAttributeArr) throws IOException {
        return this.zfs.newByteChannel(getResolvedPath(), set, fileAttributeArr);
    }

    FileChannel newFileChannel(Set<? extends OpenOption> set, FileAttribute<?>... fileAttributeArr) throws IOException {
        return this.zfs.newFileChannel(getResolvedPath(), set, fileAttributeArr);
    }

    void checkAccess(AccessMode... accessModeArr) throws IOException {
        boolean z2 = false;
        boolean z3 = false;
        int length = accessModeArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            switch (accessModeArr[i2]) {
                case READ:
                    break;
                case WRITE:
                    z2 = true;
                    break;
                case EXECUTE:
                    z3 = true;
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        }
        if (this.zfs.getFileAttributes(getResolvedPath()) == null && (this.path.length != 1 || this.path[0] != 47)) {
            throw new NoSuchFileException(toString());
        }
        if (z2 && this.zfs.isReadOnly()) {
            throw new AccessDeniedException(toString());
        }
        if (z3) {
            throw new AccessDeniedException(toString());
        }
    }

    boolean exists() {
        if (this.path.length == 1 && this.path[0] == 47) {
            return true;
        }
        try {
            return this.zfs.exists(getResolvedPath());
        } catch (IOException e2) {
            return false;
        }
    }

    OutputStream newOutputStream(OpenOption... openOptionArr) throws IOException {
        return openOptionArr.length == 0 ? this.zfs.newOutputStream(getResolvedPath(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE) : this.zfs.newOutputStream(getResolvedPath(), openOptionArr);
    }

    void move(ZipPath zipPath, CopyOption... copyOptionArr) throws IOException {
        if (Files.isSameFile(this.zfs.getZipFile(), zipPath.zfs.getZipFile())) {
            this.zfs.copyFile(true, getResolvedPath(), zipPath.getResolvedPath(), copyOptionArr);
        } else {
            copyToTarget(zipPath, copyOptionArr);
            delete();
        }
    }

    void copy(ZipPath zipPath, CopyOption... copyOptionArr) throws IOException {
        if (Files.isSameFile(this.zfs.getZipFile(), zipPath.zfs.getZipFile())) {
            this.zfs.copyFile(false, getResolvedPath(), zipPath.getResolvedPath(), copyOptionArr);
        } else {
            copyToTarget(zipPath, copyOptionArr);
        }
    }

    /* JADX WARN: Finally extract failed */
    private void copyToTarget(ZipPath zipPath, CopyOption... copyOptionArr) throws IOException {
        boolean zExists;
        boolean z2 = false;
        boolean z3 = false;
        for (CopyOption copyOption : copyOptionArr) {
            if (copyOption == StandardCopyOption.REPLACE_EXISTING) {
                z2 = true;
            } else if (copyOption == StandardCopyOption.COPY_ATTRIBUTES) {
                z3 = true;
            }
        }
        ZipFileAttributes attributes = getAttributes();
        if (z2) {
            try {
                zipPath.deleteIfExists();
                zExists = false;
            } catch (DirectoryNotEmptyException e2) {
                zExists = true;
            }
        } else {
            zExists = zipPath.exists();
        }
        if (zExists) {
            throw new FileAlreadyExistsException(zipPath.toString());
        }
        if (attributes.isDirectory()) {
            zipPath.createDirectory(new FileAttribute[0]);
        } else {
            InputStream inputStreamNewInputStream = this.zfs.newInputStream(getResolvedPath());
            try {
                OutputStream outputStreamNewOutputStream = zipPath.newOutputStream(new OpenOption[0]);
                try {
                    byte[] bArr = new byte[8192];
                    while (true) {
                        int i2 = inputStreamNewInputStream.read(bArr);
                        if (i2 == -1) {
                            break;
                        } else {
                            outputStreamNewOutputStream.write(bArr, 0, i2);
                        }
                    }
                    outputStreamNewOutputStream.close();
                } catch (Throwable th) {
                    outputStreamNewOutputStream.close();
                    throw th;
                }
            } finally {
                inputStreamNewInputStream.close();
            }
        }
        if (z3) {
            try {
                ((BasicFileAttributeView) ZipFileAttributeView.get(zipPath, BasicFileAttributeView.class)).setTimes(attributes.lastModifiedTime(), attributes.lastAccessTime(), attributes.creationTime());
            } catch (IOException e3) {
                try {
                    zipPath.delete();
                } catch (IOException e4) {
                }
                throw e3;
            }
        }
    }
}
