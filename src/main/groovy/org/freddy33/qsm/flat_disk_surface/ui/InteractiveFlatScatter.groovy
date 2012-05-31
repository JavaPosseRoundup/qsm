package org.freddy33.qsm.flat_disk_surface.ui

import org.jzy3d.plot3d.primitives.Scatter
import org.jzy3d.colors.ISingleColorable

import org.jzy3d.colors.Color

import org.jzy3d.maths.Coord3d
import javax.media.opengl.GL2
import javax.media.opengl.glu.GLU
import org.jzy3d.plot3d.rendering.view.Camera

import org.freddy33.qsm.flat_disk_surface.calc.EventBlockFlat
import org.freddy33.math.dbl.Point4d
import org.freddy33.math.dbl.Point3d
import org.freddy33.math.dbl.SphericalUnitVector2d
import org.freddy33.math.dbl.Vector3d

import org.freddy33.math.dbl.Line4d
import org.freddy33.math.bigInt.TrigoInt

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 5/27/12
 * Time: 7:39 PM
 * To change this template use File | Settings | File Templates.
 */
class InteractiveFlatScatter extends Scatter implements ISingleColorable {
    final EventBlockFlat block
    BigInteger currentTime = 0G
    Color activating = Color.BLACK;
    Color[] eventColor = [ Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW ];

    static InteractiveFlatScatter createWithPhoton() {
        new InteractiveFlatScatter(EventBlockFlat.createPhoton(
                new Point3d(0d,0d,0d),
                0G,
                new SphericalUnitVector2d(new Vector3d(1d,0d,0d)),
                0d,
                1G
        ))
    }

    static InteractiveFlatScatter createWithElectron() {
        new InteractiveFlatScatter(EventBlockFlat.createElectron(
                new Point3d(0d,0d,0d),
                0G,
                new SphericalUnitVector2d(new Vector3d(1d,0d,0d)),
                0d,
                1G
        ))
    }

    public InteractiveFlatScatter(EventBlockFlat block) {
        this.block = block
        setWidth(3f)
        float maxDist = (float) TrigoInt.ONE * 2f
        bbox.add((float)2f*maxDist, maxDist, maxDist)
        bbox.add(0f, -maxDist, maxDist)
        bbox.add(0f, -maxDist, -maxDist)
        bbox.add(0f, maxDist, -maxDist)
    }

    public void incrementTime() {
        currentTime += 10G
    }

    public void decrementTime() {
        currentTime -= 10G
        if (currentTime < 0G) currentTime = 0G
    }

    private Coord3d toCoord3d(Point4d p) {
        return new Coord3d(p.getX(), p.getY(), p.getZ());
    }

    public void draw(GL2 gl, GLU glu, Camera cam) {
        if (transform != null)
            transform.execute(gl);

        gl.glPointSize(3f);
        gl.glLineWidth(1f);

        // Print original events
        List<Point4d> originalEvents = block.getEventPoints()
        gl.glBegin(GL2.GL_POINTS);
        for (int i = 0; i < originalEvents.size(); i++) {
            setColor(gl, eventColor[i])
            setVertex3d(gl, originalEvents[i])
        }
        gl.glEnd();

        // Print waiting events lines
        List<Line4d>[] waitingEvents = block.getWaitingEvents(currentTime)
        for (int i = 0; i < waitingEvents.length; i++) {
            List<Line4d> waitingLines = waitingEvents[i];
            for (Line4d line : waitingLines) {
                gl.glBegin(GL2.GL_LINES);
                setColor(gl, eventColor[i])
                setVertex3d(gl, line.a);
                setVertex3d(gl, line.b);
                gl.glEnd();
            }
        }
    }

    private static void setVertex3d(final GL2 gl, final Point4d point) {
        gl.glVertex3f((float)point.x, (float)point.y, (float)point.z);
    }

    private static void setColor(final GL2 gl, final Color c) {
        gl.glColor4f(c.r, c.g, c.b, c.a);
    }

    def printDetails() {
        println "block = $block"
    }
}

