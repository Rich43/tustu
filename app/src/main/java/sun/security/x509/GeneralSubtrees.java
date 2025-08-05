package sun.security.x509;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

/* loaded from: rt.jar:sun/security/x509/GeneralSubtrees.class */
public class GeneralSubtrees implements Cloneable {
    private final List<GeneralSubtree> trees;
    private static final int NAME_DIFF_TYPE = -1;
    private static final int NAME_MATCH = 0;
    private static final int NAME_NARROWS = 1;
    private static final int NAME_WIDENS = 2;
    private static final int NAME_SAME_TYPE = 3;

    public GeneralSubtrees() {
        this.trees = new ArrayList();
    }

    private GeneralSubtrees(GeneralSubtrees generalSubtrees) {
        this.trees = new ArrayList(generalSubtrees.trees);
    }

    public GeneralSubtrees(DerValue derValue) throws IOException {
        this();
        if (derValue.tag != 48) {
            throw new IOException("Invalid encoding of GeneralSubtrees.");
        }
        while (derValue.data.available() != 0) {
            add(new GeneralSubtree(derValue.data.getDerValue()));
        }
    }

    public GeneralSubtree get(int i2) {
        return this.trees.get(i2);
    }

    public void remove(int i2) {
        this.trees.remove(i2);
    }

    public void add(GeneralSubtree generalSubtree) {
        if (generalSubtree == null) {
            throw new NullPointerException();
        }
        this.trees.add(generalSubtree);
    }

    public boolean contains(GeneralSubtree generalSubtree) {
        if (generalSubtree == null) {
            throw new NullPointerException();
        }
        return this.trees.contains(generalSubtree);
    }

    public int size() {
        return this.trees.size();
    }

    public Iterator<GeneralSubtree> iterator() {
        return this.trees.iterator();
    }

    public List<GeneralSubtree> trees() {
        return this.trees;
    }

    public Object clone() {
        return new GeneralSubtrees(this);
    }

