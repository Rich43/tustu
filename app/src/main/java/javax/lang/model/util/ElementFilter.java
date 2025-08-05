package javax.lang.model.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/* loaded from: rt.jar:javax/lang/model/util/ElementFilter.class */
public class ElementFilter {
    private static final Set<ElementKind> CONSTRUCTOR_KIND = Collections.unmodifiableSet(EnumSet.of(ElementKind.CONSTRUCTOR));
    private static final Set<ElementKind> FIELD_KINDS = Collections.unmodifiableSet(EnumSet.of(ElementKind.FIELD, ElementKind.ENUM_CONSTANT));
    private static final Set<ElementKind> METHOD_KIND = Collections.unmodifiableSet(EnumSet.of(ElementKind.METHOD));
    private static final Set<ElementKind> PACKAGE_KIND = Collections.unmodifiableSet(EnumSet.of(ElementKind.PACKAGE));
    private static final Set<ElementKind> TYPE_KINDS = Collections.unmodifiableSet(EnumSet.of(ElementKind.CLASS, ElementKind.ENUM, ElementKind.INTERFACE, ElementKind.ANNOTATION_TYPE));

    private ElementFilter() {
    }

    public static List<VariableElement> fieldsIn(Iterable<? extends Element> iterable) {
        return listFilter(iterable, FIELD_KINDS, VariableElement.class);
    }

    public static Set<VariableElement> fieldsIn(Set<? extends Element> set) {
        return setFilter(set, FIELD_KINDS, VariableElement.class);
    }

    public static List<ExecutableElement> constructorsIn(Iterable<? extends Element> iterable) {
        return listFilter(iterable, CONSTRUCTOR_KIND, ExecutableElement.class);
    }

    public static Set<ExecutableElement> constructorsIn(Set<? extends Element> set) {
        return setFilter(set, CONSTRUCTOR_KIND, ExecutableElement.class);
    }

    public static List<ExecutableElement> methodsIn(Iterable<? extends Element> iterable) {
        return listFilter(iterable, METHOD_KIND, ExecutableElement.class);
    }

    public static Set<ExecutableElement> methodsIn(Set<? extends Element> set) {
        return setFilter(set, METHOD_KIND, ExecutableElement.class);
    }

    public static List<TypeElement> typesIn(Iterable<? extends Element> iterable) {
        return listFilter(iterable, TYPE_KINDS, TypeElement.class);
    }

    public static Set<TypeElement> typesIn(Set<? extends Element> set) {
        return setFilter(set, TYPE_KINDS, TypeElement.class);
    }

    public static List<PackageElement> packagesIn(Iterable<? extends Element> iterable) {
        return listFilter(iterable, PACKAGE_KIND, PackageElement.class);
    }

    public static Set<PackageElement> packagesIn(Set<? extends Element> set) {
        return setFilter(set, PACKAGE_KIND, PackageElement.class);
    }

    private static <E extends Element> List<E> listFilter(Iterable<? extends Element> iterable, Set<ElementKind> set, Class<E> cls) {
        ArrayList arrayList = new ArrayList();
        for (Element element : iterable) {
            if (set.contains(element.getKind())) {
                arrayList.add(cls.cast(element));
            }
        }
        return arrayList;
    }

    private static <E extends Element> Set<E> setFilter(Set<? extends Element> set, Set<ElementKind> set2, Class<E> cls) {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (Element element : set) {
            if (set2.contains(element.getKind())) {
                linkedHashSet.add(cls.cast(element));
            }
        }
        return linkedHashSet;
    }
}
