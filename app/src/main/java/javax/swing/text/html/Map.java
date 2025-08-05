package javax.swing.text.html;

import java.awt.Polygon;
import java.io.Serializable;
import java.util.StringTokenizer;
import java.util.Vector;
import javafx.fxml.FXMLLoader;
import javax.swing.text.AttributeSet;
import javax.swing.text.html.HTML;

/* loaded from: rt.jar:javax/swing/text/html/Map.class */
class Map implements Serializable {
    private String name;
    private Vector<AttributeSet> areaAttributes;
    private Vector<RegionContainment> areas;

    /* loaded from: rt.jar:javax/swing/text/html/Map$RegionContainment.class */
    interface RegionContainment {
        boolean contains(int i2, int i3, int i4, int i5);
    }

    public Map() {
    }

    public Map(String str) {
        this.name = str;
    }

    public String getName() {
        return this.name;
    }

    public void addArea(AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }
        if (this.areaAttributes == null) {
            this.areaAttributes = new Vector<>(2);
        }
        this.areaAttributes.addElement(attributeSet.copyAttributes());
    }

    public void removeArea(AttributeSet attributeSet) {
        if (attributeSet != null && this.areaAttributes != null) {
            int size = this.areas != null ? this.areas.size() : 0;
            for (int size2 = this.areaAttributes.size() - 1; size2 >= 0; size2--) {
                if (this.areaAttributes.elementAt(size2).isEqual(attributeSet)) {
                    this.areaAttributes.removeElementAt(size2);
                    if (size2 < size) {
                        this.areas.removeElementAt(size2);
                    }
                }
            }
        }
    }

    public AttributeSet[] getAreas() {
        int size = this.areaAttributes != null ? this.areaAttributes.size() : 0;
        if (size != 0) {
            AttributeSet[] attributeSetArr = new AttributeSet[size];
            this.areaAttributes.copyInto(attributeSetArr);
            return attributeSetArr;
        }
        return null;
    }

    public AttributeSet getArea(int i2, int i3, int i4, int i5) {
        int size = this.areaAttributes != null ? this.areaAttributes.size() : 0;
        if (size > 0) {
            int size2 = this.areas != null ? this.areas.size() : 0;
            if (this.areas == null) {
                this.areas = new Vector<>(size);
            }
            for (int i6 = 0; i6 < size; i6++) {
                if (i6 >= size2) {
                    this.areas.addElement(createRegionContainment(this.areaAttributes.elementAt(i6)));
                }
                RegionContainment regionContainmentElementAt = this.areas.elementAt(i6);
                if (regionContainmentElementAt != null && regionContainmentElementAt.contains(i2, i3, i4, i5)) {
                    return this.areaAttributes.elementAt(i6);
                }
            }
            return null;
        }
        return null;
    }

    protected RegionContainment createRegionContainment(AttributeSet attributeSet) {
        Object attribute = attributeSet.getAttribute(HTML.Attribute.SHAPE);
        if (attribute == null) {
            attribute = "rect";
        }
        if (attribute instanceof String) {
            String lowerCase = ((String) attribute).toLowerCase();
            RegionContainment regionContainmentSharedInstance = null;
            try {
                if (lowerCase.equals("rect")) {
                    regionContainmentSharedInstance = new RectangleRegionContainment(attributeSet);
                } else if (lowerCase.equals("circle")) {
                    regionContainmentSharedInstance = new CircleRegionContainment(attributeSet);
                } else if (lowerCase.equals("poly")) {
                    regionContainmentSharedInstance = new PolygonRegionContainment(attributeSet);
                } else if (lowerCase.equals("default")) {
                    regionContainmentSharedInstance = DefaultRegionContainment.sharedInstance();
                }
            } catch (RuntimeException e2) {
                regionContainmentSharedInstance = null;
            }
            return regionContainmentSharedInstance;
        }
        return null;
    }

    protected static int[] extractCoords(Object obj) {
        int i2;
        if (obj == null || !(obj instanceof String)) {
            return null;
        }
        StringTokenizer stringTokenizer = new StringTokenizer((String) obj, ", \t\n\r");
        int[] iArr = null;
        int i3 = 0;
        while (stringTokenizer.hasMoreElements()) {
            String strNextToken = stringTokenizer.nextToken();
            if (strNextToken.endsWith(FXMLLoader.RESOURCE_KEY_PREFIX)) {
                i2 = -1;
                strNextToken = strNextToken.substring(0, strNextToken.length() - 1);
            } else {
                i2 = 1;
            }
            try {
                int i4 = Integer.parseInt(strNextToken);
                if (iArr == null) {
                    iArr = new int[4];
                } else if (i3 == iArr.length) {
                    int[] iArr2 = new int[iArr.length * 2];
                    System.arraycopy(iArr, 0, iArr2, 0, iArr.length);
                    iArr = iArr2;
                }
                int i5 = i3;
                i3++;
                iArr[i5] = i4 * i2;
            } catch (NumberFormatException e2) {
                return null;
            }
        }
        if (i3 > 0 && i3 != iArr.length) {
            int[] iArr3 = new int[i3];
            System.arraycopy(iArr, 0, iArr3, 0, i3);
            iArr = iArr3;
        }
        return iArr;
    }

    /* loaded from: rt.jar:javax/swing/text/html/Map$RectangleRegionContainment.class */
    static class RectangleRegionContainment implements RegionContainment {
        float[] percents;
        int lastWidth;
        int lastHeight;
        int x0;
        int y0;
        int x1;
        int y1;

        public RectangleRegionContainment(AttributeSet attributeSet) {
            int[] iArrExtractCoords = Map.extractCoords(attributeSet.getAttribute(HTML.Attribute.COORDS));
            this.percents = null;
            if (iArrExtractCoords == null || iArrExtractCoords.length != 4) {
                throw new RuntimeException("Unable to parse rectangular area");
            }
            this.x0 = iArrExtractCoords[0];
            this.y0 = iArrExtractCoords[1];
            this.x1 = iArrExtractCoords[2];
            this.y1 = iArrExtractCoords[3];
            if (this.x0 < 0 || this.y0 < 0 || this.x1 < 0 || this.y1 < 0) {
                this.percents = new float[4];
                this.lastHeight = -1;
                this.lastWidth = -1;
                for (int i2 = 0; i2 < 4; i2++) {
                    if (iArrExtractCoords[i2] < 0) {
                        this.percents[i2] = Math.abs(iArrExtractCoords[i2]) / 100.0f;
                    } else {
                        this.percents[i2] = -1.0f;
                    }
                }
            }
        }

        @Override // javax.swing.text.html.Map.RegionContainment
        public boolean contains(int i2, int i3, int i4, int i5) {
            if (this.percents == null) {
                return contains(i2, i3);
            }
            if (this.lastWidth != i4 || this.lastHeight != i5) {
                this.lastWidth = i4;
                this.lastHeight = i5;
                if (this.percents[0] != -1.0f) {
                    this.x0 = (int) (this.percents[0] * i4);
                }
                if (this.percents[1] != -1.0f) {
                    this.y0 = (int) (this.percents[1] * i5);
                }
                if (this.percents[2] != -1.0f) {
                    this.x1 = (int) (this.percents[2] * i4);
                }
                if (this.percents[3] != -1.0f) {
                    this.y1 = (int) (this.percents[3] * i5);
                }
            }
            return contains(i2, i3);
        }

        public boolean contains(int i2, int i3) {
            return i2 >= this.x0 && i2 <= this.x1 && i3 >= this.y0 && i3 <= this.y1;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/Map$PolygonRegionContainment.class */
    static class PolygonRegionContainment extends Polygon implements RegionContainment {
        float[] percentValues;
        int[] percentIndexs;
        int lastWidth;
        int lastHeight;

        public PolygonRegionContainment(AttributeSet attributeSet) {
            int[] iArrExtractCoords = Map.extractCoords(attributeSet.getAttribute(HTML.Attribute.COORDS));
            if (iArrExtractCoords == null || iArrExtractCoords.length == 0 || iArrExtractCoords.length % 2 != 0) {
                throw new RuntimeException("Unable to parse polygon area");
            }
            int i2 = 0;
            this.lastHeight = -1;
            this.lastWidth = -1;
            for (int length = iArrExtractCoords.length - 1; length >= 0; length--) {
                if (iArrExtractCoords[length] < 0) {
                    i2++;
                }
            }
            if (i2 > 0) {
                this.percentIndexs = new int[i2];
                this.percentValues = new float[i2];
                int i3 = 0;
                for (int length2 = iArrExtractCoords.length - 1; length2 >= 0; length2--) {
                    if (iArrExtractCoords[length2] < 0) {
                        this.percentValues[i3] = iArrExtractCoords[length2] / (-100.0f);
                        this.percentIndexs[i3] = length2;
                        i3++;
                    }
                }
            } else {
                this.percentIndexs = null;
                this.percentValues = null;
            }
            this.npoints = iArrExtractCoords.length / 2;
            this.xpoints = new int[this.npoints];
            this.ypoints = new int[this.npoints];
            for (int i4 = 0; i4 < this.npoints; i4++) {
                this.xpoints[i4] = iArrExtractCoords[i4 + i4];
                this.ypoints[i4] = iArrExtractCoords[i4 + i4 + 1];
            }
        }

        @Override // javax.swing.text.html.Map.RegionContainment
        public boolean contains(int i2, int i3, int i4, int i5) {
            if (this.percentValues == null || (this.lastWidth == i4 && this.lastHeight == i5)) {
                return contains(i2, i3);
            }
            this.bounds = null;
            this.lastWidth = i4;
            this.lastHeight = i5;
            float f2 = i4;
            float f3 = i5;
            for (int length = this.percentValues.length - 1; length >= 0; length--) {
                if (this.percentIndexs[length] % 2 == 0) {
                    this.xpoints[this.percentIndexs[length] / 2] = (int) (this.percentValues[length] * f2);
                } else {
                    this.ypoints[this.percentIndexs[length] / 2] = (int) (this.percentValues[length] * f3);
                }
            }
            return contains(i2, i3);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/Map$CircleRegionContainment.class */
    static class CircleRegionContainment implements RegionContainment {

        /* renamed from: x, reason: collision with root package name */
        int f12852x;

        /* renamed from: y, reason: collision with root package name */
        int f12853y;
        int radiusSquared;
        float[] percentValues;
        int lastWidth;
        int lastHeight;

        public CircleRegionContainment(AttributeSet attributeSet) {
            int[] iArrExtractCoords = Map.extractCoords(attributeSet.getAttribute(HTML.Attribute.COORDS));
            if (iArrExtractCoords == null || iArrExtractCoords.length != 3) {
                throw new RuntimeException("Unable to parse circular area");
            }
            this.f12852x = iArrExtractCoords[0];
            this.f12853y = iArrExtractCoords[1];
            this.radiusSquared = iArrExtractCoords[2] * iArrExtractCoords[2];
            if (iArrExtractCoords[0] < 0 || iArrExtractCoords[1] < 0 || iArrExtractCoords[2] < 0) {
                this.lastHeight = -1;
                this.lastWidth = -1;
                this.percentValues = new float[3];
                for (int i2 = 0; i2 < 3; i2++) {
                    if (iArrExtractCoords[i2] < 0) {
                        this.percentValues[i2] = iArrExtractCoords[i2] / (-100.0f);
                    } else {
                        this.percentValues[i2] = -1.0f;
                    }
                }
                return;
            }
            this.percentValues = null;
        }

        @Override // javax.swing.text.html.Map.RegionContainment
        public boolean contains(int i2, int i3, int i4, int i5) {
            if (this.percentValues != null && (this.lastWidth != i4 || this.lastHeight != i5)) {
                int iMin = Math.min(i4, i5) / 2;
                this.lastWidth = i4;
                this.lastHeight = i5;
                if (this.percentValues[0] != -1.0f) {
                    this.f12852x = (int) (this.percentValues[0] * i4);
                }
                if (this.percentValues[1] != -1.0f) {
                    this.f12853y = (int) (this.percentValues[1] * i5);
                }
                if (this.percentValues[2] != -1.0f) {
                    this.radiusSquared = (int) (this.percentValues[2] * Math.min(i4, i5));
                    this.radiusSquared *= this.radiusSquared;
                }
            }
            return ((i2 - this.f12852x) * (i2 - this.f12852x)) + ((i3 - this.f12853y) * (i3 - this.f12853y)) <= this.radiusSquared;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/Map$DefaultRegionContainment.class */
    static class DefaultRegionContainment implements RegionContainment {
        static DefaultRegionContainment si = null;

        DefaultRegionContainment() {
        }

        public static DefaultRegionContainment sharedInstance() {
            if (si == null) {
                si = new DefaultRegionContainment();
            }
            return si;
        }

        @Override // javax.swing.text.html.Map.RegionContainment
        public boolean contains(int i2, int i3, int i4, int i5) {
            return i2 <= i4 && i2 >= 0 && i3 >= 0 && i3 <= i4;
        }
    }
}
