package com.sun.webkit.network;

import com.sun.glass.ui.Clipboard;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PushbackInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.icepdf.core.util.PdfOps;

/* loaded from: jfxrt.jar:com/sun/webkit/network/DirectoryURLConnection.class */
final class DirectoryURLConnection extends URLConnection {
    private static final String styleSheet = "<style type=\"text/css\" media=\"screen\">TABLE { border: 0;}TR.header { background: #FFFFFF; color: black; font-weight: bold; text-align: center;}TR.odd { background: #E0E0E0;}TR.even { background: #C0C0C0;}TD.file { text-align: left;}TD.fsize { text-align: right; padding-right: 1em;}TD.dir { text-align: center; color: green; padding-right: 1em;}TD.link { text-align: center; color: red; padding-right: 1em;}TD.date { text-align: justify;}</style>";
    private final URLConnection inner;
    private final boolean sure;
    private String dirUrl;
    private boolean toHTML;
    private final boolean ftp;
    private InputStream ins;
    private static final String[] patStrings = {"([\\-ld](?:[r\\-][w\\-][x\\-]){3})\\s*\\d+ (\\w+)\\s*(\\w+)\\s*(\\d+)\\s*([A-Z][a-z][a-z]\\s*\\d+)\\s*((?:\\d\\d:\\d\\d)|(?:\\d{4}))\\s*(\\p{Print}*)", "(\\d{2}/\\d{2}/\\d{4})\\s*(\\d{2}:\\d{2}[ap])\\s*((?:[0-9,]+)|(?:<DIR>))\\s*(\\p{Graph}*)", "(\\d{2}-\\d{2}-\\d{2})\\s*(\\d{2}:\\d{2}[AP]M)\\s*((?:[0-9,]+)|(?:<DIR>))\\s*(\\p{Graph}*)"};
    private static final int[][] patternGroups = {new int[]{7, 4, 5, 6, 1}, new int[]{4, 3, 1, 2, 0}, new int[]{4, 3, 1, 2, 0}};
    private static final Pattern linkp = Pattern.compile("(\\p{Print}+) \\-\\> (\\p{Print}+)$");
    private static final Pattern[] patterns = new Pattern[patStrings.length];

