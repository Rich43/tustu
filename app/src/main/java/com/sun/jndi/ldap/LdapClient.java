package com.sun.jndi.ldap;

import com.sun.jndi.ldap.pool.PoolCallback;
import com.sun.jndi.ldap.pool.PooledConnection;
import com.sun.jndi.ldap.sasl.LdapSasl;
import com.sun.jndi.ldap.sasl.SaslInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;
import javax.naming.AuthenticationException;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.CommunicationException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.InvalidAttributeValueException;
import javax.naming.ldap.Control;
import jdk.internal.dynalink.CallSiteDescriptor;
import net.lingala.zip4j.util.InternalZipConstants;
import org.icepdf.core.pobjects.graphics.Separation;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/jndi/ldap/LdapClient.class */
public final class LdapClient implements PooledConnection {
    private static final int debug = 0;
    static final boolean caseIgnore = true;
    private static final Hashtable<String, Boolean> defaultBinaryAttrs = new Hashtable<>(23, 0.75f);
    private static final String DISCONNECT_OID = "1.3.6.1.4.1.1466.20036";
    boolean isLdapv3;
    final Connection conn;
    private final PoolCallback pcb;
    private final boolean pooled;
    static final int SCOPE_BASE_OBJECT = 0;
    static final int SCOPE_ONE_LEVEL = 1;
    static final int SCOPE_SUBTREE = 2;
    static final int ADD = 0;
    static final int DELETE = 1;
    static final int REPLACE = 2;
    static final int LDAP_VERSION3_VERSION2 = 32;
    static final int LDAP_VERSION2 = 2;
    static final int LDAP_VERSION3 = 3;
    static final int LDAP_VERSION = 3;
    static final int LDAP_REF_FOLLOW = 1;
    static final int LDAP_REF_THROW = 2;
    static final int LDAP_REF_IGNORE = 3;
    static final int LDAP_REF_FOLLOW_SCHEME = 4;
    static final String LDAP_URL = "ldap://";
    static final String LDAPS_URL = "ldaps://";
    static final int LBER_BOOLEAN = 1;
    static final int LBER_INTEGER = 2;
    static final int LBER_BITSTRING = 3;
    static final int LBER_OCTETSTRING = 4;
    static final int LBER_NULL = 5;
    static final int LBER_ENUMERATED = 10;
    static final int LBER_SEQUENCE = 48;
    static final int LBER_SET = 49;
    static final int LDAP_SUPERIOR_DN = 128;
    static final int LDAP_REQ_BIND = 96;
    static final int LDAP_REQ_UNBIND = 66;
    static final int LDAP_REQ_SEARCH = 99;
    static final int LDAP_REQ_MODIFY = 102;
    static final int LDAP_REQ_ADD = 104;
    static final int LDAP_REQ_DELETE = 74;
    static final int LDAP_REQ_MODRDN = 108;
    static final int LDAP_REQ_COMPARE = 110;
    static final int LDAP_REQ_ABANDON = 80;
    static final int LDAP_REQ_EXTENSION = 119;
    static final int LDAP_REP_BIND = 97;
    static final int LDAP_REP_SEARCH = 100;
    static final int LDAP_REP_SEARCH_REF = 115;
    static final int LDAP_REP_RESULT = 101;
    static final int LDAP_REP_MODIFY = 103;
    static final int LDAP_REP_ADD = 105;
    static final int LDAP_REP_DELETE = 107;
    static final int LDAP_REP_MODRDN = 109;
    static final int LDAP_REP_COMPARE = 111;
    static final int LDAP_REP_EXTENSION = 120;
    static final int LDAP_REP_REFERRAL = 163;
    static final int LDAP_REP_EXT_OID = 138;
    static final int LDAP_REP_EXT_VAL = 139;
    static final int LDAP_CONTROLS = 160;
    static final String LDAP_CONTROL_MANAGE_DSA_IT = "2.16.840.1.113730.3.4.2";
    static final String LDAP_CONTROL_PREFERRED_LANG = "1.3.6.1.4.1.1466.20035";
    static final String LDAP_CONTROL_PAGED_RESULTS = "1.2.840.113556.1.4.319";
    static final String LDAP_CONTROL_SERVER_SORT_REQ = "1.2.840.113556.1.4.473";
    static final String LDAP_CONTROL_SERVER_SORT_RES = "1.2.840.113556.1.4.474";
    static final int LDAP_SUCCESS = 0;
    static final int LDAP_OPERATIONS_ERROR = 1;
    static final int LDAP_PROTOCOL_ERROR = 2;
    static final int LDAP_TIME_LIMIT_EXCEEDED = 3;
    static final int LDAP_SIZE_LIMIT_EXCEEDED = 4;
    static final int LDAP_COMPARE_FALSE = 5;
    static final int LDAP_COMPARE_TRUE = 6;
    static final int LDAP_AUTH_METHOD_NOT_SUPPORTED = 7;
    static final int LDAP_STRONG_AUTH_REQUIRED = 8;
    static final int LDAP_PARTIAL_RESULTS = 9;
    static final int LDAP_REFERRAL = 10;
    static final int LDAP_ADMIN_LIMIT_EXCEEDED = 11;
    static final int LDAP_UNAVAILABLE_CRITICAL_EXTENSION = 12;
    static final int LDAP_CONFIDENTIALITY_REQUIRED = 13;
    static final int LDAP_SASL_BIND_IN_PROGRESS = 14;
    static final int LDAP_NO_SUCH_ATTRIBUTE = 16;
    static final int LDAP_UNDEFINED_ATTRIBUTE_TYPE = 17;
    static final int LDAP_INAPPROPRIATE_MATCHING = 18;
    static final int LDAP_CONSTRAINT_VIOLATION = 19;
    static final int LDAP_ATTRIBUTE_OR_VALUE_EXISTS = 20;
    static final int LDAP_INVALID_ATTRIBUTE_SYNTAX = 21;
    static final int LDAP_NO_SUCH_OBJECT = 32;
    static final int LDAP_ALIAS_PROBLEM = 33;
    static final int LDAP_INVALID_DN_SYNTAX = 34;
    static final int LDAP_IS_LEAF = 35;
    static final int LDAP_ALIAS_DEREFERENCING_PROBLEM = 36;
    static final int LDAP_INAPPROPRIATE_AUTHENTICATION = 48;
    static final int LDAP_INVALID_CREDENTIALS = 49;
    static final int LDAP_INSUFFICIENT_ACCESS_RIGHTS = 50;
    static final int LDAP_BUSY = 51;
    static final int LDAP_UNAVAILABLE = 52;
    static final int LDAP_UNWILLING_TO_PERFORM = 53;
    static final int LDAP_LOOP_DETECT = 54;
    static final int LDAP_NAMING_VIOLATION = 64;
    static final int LDAP_OBJECT_CLASS_VIOLATION = 65;
    static final int LDAP_NOT_ALLOWED_ON_NON_LEAF = 66;
    static final int LDAP_NOT_ALLOWED_ON_RDN = 67;
    static final int LDAP_ENTRY_ALREADY_EXISTS = 68;
    static final int LDAP_OBJECT_CLASS_MODS_PROHIBITED = 69;
    static final int LDAP_AFFECTS_MULTIPLE_DSAS = 71;
    static final int LDAP_OTHER = 80;
    static final String[] ldap_error_message;
    int referenceCount = 1;
    private boolean authenticateCalled = false;
    private Vector<LdapCtx> unsolicited = new Vector<>(3);

