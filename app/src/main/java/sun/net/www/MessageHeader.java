package sun.net.www;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringJoiner;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.apache.commons.math3.geometry.VectorFormat;
import sun.security.krb5.internal.PAForUserEnc;

/* loaded from: rt.jar:sun/net/www/MessageHeader.class */
public class MessageHeader {
    private String[] keys;
    private String[] values;
    private int nkeys;

    public MessageHeader() {
        grow();
    }

    public MessageHeader(InputStream inputStream) throws IOException {
        parseHeader(inputStream);
    }

    public synchronized String getHeaderNamesInList() {
        StringJoiner stringJoiner = new StringJoiner(",");
        for (int i2 = 0; i2 < this.nkeys; i2++) {
            stringJoiner.add(this.keys[i2]);
        }
        return stringJoiner.toString();
    }

    public synchronized void reset() {
        this.keys = null;
        this.values = null;
        this.nkeys = 0;
        grow();
    }

    public synchronized String findValue(String str) {
        if (str == null) {
            int i2 = this.nkeys;
            do {
                i2--;
                if (i2 < 0) {
                    return null;
                }
            } while (this.keys[i2] != null);
            return this.values[i2];
        }
        int i3 = this.nkeys;
        do {
            i3--;
            if (i3 < 0) {
                return null;
            }
        } while (!str.equalsIgnoreCase(this.keys[i3]));
        return this.values[i3];
    }

    public synchronized int getKey(String str) {
        int i2 = this.nkeys;
        while (true) {
            i2--;
            if (i2 >= 0) {
                if (this.keys[i2] == str || (str != null && str.equalsIgnoreCase(this.keys[i2]))) {
                    break;
                }
            } else {
                return -1;
            }
        }
        return i2;
    }

    public synchronized String getKey(int i2) {
        if (i2 < 0 || i2 >= this.nkeys) {
            return null;
        }
        return this.keys[i2];
    }

    public synchronized String getValue(int i2) {
        if (i2 < 0 || i2 >= this.nkeys) {
            return null;
        }
        return this.values[i2];
    }

    public synchronized String findNextValue(String str, String str2) {
        boolean z2 = false;
        if (str == null) {
            int i2 = this.nkeys;
            while (true) {
                i2--;
                if (i2 >= 0) {
                    if (this.keys[i2] == null) {
                        if (z2) {
                            return this.values[i2];
                        }
                        if (this.values[i2] == str2) {
                            z2 = true;
                        }
                    }
                } else {
                    return null;
                }
            }
        } else {
            int i3 = this.nkeys;
            while (true) {
                i3--;
                if (i3 >= 0) {
                    if (str.equalsIgnoreCase(this.keys[i3])) {
                        if (z2) {
                            return this.values[i3];
                        }
                        if (this.values[i3] == str2) {
                            z2 = true;
                        }
                    }
                } else {
                    return null;
                }
            }
        }
    }

    public boolean filterNTLMResponses(String str) {
        boolean z2 = false;
        int i2 = 0;
        while (true) {
            if (i2 >= this.nkeys) {
                break;
            }
            if (!str.equalsIgnoreCase(this.keys[i2]) || this.values[i2] == null || this.values[i2].length() <= 5 || !this.values[i2].substring(0, 5).equalsIgnoreCase("NTLM ")) {
                i2++;
            } else {
                z2 = true;
                break;
            }
        }
        if (z2) {
            int i3 = 0;
            for (int i4 = 0; i4 < this.nkeys; i4++) {
                if (!str.equalsIgnoreCase(this.keys[i4]) || (!"Negotiate".equalsIgnoreCase(this.values[i4]) && !PAForUserEnc.AUTH_PACKAGE.equalsIgnoreCase(this.values[i4]))) {
                    if (i4 != i3) {
                        this.keys[i3] = this.keys[i4];
                        this.values[i3] = this.values[i4];
                    }
                    i3++;
                }
            }
            if (i3 != this.nkeys) {
                this.nkeys = i3;
                return true;
            }
            return false;
        }
        return false;
    }

