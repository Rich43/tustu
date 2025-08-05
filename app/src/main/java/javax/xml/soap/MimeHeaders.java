package javax.xml.soap;

import java.util.Iterator;
import java.util.Vector;

/* loaded from: rt.jar:javax/xml/soap/MimeHeaders.class */
public class MimeHeaders {
    private Vector headers = new Vector();

    public String[] getHeader(String name) {
        Vector values = new Vector();
        for (int i2 = 0; i2 < this.headers.size(); i2++) {
            MimeHeader hdr = (MimeHeader) this.headers.elementAt(i2);
            if (hdr.getName().equalsIgnoreCase(name) && hdr.getValue() != null) {
                values.addElement(hdr.getValue());
            }
        }
        if (values.size() == 0) {
            return null;
        }
        String[] r2 = new String[values.size()];
        values.copyInto(r2);
        return r2;
    }

    public void setHeader(String name, String value) {
        boolean found = false;
        if (name == null || name.equals("")) {
            throw new IllegalArgumentException("Illegal MimeHeader name");
        }
        int i2 = 0;
        while (i2 < this.headers.size()) {
            MimeHeader hdr = (MimeHeader) this.headers.elementAt(i2);
            if (hdr.getName().equalsIgnoreCase(name)) {
                if (!found) {
                    this.headers.setElementAt(new MimeHeader(hdr.getName(), value), i2);
                    found = true;
                } else {
                    int i3 = i2;
                    i2--;
                    this.headers.removeElementAt(i3);
                }
            }
            i2++;
        }
        if (!found) {
            addHeader(name, value);
        }
    }

    public void addHeader(String name, String value) {
        if (name == null || name.equals("")) {
            throw new IllegalArgumentException("Illegal MimeHeader name");
        }
        int pos = this.headers.size();
        for (int i2 = pos - 1; i2 >= 0; i2--) {
            MimeHeader hdr = (MimeHeader) this.headers.elementAt(i2);
            if (hdr.getName().equalsIgnoreCase(name)) {
                this.headers.insertElementAt(new MimeHeader(name, value), i2 + 1);
                return;
            }
        }
        this.headers.addElement(new MimeHeader(name, value));
    }

    public void removeHeader(String name) {
        int i2 = 0;
        while (i2 < this.headers.size()) {
            MimeHeader hdr = (MimeHeader) this.headers.elementAt(i2);
            if (hdr.getName().equalsIgnoreCase(name)) {
                int i3 = i2;
                i2--;
                this.headers.removeElementAt(i3);
            }
            i2++;
        }
    }

    public void removeAllHeaders() {
        this.headers.removeAllElements();
    }

    public Iterator getAllHeaders() {
        return this.headers.iterator();
    }

    /* loaded from: rt.jar:javax/xml/soap/MimeHeaders$MatchingIterator.class */
    class MatchingIterator implements Iterator {
        private boolean match;
        private Iterator iterator;
        private String[] names;
        private Object nextHeader;

        MatchingIterator(String[] names, boolean match) {
            this.match = match;
            this.names = names;
            this.iterator = MimeHeaders.this.headers.iterator();
        }

        private Object nextMatch() {
            while (this.iterator.hasNext()) {
                MimeHeader hdr = (MimeHeader) this.iterator.next();
                if (this.names == null) {
                    if (this.match) {
                        return null;
                    }
                    return hdr;
                }
                int i2 = 0;
                while (true) {
                    if (i2 < this.names.length) {
                        if (!hdr.getName().equalsIgnoreCase(this.names[i2])) {
                            i2++;
                        } else if (this.match) {
                            return hdr;
                        }
                    } else if (!this.match) {
                        return hdr;
                    }
                }
            }
            return null;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.nextHeader == null) {
                this.nextHeader = nextMatch();
            }
            return this.nextHeader != null;
        }

        @Override // java.util.Iterator
        public Object next() {
            if (this.nextHeader != null) {
                Object ret = this.nextHeader;
                this.nextHeader = null;
                return ret;
            }
            if (hasNext()) {
                return this.nextHeader;
            }
            return null;
        }

        @Override // java.util.Iterator
        public void remove() {
            this.iterator.remove();
        }
    }

    public Iterator getMatchingHeaders(String[] names) {
        return new MatchingIterator(names, true);
    }

    public Iterator getNonMatchingHeaders(String[] names) {
        return new MatchingIterator(names, false);
    }
}