    static {
        defaultBinaryAttrs.put("userpassword", Boolean.TRUE);
        defaultBinaryAttrs.put("javaserializeddata", Boolean.TRUE);
        defaultBinaryAttrs.put("javaserializedobject", Boolean.TRUE);
        defaultBinaryAttrs.put("jpegphoto", Boolean.TRUE);
        defaultBinaryAttrs.put("audio", Boolean.TRUE);
        defaultBinaryAttrs.put("thumbnailphoto", Boolean.TRUE);
        defaultBinaryAttrs.put("thumbnaillogo", Boolean.TRUE);
        defaultBinaryAttrs.put("usercertificate", Boolean.TRUE);
        defaultBinaryAttrs.put("cacertificate", Boolean.TRUE);
        defaultBinaryAttrs.put("certificaterevocationlist", Boolean.TRUE);
        defaultBinaryAttrs.put("authorityrevocationlist", Boolean.TRUE);
        defaultBinaryAttrs.put("crosscertificatepair", Boolean.TRUE);
        defaultBinaryAttrs.put("photo", Boolean.TRUE);
        defaultBinaryAttrs.put("personalsignature", Boolean.TRUE);
        defaultBinaryAttrs.put("x500uniqueidentifier", Boolean.TRUE);
        ldap_error_message = new String[]{"Success", "Operations Error", "Protocol Error", "Timelimit Exceeded", "Sizelimit Exceeded", "Compare False", "Compare True", "Authentication Method Not Supported", "Strong Authentication Required", null, "Referral", "Administrative Limit Exceeded", "Unavailable Critical Extension", "Confidentiality Required", "SASL Bind In Progress", null, "No Such Attribute", "Undefined Attribute Type", "Inappropriate Matching", "Constraint Violation", "Attribute Or Value Exists", "Invalid Attribute Syntax", null, null, null, null, null, null, null, null, null, null, "No Such Object", "Alias Problem", "Invalid DN Syntax", null, "Alias Dereferencing Problem", null, null, null, null, null, null, null, null, null, null, null, "Inappropriate Authentication", "Invalid Credentials", "Insufficient Access Rights", "Busy", "Unavailable", "Unwilling To Perform", "Loop Detect", null, null, null, null, null, null, null, null, null, "Naming Violation", "Object Class Violation", "Not Allowed On Non-leaf", "Not Allowed On RDN", "Entry Already Exists", "Object Class Modifications Prohibited", null, "Affects Multiple DSAs", null, null, null, null, null, null, null, null, "Other", null, null, null, null, null, null, null, null, null, null};
    }

    LdapClient(String str, int i2, String str2, int i3, int i4, OutputStream outputStream, PoolCallback poolCallback) throws NamingException {
        this.conn = new Connection(this, str, i2, str2, i3, i4, outputStream);
        this.pcb = poolCallback;
        this.pooled = poolCallback != null;
    }

    synchronized boolean authenticateCalled() {
        return this.authenticateCalled;
    }

