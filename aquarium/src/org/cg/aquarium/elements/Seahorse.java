package org.cg.aquarium.elements;

import libs.modelparser.Material;
import libs.modelparser.Vertex;
import libs.modelparser.WavefrontObject;
import com.sun.opengl.util.GLUT;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import org.cg.aquarium.infrastructure.base.Mobile;
import org.cg.aquarium.infrastructure.base.ObjectModel;
import org.cg.aquarium.infrastructure.helpers.MathHelper;
import org.cg.aquarium.infrastructure.representations.Vector;

/**
 *
 * @author ldavid
 */
public class Seahorse extends Mobile {

    public static float DISTANCE_FROM_CENTER = 100000;
    public static float RANDOMNESS = .2f;

    private ObjectModel modelObject;

    public Seahorse() {
        super();
    }

    public Seahorse(Vector direction, float speed) {
        super(direction, speed);
    }

    public Seahorse(Vector direction, float speed, Vector position) {
        super(direction, speed, position);
    }

    protected void setMaterial(Material mat, GL gl) {
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, new float[]{mat.getKa().getX(), mat.getKa().getY(), mat.getKa().getZ(), 1}, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, new float[]{mat.getKd().getX(), mat.getKd().getY(), mat.getKd().getZ(), 1}, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, new float[]{mat.getKs().getX(), mat.getKs().getY(), mat.getKs().getZ(), 1}, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, new float[]{mat.getShininess(), 0, 0, 0}, 0);
    }

    @Override
    public void initialize() {
        size = new Vector(2, 2, 2);

        Material material = new Material("base");
        material.setKa(new Vertex(.6f, .6f, 1));
        material.setKd(new Vertex(.6f, .6f, 1));
        material.setKs(new Vertex(.2f, .2f, .2f));
        material.setShininess(30);

        modelObject = new ObjectModel(new WavefrontObject("seahorse.obj", 2, 2, 2),
                material);
    }

    @Override
    public void update() {
        float distanceFromOrigin = position.norm();
        setDirection(
                direction.scale(
                        (DISTANCE_FROM_CENTER - distanceFromOrigin) / DISTANCE_FROM_CENTER).add(
                        position.scale(RANDOMNESS - 1)
                        .add(Vector.random().normalize().scale(RANDOMNESS))
                        .scale(distanceFromOrigin / DISTANCE_FROM_CENTER)));

        move();
    }

    @Override
    public void display(GL gl, GLU glu, GLUT glut) {
        gl.glPushMatrix();

        gl.glTranslatef(position.getX(), position.getY(), position.getZ());

        modelObject.glDefineMaterial(gl);
        modelObject.glAlignDirection(gl, direction, Vector.LEFT);
        modelObject.glRender(gl);

        debugDisplayDirectionVector(gl, glu, glut);

        gl.glPopMatrix();
    }

}
