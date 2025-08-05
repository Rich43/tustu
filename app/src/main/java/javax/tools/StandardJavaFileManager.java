package javax.tools;

import java.io.File;
import java.io.IOException;
import javax.tools.JavaFileManager;

/* loaded from: rt.jar:javax/tools/StandardJavaFileManager.class */
public interface StandardJavaFileManager extends JavaFileManager {
    @Override // javax.tools.JavaFileManager
    boolean isSameFile(FileObject fileObject, FileObject fileObject2);

    Iterable<? extends JavaFileObject> getJavaFileObjectsFromFiles(Iterable<? extends File> iterable);

    Iterable<? extends JavaFileObject> getJavaFileObjects(File... fileArr);

    Iterable<? extends JavaFileObject> getJavaFileObjectsFromStrings(Iterable<String> iterable);

    Iterable<? extends JavaFileObject> getJavaFileObjects(String... strArr);

    void setLocation(JavaFileManager.Location location, Iterable<? extends File> iterable) throws IOException;

    Iterable<? extends File> getLocation(JavaFileManager.Location location);
}