    /* JADX WARN: Multi-variable type inference failed */
    synchronized LdapResult authenticate(boolean z2, String str, Object obj, int i2, String str2, Control[] controlArr, Hashtable<?, ?> hashtable) throws NamingException {
        byte[] bArr;
        LdapResult ldapResultLdapBind;
        int i3 = this.conn.readTimeout;
        this.conn.readTimeout = this.conn.connectTimeout;
        try {
            this.authenticateCalled = true;
            try {
                ensureOpen();
                switch (i2) {
                    case 2:
                        this.isLdapv3 = false;
                        break;
                    case 3:
                    case 32:
                        this.isLdapv3 = true;
                        break;
                    default:
                        throw new CommunicationException("Protocol version " + i2 + " not supported");
                }
                if (str2.equalsIgnoreCase(Separation.COLORANT_NONE) || str2.equalsIgnoreCase("anonymous")) {
                    if (!z2 || i2 == 2 || i2 == 32 || (controlArr != null && controlArr.length > 0)) {
                        try {
                            str = null;
                            bArr = null;
                            ldapResultLdapBind = ldapBind(null, (byte[]) null, controlArr, null, false);
                            if (ldapResultLdapBind.status == 0) {
                                this.conn.setBound();
                            }
                        } catch (IOException e2) {
                            CommunicationException communicationException = new CommunicationException("anonymous bind failed: " + this.conn.host + CallSiteDescriptor.TOKEN_DELIMITER + this.conn.port);
                            communicationException.setRootCause(e2);
                            throw communicationException;
                        }
                    } else {
                        ldapResultLdapBind = new LdapResult();
                        ldapResultLdapBind.status = 0;
                        bArr = obj;
                    }
                } else if (str2.equalsIgnoreCase("simple")) {
                    byte[] bArrEncodePassword = null;
                    try {
                        try {
                            bArrEncodePassword = encodePassword(obj, this.isLdapv3);
                            ldapResultLdapBind = ldapBind(str, bArrEncodePassword, controlArr, null, false);
                            if (ldapResultLdapBind.status == 0) {
                                this.conn.setBound();
                            }
                            if (bArrEncodePassword != obj && bArrEncodePassword != null) {
                                for (int i4 = 0; i4 < bArrEncodePassword.length; i4++) {
                                    bArrEncodePassword[i4] = 0;
                                }
                            }
                            bArr = obj;
                        } catch (Throwable th) {
                            if (bArrEncodePassword != obj && bArrEncodePassword != null) {
                                for (int i5 = 0; i5 < bArrEncodePassword.length; i5++) {
                                    bArrEncodePassword[i5] = 0;
                                }
                            }
                            throw th;
                        }
                    } catch (IOException e3) {
                        CommunicationException communicationException2 = new CommunicationException("simple bind failed: " + this.conn.host + CallSiteDescriptor.TOKEN_DELIMITER + this.conn.port);
                        communicationException2.setRootCause(e3);
                        throw communicationException2;
                    }
                } else {
                    if (!this.isLdapv3) {
                        throw new AuthenticationNotSupportedException(str2);
                    }
                    try {
                        ldapResultLdapBind = LdapSasl.saslBind(this, this.conn, this.conn.host, str, obj, str2, hashtable, controlArr);
                        if (ldapResultLdapBind.status == 0) {
                            this.conn.setBound();
                        }
                        bArr = obj;
                    } catch (IOException e4) {
                        CommunicationException communicationException3 = new CommunicationException("SASL bind failed: " + this.conn.host + CallSiteDescriptor.TOKEN_DELIMITER + this.conn.port);
                        communicationException3.setRootCause(e4);
                        throw communicationException3;
                    }
                }
                if (z2 && ldapResultLdapBind.status == 2 && i2 == 32 && (str2.equalsIgnoreCase(Separation.COLORANT_NONE) || str2.equalsIgnoreCase("anonymous") || str2.equalsIgnoreCase("simple"))) {
                    Object[] objArr = null;
                    boolean z3 = false;
                    try {
                        try {
                            this.isLdapv3 = false;
                            byte[] bArrEncodePassword2 = encodePassword(bArr, false);
                            ldapResultLdapBind = ldapBind(str, bArrEncodePassword2, controlArr, null, false);
                            if (ldapResultLdapBind.status == 0) {
                                this.conn.setBound();
                            }
                            if (bArrEncodePassword2 != bArr && bArrEncodePassword2 != null) {
                                for (int i6 = 0; i6 < bArrEncodePassword2.length; i6++) {
                                    bArrEncodePassword2[i6] = 0;
                                }
                            }
                        } catch (IOException e5) {
                            CommunicationException communicationException4 = new CommunicationException(str2 + CallSiteDescriptor.TOKEN_DELIMITER + this.conn.host + CallSiteDescriptor.TOKEN_DELIMITER + this.conn.port);
                            communicationException4.setRootCause(e5);
                            throw communicationException4;
                        }
                    } catch (Throwable th2) {
                        if (false != bArr && 0 != 0) {
                            for (int i7 = 0; i7 < objArr.length; i7++) {
                                (z3 ? 1 : 0)[i7] = 0;
                            }
                        }
                        throw th2;
                    }
                }
                if (ldapResultLdapBind.status == 32) {
                    throw new AuthenticationException(getErrorMessage(ldapResultLdapBind.status, ldapResultLdapBind.errorMessage));
                }
                this.conn.setV3(this.isLdapv3);
                LdapResult ldapResult = ldapResultLdapBind;
                this.conn.readTimeout = i3;
                return ldapResult;
            } catch (IOException e6) {
                CommunicationException communicationException5 = new CommunicationException();
                communicationException5.setRootCause(e6);
                throw communicationException5;
            }
        } catch (Throwable th3) {
            this.conn.readTimeout = i3;
            throw th3;
        }
    }

