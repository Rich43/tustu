package sun.awt.windows;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/* compiled from: WDataTransferer.java */
/* loaded from: rt.jar:sun/awt/windows/HTMLCodec.class */
class HTMLCodec extends InputStream {
    public static final String ENCODING = "UTF-8";
    public static final String VERSION = "Version:";
    public static final String START_HTML = "StartHTML:";
    public static final String END_HTML = "EndHTML:";
    public static final String START_FRAGMENT = "StartFragment:";
    public static final String END_FRAGMENT = "EndFragment:";
    public static final String START_SELECTION = "StartSelection:";
    public static final String END_SELECTION = "EndSelection:";
    public static final String START_FRAGMENT_CMT = "<!--StartFragment-->";
    public static final String END_FRAGMENT_CMT = "<!--EndFragment-->";
    public static final String SOURCE_URL = "SourceURL:";
    public static final String DEF_SOURCE_URL = "about:blank";
    public static final String EOLN = "\r\n";
    private static final String VERSION_NUM = "1.0";
    private static final int PADDED_WIDTH = 10;
    private final BufferedInputStream bufferedStream;
    private boolean descriptionParsed = false;
    private boolean closed = false;
    public static final int BYTE_BUFFER_LEN = 8192;
    public static final int CHAR_BUFFER_LEN = 2730;
    private static final String FAILURE_MSG = "Unable to parse HTML description: ";
    private static final String INVALID_MSG = " invalid";
    private long iHTMLStart;
    private long iHTMLEnd;
    private long iFragStart;
    private long iFragEnd;
    private long iSelStart;
    private long iSelEnd;
    private String stBaseURL;
    private String stVersion;
    private long iStartOffset;
    private long iEndOffset;
    private long iReadCount;
    private EHTMLReadMode readMode;

    private static String toPaddedString(int i2, int i3) {
        String string = "" + i2;
        int length = string.length();
        if (i2 >= 0 && length < i3) {
            char[] cArr = new char[i3 - length];
            Arrays.fill(cArr, '0');
            StringBuffer stringBuffer = new StringBuffer(i3);
            stringBuffer.append(cArr);
            stringBuffer.append(string);
            string = stringBuffer.toString();
        }
        return string;
    }

    public static byte[] convertToHTMLFormat(byte[] bArr) {
        String str = "";
        String str2 = "";
        String upperCase = new String(bArr).toUpperCase();
        if (-1 == upperCase.indexOf("<HTML")) {
            str = "<HTML>";
            str2 = "</HTML>";
            if (-1 == upperCase.indexOf("<BODY")) {
                str = str + "<BODY>";
                str2 = "</BODY>" + str2;
            }
        }
        int length = "Version:".length() + "1.0".length() + "\r\n".length() + "StartHTML:".length() + 10 + "\r\n".length() + "EndHTML:".length() + 10 + "\r\n".length() + "StartFragment:".length() + 10 + "\r\n".length() + "EndFragment:".length() + 10 + "\r\n".length() + "SourceURL:".length() + "about:blank".length() + "\r\n".length();
        int length2 = length + str.length();
        int length3 = (length2 + bArr.length) - 1;
        int length4 = length3 + str2.length();
        StringBuilder sb = new StringBuilder(length2 + "<!--StartFragment-->".length());
        sb.append("Version:");
        sb.append("1.0");
        sb.append("\r\n");
        sb.append("StartHTML:");
        sb.append(toPaddedString(length, 10));
        sb.append("\r\n");
        sb.append("EndHTML:");
        sb.append(toPaddedString(length4, 10));
        sb.append("\r\n");
        sb.append("StartFragment:");
        sb.append(toPaddedString(length2, 10));
        sb.append("\r\n");
        sb.append("EndFragment:");
        sb.append(toPaddedString(length3, 10));
        sb.append("\r\n");
        sb.append("SourceURL:");
        sb.append("about:blank");
        sb.append("\r\n");
        sb.append(str);
        byte[] bytes = null;
        byte[] bytes2 = null;
        try {
            bytes = sb.toString().getBytes("UTF-8");
            bytes2 = str2.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e2) {
        }
        byte[] bArr2 = new byte[bytes.length + bArr.length + bytes2.length];
        System.arraycopy(bytes, 0, bArr2, 0, bytes.length);
        System.arraycopy(bArr, 0, bArr2, bytes.length, bArr.length - 1);
        System.arraycopy(bytes2, 0, bArr2, (bytes.length + bArr.length) - 1, bytes2.length);
        bArr2[bArr2.length - 1] = 0;
        return bArr2;
    }

    public HTMLCodec(InputStream inputStream, EHTMLReadMode eHTMLReadMode) throws IOException {
        this.bufferedStream = new BufferedInputStream(inputStream, 8192);
        this.readMode = eHTMLReadMode;
    }

