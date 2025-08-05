package com.sun.jna.win32;

import com.sun.jna.FunctionMapper;
import com.sun.jna.NativeLibrary;
import java.lang.reflect.Method;
import org.icepdf.core.util.PdfOps;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/win32/W32APIFunctionMapper.class */
public class W32APIFunctionMapper implements FunctionMapper {
    public static final FunctionMapper UNICODE = new W32APIFunctionMapper(true);
    public static final FunctionMapper ASCII = new W32APIFunctionMapper(false);
    private final String suffix;

    protected W32APIFunctionMapper(boolean unicode) {
        this.suffix = unicode ? PdfOps.W_TOKEN : "A";
    }

    @Override // com.sun.jna.FunctionMapper
    public String getFunctionName(NativeLibrary library, Method method) {
        String name = method.getName();
        if (!name.endsWith(PdfOps.W_TOKEN) && !name.endsWith("A")) {
            try {
                name = library.getFunction(new StringBuffer().append(name).append(this.suffix).toString(), 1).getName();
            } catch (UnsatisfiedLinkError e2) {
            }
        }
        return name;
    }
}
