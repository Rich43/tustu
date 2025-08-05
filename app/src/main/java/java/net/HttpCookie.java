package java.net;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.TimeZone;
import sun.misc.JavaNetHttpCookieAccess;
import sun.misc.SharedSecrets;

/* loaded from: rt.jar:java/net/HttpCookie.class */
public final class HttpCookie implements Cloneable {
    private final String name;
    private String value;
    private String comment;
    private String commentURL;
    private boolean toDiscard;
    private String domain;
    private long maxAge;
    private String path;
    private String portlist;
    private boolean secure;
    private boolean httpOnly;
    private int version;
    private final String header;
    private final long whenCreated;
    private static final long MAX_AGE_UNSPECIFIED = -1;
    private static final String SET_COOKIE = "set-cookie:";
    private static final String SET_COOKIE2 = "set-cookie2:";
    private static final String tspecials = ",; ";
    static final TimeZone GMT;
    private static final String[] COOKIE_DATE_FORMATS = {"EEE',' dd-MMM-yyyy HH:mm:ss 'GMT'", "EEE',' dd MMM yyyy HH:mm:ss 'GMT'", "EEE MMM dd yyyy HH:mm:ss 'GMT'Z", "EEE',' dd-MMM-yy HH:mm:ss 'GMT'", "EEE',' dd MMM yy HH:mm:ss 'GMT'", "EEE MMM dd yy HH:mm:ss 'GMT'Z"};
    static final Map<String, CookieAttributeAssignor> assignors = new HashMap();

    /* loaded from: rt.jar:java/net/HttpCookie$CookieAttributeAssignor.class */
    interface CookieAttributeAssignor {
        void assign(HttpCookie httpCookie, String str, String str2);
    }

    static {
        assignors.put("comment", new CookieAttributeAssignor() { // from class: java.net.HttpCookie.1
            @Override // java.net.HttpCookie.CookieAttributeAssignor
            public void assign(HttpCookie httpCookie, String str, String str2) {
                if (httpCookie.getComment() == null) {
                    httpCookie.setComment(str2);
                }
            }
        });
        assignors.put("commenturl", new CookieAttributeAssignor() { // from class: java.net.HttpCookie.2
            @Override // java.net.HttpCookie.CookieAttributeAssignor
            public void assign(HttpCookie httpCookie, String str, String str2) {
                if (httpCookie.getCommentURL() == null) {
                    httpCookie.setCommentURL(str2);
                }
            }
        });
        assignors.put("discard", new CookieAttributeAssignor() { // from class: java.net.HttpCookie.3
            @Override // java.net.HttpCookie.CookieAttributeAssignor
            public void assign(HttpCookie httpCookie, String str, String str2) {
                httpCookie.setDiscard(true);
            }
        });
        assignors.put("domain", new CookieAttributeAssignor() { // from class: java.net.HttpCookie.4
            @Override // java.net.HttpCookie.CookieAttributeAssignor
            public void assign(HttpCookie httpCookie, String str, String str2) {
                if (httpCookie.getDomain() == null) {
                    httpCookie.setDomain(str2);
                }
            }
        });
        assignors.put("max-age", new CookieAttributeAssignor() { // from class: java.net.HttpCookie.5
            @Override // java.net.HttpCookie.CookieAttributeAssignor
            public void assign(HttpCookie httpCookie, String str, String str2) {
                try {
                    long j2 = Long.parseLong(str2);
                    if (httpCookie.getMaxAge() == -1) {
                        httpCookie.setMaxAge(j2);
                    }
                } catch (NumberFormatException e2) {
                    throw new IllegalArgumentException("Illegal cookie max-age attribute");
                }
            }
        });
        assignors.put("path", new CookieAttributeAssignor() { // from class: java.net.HttpCookie.6
            @Override // java.net.HttpCookie.CookieAttributeAssignor
            public void assign(HttpCookie httpCookie, String str, String str2) {
                if (httpCookie.getPath() == null) {
                    httpCookie.setPath(str2);
                }
            }
        });
        assignors.put(DeploymentDescriptorParser.ATTR_PORT, new CookieAttributeAssignor() { // from class: java.net.HttpCookie.7
            @Override // java.net.HttpCookie.CookieAttributeAssignor
            public void assign(HttpCookie httpCookie, String str, String str2) {
                if (httpCookie.getPortlist() == null) {
                    httpCookie.setPortlist(str2 == null ? "" : str2);
                }
            }
        });
        assignors.put("secure", new CookieAttributeAssignor() { // from class: java.net.HttpCookie.8
            @Override // java.net.HttpCookie.CookieAttributeAssignor
            public void assign(HttpCookie httpCookie, String str, String str2) {
                httpCookie.setSecure(true);
            }
        });
        assignors.put("httponly", new CookieAttributeAssignor() { // from class: java.net.HttpCookie.9
            @Override // java.net.HttpCookie.CookieAttributeAssignor
            public void assign(HttpCookie httpCookie, String str, String str2) {
                httpCookie.setHttpOnly(true);
            }
        });
        assignors.put("version", new CookieAttributeAssignor() { // from class: java.net.HttpCookie.10
            @Override // java.net.HttpCookie.CookieAttributeAssignor
            public void assign(HttpCookie httpCookie, String str, String str2) {
                try {
                    httpCookie.setVersion(Integer.parseInt(str2));
                } catch (NumberFormatException e2) {
                }
            }
        });
        assignors.put("expires", new CookieAttributeAssignor() { // from class: java.net.HttpCookie.11
            @Override // java.net.HttpCookie.CookieAttributeAssignor
            public void assign(HttpCookie httpCookie, String str, String str2) {
                if (httpCookie.getMaxAge() == -1) {
                    httpCookie.setMaxAge(httpCookie.expiryDate2DeltaSeconds(str2));
                }
            }
        });
        SharedSecrets.setJavaNetHttpCookieAccess(new JavaNetHttpCookieAccess() { // from class: java.net.HttpCookie.12
            @Override // sun.misc.JavaNetHttpCookieAccess
            public List<HttpCookie> parse(String str) {
                return HttpCookie.parse(str, true);
            }

            @Override // sun.misc.JavaNetHttpCookieAccess
            public String header(HttpCookie httpCookie) {
                return httpCookie.header;
            }
        });
        GMT = TimeZone.getTimeZone("GMT");
    }