    public synchronized String getBaseURL() throws IOException {
        if (!this.descriptionParsed) {
            parseDescription();
        }
        return this.stBaseURL;
    }

    public synchronized String getVersion() throws IOException {
        if (!this.descriptionParsed) {
            parseDescription();
        }
        return this.stVersion;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Multi-variable type inference failed */
    private void parseDescription() throws IOException {
        String line;
        this.stBaseURL = null;
        this.stVersion = null;
        this.iSelStart = -1L;
        this.iSelEnd = -1L;
        (-1).iFragStart = this;
        this.iFragEnd = this;
        this.iHTMLStart = -1L;
        (-1).iHTMLEnd = this;
        this.bufferedStream.mark(8192);
        String[] strArr = {"Version:", "StartHTML:", "EndHTML:", "StartFragment:", "EndFragment:", "StartSelection:", "EndSelection:", "SourceURL:"};
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.bufferedStream, "UTF-8"), 2730);
        long length = 0;
        long length2 = "\r\n".length();
        int length3 = strArr.length;
        int i2 = 0;
        while (i2 < length3 && null != (line = bufferedReader.readLine())) {
            while (true) {
                if (i2 >= length3) {
                    break;
                }
                if (!line.startsWith(strArr[i2])) {
                    i2++;
                } else {
                    length += line.length() + length2;
                    String strTrim = line.substring(strArr[i2].length()).trim();
                    if (null != strTrim) {
                        try {
                            switch (i2) {
                                case 0:
                                    this.stVersion = strTrim;
                                    break;
                                case 1:
                                    this.iHTMLStart = Integer.parseInt(strTrim);
                                    break;
                                case 2:
                                    this.iHTMLEnd = Integer.parseInt(strTrim);
                                    break;
                                case 3:
                                    this.iFragStart = Integer.parseInt(strTrim);
                                    break;
                                case 4:
                                    this.iFragEnd = Integer.parseInt(strTrim);
                                    break;
                                case 5:
                                    this.iSelStart = Integer.parseInt(strTrim);
                                    break;
                                case 6:
                                    this.iSelEnd = Integer.parseInt(strTrim);
                                    break;
                                case 7:
                                    this.stBaseURL = strTrim;
                                    break;
                            }
                        } catch (NumberFormatException e2) {
                            throw new IOException(FAILURE_MSG + strArr[i2] + " value " + ((Object) e2) + INVALID_MSG);
                        }
                    } else {
                        continue;
                    }
                }
            }
            i2++;
        }
        if (-1 == this.iHTMLStart) {
            this.iHTMLStart = length;
        }
        if (-1 == this.iFragStart) {
            this.iFragStart = this.iHTMLStart;
        }
        if (-1 == this.iFragEnd) {
            this.iFragEnd = this.iHTMLEnd;
        }
        if (-1 == this.iSelStart) {
            this.iSelStart = this.iFragStart;
        }
        if (-1 == this.iSelEnd) {
            this.iSelEnd = this.iFragEnd;
        }
        switch (this.readMode) {
            case HTML_READ_ALL:
                this.iStartOffset = this.iHTMLStart;
                this.iEndOffset = this.iHTMLEnd;
                break;
            case HTML_READ_FRAGMENT:
                this.iStartOffset = this.iFragStart;
                this.iEndOffset = this.iFragEnd;
                break;
            case HTML_READ_SELECTION:
            default:
                this.iStartOffset = this.iSelStart;
                this.iEndOffset = this.iSelEnd;
                break;
        }
        this.bufferedStream.reset();
        if (-1 == this.iStartOffset) {
            throw new IOException("Unable to parse HTML description: invalid HTML format.");
        }
        int iSkip = 0;
        while (true) {
            int i3 = iSkip;
            if (i3 < this.iStartOffset) {
                iSkip = (int) (i3 + this.bufferedStream.skip(this.iStartOffset - i3));
            } else {
                this.iReadCount = i3;
                if (this.iStartOffset != this.iReadCount) {
                    throw new IOException("Unable to parse HTML description: Byte stream ends in description.");
                }
                this.descriptionParsed = true;
                return;
            }
        }
    }

    @Override // java.io.InputStream
    public synchronized int read() throws IOException {
        int i2;
        if (this.closed) {
            throw new IOException("Stream closed");
        }
        if (!this.descriptionParsed) {
            parseDescription();
        }
        if ((-1 != this.iEndOffset && this.iReadCount >= this.iEndOffset) || (i2 = this.bufferedStream.read()) == -1) {
            return -1;
        }
        this.iReadCount++;
        return i2;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            this.bufferedStream.close();
        }
    }
}