    public synchronized LdapResult ldapBind(String str, byte[] bArr, Control[] controlArr, String str2, boolean z2) throws IOException, NamingException {
        ensureOpen();
        this.conn.abandonOutstandingReqs(null);
        BerEncoder berEncoder = new BerEncoder();
        int msgId = this.conn.getMsgId();
        LdapResult ldapResult = new LdapResult();
        ldapResult.status = 1;
        berEncoder.beginSeq(48);
        berEncoder.encodeInt(msgId);
        berEncoder.beginSeq(96);
        berEncoder.encodeInt(this.isLdapv3 ? 3 : 2);
        berEncoder.encodeString(str, this.isLdapv3);
        if (str2 != null) {
            berEncoder.beginSeq(163);
            berEncoder.encodeString(str2, this.isLdapv3);
            if (bArr != null) {
                berEncoder.encodeOctetString(bArr, 4);
            }
            berEncoder.endSeq();
        } else if (bArr != null) {
            berEncoder.encodeOctetString(bArr, 128);
        } else {
            berEncoder.encodeOctetString(null, 128, 0, 0);
        }
        berEncoder.endSeq();
        if (this.isLdapv3) {
            encodeControls(berEncoder, controlArr);
        }
        berEncoder.endSeq();
        LdapRequest ldapRequestWriteRequest = this.conn.writeRequest(berEncoder, msgId, z2);
        if (bArr != null) {
            berEncoder.reset();
        }
        BerDecoder reply = this.conn.readReply(ldapRequestWriteRequest);
        reply.parseSeq(null);
        reply.parseInt();
        if (reply.parseByte() != 97) {
            return ldapResult;
        }
        reply.parseLength();
        parseResult(reply, ldapResult, this.isLdapv3);
        if (this.isLdapv3 && reply.bytesLeft() > 0 && reply.peekByte() == 135) {
            ldapResult.serverCreds = reply.parseOctetString(135, null);
        }
        ldapResult.resControls = this.isLdapv3 ? parseControls(reply) : null;
        this.conn.removeRequest(ldapRequestWriteRequest);
        return ldapResult;
    }

    boolean usingSaslStreams() {
        return this.conn.inStream instanceof SaslInputStream;
    }

    boolean isUpgradedToStartTls() {
        return this.conn.isUpgradedToStartTls();
    }

    synchronized void incRefCount() {
        this.referenceCount++;
    }

    private static byte[] encodePassword(Object obj, boolean z2) throws IOException {
        if (obj instanceof char[]) {
            obj = new String((char[]) obj);
        }
        if (obj instanceof String) {
            if (z2) {
                return ((String) obj).getBytes(InternalZipConstants.CHARSET_UTF8);
            }
            return ((String) obj).getBytes("8859_1");
        }
        return (byte[]) obj;
    }

    synchronized void close(Control[] controlArr, boolean z2) {
        this.referenceCount--;
        if (this.referenceCount <= 0) {
            if (!this.pooled) {
                this.conn.cleanup(controlArr, false);
            } else if (z2) {
                this.conn.cleanup(controlArr, false);
                this.pcb.removePooledConnection(this);
            } else {
                this.pcb.releasePooledConnection(this);
            }
        }
    }

    private void forceClose(boolean z2) {
        this.referenceCount = 0;
        this.conn.cleanup(null, false);
        if (z2) {
            this.pcb.removePooledConnection(this);
        }
    }

    protected void finalize() {
        forceClose(this.pooled);
    }

    @Override // com.sun.jndi.ldap.pool.PooledConnection
    public synchronized void closeConnection() {
        forceClose(false);
    }

    void processConnectionClosure() {
        String str;
        if (this.unsolicited.size() > 0) {
            if (this.conn != null) {
                str = this.conn.host + CallSiteDescriptor.TOKEN_DELIMITER + this.conn.port + " connection closed";
            } else {
                str = "Connection closed";
            }
            notifyUnsolicited(new CommunicationException(str));
        }
        if (this.pooled) {
            this.pcb.removePooledConnection(this);
        }
    }

    LdapResult search(String str, int i2, int i3, int i4, int i5, boolean z2, String[] strArr, String str2, int i6, Control[] controlArr, Hashtable<String, Boolean> hashtable, boolean z3, int i7) throws IOException, NamingException {
        ensureOpen();
        LdapResult ldapResult = new LdapResult();
        BerEncoder berEncoder = new BerEncoder();
        int msgId = this.conn.getMsgId();
        berEncoder.beginSeq(48);
        berEncoder.encodeInt(msgId);
        berEncoder.beginSeq(99);
        berEncoder.encodeString(str == null ? "" : str, this.isLdapv3);
        berEncoder.encodeInt(i2, 10);
        berEncoder.encodeInt(i3, 10);
        berEncoder.encodeInt(i4);
        berEncoder.encodeInt(i5);
        berEncoder.encodeBoolean(z2);
        Filter.encodeFilterString(berEncoder, str2, this.isLdapv3);
        berEncoder.beginSeq(48);
        berEncoder.encodeStringArray(strArr, this.isLdapv3);
        berEncoder.endSeq();
        berEncoder.endSeq();
        if (this.isLdapv3) {
            encodeControls(berEncoder, controlArr);
        }
        berEncoder.endSeq();
        LdapRequest ldapRequestWriteRequest = this.conn.writeRequest(berEncoder, msgId, false, i7);
        ldapResult.msgId = msgId;
        ldapResult.status = 0;
        if (z3) {
            ldapResult = getSearchReply(ldapRequestWriteRequest, i6, ldapResult, hashtable);
        }
        return ldapResult;
    }

