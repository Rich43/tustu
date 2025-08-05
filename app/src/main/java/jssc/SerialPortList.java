package jssc;

import java.io.File;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.regex.Pattern;

/* JADX WARN: Classes with same name are omitted:
  jssc.jar:jssc/SerialPortList.class
 */
/* loaded from: jssc2.8.jar:jssc/SerialPortList.class */
public class SerialPortList {
    private static SerialNativeInterface serialInterface = new SerialNativeInterface();
    private static final Pattern PORTNAMES_REGEXP;
    private static final String PORTNAMES_PATH;
    private static final Comparator<String> PORTNAMES_COMPARATOR;

    static {
        switch (SerialNativeInterface.getOsType()) {
            case 0:
                PORTNAMES_REGEXP = Pattern.compile("(ttyS|ttyUSB|ttyACM|ttyAMA|rfcomm|ttyO)[0-9]{1,3}");
                PORTNAMES_PATH = "/dev/";
                break;
            case 1:
                PORTNAMES_REGEXP = Pattern.compile("");
                PORTNAMES_PATH = "";
                break;
            case 2:
                PORTNAMES_REGEXP = Pattern.compile("[0-9]*|[a-z]*");
                PORTNAMES_PATH = "/dev/term/";
                break;
            case 3:
                PORTNAMES_REGEXP = Pattern.compile("tty.(serial|usbserial|usbmodem).*");
                PORTNAMES_PATH = "/dev/";
                break;
            default:
                PORTNAMES_REGEXP = null;
                PORTNAMES_PATH = null;
                break;
        }
        PORTNAMES_COMPARATOR = new Comparator<String>() { // from class: jssc.SerialPortList.1
            @Override // java.util.Comparator
            public int compare(String valueA, String valueB) {
                if (valueA.equalsIgnoreCase(valueB)) {
                    return valueA.compareTo(valueB);
                }
                int minLength = Math.min(valueA.length(), valueB.length());
                int shiftA = 0;
                int shiftB = 0;
                int i2 = 0;
                while (i2 < minLength) {
                    char charA = valueA.charAt(i2 - shiftA);
                    char charB = valueB.charAt(i2 - shiftB);
                    if (charA != charB) {
                        if (Character.isDigit(charA) && Character.isDigit(charB)) {
                            int[] resultsA = getNumberAndLastIndex(valueA, i2 - shiftA);
                            int[] resultsB = getNumberAndLastIndex(valueB, i2 - shiftB);
                            if (resultsA[0] != resultsB[0]) {
                                return resultsA[0] - resultsB[0];
                            }
                            if (valueA.length() < valueB.length()) {
                                i2 = resultsA[1];
                                shiftB = resultsA[1] - resultsB[1];
                            } else {
                                i2 = resultsB[1];
                                shiftA = resultsB[1] - resultsA[1];
                            }
                        } else if (Character.toLowerCase(charA) - Character.toLowerCase(charB) != 0) {
                            return Character.toLowerCase(charA) - Character.toLowerCase(charB);
                        }
                    }
                    i2++;
                }
                return valueA.compareToIgnoreCase(valueB);
            }

            private int[] getNumberAndLastIndex(String str, int startIndex) {
                String numberValue = "";
                int[] returnValues = {-1, startIndex};
                for (int i2 = startIndex; i2 < str.length(); i2++) {
                    returnValues[1] = i2;
                    char c2 = str.charAt(i2);
                    if (Character.isDigit(c2)) {
                        numberValue = numberValue + c2;
                    }
                }
                try {
                    returnValues[0] = Integer.valueOf(numberValue).intValue();
                } catch (Exception e2) {
                }
                return returnValues;
            }
        };
    }

    public static String[] getPortNames() {
        return getPortNames(PORTNAMES_PATH, PORTNAMES_REGEXP, PORTNAMES_COMPARATOR);
    }

    public static String[] getPortNames(String searchPath) {
        return getPortNames(searchPath, PORTNAMES_REGEXP, PORTNAMES_COMPARATOR);
    }

    public static String[] getPortNames(Pattern pattern) {
        return getPortNames(PORTNAMES_PATH, pattern, PORTNAMES_COMPARATOR);
    }

    public static String[] getPortNames(Comparator<String> comparator) {
        return getPortNames(PORTNAMES_PATH, PORTNAMES_REGEXP, comparator);
    }

    public static String[] getPortNames(String searchPath, Pattern pattern) {
        return getPortNames(searchPath, pattern, PORTNAMES_COMPARATOR);
    }

    public static String[] getPortNames(String searchPath, Comparator<String> comparator) {
        return getPortNames(searchPath, PORTNAMES_REGEXP, comparator);
    }

    public static String[] getPortNames(Pattern pattern, Comparator<String> comparator) {
        return getPortNames(PORTNAMES_PATH, pattern, comparator);
    }

    public static String[] getPortNames(String searchPath, Pattern pattern, Comparator<String> comparator) {
        if (searchPath == null || pattern == null || comparator == null) {
            return new String[0];
        }
        if (SerialNativeInterface.getOsType() == 1) {
            return getWindowsPortNames(pattern, comparator);
        }
        return getUnixBasedPortNames(searchPath, pattern, comparator);
    }

    private static String[] getWindowsPortNames(Pattern pattern, Comparator<String> comparator) {
        String[] portNames = serialInterface.getSerialPortNames();
        if (portNames == null) {
            return new String[0];
        }
        TreeSet<String> ports = new TreeSet<>(comparator);
        for (String portName : portNames) {
            if (pattern.matcher(portName).find()) {
                ports.add(portName);
            }
        }
        return (String[]) ports.toArray(new String[ports.size()]);
    }

    private static String[] getUnixBasedPortNames(String searchPath, Pattern pattern, Comparator<String> comparator) {
        String str;
        str = (searchPath.equals("") || searchPath.endsWith("/")) ? searchPath : searchPath + "/";
        String searchPath2 = str;
        String[] returnArray = new String[0];
        File dir = new File(searchPath2);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files.length > 0) {
                TreeSet<String> portsTree = new TreeSet<>(comparator);
                for (File file : files) {
                    String fileName = file.getName();
                    if (!file.isDirectory() && !file.isFile() && pattern.matcher(fileName).find()) {
                        String portName = searchPath2 + fileName;
                        long portHandle = serialInterface.openPort(portName, false);
                        if (portHandle >= 0 || portHandle == -1) {
                            if (portHandle != -1) {
                                serialInterface.closePort(portHandle);
                            }
                            portsTree.add(portName);
                        }
                    }
                }
                returnArray = (String[]) portsTree.toArray(returnArray);
            }
        }
        return returnArray;
    }
}
