package sun.nio.cs.ext;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import sun.nio.cs.HistoricallyNamedCharset;
import sun.nio.cs.Surrogate;

/* loaded from: charsets.jar:sun/nio/cs/ext/ISCII91.class */
public class ISCII91 extends Charset implements HistoricallyNamedCharset {
    private static final char NUKTA_CHAR = 2364;
    private static final char HALANT_CHAR = 2381;
    private static final byte NO_CHAR = -1;
    private static final char[] directMapTable = {0, 1, 2, 3, 4, 5, 6, 7, '\b', '\t', '\n', 11, '\f', '\r', 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, ' ', '!', '\"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~', 127, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 2305, 2306, 2307, 2309, 2310, 2311, 2312, 2313, 2314, 2315, 2318, 2319, 2320, 2317, 2322, 2323, 2324, 2321, 2325, 2326, 2327, 2328, 2329, 2330, 2331, 2332, 2333, 2334, 2335, 2336, 2337, 2338, 2339, 2340, 2341, 2342, 2343, 2344, 2345, 2346, 2347, 2348, 2349, 2350, 2351, 2399, 2352, 2353, 2354, 2355, 2356, 2357, 2358, 2359, 2360, 2361, 8205, 2366, 2367, 2368, 2369, 2370, 2371, 2374, 2375, 2376, 2373, 2378, 2379, 2380, 2377, 2381, 2364, 2404, 65535, 65535, 65535, 65535, 65533, 65533, 2406, 2407, 2408, 2409, 2410, 2411, 2412, 2413, 2414, 2415, 65535, 65535, 65535, 65535, 65535};
    private static final byte[] encoderMappingTable = {-1, -1, -95, -1, -94, -1, -93, -1, -1, -1, -92, -1, -91, -1, -90, -1, -89, -1, -88, -1, -87, -1, -86, -1, -90, -23, -82, -1, -85, -1, -84, -1, -83, -1, -78, -1, -81, -1, -80, -1, -79, -1, -77, -1, -76, -1, -75, -1, -74, -1, -73, -1, -72, -1, -71, -1, -70, -1, -69, -1, -68, -1, -67, -1, -66, -1, -65, -1, -64, -1, -63, -1, -62, -1, -61, -1, -60, -1, -59, -1, -58, -1, -57, -1, -56, -1, -55, -1, -54, -1, -53, -1, -52, -1, -51, -1, -49, -1, -48, -1, -47, -1, -46, -1, -45, -1, -44, -1, -43, -1, -42, -1, -41, -1, -40, -1, -1, -1, -1, -1, -23, -1, -22, -23, -38, -1, -37, -1, -36, -1, -35, -1, -34, -1, -33, -1, -33, -23, -29, -1, -32, -1, -31, -1, -30, -1, -25, -1, -28, -1, -27, -1, -26, -1, -24, -1, -1, -1, -1, -1, -95, -23, -16, -75, -16, -72, -2, -1, -2, -1, -1, -1, -1, -1, -1, -1, -77, -23, -76, -23, -75, -23, -70, -23, -65, -23, -64, -23, -55, -23, -50, -1, -86, -23, -89, -23, -37, -23, -36, -23, -22, -1, -22, -22, -15, -1, -14, -1, -13, -1, -12, -1, -11, -1, -10, -1, -9, -1, -8, -1, -7, -1, -6, -1, -16, -65, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};

