package com.test;

public class matt {

    public abstract static class Material {
        abstract boolean scatter(ray r_in, RayTracer.hitRecord record, color attenuation, ray scattered);
    }

    static class lambertian extends Material {
        private final color albedo;

        lambertian(color a){
            this.albedo = a;
        }

        boolean scatter(ray r_in, RayTracer.hitRecord record, color attenuation, ray scattered){
            vec3 scatterDirection = record.normal.plus( vec3.randomUnitVector() );

            if (scatterDirection.nearZero()) scatterDirection = record.normal;

            scattered.origin = record.point;
            scattered.direction = scatterDirection;
            attenuation.e = albedo.e;
            return true;
        }
    }

    static class metal extends Material {
        private final color albedo;
        metal(color a) {
            this.albedo = a;
        }

        boolean scatter(ray r_in, RayTracer.hitRecord record, color attenuation, ray scattered){
            vec3 reflected = vec3.reflect(r_in.direction.unit(), record.normal);

            scattered.origin = record.point;
            scattered.direction = reflected;
            attenuation.e = albedo.e;
            return true;
        }

    }

}
