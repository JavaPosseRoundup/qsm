package org.freddy33.math.bigInt

import spock.lang.Specification

/**
 * User: freds
 * Date: 6/16/12
 * Time: 11:44 AM
 */
class EventAndChargeTest extends Specification {
    def "testColor"() {
        expect:
        totalCharges == EventColor.totalValue(charges)
        EventColor.sameBlock(charges, permut)
        nextCharges == EventColor.nextBlock(charges)
        nextPermut == EventColor.nextBlock(permut)
        new EventColorBlockCycle(charges) == new EventColorBlockCycle(nextCharges)
        new EventColorBlockCycle(charges) == new EventColorBlockCycle(permut)
        new EventColorBlockCycle(charges) == new EventColorBlockCycle(nextPermut)

        where:
        totalCharges << [
                [4, 0],
                [0, 0],
                [1, 3],
                [-1, -1],
                [0, 0],
                [0, 4],
        ]
        charges << [
                [EventColor.plus_1, EventColor.plus_1, EventColor.plus_1, EventColor.plus_1],
                [EventColor.plus_1, EventColor.minus_1, EventColor.plus_1, EventColor.minus_1],
                [EventColor.plus_1, EventColor.plus_i, EventColor.plus_i, EventColor.plus_i],
                [EventColor.minus_1, EventColor.minus_i, EventColor.minus_i, EventColor.plus_i],
                [EventColor.minus_1, EventColor.plus_i, EventColor.minus_i, EventColor.plus_1],
                [EventColor.plus_i, EventColor.plus_i, EventColor.plus_i, EventColor.plus_i],
        ]
        nextCharges << [
                [EventColor.plus_1, EventColor.plus_1, EventColor.plus_1, EventColor.plus_1],
                [EventColor.plus_1, EventColor.minus_1, EventColor.plus_1, EventColor.minus_1],
                [EventColor.minus_i, EventColor.minus_1, EventColor.minus_1, EventColor.minus_1],
                [EventColor.minus_i, EventColor.minus_1, EventColor.minus_1, EventColor.plus_1],
                [EventColor.plus_1, EventColor.plus_i, EventColor.minus_i, EventColor.minus_1],
                [EventColor.minus_i, EventColor.minus_i, EventColor.minus_i, EventColor.minus_i],
        ]
        permut << [
                [EventColor.plus_1, EventColor.plus_1, EventColor.plus_1, EventColor.plus_1],
                [EventColor.minus_1, EventColor.minus_1, EventColor.plus_1, EventColor.plus_1],
                [EventColor.plus_1, EventColor.plus_i, EventColor.plus_i, EventColor.plus_i],
                [EventColor.plus_i, EventColor.minus_1, EventColor.minus_i, EventColor.minus_i],
                [EventColor.plus_1, EventColor.plus_i, EventColor.minus_1, EventColor.minus_i],
                [EventColor.plus_i, EventColor.plus_i, EventColor.plus_i, EventColor.plus_i],
        ]
        nextPermut << [
                [EventColor.plus_1, EventColor.plus_1, EventColor.plus_1, EventColor.plus_1],
                [EventColor.minus_1, EventColor.minus_1, EventColor.plus_1, EventColor.plus_1],
                [EventColor.minus_i, EventColor.minus_1, EventColor.minus_1, EventColor.minus_1],
                [EventColor.plus_1, EventColor.minus_i, EventColor.minus_1, EventColor.minus_1],
                [EventColor.minus_1, EventColor.plus_i, EventColor.plus_1, EventColor.minus_i],
                [EventColor.minus_i, EventColor.minus_i, EventColor.minus_i, EventColor.minus_i],
        ]
    }
}
