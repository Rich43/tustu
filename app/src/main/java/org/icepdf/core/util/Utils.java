package org.icepdf.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.io.SeekableByteArrayInputStream;
import org.icepdf.core.io.SeekableInput;
import org.icepdf.core.pobjects.StringObject;
import org.icepdf.core.pobjects.fonts.ofont.Encoding;

/* loaded from: icepdf-core.jar:org/icepdf/core/util/Utils.class */
public class Utils {
    private static final Logger logger = Logger.getLogger(Utils.class.toString());
    private static long lastMemUsed = 0;

    public static void setIntIntoByteArrayBE(int value, byte[] buffer, int offset) {
        buffer[offset] = (byte) ((value >>> 24) & 255);
        buffer[offset + 1] = (byte) ((value >>> 16) & 255);
        buffer[offset + 2] = (byte) ((value >>> 8) & 255);
        buffer[offset + 3] = (byte) (value & 255);
    }

    public static void setShortIntoByteArrayBE(short value, byte[] buffer, int offset) {
        buffer[offset] = (byte) ((value >>> 8) & 255);
        buffer[offset + 1] = (byte) (value & 255);
    }

    public static long readLongWithVaryingBytesBE(InputStream in, int numBytes) throws IOException {
        long val = 0;
        for (int i2 = 0; i2 < numBytes; i2++) {
            int curr = in.read();
            if (curr < 0) {
                throw new EOFException();
            }
            val = (val << 8) | (curr & 255);
        }
        return val;
    }

    public static int readIntWithVaryingBytesBE(InputStream in, int numBytes) throws IOException {
        int val = 0;
        for (int i2 = 0; i2 < numBytes; i2++) {
            int curr = in.read();
            if (curr < 0) {
                throw new EOFException();
            }
            val = (val << 8) | (curr & 255);
        }
        return val;
    }

    public static void writeInteger(OutputStream in, int i2) throws IOException {
        ByteBuffer bb2 = ByteBuffer.allocate(4);
        bb2.putInt(i2);
        in.write(bb2.array());
    }

    public static void writeLong(OutputStream in, long i2) throws IOException {
        ByteBuffer bb2 = ByteBuffer.allocate(8);
        bb2.putLong(i2);
        in.write(bb2.array());
    }

    public static String convertByteArrayToHexString(byte[] buffer, boolean addSpaceSeparator) {
        return convertByteArrayToHexString(buffer, 0, buffer.length, addSpaceSeparator, -1, (char) 0);
    }

    public static String convertByteArrayToHexString(byte[] buffer, boolean addSpaceSeparator, int addDelimiterEverNBytes, char delimiter) {
        return convertByteArrayToHexString(buffer, 0, buffer.length, addSpaceSeparator, addDelimiterEverNBytes, delimiter);
    }

