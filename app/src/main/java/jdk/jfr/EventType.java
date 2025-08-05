package jdk.jfr;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import jdk.jfr.internal.JVMSupport;
import jdk.jfr.internal.MetadataRepository;
import jdk.jfr.internal.PlatformEventType;
import jdk.jfr.internal.Type;
import jdk.jfr.internal.Utils;

/* loaded from: jfr.jar:jdk/jfr/EventType.class */
public final class EventType {
    private final PlatformEventType platformEventType;
    private final List<String> UNCATEGORIZED = Collections.singletonList("Uncategorized");
    private Map<String, ValueDescriptor> cache;

    EventType(PlatformEventType platformEventType) {
        this.platformEventType = platformEventType;
    }

    public List<ValueDescriptor> getFields() {
        return this.platformEventType.getFields();
    }

    public ValueDescriptor getField(String str) {
        Objects.requireNonNull(str);
        if (this.cache == null) {
            List<ValueDescriptor> fields = getFields();
            LinkedHashMap linkedHashMap = new LinkedHashMap(fields.size());
            for (ValueDescriptor valueDescriptor : fields) {
                linkedHashMap.put(valueDescriptor.getName(), valueDescriptor);
            }
            this.cache = linkedHashMap;
        }
        return this.cache.get(str);
    }

    public String getName() {
        return this.platformEventType.getName();
    }

    public String getLabel() {
        return this.platformEventType.getLabel();
    }

    public long getId() {
        return this.platformEventType.getId();
    }

    public List<AnnotationElement> getAnnotationElements() {
        return this.platformEventType.getAnnotationElements();
    }

    public boolean isEnabled() {
        return this.platformEventType.isEnabled();
    }

    public String getDescription() {
        return this.platformEventType.getDescription();
    }

    public <A extends Annotation> A getAnnotation(Class<A> cls) {
        Objects.requireNonNull(cls);
        return (A) this.platformEventType.getAnnotation(cls);
    }

    public static EventType getEventType(Class<? extends Event> cls) {
        Objects.requireNonNull(cls);
        Utils.ensureValidEventSubclass(cls);
        JVMSupport.ensureWithInternalError();
        return MetadataRepository.getInstance().getEventType(cls);
    }

    public List<SettingDescriptor> getSettingDescriptors() {
        return Collections.unmodifiableList(this.platformEventType.getSettings());
    }

    public List<String> getCategoryNames() {
        Category category = (Category) this.platformEventType.getAnnotation(Category.class);
        if (category == null) {
            return this.UNCATEGORIZED;
        }
        return Collections.unmodifiableList(Arrays.asList(category.value()));
    }

    Type getType() {
        return this.platformEventType;
    }

    PlatformEventType getPlatformEventType() {
        return this.platformEventType;
    }
}
