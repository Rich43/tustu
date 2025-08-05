package com.sun.org.apache.xpath.internal.compiler;

import com.sun.org.apache.xml.internal.utils.ObjectVector;
import com.sun.org.apache.xml.internal.utils.PrefixResolver;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.xml.internal.XMLSecurityManager;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/compiler/Lexer.class */
class Lexer {
    private Compiler m_compiler;
    PrefixResolver m_namespaceContext;
    XPathParser m_processor;
    static final int TARGETEXTRA = 10000;
    private int m_patternMapSize;
    XMLSecurityManager m_xmlSecMgr;
    private int m_opCountLimit;
    private int m_grpCountLimit;
    private int m_opCount;
    private int m_grpCount;
    private int[] m_patternMap = new int[100];
    private boolean isLiteral = false;

    Lexer(Compiler compiler, PrefixResolver resolver, XPathParser xpathProcessor, XMLSecurityManager xmlSecMgr) {
        this.m_compiler = compiler;
        this.m_namespaceContext = resolver;
        this.m_processor = xpathProcessor;
        this.m_xmlSecMgr = xmlSecMgr;
        this.m_opCountLimit = xmlSecMgr != null ? xmlSecMgr.getLimit(XMLSecurityManager.Limit.XPATH_OP_LIMIT) : 0;
        this.m_grpCountLimit = xmlSecMgr != null ? xmlSecMgr.getLimit(XMLSecurityManager.Limit.XPATH_GROUP_LIMIT) : 0;
    }

