package com.test;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class RayTracer extends Frame{

    //ArrayList<Integer> upcomingPieces = new ArrayList<>();

    double infinity = Double.POSITIVE_INFINITY;
    double pi = 3.1415926535897932385;

    double degree2rads(double degrees) {
        return degrees * pi / 180;
    }



    double aspectRatio = 1;
    int windowWidth = (int) (256 * 2.5);

    public RayTracer() {
        setSize(windowWidth, (int) (windowWidth / aspectRatio));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setBackground(Color.BLACK);
        setVisible(true);
    }


    public void paint(Graphics g) {

        //world (all objects)

        hittableList world = new hittableList();

        matt.lambertian material_ground = new matt.lambertian(new color(0.7, 0.8, 0.0));
        matt.lambertian material_center = new matt.lambertian(new color(0.7, 0.3, 0.3));
        matt.metal material_left = new matt.metal(new color(0.8, 0.8, 0.8));
        matt.metal material_right = new matt.metal(new color(0.8, 0.6, 0.2));
        matt.metal material_back = new matt.metal(new color(0.9, 0.2, 0.2));

        world.add( new Sphere(new vec3(0, -100.5, -1), 100, material_ground) );
        world.add( new Sphere(new vec3(0, 0.5, -1.5), 0.5, material_back) );
        world.add( new Sphere(new vec3(0, -0.20, -1), 0.3, material_center));
        world.add( new Sphere(new vec3(-1.0, 0.0, -1.0), 0.5, material_left));
        world.add( new Sphere(new vec3(1.0, 0.0, -1.0), 0.5, material_right));

        Camera c = new Camera();

        c.imageWidth = windowWidth;
        c.samplesPerPixel = 100;

        c.render(g, world);
    }

    public static void main(String[] args) {
        new RayTracer();
        //for (int i = 0; i < 10; i++) System.out.println(randomDouble(-1, 1));
    }


/*    public double hitSphere(vec3 center, double radius, ray r) {
        vec3 oc = r.origin.sub(center);
        double a = r.direction.dot(r.direction);
        double half_b = oc.dot(r.direction);
        double c = oc.dot(oc) - radius * radius;
        double discriminant = half_b * half_b - a * c;

        if (discriminant < 0) {
            //ray didn't hit the sphere
            return -1.0;
        } else {
            //solve the quadratic equation for t
            return (-half_b - Math.sqrt(discriminant)) / a;
        }
    }*/

    public class Sphere extends hittable {
        final vec3 center;
        final double radius;
        final matt.Material mat;
        Sphere(vec3 center, double radius, matt.Material material) {
            this.center = center;
            this.radius = radius;
            this.mat = material;
        }

        @Override
        public boolean hit(ray r, interval ray_t, hitRecord record) {
            //We're solving a quadratic where the roots are the value of t we want to find
            vec3 oc = r.origin.sub(center);
            double a = r.direction.dot(r.direction);
            double half_b = oc.dot(r.direction);
            double c = oc.dot(oc) - radius * radius;

            //If the ray misses, skip this sphere
            double discriminant = half_b * half_b - a * c;
            if (discriminant < 0) return false;
            double sqrtD = Math.sqrt(discriminant);


            //This finds the nearest root that lies within our tMin and tMax
            double root = (-half_b - sqrtD) /a;
            //if the nearest t is out of bounds
            if (!ray_t.surrounds(root)) {
                root = (-half_b + sqrtD) / a;
                //if the higher t is out of bounds
                if (!ray_t.surrounds(root)) {
                    //if all values of t are out of range, skip this sphere
                    return false;
                }
            }

            record.t = root; // the value of t that is a hit
            record.point = r.at(record.t); // the point in space at which we hit the sphere
            vec3 normalOut = record.point.sub(center).div(radius); // a normalized vector pointing out of the sphere from our hit point
            record.setFaceNormal(r, normalOut);
            //System.out.println(this.mat);
            record.mat = this.mat;
            //System.out.println(record.mat);

            return true;
        }
    }

    public static class hitRecord {
        public vec3 point;
        public vec3 normal;
        public matt.Material mat;
        double t;
        boolean frontFace;

        void setFaceNormal (ray r, vec3 outNormal){
            //whether this interaction is with the front face.
            // if the ray and out direction are pointing in the same direction: then we're looking at the inside
            // if they're against each other (dot product is negative): we're looking at the outside
            frontFace = r.direction.dot(outNormal) < 0;
            normal = outNormal;
            //if we're looking at the backface, we should flip the normal
            if (!frontFace) { normal.neg(); }
        }

        public void assign(hitRecord other) {
            this.frontFace = other.frontFace;
            this.normal = other.normal;
            this.point = other.point;
            this.t = other.t;
            this.mat = other.mat;
        }
    }


    public abstract class hittable {
        public abstract boolean hit(ray r, interval ray_t, hitRecord record);
    }

    public class hittableList extends hittable {
        final private ArrayList<hittable> hittable_list = new ArrayList<>();

        hittableList(){}

        void clear() {
            hittable_list.clear();
        }

        void add(hittable object) {
                hittable_list.add(object);
        }

        @Override
        public boolean hit(ray r, interval ray_t, hitRecord record) {

            hitRecord tempRecord = new hitRecord();
            boolean hitAnything = false;
            double closestValue = ray_t.max;

            for (hittable object: hittable_list) {
                if (object.hit(r, new interval(ray_t.min, closestValue), tempRecord)) {
                    //System.out.println(record);
                    hitAnything = true;
                    closestValue = tempRecord.t;
                    record.assign(tempRecord);
                    //System.out.println(record);
                    //System.out.println(Arrays.toString(tempRecord.normal.e));
                    //System.out.println(Arrays.toString(record.normal.e));
                }
            }
            return hitAnything;
        }

    }

    interval newEmptyInterval(){ return new interval(); }
    interval newUniverseInterval(){ return new interval(-infinity, infinity); }

    public static class interval{
        double min;
        double max;

        interval(){
            min = Double.POSITIVE_INFINITY;
            max = Double.NEGATIVE_INFINITY;
        }
        interval(double min, double max) {
            this.min = min;
            this.max = max;
        }

        double clamp(double x) {
            if (x < min) { return min; }
            if (x > max) {return max; }
            return x;
        }

        boolean contains(double x){
            return (min <= x) && (x <= max);
        }

        boolean surrounds(double x){
            return (min < x) && (x < max);
        }


    }

    public static double randomDouble(double min, double max){
        return min + ((max - min) * Math.random());
    }
}


