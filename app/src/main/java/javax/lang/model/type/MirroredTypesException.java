package javax.lang.model.type;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: rt.jar:javax/lang/model/type/MirroredTypesException.class */
public class MirroredTypesException extends RuntimeException {
    private static final long serialVersionUID = 269;
    transient List<? extends TypeMirror> types;

    MirroredTypesException(String str, TypeMirror typeMirror) {
        super(str);
        ArrayList arrayList = new ArrayList();
        arrayList.add(typeMirror);
        this.types = Collections.unmodifiableList(arrayList);
    }

    public MirroredTypesException(List<? extends TypeMirror> list) {
        StringBuilder sbAppend = new StringBuilder().append("Attempt to access Class objects for TypeMirrors ");
        ArrayList arrayList = new ArrayList(list);
        super(sbAppend.append(arrayList.toString()).toString());
        this.types = Collections.unmodifiableList(arrayList);
    }

    public List<? extends TypeMirror> getTypeMirrors() {
        return this.types;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.types = null;
    }
}
