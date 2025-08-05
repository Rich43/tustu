package javax.microedition.io;

/* loaded from: bluecove-2.1.1.jar:javax/microedition/io/ContentConnection.class */
public interface ContentConnection extends StreamConnection {
    String getType();

    String getEncoding();

    long getLength();
}
