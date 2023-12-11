package com.test;

/**
 * Used as the base for all vectors (position, color, ect)
 */
public class vec3 {
    double[] e = new double[3];
    double x() {
        return e[0];
    }
    double y() {
        return e[1];
    }
    double z() {
        return e[2];
    }

    public static vec3 random() {
        return new vec3(Math.random(), Math.random(), Math.random());
    }

    public static vec3 random(double min, double max) {
        return new vec3(RayTracer.randomDouble(min, max), RayTracer.randomDouble(min, max), RayTracer.randomDouble(min, max));
    }

    public static vec3 ZERO() {
        return new vec3(0, 0, 0);
    }

    public static vec3 newInUnitSphere() {
        while (true) {
            vec3 p = vec3.random(-1, 1);
            if (p.lengthSquared() < 1) { return p; }
        }
    }

    public static vec3 randomUnitVector() {
        return newInUnitSphere().unit();
    }

    public static vec3 randomOnHemisphere(vec3 normal) {
        vec3 onUnitSphere = randomUnitVector();

        if ((normal.dot(onUnitSphere) <= 0.0)) {
            onUnitSphere.neg();
        }

        return onUnitSphere;
    }

    public static vec3 reflect(vec3 vector, vec3 normal){
        return vector.sub(normal.mul(2 * vector.dot(normal)));
    }


    vec3(double x, double y, double z) {
        e[0] = x;
        e[1] = y;
        e[2] = z;
    }

    vec3(double[] e) {
        assert (e.length == 3);
        this.e = e;
    }

    void plusEq(vec3 other) {
        this.e[0] += other.e[0];
        this.e[1] += other.e[1];
        this.e[2] += other.e[2];
    }

    vec3 plus(vec3 other) {
        return new vec3(
                this.e[0] + other.e[0],
                this.e[1] + other.e[1],
                this.e[2] + other.e[2]
        );
    }

    void neg() {
        this.e[0] = -this.e[0];
        this.e[1] = -this.e[1];
        this.e[2] = -this.e[2];
    }

    vec3 sub(vec3 other) {
        return new vec3(
                this.e[0] - other.e[0],
                this.e[1] - other.e[1],
                this.e[2] - other.e[2]
        );
    }

    void subEq(vec3 other) {
        this.e[0] -= other.e[0];
        this.e[1] -= other.e[1];
        this.e[2] -= other.e[2];
    }

    vec3 mul(double v) {
        return new vec3(
                e[0] * v,
                e[1] * v,
                e[2] * v
        );
    }

    void mulEq(double v) {
        e[0] *= v;
        e[1] *= v;
        e[2] *= v;
    }

    vec3 div(double v) {
        return new vec3(
                e[0] / v,
                e[1] / v,
                e[2] / v
        );
    }

    void divEq(double v) {
        e[0] /= v;
        e[1] /= v;
        e[2] /= v;
    }

    double dot(vec3 other) {
        return this.x() * other.x() + this.y() * other.y() + this.z() * other.z();
    }

    vec3 cross(vec3 other) {
        return new vec3(
                this.y() * other.z() - this.z() * other.y(),
                this.z() * other.x() - this.x() * other.z(),
                this.x() * other.y() - this.y() * other.x()
        );
    }

    vec3 unit() {
        return this.div(this.length());
    }

    void makeUnit() {
        this.divEq(this.length());
    }

    double length() {
        return Math.sqrt(this.lengthSquared());
    }

    double lengthSquared() {
        if (e[0] == 0 && e[1] == 0  && e[2] == 0 ){ return 0; }
        return (e[0] * e[0]) + (e[1] * e[1]) + (e[2] * e[2]);
    }

    boolean nearZero() {
        float s = 1e-8f;
        return(this.e[0] < s) && (this.e[1] < s) && (this.e[2] < s);
    }

}
