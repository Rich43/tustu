package jdk.jfr;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import jdk.jfr.internal.EventClassBuilder;
import jdk.jfr.internal.JVMSupport;
import jdk.jfr.internal.MetadataRepository;
import jdk.jfr.internal.Type;
import jdk.jfr.internal.Utils;

/* loaded from: jfr.jar:jdk/jfr/EventFactory.class */
public final class EventFactory {
    private static final long REGISTERED_ID = Type.getTypeId(Registered.class);
    private final Class<? extends Event> eventClass;
    private final MethodHandle constructorHandle;
    private final List<AnnotationElement> sanitizedAnnotation;
    private final List<ValueDescriptor> sanitizedFields;

    private EventFactory(Class<? extends Event> cls, List<AnnotationElement> list, List<ValueDescriptor> list2) throws IllegalAccessException, NoSuchMethodException, SecurityException {
        this.constructorHandle = MethodHandles.lookup().unreflectConstructor(cls.getConstructor(new Class[0]));
        this.eventClass = cls;
        this.sanitizedAnnotation = list;
        this.sanitizedFields = list2;
    }

    public static EventFactory create(List<AnnotationElement> list, List<ValueDescriptor> list2) throws Error, SecurityException {
        Objects.requireNonNull(list2);
        Objects.requireNonNull(list);
        JVMSupport.ensureWithInternalError();
        Utils.checkRegisterPermission();
        List<AnnotationElement> listSanitizeNullFreeList = Utils.sanitizeNullFreeList(list, AnnotationElement.class);
        List<ValueDescriptor> listSanitizeNullFreeList2 = Utils.sanitizeNullFreeList(list2, ValueDescriptor.class);
        HashSet hashSet = new HashSet();
        for (ValueDescriptor valueDescriptor : listSanitizeNullFreeList2) {
            String name = valueDescriptor.getName();
            if (valueDescriptor.isArray()) {
                throw new IllegalArgumentException("Array types are not allowed for fields");
            }
            if (!Type.isValidJavaFieldType(valueDescriptor.getTypeName())) {
                throw new IllegalArgumentException(valueDescriptor.getTypeName() + " is not a valid type for an event field");
            }
            if (!Type.isValidJavaIdentifier(valueDescriptor.getName())) {
                throw new IllegalArgumentException(name + " is not a valid name for an event field");
            }
            if (hashSet.contains(name)) {
                throw new IllegalArgumentException("Name of fields must be unique. Found two instances of " + name);
            }
            hashSet.add(name);
        }
        boolean z2 = true;
        ArrayList arrayList = new ArrayList();
        for (AnnotationElement annotationElement : listSanitizeNullFreeList) {
            long typeId = annotationElement.getTypeId();
            if (annotationElement.isInBoot()) {
                if (typeId == REGISTERED_ID) {
                    if (Boolean.FALSE.equals(annotationElement.getValue("value"))) {
                        z2 = false;
                    }
                } else {
                    arrayList.add(annotationElement);
                }
            }
        }
        arrayList.add(new AnnotationElement((Class<? extends Annotation>) Registered.class, (Object) false));
        Class<? extends Event> clsBuild = new EventClassBuilder(arrayList, listSanitizeNullFreeList2).build();
        if (z2) {
            MetadataRepository.getInstance().register(clsBuild, listSanitizeNullFreeList, listSanitizeNullFreeList2);
        }
        try {
            return new EventFactory(clsBuild, listSanitizeNullFreeList, listSanitizeNullFreeList2);
        } catch (IllegalAccessException e2) {
            throw new IllegalAccessError("Could not accees constructor of generated event handler, " + e2.getMessage());
        } catch (NoSuchMethodException e3) {
            throw new InternalError("Could not find constructor in generated event handler, " + e3.getMessage());
        }
    }

    public Event newEvent() {
        try {
            return (Event) this.constructorHandle.invoke();
        } catch (Throwable th) {
            throw new InstantiationError("Could not instantaite dynamically generated event class " + this.eventClass.getName() + ". " + th.getMessage());
        }
    }

    public EventType getEventType() {
        return EventType.getEventType(this.eventClass);
    }

    public void register() throws Error, SecurityException {
        MetadataRepository.getInstance().register(this.eventClass, this.sanitizedAnnotation, this.sanitizedFields);
    }

    public void unregister() {
        MetadataRepository.getInstance().unregister(this.eventClass);
    }
}
