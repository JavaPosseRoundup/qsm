package org.freddy33.math.bigInt

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 6/12/12
 * Time: 10:36 AM
 * To change this template use File | Settings | File Templates.
 */

class Calculator {
    public static final double sqrt3 = Math.sqrt(3d)
    public static final double sqrt24 = Math.sqrt(24d)
    public static final double sqrt8third = Math.sqrt(8d / 3d)
    public static final double sqrt6over1PlusSqrt8 = Math.sqrt(6d) / (1d+Math.sqrt(8d))

    static int calcAPrimeCircle(int a) {
        def r=(double)a/sqrt3
        def c=(double)a/sqrt24
        def tp=Math.ceil(r)
        def b=Math.sqrt(tp*tp-(a*a/3d))
        def Rp=b+c
        def ap=Rp*sqrt8third
        def floorAp = Math.floor(ap)
        if ((ap-floorAp)/floorAp < 1e-10) {
            //println "$a,$r,$c,$tp,$b,$Rp,$ap,$floorAp"
            return (int) floorAp
        }
        return 0
    }

    static BigInteger calcAPrimeK1(BigInteger a) {
        BigInteger tp=(BigInteger)Math.ceil(sqrt6over1PlusSqrt8*a.toDouble())
        BigInteger inSqrt = 8G*(3G*tp*tp - a*a)
        // Needs to check pure square
        BigInteger sq=(BigInteger)Math.sqrt(inSqrt.toDouble())
        if (sq*sq == inSqrt) {
            // Check sq+a dividable by 3
            if ((sq+a) % 3G == 0) {
                def c=((double)a)/sqrt24
                def ap = (sq+a)/3G
                println "$a,$c,$tp,$sq,$ap"
                return a
            }
        }
        return 0G
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
        def max=100000000G
        println "Finding int of a' for k=1 giving integer a'"
        for (BigInteger a=1G;a<max;a++) {
            calcAPrimeK1(a)
        }
    }

    static def showColors() {
        // Create all combination of 4 colors
        def cVals = EventColor.values()
        List<EventColor[]> blocks = []
        (0..3).each { one ->
            (0..3).each { two ->
                (0..3).each { three ->
                    (0..3).each { four ->
                        def newBlock = [cVals[one], cVals[two], cVals[three], cVals[four]]
                        if (!blocks.any { EventColor.sameBlock(it,newBlock)}) blocks << newBlock
                    }
                }
            }
        }
        List<EventColorBlockCycle> cycles = []
        blocks.each {
            def c = new EventColorBlockCycle(it)
            if (!cycles.contains(c)) cycles << c
        }

        cycles.eachWithIndex { c, i ->
            println "${i+1}$c"
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
            EventCharge[] nb = EventCharge.nextBlock(b)
            println " ,${nb.join(",")},${EventCharge.totalValue(nb)}"
            nb = EventCharge.nextBlock(nb)
            println " ,${nb.join(",")},${EventCharge.totalValue(nb)}"
            nb = EventCharge.nextBlock(nb)
            println " ,${nb.join(",")},${EventCharge.totalValue(nb)}"
        }
    }
}

Calculator.showColors()