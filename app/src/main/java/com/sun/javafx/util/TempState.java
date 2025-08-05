package com.sun.javafx.util;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;

/* loaded from: jfxrt.jar:com/sun/javafx/util/TempState.class */
public final class TempState {
    public BaseBounds bounds;
    public final BaseTransform pickTx;
    public final Affine3D leafTx;
    public final Point2D point;
    public final Vec3d vec3d;
    public final GeneralTransform3D projViewTx;
    public final Affine3D tempTx;
    private static final ThreadLocal<TempState> tempStateRef = new ThreadLocal<TempState>() { // from class: com.sun.javafx.util.TempState.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public TempState initialValue() {
            return new TempState();
        }
    };

    private TempState() {
        this.bounds = new RectBounds(0.0f, 0.0f, -1.0f, -1.0f);
        this.pickTx = new Affine3D();
        this.leafTx = new Affine3D();
        this.point = new Point2D(0.0f, 0.0f);
        this.vec3d = new Vec3d(0.0d, 0.0d, 0.0d);
        this.projViewTx = new GeneralTransform3D();
        this.tempTx = new Affine3D();
    }

    public static TempState getInstance() {
        return tempStateRef.get();
    }
}