    void clearSearchReply(LdapResult ldapResult, Control[] controlArr) {
        LdapRequest ldapRequestFindRequest;
        if (ldapResult == null || (ldapRequestFindRequest = this.conn.findRequest(ldapResult.msgId)) == null) {
            return;
        }
        if (ldapRequestFindRequest.hasSearchCompleted()) {
            this.conn.removeRequest(ldapRequestFindRequest);
        } else {
            this.conn.abandonRequest(ldapRequestFindRequest, controlArr);
        }
    }

    LdapResult getSearchReply(int i2, LdapResult ldapResult, Hashtable<String, Boolean> hashtable) throws IOException, NamingException {
        ensureOpen();
        LdapRequest ldapRequestFindRequest = this.conn.findRequest(ldapResult.msgId);
        if (ldapRequestFindRequest == null) {
            return null;
        }
        return getSearchReply(ldapRequestFindRequest, i2, ldapResult, hashtable);
    }

    private LdapResult getSearchReply(LdapRequest ldapRequest, int i2, LdapResult ldapResult, Hashtable<String, Boolean> hashtable) throws IOException, NamingException {
        if (i2 == 0) {
            i2 = Integer.MAX_VALUE;
        }
        if (ldapResult.entries != null) {
            ldapResult.entries.setSize(0);
        } else {
            ldapResult.entries = new Vector<>(i2 == Integer.MAX_VALUE ? 32 : i2);
        }
        if (ldapResult.referrals != null) {
            ldapResult.referrals.setSize(0);
        }
        int i3 = 0;
        while (i3 < i2) {
            BerDecoder reply = this.conn.readReply(ldapRequest);
            reply.parseSeq(null);
            reply.parseInt();
            int seq = reply.parseSeq(null);
            if (seq == 100) {
                BasicAttributes basicAttributes = new BasicAttributes(true);
                LdapEntry ldapEntry = new LdapEntry(reply.parseString(this.isLdapv3), basicAttributes);
                int[] iArr = new int[1];
                reply.parseSeq(iArr);
                int parsePosition = reply.getParsePosition() + iArr[0];
                while (reply.getParsePosition() < parsePosition && reply.bytesLeft() > 0) {
                    basicAttributes.put(parseAttribute(reply, hashtable));
                }
                ldapEntry.respCtls = this.isLdapv3 ? parseControls(reply) : null;
                ldapResult.entries.addElement(ldapEntry);
                i3++;
            } else if (seq == 115 && this.isLdapv3) {
                Vector<String> vector = new Vector<>(4);
                if (reply.peekByte() == 48) {
                    reply.parseSeq(null);
                }
                while (reply.bytesLeft() > 0 && reply.peekByte() == 4) {
                    vector.addElement(reply.parseString(this.isLdapv3));
                }
                if (ldapResult.referrals == null) {
                    ldapResult.referrals = new Vector<>(4);
                }
                ldapResult.referrals.addElement(vector);
                ldapResult.resControls = this.isLdapv3 ? parseControls(reply) : null;
            } else if (seq == 120) {
                parseExtResponse(reply, ldapResult);
            } else if (seq == 101) {
                parseResult(reply, ldapResult, this.isLdapv3);
                ldapResult.resControls = this.isLdapv3 ? parseControls(reply) : null;
                this.conn.removeRequest(ldapRequest);
                return ldapResult;
            }
        }
        return ldapResult;
    }

    private Attribute parseAttribute(BerDecoder berDecoder, Hashtable<String, Boolean> hashtable) throws IOException {
        int[] iArr = new int[1];
        berDecoder.parseSeq(null);
        String string = berDecoder.parseString(this.isLdapv3);
        boolean zIsBinaryValued = isBinaryValued(string, hashtable);
        LdapAttribute ldapAttribute = new LdapAttribute(string);
        if (berDecoder.parseSeq(iArr) == 49) {
            int attributeValue = iArr[0];
            while (berDecoder.bytesLeft() > 0 && attributeValue > 0) {
                try {
                    attributeValue -= parseAttributeValue(berDecoder, ldapAttribute, zIsBinaryValued);
                } catch (IOException e2) {
                    berDecoder.seek(attributeValue);
                }
            }
        } else {
            berDecoder.seek(iArr[0]);
        }
        return ldapAttribute;
    }

    private int parseAttributeValue(BerDecoder berDecoder, Attribute attribute, boolean z2) throws IOException {
        int[] iArr = new int[1];
        if (z2) {
            attribute.add(berDecoder.parseOctetString(berDecoder.peekByte(), iArr));
        } else {
            attribute.add(berDecoder.parseStringWithTag(4, this.isLdapv3, iArr));
        }
        return iArr[0];
    }

