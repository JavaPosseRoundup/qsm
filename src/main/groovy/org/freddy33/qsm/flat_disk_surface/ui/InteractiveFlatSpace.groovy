package org.freddy33.qsm.flat_disk_surface.ui

import org.jzy3d.chart.Chart
import org.jzy3d.chart.controllers.mouse.ChartMouseController
import org.jzy3d.chart.controllers.thread.ChartThreadController
import org.jzy3d.plot3d.rendering.view.Renderer2d
import org.jzy3d.ui.Plugs

import java.awt.Graphics
import java.awt.Rectangle
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

def frame = new InteractiveFlatSpaceFrame()
Plugs.frame(frame.createChart(), new Rectangle(0, 200, 400, 400), "Space Playground")

class InteractiveFlatSpaceFrame {
    Chart chart
    InteractiveFlatScatter scatter
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

        chart.getCanvas().addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {}

            public void keyReleased(KeyEvent e) {}

            public void keyTyped(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case 's':
                        scatter.incrementTime()
                        message = "Step: ${scatter.currentTime}";
                        chart.render();
                        break;
                    case 'd':
                        scatter.decrementTime()
                        message = "Step: ${scatter.currentTime}";
                        chart.render();
                        break;
                    case 'i':
                        scatter.incrementTimeIncrement()
                        break;
                    case 'o':
                        scatter.decrementTimeIncrement()
                        break;
                    case 't':
                        scatter.toggleDrawTriangles()
                        break;
                    case 'k':
                        scatter.toggleDrawMoments()
                        break;
                    case 'p':
                        scatter.printDetails()
                        break;
                    default: break;
                }
            }
        })

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

    def InteractiveFlatScatter createScatter() {
//        scatter = InteractiveFlatScatter.createWithPhoton()
        scatter = InteractiveFlatScatter.createWithElectron()
        scatter
    }
}
