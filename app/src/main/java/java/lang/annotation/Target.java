package java.lang.annotation;

@Target({ElementType.ANNOTATION_TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: rt.jar:java/lang/annotation/Target.class */
public @interface Target {
    ElementType[] value();
}
