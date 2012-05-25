package org.freddy33.qsm.sphere_surface_int.ui

import org.freddy33.qsm.sphere_surface_int.calc.SpaceTimeInt
import org.jzy3d.chart.Chart
import org.jzy3d.chart.controllers.mouse.ChartMouseController
import org.jzy3d.chart.controllers.thread.ChartThreadController
import org.jzy3d.plot3d.rendering.view.Renderer2d
import org.jzy3d.ui.Plugs

import java.awt.Graphics
import java.awt.Rectangle
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

def frame = new InteractiveSpaceFrame()
Plugs.frame(frame.createChart(), new Rectangle(0, 200, 400, 400), "Space Playground")

class InteractiveSpaceFrame {
    Chart chart
    InteractiveEventScatter scatter
    SpaceTimeInt st = new SpaceTimeInt(10G)
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
                        st.calc(true)
                        message = "Step: ${st.currentTime} Events: ${st.activeEvents.size()}";
                        chart.render();
                        break;
                    case 'p':
                        st.printDetails()
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

    def InteractiveEventScatter createScatter() {
        st.initElectron(10G)
        st.initPhoton(3G)
        scatter = new InteractiveEventScatter(st)
        scatter
    }
}
