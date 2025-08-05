package com.sun.jna.win32;

import com.sun.jna.Callback;
import com.sun.jna.FunctionMapper;
import com.sun.jna.Library;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/win32/StdCallLibrary.class */
public interface StdCallLibrary extends Library, StdCall {
    public static final int STDCALL_CONVENTION = 1;
    public static final FunctionMapper FUNCTION_MAPPER = new StdCallFunctionMapper();

    /* loaded from: JavaFTD2XX.jar:com/sun/jna/win32/StdCallLibrary$StdCallCallback.class */
    public interface StdCallCallback extends Callback, StdCall {
    }
}
