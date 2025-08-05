package org.icepdf.core.pobjects;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.io.BitStream;
import org.icepdf.core.io.ConservativeSizingByteArrayOutputStream;
import org.icepdf.core.io.SeekableInputConstrainedWrapper;
import org.icepdf.core.pobjects.filters.ASCII85Decode;
import org.icepdf.core.pobjects.filters.ASCIIHexDecode;
import org.icepdf.core.pobjects.filters.FlateDecode;
import org.icepdf.core.pobjects.filters.LZWDecode;
import org.icepdf.core.pobjects.filters.PredictorDecode;
import org.icepdf.core.pobjects.filters.RunLengthDecode;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PdfOps;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/Stream.class */
public class Stream extends Dictionary {
    private static final Logger logger = Logger.getLogger(Stream.class.toString());
    public static final Name WIDTH_KEY = new Name("Width");
    public static final Name W_KEY = new Name(PdfOps.W_TOKEN);
    public static final Name HEIGHT_KEY = new Name("Height");
    public static final Name H_KEY = new Name(PdfOps.H_TOKEN);
    public static final Name IMAGEMASK_KEY = new Name(PdfOps.IM_NAME);
    public static final Name IM_KEY = new Name(PdfOps.IM_TOKEN);
    public static final Name COLORSPACE_KEY = new Name(PdfOps.CS_NAME);
    public static final Name CS_KEY = new Name(PdfOps.CS_TOKEN);
    public static final Name DECODEPARAM_KEY = new Name(PdfOps.DP_NAME);
    public static final Name FILTER_KEY = new Name(PdfOps.F_NAME);
    public static final Name F_KEY = new Name(PdfOps.F_TOKEN);
    public static final Name INDEXED_KEY = new Name(PdfOps.I_NAME);
    public static final Name I_KEY = new Name("I");
    protected byte[] rawBytes;
    protected boolean compressed;
    protected Reference pObjectReference;

    public Stream(Library l2, HashMap h2, SeekableInputConstrainedWrapper streamInputWrapper) {
        super(l2, h2);
        this.compressed = true;
        this.pObjectReference = null;
        if (streamInputWrapper != null) {
            this.rawBytes = getRawStreamBytes(streamInputWrapper);
        }
    }

    public Stream(Library l2, HashMap h2, byte[] rawBytes) {
        super(l2, h2);
        this.compressed = true;
        this.pObjectReference = null;
        this.rawBytes = rawBytes;
    }

    @Override // org.icepdf.core.pobjects.Dictionary
    public void setPObjectReference(Reference reference) {
        this.pObjectReference = reference;
    }

    public byte[] getRawBytes() {
        return this.rawBytes;
    }

    public void setRawBytes(byte[] rawBytes) {
        this.rawBytes = rawBytes;
        this.compressed = false;
    }

    public boolean isRawBytesCompressed() {
        return this.compressed;
    }

    @Override // org.icepdf.core.pobjects.Dictionary
    public Reference getPObjectReference() {
        return this.pObjectReference;
    }

    protected boolean isImageSubtype() {
        Object subtype = this.library.getObject(this.entries, SUBTYPE_KEY);
        return subtype != null && subtype.equals("Image");
    }

    private byte[] getRawStreamBytes(SeekableInputConstrainedWrapper streamInputWrapper) {
        int length = (int) streamInputWrapper.getLength();
        byte[] rawBytes = new byte[length];
        try {
            streamInputWrapper.read(rawBytes, 0, length);
        } catch (IOException e2) {
            logger.warning("IO Error getting stream bytes");
        }
        return rawBytes;
    }

    public ByteArrayInputStream getDecodedByteArrayInputStream() {
        return new ByteArrayInputStream(getDecodedStreamBytes(0));
    }

    public byte[] getDecodedStreamBytes() {
        return getDecodedStreamBytes(8192);
    }

    public byte[] getDecodedStreamBytes(int presize) {
        int outLength;
        if (this.compressed) {
            try {
                ByteArrayInputStream streamInput = new ByteArrayInputStream(this.rawBytes);
                long rawStreamLength = this.rawBytes.length;
                InputStream input = getDecodedInputStream(streamInput, rawStreamLength);
                if (input == null) {
                    return null;
                }
                if (presize > 0) {
                    outLength = presize;
                } else {
                    outLength = Math.max(4096, (int) rawStreamLength);
                }
                ConservativeSizingByteArrayOutputStream out = new ConservativeSizingByteArrayOutputStream(outLength);
                byte[] buffer = new byte[outLength > 4096 ? 4096 : 8192];
                while (true) {
                    int read = input.read(buffer);
                    if (read > 0) {
                        out.write(buffer, 0, read);
                    } else {
                        out.flush();
                        out.close();
                        input.close();
                        out.trim();
                        return out.relinquishByteArray();
                    }
                }
            } catch (IOException e2) {
                logger.log(Level.FINE, "Problem decoding stream bytes: ", (Throwable) e2);
                return null;
            }
        } else {
            return this.rawBytes;
        }
    }

