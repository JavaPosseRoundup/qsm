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
import org.freddy33.math.dbl.Triangle3d
import org.freddy33.math.dbl.Triangle4d

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 5/27/12
 * Time: 7:39 PM
 * To change this template use File | Settings | File Templates.
 */
class InteractiveFlatScatter extends Scatter implements ISingleColorable {
    final EventBlockFlat block
    BigInteger timeIncrement = 1G
    BigInteger currentTime = 0G
    boolean drawMoments = false
    boolean drawTriangles = true
    boolean drawActivatingLines = true
    Color blockLines = Color.BLACK;
    Color activating = Color.MAGENTA;
    Color[] eventColor = [ Color.RED, Color.BLUE, Color.GREEN, Color.CYAN ];

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
        float maxDist = (float) TrigoInt.ONE * 1.3f
        bbox.add(maxDist, maxDist, maxDist)
        bbox.add(-maxDist, -maxDist, -maxDist)
    }

    boolean getDrawMoments() {
        return drawMoments
    }

    void setDrawMoments(boolean drawMoments) {
        this.drawMoments = drawMoments
    }

    boolean getDrawTriangles() {
        return drawTriangles
    }

    void setDrawTriangles(boolean drawTriangles) {
        this.drawTriangles = drawTriangles
    }

    boolean getDrawActivatingLines() {
        return drawActivatingLines
    }

    void setDrawActivatingLines(boolean drawActivatingLines) {
        this.drawActivatingLines = drawActivatingLines
    }

    public void incrementTimeIncrement() {
        timeIncrement++
    }

    public void decrementTimeIncrement() {
        timeIncrement--
        if (timeIncrement < 1G)
            timeIncrement = 1G
    }

    public void incrementTime() {
        currentTime += timeIncrement
    }

    public void decrementTime() {
        currentTime -= timeIncrement
        if (currentTime < 0G) currentTime = 0G
    }

    private Coord3d toCoord3d(Point4d p) {
        return new Coord3d(p.getX(), p.getY(), p.getZ());
    }

    public void draw(GL2 gl, GLU glu, Camera cam) {
        if (transform != null)
            transform.execute(gl);

        gl.glPointSize(4f);
        gl.glLineWidth(1f);

        // Print original events
        List<Point4d> originalEvents = block.getEventPoints()
        gl.glBegin(GL2.GL_POINTS);
        for (int i = 0; i < originalEvents.size(); i++) {
            setColor(gl, eventColor[i])
            setVertex3d(gl, originalEvents[i])
        }
        gl.glEnd();

        // Draw moment vector if asked
        if (drawMoments) {
            List<Line4d>[] allMoments = block.getEventMoments((double) timeIncrement)
            for (int i = 0; i < allMoments.length; i++) {
                List<Line4d> moment = allMoments[i];
                for (Line4d line : moment) {
                    gl.glBegin(GL2.GL_LINES);
                    setColor(gl, eventColor[i])
                    setVertex3d(gl, line.a);
                    setVertex3d(gl, line.b);
                    gl.glEnd();
                }
            }
        }

        if (drawTriangles) {
            List<Triangle4d> triangles = block.getEventTriangles()
            for (int i = 0; i < triangles.size(); i++) {
                Point4d[] pts = triangles[i].p
                for (int j = 0; j < pts.length; j++) {
                    gl.glBegin(GL2.GL_LINES)
                    setColor(gl, blockLines)
                    setVertex3d(gl, pts[j]);
                    def k = j + 1
                    if (k == pts.length) k=0
                    setVertex3d(gl, pts[k])
                    gl.glEnd()
                }
            }
        }

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

    def toggleDrawTriangles() {
        drawTriangles = !drawTriangles
    }

    def toggleDrawMoments() {
        drawMoments = !drawMoments
    }
}

