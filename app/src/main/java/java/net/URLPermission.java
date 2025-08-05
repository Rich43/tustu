package java.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:java/net/URLPermission.class */
public final class URLPermission extends Permission {
    private static final long serialVersionUID = -2702463814894478682L;
    private transient String scheme;
    private transient String ssp;
    private transient String path;
    private transient List<String> methods;
    private transient List<String> requestHeaders;
    private transient Authority authority;
    private String actions;

    public URLPermission(String str, String str2) {
        super(str);
        init(str2);
    }

    private void init(String str) {
        String strSubstring;
        String strSubstring2;
        parseURI(getName());
        int iIndexOf = str.indexOf(58);
        if (str.lastIndexOf(58) != iIndexOf) {
            throw new IllegalArgumentException("Invalid actions string: \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
        if (iIndexOf == -1) {
            strSubstring = str;
            strSubstring2 = "";
        } else {
            strSubstring = str.substring(0, iIndexOf);
            strSubstring2 = str.substring(iIndexOf + 1);
        }
        List<String> listNormalizeMethods = normalizeMethods(strSubstring);
        Collections.sort(listNormalizeMethods);
        this.methods = Collections.unmodifiableList(listNormalizeMethods);
        List<String> listNormalizeHeaders = normalizeHeaders(strSubstring2);
        Collections.sort(listNormalizeHeaders);
        this.requestHeaders = Collections.unmodifiableList(listNormalizeHeaders);
        this.actions = actions();
    }

    public URLPermission(String str) {
        this(str, "*:*");
    }

    @Override // java.security.Permission
    public String getActions() {
        return this.actions;
    }

    @Override // java.security.Permission
    public boolean implies(Permission permission) {
        if (!(permission instanceof URLPermission)) {
            return false;
        }
        URLPermission uRLPermission = (URLPermission) permission;
        if (!this.methods.get(0).equals("*") && Collections.indexOfSubList(this.methods, uRLPermission.methods) == -1) {
            return false;
        }
        if (this.requestHeaders.isEmpty() && !uRLPermission.requestHeaders.isEmpty()) {
            return false;
        }
        if ((!this.requestHeaders.isEmpty() && !this.requestHeaders.get(0).equals("*") && Collections.indexOfSubList(this.requestHeaders, uRLPermission.requestHeaders) == -1) || !this.scheme.equals(uRLPermission.scheme)) {
            return false;
        }
        if (this.ssp.equals("*")) {
            return true;
        }
        if (!this.authority.implies(uRLPermission.authority)) {
            return false;
        }
        if (this.path == null) {
            return uRLPermission.path == null;
        }
        if (uRLPermission.path == null) {
            return false;
        }
        if (this.path.endsWith("/-")) {
            return uRLPermission.path.startsWith(this.path.substring(0, this.path.length() - 1));
        }
        if (this.path.endsWith("/*")) {
            String strSubstring = this.path.substring(0, this.path.length() - 1);
            if (!uRLPermission.path.startsWith(strSubstring)) {
                return false;
            }
            String strSubstring2 = uRLPermission.path.substring(strSubstring.length());
            if (strSubstring2.indexOf(47) != -1 || strSubstring2.equals(LanguageTag.SEP)) {
                return false;
            }
            return true;
        }
        return this.path.equals(uRLPermission.path);
    }

    @Override // java.security.Permission
    public boolean equals(Object obj) {
        if (!(obj instanceof URLPermission)) {
            return false;
        }
        URLPermission uRLPermission = (URLPermission) obj;
        if (!this.scheme.equals(uRLPermission.scheme) || !getActions().equals(uRLPermission.getActions()) || !this.authority.equals(uRLPermission.authority)) {
            return false;
        }
        if (this.path != null) {
            return this.path.equals(uRLPermission.path);
        }
        return uRLPermission.path == null;
    }

    @Override // java.security.Permission
    public int hashCode() {
        return getActions().hashCode() + this.scheme.hashCode() + this.authority.hashCode() + (this.path == null ? 0 : this.path.hashCode());
    }

    private List<String> normalizeMethods(String str) {
        ArrayList arrayList = new ArrayList();
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < str.length(); i2++) {
            char cCharAt = str.charAt(i2);
            if (cCharAt == ',') {
                String string = sb.toString();
                if (string.length() > 0) {
                    arrayList.add(string);
                }
                sb = new StringBuilder();
            } else {
                if (cCharAt == ' ' || cCharAt == '\t') {
                    throw new IllegalArgumentException("White space not allowed in methods: \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN);
                }
                if (cCharAt >= 'a' && cCharAt <= 'z') {
                    cCharAt = (char) (cCharAt - ' ');
                }
                sb.append(cCharAt);
            }
        }
        String string2 = sb.toString();
        if (string2.length() > 0) {
            arrayList.add(string2);
        }
        return arrayList;
    }

    private List<String> normalizeHeaders(String str) {
        ArrayList arrayList = new ArrayList();
        StringBuilder sb = new StringBuilder();
        boolean z2 = true;
        for (int i2 = 0; i2 < str.length(); i2++) {
            char cCharAt = str.charAt(i2);
            if (cCharAt >= 'a' && cCharAt <= 'z') {
                if (z2) {
                    cCharAt = (char) (cCharAt - ' ');
                    z2 = false;
                }
                sb.append(cCharAt);
            } else {
                if (cCharAt == ' ' || cCharAt == '\t') {
                    throw new IllegalArgumentException("White space not allowed in headers: \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN);
                }
                if (cCharAt == '-') {
                    z2 = true;
                    sb.append(cCharAt);
                } else if (cCharAt == ',') {
                    String string = sb.toString();
                    if (string.length() > 0) {
                        arrayList.add(string);
                    }
                    sb = new StringBuilder();
                    z2 = true;
                } else {
                    z2 = false;
                    sb.append(cCharAt);
                }
            }
        }
        String string2 = sb.toString();
        if (string2.length() > 0) {
            arrayList.add(string2);
        }
        return arrayList;
    }

    private void parseURI(String str) {
        String strSubstring;
        int length = str.length();
        int iIndexOf = str.indexOf(58);
        if (iIndexOf == -1 || iIndexOf + 1 == length) {
            throw new IllegalArgumentException("Invalid URL string: \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
        this.scheme = str.substring(0, iIndexOf).toLowerCase();
        this.ssp = str.substring(iIndexOf + 1);
        if (!this.ssp.startsWith("//")) {
            if (!this.ssp.equals("*")) {
                throw new IllegalArgumentException("Invalid URL string: \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN);
            }
            this.authority = new Authority(this.scheme, "*");
            return;
        }
        String strSubstring2 = this.ssp.substring(2);
        int iIndexOf2 = strSubstring2.indexOf(47);
        if (iIndexOf2 == -1) {
            this.path = "";
            strSubstring = strSubstring2;
        } else {
            strSubstring = strSubstring2.substring(0, iIndexOf2);
            this.path = strSubstring2.substring(iIndexOf2);
        }
        this.authority = new Authority(this.scheme, strSubstring.toLowerCase());
    }

    private String actions() {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = this.methods.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
        }
        sb.append(CallSiteDescriptor.TOKEN_DELIMITER);
        Iterator<String> it2 = this.requestHeaders.iterator();
        while (it2.hasNext()) {
            sb.append(it2.next());
        }
        return sb.toString();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        init((String) objectInputStream.readFields().get("actions", (Object) null));
    }

    /* loaded from: rt.jar:java/net/URLPermission$Authority.class */
    static class Authority {

        /* renamed from: p, reason: collision with root package name */
        HostPortrange f12449p;

        Authority(String str, String str2) {
            int iIndexOf = str2.indexOf(64);
            if (iIndexOf == -1) {
                this.f12449p = new HostPortrange(str, str2);
            } else {
                this.f12449p = new HostPortrange(str, str2.substring(iIndexOf + 1));
            }
        }

        boolean implies(Authority authority) {
            return impliesHostrange(authority) && impliesPortrange(authority);
        }

        private boolean impliesHostrange(Authority authority) {
            String strHostname = this.f12449p.hostname();
            String strHostname2 = authority.f12449p.hostname();
            if (this.f12449p.wildcard() && strHostname.equals("")) {
                return true;
            }
            if (authority.f12449p.wildcard() && strHostname2.equals("")) {
                return false;
            }
            if (strHostname.equals(strHostname2)) {
                return true;
            }
            if (this.f12449p.wildcard()) {
                return strHostname2.endsWith(strHostname);
            }
            return false;
        }

        private boolean impliesPortrange(Authority authority) {
            int[] iArrPortrange = this.f12449p.portrange();
            int[] iArrPortrange2 = authority.f12449p.portrange();
            if (iArrPortrange[0] == -1) {
                return true;
            }
            return iArrPortrange[0] <= iArrPortrange2[0] && iArrPortrange[1] >= iArrPortrange2[1];
        }

        boolean equals(Authority authority) {
            return this.f12449p.equals(authority.f12449p);
        }

        public int hashCode() {
            return this.f12449p.hashCode();
        }
    }
}
