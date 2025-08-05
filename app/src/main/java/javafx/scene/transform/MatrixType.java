package javafx.scene.transform;

/* loaded from: jfxrt.jar:javafx/scene/transform/MatrixType.class */
public enum MatrixType {
    MT_2D_2x3(2, 3),
    MT_2D_3x3(3, 3),
    MT_3D_3x4(3, 4),
    MT_3D_4x4(4, 4);

    private int rows;
    private int cols;

    MatrixType(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public int elements() {
        return this.rows * this.cols;
    }

    public int rows() {
        return this.rows;
    }

    public int columns() {
        return this.cols;
    }

    public boolean is2D() {
        return this == MT_2D_2x3 || this == MT_2D_3x3;
    }
}
