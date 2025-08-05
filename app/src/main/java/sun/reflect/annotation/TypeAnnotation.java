package sun.reflect.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.AnnotatedElement;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/* loaded from: rt.jar:sun/reflect/annotation/TypeAnnotation.class */
public final class TypeAnnotation {
    private final TypeAnnotationTargetInfo targetInfo;
    private final LocationInfo loc;
    private final Annotation annotation;
    private final AnnotatedElement baseDeclaration;

    /* loaded from: rt.jar:sun/reflect/annotation/TypeAnnotation$TypeAnnotationTarget.class */
    public enum TypeAnnotationTarget {
        CLASS_TYPE_PARAMETER,
        METHOD_TYPE_PARAMETER,
        CLASS_EXTENDS,
        CLASS_IMPLEMENTS,
        CLASS_TYPE_PARAMETER_BOUND,
        METHOD_TYPE_PARAMETER_BOUND,
        FIELD,
        METHOD_RETURN,
        METHOD_RECEIVER,
        METHOD_FORMAL_PARAMETER,
        THROWS
    }

    public TypeAnnotation(TypeAnnotationTargetInfo typeAnnotationTargetInfo, LocationInfo locationInfo, Annotation annotation, AnnotatedElement annotatedElement) {
        this.targetInfo = typeAnnotationTargetInfo;
        this.loc = locationInfo;
        this.annotation = annotation;
        this.baseDeclaration = annotatedElement;
    }

    public TypeAnnotationTargetInfo getTargetInfo() {
        return this.targetInfo;
    }

    public Annotation getAnnotation() {
        return this.annotation;
    }

    public AnnotatedElement getBaseDeclaration() {
        return this.baseDeclaration;
    }

    public LocationInfo getLocationInfo() {
        return this.loc;
    }

    public static List<TypeAnnotation> filter(TypeAnnotation[] typeAnnotationArr, TypeAnnotationTarget typeAnnotationTarget) {
        ArrayList arrayList = new ArrayList(typeAnnotationArr.length);
        for (TypeAnnotation typeAnnotation : typeAnnotationArr) {
            if (typeAnnotation.getTargetInfo().getTarget() == typeAnnotationTarget) {
                arrayList.add(typeAnnotation);
            }
        }
        arrayList.trimToSize();
        return arrayList;
    }

    /* loaded from: rt.jar:sun/reflect/annotation/TypeAnnotation$TypeAnnotationTargetInfo.class */
    public static final class TypeAnnotationTargetInfo {
        private final TypeAnnotationTarget target;
        private final int count;
        private final int secondaryIndex;
        private static final int UNUSED_INDEX = -2;

        public TypeAnnotationTargetInfo(TypeAnnotationTarget typeAnnotationTarget) {
            this(typeAnnotationTarget, -2, -2);
        }

        public TypeAnnotationTargetInfo(TypeAnnotationTarget typeAnnotationTarget, int i2) {
            this(typeAnnotationTarget, i2, -2);
        }

        public TypeAnnotationTargetInfo(TypeAnnotationTarget typeAnnotationTarget, int i2, int i3) {
            this.target = typeAnnotationTarget;
            this.count = i2;
            this.secondaryIndex = i3;
        }

        public TypeAnnotationTarget getTarget() {
            return this.target;
        }

        public int getCount() {
            return this.count;
        }

        public int getSecondaryIndex() {
            return this.secondaryIndex;
        }

        public String toString() {
            return "" + ((Object) this.target) + ": " + this.count + ", " + this.secondaryIndex;
        }
    }

    /* loaded from: rt.jar:sun/reflect/annotation/TypeAnnotation$LocationInfo.class */
    public static final class LocationInfo {
        private final int depth;
        private final Location[] locations;
        public static final LocationInfo BASE_LOCATION = new LocationInfo();

        private LocationInfo() {
            this(0, new Location[0]);
        }

        private LocationInfo(int i2, Location[] locationArr) {
            this.depth = i2;
            this.locations = locationArr;
        }

        public static LocationInfo parseLocationInfo(ByteBuffer byteBuffer) {
            int i2 = byteBuffer.get() & 255;
            if (i2 == 0) {
                return BASE_LOCATION;
            }
            Location[] locationArr = new Location[i2];
            for (int i3 = 0; i3 < i2; i3++) {
                byte b2 = byteBuffer.get();
                short s2 = (short) (byteBuffer.get() & 255);
                if (b2 != 0) {
                    if (!((b2 == 1) | (b2 == 2)) && b2 != 3) {
                        throw new AnnotationFormatError("Bad Location encoding in Type Annotation");
                    }
                }
                if (b2 != 3 && s2 != 0) {
                    throw new AnnotationFormatError("Bad Location encoding in Type Annotation");
                }
                locationArr[i3] = new Location(b2, s2);
            }
            return new LocationInfo(i2, locationArr);
        }

        public LocationInfo pushArray() {
            return pushLocation((byte) 0, (short) 0);
        }

        public LocationInfo pushInner() {
            return pushLocation((byte) 1, (short) 0);
        }

        public LocationInfo pushWildcard() {
            return pushLocation((byte) 2, (short) 0);
        }

        public LocationInfo pushTypeArg(short s2) {
            return pushLocation((byte) 3, s2);
        }

        public LocationInfo pushLocation(byte b2, short s2) {
            int i2 = this.depth + 1;
            Location[] locationArr = new Location[i2];
            System.arraycopy(this.locations, 0, locationArr, 0, this.depth);
            locationArr[i2 - 1] = new Location(b2, (short) (s2 & 255));
            return new LocationInfo(i2, locationArr);
        }

        public TypeAnnotation[] filter(TypeAnnotation[] typeAnnotationArr) {
            ArrayList arrayList = new ArrayList(typeAnnotationArr.length);
            for (TypeAnnotation typeAnnotation : typeAnnotationArr) {
                if (isSameLocationInfo(typeAnnotation.getLocationInfo())) {
                    arrayList.add(typeAnnotation);
                }
            }
            return (TypeAnnotation[]) arrayList.toArray(new TypeAnnotation[0]);
        }

        boolean isSameLocationInfo(LocationInfo locationInfo) {
            if (this.depth != locationInfo.depth) {
                return false;
            }
            for (int i2 = 0; i2 < this.depth; i2++) {
                if (!this.locations[i2].isSameLocation(locationInfo.locations[i2])) {
                    return false;
                }
            }
            return true;
        }

        /* loaded from: rt.jar:sun/reflect/annotation/TypeAnnotation$LocationInfo$Location.class */
        public static final class Location {
            public final byte tag;
            public final short index;

            boolean isSameLocation(Location location) {
                return this.tag == location.tag && this.index == location.index;
            }

            public Location(byte b2, short s2) {
                this.tag = b2;
                this.index = s2;
            }
        }
    }

    public String toString() {
        return this.annotation.toString() + " with Targetnfo: " + this.targetInfo.toString() + " on base declaration: " + this.baseDeclaration.toString();
    }
}
