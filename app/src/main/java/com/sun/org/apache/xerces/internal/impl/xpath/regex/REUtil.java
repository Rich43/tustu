package com.sun.org.apache.xerces.internal.impl.xpath.regex;

import java.text.CharacterIterator;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xpath/regex/REUtil.class */
public final class REUtil {
    static final int CACHESIZE = 20;
    static final RegularExpression[] regexCache = new RegularExpression[20];

    private REUtil() {
    }

    static final int composeFromSurrogates(int high, int low) {
        return ((65536 + ((high - 55296) << 10)) + low) - 56320;
    }

    static final boolean isLowSurrogate(int ch) {
        return (ch & 64512) == 56320;
    }

    static final boolean isHighSurrogate(int ch) {
        return (ch & 64512) == 55296;
    }

    static final String decomposeToSurrogates(int ch) {
        int ch2 = ch - 65536;
        char[] chs = {(char) ((ch2 >> 10) + 55296), (char) ((ch2 & 1023) + 56320)};
        return new String(chs);
    }

    static final String substring(CharacterIterator iterator, int begin, int end) {
        char[] src = new char[end - begin];
        for (int i2 = 0; i2 < src.length; i2++) {
            src[i2] = iterator.setIndex(i2 + begin);
        }
        return new String(src);
    }

    static final int getOptionValue(int ch) {
        int ret = 0;
        switch (ch) {
            case 44:
                ret = 1024;
                break;
            case 70:
                ret = 256;
                break;
            case 72:
                ret = 128;
                break;
            case 88:
                ret = 512;
                break;
            case 105:
                ret = 2;
                break;
            case 109:
                ret = 8;
                break;
            case 115:
                ret = 4;
                break;
            case 117:
                ret = 32;
                break;
            case 119:
                ret = 64;
                break;
            case 120:
                ret = 16;
                break;
        }
        return ret;
    }

    static final int parseOptions(String opts) throws ParseException {
        if (opts == null) {
            return 0;
        }
        int options = 0;
        for (int i2 = 0; i2 < opts.length(); i2++) {
            int v2 = getOptionValue(opts.charAt(i2));
            if (v2 == 0) {
                throw new ParseException("Unknown Option: " + opts.substring(i2), -1);
            }
            options |= v2;
        }
        return options;
    }

    static final String createOptionString(int options) {
        StringBuffer sb = new StringBuffer(9);
        if ((options & 256) != 0) {
            sb.append('F');
        }
        if ((options & 128) != 0) {
            sb.append('H');
        }
        if ((options & 512) != 0) {
            sb.append('X');
        }
        if ((options & 2) != 0) {
            sb.append('i');
        }
        if ((options & 8) != 0) {
            sb.append('m');
        }
        if ((options & 4) != 0) {
            sb.append('s');
        }
        if ((options & 32) != 0) {
            sb.append('u');
        }
        if ((options & 64) != 0) {
            sb.append('w');
        }
        if ((options & 16) != 0) {
            sb.append('x');
        }
        if ((options & 1024) != 0) {
            sb.append(',');
        }
        return sb.toString().intern();
    }

    static String stripExtendedComment(String regex) {
        int len = regex.length();
        StringBuffer buffer = new StringBuffer(len);
        int offset = 0;
        while (offset < len) {
            int i2 = offset;
            offset++;
            int ch = regex.charAt(i2);
            if (ch != 9 && ch != 10 && ch != 12 && ch != 13 && ch != 32) {
                if (ch == 35) {
                    while (offset < len) {
                        int i3 = offset;
                        offset++;
                        int ch2 = regex.charAt(i3);
                        if (ch2 == 13 || ch2 == 10) {
                            break;
                        }
                    }
                } else if (ch == 92 && offset < len) {
                    int next = regex.charAt(offset);
                    if (next == 35 || next == 9 || next == 10 || next == 12 || next == 13 || next == 32) {
                        buffer.append((char) next);
                        offset++;
                    } else {
                        buffer.append('\\');
                        buffer.append((char) next);
                        offset++;
                    }
                } else {
                    buffer.append((char) ch);
                }
            }
        }
        return buffer.toString();
    }

