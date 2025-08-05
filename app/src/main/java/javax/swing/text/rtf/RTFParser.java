package javax.swing.text.rtf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/* loaded from: rt.jar:javax/swing/text/rtf/RTFParser.class */
abstract class RTFParser extends AbstractFilter {
    private int pendingCharacter;
    private long binaryBytesLeft;
    ByteArrayOutputStream binaryBuf;
    private boolean[] savedSpecials;
    protected PrintStream warnings;
    static final boolean[] rtfSpecialsTable = (boolean[]) noSpecialsTable.clone();
    private final int S_text = 0;
    private final int S_backslashed = 1;
    private final int S_token = 2;
    private final int S_parameter = 3;
    private final int S_aftertick = 4;
    private final int S_aftertickc = 5;
    private final int S_inblob = 6;
    private StringBuffer currentCharacters = new StringBuffer();
    private int state = 0;
    private String pendingKeyword = null;
    public int level = 0;

    public abstract boolean handleKeyword(String str);

    public abstract boolean handleKeyword(String str, int i2);

    public abstract void handleText(String str);

    public abstract void handleBinaryBlob(byte[] bArr);

    public abstract void begingroup();

    public abstract void endgroup();

    public void handleText(char c2) {
        handleText(String.valueOf(c2));
    }

    static {
        rtfSpecialsTable[10] = true;
        rtfSpecialsTable[13] = true;
        rtfSpecialsTable[123] = true;
        rtfSpecialsTable[125] = true;
        rtfSpecialsTable[92] = true;
    }

    public RTFParser() {
        this.specialsTable = rtfSpecialsTable;
    }

    @Override // javax.swing.text.rtf.AbstractFilter
    public void writeSpecial(int i2) throws IOException {
        write((char) i2);
    }

    protected void warning(String str) {
        if (this.warnings != null) {
            this.warnings.println(str);
        }
    }

    @Override // javax.swing.text.rtf.AbstractFilter
    public void write(String str) throws IOException {
        if (this.state != 0) {
            int i2 = 0;
            int length = str.length();
            while (i2 < length && this.state != 0) {
                write(str.charAt(i2));
                i2++;
            }
            if (i2 >= length) {
                return;
            } else {
                str = str.substring(i2);
            }
        }
        if (this.currentCharacters.length() > 0) {
            this.currentCharacters.append(str);
        } else {
            handleText(str);
        }
    }

