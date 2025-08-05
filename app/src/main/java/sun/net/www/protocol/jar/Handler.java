package sun.net.www.protocol.jar;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import sun.net.www.ParseUtil;

/* loaded from: rt.jar:sun/net/www/protocol/jar/Handler.class */
public class Handler extends URLStreamHandler {
    private static final String separator = "!/";

    @Override // java.net.URLStreamHandler
    protected URLConnection openConnection(URL url) throws IOException {
        return new JarURLConnection(url, this);
    }

    private static int indexOfBangSlash(String str) {
        int length = str.length();
        while (true) {
            int iLastIndexOf = str.lastIndexOf(33, length);
            if (iLastIndexOf != -1) {
                if (iLastIndexOf != str.length() - 1 && str.charAt(iLastIndexOf + 1) == '/') {
                    return iLastIndexOf + 1;
                }
                length = iLastIndexOf - 1;
            } else {
                return -1;
            }
        }
    }

    @Override // java.net.URLStreamHandler
    protected boolean sameFile(URL url, URL url2) {
        if (!url.getProtocol().equals("jar") || !url2.getProtocol().equals("jar")) {
            return false;
        }
        String file = url.getFile();
        String file2 = url2.getFile();
        int iIndexOf = file.indexOf(separator);
        int iIndexOf2 = file2.indexOf(separator);
        if (iIndexOf == -1 || iIndexOf2 == -1) {
            return super.sameFile(url, url2);
        }
        if (!file.substring(iIndexOf + 2).equals(file2.substring(iIndexOf2 + 2))) {
            return false;
        }
        try {
            if (!super.sameFile(new URL(file.substring(0, iIndexOf)), new URL(file2.substring(0, iIndexOf2)))) {
                return false;
            }
            return true;
        } catch (MalformedURLException e2) {
            return super.sameFile(url, url2);
        }
    }

    @Override // java.net.URLStreamHandler
    protected int hashCode(URL url) {
        int iHashCode = 0;
        String protocol = url.getProtocol();
        if (protocol != null) {
            iHashCode = 0 + protocol.hashCode();
        }
        String file = url.getFile();
        int iIndexOf = file.indexOf(separator);
        if (iIndexOf == -1) {
            return iHashCode + file.hashCode();
        }
        String strSubstring = file.substring(0, iIndexOf);
        try {
            iHashCode += new URL(strSubstring).hashCode();
        } catch (MalformedURLException e2) {
            iHashCode += strSubstring.hashCode();
        }
        return iHashCode + file.substring(iIndexOf + 2).hashCode();
    }

    public String checkNestedProtocol(String str) {
        if (str.regionMatches(true, 0, "jar:", 0, 4)) {
            return "Nested JAR URLs are not supported";
        }
        return null;
    }

    @Override // java.net.URLStreamHandler
    protected void parseURL(URL url, String str, int i2, int i3) {
        String absoluteSpec = null;
        String strSubstring = null;
        int iIndexOf = str.indexOf(35, i3);
        boolean z2 = iIndexOf == i2;
        if (iIndexOf > -1) {
            strSubstring = str.substring(iIndexOf + 1, str.length());
            if (z2) {
                absoluteSpec = url.getFile();
            }
        }
        boolean zEqualsIgnoreCase = false;
        if (str.length() >= 4) {
            zEqualsIgnoreCase = str.substring(0, 4).equalsIgnoreCase("jar:");
        }
        String strSubstring2 = str.substring(i2, i3);
        String strCheckNestedProtocol = checkNestedProtocol(strSubstring2);
        if (strCheckNestedProtocol != null) {
            throw new NullPointerException(strCheckNestedProtocol);
        }
        if (zEqualsIgnoreCase) {
            absoluteSpec = parseAbsoluteSpec(strSubstring2);
        } else if (!z2) {
            String contextSpec = parseContextSpec(url, strSubstring2);
            int iIndexOfBangSlash = indexOfBangSlash(contextSpec);
            absoluteSpec = contextSpec.substring(0, iIndexOfBangSlash) + new ParseUtil().canonizeString(contextSpec.substring(iIndexOfBangSlash));
        }
        setURL(url, "jar", "", -1, absoluteSpec, strSubstring);
    }

    private String parseAbsoluteSpec(String str) {
        int iIndexOfBangSlash = indexOfBangSlash(str);
        if (iIndexOfBangSlash == -1) {
            throw new NullPointerException("no !/ in spec");
        }
        try {
            new URL(str.substring(0, iIndexOfBangSlash - 1));
            return str;
        } catch (MalformedURLException e2) {
            throw new NullPointerException("invalid url: " + str + " (" + ((Object) e2) + ")");
        }
    }

    private String parseContextSpec(URL url, String str) {
        String file = url.getFile();
        if (str.startsWith("/")) {
            int iIndexOfBangSlash = indexOfBangSlash(file);
            if (iIndexOfBangSlash == -1) {
                throw new NullPointerException("malformed context url:" + ((Object) url) + ": no !/");
            }
            file = file.substring(0, iIndexOfBangSlash);
        }
        if (!file.endsWith("/") && !str.startsWith("/")) {
            int iLastIndexOf = file.lastIndexOf(47);
            if (iLastIndexOf == -1) {
                throw new NullPointerException("malformed context url:" + ((Object) url));
            }
            file = file.substring(0, iLastIndexOf + 1);
        }
        return file + str;
    }
}
