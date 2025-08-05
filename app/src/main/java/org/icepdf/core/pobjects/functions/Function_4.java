package org.icepdf.core.pobjects.functions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Stream;
import org.icepdf.core.pobjects.functions.postscript.Lexer;
import org.icepdf.core.util.Utils;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/functions/Function_4.class */
public class Function_4 extends Function {
    private static final Logger logger = Logger.getLogger(Function_4.class.toString());
    private byte[] functionContent;
    private HashMap<Integer, float[]> resultCache;

    public Function_4(Dictionary d2) {
        super(d2);
        if (d2 instanceof Stream) {
            Stream functionStream = (Stream) d2;
            this.functionContent = functionStream.getDecodedStreamBytes(0);
            if (logger.isLoggable(Level.FINER)) {
                logger.finest("Function 4: " + Utils.convertByteArrayToByteString(this.functionContent));
            }
        } else {
            logger.warning("Type 4 function operands could not be found.");
        }
        this.resultCache = new HashMap<>();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.icepdf.core.pobjects.functions.Function
    public float[] calculate(float[] x2) {
        Integer colourKey = calculateColourKey(x2);
        float[] result = this.resultCache.get(colourKey);
        if (result != null) {
            return result;
        }
        InputStream content = new ByteArrayInputStream(this.functionContent);
        Lexer lex = new Lexer();
        lex.setInputStream(content);
        try {
            lex.parse(x2);
        } catch (IOException e2) {
            logger.log(Level.WARNING, "Error Processing Type 4 definition", (Throwable) e2);
        }
        Stack stack = lex.getStack();
        int n2 = this.range.length / 2;
        float[] y2 = new float[n2];
        for (int i2 = 0; i2 < n2; i2++) {
            y2[i2] = Math.min(Math.max(((Float) stack.elementAt(i2)).floatValue(), this.range[2 * i2]), this.range[(2 * i2) + 1]);
        }
        this.resultCache.put(colourKey, y2);
        return y2;
    }

    private Integer calculateColourKey(float[] colours) {
        int length = colours.length;
        if (colours[0] > 1.0d) {
            if (length == 1) {
                return Integer.valueOf((int) colours[0]);
            }
            if (length == 2) {
                return Integer.valueOf((((int) colours[1]) << 8) | ((int) colours[0]));
            }
            if (length == 3) {
                return Integer.valueOf((((int) colours[2]) << 16) | (((int) colours[1]) << 8) | ((int) colours[0]));
            }
        }
        StringBuilder builder = new StringBuilder();
        for (float colour : colours) {
            builder.append(colour);
        }
        return Integer.valueOf(builder.toString().hashCode());
    }
}
