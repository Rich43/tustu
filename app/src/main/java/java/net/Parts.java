package java.net;

/* compiled from: URL.java */
/* loaded from: rt.jar:java/net/Parts.class */
class Parts {
    String path;
    String query;
    String ref;

    Parts(String str) {
        int iIndexOf = str.indexOf(35);
        this.ref = iIndexOf < 0 ? null : str.substring(iIndexOf + 1);
        String strSubstring = iIndexOf < 0 ? str : str.substring(0, iIndexOf);
        int iLastIndexOf = strSubstring.lastIndexOf(63);
        if (iLastIndexOf != -1) {
            this.query = strSubstring.substring(iLastIndexOf + 1);
            this.path = strSubstring.substring(0, iLastIndexOf);
        } else {
            this.path = strSubstring;
        }
    }

    String getPath() {
        return this.path;
    }

    String getQuery() {
        return this.query;
    }

    String getRef() {
        return this.ref;
    }
}
