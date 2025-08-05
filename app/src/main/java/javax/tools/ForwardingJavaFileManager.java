package javax.tools;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

/* loaded from: rt.jar:javax/tools/ForwardingJavaFileManager.class */
public class ForwardingJavaFileManager<M extends JavaFileManager> implements JavaFileManager {
    protected final M fileManager;

    protected ForwardingJavaFileManager(M m2) {
        m2.getClass();
        this.fileManager = m2;
    }

    @Override // javax.tools.JavaFileManager
    public ClassLoader getClassLoader(JavaFileManager.Location location) {
        return this.fileManager.getClassLoader(location);
    }

    @Override // javax.tools.JavaFileManager
    public Iterable<JavaFileObject> list(JavaFileManager.Location location, String str, Set<JavaFileObject.Kind> set, boolean z2) throws IOException {
        return this.fileManager.list(location, str, set, z2);
    }

    @Override // javax.tools.JavaFileManager
    public String inferBinaryName(JavaFileManager.Location location, JavaFileObject javaFileObject) {
        return this.fileManager.inferBinaryName(location, javaFileObject);
    }

    @Override // javax.tools.JavaFileManager
    public boolean isSameFile(FileObject fileObject, FileObject fileObject2) {
        return this.fileManager.isSameFile(fileObject, fileObject2);
    }

    @Override // javax.tools.JavaFileManager
    public boolean handleOption(String str, Iterator<String> it) {
        return this.fileManager.handleOption(str, it);
    }

    @Override // javax.tools.JavaFileManager
    public boolean hasLocation(JavaFileManager.Location location) {
        return this.fileManager.hasLocation(location);
    }

    @Override // javax.tools.OptionChecker
    public int isSupportedOption(String str) {
        return this.fileManager.isSupportedOption(str);
    }

    @Override // javax.tools.JavaFileManager
    public JavaFileObject getJavaFileForInput(JavaFileManager.Location location, String str, JavaFileObject.Kind kind) throws IOException {
        return this.fileManager.getJavaFileForInput(location, str, kind);
    }

    @Override // javax.tools.JavaFileManager
    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String str, JavaFileObject.Kind kind, FileObject fileObject) throws IOException {
        return this.fileManager.getJavaFileForOutput(location, str, kind, fileObject);
    }

    @Override // javax.tools.JavaFileManager
    public FileObject getFileForInput(JavaFileManager.Location location, String str, String str2) throws IOException {
        return this.fileManager.getFileForInput(location, str, str2);
    }

    @Override // javax.tools.JavaFileManager
    public FileObject getFileForOutput(JavaFileManager.Location location, String str, String str2, FileObject fileObject) throws IOException {
        return this.fileManager.getFileForOutput(location, str, str2, fileObject);
    }

    @Override // javax.tools.JavaFileManager, java.io.Flushable
    public void flush() throws IOException {
        this.fileManager.flush();
    }

    @Override // javax.tools.JavaFileManager, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.fileManager.close();
    }
}
