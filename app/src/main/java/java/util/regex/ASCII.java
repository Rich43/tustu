package java.util.regex;

import com.sun.glass.events.WindowEvent;
import com.sun.media.sound.DLSModulator;
import jdk.nashorn.internal.runtime.regexp.joni.constants.StackType;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.nntp.NNTPReply;
import sun.security.ssl.Record;

/* loaded from: rt.jar:java/util/regex/ASCII.class */
final class ASCII {
    static final int UPPER = 256;
    static final int LOWER = 512;
    static final int DIGIT = 1024;
    static final int SPACE = 2048;
    static final int PUNCT = 4096;
    static final int CNTRL = 8192;
    static final int BLANK = 16384;
    static final int HEX = 32768;
    static final int UNDER = 65536;
    static final int ASCII = 65280;
    static final int ALPHA = 768;
    static final int ALNUM = 1792;
    static final int GRAPH = 5888;
    static final int WORD = 67328;
    static final int XDIGIT = 32768;
    private static final int[] ctype = {8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 26624, 10240, 10240, 10240, 10240, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, Record.maxFragmentSize, 4096, 4096, 4096, 4096, 4096, 4096, 4096, 4096, 4096, 4096, 4096, 4096, 4096, 4096, 4096, StackType.MEM_END_MARK, 33793, 33794, 33795, 33796, 33797, 33798, 33799, 33800, 33801, 4096, 4096, 4096, 4096, 4096, 4096, 4096, 33034, 33035, 33036, 33037, 33038, 33039, 272, 273, 274, 275, DLSModulator.CONN_DST_VIB_FREQUENCY, DLSModulator.CONN_DST_VIB_STARTDELAY, 278, 279, 280, NNTPReply.AUTHENTICATION_ACCEPTED, 282, 283, 284, 285, 286, 287, 288, 289, 290, 291, 4096, 4096, 4096, 4096, 69632, 4096, 33290, 33291, 33292, 33293, 33294, 33295, 528, 529, FTPReply.NOT_LOGGED_IN, WindowEvent.MINIMIZE, 532, 533, FTPReply.REQUEST_DENIED, FTPReply.FAILED_SECURITY_CHECK, FTPReply.REQUESTED_PROT_LEVEL_NOT_SUPPORTED, 537, 538, 539, 540, 541, WindowEvent.FOCUS_GAINED, WindowEvent.FOCUS_GAINED_FORWARD, 544, WindowEvent.FOCUS_DISABLED, WindowEvent.FOCUS_UNGRAB, 547, 4096, 4096, 4096, 4096, 8192};

    ASCII() {
    }

    static int getType(int i2) {
        if ((i2 & (-128)) == 0) {
            return ctype[i2];
        }
        return 0;
    }

    static boolean isType(int i2, int i3) {
        return (getType(i2) & i3) != 0;
    }

    static boolean isAscii(int i2) {
        return (i2 & (-128)) == 0;
    }

    static boolean isAlpha(int i2) {
        return isType(i2, 768);
    }

    static boolean isDigit(int i2) {
        return ((i2 - 48) | (57 - i2)) >= 0;
    }

    static boolean isAlnum(int i2) {
        return isType(i2, 1792);
    }

    static boolean isGraph(int i2) {
        return isType(i2, GRAPH);
    }

    static boolean isPrint(int i2) {
        return ((i2 - 32) | (126 - i2)) >= 0;
    }

    static boolean isPunct(int i2) {
        return isType(i2, 4096);
    }

    static boolean isSpace(int i2) {
        return isType(i2, 2048);
    }

    static boolean isHexDigit(int i2) {
        return isType(i2, 32768);
    }

    static boolean isOctDigit(int i2) {
        return ((i2 - 48) | (55 - i2)) >= 0;
    }

    static boolean isCntrl(int i2) {
        return isType(i2, 8192);
    }

    static boolean isLower(int i2) {
        return ((i2 - 97) | (122 - i2)) >= 0;
    }

    static boolean isUpper(int i2) {
        return ((i2 - 65) | (90 - i2)) >= 0;
    }

    static boolean isWord(int i2) {
        return isType(i2, WORD);
    }

    static int toDigit(int i2) {
        return ctype[i2 & 127] & 63;
    }

    static int toLower(int i2) {
        return isUpper(i2) ? i2 + 32 : i2;
    }

    static int toUpper(int i2) {
        return isLower(i2) ? i2 - 32 : i2;
    }
}
