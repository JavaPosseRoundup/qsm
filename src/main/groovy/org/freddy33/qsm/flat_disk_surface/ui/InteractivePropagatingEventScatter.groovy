package org.freddy33.qsm.flat_disk_surface.ui

import org.freddy33.math.bigInt.TrigoInt
import org.freddy33.qsm.flat_disk_surface.calc.EventBlockDouble
import org.jzy3d.colors.Color
import org.jzy3d.colors.ISingleColorable
import org.jzy3d.maths.Coord3d
import org.jzy3d.plot3d.primitives.Scatter
import org.jzy3d.plot3d.rendering.view.Camera

import javax.media.opengl.GL
import javax.media.opengl.GL2
import javax.media.opengl.glu.GLU

import org.freddy33.math.dbl.*
import org.freddy33.qsm.flat_disk_surface.calc.PropagatingEvents

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 5/27/12
 * Time: 7:39 PM
 * To change this template use File | Settings | File Templates.
 */
class InteractivePropagatingEventScatter extends Scatter implements ISingleColorable {
    final PropagatingEvents propagatingEvents
    BigInteger currentTime = 0G
    float eventSize = 3f
    double eventMomentLength = 5d
    boolean drawOriginalEvent = true
    boolean drawOldEvents = false
    boolean drawCurrentEvents = true
    boolean drawMoments = true
    Color originalEvent = Color.GREEN;
    Color currentEvent = Color.RED;
    Color oldEvent = Color.CYAN;

    static InteractivePropagatingEventScatter create() {
        new InteractivePropagatingEventScatter()
    }

    public InteractivePropagatingEventScatter(PropagatingEvents ps) {
        this.propagatingEvents = ps
        setWidth(eventSize)
        float maxDist = (float) TrigoInt.ONE * 0.5f
        bbox.add(maxDist, maxDist, maxDist)
        bbox.add(-maxDist, -maxDist, -maxDist)
    }

    public void incrementTime() {
        propagatingEvents.increment()
        currentTime ++
    }

    public void decrementTime() {
        throw new IllegalArgumentException("not supported yet!")
    }

    private Coord3d toCoord3d(Point4d p) {
        return new Coord3d(p.getX(), p.getY(), p.getZ());
    }

    @Override
    void draw(GL glp, GLU glu, Camera cam) {
        GL2 gl = (GL2)glp;
        if (transform != null)
            transform.execute(gl);

        gl.glPointSize(eventSize);
        gl.glLineWidth(1f);

        // Print original event
        if (drawOriginalEvent) {
            gl.glBegin(GL2.GL_POINTS);
            setColor(gl, originalEvent)
            setVertex3d(gl, propagatingEvents.first.origin)
            gl.glEnd();
            // Draw moment vector if asked
            if (drawMoments) {
                List<Line4d> originalMoment = propagatingEvents.getOriginalMoment(this.eventMomentLength)
                for (Line4d line : originalMoment) {
                    gl.glBegin(GL2.GL_LINES);
                    setColor(gl, this.originalEvent)
                    setVertex3d(gl, line.a);
                    setVertex3d(gl, line.b);
                    gl.glEnd();
                }
            }
        }

        if (drawCurrentEvents) {
            propagatingEvents.events.each {
                gl.glBegin(GL2.GL_POINTS);
                setColor(gl, this.currentEvent)
                setVertex3d(gl, it.origin)
                gl.glEnd();
            }
            // Draw moment vector if asked
            if (drawMoments) {
                List<Line4d> allMoments = propagatingEvents.getCurrentMoments(this.eventMomentLength)
                for (Line4d line : allMoments) {
                    gl.glBegin(GL2.GL_LINES);
                    setColor(gl, this.currentEvent)
                    setVertex3d(gl, line.a);
                    setVertex3d(gl, line.b);
                    gl.glEnd();
                }
            }
        }
    }

    private static void setVertex3d(final GL2 gl, final Point4d point) {
        gl.glVertex3f((float)point.x, (float)point.y, (float)point.z);
    }

    private static void setColor(final GL gl, final Color c) {
        gl.glColor4f(c.r, c.g, c.b, c.a);
    }

    def toggleDrawMoments() {
        drawMoments = !drawMoments
    }
}

