package sun.nio.fs;

import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;

/* loaded from: rt.jar:sun/nio/fs/AbstractFileSystemProvider.class */
abstract class AbstractFileSystemProvider extends FileSystemProvider {
    abstract DynamicFileAttributeView getFileAttributeView(Path path, String str, LinkOption... linkOptionArr);

    abstract boolean implDelete(Path path, boolean z2) throws IOException;

    protected AbstractFileSystemProvider() {
    }

    private static String[] split(String str) {
        String[] strArr = new String[2];
        int iIndexOf = str.indexOf(58);
        if (iIndexOf == -1) {
            strArr[0] = "basic";
            strArr[1] = str;
        } else {
            int i2 = iIndexOf + 1;
            strArr[0] = str.substring(0, iIndexOf);
            strArr[1] = i2 == str.length() ? "" : str.substring(i2);
        }
        return strArr;
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public final void setAttribute(Path path, String str, Object obj, LinkOption... linkOptionArr) throws IOException {
        String[] strArrSplit = split(str);
        if (strArrSplit[0].length() == 0) {
            throw new IllegalArgumentException(str);
        }
        DynamicFileAttributeView fileAttributeView = getFileAttributeView(path, strArrSplit[0], linkOptionArr);
        if (fileAttributeView == null) {
            throw new UnsupportedOperationException("View '" + strArrSplit[0] + "' not available");
        }
        fileAttributeView.setAttribute(strArrSplit[1], obj);
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public final Map<String, Object> readAttributes(Path path, String str, LinkOption... linkOptionArr) throws IOException {
        String[] strArrSplit = split(str);
        if (strArrSplit[0].length() == 0) {
            throw new IllegalArgumentException(str);
        }
        DynamicFileAttributeView fileAttributeView = getFileAttributeView(path, strArrSplit[0], linkOptionArr);
        if (fileAttributeView == null) {
            throw new UnsupportedOperationException("View '" + strArrSplit[0] + "' not available");
        }
        return fileAttributeView.readAttributes(strArrSplit[1].split(","));
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public final void delete(Path path) throws IOException {
        implDelete(path, true);
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public final boolean deleteIfExists(Path path) throws IOException {
        return implDelete(path, false);
    }
}
