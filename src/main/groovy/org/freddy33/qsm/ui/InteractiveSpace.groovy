package org.freddy33.qsm.ui

import org.freddy33.qsm.space.SpaceTimeInt
import org.jzy3d.chart.Chart
import org.jzy3d.chart.controllers.mouse.ChartMouseController
import org.jzy3d.chart.controllers.thread.ChartThreadController
import org.jzy3d.colors.Color
import org.jzy3d.maths.Coord3d
import org.jzy3d.plot3d.primitives.interactive.InteractiveScatter
import org.jzy3d.plot3d.rendering.view.Renderer2d
import org.jzy3d.ui.Plugs

import java.awt.Graphics
import java.awt.Rectangle

def frame = new InteractiveSpaceFrame()
Plugs.frame(frame.createChart(), new Rectangle(0, 200, 400, 400), "Space Playground")

class InteractiveSpaceFrame {
    Chart chart
    InteractiveScatter scatter
    SpaceTimeInt st = new SpaceTimeInt(10)
    Renderer2d messageRenderer
    ChartThreadController threadCamera
    ChartMouseController mouseCamera
    boolean displayMessage = true
    String message

    def Chart createChart() {
        chart = new Chart("awt")
        chart.getScene().add(createScatter())
        chart.getView().setMaximized(true)

        threadCamera = new ChartThreadController(chart)
        mouseCamera = new ChartMouseController()
        mouseCamera.addSlaveThreadController(threadCamera)
        chart.addController(mouseCamera)

        message = "Interact by XXXX...";
        messageRenderer = new Renderer2d() {
            public void paint(Graphics g) {
                if (displayMessage && message != null) {
                    g.setColor(java.awt.Color.RED)
                    g.drawString(message, 10, 30)
                }
            }
        }
        chart.addRenderer(messageRenderer)
        chart
    }

    def InteractiveScatter createScatter() {
        def fp = st.fixedPoints.collect { new Coord3d(it.x.toDouble(), it.y.toDouble(), it.z.toDouble()) }
        fp.addAll(st.activeEvents.collect { new Coord3d(it.point.x.toDouble(), it.point.y.toDouble(), it.point.z.toDouble()) })
        def size = fp.size()
        def colors = new Color[size]
        for (int i = 0; i < size; i++) {
            if (i < 2)
                colors[i] = Color.BLACK
            else
                colors[i] = Color.GREEN
        }
        scatter = new InteractiveScatter(fp.toArray(new Coord3d[size]), colors)
        scatter.setWidth(3f)
        scatter
    }
}