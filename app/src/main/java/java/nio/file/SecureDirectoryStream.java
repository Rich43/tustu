package java.nio.file;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.util.Set;

/* loaded from: rt.jar:java/nio/file/SecureDirectoryStream.class */
public interface SecureDirectoryStream<T> extends DirectoryStream<T> {
    SecureDirectoryStream<T> newDirectoryStream(T t2, LinkOption... linkOptionArr) throws IOException;

    SeekableByteChannel newByteChannel(T t2, Set<? extends OpenOption> set, FileAttribute<?>... fileAttributeArr) throws IOException;

    void deleteFile(T t2) throws IOException;

    void deleteDirectory(T t2) throws IOException;

    void move(T t2, SecureDirectoryStream<T> secureDirectoryStream, T t3) throws IOException;

    <V extends FileAttributeView> V getFileAttributeView(Class<V> cls);

    <V extends FileAttributeView> V getFileAttributeView(T t2, Class<V> cls, LinkOption... linkOptionArr);
}