    public ISCII91() {
        super("x-ISCII91", ExtendedCharsets.aliasesFor("x-ISCII91"));
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "ISCII91";
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return charset.name().equals("US-ASCII") || (charset instanceof ISCII91);
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new Decoder(this);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new Encoder(this);
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/ISCII91$Decoder.class */
    private static class Decoder extends CharsetDecoder {
        private static final char ZWNJ_CHAR = 8204;
        private static final char ZWJ_CHAR = 8205;
        private static final char INVALID_CHAR = 65535;
        private char contextChar;
        private boolean needFlushing;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ISCII91.class.desiredAssertionStatus();
        }

        private Decoder(Charset charset) {
            super(charset, 1.0f, 1.0f);
            this.contextChar = (char) 65535;
            this.needFlushing = false;
        }

        @Override // java.nio.charset.CharsetDecoder
        protected CoderResult implFlush(CharBuffer charBuffer) {
            if (this.needFlushing) {
                if (charBuffer.remaining() < 1) {
                    return CoderResult.OVERFLOW;
                }
                charBuffer.put(this.contextChar);
            }
            this.contextChar = (char) 65535;
            this.needFlushing = false;
            return CoderResult.UNDERFLOW;
        }

        private CoderResult decodeArrayLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            byte[] bArrArray = byteBuffer.array();
            int iArrayOffset = byteBuffer.arrayOffset() + byteBuffer.position();
            int iArrayOffset2 = byteBuffer.arrayOffset() + byteBuffer.limit();
            if (!$assertionsDisabled && iArrayOffset > iArrayOffset2) {
                throw new AssertionError();
            }
            int i2 = iArrayOffset <= iArrayOffset2 ? iArrayOffset : iArrayOffset2;
            char[] cArrArray = charBuffer.array();
            int iArrayOffset3 = charBuffer.arrayOffset() + charBuffer.position();
            int iArrayOffset4 = charBuffer.arrayOffset() + charBuffer.limit();
            if (!$assertionsDisabled && iArrayOffset3 > iArrayOffset4) {
                throw new AssertionError();
            }
            int i3 = iArrayOffset3 <= iArrayOffset4 ? iArrayOffset3 : iArrayOffset4;
            while (i2 < iArrayOffset2) {
                try {
                    byte b2 = bArrArray[i2];
                    char c2 = ISCII91.directMapTable[b2 < 0 ? b2 + 255 : b2];
                    if (this.contextChar != 65533) {
                        switch (c2) {
                            case 2305:
                            case 2311:
                            case 2312:
                            case 2315:
                            case 2367:
                            case 2368:
                            case 2371:
                            case 2404:
                                if (!this.needFlushing) {
                                    this.contextChar = c2;
                                    this.needFlushing = true;
                                    i2++;
                                    continue;
                                } else if (iArrayOffset4 - i3 >= 1) {
                                    int i4 = i3;
                                    i3++;
                                    cArrArray[i4] = this.contextChar;
                                    this.contextChar = c2;
                                    i2++;
                                    break;
                                } else {
                                    CoderResult coderResult = CoderResult.OVERFLOW;
                                    byteBuffer.position(i2 - byteBuffer.arrayOffset());
                                    charBuffer.position(i3 - charBuffer.arrayOffset());
                                    return coderResult;
                                }
                            case ISCII91.NUKTA_CHAR /* 2364 */:
                                if (iArrayOffset4 - i3 >= 1) {
                                    switch (this.contextChar) {
                                        case 2305:
                                            int i5 = i3;
                                            i3++;
                                            cArrArray[i5] = 2384;
                                            break;
                                        case 2311:
                                            int i6 = i3;
                                            i3++;
                                            cArrArray[i6] = 2316;
                                            break;
                                        case 2312:
                                            int i7 = i3;
                                            i3++;
                                            cArrArray[i7] = 2401;
                                            break;
                                        case 2315:
                                            int i8 = i3;
                                            i3++;
                                            cArrArray[i8] = 2400;
                                            break;
                                        case 2367:
                                            int i9 = i3;
                                            i3++;
                                            cArrArray[i9] = 2402;
                                            break;
                                        case 2368:
                                            int i10 = i3;
                                            i3++;
                                            cArrArray[i10] = 2403;
                                            break;
                                        case 2371:
                                            int i11 = i3;
                                            i3++;
                                            cArrArray[i11] = 2372;
                                            break;
                                        case ISCII91.HALANT_CHAR /* 2381 */:
                                            if (!this.needFlushing) {
                                                int i12 = i3;
                                                i3++;
                                                cArrArray[i12] = 8205;
                                                break;
                                            } else {
                                                int i13 = i3;
                                                i3++;
                                                cArrArray[i13] = this.contextChar;
                                                this.contextChar = c2;
                                                i2++;
                                                break;
                                            }
                                        case 2404:
                                            int i14 = i3;
                                            i3++;
                                            cArrArray[i14] = 2365;
                                            break;
                                        default:
                                            if (!this.needFlushing) {
                                                int i15 = i3;
                                                i3++;
                                                cArrArray[i15] = 2364;
                                                break;
                                            } else {
                                                int i16 = i3;
                                                i3++;
                                                cArrArray[i16] = this.contextChar;
                                                this.contextChar = c2;
                                                i2++;
                                                break;
                                            }
                                    }
                                } else {
                                    CoderResult coderResult2 = CoderResult.OVERFLOW;
                                    byteBuffer.position(i2 - byteBuffer.arrayOffset());
                                    charBuffer.position(i3 - charBuffer.arrayOffset());
                                    return coderResult2;
                                }
                            case ISCII91.HALANT_CHAR /* 2381 */:
                                if (iArrayOffset4 - i3 >= 1) {
                                    if (!this.needFlushing) {
                                        if (this.contextChar != ISCII91.HALANT_CHAR) {
                                            int i17 = i3;
                                            i3++;
                                            cArrArray[i17] = 2381;
                                            break;
                                        } else {
                                            int i18 = i3;
                                            i3++;
                                            cArrArray[i18] = 8204;
                                            break;
                                        }
                                    } else {
                                        int i19 = i3;
                                        i3++;
                                        cArrArray[i19] = this.contextChar;
                                        this.contextChar = c2;
                                        i2++;
                                        break;
                                    }
                                } else {
                                    CoderResult coderResult3 = CoderResult.OVERFLOW;
                                    byteBuffer.position(i2 - byteBuffer.arrayOffset());
                                    charBuffer.position(i3 - charBuffer.arrayOffset());
                                    return coderResult3;
                                }
                            case 65535:
                                if (!this.needFlushing) {
                                    CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(1);
                                    byteBuffer.position(i2 - byteBuffer.arrayOffset());
                                    charBuffer.position(i3 - charBuffer.arrayOffset());
                                    return coderResultUnmappableForLength;
                                }
                                if (iArrayOffset4 - i3 < 1) {
                                    CoderResult coderResult4 = CoderResult.OVERFLOW;
                                    byteBuffer.position(i2 - byteBuffer.arrayOffset());
                                    charBuffer.position(i3 - charBuffer.arrayOffset());
                                    return coderResult4;
                                }
                                int i20 = i3;
                                i3++;
                                cArrArray[i20] = this.contextChar;
                                this.contextChar = c2;
                                i2++;
                                continue;
                            default:
                                if (iArrayOffset4 - i3 >= 1) {
                                    if (!this.needFlushing) {
                                        int i21 = i3;
                                        i3++;
                                        cArrArray[i21] = c2;
                                        break;
                                    } else {
                                        int i22 = i3;
                                        i3++;
                                        cArrArray[i22] = this.contextChar;
                                        this.contextChar = c2;
                                        i2++;
                                        break;
                                    }
                                } else {
                                    CoderResult coderResult5 = CoderResult.OVERFLOW;
                                    byteBuffer.position(i2 - byteBuffer.arrayOffset());
                                    charBuffer.position(i3 - charBuffer.arrayOffset());
                                    return coderResult5;
                                }
                        }
                        this.contextChar = c2;
                        this.needFlushing = false;
                        i2++;
                    } else {
                        if (iArrayOffset4 - i3 < 1) {
                            CoderResult coderResult6 = CoderResult.OVERFLOW;
                            byteBuffer.position(i2 - byteBuffer.arrayOffset());
                            charBuffer.position(i3 - charBuffer.arrayOffset());
                            return coderResult6;
                        }
                        int i23 = i3;
                        i3++;
                        cArrArray[i23] = 65533;
                        this.contextChar = (char) 65535;
                        this.needFlushing = false;
                        i2++;
                    }
                } catch (Throwable th) {
                    byteBuffer.position(i2 - byteBuffer.arrayOffset());
                    charBuffer.position(i3 - charBuffer.arrayOffset());
                    throw th;
                }
            }
            CoderResult coderResult7 = CoderResult.UNDERFLOW;
            byteBuffer.position(i2 - byteBuffer.arrayOffset());
            charBuffer.position(i3 - charBuffer.arrayOffset());
            return coderResult7;
        }