    private boolean isBinaryValued(String str, Hashtable<String, Boolean> hashtable) {
        String lowerCase = str.toLowerCase(Locale.ENGLISH);
        return lowerCase.indexOf(";binary") != -1 || defaultBinaryAttrs.containsKey(lowerCase) || (hashtable != null && hashtable.containsKey(lowerCase));
    }

    static void parseResult(BerDecoder berDecoder, LdapResult ldapResult, boolean z2) throws IOException {
        ldapResult.status = berDecoder.parseEnumeration();
        ldapResult.matchedDN = berDecoder.parseString(z2);
        ldapResult.errorMessage = berDecoder.parseString(z2);
        if (z2 && berDecoder.bytesLeft() > 0 && berDecoder.peekByte() == 163) {
            Vector<String> vector = new Vector<>(4);
            int[] iArr = new int[1];
            berDecoder.parseSeq(iArr);
            int parsePosition = berDecoder.getParsePosition() + iArr[0];
            while (berDecoder.getParsePosition() < parsePosition && berDecoder.bytesLeft() > 0) {
                vector.addElement(berDecoder.parseString(z2));
            }
            if (ldapResult.referrals == null) {
                ldapResult.referrals = new Vector<>(4);
            }
            ldapResult.referrals.addElement(vector);
        }
    }

    static Vector<Control> parseControls(BerDecoder berDecoder) throws IOException {
        if (berDecoder.bytesLeft() > 0 && berDecoder.peekByte() == 160) {
            Vector<Control> vector = new Vector<>(4);
            boolean z2 = false;
            byte[] octetString = null;
            int[] iArr = new int[1];
            berDecoder.parseSeq(iArr);
            int parsePosition = berDecoder.getParsePosition() + iArr[0];
            while (berDecoder.getParsePosition() < parsePosition && berDecoder.bytesLeft() > 0) {
                berDecoder.parseSeq(null);
                String string = berDecoder.parseString(true);
                if (berDecoder.bytesLeft() > 0 && berDecoder.peekByte() == 1) {
                    z2 = berDecoder.parseBoolean();
                }
                if (berDecoder.bytesLeft() > 0 && berDecoder.peekByte() == 4) {
                    octetString = berDecoder.parseOctetString(4, null);
                }
                if (string != null) {
                    vector.addElement(new BasicControl(string, z2, octetString));
                }
            }
            return vector;
        }
        return null;
    }

    private void parseExtResponse(BerDecoder berDecoder, LdapResult ldapResult) throws IOException {
        parseResult(berDecoder, ldapResult, this.isLdapv3);
        if (berDecoder.bytesLeft() > 0 && berDecoder.peekByte() == 138) {
            ldapResult.extensionId = berDecoder.parseStringWithTag(138, this.isLdapv3, null);
        }
        if (berDecoder.bytesLeft() > 0 && berDecoder.peekByte() == 139) {
            ldapResult.extensionValue = berDecoder.parseOctetString(139, null);
        }
        ldapResult.resControls = parseControls(berDecoder);
    }

    static void encodeControls(BerEncoder berEncoder, Control[] controlArr) throws IOException {
        if (controlArr == null || controlArr.length == 0) {
            return;
        }
        berEncoder.beginSeq(160);
        for (int i2 = 0; i2 < controlArr.length; i2++) {
            berEncoder.beginSeq(48);
            berEncoder.encodeString(controlArr[i2].getID(), true);
            if (controlArr[i2].isCritical()) {
                berEncoder.encodeBoolean(true);
            }
            byte[] encodedValue = controlArr[i2].getEncodedValue();
            if (encodedValue != null) {
                berEncoder.encodeOctetString(encodedValue, 4);
            }
            berEncoder.endSeq();
        }
        berEncoder.endSeq();
    }

    private LdapResult processReply(LdapRequest ldapRequest, LdapResult ldapResult, int i2) throws IOException, NamingException {
        BerDecoder reply = this.conn.readReply(ldapRequest);
        reply.parseSeq(null);
        reply.parseInt();
        if (reply.parseByte() != i2) {
            return ldapResult;
        }
        reply.parseLength();
        parseResult(reply, ldapResult, this.isLdapv3);
        ldapResult.resControls = this.isLdapv3 ? parseControls(reply) : null;
        this.conn.removeRequest(ldapRequest);
        return ldapResult;
    }

    LdapResult modify(String str, int[] iArr, Attribute[] attributeArr, Control[] controlArr) throws IOException, NamingException {
        ensureOpen();
        LdapResult ldapResult = new LdapResult();
        ldapResult.status = 1;
        if (str == null || iArr.length != attributeArr.length) {
            return ldapResult;
        }
        BerEncoder berEncoder = new BerEncoder();
        int msgId = this.conn.getMsgId();
        berEncoder.beginSeq(48);
        berEncoder.encodeInt(msgId);
        berEncoder.beginSeq(102);
        berEncoder.encodeString(str, this.isLdapv3);
        berEncoder.beginSeq(48);
        for (int i2 = 0; i2 < iArr.length; i2++) {
            berEncoder.beginSeq(48);
            berEncoder.encodeInt(iArr[i2], 10);
            if (iArr[i2] == 0 && hasNoValue(attributeArr[i2])) {
                throw new InvalidAttributeValueException(PdfOps.SINGLE_QUOTE_TOKEN + attributeArr[i2].getID() + "' has no values.");
            }
            encodeAttribute(berEncoder, attributeArr[i2]);
            berEncoder.endSeq();
        }
        berEncoder.endSeq();
        berEncoder.endSeq();
        if (this.isLdapv3) {
            encodeControls(berEncoder, controlArr);
        }
        berEncoder.endSeq();
        return processReply(this.conn.writeRequest(berEncoder, msgId), ldapResult, 103);
    }

