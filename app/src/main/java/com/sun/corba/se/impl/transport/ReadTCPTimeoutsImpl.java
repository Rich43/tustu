package com.sun.corba.se.impl.transport;

import com.sun.corba.se.spi.transport.ReadTimeouts;

/* loaded from: rt.jar:com/sun/corba/se/impl/transport/ReadTCPTimeoutsImpl.class */
public class ReadTCPTimeoutsImpl implements ReadTimeouts {
    private int initial_time_to_wait;
    private int max_time_to_wait;
    private int max_giop_header_time_to_wait;
    private double backoff_factor;

    public ReadTCPTimeoutsImpl(int i2, int i3, int i4, int i5) {
        this.initial_time_to_wait = i2;
        this.max_time_to_wait = i3;
        this.max_giop_header_time_to_wait = i4;
        this.backoff_factor = 1.0d + (i5 / 100.0d);
    }

    @Override // com.sun.corba.se.spi.transport.ReadTimeouts
    public int get_initial_time_to_wait() {
        return this.initial_time_to_wait;
    }

    @Override // com.sun.corba.se.spi.transport.ReadTimeouts
    public int get_max_time_to_wait() {
        return this.max_time_to_wait;
    }

    @Override // com.sun.corba.se.spi.transport.ReadTimeouts
    public double get_backoff_factor() {
        return this.backoff_factor;
    }

    @Override // com.sun.corba.se.spi.transport.ReadTimeouts
    public int get_max_giop_header_time_to_wait() {
        return this.max_giop_header_time_to_wait;
    }
}
