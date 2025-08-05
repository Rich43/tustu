package java.sql;

/* loaded from: rt.jar:java/sql/DriverPropertyInfo.class */
public class DriverPropertyInfo {
    public String name;
    public String value;
    public String description = null;
    public boolean required = false;
    public String[] choices = null;

    public DriverPropertyInfo(String str, String str2) {
        this.value = null;
        this.name = str;
        this.value = str2;
    }
}