    public static void main(String[] argv) {
        String pattern = null;
        try {
            String options = "";
            String target = null;
            if (argv.length == 0) {
                System.out.println("Error:Usage: java REUtil -i|-m|-s|-u|-w|-X regularExpression String");
                System.exit(0);
            }
            for (int i2 = 0; i2 < argv.length; i2++) {
                if (argv[i2].length() == 0 || argv[i2].charAt(0) != '-') {
                    if (pattern == null) {
                        pattern = argv[i2];
                    } else if (target == null) {
                        target = argv[i2];
                    } else {
                        System.err.println("Unnecessary: " + argv[i2]);
                    }
                } else if (argv[i2].equals("-i")) {
                    options = options + PdfOps.i_TOKEN;
                } else if (argv[i2].equals("-m")) {
                    options = options + PdfOps.m_TOKEN;
                } else if (argv[i2].equals("-s")) {
                    options = options + PdfOps.s_TOKEN;
                } else if (argv[i2].equals("-u")) {
                    options = options + "u";
                } else if (argv[i2].equals("-w")) {
                    options = options + PdfOps.w_TOKEN;
                } else if (argv[i2].equals("-X")) {
                    options = options + "X";
                } else {
                    System.err.println("Unknown option: " + argv[i2]);
                }
            }
            RegularExpression reg = new RegularExpression(pattern, options);
            System.out.println("RegularExpression: " + ((Object) reg));
            Match match = new Match();
            reg.matches(target, match);
            for (int i3 = 0; i3 < match.getNumberOfGroups(); i3++) {
                if (i3 == 0) {
                    System.out.print("Matched range for the whole pattern: ");
                } else {
                    System.out.print("[" + i3 + "]: ");
                }
                if (match.getBeginning(i3) < 0) {
                    System.out.println("-1");
                } else {
                    System.out.print(match.getBeginning(i3) + ", " + match.getEnd(i3) + ", ");
                    System.out.println(PdfOps.DOUBLE_QUOTE__TOKEN + match.getCapturedText(i3) + PdfOps.DOUBLE_QUOTE__TOKEN);
                }
            }
        } catch (ParseException pe) {
            if (pattern == null) {
                pe.printStackTrace();
                return;
            }
            System.err.println("com.sun.org.apache.xerces.internal.utils.regex.ParseException: " + pe.getMessage());
            System.err.println("        " + pattern);
            int loc = pe.getLocation();
            if (loc >= 0) {
                System.err.print("        ");
                for (int i4 = 0; i4 < loc; i4++) {
                    System.err.print(LanguageTag.SEP);
                }
                System.err.println("^");
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static RegularExpression createRegex(String pattern, String options) throws ParseException {
        RegularExpression re = null;
        int intOptions = parseOptions(options);
        synchronized (regexCache) {
            int i2 = 0;
            while (true) {
                if (i2 >= 20) {
                    break;
                }
                RegularExpression cached = regexCache[i2];
                if (cached == null) {
                    i2 = -1;
                    break;
                }
                if (!cached.equals(pattern, intOptions)) {
                    i2++;
                } else {
                    re = cached;
                    break;
                }
            }
            if (re != null) {
                if (i2 != 0) {
                    System.arraycopy(regexCache, 0, regexCache, 1, i2);
                    regexCache[0] = re;
                }
            } else {
                re = new RegularExpression(pattern, options);
                System.arraycopy(regexCache, 0, regexCache, 1, 19);
                regexCache[0] = re;
            }
        }
        return re;
    }

    public static boolean matches(String regex, String target) throws ParseException {
        return createRegex(regex, null).matches(target);
    }

    public static boolean matches(String regex, String options, String target) throws ParseException {
        return createRegex(regex, options).matches(target);
    }

    public static String quoteMeta(String literal) {
        int len = literal.length();
        StringBuffer buffer = null;
        for (int i2 = 0; i2 < len; i2++) {
            int ch = literal.charAt(i2);
            if (".*+?{[()|\\^$".indexOf(ch) >= 0) {
                if (buffer == null) {
                    buffer = new StringBuffer(i2 + ((len - i2) * 2));
                    if (i2 > 0) {
                        buffer.append(literal.substring(0, i2));
                    }
                }
                buffer.append('\\');
                buffer.append((char) ch);
            } else if (buffer != null) {
                buffer.append((char) ch);
            }
        }
        return buffer != null ? buffer.toString() : literal;
    }

    static void dumpString(String v2) {
        for (int i2 = 0; i2 < v2.length(); i2++) {
            System.out.print(Integer.toHexString(v2.charAt(i2)));
            System.out.print(" ");
        }
        System.out.println();
    }
}