    public HttpCookie(String str, String str2) {
        this(str, str2, null);
    }

    private HttpCookie(String str, String str2, String str3) {
        this.maxAge = -1L;
        this.version = 1;
        String strTrim = str.trim();
        if (strTrim.length() == 0 || !isToken(strTrim) || strTrim.charAt(0) == '$') {
            throw new IllegalArgumentException("Illegal cookie name");
        }
        this.name = strTrim;
        this.value = str2;
        this.toDiscard = false;
        this.secure = false;
        this.whenCreated = System.currentTimeMillis();
        this.portlist = null;
        this.header = str3;
    }

    public static List<HttpCookie> parse(String str) {
        return parse(str, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static List<HttpCookie> parse(String str, boolean z2) {
        int iGuessCookieVersion = guessCookieVersion(str);
        if (startsWithIgnoreCase(str, SET_COOKIE2)) {
            str = str.substring(SET_COOKIE2.length());
        } else if (startsWithIgnoreCase(str, SET_COOKIE)) {
            str = str.substring(SET_COOKIE.length());
        }
        ArrayList arrayList = new ArrayList();
        if (iGuessCookieVersion == 0) {
            HttpCookie internal = parseInternal(str, z2);
            internal.setVersion(0);
            arrayList.add(internal);
        } else {
            Iterator<String> it = splitMultiCookies(str).iterator();
            while (it.hasNext()) {
                HttpCookie internal2 = parseInternal(it.next(), z2);
                internal2.setVersion(1);
                arrayList.add(internal2);
            }
        }
        return arrayList;
    }

    public boolean hasExpired() {
        if (this.maxAge == 0) {
            return true;
        }
        if (this.maxAge != -1 && (System.currentTimeMillis() - this.whenCreated) / 1000 > this.maxAge) {
            return true;
        }
        return false;
    }

    public void setComment(String str) {
        this.comment = str;
    }

    public String getComment() {
        return this.comment;
    }

    public void setCommentURL(String str) {
        this.commentURL = str;
    }

    public String getCommentURL() {
        return this.commentURL;
    }

    public void setDiscard(boolean z2) {
        this.toDiscard = z2;
    }

    public boolean getDiscard() {
        return this.toDiscard;
    }

    public void setPortlist(String str) {
        this.portlist = str;
    }

    public String getPortlist() {
        return this.portlist;
    }

    public void setDomain(String str) {
        if (str != null) {
            this.domain = str.toLowerCase();
        } else {
            this.domain = str;
        }
    }

    public String getDomain() {
        return this.domain;
    }

    public void setMaxAge(long j2) {
        this.maxAge = j2;
    }

    public long getMaxAge() {
        return this.maxAge;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public String getPath() {
        return this.path;
    }

    public void setSecure(boolean z2) {
        this.secure = z2;
    }

    public boolean getSecure() {
        return this.secure;
    }

    public String getName() {
        return this.name;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public String getValue() {
        return this.value;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int i2) {
        if (i2 != 0 && i2 != 1) {
            throw new IllegalArgumentException("cookie version should be 0 or 1");
        }
        this.version = i2;
    }

    public boolean isHttpOnly() {
        return this.httpOnly;
    }

    public void setHttpOnly(boolean z2) {
        this.httpOnly = z2;
    }

    public static boolean domainMatches(String str, String str2) {
        if (str == null || str2 == null) {
            return false;
        }
        boolean zEqualsIgnoreCase = ".local".equalsIgnoreCase(str);
        int iIndexOf = str.indexOf(46);
        if (iIndexOf == 0) {
            iIndexOf = str.indexOf(46, 1);
        }
        if (!zEqualsIgnoreCase && (iIndexOf == -1 || iIndexOf == str.length() - 1)) {
            return false;
        }
        if (str2.indexOf(46) == -1 && (zEqualsIgnoreCase || str.equalsIgnoreCase(str2 + ".local"))) {
            return true;
        }
        int length = str2.length() - str.length();
        if (length == 0) {
            return str2.equalsIgnoreCase(str);
        }
        if (length > 0) {
            return str2.substring(0, length).indexOf(46) == -1 && str2.substring(length).equalsIgnoreCase(str);
        }
        return length == -1 && str.charAt(0) == '.' && str2.equalsIgnoreCase(str.substring(1));
    }

    public String toString() {
        if (getVersion() > 0) {
            return toRFC2965HeaderString();
        }
        return toNetscapeHeaderString();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof HttpCookie)) {
            return false;
        }
        HttpCookie httpCookie = (HttpCookie) obj;
        return equalsIgnoreCase(getName(), httpCookie.getName()) && equalsIgnoreCase(getDomain(), httpCookie.getDomain()) && Objects.equals(getPath(), httpCookie.getPath());
    }

    public int hashCode() {
        return this.name.toLowerCase().hashCode() + (this.domain != null ? this.domain.toLowerCase().hashCode() : 0) + (this.path != null ? this.path.hashCode() : 0);
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new RuntimeException(e2.getMessage());
        }
    }

    private static boolean isToken(String str) {
        int length = str.length();
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = str.charAt(i2);
            if (cCharAt < ' ' || cCharAt >= 127 || tspecials.indexOf(cCharAt) != -1) {
                return false;
            }
        }
        return true;
    }

