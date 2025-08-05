package com.intel.bluetooth.obex;

import com.intel.bluetooth.DebugLog;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.TimeZone;
import java.util.Vector;
import javax.obex.HeaderSet;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/obex/OBEXHeaderSetImpl.class */
class OBEXHeaderSetImpl implements HeaderSet {
    static final int OBEX_HDR_COUNT = 192;
    static final int OBEX_HDR_NAME = 1;
    static final int OBEX_HDR_TYPE = 66;
    static final int OBEX_HDR_LENGTH = 195;
    static final int OBEX_HDR_TIME = 68;
    static final int OBEX_HDR_TIME2 = 196;
    static final int OBEX_HDR_DESCRIPTION = 5;
    static final int OBEX_HDR_TARGET = 70;
    static final int OBEX_HDR_HTTP = 71;
    static final int OBEX_HDR_BODY = 72;
    static final int OBEX_HDR_BODY_END = 73;
    static final int OBEX_HDR_WHO = 74;
    static final int OBEX_HDR_CONNECTION = 203;
    static final int OBEX_HDR_APP_PARAM = 76;
    static final int OBEX_HDR_AUTH_CHALLENGE = 77;
    static final int OBEX_HDR_AUTH_RESPONSE = 78;
    static final int OBEX_HDR_OBJECTCLASS = 79;
    static final int OBEX_HDR_CREATOR = 207;
    static final int OBEX_HDR_WANUUID = 80;
    static final int OBEX_HDR_SESSIONPARAM = 82;
    static final int OBEX_HDR_SESSIONSEQ = 147;
    static final int OBEX_HDR_USER = 48;
    static final int OBEX_HDR_HI_MASK = 192;
    static final int OBEX_HDR_ID_MASK = 63;
    static final int OBEX_STRING = 0;
    static final int OBEX_BYTE_STREAM = 64;
    static final int OBEX_BYTE = 128;
    static final int OBEX_INT = 192;
    private static final int OBEX_MAX_FIELD_LEN = 255;
    private int responseCode;
    private Hashtable headerValues;
    private Vector authResponses;
    private Vector authChallenges;
    private static final int NO_RESPONSE_CODE = Integer.MIN_VALUE;

    OBEXHeaderSetImpl() {
        this(Integer.MIN_VALUE);
    }

    private OBEXHeaderSetImpl(int responseCode) {
        this.headerValues = new Hashtable();
        this.responseCode = responseCode;
        this.authResponses = null;
        this.authChallenges = null;
    }

    static void validateCreatedHeaderSet(HeaderSet headers) {
        if (headers == null) {
            return;
        }
        if (!(headers instanceof OBEXHeaderSetImpl)) {
            throw new IllegalArgumentException("Illegal HeaderSet type");
        }
        if (((OBEXHeaderSetImpl) headers).responseCode != Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Illegal HeaderSet");
        }
    }

    private void validateHeaderID(int headerID) throws IllegalArgumentException {
        if (headerID < 0 || headerID > 255) {
            throw new IllegalArgumentException("Expected header ID in range 0 to 255");
        }
        int identifier = headerID & 63;
        if (identifier >= 16 && identifier < 47) {
            throw new IllegalArgumentException("Reserved header ID");
        }
    }

    @Override // javax.obex.HeaderSet
    public void setHeader(int headerID, Object headerValue) throws IllegalArgumentException {
        validateHeaderID(headerID);
        if (headerValue == null) {
            this.headerValues.remove(new Integer(headerID));
            return;
        }
        if (headerID == 68 || headerID == 196) {
            if (!(headerValue instanceof Calendar)) {
                throw new IllegalArgumentException("Expected java.util.Calendar");
            }
        } else if (headerID == 66) {
            if (!(headerValue instanceof String)) {
                throw new IllegalArgumentException("Expected java.lang.String");
            }
        } else {
            switch (headerID & 192) {
                case 0:
                    if (!(headerValue instanceof String)) {
                        throw new IllegalArgumentException("Expected java.lang.String");
                    }
                    break;
                case 64:
                    if (!(headerValue instanceof byte[])) {
                        throw new IllegalArgumentException("Expected byte[]");
                    }
                    break;
                case 128:
                    if (!(headerValue instanceof Byte)) {
                        throw new IllegalArgumentException("Expected java.lang.Byte");
                    }
                    break;
                case 192:
                    if (!(headerValue instanceof Long)) {
                        throw new IllegalArgumentException("Expected java.lang.Long");
                    }
                    long v2 = ((Long) headerValue).longValue();
                    if (v2 < 0 || v2 > 4294967295L) {
                        throw new IllegalArgumentException("Expected long in range 0 to 2^32-1");
                    }
                    break;
                default:
                    throw new IllegalArgumentException(new StringBuffer().append("Unsupported encoding ").append(headerID & 192).toString());
            }
        }
        this.headerValues.put(new Integer(headerID), headerValue);
    }

