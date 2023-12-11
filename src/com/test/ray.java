package com.test;

public class ray {
    vec3 direction;
    vec3 origin;

    ray(vec3 direction, vec3 origin) {
        this.direction = direction;
        this.origin = origin;
    }

    ray() {
        this.direction = vec3.ZERO();
        this.origin = vec3.ZERO();
    }

    vec3 at(double i) {
        return origin.plus(direction.mul(i));
    }

    public vec3 getDirection() {
        return direction;
    }

    public vec3 getOrigin() {
        return origin;
    }
}
