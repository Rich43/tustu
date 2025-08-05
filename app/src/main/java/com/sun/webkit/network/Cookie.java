package com.sun.webkit.network;

import java.net.URI;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: jfxrt.jar:com/sun/webkit/network/Cookie.class */
final class Cookie {
    private static final Logger logger = Logger.getLogger(Cookie.class.getName());
    private static final Pattern IP_ADDRESS_PATTERN = Pattern.compile("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})");
    private final String name;
    private final String value;
    private final long expiryTime;
    private String domain;
    private String path;
    private ExtendedTime creationTime;
    private long lastAccessTime;
    private final boolean persistent;
    private boolean hostOnly;
    private final boolean secureOnly;
    private final boolean httpOnly;

    private Cookie(String name, String value, long expiryTime, String domain, String path, ExtendedTime creationTime, long lastAccessTime, boolean persistent, boolean hostOnly, boolean secureOnly, boolean httpOnly) {
        this.name = name;
        this.value = value;
        this.expiryTime = expiryTime;
        this.domain = domain;
        this.path = path;
        this.creationTime = creationTime;
        this.lastAccessTime = lastAccessTime;
        this.persistent = persistent;
        this.hostOnly = hostOnly;
        this.secureOnly = secureOnly;
        this.httpOnly = httpOnly;
    }

    static Cookie parse(String setCookieString, ExtendedTime currentTime) {
        boolean persistent;
        long expiryTime;
        logger.log(Level.FINEST, "setCookieString: [{0}]", setCookieString);
        String[] items = setCookieString.split(";", -1);
        String[] nameValuePair = items[0].split("=", 2);
        if (nameValuePair.length != 2) {
            logger.log(Level.FINEST, "Name-value pair string lacks '=', ignoring cookie");
            return null;
        }
        String name = nameValuePair[0].trim();
        String value = nameValuePair[1].trim();
        if (name.length() == 0) {
            logger.log(Level.FINEST, "Name string is empty, ignoring cookie");
            return null;
        }
        Long expires = null;
        Long maxAge = null;
        String domain = null;
        String path = null;
        boolean secure = false;
        boolean httpOnly = false;
        for (int i2 = 1; i2 < items.length; i2++) {
            String[] terms = items[i2].split("=", 2);
            String attrName = terms[0].trim();
            String attrValue = (terms.length > 1 ? terms[1] : "").trim();
            try {
                if ("Expires".equalsIgnoreCase(attrName)) {
                    expires = Long.valueOf(parseExpires(attrValue));
                } else if ("Max-Age".equalsIgnoreCase(attrName)) {
                    maxAge = Long.valueOf(parseMaxAge(attrValue, currentTime.baseTime()));
                } else if ("Domain".equalsIgnoreCase(attrName)) {
                    domain = parseDomain(attrValue);
                } else if ("Path".equalsIgnoreCase(attrName)) {
                    path = parsePath(attrValue);
                } else if ("Secure".equalsIgnoreCase(attrName)) {
                    secure = true;
                } else if ("HttpOnly".equalsIgnoreCase(attrName)) {
                    httpOnly = true;
                } else {
                    logger.log(Level.FINEST, "Unknown attribute: [{0}], ignoring", attrName);
                }
            } catch (ParseException ex) {
                logger.log(Level.FINEST, "{0}, ignoring", ex.getMessage());
            }
        }
        if (maxAge != null) {
            persistent = true;
            expiryTime = maxAge.longValue();
        } else if (expires != null) {
            persistent = true;
            expiryTime = expires.longValue();
        } else {
            persistent = false;
            expiryTime = Long.MAX_VALUE;
        }
        if (domain == null) {
            domain = "";
        }
        Cookie result = new Cookie(name, value, expiryTime, domain, path, currentTime, currentTime.baseTime(), persistent, false, secure, httpOnly);
        logger.log(Level.FINEST, "result: {0}", result);
        return result;
    }

    private static long parseExpires(String attributeValue) throws ParseException {
        try {
            return Math.max(DateParser.parse(attributeValue), 0L);
        } catch (ParseException e2) {
            throw new ParseException("Error parsing Expires attribute", 0);
        }
    }