    @Override // javax.obex.HeaderSet
    public Object getHeader(int headerID) throws IOException {
        validateHeaderID(headerID);
        return this.headerValues.get(new Integer(headerID));
    }

    @Override // javax.obex.HeaderSet
    public int[] getHeaderList() throws IOException {
        if (this.headerValues.size() == 0) {
            return null;
        }
        int[] headerIDArray = new int[this.headerValues.size()];
        int i2 = 0;
        Enumeration e2 = this.headerValues.keys();
        while (e2.hasMoreElements()) {
            int i3 = i2;
            i2++;
            headerIDArray[i3] = ((Integer) e2.nextElement2()).intValue();
        }
        return headerIDArray;
    }

    @Override // javax.obex.HeaderSet
    public int getResponseCode() throws IOException {
        if (this.responseCode == Integer.MIN_VALUE) {
            throw new IOException();
        }
        return this.responseCode;
    }

    boolean hasIncommingData() {
        return this.headerValues.contains(new Integer(72)) || this.headerValues.contains(new Integer(73));
    }

    static OBEXHeaderSetImpl cloneHeaders(HeaderSet headers) throws IOException {
        if (headers == null) {
            return null;
        }
        if (!(headers instanceof OBEXHeaderSetImpl)) {
            throw new IllegalArgumentException("Illegal HeaderSet type");
        }
        OBEXHeaderSetImpl hs = new OBEXHeaderSetImpl(((OBEXHeaderSetImpl) headers).responseCode);
        int[] headerIDArray = headers.getHeaderList();
        for (int i2 = 0; headerIDArray != null && i2 < headerIDArray.length; i2++) {
            int headerID = headerIDArray[i2];
            if (headerID != 72 && headerID != 73) {
                hs.setHeader(headerID, headers.getHeader(headerID));
            }
        }
        return hs;
    }

    static HeaderSet appendHeaders(HeaderSet dst, HeaderSet src) throws IOException {
        int[] headerIDArray = src.getHeaderList();
        for (int i2 = 0; headerIDArray != null && i2 < headerIDArray.length; i2++) {
            int headerID = headerIDArray[i2];
            if (headerID != 72 && headerID != 73) {
                dst.setHeader(headerID, src.getHeader(headerID));
            }
        }
        return dst;
    }

    @Override // javax.obex.HeaderSet
    public synchronized void createAuthenticationChallenge(String realm, boolean isUserIdRequired, boolean isFullAccess) {
        if (this.authChallenges == null) {
            this.authChallenges = new Vector();
        }
        this.authChallenges.addElement(OBEXAuthentication.createChallenge(realm, isUserIdRequired, isFullAccess));
    }

    synchronized void addAuthenticationResponse(byte[] authResponse) {
        if (this.authResponses == null) {
            this.authResponses = new Vector();
        }
        this.authResponses.addElement(authResponse);
    }

    boolean hasAuthenticationChallenge() {
        return (this.authChallenges == null || this.authChallenges.isEmpty()) ? false : true;
    }

    Enumeration getAuthenticationChallenges() {
        return this.authChallenges.elements();
    }

    boolean hasAuthenticationResponses() {
        return (this.authResponses == null || this.authResponses.isEmpty()) ? false : true;
    }

    Enumeration getAuthenticationResponses() {
        return this.authResponses.elements();
    }

    static long readObexInt(byte[] data, int off) throws IOException {
        long l2 = 0;
        for (int i2 = 0; i2 < 4; i2++) {
            l2 = (l2 << 8) + (data[off + i2] & 255);
        }
        return l2;
    }

    static void writeObexInt(OutputStream out, int headerID, long data) throws IOException {
        byte[] b2 = {(byte) headerID, (byte) ((data >>> 24) & 255), (byte) ((data >>> 16) & 255), (byte) ((data >>> 8) & 255), (byte) ((data >>> 0) & 255)};
        out.write(b2);
    }