    public String toString() {
        return "   GeneralSubtrees:\n" + this.trees.toString() + "\n";
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        DerOutputStream derOutputStream2 = new DerOutputStream();
        int size = size();
        for (int i2 = 0; i2 < size; i2++) {
            get(i2).encode(derOutputStream2);
        }
        derOutputStream.write((byte) 48, derOutputStream2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GeneralSubtrees)) {
            return false;
        }
        return this.trees.equals(((GeneralSubtrees) obj).trees);
    }

    public int hashCode() {
        return this.trees.hashCode();
    }

    private GeneralNameInterface getGeneralNameInterface(int i2) {
        return getGeneralNameInterface(get(i2));
    }

    private static GeneralNameInterface getGeneralNameInterface(GeneralSubtree generalSubtree) {
        return generalSubtree.getName().getName();
    }

    private void minimize() {
        int i2 = 0;
        while (i2 < size() - 1) {
            GeneralNameInterface generalNameInterface = getGeneralNameInterface(i2);
            boolean z2 = false;
            int i3 = i2 + 1;
            while (true) {
                if (i3 < size()) {
                    switch (generalNameInterface.constrains(getGeneralNameInterface(i3))) {
                        case -1:
                        case 3:
                            i3++;
                        case 0:
                            z2 = true;
                            break;
                        case 1:
                            remove(i3);
                            i3--;
                            i3++;
                        case 2:
                            z2 = true;
                            break;
                    }
                }
            }
            if (z2) {
                remove(i2);
                i2--;
            }
            i2++;
        }
    }

    private GeneralSubtree createWidestSubtree(GeneralNameInterface generalNameInterface) throws IOException {
        GeneralName generalName;
        try {
            switch (generalNameInterface.getType()) {
                case 0:
                    generalName = new GeneralName(new OtherName(((OtherName) generalNameInterface).getOID(), null));
                    break;
                case 1:
                    generalName = new GeneralName(new RFC822Name(""));
                    break;
                case 2:
                    generalName = new GeneralName(new DNSName(""));
                    break;
                case 3:
                    generalName = new GeneralName(new X400Address((byte[]) null));
                    break;
                case 4:
                    generalName = new GeneralName(new X500Name(""));
                    break;
                case 5:
                    generalName = new GeneralName(new EDIPartyName(""));
                    break;
                case 6:
                    generalName = new GeneralName(new URIName(""));
                    break;
                case 7:
                    generalName = new GeneralName(new IPAddressName((byte[]) null));
                    break;
                case 8:
                    generalName = new GeneralName(new OIDName(new ObjectIdentifier((int[]) null)));
                    break;
                default:
                    throw new IOException("Unsupported GeneralNameInterface type: " + generalNameInterface.getType());
            }
            return new GeneralSubtree(generalName, 0, -1);
        } catch (IOException e2) {
            throw new RuntimeException("Unexpected error: " + ((Object) e2), e2);
        }
    }

    public GeneralSubtrees intersect(GeneralSubtrees generalSubtrees) throws UnsupportedOperationException, IOException {
        if (generalSubtrees == null) {
            throw new NullPointerException("other GeneralSubtrees must not be null");
        }
        GeneralSubtrees generalSubtrees2 = new GeneralSubtrees();
        GeneralSubtrees generalSubtrees3 = null;
        if (size() == 0) {
            union(generalSubtrees);
            return null;
        }
        minimize();
        generalSubtrees.minimize();
        int i2 = 0;
        while (i2 < size()) {
            GeneralNameInterface generalNameInterface = getGeneralNameInterface(i2);
            boolean z2 = false;
            int i3 = 0;
            while (true) {
                if (i3 < generalSubtrees.size()) {
                    GeneralSubtree generalSubtree = generalSubtrees.get(i3);
                    switch (generalNameInterface.constrains(getGeneralNameInterface(generalSubtree))) {
                        case -1:
                        default:
                            i3++;
                        case 0:
                        case 2:
                            z2 = false;
                            break;
                        case 1:
                            remove(i2);
                            i2--;
                            generalSubtrees2.add(generalSubtree);
                            z2 = false;
                            break;
                        case 3:
                            z2 = true;
                            i3++;
                    }
                }
            }
            if (z2) {
                boolean z3 = false;
                for (int i4 = 0; i4 < size(); i4++) {
                    GeneralNameInterface generalNameInterface2 = getGeneralNameInterface(i4);
                    if (generalNameInterface2.getType() == generalNameInterface.getType()) {
                        for (int i5 = 0; i5 < generalSubtrees.size(); i5++) {
                            int iConstrains = generalNameInterface2.constrains(generalSubtrees.getGeneralNameInterface(i5));
                            if (iConstrains == 0 || iConstrains == 2 || iConstrains == 1) {
                                z3 = true;
                            }
                        }
                    }
                }
                if (!z3) {
                    if (generalSubtrees3 == null) {
                        generalSubtrees3 = new GeneralSubtrees();
                    }
                    GeneralSubtree generalSubtreeCreateWidestSubtree = createWidestSubtree(generalNameInterface);
                    if (!generalSubtrees3.contains(generalSubtreeCreateWidestSubtree)) {
                        generalSubtrees3.add(generalSubtreeCreateWidestSubtree);
                    }
                }
                remove(i2);
                i2--;
            }
            i2++;
        }
        if (generalSubtrees2.size() > 0) {
            union(generalSubtrees2);
        }
        for (int i6 = 0; i6 < generalSubtrees.size(); i6++) {
            GeneralSubtree generalSubtree2 = generalSubtrees.get(i6);
            GeneralNameInterface generalNameInterface3 = getGeneralNameInterface(generalSubtree2);
            boolean z4 = false;
            int i7 = 0;
            while (true) {
                if (i7 < size()) {
                    switch (getGeneralNameInterface(i7).constrains(generalNameInterface3)) {
                        case -1:
                            z4 = true;
                            i7++;
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                            z4 = false;
                            break;
                        default:
                            i7++;
                    }
                }
            }
            if (z4) {
                add(generalSubtree2);
            }
        }
        return generalSubtrees3;
    }

    public void union(GeneralSubtrees generalSubtrees) {
        if (generalSubtrees != null) {
            int size = generalSubtrees.size();
            for (int i2 = 0; i2 < size; i2++) {
                add(generalSubtrees.get(i2));
            }
            minimize();
        }
    }

    public void reduce(GeneralSubtrees generalSubtrees) {
        if (generalSubtrees == null) {
            return;
        }
        int size = generalSubtrees.size();
        for (int i2 = 0; i2 < size; i2++) {
            GeneralNameInterface generalNameInterface = generalSubtrees.getGeneralNameInterface(i2);
            int i3 = 0;
            while (i3 < size()) {
                switch (generalNameInterface.constrains(getGeneralNameInterface(i3))) {
                    case 0:
                        remove(i3);
                        i3--;
                        break;
                    case 1:
                        remove(i3);
                        i3--;
                        break;
                }
                i3++;
            }
        }
    }
}