    private static HttpCookie parseInternal(String str, boolean z2) {
        HttpCookie httpCookie;
        String strTrim;
        String strTrim2;
        StringTokenizer stringTokenizer = new StringTokenizer(str, ";");
        try {
            String strNextToken = stringTokenizer.nextToken();
            int iIndexOf = strNextToken.indexOf(61);
            if (iIndexOf != -1) {
                String strTrim3 = strNextToken.substring(0, iIndexOf).trim();
                String strTrim4 = strNextToken.substring(iIndexOf + 1).trim();
                if (z2) {
                    httpCookie = new HttpCookie(strTrim3, stripOffSurroundingQuote(strTrim4), str);
                } else {
                    httpCookie = new HttpCookie(strTrim3, stripOffSurroundingQuote(strTrim4));
                }
                while (stringTokenizer.hasMoreTokens()) {
                    String strNextToken2 = stringTokenizer.nextToken();
                    int iIndexOf2 = strNextToken2.indexOf(61);
                    if (iIndexOf2 != -1) {
                        strTrim = strNextToken2.substring(0, iIndexOf2).trim();
                        strTrim2 = strNextToken2.substring(iIndexOf2 + 1).trim();
                    } else {
                        strTrim = strNextToken2.trim();
                        strTrim2 = null;
                    }
                    assignAttribute(httpCookie, strTrim, strTrim2);
                }
                return httpCookie;
            }
            throw new IllegalArgumentException("Invalid cookie name-value pair");
        } catch (NoSuchElementException e2) {
            throw new IllegalArgumentException("Empty cookie header string");
        }
    }

