package javax.sound.sampled;

/* loaded from: rt.jar:javax/sound/sampled/Control.class */
public abstract class Control {
    private final Type type;

    protected Control(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    public String toString() {
        return new String(((Object) getType()) + " Control");
    }

    /* loaded from: rt.jar:javax/sound/sampled/Control$Type.class */
    public static class Type {
        private String name;

        protected Type(String str) {
            this.name = str;
        }

        public final boolean equals(Object obj) {
            return super.equals(obj);
        }

        public final int hashCode() {
            return super.hashCode();
        }

        public final String toString() {
            return this.name;
        }
    }
}