    private InputStream getDecodedInputStream(InputStream streamInput, long streamLength) {
        if (streamInput == null || streamLength < 1) {
            return null;
        }
        int bufferSize = Math.min(Math.max((int) streamLength, 64), 16384);
        InputStream input = new BufferedInputStream(streamInput, bufferSize);
        if (this.library.securityManager != null) {
            HashMap decodeParams = this.library.getDictionary(this.entries, DECODEPARAM_KEY);
            input = this.library.getSecurityManager().getEncryptionInputStream(getPObjectReference(), this.library.getSecurityManager().getDecryptionKey(), decodeParams, input, true);
        }
        List filterNames = getFilterNames();
        if (filterNames == null) {
            return input;
        }
        for (Object filterName1 : filterNames) {
            String filterName = filterName1.toString();
            if (filterName.equals("FlateDecode") || filterName.equals("/Fl") || filterName.equals("Fl")) {
                input = new FlateDecode(this.library, this.entries, input);
            } else if (filterName.equals("LZWDecode") || filterName.equals("/LZW") || filterName.equals("LZW")) {
                input = new LZWDecode(new BitStream(input), this.library, this.entries);
            } else if (filterName.equals("ASCII85Decode") || filterName.equals("/A85") || filterName.equals("A85")) {
                input = new ASCII85Decode(input);
            } else if (filterName.equals("ASCIIHexDecode") || filterName.equals("/AHx") || filterName.equals("AHx")) {
                input = new ASCIIHexDecode(input);
            } else if (filterName.equals("RunLengthDecode") || filterName.equals("/RL") || filterName.equals("RL")) {
                input = new RunLengthDecode(input);
            } else if (!filterName.equals("CCITTFaxDecode") && !filterName.equals("/CCF") && !filterName.equals("CCF") && !filterName.equals("DCTDecode") && !filterName.equals("/DCT") && !filterName.equals("DCT") && !filterName.equals("JBIG2Decode") && !filterName.equals("JPXDecode") && logger.isLoggable(Level.FINE)) {
                logger.fine("UNSUPPORTED:" + filterName + " " + ((Object) this.entries));
            }
        }
        if (PredictorDecode.isPredictor(this.library, this.entries)) {
            input = new PredictorDecode(input, this.library, this.entries);
        }
        return input;
    }

    protected List<String> getFilterNames() {
        List<String> filterNames = null;
        Object o2 = this.library.getObject(this.entries, FILTER_KEY);
        if (o2 instanceof Name) {
            filterNames = new ArrayList(1);
            filterNames.add(o2.toString());
        } else if (o2 instanceof List) {
            filterNames = (List) o2;
        }
        return filterNames;
    }

    protected List<String> getNormalisedFilterNames() {
        List<String> filterNames = getFilterNames();
        if (filterNames == null) {
            return null;
        }
        for (int i2 = 0; i2 < filterNames.size(); i2++) {
            String filterName = filterNames.get(i2);
            if (filterName.equals("FlateDecode") || filterName.equals("/Fl") || filterName.equals("Fl")) {
                filterName = "FlateDecode";
            } else if (filterName.equals("LZWDecode") || filterName.equals("/LZW") || filterName.equals("LZW")) {
                filterName = "LZWDecode";
            } else if (filterName.equals("ASCII85Decode") || filterName.equals("/A85") || filterName.equals("A85")) {
                filterName = "ASCII85Decode";
            } else if (filterName.equals("ASCIIHexDecode") || filterName.equals("/AHx") || filterName.equals("AHx")) {
                filterName = "ASCIIHexDecode";
            } else if (filterName.equals("RunLengthDecode") || filterName.equals("/RL") || filterName.equals("RL")) {
                filterName = "RunLengthDecode";
            } else if (filterName.equals("CCITTFaxDecode") || filterName.equals("/CCF") || filterName.equals("CCF")) {
                filterName = "CCITTFaxDecode";
            } else if (filterName.equals("DCTDecode") || filterName.equals("/DCT") || filterName.equals("DCT")) {
                filterName = "DCTDecode";
            }
            filterNames.set(i2, filterName);
        }
        return filterNames;
    }

    @Override // org.icepdf.core.pobjects.Dictionary
    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append("STREAM= ");
        sb.append((Object) this.entries);
        if (getPObjectReference() != null) {
            sb.append(Constants.INDENT);
            sb.append((Object) getPObjectReference());
        }
        return sb.toString();
    }
}
