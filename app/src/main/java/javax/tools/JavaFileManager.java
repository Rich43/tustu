package javax.tools;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import javax.tools.JavaFileObject;

/* loaded from: rt.jar:javax/tools/JavaFileManager.class */
public interface JavaFileManager extends Closeable, Flushable, OptionChecker {

    /* loaded from: rt.jar:javax/tools/JavaFileManager$Location.class */
    public interface Location {
        String getName();

        boolean isOutputLocation();
    }

    ClassLoader getClassLoader(Location location);

    Iterable<JavaFileObject> list(Location location, String str, Set<JavaFileObject.Kind> set, boolean z2) throws IOException;

    String inferBinaryName(Location location, JavaFileObject javaFileObject);

    boolean isSameFile(FileObject fileObject, FileObject fileObject2);

    boolean handleOption(String str, Iterator<String> it);

    boolean hasLocation(Location location);

    JavaFileObject getJavaFileForInput(Location location, String str, JavaFileObject.Kind kind) throws IOException;

    JavaFileObject getJavaFileForOutput(Location location, String str, JavaFileObject.Kind kind, FileObject fileObject) throws IOException;

    FileObject getFileForInput(Location location, String str, String str2) throws IOException;

    FileObject getFileForOutput(Location location, String str, String str2, FileObject fileObject) throws IOException;

    void flush() throws IOException;

    @Override // java.io.Closeable, java.lang.AutoCloseable
    void close() throws IOException;
}