    private void encodeAttribute(BerEncoder berEncoder, Attribute attribute) throws IOException, NamingException {
        berEncoder.beginSeq(48);
        berEncoder.encodeString(attribute.getID(), this.isLdapv3);
        berEncoder.beginSeq(49);
        NamingEnumeration<?> all = attribute.getAll();
        while (all.hasMore()) {
            Object next = all.next();
            if (next instanceof String) {
                berEncoder.encodeString((String) next, this.isLdapv3);
            } else if (next instanceof byte[]) {
                berEncoder.encodeOctetString((byte[]) next, 4);
            } else if (next != null) {
                throw new InvalidAttributeValueException("Malformed '" + attribute.getID() + "' attribute value");
            }
        }
        berEncoder.endSeq();
        berEncoder.endSeq();
    }

    private static boolean hasNoValue(Attribute attribute) throws NamingException {
        return attribute.size() == 0 || (attribute.size() == 1 && attribute.get() == null);
    }

    LdapResult add(LdapEntry ldapEntry, Control[] controlArr) throws IOException, NamingException {
        ensureOpen();
        LdapResult ldapResult = new LdapResult();
        ldapResult.status = 1;
        if (ldapEntry == null || ldapEntry.DN == null) {
            return ldapResult;
        }
        BerEncoder berEncoder = new BerEncoder();
        int msgId = this.conn.getMsgId();
        berEncoder.beginSeq(48);
        berEncoder.encodeInt(msgId);
        berEncoder.beginSeq(104);
        berEncoder.encodeString(ldapEntry.DN, this.isLdapv3);
        berEncoder.beginSeq(48);
        NamingEnumeration<? extends Attribute> all = ldapEntry.attributes.getAll();
        while (all.hasMore()) {
            Attribute next = all.next();
            if (hasNoValue(next)) {
                throw new InvalidAttributeValueException(PdfOps.SINGLE_QUOTE_TOKEN + next.getID() + "' has no values.");
            }
            encodeAttribute(berEncoder, next);
        }
        berEncoder.endSeq();
        berEncoder.endSeq();
        if (this.isLdapv3) {
            encodeControls(berEncoder, controlArr);
        }
        berEncoder.endSeq();
        return processReply(this.conn.writeRequest(berEncoder, msgId), ldapResult, 105);
    }

    LdapResult delete(String str, Control[] controlArr) throws IOException, NamingException {
        ensureOpen();
        LdapResult ldapResult = new LdapResult();
        ldapResult.status = 1;
        if (str == null) {
            return ldapResult;
        }
        BerEncoder berEncoder = new BerEncoder();
        int msgId = this.conn.getMsgId();
        berEncoder.beginSeq(48);
        berEncoder.encodeInt(msgId);
        berEncoder.encodeString(str, 74, this.isLdapv3);
        if (this.isLdapv3) {
            encodeControls(berEncoder, controlArr);
        }
        berEncoder.endSeq();
        return processReply(this.conn.writeRequest(berEncoder, msgId), ldapResult, 107);
    }

    LdapResult moddn(String str, String str2, boolean z2, String str3, Control[] controlArr) throws IOException, NamingException {
        ensureOpen();
        boolean z3 = str3 != null && str3.length() > 0;
        LdapResult ldapResult = new LdapResult();
        ldapResult.status = 1;
        if (str == null || str2 == null) {
            return ldapResult;
        }
        BerEncoder berEncoder = new BerEncoder();
        int msgId = this.conn.getMsgId();
        berEncoder.beginSeq(48);
        berEncoder.encodeInt(msgId);
        berEncoder.beginSeq(108);
        berEncoder.encodeString(str, this.isLdapv3);
        berEncoder.encodeString(str2, this.isLdapv3);
        berEncoder.encodeBoolean(z2);
        if (this.isLdapv3 && z3) {
            berEncoder.encodeString(str3, 128, this.isLdapv3);
        }
        berEncoder.endSeq();
        if (this.isLdapv3) {
            encodeControls(berEncoder, controlArr);
        }
        berEncoder.endSeq();
        return processReply(this.conn.writeRequest(berEncoder, msgId), ldapResult, 109);
    }

    LdapResult compare(String str, String str2, String str3, Control[] controlArr) throws IOException, NamingException {
        ensureOpen();
        LdapResult ldapResult = new LdapResult();
        ldapResult.status = 1;
        if (str == null || str2 == null || str3 == null) {
            return ldapResult;
        }
        BerEncoder berEncoder = new BerEncoder();
        int msgId = this.conn.getMsgId();
        berEncoder.beginSeq(48);
        berEncoder.encodeInt(msgId);
        berEncoder.beginSeq(110);
        berEncoder.encodeString(str, this.isLdapv3);
        berEncoder.beginSeq(48);
        berEncoder.encodeString(str2, this.isLdapv3);
        byte[] bytes = this.isLdapv3 ? str3.getBytes(InternalZipConstants.CHARSET_UTF8) : str3.getBytes("8859_1");
        berEncoder.encodeOctetString(Filter.unescapeFilterValue(bytes, 0, bytes.length), 4);
        berEncoder.endSeq();
        berEncoder.endSeq();
        if (this.isLdapv3) {
            encodeControls(berEncoder, controlArr);
        }
        berEncoder.endSeq();
        return processReply(this.conn.writeRequest(berEncoder, msgId), ldapResult, 111);
    }

