package com.test;

import java.awt.Color;

public class color{
    
    double[] e = {0.0, 0.0, 0.0};

    double r() {
        return e[0];
    }

    double g() {
        return e[1];
    }

    double b() {
        return e[2];
    }

    color(double r, double g, double b) {
        e[0] = r; e[1] = g; e[2] = b;
    }

    color(double[] e) {
        assert (e.length == 3);
        this.e = e;
    }

    color(vec3 vector) {
        this.e = vector.e;
    }


    void plusEq(color other) {
        this.e[0] += other.e[0];
        this.e[1] += other.e[1];
        this.e[2] += other.e[2];
    }

    color plus(color other) {
        return new color(
                this.e[0] + other.e[0],
                this.e[1] + other.e[1],
                this.e[2] + other.e[2]
        );
    }

    color sub(color other) {
        return new color(
                this.e[0] - other.e[0],
                this.e[1] - other.e[1],
                this.e[2] - other.e[2]
        );
    }

    void subEq(color other) {
        this.e[0] -= other.e[0];
        this.e[1] -= other.e[1];
        this.e[2] -= other.e[2];
    }

    color mul(double v) {
        return new color(
                e[0] * v,
                e[1] * v,
                e[2] * v
        );
    }

    color mul(color c) {
        return new color(
                this.e[0] * c.e[0],
                this.e[1] * c.e[1],
                this.e[2] * c.e[2]
        );
    }

    void mulEq(double v) {
        e[0] *= v;
        e[1] *= v;
        e[2] *= v;
    }

    color div(double v) {
        return new color(
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

    float linear2gamma(double in) {return (float) Math.sqrt(in); }

    public Color toColor() {

        return new Color(linear2gamma(r()), linear2gamma(g()), linear2gamma(b()));
    }
    public Color toLinearColor() {

        return new Color( (float) r(), (float) g(), (float) b());
    }
}
