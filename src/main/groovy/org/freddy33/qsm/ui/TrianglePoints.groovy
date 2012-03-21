/**
 * @author Fred Simon
 */
package org.freddy33.qsm.ui

//import java.awt.Graphics2D
//import java.awt.Rectangle
//import org.jzy3d.chart.Chart
//import org.jzy3d.colors.Color
//import org.jzy3d.maths.Coord3d
//import org.jzy3d.maths.TicToc
//import org.jzy3d.plot3d.primitives.Scatter
//import org.jzy3d.plot3d.rendering.view.Renderer2d
//import org.jzy3d.ui.ChartLauncher
//import org.freddy33.qsm.space.Calculator
//import org.freddy33.math.MathUtils
//import org.freddy33.qsm.space.SpaceTime

// G.var provides Global container for script vars
class G {
    static var = [:]
}

/*
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
                    new Coord3d((float)(i * nextX), ratio*j, ratio*k),
                    new Coord3d(-1f, 0f, 0f),
                    new Coord3d(0f, 0f, 1f),
                    ratio
            )
        }
    }
}
SpaceTime.addPhoton(calculator.spaceTime.spaces[0],
        new Coord3d(-3f * nextX, 0f, 0f),
        new Coord3d(1f, 0f, 0f),
        new Coord3d(0f, 0f, 1f),
        ratio
)
SpaceTime.addPhoton(calculator.spaceTime.spaces[0],
        new Coord3d(0f, -3f * nextX, 0f),
        new Coord3d(0f, 1f, 0f),
        new Coord3d(1f, 0f, 0f),
        ratio
)
SpaceTime.addPhoton(calculator.spaceTime.spaces[0],
        new Coord3d(0f, 3f * nextX, 0f),
        new Coord3d(0f, -1f, 0f),
        new Coord3d(1f, 0f, 0f),
        ratio
)
SpaceTime.addPhoton(calculator.spaceTime.spaces[0],
        new Coord3d(0f, 0f, -3f * nextX),
        new Coord3d(0f, 0f, 1f),
        new Coord3d(0f, 1f, 0f),
        ratio
)
SpaceTime.addPhoton(calculator.spaceTime.spaces[0],
        new Coord3d(ratio, 0f, -3f * nextX),
        new Coord3d(0f, 0f, 1f),
        new Coord3d(0f, 1f, 0f),
        ratio
)
SpaceTime.addPhoton(calculator.spaceTime.spaces[0],
        new Coord3d(0f, ratio, -3f * nextX),
        new Coord3d(0f, 0f, 1f),
        new Coord3d(0f, 1f, 0f),
        ratio
)
SpaceTime.addPhoton(calculator.spaceTime.spaces[0],
        new Coord3d(ratio, ratio, -3f * nextX),
        new Coord3d(0f, 0f, 1f),
        new Coord3d(0f, 1f, 0f),
        ratio
)
SpaceTime.addPhoton(calculator.spaceTime.spaces[0],
        new Coord3d(-ratio, 0f, -3f * nextX),
        new Coord3d(0f, 0f, 1f),
        new Coord3d(0f, 1f, 0f),
        ratio
)
SpaceTime.addPhoton(calculator.spaceTime.spaces[0],
        new Coord3d(0f, -ratio, -3f * nextX),
        new Coord3d(0f, 0f, 1f),
        new Coord3d(0f, 1f, 0f),
        ratio
)
SpaceTime.addPhoton(calculator.spaceTime.spaces[0],
        new Coord3d(-ratio, -ratio, -3f * nextX),
        new Coord3d(0f, 0f, 1f),
        new Coord3d(0f, 1f, 0f),
        ratio
)
SpaceTime.addPhoton(calculator.spaceTime.spaces[0],
        new Coord3d(0f, 0f, 3f * nextX),
        new Coord3d(0f, 0f, -1f),
        new Coord3d(0f, 1f, 0f),
        ratio
)
scatter.setData(calculator.currentPoints())
def chart = buildChart(scatter)
ChartLauncher.openChart(chart, rectangle, "Triangles", false)

Thread.start {
    int next = 0
    while (!G.var.keyHit) {
        next++
        sleep 25
        tt.tic()
        scatter.setData(calculator.currentPoints())
        chart.render()
        calculator.manyCalc(10)
        tt.toc()
        G.var.fpsText = String.format('%4d: %.4f FPS', calculator.spaceTime.currentTime, 1.0 / tt.elapsedSecond())
    }
}
*/
