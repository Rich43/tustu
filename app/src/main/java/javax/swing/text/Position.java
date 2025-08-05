package javax.swing.text;

/* loaded from: rt.jar:javax/swing/text/Position.class */
public interface Position {
    int getOffset();

    /* loaded from: rt.jar:javax/swing/text/Position$Bias.class */
    public static final class Bias {
        public static final Bias Forward = new Bias("Forward");
        public static final Bias Backward = new Bias("Backward");
        private String name;

        public String toString() {
            return this.name;
        }

        private Bias(String str) {
            this.name = str;
        }
    }
}
