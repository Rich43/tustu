package java.time.temporal;

@FunctionalInterface
/* loaded from: rt.jar:java/time/temporal/TemporalQuery.class */
public interface TemporalQuery<R> {
    R queryFrom(TemporalAccessor temporalAccessor);
}
