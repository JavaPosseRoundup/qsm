package org.freddy33.math.bigInt

/**
 * User: freds
 * Date: 4/9/12
 * Time: 8:27 PM
 */
public enum EventColor {
    plus_1, minus_1, plus_i, minus_i;

    /**
     * Equals per permutation
     */
    public static boolean sameBlock(def e, def f) {
        e.permutations().any {
            f == it
        }
    }

    public static EventColor[] nextBlock(def e) {
        [
            nextColor(e[1], e[2], e[3]),
            nextColor(e[0], e[2], e[3]),
            nextColor(e[0], e[1], e[3]),
            nextColor(e[0], e[1], e[2])
        ]
    }

    public static EventColor nextColor(EventColor... e) {
        EventColor n = plus_1
        for (EventColor s : e) {
            n = n * s
        }
        n
    }

    public static int[] totalValue(def e) {
        int[] r=[0,0]
        for (EventColor s : e) {
            def v = s.value()
            r[0] += v[0]
            r[1] += v[1]
        }
        r
    }

    int[] value() {
        switch (this) {
            case plus_1: return [1,0]
            case minus_1: return [-1,0]
            case plus_i: return [0,1]
            case minus_i: return [0,-1]
        }
        return [0,0]
    }

    EventColor multiply(EventColor s) {
        if (this == plus_1) return s
        if (s == plus_1) return this
        if (this == minus_1) return -s
        if (s == minus_1) return -this

        // Now this and s are either +i or -i
        if (this == s) return minus_1
        if (this != s) return plus_1
    }

    EventColor negative() {
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

/**
 * Represent 4 cycles of a block of 4 rotating
 */
class EventColorBlockCycle {
    final List<List<EventColor>> cycles = []

    /**
     * From original block generate the sequence of 4
     * @param block original block
     */
    EventColorBlockCycle(def block) {
        cycles << block
        def nb = EventColor.nextBlock(block)
        cycles << nb
        nb = EventColor.nextBlock(nb)
        cycles << nb
        nb = EventColor.nextBlock(nb)
        cycles << nb
        // Verify we are good
        nb = EventColor.nextBlock(nb)
        if (nb != block) {
            throw new IllegalStateException("Group not cyclic after 4!?!")
        }
    }

    @Override
    boolean equals(Object obj) {
        EventColorBlockCycle o = obj
        cycles.any {
            EventColor.sameBlock(o.cycles[0], it)
        }
    }

    @Override
    String toString() {
        String res = ""
        cycles.each {
            def v = EventColor.totalValue(it)
            res += "\t${it.join(",")},(${display(v[0])},${display(v[1])})"
        }
        res
    }

    String display(int val) {
        if (val == 0) return " 0"
        if (val < 0) return "$val"
        if (val > 0) return "+$val"
        ""
    }
}

/**
 * Another type of permutation that does not work nicely...
 */
public enum EventCharge {
    p, m, n;

    public static EventCharge[] nextBlock(def b) {
        EventCharge[] nb = new EventCharge[4]
        nb[0] = EventCharge.nextCharge(b[1],b[2],b[3])
        nb[1] = EventCharge.nextCharge(b[0],b[2],b[3])
        nb[2] = EventCharge.nextCharge(b[1],b[0],b[3])
        nb[3] = EventCharge.nextCharge(b[1],b[2],b[0])
        nb
    }

    public static EventCharge nextCharge(EventCharge a, b, c) {
        if (a == n) return nextCharge(b,c)
        if (b == n) return nextCharge(a,c)
        if (c == n) return nextCharge(a,b)
        // If 3 equals go neutral
        if (a == b && b == c) return n

        // If 2 equals win over
        if (a == b) return a
        if (b == c) return b
        if (c == a) return c
        throw new IllegalStateException("Don't know how to enumerate!")
    }

    public static EventCharge nextCharge(EventCharge a, b) {
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