    /* loaded from: rt.jar:sun/net/www/MessageHeader$HeaderIterator.class */
    class HeaderIterator implements Iterator<String> {
        String key;
        Object lock;
        int index = 0;
        int next = -1;
        boolean haveNext = false;

        public HeaderIterator(String str, Object obj) {
            this.key = str;
            this.lock = obj;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            synchronized (this.lock) {
                if (!this.haveNext) {
                    while (this.index < MessageHeader.this.nkeys) {
                        if (this.key.equalsIgnoreCase(MessageHeader.this.keys[this.index])) {
                            this.haveNext = true;
                            int i2 = this.index;
                            this.index = i2 + 1;
                            this.next = i2;
                            return true;
                        }
                        this.index++;
                    }
                    return false;
                }
                return true;
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public String next() {
            synchronized (this.lock) {
                if (this.haveNext) {
                    this.haveNext = false;
                    return MessageHeader.this.values[this.next];
                }
                if (hasNext()) {
                    return next();
                }
                throw new NoSuchElementException("No more elements");
            }
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("remove not allowed");
        }
    }

    public Iterator<String> multiValueIterator(String str) {
        return new HeaderIterator(str, this);
    }

    public synchronized Map<String, List<String>> getHeaders() {
        return getHeaders(null);
    }

    public synchronized Map<String, List<String>> getHeaders(String[] strArr) {
        return filterAndAddHeaders(strArr, null);
    }

    public synchronized Map<String, List<String>> filterAndAddHeaders(String[] strArr, Map<String, List<String>> map) {
        boolean z2 = false;
        HashMap map2 = new HashMap();
        int i2 = this.nkeys;
        while (true) {
            i2--;
            if (i2 < 0) {
                break;
            }
            if (strArr != null) {
                int i3 = 0;
                while (true) {
                    if (i3 >= strArr.length) {
                        break;
                    }
                    if (strArr[i3] == null || !strArr[i3].equalsIgnoreCase(this.keys[i2])) {
                        i3++;
                    } else {
                        z2 = true;
                        break;
                    }
                }
            }
            if (!z2) {
                List arrayList = (List) map2.get(this.keys[i2]);
                if (arrayList == null) {
                    arrayList = new ArrayList();
                    map2.put(this.keys[i2], arrayList);
                }
                arrayList.add(this.values[i2]);
            } else {
                z2 = false;
            }
        }
        if (map != null) {
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                List arrayList2 = (List) map2.get(entry.getKey());
                if (arrayList2 == null) {
                    arrayList2 = new ArrayList();
                    map2.put(entry.getKey(), arrayList2);
                }
                arrayList2.addAll(entry.getValue());
            }
        }
        for (K k2 : map2.keySet()) {
            map2.put(k2, Collections.unmodifiableList((List) map2.get(k2)));
        }
        return Collections.unmodifiableMap(map2);
    }

    private boolean isRequestline(String str) {
        String strTrim = str.trim();
        int iLastIndexOf = strTrim.lastIndexOf(32);
        if (iLastIndexOf <= 0) {
            return false;
        }
        int length = strTrim.length();
        if (length - iLastIndexOf < 9) {
            return false;
        }
        char cCharAt = strTrim.charAt(length - 3);
        char cCharAt2 = strTrim.charAt(length - 2);
        char cCharAt3 = strTrim.charAt(length - 1);
        if (cCharAt < '1' || cCharAt > '9' || cCharAt2 != '.' || cCharAt3 < '0' || cCharAt3 > '9') {
            return false;
        }
        return strTrim.substring(iLastIndexOf + 1, length - 3).equalsIgnoreCase("HTTP/");
    }

    public synchronized void print(PrintStream printStream) {
        for (int i2 = 0; i2 < this.nkeys; i2++) {
            if (this.keys[i2] != null) {
                StringBuilder sb = new StringBuilder(this.keys[i2]);
                if (this.values[i2] != null) {
                    sb.append(": " + this.values[i2]);
                } else if (i2 != 0 || !isRequestline(this.keys[i2])) {
                    sb.append(CallSiteDescriptor.TOKEN_DELIMITER);
                }
                printStream.print(sb.append("\r\n"));
            }
        }
        printStream.print("\r\n");
        printStream.flush();
    }

