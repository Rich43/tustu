package java.lang.annotation;

@Target({ElementType.ANNOTATION_TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: rt.jar:java/lang/annotation/Retention.class */
public @interface Retention {
    RetentionPolicy value();
}
