package com.sun.corba.se.spi.orb;

/* loaded from: rt.jar:com/sun/corba/se/spi/orb/StringPair.class */
public class StringPair {
    private String first;
    private String second;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StringPair)) {
            return false;
        }
        StringPair stringPair = (StringPair) obj;
        return this.first.equals(stringPair.first) && this.second.equals(stringPair.second);
    }

    public int hashCode() {
        return this.first.hashCode() ^ this.second.hashCode();
    }

    public StringPair(String str, String str2) {
        this.first = str;
        this.second = str2;
    }

    public String getFirst() {
        return this.first;
    }

    public String getSecond() {
        return this.second;
    }
}
