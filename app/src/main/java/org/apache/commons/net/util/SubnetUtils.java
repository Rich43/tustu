package org.apache.commons.net.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/util/SubnetUtils.class */
public class SubnetUtils {
    private static final int NBITS = 32;
    private int netmask = 0;
    private int address = 0;
    private int network = 0;
    private int broadcast = 0;
    private boolean inclusiveHostCount = false;
    private static final String IP_ADDRESS = "(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})";
    private static final Pattern addressPattern = Pattern.compile(IP_ADDRESS);
    private static final String SLASH_FORMAT = "(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})/(\\d{1,3})";
    private static final Pattern cidrPattern = Pattern.compile(SLASH_FORMAT);

    public SubnetUtils(String cidrNotation) {
        calculate(cidrNotation);
    }

    public SubnetUtils(String address, String mask) {
        calculate(toCidrNotation(address, mask));
    }

    public boolean isInclusiveHostCount() {
        return this.inclusiveHostCount;
    }

    public void setInclusiveHostCount(boolean inclusiveHostCount) {
        this.inclusiveHostCount = inclusiveHostCount;
    }

    /* loaded from: commons-net-3.6.jar:org/apache/commons/net/util/SubnetUtils$SubnetInfo.class */
    public final class SubnetInfo {
        private static final long UNSIGNED_INT_MASK = 4294967295L;

        private SubnetInfo() {
        }

        private int netmask() {
            return SubnetUtils.this.netmask;
        }

        private int network() {
            return SubnetUtils.this.network;
        }

        private int address() {
            return SubnetUtils.this.address;
        }

        private int broadcast() {
            return SubnetUtils.this.broadcast;
        }

        private long networkLong() {
            return SubnetUtils.this.network & 4294967295L;
        }

        private long broadcastLong() {
            return SubnetUtils.this.broadcast & 4294967295L;
        }

        private int low() {
            if (SubnetUtils.this.isInclusiveHostCount()) {
                return network();
            }
            if (broadcastLong() - networkLong() > 1) {
                return network() + 1;
            }
            return 0;
        }

        private int high() {
            if (SubnetUtils.this.isInclusiveHostCount()) {
                return broadcast();
            }
            if (broadcastLong() - networkLong() > 1) {
                return broadcast() - 1;
            }
            return 0;
        }

        public boolean isInRange(String address) {
            return isInRange(SubnetUtils.this.toInteger(address));
        }

        public boolean isInRange(int address) {
            long addLong = address & 4294967295L;
            long lowLong = low() & 4294967295L;
            long highLong = high() & 4294967295L;
            return addLong >= lowLong && addLong <= highLong;
        }

        public String getBroadcastAddress() {
            return SubnetUtils.this.format(SubnetUtils.this.toArray(broadcast()));
        }

        public String getNetworkAddress() {
            return SubnetUtils.this.format(SubnetUtils.this.toArray(network()));
        }

        public String getNetmask() {
            return SubnetUtils.this.format(SubnetUtils.this.toArray(netmask()));
        }

        public String getAddress() {
            return SubnetUtils.this.format(SubnetUtils.this.toArray(address()));
        }

        public String getLowAddress() {
            return SubnetUtils.this.format(SubnetUtils.this.toArray(low()));
        }

        public String getHighAddress() {
            return SubnetUtils.this.format(SubnetUtils.this.toArray(high()));
        }

        @Deprecated
        public int getAddressCount() {
            long countLong = getAddressCountLong();
            if (countLong > 2147483647L) {
                throw new RuntimeException("Count is larger than an integer: " + countLong);
            }
            return (int) countLong;
        }

        public long getAddressCountLong() {
            long b2 = broadcastLong();
            long n2 = networkLong();
            long count = (b2 - n2) + (SubnetUtils.this.isInclusiveHostCount() ? 1 : -1);
            if (count < 0) {
                return 0L;
            }
            return count;
        }

        public int asInteger(String address) {
            return SubnetUtils.this.toInteger(address);
        }

        public String getCidrSignature() {
            return SubnetUtils.this.toCidrNotation(SubnetUtils.this.format(SubnetUtils.this.toArray(address())), SubnetUtils.this.format(SubnetUtils.this.toArray(netmask())));
        }

        public String[] getAllAddresses() {
            int ct = getAddressCount();
            String[] addresses = new String[ct];
            if (ct == 0) {
                return addresses;
            }
            int add = low();
            int j2 = 0;
            while (add <= high()) {
                addresses[j2] = SubnetUtils.this.format(SubnetUtils.this.toArray(add));
                add++;
                j2++;
            }
            return addresses;
        }

        public String toString() {
            StringBuilder buf = new StringBuilder();
            buf.append("CIDR Signature:\t[").append(getCidrSignature()).append("]").append(" Netmask: [").append(getNetmask()).append("]\n").append("Network:\t[").append(getNetworkAddress()).append("]\n").append("Broadcast:\t[").append(getBroadcastAddress()).append("]\n").append("First Address:\t[").append(getLowAddress()).append("]\n").append("Last Address:\t[").append(getHighAddress()).append("]\n").append("# Addresses:\t[").append(getAddressCount()).append("]\n");
            return buf.toString();
        }
    }

    public final SubnetInfo getInfo() {
        return new SubnetInfo();
    }

    private void calculate(String mask) {
        Matcher matcher = cidrPattern.matcher(mask);
        if (matcher.matches()) {
            this.address = matchAddress(matcher);
            int cidrPart = rangeCheck(Integer.parseInt(matcher.group(5)), 0, 32);
            for (int j2 = 0; j2 < cidrPart; j2++) {
                this.netmask |= 1 << (31 - j2);
            }
            this.network = this.address & this.netmask;
            this.broadcast = this.network | (this.netmask ^ (-1));
            return;
        }
        throw new IllegalArgumentException("Could not parse [" + mask + "]");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int toInteger(String address) {
        Matcher matcher = addressPattern.matcher(address);
        if (matcher.matches()) {
            return matchAddress(matcher);
        }
        throw new IllegalArgumentException("Could not parse [" + address + "]");
    }

    private int matchAddress(Matcher matcher) {
        int addr = 0;
        for (int i2 = 1; i2 <= 4; i2++) {
            int n2 = rangeCheck(Integer.parseInt(matcher.group(i2)), 0, 255);
            addr |= (n2 & 255) << (8 * (4 - i2));
        }
        return addr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int[] toArray(int val) {
        int[] ret = new int[4];
        for (int j2 = 3; j2 >= 0; j2--) {
            int i2 = j2;
            ret[i2] = ret[i2] | ((val >>> (8 * (3 - j2))) & 255);
        }
        return ret;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String format(int[] octets) {
        StringBuilder str = new StringBuilder();
        for (int i2 = 0; i2 < octets.length; i2++) {
            str.append(octets[i2]);
            if (i2 != octets.length - 1) {
                str.append(".");
            }
        }
        return str.toString();
    }

    private int rangeCheck(int value, int begin, int end) {
        if (value >= begin && value <= end) {
            return value;
        }
        throw new IllegalArgumentException("Value [" + value + "] not in range [" + begin + "," + end + "]");
    }

    int pop(int x2) {
        int x3 = x2 - ((x2 >>> 1) & 1431655765);
        int x4 = (x3 & 858993459) + ((x3 >>> 2) & 858993459);
        int x5 = (x4 + (x4 >>> 4)) & 252645135;
        int x6 = x5 + (x5 >>> 8);
        return (x6 + (x6 >>> 16)) & 63;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String toCidrNotation(String addr, String mask) {
        return addr + "/" + pop(toInteger(mask));
    }
}
