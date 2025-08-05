package javax.script;

/* loaded from: rt.jar:javax/script/ScriptException.class */
public class ScriptException extends Exception {
    private static final long serialVersionUID = 8265071037049225001L;
    private String fileName;
    private int lineNumber;
    private int columnNumber;

    public ScriptException(String str) {
        super(str);
        this.fileName = null;
        this.lineNumber = -1;
        this.columnNumber = -1;
    }

    public ScriptException(Exception exc) {
        super(exc);
        this.fileName = null;
        this.lineNumber = -1;
        this.columnNumber = -1;
    }

    public ScriptException(String str, String str2, int i2) {
        super(str);
        this.fileName = str2;
        this.lineNumber = i2;
        this.columnNumber = -1;
    }

    public ScriptException(String str, String str2, int i2, int i3) {
        super(str);
        this.fileName = str2;
        this.lineNumber = i2;
        this.columnNumber = i3;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        String message = super.getMessage();
        if (this.fileName != null) {
            message = message + " in " + this.fileName;
            if (this.lineNumber != -1) {
                message = message + " at line number " + this.lineNumber;
            }
            if (this.columnNumber != -1) {
                message = message + " at column number " + this.columnNumber;
            }
        }
        return message;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public int getColumnNumber() {
        return this.columnNumber;
    }

    public String getFileName() {
        return this.fileName;
    }
}
