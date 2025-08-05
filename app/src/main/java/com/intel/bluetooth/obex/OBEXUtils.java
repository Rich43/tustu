package com.intel.bluetooth.obex;

import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/obex/OBEXUtils.class */
abstract class OBEXUtils {
    OBEXUtils() {
    }

    static void readFully(InputStream is, OBEXConnectionParams obexConnectionParams, byte[] b2) throws IOException {
        readFully(is, obexConnectionParams, b2, 0, b2.length);
    }

    static void readFully(InputStream is, OBEXConnectionParams obexConnectionParams, byte[] b2, int off, int len) throws IOException {
        int available;
        if (len < 0) {
            throw new IndexOutOfBoundsException();
        }
        int i2 = 0;
        while (true) {
            int got = i2;
            if (got < len) {
                if (obexConnectionParams.timeouts) {
                    long endOfDellay = System.currentTimeMillis() + obexConnectionParams.timeout;
                    do {
                        available = is.available();
                        if (available == 0) {
                            if (System.currentTimeMillis() > endOfDellay) {
                                throw new InterruptedIOException(new StringBuffer().append("OBEX read timeout; received ").append(got).append(" form ").append(len).append(" expected").toString());
                            }
                            try {
                                Thread.sleep(100L);
                            } catch (InterruptedException e2) {
                                throw new InterruptedIOException();
                            }
                        }
                    } while (available == 0);
                }
                int rc = is.read(b2, off + got, len - got);
                if (rc < 0) {
                    throw new EOFException(new StringBuffer().append("EOF while reading OBEX packet; received ").append(got).append(" form ").append(len).append(" expected").toString());
                }
                i2 = got + rc;
            } else {
                return;
            }
        }
    }

    static String newStringUTF16Simple(byte[] bytes) throws UnsupportedEncodingException {
        StringBuffer buf = new StringBuffer();
        for (int i2 = 0; i2 < bytes.length; i2 += 2) {
            buf.append((char) bytesToShort(bytes[i2], bytes[i2 + 1]));
        }
        return buf.toString();
    }

    static String newStringUTF16(byte[] bytes) throws UnsupportedEncodingException {
        try {
            return new String(bytes, FastInfosetSerializer.UTF_16BE);
        } catch (UnsupportedEncodingException e2) {
            return newStringUTF16Simple(bytes);
        } catch (IllegalArgumentException e3) {
            return newStringUTF16Simple(bytes);
        }
    }

    static byte[] getUTF16BytesSimple(String str) throws UnsupportedEncodingException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int len = str.length();
        for (int i2 = 0; i2 < len; i2++) {
            char c2 = str.charAt(i2);
            buf.write(hiByte(c2));
            buf.write(loByte(c2));
        }
        return buf.toByteArray();
    }

    static byte[] getUTF16Bytes(String str) throws UnsupportedEncodingException {
        try {
            return str.getBytes(FastInfosetSerializer.UTF_16BE);
        } catch (UnsupportedEncodingException e2) {
            return getUTF16BytesSimple(str);
        } catch (IllegalArgumentException e3) {
            return getUTF16BytesSimple(str);
        }
    }

    static byte hiByte(int value) {
        return (byte) ((value >> 8) & 255);
    }

    static byte loByte(int value) {
        return (byte) (255 & value);
    }

    static int bytesToShort(byte valueHi, byte valueLo) {
        return ((valueHi << 8) & NormalizerImpl.CC_MASK) + (valueLo & 255);
    }

    public static String toStringObexResponseCodes(byte code) {
        return toStringObexResponseCodes(code & 255);
    }

    public static String toStringObexResponseCodes(int code) {
        switch (code) {
            case 2:
                return "PUT";
            case 3:
                return "GET";
            case 5:
                return "SETPATH";
            case 7:
                return "SESSION";
            case 128:
                return "CONNECT";
            case 129:
                return "DISCONNECT";
            case 130:
                return "PUT FINAL";
            case 131:
                return "GET FINAL";
            case 133:
                return "SETPATH FINAL";
            case 135:
                return "SESSION FINAL";
            case 144:
                return "OBEX_RESPONSE_CONTINUE";
            case 160:
                return "OBEX_HTTP_OK";
            case 161:
                return "OBEX_HTTP_CREATED";
            case 162:
                return "OBEX_HTTP_ACCEPTED";
            case 163:
                return "OBEX_HTTP_NOT_AUTHORITATIVE";
            case 164:
                return "OBEX_HTTP_NO_CONTENT";
            case 165:
                return "OBEX_HTTP_RESET";
            case 166:
                return "OBEX_HTTP_PARTIAL";
            case 176:
                return "OBEX_HTTP_MULT_CHOICE";
            case 177:
                return "OBEX_HTTP_MOVED_PERM";
            case 178:
                return "OBEX_HTTP_MOVED_TEMP";
            case 179:
                return "OBEX_HTTP_SEE_OTHER";
            case 180:
                return "OBEX_HTTP_NOT_MODIFIED";
            case 181:
                return "OBEX_HTTP_USE_PROXY";
            case 192:
                return "OBEX_HTTP_BAD_REQUEST";
            case 193:
                return "OBEX_HTTP_UNAUTHORIZED";
            case 194:
                return "OBEX_HTTP_PAYMENT_REQUIRED";
            case 195:
                return "OBEX_HTTP_FORBIDDEN";
            case 196:
                return "OBEX_HTTP_NOT_FOUND";
            case 197:
                return "OBEX_HTTP_BAD_METHOD";
            case 198:
                return "OBEX_HTTP_NOT_ACCEPTABLE";
            case 199:
                return "OBEX_HTTP_PROXY_AUTH";
            case 200:
                return "OBEX_HTTP_TIMEOUT";
            case 201:
                return "OBEX_HTTP_CONFLICT";
            case 202:
                return "OBEX_HTTP_GONE";
            case 203:
                return "OBEX_HTTP_LENGTH_REQUIRED";
            case 204:
                return "OBEX_HTTP_PRECON_FAILED";
            case 205:
                return "OBEX_HTTP_ENTITY_TOO_LARGE";
            case 206:
                return "OBEX_HTTP_REQ_TOO_LARGE";
            case 207:
                return "OBEX_HTTP_UNSUPPORTED_TYPE";
            case 208:
                return "OBEX_HTTP_INTERNAL_ERROR";
            case 209:
                return "OBEX_HTTP_NOT_IMPLEMENTED";
            case 210:
                return "OBEX_HTTP_BAD_GATEWAY";
            case 211:
                return "OBEX_HTTP_UNAVAILABLE";
            case 212:
                return "OBEX_HTTP_GATEWAY_TIMEOUT";
            case 213:
                return "OBEX_HTTP_VERSION";
            case 224:
                return "OBEX_DATABASE_FULL";
            case 225:
                return "OBEX_DATABASE_LOCKED";
            case 255:
                return "ABORT";
            default:
                return new StringBuffer().append("Unknown 0x").append(Integer.toHexString(code)).toString();
        }
    }
}
