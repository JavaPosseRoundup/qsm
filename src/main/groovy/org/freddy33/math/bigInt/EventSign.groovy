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
