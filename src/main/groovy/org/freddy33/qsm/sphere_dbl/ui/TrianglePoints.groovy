/**
 * @author Fred Simon
 */
package org.freddy33.qsm.sphere_dbl.ui

import org.freddy33.math.dbl.Coord4d
import org.freddy33.math.MathUtils
import org.freddy33.qsm.sphere_dbl.SpaceTimeDouble
import org.jzy3d.chart.Chart
import org.jzy3d.colors.Color
import org.jzy3d.maths.Coord3d
import org.jzy3d.maths.TicToc
import org.jzy3d.plot3d.primitives.Scatter
import org.jzy3d.plot3d.rendering.view.Renderer2d
import org.jzy3d.ui.ChartLauncher

import java.awt.Graphics2D
import java.awt.Rectangle

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
SpaceTimeDouble spaceTime = new SpaceTimeDouble((int) ratio)
float bigDist = (float) ratio * MathUtils.sin120 * 2f
int nextInt = 1 + (int) bigDist
float nextX = (float) Math.sqrt((nextInt * nextInt) - (ratio * ratio))

/*
for (int i = 1; i <= 3; i++) {
    for (int j = -1; j <= 1; j++) {
        for (int k = -1; k <= 1; k++) {
            spaceTime.addPhoton(
                    new Coord4d((float) (i * nextX), ratio * j, ratio * k),
                    new Vector3d(-1d, 0d, 0d),
                    new Vector3d(0d, 0d, 1d),
                    ratio
            )
        }
    }
}
*/

Coord3d[] calcPoints(SpaceTimeDouble st) {
    List<Coord4d> points = st.currentPoints()
    points.addAll(st.fixedPoints)
    points.collect { new Coord3d(it.x, it.y, it.z) }.toArray(new Coord3d[0])
}

scatter.setData(calcPoints(spaceTime))
def chart = buildChart(scatter)
ChartLauncher.openChart(chart, rectangle, "Triangles", false)

Thread.start {
    int next = 0
    while (!G.var.keyHit) {
        next++
        sleep 25
        tt.tic()
        scatter.setData(calcPoints(spaceTime))
        chart.render()
        spaceTime.manyCalc(10)
        tt.toc()
        G.var.fpsText = String.format('%4d: %.4f FPS', spaceTime.currentTime, 1.0 / tt.elapsedSecond())
    }
}
