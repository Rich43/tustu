package java.io;

/* loaded from: rt.jar:java/io/DefaultFileSystem.class */
class DefaultFileSystem {
    DefaultFileSystem() {
    }

    public static FileSystem getFileSystem() {
        return new WinNTFileSystem();
    }
}
