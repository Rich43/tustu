package com.sun.org.apache.xml.internal.resolver.readers;

import com.sun.org.apache.xml.internal.resolver.Catalog;
import com.sun.org.apache.xml.internal.resolver.CatalogException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Stack;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/resolver/readers/TextCatalogReader.class */
public class TextCatalogReader implements CatalogReader {
    protected InputStream catfile = null;
    protected int[] stack = new int[3];
    protected Stack tokenStack = new Stack();
    protected int top = -1;
    protected boolean caseSensitive = false;

    public void setCaseSensitive(boolean isCaseSensitive) {
        this.caseSensitive = isCaseSensitive;
    }

    public boolean getCaseSensitive() {
        return this.caseSensitive;
    }

    @Override // com.sun.org.apache.xml.internal.resolver.readers.CatalogReader
    public void readCatalog(Catalog catalog, String fileUrl) throws IOException {
        URL catURL;
        try {
            catURL = new URL(fileUrl);
        } catch (MalformedURLException e2) {
            catURL = new URL("file:///" + fileUrl);
        }
        URLConnection urlCon = catURL.openConnection();
        try {
            readCatalog(catalog, urlCon.getInputStream());
        } catch (FileNotFoundException e3) {
            catalog.getCatalogManager().debug.message(1, "Failed to load catalog, file not found", catURL.toString());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:42:0x00fd  */
    /* JADX WARN: Removed duplicated region for block: B:55:? A[RETURN, SYNTHETIC] */
    @Override // com.sun.org.apache.xml.internal.resolver.readers.CatalogReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void readCatalog(com.sun.org.apache.xml.internal.resolver.Catalog r7, java.io.InputStream r8) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 270
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xml.internal.resolver.readers.TextCatalogReader.readCatalog(com.sun.org.apache.xml.internal.resolver.Catalog, java.io.InputStream):void");
    }

    protected void finalize() {
        if (this.catfile != null) {
            try {
                this.catfile.close();
            } catch (IOException e2) {
            }
        }
        this.catfile = null;
    }

    protected String nextToken() throws CatalogException, IOException {
        int nextch;
        String token = "";
        if (!this.tokenStack.empty()) {
            return (String) this.tokenStack.pop();
        }
        do {
            int ch = this.catfile.read();
            while (ch <= 32) {
                ch = this.catfile.read();
                if (ch < 0) {
                    return null;
                }
            }
            int nextch2 = this.catfile.read();
            if (nextch2 < 0) {
                return null;
            }
            if (ch == 45 && nextch2 == 45) {
                int ch2 = 32;
                int iNextChar = nextChar();
                while (true) {
                    nextch = iNextChar;
                    if ((ch2 == 45 && nextch == 45) || nextch <= 0) {
                        break;
                    }
                    ch2 = nextch;
                    iNextChar = nextChar();
                }
            } else {
                int[] iArr = this.stack;
                int i2 = this.top + 1;
                this.top = i2;
                iArr[i2] = nextch2;
                int[] iArr2 = this.stack;
                int i3 = this.top + 1;
                this.top = i3;
                iArr2[i3] = ch;
                int ch3 = nextChar();
                if (ch3 != 34 && ch3 != 39) {
                    while (ch3 > 32) {
                        int nextch3 = nextChar();
                        if (ch3 == 45 && nextch3 == 45) {
                            int[] iArr3 = this.stack;
                            int i4 = this.top + 1;
                            this.top = i4;
                            iArr3[i4] = ch3;
                            int[] iArr4 = this.stack;
                            int i5 = this.top + 1;
                            this.top = i5;
                            iArr4[i5] = nextch3;
                            return token;
                        }
                        char[] chararr = {(char) ch3};
                        String s2 = new String(chararr);
                        token = token.concat(s2);
                        ch3 = nextch3;
                    }
                    return token;
                }
                while (true) {
                    int ch4 = nextChar();
                    if (ch4 != ch3) {
                        char[] chararr2 = {(char) ch4};
                        String s3 = new String(chararr2);
                        token = token.concat(s3);
                    } else {
                        return token;
                    }
                }
            }
        } while (nextch >= 0);
        throw new CatalogException(8, "Unterminated comment in catalog file; EOF treated as end-of-comment.");
    }

    protected int nextChar() throws IOException {
        if (this.top < 0) {
            return this.catfile.read();
        }
        int[] iArr = this.stack;
        int i2 = this.top;
        this.top = i2 - 1;
        return iArr[i2];
    }
}