    void tokenize(String pat) throws TransformerException {
        tokenize(pat, null);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:127:0x043f A[PHI: r14
  0x043f: PHI (r14v3 'posOfNSSep' int) = (r14v1 'posOfNSSep' int), (r14v1 'posOfNSSep' int), (r14v4 'posOfNSSep' int) binds: [B:6:0x004c, B:117:0x03e9, B:126:0x043b] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:129:0x044a  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x0458  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x019c  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0225  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0297  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x02a8 A[PHI: r16 r17
  0x02a8: PHI (r16v3 'isAttrName' boolean) = (r16v1 'isAttrName' boolean), (r16v7 'isAttrName' boolean), (r16v7 'isAttrName' boolean) binds: [B:6:0x004c, B:55:0x0294, B:61:0x02a5] A[DONT_GENERATE, DONT_INLINE]
  0x02a8: PHI (r17v6 'isNum' boolean) = (r17v1 'isNum' boolean), (r17v1 'isNum' boolean), (r17v9 'isNum' boolean) binds: [B:6:0x004c, B:55:0x0294, B:61:0x02a5] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x02ae  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x02e8  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x032b  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0341  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void tokenize(java.lang.String r10, java.util.Vector r11) throws javax.xml.transform.TransformerException {
        /*
            Method dump skipped, instructions count: 1381
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xpath.internal.compiler.Lexer.tokenize(java.lang.String, java.util.Vector):void");
    }

    private char peekNext(String s2, int index) {
        if (index >= 0 && index < s2.length() - 1) {
            return s2.charAt(index + 1);
        }
        return (char) 0;
    }

    private boolean mapPatternElemPos(int nesting, boolean isStart, boolean isAttrName) {
        if (0 == nesting) {
            if (this.m_patternMapSize >= this.m_patternMap.length) {
                int[] patternMap = this.m_patternMap;
                int len = this.m_patternMap.length;
                this.m_patternMap = new int[this.m_patternMapSize + 100];
                System.arraycopy(patternMap, 0, this.m_patternMap, 0, len);
            }
            if (!isStart) {
                int[] iArr = this.m_patternMap;
                int i2 = this.m_patternMapSize - 1;
                iArr[i2] = iArr[i2] - 10000;
            }
            this.m_patternMap[this.m_patternMapSize] = (this.m_compiler.getTokenQueueSize() - (isAttrName ? 1 : 0)) + 10000;
            this.m_patternMapSize++;
            isStart = false;
        }
        return isStart;
    }

    private int getTokenQueuePosFromMap(int i2) {
        int pos = this.m_patternMap[i2];
        return pos >= 10000 ? pos - 10000 : pos;
    }

    private final void resetTokenMark(int mark) {
        int qsz = this.m_compiler.getTokenQueueSize();
        this.m_processor.m_queueMark = mark > 0 ? mark <= qsz ? mark - 1 : mark : 0;
        if (this.m_processor.m_queueMark < qsz) {
            XPathParser xPathParser = this.m_processor;
            ObjectVector tokenQueue = this.m_compiler.getTokenQueue();
            XPathParser xPathParser2 = this.m_processor;
            int i2 = xPathParser2.m_queueMark;
            xPathParser2.m_queueMark = i2 + 1;
            xPathParser.m_token = (String) tokenQueue.elementAt(i2);
            this.m_processor.m_tokenChar = this.m_processor.m_token.charAt(0);
            return;
        }
        this.m_processor.m_token = null;
        this.m_processor.m_tokenChar = (char) 0;
    }

    final int getKeywordToken(String key) {
        int tok;
        try {
            Integer itok = Keywords.getKeyWord(key);
            tok = null != itok ? itok.intValue() : 0;
        } catch (ClassCastException e2) {
            tok = 0;
        } catch (NullPointerException e3) {
            tok = 0;
        }
        return tok;
    }

    private void recordTokenString(Vector targetStrings) {
        int tokPos = getTokenQueuePosFromMap(this.m_patternMapSize - 1);
        resetTokenMark(tokPos + 1);
        if (this.m_processor.lookahead('(', 1)) {
            int tok = getKeywordToken(this.m_processor.m_token);
            switch (tok) {
                case 35:
                    targetStrings.addElement("/");
                    break;
                case 36:
                    targetStrings.addElement("*");
                    break;
                case OpCodes.NODETYPE_COMMENT /* 1030 */:
                    targetStrings.addElement(PsuedoNames.PSEUDONAME_COMMENT);
                    break;
                case OpCodes.NODETYPE_TEXT /* 1031 */:
                    targetStrings.addElement(PsuedoNames.PSEUDONAME_TEXT);
                    break;
                case OpCodes.NODETYPE_PI /* 1032 */:
                    targetStrings.addElement("*");
                    break;
                case 1033:
                    targetStrings.addElement("*");
                    break;
                default:
                    targetStrings.addElement("*");
                    break;
            }
        }
        if (this.m_processor.tokenIs('@')) {
            tokPos++;
            resetTokenMark(tokPos + 1);
        }
        if (this.m_processor.lookahead(':', 1)) {
            tokPos += 2;
        }
        targetStrings.addElement(this.m_compiler.getTokenQueue().elementAt(tokPos));
    }

    private final void addToTokenQueue(String s2) {
        this.m_compiler.getTokenQueue().addElement(s2);
    }

    private int mapNSTokens(String pat, int startSubstring, int posOfNSSep, int posOfScan) throws TransformerException {
        String uName;
        String prefix = "";
        if (startSubstring >= 0 && posOfNSSep >= 0) {
            prefix = pat.substring(startSubstring, posOfNSSep);
        }
        if (null != this.m_namespaceContext && !prefix.equals("*") && !prefix.equals("xmlns")) {
            try {
                if (prefix.length() > 0) {
                    uName = this.m_namespaceContext.getNamespaceForPrefix(prefix);
                } else {
                    uName = this.m_namespaceContext.getNamespaceForPrefix(prefix);
                }
            } catch (ClassCastException e2) {
                uName = this.m_namespaceContext.getNamespaceForPrefix(prefix);
            }
        } else {
            uName = prefix;
        }
        if (null != uName && uName.length() > 0) {
            addToTokenQueue(uName);
            addToTokenQueue(CallSiteDescriptor.TOKEN_DELIMITER);
            String s2 = pat.substring(posOfNSSep + 1, posOfScan);
            if (s2.length() > 0) {
                addToTokenQueue(s2);
                return -1;
            }
            return -1;
        }
        this.m_processor.errorForDOM3("ER_PREFIX_MUST_RESOLVE", new String[]{prefix});
        return -1;
    }
}
