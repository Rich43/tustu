package javax.annotation.processing;

/* loaded from: rt.jar:javax/annotation/processing/Completions.class */
public class Completions {
    private Completions() {
    }

    /* loaded from: rt.jar:javax/annotation/processing/Completions$SimpleCompletion.class */
    private static class SimpleCompletion implements Completion {
        private String value;
        private String message;

        SimpleCompletion(String str, String str2) {
            if (str == null || str2 == null) {
                throw new NullPointerException("Null completion strings not accepted.");
            }
            this.value = str;
            this.message = str2;
        }

        @Override // javax.annotation.processing.Completion
        public String getValue() {
            return this.value;
        }

        @Override // javax.annotation.processing.Completion
        public String getMessage() {
            return this.message;
        }

        public String toString() {
            return "[\"" + this.value + "\", \"" + this.message + "\"]";
        }
    }

    public static Completion of(String str, String str2) {
        return new SimpleCompletion(str, str2);
    }

    public static Completion of(String str) {
        return new SimpleCompletion(str, "");
    }
}
