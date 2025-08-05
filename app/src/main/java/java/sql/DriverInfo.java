package java.sql;

/* compiled from: DriverManager.java */
/* loaded from: rt.jar:java/sql/DriverInfo.class */
class DriverInfo {
    final Driver driver;
    DriverAction da;

    DriverInfo(Driver driver, DriverAction driverAction) {
        this.driver = driver;
        this.da = driverAction;
    }

    public boolean equals(Object obj) {
        return (obj instanceof DriverInfo) && this.driver == ((DriverInfo) obj).driver;
    }

    public int hashCode() {
        return this.driver.hashCode();
    }

    public String toString() {
        return "driver[className=" + ((Object) this.driver) + "]";
    }

    DriverAction action() {
        return this.da;
    }
}