    public synchronized void add(String str, String str2) {
        grow();
        this.keys[this.nkeys] = str;
        this.values[this.nkeys] = str2;
        this.nkeys++;
    }

    public synchronized void prepend(String str, String str2) {
        grow();
        for (int i2 = this.nkeys; i2 > 0; i2--) {
            this.keys[i2] = this.keys[i2 - 1];
            this.values[i2] = this.values[i2 - 1];
        }
        this.keys[0] = str;
        this.values[0] = str2;
        this.nkeys++;
    }

    public synchronized void set(int i2, String str, String str2) {
        grow();
        if (i2 < 0) {
            return;
        }
        if (i2 >= this.nkeys) {
            add(str, str2);
        } else {
            this.keys[i2] = str;
            this.values[i2] = str2;
        }
    }

    private void grow() {
        if (this.keys == null || this.nkeys >= this.keys.length) {
            String[] strArr = new String[this.nkeys + 4];
            String[] strArr2 = new String[this.nkeys + 4];
            if (this.keys != null) {
                System.arraycopy(this.keys, 0, strArr, 0, this.nkeys);
            }
            if (this.values != null) {
                System.arraycopy(this.values, 0, strArr2, 0, this.nkeys);
            }
            this.keys = strArr;
            this.values = strArr2;
        }
    }

    public synchronized void remove(String str) {
        if (str == null) {
            for (int i2 = 0; i2 < this.nkeys; i2++) {
                while (this.keys[i2] == null && i2 < this.nkeys) {
                    for (int i3 = i2; i3 < this.nkeys - 1; i3++) {
                        this.keys[i3] = this.keys[i3 + 1];
                        this.values[i3] = this.values[i3 + 1];
                    }
                    this.nkeys--;
                }
            }
            return;
        }
        for (int i4 = 0; i4 < this.nkeys; i4++) {
            while (str.equalsIgnoreCase(this.keys[i4]) && i4 < this.nkeys) {
                for (int i5 = i4; i5 < this.nkeys - 1; i5++) {
                    this.keys[i5] = this.keys[i5 + 1];
                    this.values[i5] = this.values[i5 + 1];
                }
                this.nkeys--;
            }
        }
    }

    public synchronized void set(String str, String str2) {
        int i2 = this.nkeys;
        do {
            i2--;
            if (i2 < 0) {
                add(str, str2);
                return;
            }
        } while (!str.equalsIgnoreCase(this.keys[i2]));
        this.values[i2] = str2;
    }

    public synchronized void setIfNotSet(String str, String str2) {
        if (findValue(str) == null) {
            add(str, str2);
        }
    }

    public static String canonicalID(String str) {
        boolean z2;
        char cCharAt;
        char cCharAt2;
        if (str == null) {
            return "";
        }
        int i2 = 0;
        int length = str.length();
        boolean z3 = false;
        while (true) {
            z2 = z3;
            if (i2 >= length || ((cCharAt2 = str.charAt(i2)) != '<' && cCharAt2 > ' ')) {
                break;
            }
            i2++;
            z3 = true;
        }
        while (i2 < length && ((cCharAt = str.charAt(length - 1)) == '>' || cCharAt <= ' ')) {
            length--;
            z2 = true;
        }
        return z2 ? str.substring(i2, length) : str;
    }

    public void parseHeader(InputStream inputStream) throws IOException {
        synchronized (this) {
            this.nkeys = 0;
        }
        mergeHeader(inputStream);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00d8  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x00ed A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void mergeHeader(java.io.InputStream r7) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 382
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.net.www.MessageHeader.mergeHeader(java.io.InputStream):void");
    }

    public synchronized String toString() {
        String str = super.toString() + this.nkeys + " pairs: ";
        for (int i2 = 0; i2 < this.keys.length && i2 < this.nkeys; i2++) {
            str = str + VectorFormat.DEFAULT_PREFIX + this.keys[i2] + ": " + this.values[i2] + "}";
        }
        return str;
    }
}
