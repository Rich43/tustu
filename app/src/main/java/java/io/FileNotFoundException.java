package java.io;

/* loaded from: rt.jar:java/io/FileNotFoundException.class */
public class FileNotFoundException extends IOException {
    private static final long serialVersionUID = -897856973823710492L;

    public FileNotFoundException() {
    }

    public FileNotFoundException(String str) {
        super(str);
    }

    private FileNotFoundException(String str, String str2) {
        super(str + (str2 == null ? "" : " (" + str2 + ")"));
    }
}
