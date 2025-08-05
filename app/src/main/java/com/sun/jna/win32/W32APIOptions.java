package com.sun.jna.win32;

import com.sun.jna.Library;
import java.util.HashMap;
import java.util.Map;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/win32/W32APIOptions.class */
public interface W32APIOptions extends StdCallLibrary {
    public static final Map UNICODE_OPTIONS = new HashMap() { // from class: com.sun.jna.win32.W32APIOptions.1
        {
            put(Library.OPTION_TYPE_MAPPER, W32APITypeMapper.UNICODE);
            put(Library.OPTION_FUNCTION_MAPPER, W32APIFunctionMapper.UNICODE);
        }
    };
    public static final Map ASCII_OPTIONS = new HashMap() { // from class: com.sun.jna.win32.W32APIOptions.2
        {
            put(Library.OPTION_TYPE_MAPPER, W32APITypeMapper.ASCII);
            put(Library.OPTION_FUNCTION_MAPPER, W32APIFunctionMapper.ASCII);
        }
    };
    public static final Map DEFAULT_OPTIONS;

    static {
        DEFAULT_OPTIONS = Boolean.getBoolean("w32.ascii") ? ASCII_OPTIONS : UNICODE_OPTIONS;
    }
}
