package org.freddy33.qsm.sphere_surface_int.ui;

import org.freddy33.math.bigInt.Point4i;
import org.freddy33.qsm.sphere_surface_int.calc.EventBlockInt;
import org.freddy33.qsm.sphere_surface_int.calc.EventInt;
import org.freddy33.qsm.sphere_surface_int.calc.EventTriangleInt;
import org.freddy33.qsm.sphere_surface_int.calc.SpaceTimeInt;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ISingleColorable;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.rendering.view.Camera;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InteractiveEventScatter extends Scatter implements ISingleColorable {
    final SpaceTimeInt st;
    Color blockLine = Color.RED;
    Color activationLine = Color.BLUE;
    Color newBlockLine = Color.CYAN;
    Color activeNow = Color.GREEN;
    Color stillActive = Color.YELLOW;
    Color activating = Color.BLACK;

    public InteractiveEventScatter(SpaceTimeInt spaceTime) {
        super();
        this.st = spaceTime;
        setWidth(3f);
        List<Point4i> fixedPoints = spaceTime.getFixedPoints();
        for (Point4i fixedPoint : fixedPoints) {
            bbox.add(toCoord3d(fixedPoint));
        }
    }

    private Coord3d toCoord3d(Point4i fixedPoint) {
        return new Coord3d(fixedPoint.getX().doubleValue(),
                fixedPoint.getY().doubleValue(),
                fixedPoint.getZ().doubleValue());
    }

    public void draw(GL2 gl, GLU glu, Camera cam) {
        if (transform != null)
            transform.execute(gl);

        gl.glPointSize(3f);
        gl.glLineWidth(1f);

        gl.glBegin(GL2.GL_POINTS);

        // Print all active events in active now t and in still active
        List<EventInt> activeEvents = st.getActiveEvents();
        Map<EventBlockInt, List<EventInt>> pyramids = new HashMap<EventBlockInt, List<EventInt>>();
        for (EventInt activeEvent : activeEvents) {
            Point4i point = activeEvent.point;
            if (Math.abs(point.getT().subtract(st.getCurrentTime()).doubleValue()) < 3d ) {
                EventBlockInt block = activeEvent.createdByBlock;
                if (block != null) {
                    List<EventInt> events = pyramids.get(block);
                    if (events == null) {
                        events = new ArrayList<EventInt>();
                        pyramids.put(block, events);
                    }
                    events.add(activeEvent);
                }
                setColor(gl, activeNow);
            } else {
                setColor(gl, stillActive);
            }
            setVertex3f(gl, point);
        }

        // Print all dead events that currently activated a new event
        setColor(gl, activating);
        for (EventBlockInt eb : pyramids.keySet()) {
            for (int i = 0; i < eb.e.length; i++) {
                setVertex3f(gl, eb.e[i].point);
            }
        }
        gl.glEnd();

        // Printing lines
        for (List<EventInt> events : pyramids.values()) {
            int i = 0;
            for (EventInt event : events) {
                EventTriangleInt createdByTriangle = event.createdByTriangle;

                // All lines pointing from creating triangle to new event
                setColor(gl, activationLine);
                gl.glBegin(GL2.GL_LINES);
                setVertex3f(gl, event.point);
                setVertex3f(gl, createdByTriangle.e1().point);
                gl.glEnd();
                gl.glBegin(GL2.GL_LINES);
                setVertex3f(gl, event.point);
                setVertex3f(gl, createdByTriangle.e2().point);
                gl.glEnd();
                gl.glBegin(GL2.GL_LINES);
                setVertex3f(gl, event.point);
                setVertex3f(gl, createdByTriangle.e3().point);
                gl.glEnd();

                // The old triangle (ending up doing the all block lines)
                setColor(gl, this.blockLine);
                gl.glBegin(GL2.GL_LINES);
                setVertex3f(gl, createdByTriangle.e1().point);
                setVertex3f(gl, createdByTriangle.e2().point);
                gl.glEnd();
                gl.glBegin(GL2.GL_LINES);
                setVertex3f(gl, createdByTriangle.e2().point);
                setVertex3f(gl, createdByTriangle.e3().point);
                gl.glEnd();
                gl.glBegin(GL2.GL_LINES);
                setVertex3f(gl, createdByTriangle.e3().point);
                setVertex3f(gl, createdByTriangle.e1().point);
                gl.glEnd();

                // The new block one at a time
                setColor(gl, this.newBlockLine);
                if (i < events.size() - 1) {
                    for (int j = i + 1; j < events.size(); j++) {
                        gl.glBegin(GL2.GL_LINES);
                        setVertex3f(gl, event.point);
                        setVertex3f(gl, events.get(j).point);
                        gl.glEnd();
                    }
                }
                i++;
            }
        }
    }

    private static void setVertex3f(final GL2 gl, final Point4i point) {
        gl.glVertex3f(point.getX().floatValue(), point.getY().floatValue(), point.getZ().floatValue());
    }

    private static void setColor(final GL2 gl, final Color c) {
        gl.glColor4f(c.r, c.g, c.b, c.a);
    }
}


