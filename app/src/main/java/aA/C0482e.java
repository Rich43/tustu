package aa;

import G.B;
import G.R;
import G.aH;
import G.dh;
import W.InterfaceC0192r;
import W.N;
import W.O;
import W.P;
import bH.W;
import com.efiAnalytics.plugin.ecu.ControllerParameter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;
import org.apache.commons.math3.geometry.VectorFormat;
import org.icepdf.core.util.PdfOps;

/* renamed from: aa.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aa/e.class */
public class C0482e implements O {
    @Override // W.O
    public String a() {
        return "OutputChannels";
    }

    @Override // W.O
    public String b() {
        return "    ;OutputChannels have to basic forms:\n    ; 1) Controller Channels - values contained in the runtime data stream recieved from the controller.\n    ; 2) Expression based Channels - new channels based on any other channels or Constant using \n    ;    mathematical operations and TunerStudio functions.\n    ; Type one are primarily used by Firmware developers, type 2 can easily be user extensions.\n    ;\n    ; Type 1 format for scalar:\n    ; channelName      = scalar, dataType,    offset, \"Units\",   scale, translate\n    ;   channelName can be any alphanumeric string. It must start with a letter and contain no special characters or white spaces.\n    ;   dataType will be U08, S08, U16, S16, U16, S32 or F32. For F32\n    ;   offset - the index of the 1st byte in the read datastream, this can be numeric or key words nextOffset and lastOffset\n    ;   scale and translate will be applied to the raw value using the standard formulas: \n    ;      msValue   = userValue / scale - translate\n    ;      userValue = (msValue + translate) * scale\n    ;   Also of note, scale and translate can be expressions, units can use String function expressions\n    ; Example:\n    ;   seconds          = scalar, U16,    0, \"s\",   1.000, 0.0\n    ;\n    ; Type 1 format for paramClass bit \n    ;   channelName = bits, dataType, offset, bitsOfInterest\n    ; Examples:\n    ;   ready            = bits,   U08,   11, [0:0]\n    ;   crank            = bits,   U08,   11, [1:1]\n    ;   startw           = bits,   U08,   11, [2:2]\n    ;   warmup           = bits,   U08,   11, [3:3]\n    ;   tpsaccaen        = bits,   U08,   11, [4:4]\n    ;   tpsaccden        = bits,   U08,   11, [5:5]\n    ;\n    ; 6 bit fields defined from 1 byte at offset 11. \n    ; the bits of interest are described in the format [n:m] where n is the starting bit and m the last bit.\n    ; in the above examples, n=m so each channel is a single bit.\n    ;\n    ;\n    ; Type 2 format:\n    ; channelName = { someExpression }, \"Units\"\n    ; someExpression can be made up of any set of constants and OutputChannels using any \n    ; of the TunerStudio operators and functions. \n    ; \n    ; For more information on TunerStudio functions, see:\n    ; http://tunerstudio.com/index.php/manuals/88-math-parser-functions\n    ;------------------------------------------------------------------------------------------\n\n    ;channelName      =  class,   dataType,   offset,     \"Units\",     scale,       translate\n    ;------------        -------  ---------   -------     --------       ------       ---------\n\n";
    }

    @Override // W.O
    public void a(R r2, BufferedWriter bufferedWriter, N n2, InterfaceC0192r interfaceC0192r) throws IOException {
        Iterator itQ = r2.q();
        while (itQ.hasNext()) {
            aH aHVar = (aH) itQ.next();
            if (interfaceC0192r == null || interfaceC0192r.a(aHVar)) {
                a(r2, aHVar, bufferedWriter);
            }
        }
        bufferedWriter.write("\n\n");
    }