        private CoderResult decodeBufferLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            int iPosition = byteBuffer.position();
            while (byteBuffer.hasRemaining()) {
                try {
                    byte b2 = byteBuffer.get();
                    char c2 = ISCII91.directMapTable[b2 < 0 ? b2 + 255 : b2];
                    if (this.contextChar != 65533) {
                        switch (c2) {
                            case 2305:
                            case 2311:
                            case 2312:
                            case 2315:
                            case 2367:
                            case 2368:
                            case 2371:
                            case 2404:
                                if (!this.needFlushing) {
                                    this.contextChar = c2;
                                    this.needFlushing = true;
                                    iPosition++;
                                    continue;
                                } else if (charBuffer.remaining() >= 1) {
                                    charBuffer.put(this.contextChar);
                                    this.contextChar = c2;
                                    iPosition++;
                                    break;
                                } else {
                                    CoderResult coderResult = CoderResult.OVERFLOW;
                                    byteBuffer.position(iPosition);
                                    return coderResult;
                                }
                            case ISCII91.NUKTA_CHAR /* 2364 */:
                                if (charBuffer.remaining() >= 1) {
                                    switch (this.contextChar) {
                                        case 2305:
                                            charBuffer.put((char) 2384);
                                            break;
                                        case 2311:
                                            charBuffer.put((char) 2316);
                                            break;
                                        case 2312:
                                            charBuffer.put((char) 2401);
                                            break;
                                        case 2315:
                                            charBuffer.put((char) 2400);
                                            break;
                                        case 2367:
                                            charBuffer.put((char) 2402);
                                            break;
                                        case 2368:
                                            charBuffer.put((char) 2403);
                                            break;
                                        case 2371:
                                            charBuffer.put((char) 2372);
                                            break;
                                        case ISCII91.HALANT_CHAR /* 2381 */:
                                            if (!this.needFlushing) {
                                                charBuffer.put((char) 8205);
                                                break;
                                            } else {
                                                charBuffer.put(this.contextChar);
                                                this.contextChar = c2;
                                                iPosition++;
                                                break;
                                            }
                                        case 2404:
                                            charBuffer.put((char) 2365);
                                            break;
                                        default:
                                            if (!this.needFlushing) {
                                                charBuffer.put((char) 2364);
                                                break;
                                            } else {
                                                charBuffer.put(this.contextChar);
                                                this.contextChar = c2;
                                                iPosition++;
                                                break;
                                            }
                                    }
                                } else {
                                    CoderResult coderResult2 = CoderResult.OVERFLOW;
                                    byteBuffer.position(iPosition);
                                    return coderResult2;
                                }
                            case ISCII91.HALANT_CHAR /* 2381 */:
                                if (charBuffer.remaining() >= 1) {
                                    if (!this.needFlushing) {
                                        if (this.contextChar != ISCII91.HALANT_CHAR) {
                                            charBuffer.put((char) 2381);
                                            break;
                                        } else {
                                            charBuffer.put((char) 8204);
                                            break;
                                        }
                                    } else {
                                        charBuffer.put(this.contextChar);
                                        this.contextChar = c2;
                                        iPosition++;
                                        break;
                                    }
                                } else {
                                    CoderResult coderResult3 = CoderResult.OVERFLOW;
                                    byteBuffer.position(iPosition);
                                    return coderResult3;
                                }
                            case 65535:
                                if (!this.needFlushing) {
                                    CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(1);
                                    byteBuffer.position(iPosition);
                                    return coderResultUnmappableForLength;
                                }
                                if (charBuffer.remaining() < 1) {
                                    CoderResult coderResult4 = CoderResult.OVERFLOW;
                                    byteBuffer.position(iPosition);
                                    return coderResult4;
                                }
                                charBuffer.put(this.contextChar);
                                this.contextChar = c2;
                                iPosition++;
                                continue;
                            default:
                                if (charBuffer.remaining() >= 1) {
                                    if (!this.needFlushing) {
                                        charBuffer.put(c2);
                                        break;
                                    } else {
                                        charBuffer.put(this.contextChar);
                                        this.contextChar = c2;
                                        iPosition++;
                                        break;
                                    }
                                } else {
                                    CoderResult coderResult5 = CoderResult.OVERFLOW;
                                    byteBuffer.position(iPosition);
                                    return coderResult5;
                                }
                        }
                        this.contextChar = c2;
                        this.needFlushing = false;
                        iPosition++;
                    } else {
                        if (charBuffer.remaining() < 1) {
                            CoderResult coderResult6 = CoderResult.OVERFLOW;
                            byteBuffer.position(iPosition);
                            return coderResult6;
                        }
                        charBuffer.put((char) 65533);
                        this.contextChar = (char) 65535;
                        this.needFlushing = false;
                        iPosition++;
                    }
                } catch (Throwable th) {
                    byteBuffer.position(iPosition);
                    throw th;
                }
            }
            CoderResult coderResult7 = CoderResult.UNDERFLOW;
            byteBuffer.position(iPosition);
            return coderResult7;
        }

        @Override // java.nio.charset.CharsetDecoder
        protected CoderResult decodeLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            if (byteBuffer.hasArray() && charBuffer.hasArray()) {
                return decodeArrayLoop(byteBuffer, charBuffer);
            }
            return decodeBufferLoop(byteBuffer, charBuffer);
        }
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/ISCII91$Encoder.class */
    private static class Encoder extends CharsetEncoder {
        private static final byte NO_CHAR = -1;
        private final Surrogate.Parser sgp;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ISCII91.class.desiredAssertionStatus();
        }

        private Encoder(Charset charset) {
            super(charset, 2.0f, 2.0f);
            this.sgp = new Surrogate.Parser();
        }

        @Override // java.nio.charset.CharsetEncoder
        public boolean canEncode(char c2) {
            return (c2 >= 2304 && c2 <= 2431 && ISCII91.encoderMappingTable[2 * (c2 - 2304)] != -1) || c2 == 8205 || c2 == 8204 || c2 <= 127;
        }

        private CoderResult encodeArrayLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
            char[] cArrArray = charBuffer.array();
            int iArrayOffset = charBuffer.arrayOffset() + charBuffer.position();
            int iArrayOffset2 = charBuffer.arrayOffset() + charBuffer.limit();
            if (!$assertionsDisabled && iArrayOffset > iArrayOffset2) {
                throw new AssertionError();
            }
            int i2 = iArrayOffset <= iArrayOffset2 ? iArrayOffset : iArrayOffset2;
            byte[] bArrArray = byteBuffer.array();
            int iArrayOffset3 = byteBuffer.arrayOffset() + byteBuffer.position();
            int iArrayOffset4 = byteBuffer.arrayOffset() + byteBuffer.limit();
            if (!$assertionsDisabled && iArrayOffset3 > iArrayOffset4) {
                throw new AssertionError();
            }
            int i3 = iArrayOffset3 <= iArrayOffset4 ? iArrayOffset3 : iArrayOffset4;
            while (i2 < iArrayOffset2) {
                try {
                    int i4 = Integer.MIN_VALUE;
                    char c2 = cArrArray[i2];
                    if (c2 < 0 || c2 > 127) {
                        if (c2 == 8204) {
                            c2 = ISCII91.HALANT_CHAR;
                        } else if (c2 == 8205) {
                            c2 = ISCII91.NUKTA_CHAR;
                        }
                        if (c2 >= 2304 && c2 <= 2431) {
                            i4 = (c2 - 2304) * 2;
                        }
                        if (Character.isSurrogate(c2)) {
                            if (this.sgp.parse(c2, cArrArray, i2, iArrayOffset2) < 0) {
                                CoderResult coderResultError = this.sgp.error();
                                charBuffer.position(i2 - charBuffer.arrayOffset());
                                byteBuffer.position(i3 - byteBuffer.arrayOffset());
                                return coderResultError;
                            }
                            CoderResult coderResultUnmappableResult = this.sgp.unmappableResult();
                            charBuffer.position(i2 - charBuffer.arrayOffset());
                            byteBuffer.position(i3 - byteBuffer.arrayOffset());
                            return coderResultUnmappableResult;
                        }
                        if (i4 == Integer.MIN_VALUE || ISCII91.encoderMappingTable[i4] == -1) {
                            CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(1);
                            charBuffer.position(i2 - charBuffer.arrayOffset());
                            byteBuffer.position(i3 - byteBuffer.arrayOffset());
                            return coderResultUnmappableForLength;
                        }
                        if (ISCII91.encoderMappingTable[i4 + 1] == -1) {
                            if (iArrayOffset4 - i3 < 1) {
                                CoderResult coderResult = CoderResult.OVERFLOW;
                                charBuffer.position(i2 - charBuffer.arrayOffset());
                                byteBuffer.position(i3 - byteBuffer.arrayOffset());
                                return coderResult;
                            }
                            int i5 = i3;
                            i3++;
                            bArrArray[i5] = ISCII91.encoderMappingTable[i4];
                        } else {
                            if (iArrayOffset4 - i3 < 2) {
                                CoderResult coderResult2 = CoderResult.OVERFLOW;
                                charBuffer.position(i2 - charBuffer.arrayOffset());
                                byteBuffer.position(i3 - byteBuffer.arrayOffset());
                                return coderResult2;
                            }
                            int i6 = i3;
                            int i7 = i3 + 1;
                            bArrArray[i6] = ISCII91.encoderMappingTable[i4];
                            i3 = i7 + 1;
                            bArrArray[i7] = ISCII91.encoderMappingTable[i4 + 1];
                        }
                        i2++;
                    } else {
                        if (iArrayOffset4 - i3 < 1) {
                            CoderResult coderResult3 = CoderResult.OVERFLOW;
                            charBuffer.position(i2 - charBuffer.arrayOffset());
                            byteBuffer.position(i3 - byteBuffer.arrayOffset());
                            return coderResult3;
                        }
                        int i8 = i3;
                        i3++;
                        bArrArray[i8] = (byte) c2;
                        i2++;
                    }
                } catch (Throwable th) {
                    charBuffer.position(i2 - charBuffer.arrayOffset());
                    byteBuffer.position(i3 - byteBuffer.arrayOffset());
                    throw th;
                }
            }
            CoderResult coderResult4 = CoderResult.UNDERFLOW;
            charBuffer.position(i2 - charBuffer.arrayOffset());
            byteBuffer.position(i3 - byteBuffer.arrayOffset());
            return coderResult4;
        }

        private CoderResult encodeBufferLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
            int iPosition = charBuffer.position();
            while (charBuffer.hasRemaining()) {
                try {
                    int i2 = Integer.MIN_VALUE;
                    char c2 = charBuffer.get();
                    if (c2 < 0 || c2 > 127) {
                        if (c2 == 8204) {
                            c2 = ISCII91.HALANT_CHAR;
                        } else if (c2 == 8205) {
                            c2 = ISCII91.NUKTA_CHAR;
                        }
                        if (c2 >= 2304 && c2 <= 2431) {
                            i2 = (c2 - 2304) * 2;
                        }
                        if (Character.isSurrogate(c2)) {
                            if (this.sgp.parse(c2, charBuffer) < 0) {
                                CoderResult coderResultError = this.sgp.error();
                                charBuffer.position(iPosition);
                                return coderResultError;
                            }
                            CoderResult coderResultUnmappableResult = this.sgp.unmappableResult();
                            charBuffer.position(iPosition);
                            return coderResultUnmappableResult;
                        }
                        if (i2 == Integer.MIN_VALUE || ISCII91.encoderMappingTable[i2] == -1) {
                            CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(1);
                            charBuffer.position(iPosition);
                            return coderResultUnmappableForLength;
                        }
                        if (ISCII91.encoderMappingTable[i2 + 1] == -1) {
                            if (byteBuffer.remaining() < 1) {
                                CoderResult coderResult = CoderResult.OVERFLOW;
                                charBuffer.position(iPosition);
                                return coderResult;
                            }
                            byteBuffer.put(ISCII91.encoderMappingTable[i2]);
                        } else {
                            if (byteBuffer.remaining() < 2) {
                                CoderResult coderResult2 = CoderResult.OVERFLOW;
                                charBuffer.position(iPosition);
                                return coderResult2;
                            }
                            byteBuffer.put(ISCII91.encoderMappingTable[i2]);
                            byteBuffer.put(ISCII91.encoderMappingTable[i2 + 1]);
                        }
                        iPosition++;
                    } else {
                        if (byteBuffer.remaining() < 1) {
                            CoderResult coderResult3 = CoderResult.OVERFLOW;
                            charBuffer.position(iPosition);
                            return coderResult3;
                        }
                        byteBuffer.put((byte) c2);
                        iPosition++;
                    }
                } catch (Throwable th) {
                    charBuffer.position(iPosition);
                    throw th;
                }
            }
            CoderResult coderResult4 = CoderResult.UNDERFLOW;
            charBuffer.position(iPosition);
            return coderResult4;
        }

        @Override // java.nio.charset.CharsetEncoder
        protected CoderResult encodeLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
            if (charBuffer.hasArray() && byteBuffer.hasArray()) {
                return encodeArrayLoop(charBuffer, byteBuffer);
            }
            return encodeBufferLoop(charBuffer, byteBuffer);
        }
    }
}
