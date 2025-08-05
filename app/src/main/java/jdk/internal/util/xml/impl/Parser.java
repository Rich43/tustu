package jdk.internal.util.xml.impl;

import com.sun.glass.ui.Clipboard;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import jdk.internal.org.xml.sax.InputSource;
import jdk.internal.org.xml.sax.SAXException;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:jdk/internal/util/xml/impl/Parser.class */
public abstract class Parser {
    public static final String FAULT = "";
    protected static final int BUFFSIZE_READER = 512;
    protected static final int BUFFSIZE_PARSER = 128;
    private static final int MAX_ARRAY_SIZE = 67108864;
    public static final char EOS = 65535;
    private Pair mNoNS;
    private Pair mXml;
    private Map<String, Input> mEnt;
    private Map<String, Input> mPEnt;
    protected boolean mIsSAlone;
    protected boolean mIsSAloneSet;
    protected boolean mIsNSAware;
    protected static final int PH_BEFORE_DOC = -1;
    protected static final int PH_DOC_START = 0;
    protected static final int PH_MISC_DTD = 1;
    protected static final int PH_DTD = 2;
    protected static final int PH_DTD_MISC = 3;
    protected static final int PH_DOCELM = 4;
    protected static final int PH_DOCELM_MISC = 5;
    protected static final int PH_AFTER_DOC = 6;
    protected int mEvt;
    protected static final int EV_NULL = 0;
    protected static final int EV_ELM = 1;
    protected static final int EV_ELMS = 2;
    protected static final int EV_ELME = 3;
    protected static final int EV_TEXT = 4;
    protected static final int EV_WSPC = 5;
    protected static final int EV_PI = 6;
    protected static final int EV_CDAT = 7;
    protected static final int EV_COMM = 8;
    protected static final int EV_DTD = 9;
    protected static final int EV_ENT = 10;
    private char mESt;
    protected int mBuffIdx;
    protected Pair mPref;
    protected Pair mElm;
    protected Pair mAttL;
    protected Input mDoc;
    protected Input mInp;
    private char[] mChars;
    private int mChLen;
    private int mChIdx;
    private String[] mItems;
    private char mAttrIdx;
    private String mUnent;
    private Pair mDltd;
    private static final char[] NONS = new char[1];
    private static final char[] XML;
    private static final char[] XMLNS;
    private static final byte[] asctyp;
    private static final byte[] nmttyp;
    protected int mPh = -1;
    protected char[] mBuff = new char[128];
    protected Attrs mAttrs = new Attrs();

    protected abstract void docType(String str, String str2, String str3) throws SAXException;

    protected abstract void comm(char[] cArr, int i2);

    protected abstract void pi(String str, String str2) throws Exception;

    protected abstract void newPrefix() throws Exception;

    protected abstract void skippedEnt(String str) throws Exception;

    protected abstract InputSource resolveEnt(String str, String str2, String str3) throws Exception;

    protected abstract void notDecl(String str, String str2, String str3) throws Exception;

    protected abstract void unparsedEntDecl(String str, String str2, String str3, String str4) throws Exception;

    protected abstract void panic(String str) throws Exception;

    protected abstract void bflash() throws Exception;

    protected abstract void bflash_ws() throws Exception;

    static {
        short s2;
        NONS[0] = 0;
        XML = new char[4];
        XML[0] = 4;
        XML[1] = 'x';
        XML[2] = 'm';
        XML[3] = 'l';
        XMLNS = new char[6];
        XMLNS[0] = 6;
        XMLNS[1] = 'x';
        XMLNS[2] = 'm';
        XMLNS[3] = 'l';
        XMLNS[4] = 'n';
        XMLNS[5] = 's';
        short s3 = 0;
        asctyp = new byte[128];
        while (s3 < 32) {
            short s4 = s3;
            s3 = (short) (s3 + 1);
            asctyp[s4] = 122;
        }
        asctyp[9] = 32;
        asctyp[13] = 32;
        asctyp[10] = 32;
        while (s3 < 48) {
            short s5 = s3;
            short s6 = s3;
            s3 = (short) (s3 + 1);
            asctyp[s5] = (byte) s6;
        }
        while (s3 <= 57) {
            short s7 = s3;
            s3 = (short) (s3 + 1);
            asctyp[s7] = 100;
        }
        while (s3 < 65) {
            short s8 = s3;
            short s9 = s3;
            s3 = (short) (s3 + 1);
            asctyp[s8] = (byte) s9;
        }
        while (s3 <= 90) {
            short s10 = s3;
            s3 = (short) (s3 + 1);
            asctyp[s10] = 65;
        }
        while (s3 < 97) {
            short s11 = s3;
            short s12 = s3;
            s3 = (short) (s3 + 1);
            asctyp[s11] = (byte) s12;
        }
        while (s3 <= 122) {
            short s13 = s3;
            s3 = (short) (s3 + 1);
            asctyp[s13] = 97;
        }
        while (s3 < 128) {
            short s14 = s3;
            short s15 = s3;
            s3 = (short) (s3 + 1);
            asctyp[s14] = (byte) s15;
        }
        nmttyp = new byte[128];
        short s16 = 0;
        while (true) {
            s2 = s16;
            if (s2 >= 48) {
                break;
            }
            nmttyp[s2] = -1;
            s16 = (short) (s2 + 1);
        }
        while (s2 <= 57) {
            short s17 = s2;
            s2 = (short) (s2 + 1);
            nmttyp[s17] = 2;
        }
        while (s2 < 65) {
            short s18 = s2;
            s2 = (short) (s2 + 1);
            nmttyp[s18] = -1;
        }
        short s19 = 91;
        while (true) {
            short s20 = s19;
            if (s20 >= 97) {
                break;
            }
            nmttyp[s20] = -1;
            s19 = (short) (s20 + 1);
        }
        short s21 = 123;
        while (true) {
            short s22 = s21;
            if (s22 < 128) {
                nmttyp[s22] = -1;
                s21 = (short) (s22 + 1);
            } else {
                nmttyp[95] = 0;
                nmttyp[58] = 1;
                nmttyp[46] = 2;
                nmttyp[45] = 2;
                nmttyp[32] = 3;
                nmttyp[9] = 3;
                nmttyp[13] = 3;
                nmttyp[10] = 3;
                return;
            }
        }
    }

    protected Parser() {
        this.mPref = pair(this.mPref);
        this.mPref.name = "";
        this.mPref.value = "";
        this.mPref.chars = NONS;
        this.mNoNS = this.mPref;
        this.mPref = pair(this.mPref);
        this.mPref.name = "xml";
        this.mPref.value = "http://www.w3.org/XML/1998/namespace";
        this.mPref.chars = XML;
        this.mXml = this.mPref;
    }

    protected void init() {
        this.mUnent = null;
        this.mElm = null;
        this.mPref = this.mXml;
        this.mAttL = null;
        this.mPEnt = new HashMap();
        this.mEnt = new HashMap();
        this.mDoc = this.mInp;
        this.mChars = this.mInp.chars;
        this.mPh = 0;
    }

    protected void cleanup() {
        while (this.mAttL != null) {
            while (this.mAttL.list != null) {
                if (this.mAttL.list.list != null) {
                    del(this.mAttL.list.list);
                }
                this.mAttL.list = del(this.mAttL.list);
            }
            this.mAttL = del(this.mAttL);
        }
        while (this.mElm != null) {
            this.mElm = del(this.mElm);
        }
        while (this.mPref != this.mXml) {
            this.mPref = del(this.mPref);
        }
        while (this.mInp != null) {
            pop();
        }
        if (this.mDoc != null && this.mDoc.src != null) {
            try {
                this.mDoc.src.close();
            } catch (IOException e2) {
            }
        }
        this.mPEnt = null;
        this.mEnt = null;
        this.mDoc = null;
        this.mPh = 6;
    }

