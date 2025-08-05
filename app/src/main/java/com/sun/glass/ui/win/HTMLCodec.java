package com.sun.glass.ui.win;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/* compiled from: WinHTMLCodec.java */
/* loaded from: jfxrt.jar:com/sun/glass/ui/win/HTMLCodec.class */
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

    private static String toPaddedString(int n2, int width) {
        String string = "" + n2;
        int len = string.length();
        if (n2 >= 0 && len < width) {
            char[] array = new char[width - len];
            Arrays.fill(array, '0');
            StringBuffer buffer = new StringBuffer(width);
            buffer.append(array);
            buffer.append(string);
            string = buffer.toString();
        }
        return string;
    }

    public static byte[] convertToHTMLFormat(byte[] bytes) {
        String htmlPrefix = "";
        String htmlSuffix = "";
        String stContext = new String(bytes);
        String stUpContext = stContext.toUpperCase();
        if (-1 == stUpContext.indexOf("<HTML")) {
            htmlPrefix = "<HTML>";
            htmlSuffix = "</HTML>";
            if (-1 == stUpContext.indexOf("<BODY")) {
                htmlPrefix = htmlPrefix + "<BODY>";
                htmlSuffix = "</BODY>" + htmlSuffix;
            }
        }
        String htmlPrefix2 = htmlPrefix + "<!--StartFragment-->";
        String htmlSuffix2 = "<!--EndFragment-->" + htmlSuffix;
        int nStartHTML = "Version:".length() + "1.0".length() + "\r\n".length() + "StartHTML:".length() + 10 + "\r\n".length() + "EndHTML:".length() + 10 + "\r\n".length() + "StartFragment:".length() + 10 + "\r\n".length() + "EndFragment:".length() + 10 + "\r\n".length() + "SourceURL:".length() + "about:blank".length() + "\r\n".length();
        int nStartFragment = nStartHTML + htmlPrefix2.length();
        int nEndFragment = (nStartFragment + bytes.length) - 1;
        int nEndHTML = nEndFragment + htmlSuffix2.length();
        StringBuilder header = new StringBuilder(nStartFragment + "<!--StartFragment-->".length());
        header.append("Version:");
        header.append("1.0");
        header.append("\r\n");
        header.append("StartHTML:");
        header.append(toPaddedString(nStartHTML, 10));
        header.append("\r\n");
        header.append("EndHTML:");
        header.append(toPaddedString(nEndHTML, 10));
        header.append("\r\n");
        header.append("StartFragment:");
        header.append(toPaddedString(nStartFragment, 10));
        header.append("\r\n");
        header.append("EndFragment:");
        header.append(toPaddedString(nEndFragment, 10));
        header.append("\r\n");
        header.append("SourceURL:");
        header.append("about:blank");
        header.append("\r\n");
        header.append(htmlPrefix2);
        try {
            byte[] headerBytes = header.toString().getBytes("UTF-8");
            byte[] trailerBytes = htmlSuffix2.getBytes("UTF-8");
            byte[] retval = new byte[headerBytes.length + bytes.length + trailerBytes.length];
            System.arraycopy(headerBytes, 0, retval, 0, headerBytes.length);
            System.arraycopy(bytes, 0, retval, headerBytes.length, bytes.length - 1);
            System.arraycopy(trailerBytes, 0, retval, (headerBytes.length + bytes.length) - 1, trailerBytes.length);
            retval[retval.length - 1] = 0;
            return retval;
        } catch (UnsupportedEncodingException e2) {
            return null;
        }
    }

    public HTMLCodec(InputStream _bytestream, EHTMLReadMode _readMode) throws IOException {
        this.bufferedStream = new BufferedInputStream(_bytestream, 8192);
        this.readMode = _readMode;
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
        String stLine;
        this.stBaseURL = null;
        this.stVersion = null;
        this.iSelStart = -1L;
        this.iSelEnd = -1L;
        (-1).iFragStart = this;
        this.iFragEnd = this;
        this.iHTMLStart = -1L;
        (-1).iHTMLEnd = this;
        this.bufferedStream.mark(8192);
        String[] astEntries = {"Version:", "StartHTML:", "EndHTML:", "StartFragment:", "EndFragment:", "StartSelection:", "EndSelection:", "SourceURL:"};
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.bufferedStream, "UTF-8"), 2730);
        long iHeadSize = 0;
        long iCRSize = "\r\n".length();
        int iEntCount = astEntries.length;
        int iEntry = 0;
        while (iEntry < iEntCount && null != (stLine = bufferedReader.readLine())) {
            while (true) {
                if (iEntry >= iEntCount) {
                    break;
                }
                if (!stLine.startsWith(astEntries[iEntry])) {
                    iEntry++;
                } else {
                    iHeadSize += stLine.length() + iCRSize;
                    String stValue = stLine.substring(astEntries[iEntry].length()).trim();
                    if (null != stValue) {
                        try {
                            switch (iEntry) {
                                case 0:
                                    this.stVersion = stValue;
                                    break;
                                case 1:
                                    this.iHTMLStart = Integer.parseInt(stValue);
                                    break;
                                case 2:
                                    this.iHTMLEnd = Integer.parseInt(stValue);
                                    break;
                                case 3:
                                    this.iFragStart = Integer.parseInt(stValue);
                                    break;
                                case 4:
                                    this.iFragEnd = Integer.parseInt(stValue);
                                    break;
                                case 5:
                                    this.iSelStart = Integer.parseInt(stValue);
                                    break;
                                case 6:
                                    this.iSelEnd = Integer.parseInt(stValue);
                                    break;
                                case 7:
                                    this.stBaseURL = stValue;
                                    break;
                            }
                        } catch (NumberFormatException e2) {
                            throw new IOException(FAILURE_MSG + astEntries[iEntry] + " value " + ((Object) e2) + INVALID_MSG);
                        }
                    } else {
                        continue;
                    }
                }
            }
            iEntry++;
        }
        if (-1 == this.iHTMLStart) {
            this.iHTMLStart = iHeadSize;
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
            int curOffset = iSkip;
            if (curOffset < this.iStartOffset) {
                iSkip = (int) (curOffset + this.bufferedStream.skip(this.iStartOffset - curOffset));
            } else {
                this.iReadCount = curOffset;
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
        int retval;
        if (this.closed) {
            throw new IOException("Stream closed");
        }
        if (!this.descriptionParsed) {
            parseDescription();
        }
        if ((-1 != this.iEndOffset && this.iReadCount >= this.iEndOffset) || (retval = this.bufferedStream.read()) == -1) {
            return -1;
        }
        this.iReadCount++;
        return retval;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            this.bufferedStream.close();
        }
    }
}
