package java.io;

/* loaded from: rt.jar:java/io/FileWriter.class */
public class FileWriter extends OutputStreamWriter {
    public FileWriter(String str) throws IOException {
        super(new FileOutputStream(str));
    }

    public FileWriter(String str, boolean z2) throws IOException {
        super(new FileOutputStream(str, z2));
    }

    public FileWriter(File file) throws IOException {
        super(new FileOutputStream(file));
    }

    public FileWriter(File file, boolean z2) throws IOException {
        super(new FileOutputStream(file, z2));
    }

    public FileWriter(FileDescriptor fileDescriptor) {
        super(new FileOutputStream(fileDescriptor));
    }
}