    protected int step() throws Exception {
        char chVar;
        this.mEvt = 0;
        boolean z2 = false;
        while (this.mEvt == 0) {
            if (this.mChIdx < this.mChLen) {
                char[] cArr = this.mChars;
                int i2 = this.mChIdx;
                this.mChIdx = i2 + 1;
                chVar = cArr[i2];
            } else {
                chVar = getch();
            }
            char c2 = chVar;
            switch (z2) {
                case false:
                    if (c2 != '<') {
                        bkch();
                        this.mBuffIdx = -1;
                        z2 = true;
                        break;
                    } else {
                        switch (getch()) {
                            case '!':
                                char chVar2 = getch();
                                bkch();
                                switch (chVar2) {
                                    case '-':
                                        this.mEvt = 8;
                                        comm();
                                        break;
                                    case '[':
                                        this.mEvt = 7;
                                        cdat();
                                        break;
                                    default:
                                        this.mEvt = 9;
                                        dtd();
                                        break;
                                }
                            case '/':
                                this.mEvt = 3;
                                if (this.mElm == null) {
                                    panic("");
                                }
                                this.mBuffIdx = -1;
                                bname(this.mIsNSAware);
                                char[] cArr2 = this.mElm.chars;
                                if (cArr2.length == this.mBuffIdx + 1) {
                                    char c3 = 1;
                                    while (true) {
                                        char c4 = c3;
                                        if (c4 <= this.mBuffIdx) {
                                            if (cArr2[c4] != this.mBuff[c4]) {
                                                panic("");
                                            }
                                            c3 = (char) (c4 + 1);
                                        }
                                    }
                                } else {
                                    panic("");
                                }
                                if (wsskip() != '>') {
                                    panic("");
                                }
                                getch();
                                break;
                            case '?':
                                this.mEvt = 6;
                                pi();
                                break;
                            default:
                                bkch();
                                this.mElm = pair(this.mElm);
                                this.mElm.chars = qname(this.mIsNSAware);
                                this.mElm.name = this.mElm.local();
                                this.mElm.id = this.mElm.next != null ? this.mElm.next.id : 0;
                                this.mElm.num = 0;
                                Pair pairFind = find(this.mAttL, this.mElm.chars);
                                this.mElm.list = pairFind != null ? pairFind.list : null;
                                this.mAttrIdx = (char) 0;
                                Pair pair = pair(null);
                                pair.num = 0;
                                attr(pair);
                                del(pair);
                                this.mElm.value = this.mIsNSAware ? rslv(this.mElm.chars) : null;
                                switch (wsskip()) {
                                    case '/':
                                        getch();
                                        if (getch() != '>') {
                                            panic("");
                                        }
                                        this.mEvt = 1;
                                        break;
                                    case '>':
                                        getch();
                                        this.mEvt = 2;
                                        break;
                                    default:
                                        panic("");
                                        break;
                                }
                        }
                    }
                case true:
                    switch (c2) {
                        case '\t':
                        case '\n':
                        case ' ':
                            bappend(c2);
                            break;
                        case '\r':
                            if (getch() != '\n') {
                                bkch();
                            }
                            bappend('\n');
                            break;
                        case '<':
                            this.mEvt = 5;
                            bkch();
                            bflash_ws();
                            break;
                        default:
                            bkch();
                            z2 = 2;
                            break;
                    }
                case true:
                    switch (c2) {
                        case '\r':
                            if (getch() != '\n') {
                                bkch();
                            }
                            bappend('\n');
                            continue;
                        case '&':
                            if (this.mUnent == null) {
                                String strEnt = ent('x');
                                this.mUnent = strEnt;
                                if (strEnt == null) {
                                    break;
                                } else {
                                    this.mEvt = 4;
                                    bkch();
                                    setch('&');
                                    bflash();
                                    break;
                                }
                            } else {
                                this.mEvt = 10;
                                skippedEnt(this.mUnent);
                                this.mUnent = null;
                                continue;
                            }
                        case '<':
                            this.mEvt = 4;
                            bkch();
                            bflash();
                            continue;
                        case 65535:
                            panic("");
                            break;
                    }
                    bappend(c2);
                    break;
                default:
                    panic("");
                    break;
            }
        }
        return this.mEvt;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v7 */
    private void dtd() throws Exception {
        String strName = null;
        Pair pairPubsys = null;
        if (!"DOCTYPE".equals(name(false))) {
            panic("");
        }
        this.mPh = 2;
        boolean z2 = false;
        while (z2 >= 0) {
            char chVar = getch();
            if (chVar == 65535) {
                panic("");
            }
            switch (z2) {
                case false:
                    if (chtyp(chVar) == ' ') {
                        break;
                    } else {
                        bkch();
                        strName = name(this.mIsNSAware);
                        wsskip();
                        z2 = true;
                        break;
                    }
                case true:
                    switch (chtyp(chVar)) {
                        case '>':
                            bkch();
                            z2 = 3;
                            docType(strName, null, null);
                            break;
                        case 'A':
                            bkch();
                            pairPubsys = pubsys(' ');
                            z2 = 2;
                            docType(strName, pairPubsys.name, pairPubsys.value);
                            break;
                        case '[':
                            bkch();
                            z2 = 2;
                            docType(strName, null, null);
                            break;
                        default:
                            panic("");
                            break;
                    }
                case true:
                    switch (chtyp(chVar)) {
                        case ' ':
                            break;
                        case '>':
                            bkch();
                            z2 = 3;
                            break;
                        case '[':
                            dtdsub();
                            z2 = 3;
                            break;
                        default:
                            panic("");
                            break;
                    }
                case true:
                    switch (chtyp(chVar)) {
                        case ' ':
                            break;
                        case '>':
                            if (pairPubsys != null) {
                                InputSource inputSourceResolveEnt = resolveEnt(strName, pairPubsys.name, pairPubsys.value);
                                if (inputSourceResolveEnt != null) {
                                    if (!this.mIsSAlone) {
                                        bkch();
                                        setch(']');
                                        push(new Input(512));
                                        setinp(inputSourceResolveEnt);
                                        this.mInp.pubid = pairPubsys.name;
                                        this.mInp.sysid = pairPubsys.value;
                                        dtdsub();
                                    } else {
                                        skippedEnt("[dtd]");
                                        if (inputSourceResolveEnt.getCharacterStream() != null) {
                                            try {
                                                inputSourceResolveEnt.getCharacterStream().close();
                                            } catch (IOException e2) {
                                            }
                                        }
                                        if (inputSourceResolveEnt.getByteStream() != null) {
                                            try {
                                                inputSourceResolveEnt.getByteStream().close();
                                            } catch (IOException e3) {
                                            }
                                        }
                                    }
                                } else {
                                    skippedEnt("[dtd]");
                                }
                                del(pairPubsys);
                            }
                            z2 = -1;
                            break;
                        default:
                            panic("");
                            break;
                    }
                default:
                    panic("");
                    break;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1 */
    private void dtdsub() throws Exception {
        boolean z2 = false;
        while (z2 >= 0) {
            char chVar = getch();
            switch (z2) {
                case false:
                    switch (chtyp(chVar)) {
                        case ' ':
                            break;
                        case '%':
                            pent(' ');
                            break;
                        case '<':
                            switch (getch()) {
                                case '!':
                                    char chVar2 = getch();
                                    bkch();
                                    if (chVar2 == '-') {
                                        comm();
                                        break;
                                    } else {
                                        bntok();
                                        switch (bkeyword()) {
                                            case 'a':
                                                dtdattl();
                                                break;
                                            case 'e':
                                                dtdelm();
                                                break;
                                            case 'n':
                                                dtdent();
                                                break;
                                            case 'o':
                                                dtdnot();
                                                break;
                                            default:
                                                panic("");
                                                break;
                                        }
                                        z2 = true;
                                        break;
                                    }
                                case '?':
                                    pi();
                                    break;
                                default:
                                    panic("");
                                    break;
                            }
                        case 'Z':
                            if (getch() != ']') {
                                panic("");
                            }
                            z2 = -1;
                            break;
                        case ']':
                            z2 = -1;
                            break;
                        default:
                            panic("");
                            break;
                    }
                case true:
                    switch (chVar) {
                        case '\t':
                        case '\n':
                        case '\r':
                        case ' ':
                            break;
                        case '>':
                            z2 = false;
                            break;
                        default:
                            panic("");
                            break;
                    }
                default:
                    panic("");
                    break;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v5 */
    private void dtdent() throws Exception {
        String strName = null;
        boolean z2 = false;
        while (z2 >= 0) {
            char chVar = getch();
            switch (z2) {
                case false:
                    switch (chtyp(chVar)) {
                        case ' ':
                            break;
                        case '%':
                            char chVar2 = getch();
                            bkch();
                            if (chtyp(chVar2) == ' ') {
                                wsskip();
                                strName = name(false);
                                switch (chtyp(wsskip())) {
                                    case '\"':
                                    case '\'':
                                        bqstr('d');
                                        char[] cArr = new char[this.mBuffIdx + 1];
                                        System.arraycopy(this.mBuff, 1, cArr, 1, cArr.length - 1);
                                        cArr[0] = ' ';
                                        if (!this.mPEnt.containsKey(strName)) {
                                            Input input = new Input(cArr);
                                            input.pubid = this.mInp.pubid;
                                            input.sysid = this.mInp.sysid;
                                            input.xmlenc = this.mInp.xmlenc;
                                            input.xmlver = this.mInp.xmlver;
                                            this.mPEnt.put(strName, input);
                                        }
                                        z2 = -1;
                                        break;
                                    case 'A':
                                        Pair pairPubsys = pubsys(' ');
                                        if (wsskip() == '>') {
                                            if (!this.mPEnt.containsKey(strName)) {
                                                Input input2 = new Input();
                                                input2.pubid = pairPubsys.name;
                                                input2.sysid = pairPubsys.value;
                                                this.mPEnt.put(strName, input2);
                                            }
                                        } else {
                                            panic("");
                                        }
                                        del(pairPubsys);
                                        z2 = -1;
                                        break;
                                    default:
                                        panic("");
                                        break;
                                }
                            } else {
                                pent(' ');
                                break;
                            }
                        default:
                            bkch();
                            strName = name(false);
                            z2 = true;
                            break;
                    }
                case true:
                    switch (chtyp(chVar)) {
                        case ' ':
                            break;
                        case '\"':
                        case '\'':
                            bkch();
                            bqstr('d');
                            if (this.mEnt.get(strName) == null) {
                                char[] cArr2 = new char[this.mBuffIdx];
                                System.arraycopy(this.mBuff, 1, cArr2, 0, cArr2.length);
                                if (!this.mEnt.containsKey(strName)) {
                                    Input input3 = new Input(cArr2);
                                    input3.pubid = this.mInp.pubid;
                                    input3.sysid = this.mInp.sysid;
                                    input3.xmlenc = this.mInp.xmlenc;
                                    input3.xmlver = this.mInp.xmlver;
                                    this.mEnt.put(strName, input3);
                                }
                            }
                            z2 = -1;
                            break;
                        case 'A':
                            bkch();
                            Pair pairPubsys2 = pubsys(' ');
                            switch (wsskip()) {
                                case '>':
                                    if (!this.mEnt.containsKey(strName)) {
                                        Input input4 = new Input();
                                        input4.pubid = pairPubsys2.name;
                                        input4.sysid = pairPubsys2.value;
                                        this.mEnt.put(strName, input4);
                                        break;
                                    }
                                    break;
                                case 'N':
                                    if ("NDATA".equals(name(false))) {
                                        wsskip();
                                        unparsedEntDecl(strName, pairPubsys2.name, pairPubsys2.value, name(false));
                                        break;
                                    }
                                default:
                                    panic("");
                                    break;
                            }
                            del(pairPubsys2);
                            z2 = -1;
                            break;
                        default:
                            panic("");
                            break;
                    }
                default:
                    panic("");
                    break;
            }
        }
    }

    private void dtdelm() throws Exception {
        wsskip();
        name(this.mIsNSAware);
        while (true) {
            switch (getch()) {
                case '>':
                    bkch();
                    return;
                case 65535:
                    panic("");
                    break;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v3 */
    private void dtdattl() throws Exception {
        Pair pairFind = null;
        boolean z2 = false;
        while (z2 >= 0) {
            char chVar = getch();
            switch (z2) {
                case false:
                    switch (chtyp(chVar)) {
                        case ' ':
                            break;
                        case '%':
                            pent(' ');
                            break;
                        case ':':
                        case 'A':
                        case 'X':
                        case '_':
                        case 'a':
                            bkch();
                            char[] cArrQname = qname(this.mIsNSAware);
                            pairFind = find(this.mAttL, cArrQname);
                            if (pairFind == null) {
                                pairFind = pair(this.mAttL);
                                pairFind.chars = cArrQname;
                                this.mAttL = pairFind;
                            }
                            z2 = true;
                            break;
                        default:
                            panic("");
                            break;
                    }
                case true:
                    switch (chtyp(chVar)) {
                        case ' ':
                            continue;
                        case '%':
                            pent(' ');
                            break;
                        case ':':
                        case 'A':
                        case 'X':
                        case '_':
                        case 'a':
                            bkch();
                            dtdatt(pairFind);
                            if (wsskip() != '>') {
                                break;
                            } else {
                                return;
                            }
                        default:
                            panic("");
                            break;
                    }
                default:
                    panic("");
                    break;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v3 */
    private void dtdatt(Pair pair) throws Exception {
        Pair pair2 = null;
        boolean z2 = false;
        while (z2 >= 0) {
            char chVar = getch();
            switch (z2) {
                case false:
                    switch (chtyp(chVar)) {
                        case ' ':
                            break;
                        case '%':
                            pent(' ');
                            break;
                        case ':':
                        case 'A':
                        case 'X':
                        case '_':
                        case 'a':
                            bkch();
                            char[] cArrQname = qname(this.mIsNSAware);
                            if (find(pair.list, cArrQname) == null) {
                                pair2 = pair(pair.list);
                                pair2.chars = cArrQname;
                                pair.list = pair2;
                            } else {
                                pair2 = pair(null);
                                pair2.chars = cArrQname;
                                pair2.id = 99;
                            }
                            wsskip();
                            z2 = true;
                            break;
                        default:
                            panic("");
                            break;
                    }
                case true:
                    switch (chtyp(chVar)) {
                        case ' ':
                            break;
                        case '%':
                            pent(' ');
                            break;
                        case '(':
                            pair2.id = 117;
                            z2 = 2;
                            break;
                        default:
                            bkch();
                            bntok();
                            pair2.id = bkeyword();
                            switch (pair2.id) {
                                case 78:
                                case 82:
                                case 84:
                                case 99:
                                case 105:
                                case 110:
                                case 114:
                                case 116:
                                    wsskip();
                                    z2 = 4;
                                    break;
                                case 111:
                                    if (wsskip() != '(') {
                                        panic("");
                                    }
                                    getch();
                                    z2 = 2;
                                    break;
                                default:
                                    panic("");
                                    break;
                            }
                    }
                case true:
                    switch (chtyp(chVar)) {
                        case ' ':
                            break;
                        case '%':
                            pent(' ');
                            break;
                        case '-':
                        case '.':
                        case ':':
                        case 'A':
                        case 'X':
                        case '_':
                        case 'a':
                        case 'd':
                            bkch();
                            switch (pair2.id) {
                                case 111:
                                    this.mBuffIdx = -1;
                                    bname(false);
                                    break;
                                case 117:
                                    bntok();
                                    break;
                                default:
                                    panic("");
                                    break;
                            }
                            wsskip();
                            z2 = 3;
                            break;
                        default:
                            panic("");
                            break;
                    }
                case true:
                    switch (chVar) {
                        case '%':
                            pent(' ');
                            break;
                        case ')':
                            wsskip();
                            z2 = 4;
                            break;
                        case '|':
                            wsskip();
                            switch (pair2.id) {
                                case 111:
                                    this.mBuffIdx = -1;
                                    bname(false);
                                    break;
                                case 117:
                                    bntok();
                                    break;
                                default:
                                    panic("");
                                    break;
                            }
                            wsskip();
                            break;
                        default:
                            panic("");
                            break;
                    }
                case true:
                    switch (chVar) {
                        case '\t':
                        case '\n':
                        case '\r':
                        case ' ':
                            break;
                        case '\"':
                        case '\'':
                            bkch();
                            z2 = 5;
                            break;
                        case '#':
                            bntok();
                            switch (bkeyword()) {
                                case 'F':
                                    switch (wsskip()) {
                                        case '\"':
                                        case '\'':
                                            z2 = 5;
                                            continue;
                                        case 65535:
                                            panic("");
                                            break;
                                    }
                                    z2 = -1;
                                    break;
                                case 'I':
                                case 'Q':
                                    z2 = -1;
                                    break;
                                default:
                                    panic("");
                                    break;
                            }
                        case '%':
                            pent(' ');
                            break;
                        default:
                            bkch();
                            z2 = -1;
                            break;
                    }
                case true:
                    switch (chVar) {
                        case '\"':
                        case '\'':
                            bkch();
                            bqstr('d');
                            pair2.list = pair(null);
                            pair2.list.chars = new char[pair2.chars.length + this.mBuffIdx + 3];
                            System.arraycopy(pair2.chars, 1, pair2.list.chars, 0, pair2.chars.length - 1);
                            pair2.list.chars[pair2.chars.length - 1] = '=';
                            pair2.list.chars[pair2.chars.length] = chVar;
                            System.arraycopy(this.mBuff, 1, pair2.list.chars, pair2.chars.length + 1, this.mBuffIdx);
                            pair2.list.chars[pair2.chars.length + this.mBuffIdx + 1] = chVar;
                            pair2.list.chars[pair2.chars.length + this.mBuffIdx + 2] = ' ';
                            z2 = -1;
                            break;
                        default:
                            panic("");
                            break;
                    }
                default:
                    panic("");
                    break;
            }
        }
    }

    private void dtdnot() throws Exception {
        wsskip();
        String strName = name(false);
        wsskip();
        Pair pairPubsys = pubsys('N');
        notDecl(strName, pairPubsys.name, pairPubsys.value);
        del(pairPubsys);
    }

    private void attr(Pair pair) throws Exception {
        switch (wsskip()) {
            case '/':
            case '>':
                if ((pair.num & 2) == 0) {
                    pair.num |= 2;
                    Input input = this.mInp;
                    Pair pair2 = this.mElm.list;
                    while (true) {
                        Pair pair3 = pair2;
                        if (pair3 != null) {
                            if (pair3.list != null && find(pair.next, pair3.chars) == null) {
                                push(new Input(pair3.list.chars));
                            }
                            pair2 = pair3.next;
                        } else if (this.mInp != input) {
                            attr(pair);
                            return;
                        }
                    }
                }
                this.mAttrs.setLength(this.mAttrIdx);
                this.mItems = this.mAttrs.mItems;
                return;
            case 65535:
                panic("");
                break;
        }
        pair.chars = qname(this.mIsNSAware);
        pair.name = pair.local();
        String strAtype = atype(pair);
        wsskip();
        if (getch() != '=') {
            panic("");
        }
        bqstr((char) pair.id);
        String str = new String(this.mBuff, 1, this.mBuffIdx);
        Pair pair4 = pair(pair);
        pair4.num = pair.num & (-2);
        if (!this.mIsNSAware || !isdecl(pair, str)) {
            this.mAttrIdx = (char) (this.mAttrIdx + 1);
            attr(pair4);
            this.mAttrIdx = (char) (this.mAttrIdx - 1);
            char c2 = (char) (this.mAttrIdx << 3);
            this.mItems[c2 + 1] = pair.qname();
            this.mItems[c2 + 2] = this.mIsNSAware ? pair.name : "";
            this.mItems[c2 + 3] = str;
            this.mItems[c2 + 4] = strAtype;
            switch (pair.num & 3) {
                case 0:
                    this.mItems[c2 + 5] = null;
                    break;
                case 1:
                    this.mItems[c2 + 5] = PdfOps.d_TOKEN;
                    break;
                default:
                    this.mItems[c2 + 5] = PdfOps.D_TOKEN;
                    break;
            }
            this.mItems[c2 + 0] = pair.chars[0] != 0 ? rslv(pair.chars) : "";
        } else {
            newPrefix();
            attr(pair4);
        }
        del(pair4);
    }

    private String atype(Pair pair) throws Exception {
        Pair pairFind;
        pair.id = 99;
        if (this.mElm.list == null || (pairFind = find(this.mElm.list, pair.chars)) == null) {
            return "CDATA";
        }
        pair.num |= 1;
        pair.id = 105;
        switch (pairFind.id) {
            case 78:
                return SchemaSymbols.ATTVAL_ENTITIES;
            case 79:
            case 80:
            case 81:
            case 83:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
            case 106:
            case 107:
            case 108:
            case 109:
            case 112:
            case 113:
            case 115:
            default:
                panic("");
                return null;
            case 82:
                return SchemaSymbols.ATTVAL_IDREFS;
            case 84:
                return SchemaSymbols.ATTVAL_NMTOKENS;
            case 99:
                pair.id = 99;
                return "CDATA";
            case 105:
                return "ID";
            case 110:
                return SchemaSymbols.ATTVAL_ENTITY;
            case 111:
                return SchemaSymbols.ATTVAL_NOTATION;
            case 114:
                return SchemaSymbols.ATTVAL_IDREF;
            case 116:
                return SchemaSymbols.ATTVAL_NMTOKEN;
            case 117:
                return SchemaSymbols.ATTVAL_NMTOKEN;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v4 */
    private void comm() throws Exception {
        char chVar;
        if (this.mPh == 0) {
            this.mPh = 1;
        }
        this.mBuffIdx = -1;
        boolean z2 = false;
        while (z2 >= 0) {
            if (this.mChIdx < this.mChLen) {
                char[] cArr = this.mChars;
                int i2 = this.mChIdx;
                this.mChIdx = i2 + 1;
                chVar = cArr[i2];
            } else {
                chVar = getch();
            }
            char c2 = chVar;
            if (c2 == 65535) {
                panic("");
            }
            switch (z2) {
                case false:
                    if (c2 == '-') {
                        z2 = true;
                    } else {
                        panic("");
                        continue;
                    }
                case true:
                    if (c2 == '-') {
                        z2 = 2;
                    } else {
                        panic("");
                        continue;
                    }
                case true:
                    switch (c2) {
                        case '-':
                            z2 = 3;
                            break;
                        default:
                            bappend(c2);
                            continue;
                    }
                case true:
                    switch (c2) {
                        case '-':
                            z2 = 4;
                            break;
                        default:
                            bappend('-');
                            bappend(c2);
                            z2 = 2;
                            continue;
                    }
                case true:
                    if (c2 != '>') {
                        break;
                    } else {
                        comm(this.mBuff, this.mBuffIdx + 1);
                        z2 = -1;
                    }
            }
            panic("");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v3 */
    private void pi() throws Exception {
        String strName = null;
        this.mBuffIdx = -1;
        boolean z2 = false;
        while (z2 >= 0) {
            char chVar = getch();
            if (chVar == 65535) {
                panic("");
            }
            switch (z2) {
                case false:
                    switch (chtyp(chVar)) {
                        case ':':
                        case 'A':
                        case 'X':
                        case '_':
                        case 'a':
                            bkch();
                            strName = name(false);
                            if (strName.length() == 0 || this.mXml.name.equals(strName.toLowerCase())) {
                                panic("");
                            }
                            if (this.mPh == 0) {
                                this.mPh = 1;
                            }
                            wsskip();
                            z2 = true;
                            this.mBuffIdx = -1;
                            break;
                        default:
                            panic("");
                            break;
                    }
                case true:
                    switch (chVar) {
                        case '?':
                            z2 = 2;
                            break;
                        default:
                            bappend(chVar);
                            break;
                    }
                case true:
                    switch (chVar) {
                        case '>':
                            pi(strName, new String(this.mBuff, 0, this.mBuffIdx + 1));
                            z2 = -1;
                            break;
                        case '?':
                            bappend('?');
                            break;
                        default:
                            bappend('?');
                            bappend(chVar);
                            z2 = true;
                            break;
                    }
                default:
                    panic("");
                    break;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2 */
    private void cdat() throws Exception {
        this.mBuffIdx = -1;
        boolean z2 = false;
        while (z2 >= 0) {
            char chVar = getch();
            if (chVar == 65535) {
                panic("");
            }
            switch (z2) {
                case false:
                    if (chVar == '[') {
                        z2 = true;
                        break;
                    } else {
                        panic("");
                        break;
                    }
                case true:
                    if (chtyp(chVar) == 'A') {
                        bappend(chVar);
                        break;
                    } else {
                        if (!"CDATA".equals(new String(this.mBuff, 0, this.mBuffIdx + 1))) {
                            panic("");
                        }
                        bkch();
                        z2 = 2;
                        break;
                    }
                case true:
                    if (chVar != '[') {
                        panic("");
                    }
                    this.mBuffIdx = -1;
                    z2 = 3;
                    break;
                case true:
                    if (chVar != ']') {
                        bappend(chVar);
                        break;
                    } else {
                        z2 = 4;
                        break;
                    }
                case true:
                    if (chVar != ']') {
                        bappend(']');
                        bappend(chVar);
                        z2 = 3;
                        break;
                    } else {
                        z2 = 5;
                        break;
                    }
                case true:
                    switch (chVar) {
                        case '>':
                            bflash();
                            z2 = -1;
                            break;
                        case ']':
                            bappend(']');
                            break;
                        default:
                            bappend(']');
                            bappend(']');
                            bappend(chVar);
                            z2 = 3;
                            break;
                    }
                default:
                    panic("");
                    break;
            }
        }
    }

    protected String name(boolean z2) throws Exception {
        this.mBuffIdx = -1;
        bname(z2);
        return new String(this.mBuff, 1, this.mBuffIdx);
    }

    protected char[] qname(boolean z2) throws Exception {
        this.mBuffIdx = -1;
        bname(z2);
        char[] cArr = new char[this.mBuffIdx + 1];
        System.arraycopy(this.mBuff, 0, cArr, 0, this.mBuffIdx + 1);
        return cArr;
    }

    private void pubsys(Input input) throws Exception {
        Pair pairPubsys = pubsys(' ');
        input.pubid = pairPubsys.name;
        input.sysid = pairPubsys.value;
        del(pairPubsys);
    }

    private Pair pubsys(char c2) throws Exception {
        Pair pair = pair(null);
        String strName = name(false);
        if ("PUBLIC".equals(strName)) {
            bqstr('i');
            pair.name = new String(this.mBuff, 1, this.mBuffIdx);
            switch (wsskip()) {
                case '\"':
                case '\'':
                    bqstr(' ');
                    pair.value = new String(this.mBuff, 1, this.mBuffIdx);
                    break;
                case 65535:
                    panic("");
                default:
                    if (c2 != 'N') {
                        panic("");
                    }
                    pair.value = null;
                    break;
            }
            return pair;
        }
        if (Clipboard.SYSTEM.equals(strName)) {
            pair.name = null;
            bqstr(' ');
            pair.value = new String(this.mBuff, 1, this.mBuffIdx);
            return pair;
        }
        panic("");
        return null;
    }

    protected String eqstr(char c2) throws Exception {
        if (c2 == '=') {
            wsskip();
            if (getch() != '=') {
                panic("");
            }
        }
        bqstr(c2 == '=' ? '-' : c2);
        return new String(this.mBuff, 1, this.mBuffIdx);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v8 */
    private String ent(char c2) throws Exception {
        char chVar;
        int i2 = this.mBuffIdx + 1;
        String str = null;
        this.mESt = (char) 256;
        bappend('&');
        boolean z2 = false;
        while (z2 >= 0) {
            if (this.mChIdx < this.mChLen) {
                char[] cArr = this.mChars;
                int i3 = this.mChIdx;
                this.mChIdx = i3 + 1;
                chVar = cArr[i3];
            } else {
                chVar = getch();
            }
            char c3 = chVar;
            switch (z2) {
                case false:
                case true:
                    switch (chtyp(c3)) {
                        case '#':
                            if (z2) {
                                panic("");
                            }
                            z2 = 2;
                            continue;
                        case '-':
                        case '.':
                        case 'd':
                            if (!z2) {
                                panic("");
                                break;
                            }
                            break;
                        case ':':
                            if (this.mIsNSAware) {
                                panic("");
                            }
                            bappend(c3);
                            eappend(c3);
                            z2 = true;
                            continue;
                        case ';':
                            if (this.mESt < 256) {
                                this.mBuffIdx = i2 - 1;
                                bappend(this.mESt);
                                z2 = -1;
                                break;
                            } else if (this.mPh == 2) {
                                bappend(';');
                                z2 = -1;
                                break;
                            } else {
                                str = new String(this.mBuff, i2 + 1, this.mBuffIdx - i2);
                                Input input = this.mEnt.get(str);
                                this.mBuffIdx = i2 - 1;
                                if (input != null) {
                                    if (input.chars == null) {
                                        InputSource inputSourceResolveEnt = resolveEnt(str, input.pubid, input.sysid);
                                        if (inputSourceResolveEnt != null) {
                                            push(new Input(512));
                                            setinp(inputSourceResolveEnt);
                                            this.mInp.pubid = input.pubid;
                                            this.mInp.sysid = input.sysid;
                                            str = null;
                                        } else if (c2 != 'x') {
                                            panic("");
                                        }
                                    } else {
                                        push(input);
                                        str = null;
                                    }
                                } else if (c2 != 'x') {
                                    panic("");
                                }
                                z2 = -1;
                                continue;
                            }
                        case 'A':
                        case 'X':
                        case '_':
                        case 'a':
                            break;
                        default:
                            panic("");
                            continue;
                    }
                    bappend(c3);
                    eappend(c3);
                    z2 = true;
                    break;
                case true:
                    switch (chtyp(c3)) {
                        case ';':
                            try {
                                int i4 = Integer.parseInt(new String(this.mBuff, i2 + 1, this.mBuffIdx - i2), 10);
                                if (i4 >= 65535) {
                                    panic("");
                                }
                                c3 = (char) i4;
                            } catch (NumberFormatException e2) {
                                panic("");
                            }
                            this.mBuffIdx = i2 - 1;
                            if (c3 == ' ' || this.mInp.next != null) {
                                bappend(c3, c2);
                            } else {
                                bappend(c3);
                            }
                            z2 = -1;
                            continue;
                        case 'a':
                            if (this.mBuffIdx != i2 || c3 != 'x') {
                                break;
                            } else {
                                z2 = 3;
                                break;
                            }
                            break;
                        case 'd':
                            bappend(c3);
                            continue;
                    }
                    panic("");
                    break;
                case true:
                    switch (chtyp(c3)) {
                        case ';':
                            try {
                                int i5 = Integer.parseInt(new String(this.mBuff, i2 + 1, this.mBuffIdx - i2), 16);
                                if (i5 >= 65535) {
                                    panic("");
                                }
                                c3 = (char) i5;
                            } catch (NumberFormatException e3) {
                                panic("");
                            }
                            this.mBuffIdx = i2 - 1;
                            if (c3 == ' ' || this.mInp.next != null) {
                                bappend(c3, c2);
                            } else {
                                bappend(c3);
                            }
                            z2 = -1;
                            break;
                        case 'A':
                        case 'a':
                        case 'd':
                            bappend(c3);
                            break;
                        default:
                            panic("");
                            break;
                    }
                default:
                    panic("");
                    break;
            }
        }
        return str;
    }

    private void pent(char c2) throws Exception {
        int i2 = this.mBuffIdx + 1;
        bappend('%');
        if (this.mPh != 2) {
            return;
        }
        bname(false);
        String str = new String(this.mBuff, i2 + 2, (this.mBuffIdx - i2) - 1);
        if (getch() != ';') {
            panic("");
        }
        Input input = this.mPEnt.get(str);
        this.mBuffIdx = i2 - 1;
        if (input != null) {
            if (input.chars == null) {
                InputSource inputSourceResolveEnt = resolveEnt(str, input.pubid, input.sysid);
                if (inputSourceResolveEnt != null) {
                    if (c2 != '-') {
                        bappend(' ');
                    }
                    push(new Input(512));
                    setinp(inputSourceResolveEnt);
                    this.mInp.pubid = input.pubid;
                    this.mInp.sysid = input.sysid;
                    return;
                }
                skippedEnt(FXMLLoader.RESOURCE_KEY_PREFIX + str);
                return;
            }
            if (c2 == '-') {
                input.chIdx = 1;
            } else {
                bappend(' ');
                input.chIdx = 0;
            }
            push(input);
            return;
        }
        skippedEnt(FXMLLoader.RESOURCE_KEY_PREFIX + str);
    }

    private boolean isdecl(Pair pair, String str) {
        if (pair.chars[0] == 0) {
            if ("xmlns".equals(pair.name)) {
                this.mPref = pair(this.mPref);
                this.mPref.list = this.mElm;
                this.mPref.value = str;
                this.mPref.name = "";
                this.mPref.chars = NONS;
                this.mElm.num++;
                return true;
            }
            return false;
        }
        if (pair.eqpref(XMLNS)) {
            int length = pair.name.length();
            this.mPref = pair(this.mPref);
            this.mPref.list = this.mElm;
            this.mPref.value = str;
            this.mPref.name = pair.name;
            this.mPref.chars = new char[length + 1];
            this.mPref.chars[0] = (char) (length + 1);
            pair.name.getChars(0, length, this.mPref.chars, 1);
            this.mElm.num++;
            return true;
        }
        return false;
    }

    private String rslv(char[] cArr) throws Exception {
        Pair pair = this.mPref;
        while (true) {
            Pair pair2 = pair;
            if (pair2 != null) {
                if (!pair2.eqpref(cArr)) {
                    pair = pair2.next;
                } else {
                    return pair2.value;
                }
            } else {
                if (cArr[0] == 1) {
                    Pair pair3 = this.mPref;
                    while (true) {
                        Pair pair4 = pair3;
                        if (pair4 == null) {
                            break;
                        }
                        if (pair4.chars[0] != 0) {
                            pair3 = pair4.next;
                        } else {
                            return pair4.value;
                        }
                    }
                }
                panic("");
                return null;
            }
        }
    }

    protected char wsskip() throws IOException {
        char chVar;
        char c2;
        do {
            if (this.mChIdx < this.mChLen) {
                char[] cArr = this.mChars;
                int i2 = this.mChIdx;
                this.mChIdx = i2 + 1;
                chVar = cArr[i2];
            } else {
                chVar = getch();
            }
            c2 = chVar;
            if (c2 >= 128) {
                break;
            }
        } while (nmttyp[c2] == 3);
        this.mChIdx--;
        return c2;
    }

    private void bname(boolean z2) throws Exception {
        this.mBuffIdx++;
        int i2 = this.mBuffIdx;
        int i3 = i2;
        int i4 = i2 + 1;
        int i5 = i4;
        int i6 = this.mChIdx;
        short s2 = (short) (z2 ? 0 : 2);
        while (true) {
            if (this.mChIdx >= this.mChLen) {
                bcopy(i6, i5);
                getch();
                this.mChIdx--;
                i6 = this.mChIdx;
                i5 = i4;
            }
            char[] cArr = this.mChars;
            int i7 = this.mChIdx;
            this.mChIdx = i7 + 1;
            char c2 = cArr[i7];
            char c3 = 0;
            if (c2 < 128) {
                c3 = (char) nmttyp[c2];
            } else if (c2 == 65535) {
                panic("");
            }
            switch (s2) {
                case 0:
                case 2:
                    switch (c3) {
                        case 0:
                            i4++;
                            s2 = (short) (s2 + 1);
                            break;
                        case 1:
                            this.mChIdx--;
                            s2 = (short) (s2 + 1);
                            break;
                        default:
                            panic("");
                            break;
                    }
                case 1:
                case 3:
                    switch (c3) {
                        case 0:
                        case 2:
                            i4++;
                            break;
                        case 1:
                            i4++;
                            if (z2) {
                                if (i3 != i2) {
                                    panic("");
                                }
                                i3 = i4 - 1;
                                if (s2 != 1) {
                                    break;
                                } else {
                                    s2 = 2;
                                    break;
                                }
                            } else {
                                break;
                            }
                        default:
                            this.mChIdx--;
                            bcopy(i6, i5);
                            this.mBuff[i2] = (char) (i3 - i2);
                            return;
                    }
                default:
                    panic("");
                    break;
            }
        }
    }

    private void bntok() throws Exception {
        this.mBuffIdx = -1;
        bappend((char) 0);
        while (true) {
            char chVar = getch();
            switch (chtyp(chVar)) {
                case '-':
                case '.':
                case ':':
                case 'A':
                case 'X':
                case '_':
                case 'a':
                case 'd':
                    bappend(chVar);
                case 'Z':
                    panic("");
                    break;
            }
        }
        bkch();
    }

    private char bkeyword() throws Exception {
        String str = new String(this.mBuff, 1, this.mBuffIdx);
        switch (str.length()) {
            case 2:
                if ("ID".equals(str)) {
                }
                break;
            case 5:
                switch (this.mBuff[1]) {
                    case 'C':
                        if ("CDATA".equals(str)) {
                        }
                        break;
                    case 'F':
                        if ("FIXED".equals(str)) {
                        }
                        break;
                    case 'I':
                        if (SchemaSymbols.ATTVAL_IDREF.equals(str)) {
                        }
                        break;
                }
            case 6:
                switch (this.mBuff[1]) {
                    case 'E':
                        if (SchemaSymbols.ATTVAL_ENTITY.equals(str)) {
                        }
                        break;
                    case 'I':
                        if (SchemaSymbols.ATTVAL_IDREFS.equals(str)) {
                        }
                        break;
                }
            case 7:
                switch (this.mBuff[1]) {
                    case 'A':
                        if ("ATTLIST".equals(str)) {
                        }
                        break;
                    case 'E':
                        if ("ELEMENT".equals(str)) {
                        }
                        break;
                    case 'I':
                        if ("IMPLIED".equals(str)) {
                        }
                        break;
                    case 'N':
                        if (SchemaSymbols.ATTVAL_NMTOKEN.equals(str)) {
                        }
                        break;
                }
            case 8:
                switch (this.mBuff[2]) {
                    case 'E':
                        if ("REQUIRED".equals(str)) {
                        }
                        break;
                    case 'M':
                        if (SchemaSymbols.ATTVAL_NMTOKENS.equals(str)) {
                        }
                        break;
                    case 'N':
                        if (SchemaSymbols.ATTVAL_ENTITIES.equals(str)) {
                        }
                        break;
                    case 'O':
                        if (SchemaSymbols.ATTVAL_NOTATION.equals(str)) {
                        }
                        break;
                }
        }
        return '?';
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0196  */
    /* JADX WARN: Type inference failed for: r0v23 */
    /* JADX WARN: Type inference failed for: r0v49 */
    /* JADX WARN: Type inference failed for: r0v5 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void bqstr(char r7) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 462
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: jdk.internal.util.xml.impl.Parser.bqstr(char):void");
    }

    private void bappend(char c2, char c3) throws Exception {
        switch (c3) {
            case 'c':
                switch (c2) {
                    case '\t':
                    case '\n':
                    case '\r':
                        c2 = ' ';
                        break;
                }
            case 'i':
                switch (c2) {
                    case '\t':
                    case '\n':
                    case '\r':
                    case ' ':
                        if (this.mBuffIdx > 0 && this.mBuff[this.mBuffIdx] != ' ') {
                            bappend(' ');
                            break;
                        }
                        break;
                }
                return;
        }
        this.mBuffIdx++;
        if (this.mBuffIdx < this.mBuff.length) {
            this.mBuff[this.mBuffIdx] = c2;
        } else {
            this.mBuffIdx--;
            bappend(c2);
        }
    }

    private void bappend(char c2) throws Exception {
        int i2 = this.mBuffIdx + 1;
        this.mBuffIdx = i2;
        ensureCapacity(i2);
        this.mBuff[this.mBuffIdx] = c2;
    }

    private void bcopy(int i2, int i3) throws Exception {
        int i4 = this.mChIdx - i2;
        ensureCapacity(i3 + i4 + 1);
        System.arraycopy(this.mChars, i2, this.mBuff, i3, i4);
        this.mBuffIdx += i4;
    }

    private void eappend(char c2) {
        switch (this.mESt) {
            case '\"':
            case '&':
            case '\'':
            case '<':
            case '>':
                this.mESt = (char) 512;
                break;
            case 256:
                switch (c2) {
                    case 'a':
                        this.mESt = (char) 259;
                        break;
                    case 'g':
                        this.mESt = (char) 258;
                        break;
                    case 'l':
                        this.mESt = (char) 257;
                        break;
                    case 'q':
                        this.mESt = (char) 263;
                        break;
                    default:
                        this.mESt = (char) 512;
                        break;
                }
            case 257:
                this.mESt = c2 == 't' ? '<' : (char) 512;
                break;
            case 258:
                this.mESt = c2 == 't' ? '>' : (char) 512;
                break;
            case 259:
                switch (c2) {
                    case 'm':
                        this.mESt = (char) 260;
                        break;
                    case 'p':
                        this.mESt = (char) 261;
                        break;
                    default:
                        this.mESt = (char) 512;
                        break;
                }
            case 260:
                this.mESt = c2 == 'p' ? '&' : (char) 512;
                break;
            case 261:
                this.mESt = c2 == 'o' ? (char) 262 : (char) 512;
                break;
            case 262:
                this.mESt = c2 == 's' ? '\'' : (char) 512;
                break;
            case KeyEvent.VK_INPUT_METHOD_ON_OFF /* 263 */:
                this.mESt = c2 == 'u' ? (char) 264 : (char) 512;
                break;
            case 264:
                this.mESt = c2 == 'o' ? (char) 265 : (char) 512;
                break;
            case 265:
                this.mESt = c2 == 't' ? '\"' : (char) 512;
                break;
        }
    }

    protected void setinp(InputSource inputSource) throws Exception {
        Reader readerBom = null;
        this.mChIdx = 0;
        this.mChLen = 0;
        this.mChars = this.mInp.chars;
        this.mInp.src = null;
        if (this.mPh < 0) {
            this.mIsSAlone = false;
        }
        this.mIsSAloneSet = false;
        if (inputSource.getCharacterStream() != null) {
            readerBom = inputSource.getCharacterStream();
            xml(readerBom);
        } else if (inputSource.getByteStream() != null) {
            if (inputSource.getEncoding() != null) {
                String upperCase = inputSource.getEncoding().toUpperCase();
                if (upperCase.equals("UTF-16")) {
                    readerBom = bom(inputSource.getByteStream(), 'U');
                } else {
                    readerBom = enc(upperCase, inputSource.getByteStream());
                }
                xml(readerBom);
            } else {
                readerBom = bom(inputSource.getByteStream(), ' ');
                if (readerBom == null) {
                    String strXml = xml(enc("UTF-8", inputSource.getByteStream()));
                    if (strXml.startsWith("UTF-16")) {
                        panic("");
                    }
                    readerBom = enc(strXml, inputSource.getByteStream());
                } else {
                    xml(readerBom);
                }
            }
        } else {
            panic("");
        }
        this.mInp.src = readerBom;
        this.mInp.pubid = inputSource.getPublicId();
        this.mInp.sysid = inputSource.getSystemId();
    }

    private Reader bom(InputStream inputStream, char c2) throws Exception {
        int i2 = inputStream.read();
        switch (i2) {
            case -1:
                char[] cArr = this.mChars;
                int i3 = this.mChIdx;
                this.mChIdx = i3 + 1;
                cArr[i3] = 65535;
                return new ReaderUTF8(inputStream);
            case 239:
                if (c2 == 'U') {
                    panic("");
                }
                if (inputStream.read() != 187) {
                    panic("");
                }
                if (inputStream.read() != 191) {
                    panic("");
                }
                return new ReaderUTF8(inputStream);
            case 254:
                if (inputStream.read() != 255) {
                    panic("");
                }
                return new ReaderUTF16(inputStream, 'b');
            case 255:
                if (inputStream.read() != 254) {
                    panic("");
                }
                return new ReaderUTF16(inputStream, 'l');
            default:
                if (c2 == 'U') {
                    panic("");
                }
                switch (i2 & 240) {
                    case 192:
                    case 208:
                        char[] cArr2 = this.mChars;
                        int i4 = this.mChIdx;
                        this.mChIdx = i4 + 1;
                        cArr2[i4] = (char) (((i2 & 31) << 6) | (inputStream.read() & 63));
                        return null;
                    case 224:
                        char[] cArr3 = this.mChars;
                        int i5 = this.mChIdx;
                        this.mChIdx = i5 + 1;
                        cArr3[i5] = (char) (((i2 & 15) << 12) | ((inputStream.read() & 63) << 6) | (inputStream.read() & 63));
                        return null;
                    case 240:
                        throw new UnsupportedEncodingException();
                    default:
                        char[] cArr4 = this.mChars;
                        int i6 = this.mChIdx;
                        this.mChIdx = i6 + 1;
                        cArr4[i6] = (char) i2;
                        return null;
                }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v11 */
    /* JADX WARN: Type inference failed for: r0v54 */
    private String xml(Reader reader) throws Exception {
        short s2;
        String str = "UTF-8";
        if (this.mChIdx != 0) {
            s2 = (short) (this.mChars[0] == '<' ? 1 : -1);
        } else {
            s2 = 0;
        }
        while (s2 >= 0 && this.mChIdx < this.mChars.length) {
            int i2 = reader.read();
            char c2 = i2 >= 0 ? (char) i2 : (char) 65535;
            char[] cArr = this.mChars;
            int i3 = this.mChIdx;
            this.mChIdx = i3 + 1;
            cArr[i3] = c2;
            switch (s2) {
                case 0:
                    switch (c2) {
                        case '<':
                            s2 = 1;
                            break;
                        case 65279:
                            int i4 = reader.read();
                            char c3 = i4 >= 0 ? (char) i4 : (char) 65535;
                            this.mChars[this.mChIdx - 1] = c3;
                            s2 = (short) (c3 == '<' ? 1 : -1);
                            break;
                        default:
                            s2 = -1;
                            break;
                    }
                case 1:
                    s2 = (short) (c2 == '?' ? 2 : -1);
                    break;
                case 2:
                    s2 = (short) (c2 == 'x' ? 3 : -1);
                    break;
                case 3:
                    s2 = (short) (c2 == 'm' ? 4 : -1);
                    break;
                case 4:
                    s2 = (short) (c2 == 'l' ? 5 : -1);
                    break;
                case 5:
                    switch (c2) {
                        case '\t':
                        case '\n':
                        case '\r':
                        case ' ':
                            s2 = 6;
                            break;
                        default:
                            s2 = -1;
                            break;
                    }
                case 6:
                    switch (c2) {
                        case '?':
                            s2 = 7;
                            break;
                        case 65535:
                            s2 = -2;
                            break;
                    }
                case 7:
                    switch (c2) {
                        case '>':
                        case 65535:
                            s2 = -2;
                            break;
                        default:
                            s2 = 6;
                            break;
                    }
                default:
                    panic("");
                    break;
            }
        }
        this.mChLen = this.mChIdx;
        this.mChIdx = 0;
        if (s2 == -1) {
            return str;
        }
        this.mChIdx = 5;
        boolean z2 = false;
        while (z2 >= 0) {
            char chVar = getch();
            switch (z2) {
                case false:
                    if (chtyp(chVar) == ' ') {
                        break;
                    } else {
                        bkch();
                        z2 = true;
                        break;
                    }
                case true:
                case true:
                case true:
                    switch (chtyp(chVar)) {
                        case ' ':
                            break;
                        case '?':
                            if (z2) {
                                panic("");
                            }
                            bkch();
                            z2 = 4;
                            break;
                        case 'A':
                        case '_':
                        case 'a':
                            bkch();
                            String lowerCase = name(false).toLowerCase();
                            if ("version".equals(lowerCase)) {
                                if (!z2) {
                                    panic("");
                                }
                                if (!"1.0".equals(eqstr('='))) {
                                    panic("");
                                }
                                this.mInp.xmlver = (char) 256;
                                z2 = 2;
                                break;
                            } else if ("encoding".equals(lowerCase)) {
                                if (z2 != 2) {
                                    panic("");
                                }
                                this.mInp.xmlenc = eqstr('=').toUpperCase();
                                str = this.mInp.xmlenc;
                                z2 = 3;
                                break;
                            } else if ("standalone".equals(lowerCase)) {
                                if (z2 || this.mPh >= 0) {
                                    panic("");
                                }
                                String lowerCase2 = eqstr('=').toLowerCase();
                                if (lowerCase2.equals("yes")) {
                                    this.mIsSAlone = true;
                                } else if (lowerCase2.equals("no")) {
                                    this.mIsSAlone = false;
                                } else {
                                    panic("");
                                }
                                this.mIsSAloneSet = true;
                                z2 = 4;
                                break;
                            } else {
                                panic("");
                                break;
                            }
                            break;
                        default:
                            panic("");
                            break;
                    }
                case true:
                    switch (chtyp(chVar)) {
                        case ' ':
                            break;
                        case '?':
                            if (getch() != '>') {
                                panic("");
                            }
                            if (this.mPh <= 0) {
                                this.mPh = 1;
                            }
                            z2 = -1;
                            break;
                        default:
                            panic("");
                            break;
                    }
                default:
                    panic("");
                    break;
            }
        }
        return str;
    }

    private Reader enc(String str, InputStream inputStream) throws UnsupportedEncodingException {
        if (str.equals("UTF-8")) {
            return new ReaderUTF8(inputStream);
        }
        if (str.equals("UTF-16LE")) {
            return new ReaderUTF16(inputStream, 'l');
        }
        if (str.equals(FastInfosetSerializer.UTF_16BE)) {
            return new ReaderUTF16(inputStream, 'b');
        }
        return new InputStreamReader(inputStream, str);
    }

    protected void push(Input input) {
        this.mInp.chLen = this.mChLen;
        this.mInp.chIdx = this.mChIdx;
        input.next = this.mInp;
        this.mInp = input;
        this.mChars = input.chars;
        this.mChLen = input.chLen;
        this.mChIdx = input.chIdx;
    }

    protected void pop() {
        if (this.mInp.src != null) {
            try {
                this.mInp.src.close();
            } catch (IOException e2) {
            }
            this.mInp.src = null;
        }
        this.mInp = this.mInp.next;
        if (this.mInp != null) {
            this.mChars = this.mInp.chars;
            this.mChLen = this.mInp.chLen;
            this.mChIdx = this.mInp.chIdx;
        } else {
            this.mChars = null;
            this.mChLen = 0;
            this.mChIdx = 0;
        }
    }

    protected char chtyp(char c2) {
        if (c2 < 128) {
            return (char) asctyp[c2];
        }
        return c2 != 65535 ? 'X' : 'Z';
    }

    protected char getch() throws IOException {
        if (this.mChIdx >= this.mChLen) {
            if (this.mInp.src == null) {
                pop();
                return getch();
            }
            int i2 = this.mInp.src.read(this.mChars, 0, this.mChars.length);
            if (i2 < 0) {
                if (this.mInp != this.mDoc) {
                    pop();
                    return getch();
                }
                this.mChars[0] = 65535;
                this.mChLen = 1;
            } else {
                this.mChLen = i2;
            }
            this.mChIdx = 0;
        }
        char[] cArr = this.mChars;
        int i3 = this.mChIdx;
        this.mChIdx = i3 + 1;
        return cArr[i3];
    }

    protected void bkch() throws Exception {
        if (this.mChIdx <= 0) {
            panic("");
        }
        this.mChIdx--;
    }

    protected void setch(char c2) {
        this.mChars[this.mChIdx] = c2;
    }

    protected Pair find(Pair pair, char[] cArr) {
        Pair pair2 = pair;
        while (true) {
            Pair pair3 = pair2;
            if (pair3 != null) {
                if (!pair3.eqname(cArr)) {
                    pair2 = pair3.next;
                } else {
                    return pair3;
                }
            } else {
                return null;
            }
        }
    }

    protected Pair pair(Pair pair) {
        Pair pair2;
        if (this.mDltd != null) {
            pair2 = this.mDltd;
            this.mDltd = pair2.next;
        } else {
            pair2 = new Pair();
        }
        pair2.next = pair;
        return pair2;
    }

    protected Pair del(Pair pair) {
        Pair pair2 = pair.next;
        pair.name = null;
        pair.value = null;
        pair.chars = null;
        pair.list = null;
        pair.next = this.mDltd;
        this.mDltd = pair;
        return pair2;
    }

    private void ensureCapacity(int i2) throws Exception {
        if (this.mBuff == null) {
            this.mBuff = new char[i2 > 128 ? i2 + 128 : 128];
            return;
        }
        if (this.mBuff.length <= i2) {
            int length = this.mBuff.length << 1;
            int i3 = length > i2 ? length : i2 + 128;
            if (i3 < 0 || i3 > 67108864) {
                panic("");
            }
            this.mBuff = Arrays.copyOf(this.mBuff, i3);
        }
    }
}
