package sun.tools.jar;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.jar.JarFile;
import sun.net.www.MessageHeader;

/* loaded from: rt.jar:sun/tools/jar/Manifest.class */
public class Manifest {
    private Vector<MessageHeader> entries;
    private byte[] tmpbuf;
    private Hashtable<String, MessageHeader> tableEntries;
    static final String[] hashes = {"SHA"};
    static final byte[] EOL = {13, 10};
    static final boolean debug = false;
    static final String VERSION = "1.0";

    static final void debug(String str) {
    }

    public Manifest() {
        this.entries = new Vector<>();
        this.tmpbuf = new byte[512];
        this.tableEntries = new Hashtable<>();
    }

    public Manifest(byte[] bArr) throws IOException {
        this(new ByteArrayInputStream(bArr), false);
    }

    public Manifest(InputStream inputStream) throws IOException {
        this(inputStream, true);
    }

    public Manifest(InputStream inputStream, boolean z2) throws IOException {
        this.entries = new Vector<>();
        this.tmpbuf = new byte[512];
        this.tableEntries = new Hashtable<>();
        inputStream = inputStream.markSupported() ? inputStream : new BufferedInputStream(inputStream);
        while (true) {
            inputStream.mark(1);
            if (inputStream.read() != -1) {
                inputStream.reset();
                MessageHeader messageHeader = new MessageHeader(inputStream);
                if (z2) {
                    doHashes(messageHeader);
                }
                addEntry(messageHeader);
            } else {
                return;
            }
        }
    }

    public Manifest(String[] strArr) throws IOException {
        this.entries = new Vector<>();
        this.tmpbuf = new byte[512];
        this.tableEntries = new Hashtable<>();
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.add("Manifest-Version", "1.0");
        messageHeader.add("Created-By", "Manifest JDK " + System.getProperty("java.version"));
        addEntry(messageHeader);
        addFiles(null, strArr);
    }

    public void addEntry(MessageHeader messageHeader) {
        this.entries.addElement(messageHeader);
        String strFindValue = messageHeader.findValue("Name");
        debug("addEntry for name: " + strFindValue);
        if (strFindValue != null) {
            this.tableEntries.put(strFindValue, messageHeader);
        }
    }

    public MessageHeader getEntry(String str) {
        return this.tableEntries.get(str);
    }

    public MessageHeader entryAt(int i2) {
        return this.entries.elementAt(i2);
    }

    public Enumeration<MessageHeader> entries() {
        return this.entries.elements();
    }

    public void addFiles(File file, String[] strArr) throws IOException {
        File file2;
        if (strArr == null) {
            return;
        }
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (file == null) {
                file2 = new File(strArr[i2]);
            } else {
                file2 = new File(file, strArr[i2]);
            }
            if (file2.isDirectory()) {
                addFiles(file2, file2.list());
            } else {
                addFile(file2);
            }
        }
    }

    private final String stdToLocal(String str) {
        return str.replace('/', File.separatorChar);
    }

    private final String localToStd(String str) {
        String strReplace = str.replace(File.separatorChar, '/');
        if (strReplace.startsWith("./")) {
            strReplace = strReplace.substring(2);
        } else if (strReplace.startsWith("/")) {
            strReplace = strReplace.substring(1);
        }
        return strReplace;
    }

    public void addFile(File file) throws IOException {
        String strLocalToStd = localToStd(file.getPath());
        if (this.tableEntries.get(strLocalToStd) == null) {
            MessageHeader messageHeader = new MessageHeader();
            messageHeader.add("Name", strLocalToStd);
            addEntry(messageHeader);
        }
    }

    public void doHashes(MessageHeader messageHeader) throws IOException {
        String strFindValue = messageHeader.findValue("Name");
        if (strFindValue == null || strFindValue.endsWith("/")) {
            return;
        }
        for (int i2 = 0; i2 < hashes.length; i2++) {
            FileInputStream fileInputStream = new FileInputStream(stdToLocal(strFindValue));
            try {
                try {
                    MessageDigest messageDigest = MessageDigest.getInstance(hashes[i2]);
                    while (true) {
                        int i3 = fileInputStream.read(this.tmpbuf, 0, this.tmpbuf.length);
                        if (i3 == -1) {
                            break;
                        } else {
                            messageDigest.update(this.tmpbuf, 0, i3);
                        }
                    }
                    messageHeader.set(hashes[i2] + "-Digest", Base64.getMimeEncoder().encodeToString(messageDigest.digest()));
                    fileInputStream.close();
                } catch (NoSuchAlgorithmException e2) {
                    throw new JarException("Digest algorithm " + hashes[i2] + " not available.");
                }
            } catch (Throwable th) {
                fileInputStream.close();
                throw th;
            }
        }
    }

    public void stream(OutputStream outputStream) throws IOException {
        PrintStream printStream;
        if (outputStream instanceof PrintStream) {
            printStream = (PrintStream) outputStream;
        } else {
            printStream = new PrintStream(outputStream);
        }
        MessageHeader messageHeaderElementAt = this.entries.elementAt(0);
        if (messageHeaderElementAt.findValue("Manifest-Version") == null) {
            String property = System.getProperty("java.version");
            if (messageHeaderElementAt.findValue("Name") == null) {
                messageHeaderElementAt.prepend("Manifest-Version", "1.0");
                messageHeaderElementAt.add("Created-By", "Manifest JDK " + property);
            } else {
                printStream.print("Manifest-Version: 1.0\r\nCreated-By: " + property + "\r\n\r\n");
            }
            printStream.flush();
        }
        messageHeaderElementAt.print(printStream);
        for (int i2 = 1; i2 < this.entries.size(); i2++) {
            this.entries.elementAt(i2).print(printStream);
        }
    }

    public static boolean isManifestName(String str) {
        if (str.charAt(0) == '/') {
            str = str.substring(1, str.length());
        }
        if (str.toUpperCase().equals(JarFile.MANIFEST_NAME)) {
            return true;
        }
        return false;
    }
}