    private static long parseMaxAge(String attributeValue, long currentTime) throws ParseException {
        try {
            long maxAge = Long.parseLong(attributeValue);
            if (maxAge <= 0) {
                return 0L;
            }
            return maxAge > (Long.MAX_VALUE - currentTime) / 1000 ? Long.MAX_VALUE : currentTime + (maxAge * 1000);
        } catch (NumberFormatException e2) {
            throw new ParseException("Error parsing Max-Age attribute", 0);
        }
    }

    private static String parseDomain(String attributeValue) throws ParseException {
        if (attributeValue.length() == 0) {
            throw new ParseException("Domain attribute is empty", 0);
        }
        if (attributeValue.startsWith(".")) {
            attributeValue = attributeValue.substring(1);
        }
        return attributeValue.toLowerCase();
    }

    private static String parsePath(String attributeValue) {
        if (attributeValue.startsWith("/")) {
            return attributeValue;
        }
        return null;
    }

    String getName() {
        return this.name;
    }

    String getValue() {
        return this.value;
    }

    long getExpiryTime() {
        return this.expiryTime;
    }

    String getDomain() {
        return this.domain;
    }

    void setDomain(String domain) {
        this.domain = domain;
    }

    String getPath() {
        return this.path;
    }

    void setPath(String path) {
        this.path = path;
    }

    ExtendedTime getCreationTime() {
        return this.creationTime;
    }

    void setCreationTime(ExtendedTime creationTime) {
        this.creationTime = creationTime;
    }

    long getLastAccessTime() {
        return this.lastAccessTime;
    }

    void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    boolean getPersistent() {
        return this.persistent;
    }

    boolean getHostOnly() {
        return this.hostOnly;
    }

    void setHostOnly(boolean hostOnly) {
        this.hostOnly = hostOnly;
    }

    boolean getSecureOnly() {
        return this.secureOnly;
    }

    boolean getHttpOnly() {
        return this.httpOnly;
    }

    boolean hasExpired() {
        return System.currentTimeMillis() > this.expiryTime;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Cookie) {
            Cookie cookie = (Cookie) obj;
            return equal(this.name, cookie.name) && equal(this.domain, cookie.domain) && equal(this.path, cookie.path);
        }
        return false;
    }

    private static boolean equal(Object obj1, Object obj2) {
        return (obj1 == null && obj2 == null) || (obj1 != null && obj1.equals(obj2));
    }

    public int hashCode() {
        int hashCode = (53 * 7) + hashCode(this.name);
        return (53 * ((53 * hashCode) + hashCode(this.domain))) + hashCode(this.path);
    }

    private static int hashCode(Object obj) {
        if (obj != null) {
            return obj.hashCode();
        }
        return 0;
    }

    public String toString() {
        return "[name=" + this.name + ", value=" + this.value + ", expiryTime=" + this.expiryTime + ", domain=" + this.domain + ", path=" + this.path + ", creationTime=" + ((Object) this.creationTime) + ", lastAccessTime=" + this.lastAccessTime + ", persistent=" + this.persistent + ", hostOnly=" + this.hostOnly + ", secureOnly=" + this.secureOnly + ", httpOnly=" + this.httpOnly + "]";
    }

    static boolean domainMatches(String domain, String cookieDomain) {
        return domain.endsWith(cookieDomain) && (domain.length() == cookieDomain.length() || (domain.charAt((domain.length() - cookieDomain.length()) - 1) == '.' && !isIpAddress(domain)));
    }

    private static boolean isIpAddress(String hostname) {
        Matcher matcher = IP_ADDRESS_PATTERN.matcher(hostname);
        if (!matcher.matches()) {
            return false;
        }
        for (int i2 = 1; i2 <= matcher.groupCount(); i2++) {
            if (Integer.parseInt(matcher.group(i2)) > 255) {
                return false;
            }
        }
        return true;
    }

    static String defaultPath(URI uri) {
        String path = uri.getPath();
        if (path == null || !path.startsWith("/")) {
            return "/";
        }
        String path2 = path.substring(0, path.lastIndexOf("/"));
        if (path2.length() == 0) {
            return "/";
        }
        return path2;
    }

    static boolean pathMatches(String path, String cookiePath) {
        return path != null && path.startsWith(cookiePath) && (path.length() == cookiePath.length() || cookiePath.endsWith("/") || path.charAt(cookiePath.length()) == '/');
    }
}