    public static String convertByteArrayToHexString(byte[] buffer, int offset, int length, boolean addSpaceSeparator, int addDelimiterEverNBytes, char delimiter) {
        int presize = length * (addSpaceSeparator ? 3 : 2);
        if (addDelimiterEverNBytes > 0) {
            presize += length / addDelimiterEverNBytes;
        }
        StringBuilder sb = new StringBuilder(presize);
        int delimiterCount = 0;
        int end = offset + length;
        for (int index = offset; index < end; index++) {
            int currValue = 0 | (255 & buffer[index]);
            String s2 = Integer.toHexString(currValue);
            for (int i2 = s2.length(); i2 < 2; i2++) {
                sb.append('0');
            }
            sb.append(s2);
            if (addSpaceSeparator) {
                sb.append(' ');
            }
            delimiterCount++;
            if (addDelimiterEverNBytes > 0 && delimiterCount == addDelimiterEverNBytes) {
                delimiterCount = 0;
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    public static boolean reflectGraphicsEnvironmentISHeadlessInstance(Object graphicsEnvironment, boolean defaultReturnIfNoMethod) {
        try {
            Class<?> clazz = graphicsEnvironment.getClass();
            Method isHeadlessInstanceMethod = clazz.getMethod("isHeadlessInstance", new Class[0]);
            if (isHeadlessInstanceMethod != null) {
                Object ret = isHeadlessInstanceMethod.invoke(graphicsEnvironment, new Object[0]);
                if (ret instanceof Boolean) {
                    return ((Boolean) ret).booleanValue();
                }
            }
        } catch (Throwable th) {
            logger.log(Level.FINE, "ImageCache: Java 1.4 Headless support not found.");
        }
        return defaultReturnIfNoMethod;
    }

    public static String getContentAndReplaceInputStream(InputStream[] inArray, boolean convertToHex) {
        String content = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
            InputStream in = inArray[0];
            byte[] buf = new byte[1024];
            while (true) {
                int read = in.read(buf, 0, buf.length);
                if (read < 0) {
                    break;
                }
                out.write(buf, 0, read);
            }
            if (!(in instanceof SeekableInput)) {
                in.close();
            }
            out.flush();
            out.close();
            byte[] data = out.toByteArray();
            inArray[0] = new ByteArrayInputStream(data);
            if (convertToHex) {
                content = convertByteArrayToHexString(data, true);
            } else {
                content = new String(data);
            }
        } catch (IOException ioe) {
            logger.log(Level.FINE, "Problem getting debug string", (Throwable) ioe);
        } catch (Throwable th) {
            logger.log(Level.FINE, "Problem getting content stream, skipping");
        }
        return content;
    }

    public static String getContentFromSeekableInput(SeekableInput in, boolean convertToHex) {
        String content = null;
        try {
            try {
                in.beginThreadAccess();
                long position = in.getAbsolutePosition();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                while (true) {
                    int read = in.getInputStream().read();
                    if (read < 0) {
                        break;
                    }
                    out.write(read);
                }
                in.seekAbsolute(position);
                out.flush();
                out.close();
                byte[] data = out.toByteArray();
                if (convertToHex) {
                    content = convertByteArrayToHexString(data, true);
                } else {
                    content = new String(data);
                }
                in.endThreadAccess();
            } catch (IOException e2) {
                logger.log(Level.FINE, "Problem getting debug string");
                in.endThreadAccess();
            }
            return content;
        } catch (Throwable th) {
            in.endThreadAccess();
            throw th;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static SeekableInput replaceInputStreamWithSeekableInput(InputStream inputStream) {
        if (inputStream instanceof SeekableInput) {
            return (SeekableInput) inputStream;
        }
        SeekableInput sin = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
            while (true) {
                int read = inputStream.read();
                if (read < 0) {
                    break;
                }
                out.write(read);
            }
            inputStream.close();
            out.flush();
            out.close();
            byte[] data = out.toByteArray();
            sin = new SeekableByteArrayInputStream(data);
        } catch (IOException e2) {
            logger.log(Level.FINE, "Problem getting debug string");
        }
        return sin;
    }

    public static void printMemory(String str) {
        long total = Runtime.getRuntime().totalMemory();
        long free = Runtime.getRuntime().freeMemory();
        long used = total - free;
        System.out.println("MEM  " + str + "    used: " + (used / 1024) + " KB    delta: " + ((used - lastMemUsed) / 1024) + " KB");
        lastMemUsed = used;
    }

    public static int numBytesToHoldBits(int numBits) {
        int numBytes = numBits / 8;
        if (numBits % 8 > 0) {
            numBytes++;
        }
        return numBytes;
    }

    public static byte[] convertByteCharSequenceToByteArray(CharSequence string) {
        int max = string.length();
        byte[] bytes = new byte[max];
        for (int i2 = 0; i2 < max; i2++) {
            bytes[i2] = (byte) string.charAt(i2);
        }
        return bytes;
    }

    public static String convertByteArrayToByteString(byte[] bytes) {
        int max = bytes.length;
        StringBuilder sb = new StringBuilder(max);
        for (byte aByte : bytes) {
            int b2 = aByte & 255;
            sb.append((char) b2);
        }
        return sb.toString();
    }

    public static String convertStringObject(Library library, StringObject stringObject) {
        String convertedStringObject = null;
        String titleText = stringObject.getDecryptedLiteralString(library.securityManager);
        if (titleText != null && titleText.length() >= 2 && titleText.charAt(0) == 254 && titleText.charAt(1) == 255) {
            StringBuilder sb1 = new StringBuilder();
            for (int i2 = 2; i2 < titleText.length(); i2 += 2) {
                try {
                    int b1 = titleText.charAt(i2) & 255;
                    int b2 = titleText.charAt(i2 + 1) & 255;
                    sb1.append((char) ((b1 * 256) + b2));
                } catch (Exception e2) {
                }
            }
            convertedStringObject = sb1.toString();
        } else if (titleText != null) {
            StringBuilder sb = new StringBuilder();
            Encoding enc = Encoding.getPDFDoc();
            for (int i3 = 0; i3 < titleText.length(); i3++) {
                sb.append(enc.get(titleText.charAt(i3)));
            }
            convertedStringObject = sb.toString();
        }
        return convertedStringObject;
    }
}
