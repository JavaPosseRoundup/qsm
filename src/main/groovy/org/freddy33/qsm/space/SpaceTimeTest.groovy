package org.freddy33.qsm.space

import org.freddy33.math.Coord4d
import org.freddy33.math.MathUtils
import org.freddy33.math.Triangle
import org.freddy33.math.Vector4d

def evt1 = new Event(0d, 0d, 0d, 3, new Vector4d(3d, 0d, 0d))
assert evt1.direction.x == 1d
assert evt1.direction.y == 0d
assert evt1.direction.z == 0d

def evt2 = new Event(0d, 0d, 1d, 3, new Vector4d(3d, 3d, 0d))
def s22 = Math.sqrt(2d) / 2d
assert evt2.direction.x == s22
assert evt2.direction.y == s22
assert evt2.direction.z == 0d

def sin120 = Math.sin(2d * Math.PI / 3d)
def cos120 = -0.5d
def evt3 = new Event(0d, sin120, cos120, 3, evt1.direction)
def evt4 = new Event(0d, -sin120, cos120, 3, evt1.direction)

// So 1,2,3,4 perfect triangle happening a t=3 moving in +x direction
def bigTr = new Triangle(evt2.point, evt3.point, evt4.point)
assert !bigTr.isFlat()
assert bigTr.findCenter() == new Coord4d(0d, 0d, 0d)
assert bigTr.finalDir(evt1.direction) == new Vector4d(1d, 0d, 0d)
assert MathUtils.eq(bigTr.finalDir(evt2.direction), new Vector4d(1d, 0d, 0d))
assert MathUtils.eq(bigTr.finalDir(-evt1.direction), new Vector4d(-1d, 0d, 0d))
assert MathUtils.eq(bigTr.finalDir(-evt2.direction), new Vector4d(-1d, 0d, 0d))
assert bigTr.findEvent(1, evt1.direction) == null
assert MathUtils.eq(bigTr.findEvent(2, evt1.direction), new Coord4d(Math.sqrt(3d), 0d, 0d))

def smallTr = new Triangle(evt1.point, evt3.point, evt4.point)
assert !smallTr.isFlat()
assert MathUtils.eq(smallTr.finalDir(evt1.direction), new Vector4d(1d, 0d, 0d))
assert MathUtils.eq(smallTr.finalDir(evt2.direction), new Vector4d(1d, 0d, 0d))
assert MathUtils.eq(smallTr.finalDir(-evt1.direction), new Vector4d(-1d, 0d, 0d))
assert MathUtils.eq(smallTr.finalDir(-evt2.direction), new Vector4d(-1d, 0d, 0d))
assert smallTr.findEvent(1, evt1.direction) == null
assert MathUtils.eq(smallTr.radius2(), 1d)
assert MathUtils.eq(smallTr.findCenter(), new Coord4d(0d, 0d, -1d))
assert MathUtils.eq(smallTr.findEvent(2, evt1.direction), new Coord4d(Math.sqrt(3d), 0d, -1d))

double ratio = 100d
def incTr = new Triangle(evt2.point * ratio, evt3.point * ratio, evt4.point * ratio)
assert !incTr.isFlat()
assert MathUtils.eq(incTr.findCenter(), new Coord4d(0d, 0d, 0d))
assert MathUtils.eq(incTr.finalDir(evt1.direction), new Vector4d(1d, 0d, 0d))
assert MathUtils.eq(incTr.finalDir(evt2.direction), new Vector4d(1d, 0d, 0d))
assert MathUtils.eq(incTr.finalDir(-evt1.direction), new Vector4d(-1d, 0d, 0d))
assert MathUtils.eq(incTr.finalDir(-evt2.direction), new Vector4d(-1d, 0d, 0d))
double bigDist = ratio * MathUtils.sin120 * 2d
int nextInt = (int) bigDist
double nextX = Math.sqrt((nextInt + 1) * (nextInt + 1) - (ratio * ratio))
assert incTr.findEvent(nextInt, evt1.direction) == null
assert MathUtils.eq(incTr.findEvent(nextInt + 1, evt1.direction), new Coord4d(nextX, 0d, 0d))

def incSmTr = new Triangle(evt1.point * ratio, evt3.point * ratio, evt4.point * ratio)
assert !incSmTr.isFlat()
assert MathUtils.eq(incSmTr.finalDir(evt1.direction), new Vector4d(1d, 0d, 0d))
assert MathUtils.eq(incSmTr.finalDir(evt2.direction), new Vector4d(1d, 0d, 0d))
assert MathUtils.eq(incSmTr.finalDir(-evt1.direction), new Vector4d(-1d, 0d, 0d))
assert MathUtils.eq(incSmTr.finalDir(-evt2.direction), new Vector4d(-1d, 0d, 0d))
assert incSmTr.findEvent(nextInt, evt1.direction) == null
assert MathUtils.eq(incSmTr.radius2(), ratio * ratio)
assert MathUtils.eq(incSmTr.findCenter(), new Coord4d(0d, 0d, -ratio))
assert MathUtils.eq(incSmTr.findEvent(nextInt + 1, evt1.direction), new Coord4d(nextX, 0d, -ratio))

def st = new SpaceTime((int) ratio)
assert st.spaces[0].events.size() == 4
Calculator calculator = new Calculator(st)
calculator.calc()
assert st.currentTime == 1
assert st.allEvents.size() == 4
assert st.spaces[0].events.size() == 4
for (int i = 2; i <= nextInt; i++) {
    calculator.calc()
    assert st.currentTime == i
    assert st.allEvents.size() == 4
    assert st.spaces[0].events.size() == 4
    assert st.spaces[i - 1] == null
    assert st.currentPoints().size() == 4
}
calculator.calc()
assert st.currentTime == nextInt + 1
assert st.spaces[st.currentTime].events.size() == 4
assert st.allEvents.size() == 4
assert st.spaces[0] == null
calculator.calc()
assert st.currentTime == nextInt + 2
assert st.spaces[nextInt + 1].events.size() == 4
assert st.spaces[0] == null
assert st.allEvents.size() == 8
def points = st.currentPoints()
assert points.size() == 4
assert points.any { MathUtils.eq(it, new Coord4d(nextX, 0d, -ratio)) }

for (int i = 3; i <= nextInt + 1; i++) {
    calculator.calc()
    assert st.currentTime == i + nextInt
    assert st.allEvents.size() == 8
    assert st.spaces[0] == null
    assert st.spaces[nextInt + 1].events.size() == 4
    assert st.currentPoints().size() == 4
}
calculator.calc()
assert st.currentTime == nextInt + nextInt + 2
assert st.spaces[st.currentTime].events.size() == 4
assert st.allEvents.size() == 8
assert st.spaces[nextInt + 1] == null
calculator.calc()
assert st.currentTime == nextInt + nextInt + 3
assert st.spaces[nextInt + nextInt + 2].events.size() == 4
assert st.spaces[nextInt + 1] == null
assert st.allEvents.size() == 12
points = st.currentPoints()
assert points.size() == 4
assert points.any { MathUtils.eq(it, new Coord4d(nextX + nextX, 0d, ratio)) }

