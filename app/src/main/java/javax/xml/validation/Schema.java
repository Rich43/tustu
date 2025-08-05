package javax.xml.validation;

/* loaded from: rt.jar:javax/xml/validation/Schema.class */
public abstract class Schema {
    public abstract Validator newValidator();

    public abstract ValidatorHandler newValidatorHandler();

    protected Schema() {
    }
}
