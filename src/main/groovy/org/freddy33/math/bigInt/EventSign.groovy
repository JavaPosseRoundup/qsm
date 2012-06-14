package org.freddy33.math.bigInt

/**
 * User: freds
 * Date: 4/9/12
 * Time: 8:27 PM
 */
public enum EventSign {
    plus_1, minus_1, plus_i, minus_i;

    EventSign multiply(EventSign s) {
        if (this == plus_1) return s
        if (s == plus_1) return this
        if (this == minus_1) return -s
        if (s == minus_1) return -this

        // Now this and s are either +i or -i
        if (this == s) return minus_1
        if (this != s) return plus_1
    }

    EventSign negative() {
        switch (this) {
            case plus_1: return minus_1;
            case minus_1: return plus_1;
            case plus_i: return minus_i;
            case minus_i: return plus_i;
        }
        return null
    }

    @Override
    String toString() {
        switch (this) {
            case plus_1: return "+1";
            case minus_1: return "-1";
            case plus_i: return "+i";
            case minus_i: return "-i";
        }
        return super.toString()
    }
}

public enum EventCharge {
    p, m, n;

    public static EventCharge[] next(def b) {
        EventCharge[] nb = new EventCharge[4]
        nb[0] = EventCharge.next(b[1],b[2],b[3])
        nb[1] = EventCharge.next(b[0],b[2],b[3])
        nb[2] = EventCharge.next(b[1],b[0],b[3])
        nb[3] = EventCharge.next(b[1],b[2],b[0])
        nb
    }

    public static EventCharge next(EventCharge a, b, c) {
        if (a == n) return next(b,c)
        if (b == n) return next(a,c)
        if (c == n) return next(a,b)
        // If 2 equals win over
        if (a == b) return a
        if (b == c) return b
        if (c == a) return c
        throw new IllegalStateException("Don't know how to enumerate!")
    }

    public static EventCharge next(EventCharge a, b) {
        if (a == n) return b
        if (b == n) return a
        if (a == b) return a
        if (a != b) return n
        throw new IllegalStateException("Don't know how to enumerate!")
    }

    public static int totalValue(def charges) {
        int res = 0
        for (EventCharge c : charges) {
            switch (c) {
                case p: res++; break;
                case m: res--; break;
                case n: break;
            }
        }
        res
    }
}
