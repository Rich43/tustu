package jdk.nashorn.api.scripting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: nashorn.jar:jdk/nashorn/api/scripting/Formatter.class */
final class Formatter {
    private static final String formatSpecifier = "%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])";
    private static final Pattern FS_PATTERN = Pattern.compile(formatSpecifier);

    private Formatter() {
    }

    static String format(String format, Object[] args) {
        Matcher m2 = FS_PATTERN.matcher(format);
        int positionalParameter = 1;
        while (m2.find()) {
            int index = index(m2.group(1));
            boolean previous = isPreviousArgument(m2.group(2));
            char conversion = m2.group(6).charAt(0);
            if (index >= 0 && !previous && conversion != 'n' && conversion != '%') {
                if (index == 0) {
                    int i2 = positionalParameter;
                    positionalParameter++;
                    index = i2;
                }
                if (index <= args.length) {
                    Object arg = args[index - 1];
                    if (m2.group(5) != null) {
                        if (arg instanceof Double) {
                            args[index - 1] = Long.valueOf(((Double) arg).longValue());
                        }
                    } else {
                        switch (conversion) {
                            case 'A':
                            case 'E':
                            case 'G':
                            case 'a':
                            case 'e':
                            case 'f':
                            case 'g':
                                if (!(arg instanceof Integer)) {
                                    break;
                                } else {
                                    args[index - 1] = Double.valueOf(((Integer) arg).doubleValue());
                                    break;
                                }
                            case 'X':
                            case 'd':
                            case 'o':
                            case 'x':
                                if (arg instanceof Double) {
                                    args[index - 1] = Long.valueOf(((Double) arg).longValue());
                                    break;
                                } else if ((arg instanceof String) && ((String) arg).length() > 0) {
                                    args[index - 1] = Integer.valueOf(((String) arg).charAt(0));
                                    break;
                                } else {
                                    break;
                                }
                            case 'c':
                                if (arg instanceof Double) {
                                    args[index - 1] = Integer.valueOf(((Double) arg).intValue());
                                    break;
                                } else if ((arg instanceof String) && ((String) arg).length() > 0) {
                                    args[index - 1] = Integer.valueOf(((String) arg).charAt(0));
                                    break;
                                } else {
                                    break;
                                }
                                break;
                        }
                    }
                }
            }
        }
        return String.format(format, args);
    }

    private static int index(String s2) {
        int index = -1;
        if (s2 != null) {
            try {
                index = Integer.parseInt(s2.substring(0, s2.length() - 1));
            } catch (NumberFormatException e2) {
            }
        } else {
            index = 0;
        }
        return index;
    }

    private static boolean isPreviousArgument(String s2) {
        return s2 != null && s2.indexOf(60) >= 0;
    }
}
