package jdk.nashorn.internal.runtime;

import java.io.OutputStream;
import java.io.PrintWriter;
import jdk.nashorn.internal.parser.Token;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/ErrorManager.class */
public class ErrorManager {
    private final PrintWriter writer;
    private int errors;
    private int warnings;
    private int limit;
    private boolean warningsAsErrors;

    public ErrorManager() {
        this(new PrintWriter((OutputStream) System.err, true));
    }

    public ErrorManager(PrintWriter writer) {
        this.writer = writer;
        this.limit = 100;
        this.warningsAsErrors = false;
    }

    private void checkLimit() {
        int count = this.errors;
        if (this.warningsAsErrors) {
            count += this.warnings;
        }
        if (this.limit != 0 && count > this.limit) {
            throw ECMAErrors.rangeError("too.many.errors", Integer.toString(this.limit));
        }
    }

    public static String format(String message, Source source, int line, int column, long token) {
        String eoln = System.lineSeparator();
        int position = Token.descPosition(token);
        StringBuilder sb = new StringBuilder();
        sb.append(source.getName()).append(':').append(line).append(':').append(column).append(' ').append(message).append(eoln);
        String sourceLine = source.getSourceLine(position);
        sb.append(sourceLine).append(eoln);
        for (int i2 = 0; i2 < column; i2++) {
            if (i2 < sourceLine.length() && sourceLine.charAt(i2) == '\t') {
                sb.append('\t');
            } else {
                sb.append(' ');
            }
        }
        sb.append('^');
        return sb.toString();
    }

    public void error(ParserException e2) {
        error(e2.getMessage());
    }

    public void error(String message) {
        this.writer.println(message);
        this.writer.flush();
        this.errors++;
        checkLimit();
    }

    public void warning(ParserException e2) {
        warning(e2.getMessage());
    }

    public void warning(String message) {
        this.writer.println(message);
        this.writer.flush();
        this.warnings++;
        checkLimit();
    }

    public boolean hasErrors() {
        return this.errors != 0;
    }

    public int getLimit() {
        return this.limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isWarningsAsErrors() {
        return this.warningsAsErrors;
    }

    public void setWarningsAsErrors(boolean warningsAsErrors) {
        this.warningsAsErrors = warningsAsErrors;
    }

    public int getNumberOfErrors() {
        return this.errors;
    }

    public int getNumberOfWarnings() {
        return this.warnings;
    }

    void reset() {
        this.warnings = 0;
        this.errors = 0;
    }
}
