package sun.tools.jar;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;
import sun.net.www.MessageHeader;
import sun.security.pkcs.PKCS7;
import sun.security.x509.AlgorithmId;

/* loaded from: rt.jar:sun/tools/jar/SignatureFile.class */
public class SignatureFile {
    static final boolean debug = false;
    private Vector<MessageHeader> entries;
    static final String[] hashes = {"SHA"};
    private Manifest manifest;
    private String rawName;
    private PKCS7 signatureBlock;
    private Hashtable<String, MessageDigest> digests;

    static final void debug(String str) {
    }

    private SignatureFile(String str) throws JarException {
        this.entries = new Vector<>();
        this.digests = new Hashtable<>();
        this.entries = new Vector<>();
        if (str != null) {
            if (str.length() > 8 || str.indexOf(46) != -1) {
                throw new JarException("invalid file name");
            }
            this.rawName = str.toUpperCase(Locale.ENGLISH);
        }
    }

    private SignatureFile(String str, boolean z2) throws JarException {
        this(str);
        if (z2) {
            MessageHeader messageHeader = new MessageHeader();
            messageHeader.set("Signature-Version", "1.0");
            this.entries.addElement(messageHeader);
        }
    }

    public SignatureFile(Manifest manifest, String str) throws JarException {
        this(str, true);
        this.manifest = manifest;
        Enumeration<MessageHeader> enumerationEntries = manifest.entries();
        while (enumerationEntries.hasMoreElements()) {
            String strFindValue = enumerationEntries.nextElement2().findValue("Name");
            if (strFindValue != null) {
                add(strFindValue);
            }
        }
    }

    public SignatureFile(Manifest manifest, String[] strArr, String str) throws JarException {
        this(str, true);
        this.manifest = manifest;
        add(strArr);
    }

    public SignatureFile(InputStream inputStream, String str) throws IOException {
        this(str);
        while (inputStream.available() > 0) {
            this.entries.addElement(new MessageHeader(inputStream));
        }
    }

    public SignatureFile(InputStream inputStream) throws IOException {
        this(inputStream, (String) null);
    }

    public SignatureFile(byte[] bArr) throws IOException {
        this(new ByteArrayInputStream(bArr));
    }

    public String getName() {
        return "META-INF/" + this.rawName + ".SF";
    }

    public String getBlockName() {
        String name = "DSA";
        if (this.signatureBlock != null) {
            name = this.signatureBlock.getSignerInfos()[0].getDigestEncryptionAlgorithmId().getName();
            String encAlgFromSigAlg = AlgorithmId.getEncAlgFromSigAlg(name);
            if (encAlgFromSigAlg != null) {
                name = encAlgFromSigAlg;
            }
        }
        return "META-INF/" + this.rawName + "." + name;
    }

    public PKCS7 getBlock() {
        return this.signatureBlock;
    }

    public void setBlock(PKCS7 pkcs7) {
        this.signatureBlock = pkcs7;
    }

    public void add(String[] strArr) throws JarException {
        for (String str : strArr) {
            add(str);
        }
    }

    public void add(String str) throws JarException {
        MessageHeader entry = this.manifest.getEntry(str);
        if (entry == null) {
            throw new JarException("entry " + str + " not in manifest");
        }
        try {
            this.entries.addElement(computeEntry(entry));
        } catch (IOException e2) {
            throw new JarException(e2.getMessage());
        }
    }

    public MessageHeader getEntry(String str) {
        Enumeration<MessageHeader> enumerationEntries = entries();
        while (enumerationEntries.hasMoreElements()) {
            MessageHeader messageHeaderNextElement2 = enumerationEntries.nextElement2();
            if (str.equals(messageHeaderNextElement2.findValue("Name"))) {
                return messageHeaderNextElement2;
            }
        }
        return null;
    }

    public MessageHeader entryAt(int i2) {
        return this.entries.elementAt(i2);
    }

    public Enumeration<MessageHeader> entries() {
        return this.entries.elements();
    }

    private MessageHeader computeEntry(MessageHeader messageHeader) throws IOException {
        MessageHeader messageHeader2 = new MessageHeader();
        String strFindValue = messageHeader.findValue("Name");
        if (strFindValue == null) {
            return null;
        }
        messageHeader2.set("Name", strFindValue);
        for (int i2 = 0; i2 < hashes.length; i2++) {
            try {
                MessageDigest digest = getDigest(hashes[i2]);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                messageHeader.print(new PrintStream(byteArrayOutputStream));
                messageHeader2.set(hashes[i2] + "-Digest", Base64.getMimeEncoder().encodeToString(digest.digest(byteArrayOutputStream.toByteArray())));
            } catch (NoSuchAlgorithmException e2) {
                throw new JarException(e2.getMessage());
            }
        }
        return messageHeader2;
    }

    private MessageDigest getDigest(String str) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = this.digests.get(str);
        if (messageDigest == null) {
            messageDigest = MessageDigest.getInstance(str);
            this.digests.put(str, messageDigest);
        }
        messageDigest.reset();
        return messageDigest;
    }

    public void stream(OutputStream outputStream) throws IOException {
        MessageHeader messageHeaderElementAt = this.entries.elementAt(0);
        if (messageHeaderElementAt.findValue("Signature-Version") == null) {
            throw new JarException("Signature file requires Signature-Version: 1.0 in 1st header");
        }
        PrintStream printStream = new PrintStream(outputStream);
        messageHeaderElementAt.print(printStream);
        for (int i2 = 1; i2 < this.entries.size(); i2++) {
            this.entries.elementAt(i2).print(printStream);
        }
    }
}