    /* JADX WARN: Type inference failed for: r0v3, types: [int[], int[][]] */
    static {
        for (int i2 = 0; i2 < patStrings.length; i2++) {
            patterns[i2] = Pattern.compile(patStrings[i2]);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/webkit/network/DirectoryURLConnection$DirectoryInputStream.class */
    private final class DirectoryInputStream extends PushbackInputStream {
        private final byte[] buffer;
        private boolean endOfStream;
        private ByteArrayOutputStream bytesOut;
        private PrintStream out;
        private ByteArrayInputStream bytesIn;
        private final StringBuffer tmpString;
        private int lineCount;

        private DirectoryInputStream(InputStream ins, boolean guess) {
            int index;
            super(ins, 512);
            this.endOfStream = false;
            this.bytesOut = new ByteArrayOutputStream();
            this.out = new PrintStream(this.bytesOut);
            this.bytesIn = null;
            this.tmpString = new StringBuffer();
            this.lineCount = 0;
            this.buffer = new byte[512];
            if (guess) {
                StringBuffer line = new StringBuffer();
                int l2 = 0;
                try {
                    l2 = super.read(this.buffer, 0, this.buffer.length);
                } catch (IOException e2) {
                }
                if (l2 <= 0) {
                    DirectoryURLConnection.this.toHTML = false;
                } else {
                    for (int i2 = 0; i2 < l2; i2++) {
                        line.append((char) this.buffer[i2]);
                    }
                    String line2 = line.toString();
                    DirectoryURLConnection.this.toHTML = false;
                    Pattern[] patternArr = DirectoryURLConnection.patterns;
                    int length = patternArr.length;
                    int i3 = 0;
                    while (true) {
                        if (i3 < length) {
                            Pattern p2 = patternArr[i3];
                            Matcher m2 = p2.matcher(line2);
                            if (m2.find()) {
                                DirectoryURLConnection.this.toHTML = true;
                                break;
                            }
                            i3++;
                        }
                    }
                    try {
                        super.unread(this.buffer, 0, l2);
                        break;
                    } catch (IOException e3) {
                    }
                }
            }
            if (DirectoryURLConnection.this.toHTML) {
                String parent = null;
                URL prevUrl = null;
                if (!DirectoryURLConnection.this.dirUrl.endsWith("/")) {
                    DirectoryURLConnection.this.dirUrl += "/";
                }
                try {
                    prevUrl = URLs.newURL(DirectoryURLConnection.this.dirUrl);
                } catch (Exception e4) {
                }
                String path = prevUrl.getPath();
                if (path != null && !path.isEmpty() && (index = path.lastIndexOf("/", path.length() - 2)) >= 0) {
                    int removed = (path.length() - index) - 1;
                    int index2 = DirectoryURLConnection.this.dirUrl.indexOf(path);
                    parent = DirectoryURLConnection.this.dirUrl.substring(0, (index2 + path.length()) - removed) + DirectoryURLConnection.this.dirUrl.substring(index2 + path.length());
                }
                this.out.print("<html><head><title>index of ");
                this.out.print(DirectoryURLConnection.this.dirUrl);
                this.out.print("</title>");
                this.out.print(DirectoryURLConnection.styleSheet);
                this.out.print("</head><body><h1>Index of ");
                this.out.print(DirectoryURLConnection.this.dirUrl);
                this.out.print("</h1><hr></hr>");
                this.out.print("<TABLE width=\"95%\" cellpadding=\"5\" cellspacing=\"5\">");
                this.out.print("<TR class=\"header\"><TD>File</TD><TD>Size</TD><TD>Last Modified</TD></TR>");
                if (parent != null) {
                    this.lineCount++;
                    this.out.print("<TR class=\"odd\"><TD colspan=3 class=\"file\"><a href=\"");
                    this.out.print(parent);
                    this.out.print("\">Up to parent directory</a></TD></TR>");
                }
                this.out.close();
                this.bytesIn = new ByteArrayInputStream(this.bytesOut.toByteArray());
                this.out = null;
                this.bytesOut = null;
            }
        }

        private void parseFile(String s2) {
            this.tmpString.append(s2);
            while (true) {
                int i2 = this.tmpString.indexOf("\n");
                if (i2 < 0) {
                    break;
                }
                String sb = this.tmpString.substring(0, i2);
                this.tmpString.delete(0, i2 + 1);
                String size = null;
                String date = null;
                boolean dir = false;
                boolean noaccess = false;
                URL furl = null;
                if (sb != null) {
                    this.lineCount++;
                    try {
                        furl = URLs.newURL(DirectoryURLConnection.this.dirUrl + URLEncoder.encode(sb, "UTF-8"));
                        URLConnection fconn = furl.openConnection();
                        fconn.connect();
                        date = fconn.getHeaderField("last-modified");
                        size = fconn.getHeaderField("content-length");
                        if (size == null) {
                            dir = true;
                        }
                        fconn.getInputStream().close();
                    } catch (IOException e2) {
                        noaccess = true;
                    }
                    if (this.bytesOut == null) {
                        this.bytesOut = new ByteArrayOutputStream();
                        this.out = new PrintStream(this.bytesOut);
                    }
                    this.out.print("<TR class=\"" + (this.lineCount % 2 == 0 ? "even" : "odd") + "\"><TD class=\"file\">");
                    if (noaccess) {
                        this.out.print(sb);
                    } else {
                        this.out.print("<a href=\"");
                        this.out.print(furl.toExternalForm());
                        this.out.print("\">");
                        this.out.print(sb);
                        this.out.print("</a>");
                    }
                    if (dir) {
                        this.out.print("</TD><TD class=\"dir\">&lt;Directory&gt;</TD>");
                    } else {
                        this.out.print("</TD><TD class=\"fsize\">" + (size == null ? " " : size) + "</TD>");
                    }
                    this.out.print("<TD class=\"date\">" + (date == null ? " " : date) + "</TD></TR>");
                }
            }
            if (this.bytesOut != null) {
                this.out.close();
                this.bytesIn = new ByteArrayInputStream(this.bytesOut.toByteArray());
                this.out = null;
                this.bytesOut = null;
            }
        }

        private void parseFTP(String s2) {
            this.tmpString.append(s2);
            while (true) {
                int i2 = this.tmpString.indexOf("\n");
                if (i2 < 0) {
                    break;
                }
                String sb = this.tmpString.substring(0, i2);
                this.tmpString.delete(0, i2 + 1);
                String filename = null;
                String link = null;
                String size = null;
                String date = null;
                boolean dir = false;
                for (int j2 = 0; j2 < DirectoryURLConnection.patterns.length; j2++) {
                    Matcher m2 = DirectoryURLConnection.patterns[j2].matcher(sb);
                    if (m2.find()) {
                        filename = m2.group(DirectoryURLConnection.patternGroups[j2][0]);
                        size = m2.group(DirectoryURLConnection.patternGroups[j2][1]);
                        date = m2.group(DirectoryURLConnection.patternGroups[j2][2]);
                        if (DirectoryURLConnection.patternGroups[j2][3] > 0) {
                            date = date + " " + m2.group(DirectoryURLConnection.patternGroups[j2][3]);
                        }
                        if (DirectoryURLConnection.patternGroups[j2][4] > 0) {
                            String perms = m2.group(DirectoryURLConnection.patternGroups[j2][4]);
                            dir = perms.startsWith(PdfOps.d_TOKEN);
                        }
                        if ("<DIR>".equals(size)) {
                            dir = true;
                            size = null;
                        }
                    }
                }
                if (filename != null) {
                    Matcher m3 = DirectoryURLConnection.linkp.matcher(filename);
                    if (m3.find()) {
                        filename = m3.group(1);
                        link = m3.group(2);
                    }
                    if (this.bytesOut == null) {
                        this.bytesOut = new ByteArrayOutputStream();
                        this.out = new PrintStream(this.bytesOut);
                    }
                    this.lineCount++;
                    this.out.print("<TR class=\"" + (this.lineCount % 2 == 0 ? "even" : "odd") + "\"><TD class=\"file\"><a href=\"");
                    try {
                        this.out.print(DirectoryURLConnection.this.dirUrl + URLEncoder.encode(filename, "UTF-8"));
                    } catch (UnsupportedEncodingException e2) {
                    }
                    if (dir) {
                        this.out.print("/");
                    }
                    this.out.print("\">");
                    this.out.print(filename);
                    this.out.print("</a>");
                    if (link != null) {
                        this.out.print(" &rarr; " + link + "</TD><TD class=\"link\">&lt;Link&gt;</TD>");
                    } else if (dir) {
                        this.out.print("</TD><TD class=\"dir\">&lt;Directory&gt;</TD>");
                    } else {
                        this.out.print("</TD><TD class=\"fsize\">" + size + "</TD>");
                    }
                    this.out.print("<TD class=\"date\">" + date + "</TD></TR>");
                }
            }
            if (this.bytesOut != null) {
                this.out.close();
                this.bytesIn = new ByteArrayInputStream(this.bytesOut.toByteArray());
                this.out = null;
                this.bytesOut = null;
            }
        }

        private void endOfList() {
            if (DirectoryURLConnection.this.ftp) {
                parseFTP("\n");
            } else {
                parseFile("\n");
            }
            if (this.bytesOut == null) {
                this.bytesOut = new ByteArrayOutputStream();
                this.out = new PrintStream(this.bytesOut);
            }
            this.out.print("</TABLE><br><hr></hr></body></html>");
            this.out.close();
            this.bytesIn = new ByteArrayInputStream(this.bytesOut.toByteArray());
            this.out = null;
            this.bytesOut = null;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read(byte[] buf) throws IOException {
            return read(buf, 0, buf.length);
        }

        @Override // java.io.PushbackInputStream, java.io.FilterInputStream, java.io.InputStream
        public int read(byte[] buf, int offset, int length) throws IOException {
            if (!DirectoryURLConnection.this.toHTML) {
                return super.read(buf, offset, length);
            }
            if (this.bytesIn != null) {
                int l2 = this.bytesIn.read(buf, offset, length);
                if (l2 == -1) {
                    this.bytesIn.close();
                    this.bytesIn = null;
                    if (this.endOfStream) {
                        return -1;
                    }
                } else {
                    return l2;
                }
            }
            if (!this.endOfStream) {
                int l3 = super.read(this.buffer, 0, this.buffer.length);
                if (l3 != -1) {
                    if (DirectoryURLConnection.this.ftp) {
                        parseFTP(new String(this.buffer, 0, l3));
                    } else {
                        parseFile(new String(this.buffer, 0, l3));
                    }
                    if (this.bytesIn != null) {
                        return read(buf, offset, length);
                    }
                    return 0;
                }
                this.endOfStream = true;
                endOfList();
                return read(buf, offset, length);
            }
            return 0;
        }
    }

    DirectoryURLConnection(URLConnection con, boolean notsure) {
        super(con.getURL());
        this.dirUrl = null;
        this.toHTML = true;
        this.ins = null;
        this.dirUrl = con.getURL().toExternalForm();
        this.inner = con;
        this.sure = !notsure;
        this.ftp = true;
    }

    DirectoryURLConnection(URLConnection con) {
        super(con.getURL());
        this.dirUrl = null;
        this.toHTML = true;
        this.ins = null;
        this.dirUrl = con.getURL().toExternalForm();
        this.ftp = false;
        this.sure = true;
        this.inner = con;
    }

    @Override // java.net.URLConnection
    public void connect() throws IOException {
        this.inner.connect();
    }

    @Override // java.net.URLConnection
    public InputStream getInputStream() throws IOException {
        if (this.ins == null) {
            if (this.ftp) {
                this.ins = new DirectoryInputStream(this.inner.getInputStream(), !this.sure);
            } else {
                this.ins = new DirectoryInputStream(this.inner.getInputStream(), false);
            }
        }
        return this.ins;
    }

    @Override // java.net.URLConnection
    public String getContentType() {
        try {
            if (!this.sure) {
                getInputStream();
            }
        } catch (IOException e2) {
        }
        if (this.toHTML) {
            return Clipboard.HTML_TYPE;
        }
        return this.inner.getContentType();
    }

    @Override // java.net.URLConnection
    public String getContentEncoding() {
        return this.inner.getContentEncoding();
    }

    @Override // java.net.URLConnection
    public int getContentLength() {
        return this.inner.getContentLength();
    }

    @Override // java.net.URLConnection
    public Map<String, List<String>> getHeaderFields() {
        return this.inner.getHeaderFields();
    }

    @Override // java.net.URLConnection
    public String getHeaderField(String key) {
        return this.inner.getHeaderField(key);
    }
}