    LdapResult extendedOp(String str, byte[] bArr, Control[] controlArr, boolean z2) throws IOException, NamingException {
        ensureOpen();
        LdapResult ldapResult = new LdapResult();
        ldapResult.status = 1;
        if (str == null) {
            return ldapResult;
        }
        BerEncoder berEncoder = new BerEncoder();
        int msgId = this.conn.getMsgId();
        berEncoder.beginSeq(48);
        berEncoder.encodeInt(msgId);
        berEncoder.beginSeq(119);
        berEncoder.encodeString(str, 128, this.isLdapv3);
        if (bArr != null) {
            berEncoder.encodeOctetString(bArr, 129);
        }
        berEncoder.endSeq();
        encodeControls(berEncoder, controlArr);
        berEncoder.endSeq();
        LdapRequest ldapRequestWriteRequest = this.conn.writeRequest(berEncoder, msgId, z2);
        BerDecoder reply = this.conn.readReply(ldapRequestWriteRequest);
        reply.parseSeq(null);
        reply.parseInt();
        if (reply.parseByte() != 120) {
            return ldapResult;
        }
        reply.parseLength();
        parseExtResponse(reply, ldapResult);
        this.conn.removeRequest(ldapRequestWriteRequest);
        return ldapResult;
    }

    static String getErrorMessage(int i2, String str) {
        String str2 = "[LDAP: error code " + i2;
        if (str != null && str.length() != 0) {
            str2 = str2 + " - " + str + "]";
        } else {
            try {
                if (ldap_error_message[i2] != null) {
                    str2 = str2 + " - " + ldap_error_message[i2] + "]";
                }
            } catch (ArrayIndexOutOfBoundsException e2) {
                str2 = str2 + "]";
            }
        }
        return str2;
    }

    void addUnsolicited(LdapCtx ldapCtx) {
        this.unsolicited.addElement(ldapCtx);
    }

    void removeUnsolicited(LdapCtx ldapCtx) {
        this.unsolicited.removeElement(ldapCtx);
    }

    void processUnsolicited(BerDecoder berDecoder) throws IOException {
        try {
            LdapResult ldapResult = new LdapResult();
            berDecoder.parseSeq(null);
            berDecoder.parseInt();
            if (berDecoder.parseByte() != 120) {
                throw new IOException("Unsolicited Notification must be an Extended Response");
            }
            berDecoder.parseLength();
            parseExtResponse(berDecoder, ldapResult);
            if (DISCONNECT_OID.equals(ldapResult.extensionId)) {
                forceClose(this.pooled);
            }
            Object unsolicitedResponseImpl = null;
            synchronized (this.unsolicited) {
                if (this.unsolicited.size() > 0) {
                    unsolicitedResponseImpl = new UnsolicitedResponseImpl(ldapResult.extensionId, ldapResult.extensionValue, ldapResult.referrals, ldapResult.status, ldapResult.errorMessage, ldapResult.matchedDN, ldapResult.resControls != null ? this.unsolicited.elementAt(0).convertControls(ldapResult.resControls) : null);
                }
            }
            if (unsolicitedResponseImpl != null) {
                notifyUnsolicited(unsolicitedResponseImpl);
                if (DISCONNECT_OID.equals(ldapResult.extensionId)) {
                    notifyUnsolicited(new CommunicationException("Connection closed"));
                }
            }
        } catch (IOException e2) {
            CommunicationException communicationException = new CommunicationException("Problem parsing unsolicited notification");
            communicationException.setRootCause(e2);
            notifyUnsolicited(communicationException);
        } catch (NamingException e3) {
            notifyUnsolicited(e3);
        }
    }

    private void notifyUnsolicited(Object obj) {
        Vector vector;
        synchronized (this.unsolicited) {
            vector = new Vector(this.unsolicited);
            if (obj instanceof NamingException) {
                this.unsolicited.setSize(0);
            }
        }
        for (int i2 = 0; i2 < vector.size(); i2++) {
            ((LdapCtx) vector.elementAt(i2)).fireUnsolicited(obj);
        }
    }

    private void ensureOpen() throws IOException {
        if (this.conn == null || !this.conn.useable) {
            if (this.conn != null && this.conn.closureReason != null) {
                throw this.conn.closureReason;
            }
            throw new IOException("connection closed");
        }
    }

    static LdapClient getInstance(boolean z2, String str, int i2, String str2, int i3, int i4, OutputStream outputStream, int i5, String str3, Control[] controlArr, String str4, String str5, Object obj, Hashtable<?, ?> hashtable) throws NamingException {
        if (z2 && LdapPoolManager.isPoolingAllowed(str2, outputStream, str3, str4, hashtable)) {
            LdapClient ldapClient = LdapPoolManager.getLdapClient(str, i2, str2, i3, i4, outputStream, i5, str3, controlArr, str4, str5, obj, hashtable);
            ldapClient.referenceCount = 1;
            return ldapClient;
        }
        return new LdapClient(str, i2, str2, i3, i4, outputStream, null);
    }
}
