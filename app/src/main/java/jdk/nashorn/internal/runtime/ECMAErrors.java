package jdk.nashorn.internal.runtime;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.scripts.JS;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/ECMAErrors.class */
public final class ECMAErrors {
    private static final String MESSAGES_RESOURCE = "jdk.nashorn.internal.runtime.resources.Messages";
    private static final ResourceBundle MESSAGES_BUNDLE;
    private static final String scriptPackage;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ECMAErrors.class.desiredAssertionStatus();
        MESSAGES_BUNDLE = ResourceBundle.getBundle(MESSAGES_RESOURCE, Locale.getDefault());
        String name = JS.class.getName();
        scriptPackage = name.substring(0, name.lastIndexOf(46));
    }

    private ECMAErrors() {
    }

    private static ECMAException error(Object thrown, Throwable cause) {
        return new ECMAException(thrown, cause);
    }

    public static ECMAException asEcmaException(ParserException e2) {
        return asEcmaException(Context.getGlobal(), e2);
    }

    public static ECMAException asEcmaException(Global global, ParserException e2) {
        JSErrorType errorType = e2.getErrorType();
        if (!$assertionsDisabled && errorType == null) {
            throw new AssertionError((Object) ("error type for " + ((Object) e2) + " was null"));
        }
        String msg = e2.getMessage();
        switch (errorType) {
            case ERROR:
                return error(global.newError(msg), e2);
            case EVAL_ERROR:
                return error(global.newEvalError(msg), e2);
            case RANGE_ERROR:
                return error(global.newRangeError(msg), e2);
            case REFERENCE_ERROR:
                return error(global.newReferenceError(msg), e2);
            case SYNTAX_ERROR:
                return error(global.newSyntaxError(msg), e2);
            case TYPE_ERROR:
                return error(global.newTypeError(msg), e2);
            case URI_ERROR:
                return error(global.newURIError(msg), e2);
            default:
                throw new AssertionError((Object) e2.getMessage());
        }
    }

    public static ECMAException syntaxError(String msgId, String... args) {
        return syntaxError(Context.getGlobal(), msgId, args);
    }

    public static ECMAException syntaxError(Global global, String msgId, String... args) {
        return syntaxError(global, null, msgId, args);
    }

    public static ECMAException syntaxError(Throwable cause, String msgId, String... args) {
        return syntaxError(Context.getGlobal(), cause, msgId, args);
    }

    public static ECMAException syntaxError(Global global, Throwable cause, String msgId, String... args) {
        String msg = getMessage("syntax.error." + msgId, args);
        return error(global.newSyntaxError(msg), cause);
    }

    public static ECMAException typeError(String msgId, String... args) {
        return typeError(Context.getGlobal(), msgId, args);
    }

    public static ECMAException typeError(Global global, String msgId, String... args) {
        return typeError(global, null, msgId, args);
    }

    public static ECMAException typeError(Throwable cause, String msgId, String... args) {
        return typeError(Context.getGlobal(), cause, msgId, args);
    }

    public static ECMAException typeError(Global global, Throwable cause, String msgId, String... args) {
        String msg = getMessage("type.error." + msgId, args);
        return error(global.newTypeError(msg), cause);
    }

    public static ECMAException rangeError(String msgId, String... args) {
        return rangeError(Context.getGlobal(), msgId, args);
    }

    public static ECMAException rangeError(Global global, String msgId, String... args) {
        return rangeError(global, null, msgId, args);
    }

    public static ECMAException rangeError(Throwable cause, String msgId, String... args) {
        return rangeError(Context.getGlobal(), cause, msgId, args);
    }

    public static ECMAException rangeError(Global global, Throwable cause, String msgId, String... args) {
        String msg = getMessage("range.error." + msgId, args);
        return error(global.newRangeError(msg), cause);
    }

    public static ECMAException referenceError(String msgId, String... args) {
        return referenceError(Context.getGlobal(), msgId, args);
    }

    public static ECMAException referenceError(Global global, String msgId, String... args) {
        return referenceError(global, null, msgId, args);
    }

    public static ECMAException referenceError(Throwable cause, String msgId, String... args) {
        return referenceError(Context.getGlobal(), cause, msgId, args);
    }

    public static ECMAException referenceError(Global global, Throwable cause, String msgId, String... args) {
        String msg = getMessage("reference.error." + msgId, args);
        return error(global.newReferenceError(msg), cause);
    }

    public static ECMAException uriError(String msgId, String... args) {
        return uriError(Context.getGlobal(), msgId, args);
    }

    public static ECMAException uriError(Global global, String msgId, String... args) {
        return uriError(global, null, msgId, args);
    }

    public static ECMAException uriError(Throwable cause, String msgId, String... args) {
        return uriError(Context.getGlobal(), cause, msgId, args);
    }

    public static ECMAException uriError(Global global, Throwable cause, String msgId, String... args) {
        String msg = getMessage("uri.error." + msgId, args);
        return error(global.newURIError(msg), cause);
    }

    public static String getMessage(String msgId, String... args) {
        try {
            return new MessageFormat(MESSAGES_BUNDLE.getString(msgId)).format(args);
        } catch (MissingResourceException e2) {
            throw new RuntimeException("no message resource found for message id: " + msgId);
        }
    }

    public static boolean isScriptFrame(StackTraceElement frame) {
        String source;
        String className = frame.getClassName();
        return (!className.startsWith(scriptPackage) || CompilerConstants.isInternalMethodName(frame.getMethodName()) || (source = frame.getFileName()) == null || source.endsWith(".java")) ? false : true;
    }
}
