package javafx.scene.paint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.beans.NamedArg;
import javafx.fxml.FXMLLoader;

/* loaded from: jfxrt.jar:javafx/scene/paint/Stop.class */
public final class Stop {
    static final List<Stop> NO_STOPS = Collections.unmodifiableList(Arrays.asList(new Stop(0.0d, Color.TRANSPARENT), new Stop(1.0d, Color.TRANSPARENT)));
    private double offset;
    private Color color;
    private int hash = 0;

    static List<Stop> normalize(Stop[] stops) {
        List<Stop> stoplist = stops == null ? null : Arrays.asList(stops);
        return normalize(stoplist);
    }

    static List<Stop> normalize(List<Stop> stops) {
        Color zerocolor;
        if (stops == null) {
            return NO_STOPS;
        }
        Stop zerostop = null;
        Stop onestop = null;
        List<Stop> newlist = new ArrayList<>(stops.size());
        Iterator<Stop> it = stops.iterator();
        while (it.hasNext()) {
            Stop s2 = it.next();
            if (s2 != null && s2.getColor() != null) {
                double off = s2.getOffset();
                if (off <= 0.0d) {
                    if (zerostop == null || off >= zerostop.getOffset()) {
                        zerostop = s2;
                    }
                } else if (off >= 1.0d) {
                    if (onestop == null || off < onestop.getOffset()) {
                        onestop = s2;
                    }
                } else if (off == off) {
                    int i2 = newlist.size() - 1;
                    while (true) {
                        if (i2 < 0) {
                            break;
                        }
                        Stop s22 = newlist.get(i2);
                        if (s22.getOffset() > off) {
                            i2--;
                        } else {
                            if (s22.getOffset() == off && i2 > 0 && newlist.get(i2 - 1).getOffset() == off) {
                                newlist.set(i2, s2);
                            } else {
                                newlist.add(i2 + 1, s2);
                            }
                            s2 = null;
                        }
                    }
                    if (s2 != null) {
                        newlist.add(0, s2);
                    }
                }
            }
        }
        if (zerostop == null) {
            if (newlist.isEmpty()) {
                if (onestop == null) {
                    return NO_STOPS;
                }
                zerocolor = onestop.getColor();
            } else {
                zerocolor = newlist.get(0).getColor();
                if (onestop == null && newlist.size() == 1) {
                    newlist.clear();
                }
            }
            zerostop = new Stop(0.0d, zerocolor);
        } else if (zerostop.getOffset() < 0.0d) {
            zerostop = new Stop(0.0d, zerostop.getColor());
        }
        newlist.add(0, zerostop);
        if (onestop == null) {
            onestop = new Stop(1.0d, newlist.get(newlist.size() - 1).getColor());
        } else if (onestop.getOffset() > 1.0d) {
            onestop = new Stop(1.0d, onestop.getColor());
        }
        newlist.add(onestop);
        return Collections.unmodifiableList(newlist);
    }

    public final double getOffset() {
        return this.offset;
    }

    public final Color getColor() {
        return this.color;
    }

    public Stop(@NamedArg("offset") double offset, @NamedArg(value = "color", defaultValue = "BLACK") Color color) {
        this.offset = offset;
        this.color = color;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj instanceof Stop) {
            Stop other = (Stop) obj;
            return this.offset == other.offset && (this.color != null ? this.color.equals(other.color) : other.color == null);
        }
        return false;
    }

    public int hashCode() {
        if (this.hash == 0) {
            long bits = (37 * ((37 * 17) + Double.doubleToLongBits(this.offset))) + this.color.hashCode();
            this.hash = (int) (bits ^ (bits >> 32));
        }
        return this.hash;
    }

    public String toString() {
        return ((Object) this.color) + " " + (this.offset * 100.0d) + FXMLLoader.RESOURCE_KEY_PREFIX;
    }
}
