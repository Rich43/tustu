package sun.security.pkcs;

import java.io.IOException;
import javax.xml.datatype.DatatypeConstants;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

/* loaded from: rt.jar:sun/security/pkcs/ContentInfo.class */
public class ContentInfo {
    private static int[] pkcs7 = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 7};
    private static int[] data = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 7, 1};
    private static int[] sdata = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 7, 2};
    private static int[] edata = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 7, 3};
    private static int[] sedata = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 7, 4};
    private static int[] ddata = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 7, 5};
    private static int[] crdata = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 7, 6};
    private static int[] nsdata = {2, 16, DatatypeConstants.MIN_TIMEZONE_OFFSET, 1, 113730, 2, 5};
    private static int[] tstInfo = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 9, 16, 1, 4};
    private static final int[] OLD_SDATA = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 1113549, 1, 7, 2};
    private static final int[] OLD_DATA = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 1113549, 1, 7, 1};
    public static ObjectIdentifier PKCS7_OID = ObjectIdentifier.newInternal(pkcs7);
    public static ObjectIdentifier DATA_OID = ObjectIdentifier.newInternal(data);
    public static ObjectIdentifier SIGNED_DATA_OID = ObjectIdentifier.newInternal(sdata);
    public static ObjectIdentifier ENVELOPED_DATA_OID = ObjectIdentifier.newInternal(edata);
    public static ObjectIdentifier SIGNED_AND_ENVELOPED_DATA_OID = ObjectIdentifier.newInternal(sedata);
    public static ObjectIdentifier DIGESTED_DATA_OID = ObjectIdentifier.newInternal(ddata);
    public static ObjectIdentifier ENCRYPTED_DATA_OID = ObjectIdentifier.newInternal(crdata);
    public static ObjectIdentifier OLD_SIGNED_DATA_OID = ObjectIdentifier.newInternal(OLD_SDATA);
    public static ObjectIdentifier OLD_DATA_OID = ObjectIdentifier.newInternal(OLD_DATA);
    public static ObjectIdentifier NETSCAPE_CERT_SEQUENCE_OID = ObjectIdentifier.newInternal(nsdata);
    public static ObjectIdentifier TIMESTAMP_TOKEN_INFO_OID = ObjectIdentifier.newInternal(tstInfo);
    ObjectIdentifier contentType;
    DerValue content;

    public ContentInfo(ObjectIdentifier objectIdentifier, DerValue derValue) {
        this.contentType = objectIdentifier;
        this.content = derValue;
    }

    public ContentInfo(byte[] bArr) {
        DerValue derValue = new DerValue((byte) 4, bArr);
        this.contentType = DATA_OID;
        this.content = derValue;
    }

    public ContentInfo(DerInputStream derInputStream) throws IOException {
        this(derInputStream, false);
    }

    public ContentInfo(DerInputStream derInputStream, boolean z2) throws IOException {
        DerValue[] sequence = derInputStream.getSequence(2);
        if (sequence.length < 1 || sequence.length > 2) {
            throw new ParsingException("Invalid length for ContentInfo");
        }
        this.contentType = new DerInputStream(sequence[0].toByteArray()).getOID();
        if (z2) {
            if (sequence.length > 1) {
                this.content = sequence[1];
            }
        } else if (sequence.length > 1) {
            DerValue[] set = new DerInputStream(sequence[1].toByteArray()).getSet(1, true);
            if (set.length != 1) {
                throw new ParsingException("ContentInfo encoding error");
            }
            this.content = set[0];
        }
    }

    public DerValue getContent() {
        return this.content;
    }

    public ObjectIdentifier getContentType() {
        return this.contentType;
    }

    public byte[] getData() throws IOException {
        if (this.contentType.equals((Object) DATA_OID) || this.contentType.equals((Object) OLD_DATA_OID) || this.contentType.equals((Object) TIMESTAMP_TOKEN_INFO_OID)) {
            if (this.content == null) {
                return null;
            }
            return this.content.getOctetString();
        }
        throw new IOException("content type is not DATA: " + ((Object) this.contentType));
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.putOID(this.contentType);
        if (this.content != null) {
            DerOutputStream derOutputStream3 = new DerOutputStream();
            this.content.encode(derOutputStream3);
            derOutputStream2.putDerValue(new DerValue((byte) -96, derOutputStream3.toByteArray()));
        }
        derOutputStream.write((byte) 48, derOutputStream2);
    }

    public byte[] getContentBytes() throws IOException {
        if (this.content == null) {
            return null;
        }
        return new DerInputStream(this.content.toByteArray()).getOctetString();
    }

    public String toString() {
        return ("Content Info Sequence\n\tContent type: " + ((Object) this.contentType) + "\n") + "\tContent: " + ((Object) this.content);
    }
}