    private static void assignAttribute(HttpCookie httpCookie, String str, String str2) {
        String strStripOffSurroundingQuote = stripOffSurroundingQuote(str2);
        CookieAttributeAssignor cookieAttributeAssignor = assignors.get(str.toLowerCase());
        if (cookieAttributeAssignor != null) {
            cookieAttributeAssignor.assign(httpCookie, str, strStripOffSurroundingQuote);
        }
    }

    private String header() {
        return this.header;
    }

    private String toNetscapeHeaderString() {
        return getName() + "=" + getValue();
    }

    private String toRFC2965HeaderString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append("=\"").append(getValue()).append('\"');
        if (getPath() != null) {
            sb.append(";$Path=\"").append(getPath()).append('\"');
        }
        if (getDomain() != null) {
            sb.append(";$Domain=\"").append(getDomain()).append('\"');
        }
        if (getPortlist() != null) {
            sb.append(";$Port=\"").append(getPortlist()).append('\"');
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long expiryDate2DeltaSeconds(String str) {
        int i2;
        GregorianCalendar gregorianCalendar = new GregorianCalendar(GMT);
        for (int i3 = 0; i3 < COOKIE_DATE_FORMATS.length; i3++) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(COOKIE_DATE_FORMATS[i3], Locale.US);
            gregorianCalendar.set(1970, 0, 1, 0, 0, 0);
            simpleDateFormat.setTimeZone(GMT);
            simpleDateFormat.setLenient(false);
            simpleDateFormat.set2DigitYearStart(gregorianCalendar.getTime());
            try {
                gregorianCalendar.setTime(simpleDateFormat.parse(str));
                if (!COOKIE_DATE_FORMATS[i3].contains("yyyy")) {
                    int i4 = gregorianCalendar.get(1) % 100;
                    if (i4 < 70) {
                        i2 = i4 + 2000;
                    } else {
                        i2 = i4 + 1900;
                    }
                    gregorianCalendar.set(1, i2);
                }
                return (gregorianCalendar.getTimeInMillis() - this.whenCreated) / 1000;
            } catch (Exception e2) {
            }
        }
        return 0L;
    }

    private static int guessCookieVersion(String str) {
        int i2 = 0;
        String lowerCase = str.toLowerCase();
        if (lowerCase.indexOf("expires=") != -1) {
            i2 = 0;
        } else if (lowerCase.indexOf("version=") != -1 || lowerCase.indexOf("max-age") != -1 || startsWithIgnoreCase(lowerCase, SET_COOKIE2)) {
            i2 = 1;
        }
        return i2;
    }

    private static String stripOffSurroundingQuote(String str) {
        if (str != null && str.length() > 2 && str.charAt(0) == '\"' && str.charAt(str.length() - 1) == '\"') {
            return str.substring(1, str.length() - 1);
        }
        if (str != null && str.length() > 2 && str.charAt(0) == '\'' && str.charAt(str.length() - 1) == '\'') {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }

    private static boolean equalsIgnoreCase(String str, String str2) {
        if (str == str2) {
            return true;
        }
        if (str != null && str2 != null) {
            return str.equalsIgnoreCase(str2);
        }
        return false;
    }

    private static boolean startsWithIgnoreCase(String str, String str2) {
        if (str != null && str2 != null && str.length() >= str2.length() && str2.equalsIgnoreCase(str.substring(0, str2.length()))) {
            return true;
        }
        return false;
    }

    private static List<String> splitMultiCookies(String str) {
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < str.length(); i4++) {
            char cCharAt = str.charAt(i4);
            if (cCharAt == '\"') {
                i2++;
            }
            if (cCharAt == ',' && i2 % 2 == 0) {
                arrayList.add(str.substring(i3, i4));
                i3 = i4 + 1;
            }
        }
        arrayList.add(str.substring(i3));
        return arrayList;
    }
}