    static void writeObexLen(OutputStream out, int headerID, int len) throws IOException {
        byte[] b2 = new byte[3];
        b2[0] = (byte) headerID;
        if (len < 0 || len > 65535) {
            throw new IOException(new StringBuffer().append("very large data").append(len).toString());
        }
        b2[1] = OBEXUtils.hiByte(len);
        b2[2] = OBEXUtils.loByte(len);
        out.write(b2);
    }

    static void writeObexASCII(OutputStream out, int headerID, String value) throws IOException {
        writeObexLen(out, headerID, 3 + value.length() + 1);
        out.write(value.getBytes("iso-8859-1"));
        out.write(0);
    }

    static void writeObexUnicode(OutputStream out, int headerID, String value) throws IOException {
        if (value.length() == 0) {
            writeObexLen(out, headerID, 3);
            return;
        }
        byte[] b2 = OBEXUtils.getUTF16Bytes(value);
        writeObexLen(out, headerID, 3 + b2.length + 2);
        out.write(b2);
        out.write(new byte[]{0, 0});
    }

    static byte[] toByteArray(HeaderSet headers) throws IOException {
        if (headers == null) {
            return new byte[0];
        }
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int[] headerIDArray = headers.getHeaderList();
        for (int i2 = 0; headerIDArray != null && i2 < headerIDArray.length; i2++) {
            int hi = headerIDArray[i2];
            if (hi == 68) {
                Calendar c2 = (Calendar) headers.getHeader(hi);
                writeObexLen(buf, hi, 19);
                writeTimeISO8601(buf, c2);
            } else if (hi == 196) {
                Calendar c3 = (Calendar) headers.getHeader(hi);
                writeObexInt(buf, hi, c3.getTime().getTime() / 1000);
            } else if (hi == 66) {
                writeObexASCII(buf, hi, (String) headers.getHeader(hi));
            } else {
                switch (hi & 192) {
                    case 0:
                        writeObexUnicode(buf, hi, (String) headers.getHeader(hi));
                        break;
                    case 64:
                        byte[] data = (byte[]) headers.getHeader(hi);
                        writeObexLen(buf, hi, 3 + data.length);
                        buf.write(data);
                        break;
                    case 128:
                        buf.write(hi);
                        buf.write(((Byte) headers.getHeader(hi)).byteValue());
                        break;
                    case 192:
                        writeObexInt(buf, hi, ((Long) headers.getHeader(hi)).longValue());
                        break;
                    default:
                        throw new IOException(new StringBuffer().append("Unsupported encoding ").append(hi & 192).toString());
                }
            }
        }
        if (headerIDArray != null && headerIDArray.length != 0) {
            DebugLog.debug("written headers", headerIDArray.length);
        }
        if (((OBEXHeaderSetImpl) headers).hasAuthenticationChallenge()) {
            Enumeration iter = ((OBEXHeaderSetImpl) headers).authChallenges.elements();
            while (iter.hasMoreElements()) {
                byte[] authChallenge = (byte[]) iter.nextElement2();
                writeObexLen(buf, 77, 3 + authChallenge.length);
                buf.write(authChallenge);
                DebugLog.debug("written AUTH_CHALLENGE");
            }
        }
        if (((OBEXHeaderSetImpl) headers).hasAuthenticationResponses()) {
            Enumeration iter2 = ((OBEXHeaderSetImpl) headers).authResponses.elements();
            while (iter2.hasMoreElements()) {
                byte[] authResponse = (byte[]) iter2.nextElement2();
                writeObexLen(buf, 78, 3 + authResponse.length);
                buf.write(authResponse);
                DebugLog.debug("written AUTH_RESPONSE");
            }
        }
        return buf.toByteArray();
    }

    static OBEXHeaderSetImpl readHeaders(byte[] buf, int off) throws IOException {
        return readHeaders(new OBEXHeaderSetImpl(Integer.MIN_VALUE), buf, off);
    }

    static OBEXHeaderSetImpl readHeaders(byte responseCode, byte[] buf, int off) throws IOException {
        return readHeaders(new OBEXHeaderSetImpl(255 & responseCode), buf, off);
    }

