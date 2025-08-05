package javafx.scene.shape;

import java.util.HashMap;

/* loaded from: jfxrt.jar:javafx/scene/shape/PredefinedMeshManager.class */
final class PredefinedMeshManager {
    private static final PredefinedMeshManager INSTANCE = new PredefinedMeshManager();
    private static final int INITAL_CAPACITY = 17;
    private static final float LOAD_FACTOR = 0.75f;
    private HashMap<Integer, TriangleMesh> boxCache = null;
    private HashMap<Integer, TriangleMesh> sphereCache = null;
    private HashMap<Integer, TriangleMesh> cylinderCache = null;

    private PredefinedMeshManager() {
    }

    static PredefinedMeshManager getInstance() {
        return INSTANCE;
    }

    synchronized TriangleMesh getBoxMesh(float w2, float h2, float d2, int key) {
        if (this.boxCache == null) {
            this.boxCache = BoxCacheLoader.INSTANCE;
        }
        TriangleMesh mesh = this.boxCache.get(Integer.valueOf(key));
        if (mesh == null) {
            mesh = Box.createMesh(w2, h2, d2);
            this.boxCache.put(Integer.valueOf(key), mesh);
        } else {
            mesh.incRef();
        }
        return mesh;
    }

    synchronized TriangleMesh getSphereMesh(float r2, int div, int key) {
        if (this.sphereCache == null) {
            this.sphereCache = SphereCacheLoader.INSTANCE;
        }
        TriangleMesh mesh = this.sphereCache.get(Integer.valueOf(key));
        if (mesh == null) {
            mesh = Sphere.createMesh(div, r2);
            this.sphereCache.put(Integer.valueOf(key), mesh);
        } else {
            mesh.incRef();
        }
        return mesh;
    }

    synchronized TriangleMesh getCylinderMesh(float h2, float r2, int div, int key) {
        if (this.cylinderCache == null) {
            this.cylinderCache = CylinderCacheLoader.INSTANCE;
        }
        TriangleMesh mesh = this.cylinderCache.get(Integer.valueOf(key));
        if (mesh == null) {
            mesh = Cylinder.createMesh(div, h2, r2);
            this.cylinderCache.put(Integer.valueOf(key), mesh);
        } else {
            mesh.incRef();
        }
        return mesh;
    }

    synchronized void invalidateBoxMesh(int key) {
        TriangleMesh mesh;
        if (this.boxCache != null && (mesh = this.boxCache.get(Integer.valueOf(key))) != null) {
            mesh.decRef();
            int count = mesh.getRefCount();
            if (count == 0) {
                this.boxCache.remove(Integer.valueOf(key));
            }
        }
    }

    synchronized void invalidateSphereMesh(int key) {
        TriangleMesh mesh;
        if (this.sphereCache != null && (mesh = this.sphereCache.get(Integer.valueOf(key))) != null) {
            mesh.decRef();
            int count = mesh.getRefCount();
            if (count == 0) {
                this.sphereCache.remove(Integer.valueOf(key));
            }
        }
    }

    synchronized void invalidateCylinderMesh(int key) {
        TriangleMesh mesh;
        if (this.cylinderCache != null && (mesh = this.cylinderCache.get(Integer.valueOf(key))) != null) {
            mesh.decRef();
            int count = mesh.getRefCount();
            if (count == 0) {
                this.cylinderCache.remove(Integer.valueOf(key));
            }
        }
    }

    synchronized void dispose() {
        if (this.boxCache != null) {
            this.boxCache.clear();
        }
        if (this.sphereCache != null) {
            this.sphereCache.clear();
        }
        if (this.cylinderCache != null) {
            this.cylinderCache.clear();
        }
    }

    synchronized void printStats() {
        if (this.boxCache != null) {
            System.out.println("BoxCache size:  " + this.boxCache.size());
        }
        if (this.sphereCache != null) {
            System.out.println("SphereCache size:    " + this.sphereCache.size());
        }
        if (this.cylinderCache != null) {
            System.out.println("CylinderCache size:    " + this.cylinderCache.size());
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/shape/PredefinedMeshManager$BoxCacheLoader.class */
    private static final class BoxCacheLoader {
        private static final HashMap<Integer, TriangleMesh> INSTANCE = new HashMap<>(17, 0.75f);

        private BoxCacheLoader() {
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/shape/PredefinedMeshManager$SphereCacheLoader.class */
    private static final class SphereCacheLoader {
        private static final HashMap<Integer, TriangleMesh> INSTANCE = new HashMap<>(17, 0.75f);

        private SphereCacheLoader() {
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/shape/PredefinedMeshManager$CylinderCacheLoader.class */
    private static final class CylinderCacheLoader {
        private static final HashMap<Integer, TriangleMesh> INSTANCE = new HashMap<>(17, 0.75f);

        private CylinderCacheLoader() {
        }
    }
}
