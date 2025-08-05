package sun.security.provider.certpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: rt.jar:sun/security/provider/certpath/AdjacencyList.class */
public class AdjacencyList {
    private ArrayList<BuildStep> mStepList = new ArrayList<>();
    private List<List<Vertex>> mOrigList;

    public AdjacencyList(List<List<Vertex>> list) {
        this.mOrigList = list;
        buildList(list, 0, null);
    }

    public Iterator<BuildStep> iterator() {
        return Collections.unmodifiableList(this.mStepList).iterator();
    }

    public int numAttemptedPaths() {
        return this.mOrigList.size();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean buildList(List<List<Vertex>> list, int i2, BuildStep buildStep) {
        List<Vertex> list2 = list.get(i2);
        boolean z2 = true;
        boolean z3 = true;
        for (Vertex vertex : list2) {
            if (vertex.getIndex() != -1) {
                if (list.get(vertex.getIndex()).size() != 0) {
                    z2 = false;
                }
            } else if (vertex.getThrowable() == null) {
                z3 = false;
            }
            this.mStepList.add(new BuildStep(vertex, 1));
        }
        if (z2) {
            if (z3) {
                if (buildStep == null) {
                    this.mStepList.add(new BuildStep(null, 4));
                    return false;
                }
                this.mStepList.add(new BuildStep(buildStep.getVertex(), 2));
                return false;
            }
            ArrayList arrayList = new ArrayList();
            for (Vertex vertex2 : list2) {
                if (vertex2.getThrowable() == null) {
                    arrayList.add(vertex2);
                }
            }
            if (arrayList.size() == 1) {
                this.mStepList.add(new BuildStep((Vertex) arrayList.get(0), 5));
                return true;
            }
            this.mStepList.add(new BuildStep((Vertex) arrayList.get(0), 5));
            return true;
        }
        boolean zBuildList = false;
        for (Vertex vertex3 : list2) {
            if (vertex3.getIndex() != -1 && list.get(vertex3.getIndex()).size() != 0) {
                BuildStep buildStep2 = new BuildStep(vertex3, 3);
                this.mStepList.add(buildStep2);
                zBuildList = buildList(list, vertex3.getIndex(), buildStep2);
            }
        }
        if (zBuildList) {
            return true;
        }
        if (buildStep == null) {
            this.mStepList.add(new BuildStep(null, 4));
            return false;
        }
        this.mStepList.add(new BuildStep(buildStep.getVertex(), 2));
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[\n");
        int i2 = 0;
        for (List<Vertex> list : this.mOrigList) {
            int i3 = i2;
            i2++;
            sb.append("LinkedList[").append(i3).append("]:\n");
            Iterator<Vertex> it = list.iterator();
            while (it.hasNext()) {
                sb.append(it.next().toString()).append("\n");
            }
        }
        sb.append("]\n");
        return sb.toString();
    }
}
