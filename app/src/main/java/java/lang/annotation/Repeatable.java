package java.lang.annotation;

@Target({ElementType.ANNOTATION_TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: rt.jar:java/lang/annotation/Repeatable.class */
public @interface Repeatable {
    Class<? extends Annotation> value();
}