    @Override // javax.swing.text.rtf.AbstractFilter
    public void write(char c2) throws IOException {
        switch (this.state) {
            case 0:
                if (c2 != '\n' && c2 != '\r') {
                    if (c2 == '{') {
                        if (this.currentCharacters.length() > 0) {
                            handleText(this.currentCharacters.toString());
                            this.currentCharacters = new StringBuffer();
                        }
                        this.level++;
                        begingroup();
                        return;
                    }
                    if (c2 == '}') {
                        if (this.currentCharacters.length() > 0) {
                            handleText(this.currentCharacters.toString());
                            this.currentCharacters = new StringBuffer();
                        }
                        if (this.level == 0) {
                            throw new IOException("Too many close-groups in RTF text");
                        }
                        endgroup();
                        this.level--;
                        return;
                    }
                    if (c2 == '\\') {
                        if (this.currentCharacters.length() > 0) {
                            handleText(this.currentCharacters.toString());
                            this.currentCharacters = new StringBuffer();
                        }
                        this.state = 1;
                        return;
                    }
                    this.currentCharacters.append(c2);
                    return;
                }
                return;
            case 1:
                if (c2 == '\'') {
                    this.state = 4;
                    return;
                }
                if (!Character.isLetter(c2)) {
                    char[] cArr = {c2};
                    if (!handleKeyword(new String(cArr))) {
                        warning("Unknown keyword: " + ((Object) cArr) + " (" + ((int) ((byte) c2)) + ")");
                    }
                    this.state = 0;
                    this.pendingKeyword = null;
                    return;
                }
                this.state = 2;
                break;
            case 2:
                break;
            case 3:
                if (Character.isDigit(c2)) {
                    this.currentCharacters.append(c2);
                    return;
                }
                if (this.pendingKeyword.equals("bin")) {
                    try {
                        long j2 = Long.parseLong(this.currentCharacters.toString());
                        this.pendingKeyword = null;
                        this.state = 6;
                        this.binaryBytesLeft = j2;
                        if (this.binaryBytesLeft > 4194304) {
                            this.binaryBuf = new ByteArrayOutputStream(4194304);
                        } else if (this.binaryBytesLeft < 0) {
                            this.binaryBytesLeft = 0L;
                            this.binaryBuf = new ByteArrayOutputStream((int) this.binaryBytesLeft);
                        } else {
                            this.binaryBuf = new ByteArrayOutputStream((int) this.binaryBytesLeft);
                        }
                        this.savedSpecials = this.specialsTable;
                        this.specialsTable = allSpecialsTable;
                        return;
                    } catch (NumberFormatException e2) {
                        warning("Illegal number format " + this.currentCharacters.toString() + " in \bin tag");
                        this.pendingKeyword = null;
                        this.currentCharacters = new StringBuffer();
                        this.state = 0;
                        if (!Character.isWhitespace(c2)) {
                            write(c2);
                            return;
                        }
                        return;
                    }
                }
                try {
                    if (!handleKeyword(this.pendingKeyword, Integer.parseInt(this.currentCharacters.toString()))) {
                        warning("Unknown keyword: " + this.pendingKeyword + " (param " + ((Object) this.currentCharacters) + ")");
                    }
                } catch (NumberFormatException e3) {
                    warning("Illegal number format " + this.currentCharacters.toString() + " in " + this.pendingKeyword + " tag");
                }
                this.pendingKeyword = null;
                this.currentCharacters = new StringBuffer();
                this.state = 0;
                if (!Character.isWhitespace(c2)) {
                    write(c2);
                    return;
                }
                return;
            case 4:
                if (Character.digit(c2, 16) == -1) {
                    this.state = 0;
                    return;
                } else {
                    this.pendingCharacter = Character.digit(c2, 16);
                    this.state = 5;
                    return;
                }
            case 5:
                this.state = 0;
                if (Character.digit(c2, 16) != -1) {
                    this.pendingCharacter = (this.pendingCharacter * 16) + Character.digit(c2, 16);
                    char c3 = this.translationTable[this.pendingCharacter];
                    if (c3 != 0) {
                        handleText(c3);
                        return;
                    }
                    return;
                }
                return;
            case 6:
                if (this.binaryBytesLeft > 0) {
                    this.binaryBuf.write(c2);
                    this.binaryBytesLeft--;
                }
                if (this.binaryBytesLeft == 0) {
                    this.state = 0;
                    this.specialsTable = this.savedSpecials;
                    this.savedSpecials = null;
                    handleBinaryBlob(this.binaryBuf.toByteArray());
                    this.binaryBuf = null;
                    return;
                }
                return;
            default:
                return;
        }
        if (Character.isLetter(c2)) {
            this.currentCharacters.append(c2);
            return;
        }
        this.pendingKeyword = this.currentCharacters.toString();
        this.currentCharacters = new StringBuffer();
        if (Character.isDigit(c2) || c2 == '-') {
            this.state = 3;
            this.currentCharacters.append(c2);
            return;
        }
        if (!handleKeyword(this.pendingKeyword)) {
            warning("Unknown keyword: " + this.pendingKeyword);
        }
        this.pendingKeyword = null;
        this.state = 0;
        if (!Character.isWhitespace(c2)) {
            write(c2);
        }
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        super.flush();
        if (this.state == 0 && this.currentCharacters.length() > 0) {
            handleText(this.currentCharacters.toString());
            this.currentCharacters = new StringBuffer();
        }
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        flush();
        if (this.state != 0 || this.level > 0) {
            warning("Truncated RTF file.");
            while (this.level > 0) {
                endgroup();
                this.level--;
            }
        }
        super.close();
    }
}
