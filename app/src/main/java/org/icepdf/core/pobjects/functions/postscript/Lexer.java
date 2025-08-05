package org.icepdf.core.pobjects.functions.postscript;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Stack;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/functions/postscript/Lexer.class */
public class Lexer {
    private Reader reader;
    private int expressionDepth;
    private static final int TOKEN_NUMBER = 1;
    private static final int TOKEN_OPERAND = 2;
    private static final int TOKEN_EXPRESSION = 3;
    private static final int TOKEN_BOOLEAN = 5;
    private Procedure currentProcedure;
    private char[] buf = new char[2056];
    private int pos = 0;
    private int numRead = 0;
    private int startTokenPos = 0;
    private int tokenType = 0;
    private Procedure procedures = new Procedure(null);

    public void setInputStream(InputStream in) {
        setReader(new InputStreamReader(in));
    }

    protected void setReader(Reader reader) {
        this.reader = reader;
    }

    public void parse(float[] input) throws IOException {
        if (this.reader == null) {
            throw new IOException("Type 4 function, null input stream reader.");
        }
        this.currentProcedure = this.procedures;
        for (float f2 : input) {
            this.currentProcedure.getProc().push(Float.valueOf(f2));
        }
        this.tokenType = 3;
        while (0 == 0) {
            if (this.pos == this.buf.length) {
                System.arraycopy(this.buf, this.startTokenPos, this.buf, 0, this.pos - this.startTokenPos);
                this.pos = this.buf.length - this.startTokenPos;
                this.startTokenPos = 0;
                this.numRead = this.pos;
            }
            int n2 = this.reader.read(this.buf, this.pos, this.buf.length - this.pos);
            if (n2 > 0) {
                this.numRead += n2;
                while (this.pos < this.numRead) {
                    if (this.tokenType == 1) {
                        numberStart();
                    } else if (this.tokenType == 2) {
                        operandStart();
                    } else if (this.tokenType == 5) {
                        booleanStart();
                    } else if (this.tokenType == 3) {
                        expressionStart();
                    }
                }
            } else {
                return;
            }
        }
    }

    public Stack getStack() {
        return this.procedures.getProc();
    }

    private void parseNextState() {
        while (this.pos < this.numRead && (this.buf[this.pos] == ' ' || this.buf[this.pos] == '\t' || this.buf[this.pos] == '\n' || this.buf[this.pos] == '\r')) {
            this.pos++;
        }
        if (this.pos < this.numRead) {
            this.startTokenPos = this.pos;
            if (this.buf[this.pos] < 'A') {
                this.tokenType = 1;
                return;
            }
            if ((this.buf[this.pos] == 'f' && this.buf[this.pos + 1] == 'a') || (this.buf[this.pos] == 't' && this.buf[this.pos + 3] == 'e')) {
                this.tokenType = 5;
                return;
            }
            if (this.buf[this.pos] < '{') {
                this.tokenType = 2;
            } else if (this.buf[this.pos] == '{' || this.buf[this.pos] == '}') {
                this.tokenType = 3;
            } else {
                parseNextState();
            }
        }
    }

    private void expressionStart() {
        while (true) {
            if (this.pos < this.numRead && (this.buf[this.pos] == '{' || this.buf[this.pos] == '}')) {
                if (this.pos + 1 < this.numRead && this.buf[this.pos] == '}' && this.buf[this.pos + 1] == '{') {
                    this.pos++;
                    break;
                }
                this.pos++;
            } else {
                break;
            }
        }
        if (this.pos < this.numRead) {
            Operator operand = OperatorFactory.getOperator(this.buf, this.startTokenPos, this.pos - this.startTokenPos);
            if (operand.getType() == 43) {
                this.expressionDepth++;
                if (this.expressionDepth > 1) {
                    this.currentProcedure = new Procedure(this.currentProcedure);
                }
            }
            if (operand.getType() == 44) {
                this.currentProcedure = this.currentProcedure.getPrevious();
                this.expressionDepth--;
            }
        }
        parseNextState();
    }

    private void operandStart() {
        this.startTokenPos = this.pos;
        while (this.pos < this.numRead && !isDelimiter(this.buf[this.pos])) {
            this.pos++;
        }
        if (this.pos < this.numRead && this.pos > this.startTokenPos) {
            Operator operand = OperatorFactory.getOperator(this.buf, this.startTokenPos, this.pos - this.startTokenPos);
            if (this.expressionDepth > 1) {
                this.currentProcedure.getProc().push(operand);
            } else {
                operand.eval(this.currentProcedure.getProc());
            }
        }
        parseNextState();
    }

    private void numberStart() {
        this.startTokenPos = this.pos;
        while (this.pos < this.numRead && !isDelimiter(this.buf[this.pos])) {
            this.pos++;
        }
        if (this.pos < this.numRead) {
            this.currentProcedure.getProc().push(Float.valueOf(Float.parseFloat(new String(this.buf, this.startTokenPos, this.pos - this.startTokenPos))));
        }
        parseNextState();
    }

    private void booleanStart() {
        while (this.pos < this.numRead && !isDelimiter(this.buf[this.pos])) {
            this.pos++;
        }
        if (this.pos < this.numRead) {
            this.currentProcedure.getProc().push(Boolean.valueOf(new String(this.buf, this.startTokenPos, this.pos - this.startTokenPos)));
        }
        parseNextState();
    }

    private static boolean isDelimiter(char c2) {
        return c2 == ' ' || c2 == '\t' || c2 == '\n' || c2 == '\r' || c2 == '{' || c2 == '}';
    }
}
