package org.freddy33.math.bigInt

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 6/12/12
 * Time: 10:36 AM
 * To change this template use File | Settings | File Templates.
 */

class Calculator {
    public static final double sqrt3 = Math.sqrt(3)
    public static final double sqrt24 = Math.sqrt(24)
    public static final double sqrt8third = Math.sqrt(8 / 3)
    public static final double sqrt6over1PlusSqrt8 = Math.sqrt(6) / (1+Math.sqrt(8))

    static int calcAPrimeCircle(int a) {
        def r=(double)a/sqrt3
        def c=(double)a/sqrt24
        def tp=Math.ceil(r)
        def b=Math.sqrt(tp*tp-(a*a/3))
        def Rp=b+c
        def ap=Rp*sqrt8third
        def floorAp = Math.floor(ap)
        if ((ap-floorAp)/floorAp < 1e-10) {
            //println "$a,$r,$c,$tp,$b,$Rp,$ap,$floorAp"
            return (int) floorAp
        }
        return 0
    }

    static int calcAPrimeK1(int a) {
        def c=(double)a/sqrt24
        def tp=Math.ceil(sqrt6over1PlusSqrt8*a)
        def b=Math.sqrt(tp*tp-(a*a/3))
        def Rp=b+c
        def ap=Rp*sqrt8third
        def floorAp = Math.floor(ap)
        if ((ap-floorAp)/floorAp < 1e-10) {
            println "$a,$c,$tp,$b,$Rp,$ap,$floorAp"
            return (int) floorAp
        }
        return 0
    }

    static def showKCircle() {
        int max=120000000
        println "Finding a and a+1 giving integer a'"
        int previousValue=0
        for (int a=12000000;a<max;a++) {
            def ap = calcAPrimeCircle(a)
            if (ap != 0) {
                if (previousValue != 0) {
                    println "SUITE --- Found a=$a and ap=$ap for previous=$previousValue"
                }
                previousValue = ap
            } else {
                previousValue = 0
            }
        }
    }

    static def showK1() {
        int max=1200000
        println "Finding int of a' for k=1 giving integer a'"
        for (int a=1;a<max;a++) {
            calcAPrimeK1(a)
        }
    }

    static def showCharges() {
        // Create all combination of 4 charges
        def cVals = EventCharge.values()
        List<EventCharge[]> blocks = []
        (0..2).each { one ->
            (0..2).each { two ->
                (0..2).each { three ->
                    (0..2).each { four ->
                        blocks << [cVals[one], cVals[two], cVals[three], cVals[four]]
                    }
                }
            }
        }
        blocks.eachWithIndex { b, i ->
            println "$i"
            println " ,${b.join(",")},${EventCharge.totalValue(b)}"
            EventCharge[] nb = EventCharge.next(b)
            println " ,${nb.join(",")},${EventCharge.totalValue(nb)}"
            nb = EventCharge.next(nb)
            println " ,${nb.join(",")},${EventCharge.totalValue(nb)}"
            nb = EventCharge.next(nb)
            println " ,${nb.join(",")},${EventCharge.totalValue(nb)}"
        }
    }
}

Calculator.showCharges()