    private static OBEXHeaderSetImpl readHeaders(OBEXHeaderSetImpl hs, byte[] buf, int off) throws IOException, IllegalArgumentException {
        int len;
        int count = 0;
        while (off < buf.length) {
            int hi = 255 & buf[off];
            switch (hi & 192) {
                case 0:
                    len = OBEXUtils.bytesToShort(buf[off + 1], buf[off + 2]);
                    if (len == 3) {
                        hs.setHeader(hi, "");
                        break;
                    } else {
                        byte[] data = new byte[len - 5];
                        System.arraycopy(buf, off + 3, data, 0, data.length);
                        hs.setHeader(hi, OBEXUtils.newStringUTF16(data));
                        break;
                    }
                case 64:
                    len = OBEXUtils.bytesToShort(buf[off + 1], buf[off + 2]);
                    byte[] data2 = new byte[len - 3];
                    System.arraycopy(buf, off + 3, data2, 0, data2.length);
                    if (hi == 66) {
                        if (data2[data2.length - 1] != 0) {
                            hs.setHeader(hi, new String(data2, "iso-8859-1"));
                            break;
                        } else {
                            hs.setHeader(hi, new String(data2, 0, data2.length - 1, "iso-8859-1"));
                            break;
                        }
                    } else if (hi == 68) {
                        hs.setHeader(hi, readTimeISO8601(data2));
                        break;
                    } else if (hi == 77) {
                        synchronized (hs) {
                            if (hs.authChallenges == null) {
                                hs.authChallenges = new Vector();
                            }
                        }
                        hs.authChallenges.addElement(data2);
                        DebugLog.debug("received AUTH_CHALLENGE");
                        break;
                    } else if (hi == 78) {
                        synchronized (hs) {
                            if (hs.authResponses == null) {
                                hs.authResponses = new Vector();
                            }
                        }
                        hs.authResponses.addElement(data2);
                        DebugLog.debug("received AUTH_RESPONSE");
                        break;
                    } else {
                        hs.setHeader(hi, data2);
                        break;
                    }
                case 128:
                    len = 2;
                    hs.setHeader(hi, new Byte(buf[off + 1]));
                    break;
                case 192:
                    len = 5;
                    long intValue = readObexInt(buf, off + 1);
                    if (hi == 196) {
                        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                        cal.setTime(new Date(intValue * 1000));
                        hs.setHeader(hi, cal);
                        break;
                    } else {
                        hs.setHeader(hi, new Long(intValue));
                        break;
                    }
                default:
                    throw new IOException(new StringBuffer().append("Unsupported encoding ").append(hi & 192).toString());
            }
            off += len;
            count++;
        }
        if (count != 0) {
            DebugLog.debug("read headers", count);
        }
        return hs;
    }

    private static byte[] d4(int i2) {
        byte[] b2 = new byte[4];
        int d2 = 1000;
        for (int k2 = 0; k2 < 4; k2++) {
            b2[k2] = (byte) ((i2 / d2) + 48);
            i2 %= d2;
            d2 /= 10;
        }
        return b2;
    }

    private static byte[] d2(int i2) {
        byte[] b2 = {(byte) ((i2 / 10) + 48), (byte) ((i2 % 10) + 48)};
        return b2;
    }

    static void writeTimeISO8601(OutputStream out, Calendar c2) throws IOException {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(c2.getTime());
        out.write(d4(cal.get(1)));
        out.write(d2(cal.get(2) + 1));
        out.write(d2(cal.get(5)));
        out.write(84);
        out.write(d2(cal.get(11)));
        out.write(d2(cal.get(12)));
        out.write(d2(cal.get(13)));
        out.write(90);
    }

    static Calendar readTimeISO8601(byte[] data) throws IOException {
        boolean utc = false;
        if (data.length != 16 && data.length != 15) {
            throw new IOException(new StringBuffer().append("Invalid ISO-8601 date length ").append(new String(data)).append(" length ").append(data.length).toString());
        }
        if (data[8] != 84) {
            throw new IOException(new StringBuffer().append("Invalid ISO-8601 date ").append(new String(data)).toString());
        }
        if (data.length == 16) {
            if (data[15] != 90) {
                throw new IOException(new StringBuffer().append("Invalid ISO-8601 date ").append(new String(data)).toString());
            }
            utc = true;
        }
        Calendar cal = utc ? Calendar.getInstance(TimeZone.getTimeZone("UTC")) : Calendar.getInstance();
        cal.set(1, Integer.valueOf(new String(data, 0, 4)).intValue());
        cal.set(2, Integer.valueOf(new String(data, 4, 2)).intValue() - 1);
        cal.set(5, Integer.valueOf(new String(data, 6, 2)).intValue());
        cal.set(11, Integer.valueOf(new String(data, 9, 2)).intValue());
        cal.set(12, Integer.valueOf(new String(data, 11, 2)).intValue());
        cal.set(13, Integer.valueOf(new String(data, 13, 2)).intValue());
        return cal;
    }
}
