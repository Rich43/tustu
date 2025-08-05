package java.util.zip;

/* loaded from: rt.jar:java/util/zip/Checksum.class */
public interface Checksum {
    void update(int i2);

    void update(byte[] bArr, int i2, int i3);

    long getValue();

    void reset();
}
