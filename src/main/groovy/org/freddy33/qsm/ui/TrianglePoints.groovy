/**
 * @author Fred Simon
 */
package org.freddy33.qsm.ui

import java.awt.Graphics2D
import java.awt.Rectangle
import org.jzy3d.chart.Chart
import org.jzy3d.colors.Color
import org.jzy3d.maths.Coord3d
import org.jzy3d.maths.TicToc
import org.jzy3d.plot3d.primitives.Scatter
import org.jzy3d.plot3d.rendering.view.Renderer2d
import org.jzy3d.ui.ChartLauncher
import org.freddy33.qsm.space.Calculator
import org.freddy33.math.MathUtils
import org.freddy33.qsm.space.SpaceTime
import org.freddy33.math.Coord4d

// G.var provides Global container for script vars
class G {
    static var = [:]
}

def buildChart(Scatter sc) {
    def chart = new Chart()
    sc.width = 5
    sc.color = Color.GREEN
    chart.getScene().add(sc)

    chart.addRenderer(
            {g ->
                Graphics2D g2d = (Graphics2D) g
                g2d.setColor(java.awt.Color.BLACK)
                g2d.drawString(G.var.fpsText, 50, 50)
            } as Renderer2d)

    return chart
}

G.var.fpsText = "0 FPS"
G.var.keyHit = false
def rectangle = new Rectangle(600, 600)
def tt = new TicToc()
Scatter scatter = new Scatter()
float ratio = 100f
Calculator calculator = new Calculator((int) ratio)
float bigDist = (float) ratio * MathUtils.sin120 * 2f
int nextInt = 1 + (int) bigDist
float nextX = (float) Math.sqrt((nextInt * nextInt) - (ratio * ratio))

for (int i = 1; i <= 3; i++) {
    for (int j = -1; j <= 1; j++) {
        for (int k = -1; k <= 1; k++) {
            SpaceTime.addPhoton(calculator.spaceTime.spaces[0],
                    new Coord4d((float)(i * nextX), ratio*j, ratio*k),
                    new Coord4d(-1f, 0f, 0f),
                    new Coord4d(0f, 0f, 1f),
                    ratio
            )
        }
    }
}

Coord3d[] calcPoints(Calculator calc) {
    List<Coord4d> points = calc.spaceTime.currentPoints()
    points.collect { new Coord3d(it.x, it.y, it.y) }.toArray(new Coord3d[0])
}

scatter.setData(calcPoints(calculator))
def chart = buildChart(scatter)
ChartLauncher.openChart(chart, rectangle, "Triangles", false)

Thread.start {
    int next = 0
    while (!G.var.keyHit) {
        next++
        sleep 25
        tt.tic()
        scatter.setData(calcPoints(calculator))
        chart.render()
        calculator.manyCalc(10)
        tt.toc()
        G.var.fpsText = String.format('%4d: %.4f FPS', calculator.spaceTime.currentTime, 1.0 / tt.elapsedSecond())
    }
}