    private void a(R r2, aH aHVar, BufferedWriter bufferedWriter) throws IOException {
        if (aHVar.b().equals("formula")) {
            bufferedWriter.write("     ");
            bufferedWriter.write(aHVar.aJ());
            for (int length = aHVar.aJ().length(); length < P.f1944a; length++) {
                bufferedWriter.write(32);
            }
            bufferedWriter.write("= {");
            bufferedWriter.write(aHVar.k());
            bufferedWriter.write("}, \"");
            bufferedWriter.write(aHVar.e());
            bufferedWriter.write(PdfOps.DOUBLE_QUOTE__TOKEN);
            if (!aHVar.s()) {
                bufferedWriter.write(", hidden");
            }
        } else if (aHVar.b().equals(ControllerParameter.PARAM_CLASS_SCALAR)) {
            bufferedWriter.write("     ");
            bufferedWriter.write(aHVar.aJ());
            for (int length2 = aHVar.aJ().length(); length2 < P.f1944a; length2++) {
                bufferedWriter.write(32);
            }
            bufferedWriter.write("= scalar,    ");
            bufferedWriter.write(aHVar.c());
            bufferedWriter.write(", ");
            if (aHVar.x() >= 0) {
                String upperCase = Long.toHexString(aHVar.x() - r2.O().af()).toUpperCase();
                bufferedWriter.write(W.a("0x" + (aHVar.x() - ((long) r2.O().af()) > 65535 ? W.a(upperCase, '0', 8) : W.a(upperCase, '0', 4)), ' ', P.f1945b));
            } else {
                bufferedWriter.write(W.a(Double.toString(aHVar.a()), ' ', P.f1945b));
            }
            bufferedWriter.write(", ");
            for (int length3 = aHVar.e().length(); length3 < P.f1945b; length3++) {
                bufferedWriter.write(32);
            }
            bufferedWriter.write(PdfOps.DOUBLE_QUOTE__TOKEN);
            bufferedWriter.write(aHVar.e());
            bufferedWriter.write("\", ");
            dh dhVarG = aHVar.g();
            if (dhVarG instanceof B) {
                bufferedWriter.write(W.a(Double.toString(aHVar.h()), ' ', P.f1945b));
                bufferedWriter.write(", ");
            } else {
                bufferedWriter.write(VectorFormat.DEFAULT_PREFIX);
                bufferedWriter.write(dhVarG.toString());
                bufferedWriter.write(" }, ");
            }
            dh dhVarJ = aHVar.j();
            if (dhVarJ instanceof B) {
                bufferedWriter.write(W.a(Double.toString(aHVar.i()), ' ', P.f1945b));
                bufferedWriter.write(", ");
            } else {
                bufferedWriter.write(VectorFormat.DEFAULT_PREFIX);
                bufferedWriter.write(dhVarJ.toString());
                bufferedWriter.write(" }, ");
            }
            if (!aHVar.s()) {
                bufferedWriter.write(", hidden");
            }
        } else if (aHVar.b().equals(ControllerParameter.PARAM_CLASS_BITS)) {
            bufferedWriter.write("     ");
            bufferedWriter.write(aHVar.aJ());
            for (int length4 = aHVar.aJ().length(); length4 < P.f1944a; length4++) {
                bufferedWriter.write(32);
            }
            bufferedWriter.write("=   bits,    ");
            bufferedWriter.write(aHVar.c());
            bufferedWriter.write(", ");
            if (aHVar.x() >= 0) {
                String upperCase2 = Long.toHexString(aHVar.x() - r2.O().af()).toUpperCase();
                bufferedWriter.write(W.a("0x" + (aHVar.x() - ((long) r2.O().af()) > 65535 ? W.a(upperCase2, '0', 8) : W.a(upperCase2, '0', 4)), ' ', P.f1945b));
            } else {
                bufferedWriter.write(W.a(Integer.toString(aHVar.a()), ' ', P.f1945b));
            }
            bufferedWriter.write(", ");
            bufferedWriter.write("    [");
            bufferedWriter.write(aHVar.q());
            bufferedWriter.write(58);
            bufferedWriter.write("]");
        }
        if (aHVar.aI() != null && !aHVar.aI().trim().isEmpty()) {
            bufferedWriter.write(VectorFormat.DEFAULT_SEPARATOR);
            bufferedWriter.write(aHVar.aI());
        }
        bufferedWriter.write("\n");
    }
}
