package com.test;

import java.awt.*;
import java.util.Arrays;

public class Camera {
    public double aspectRatio = 1.0;
    public int imageWidth = 256;
    public int samplesPerPixel = 10;
    public int maxDepth = 10;

    private int imageHeight;
    vec3 center;
    vec3 firstPixelLocation;
    vec3 pixelDelta_x;
    vec3 pixelDelta_y;

    private void init() {
        imageHeight = (int) (imageWidth / aspectRatio);
        if (imageHeight < 1) { imageHeight = 1; }

        center = new vec3(0, 0, 0);

        double focalLength = 1.0;
        double viewportHeight = 2.0;
        double viewportWidth = viewportHeight * (double) (imageWidth / imageHeight);

        vec3 viewportDelta_x = new vec3(viewportWidth, 0, 0);
        vec3 viewportDelta_y = new vec3(0, -viewportHeight, 0);

        pixelDelta_x = viewportDelta_x.div(imageWidth);
        pixelDelta_y = viewportDelta_y.div(imageHeight);

        vec3 viewportTopLeft = center
                .sub(new vec3(0, 0, focalLength))
                .sub(viewportDelta_x.div(2))
                .sub(viewportDelta_y.div(2));

        firstPixelLocation = viewportTopLeft.plus(
                pixelDelta_x.plus(pixelDelta_y).mul(0.5)
        );
    }

    public void render(Graphics g, RayTracer.hittable world) {
        init();

        //System.out.println("Pixel origin: " + Arrays.toString(firstPixelPosition.e));
        //System.out.println(Arrays.toString(cameraCenter.e));

        for (int y = 0; y < imageHeight; y++){
            System.out.println("Scanlines remaining: " + (imageHeight - y));
            for (int x = 0; x < imageWidth; x++){
                color pixelColor = new color(0, 0, 0);

                for (int s = 0; s < samplesPerPixel; s++) {
                    ray r = makeRay(x, y);
                    pixelColor.plusEq(rayColor(r, maxDepth, world));

                    //System.out.println("x:" + x+ " y:" + y + " sample:" + s);
                }
                Color c = getColor(pixelColor, samplesPerPixel);

                //System.out.println(c.toString());
                //pixel_center = firstPixel + (xOffset * x) + (yOffset * y)

                g.setColor(c);
                //g.setColor(c);
                g.drawLine(x, y, x, y);
            }
        }
    }

    ray makeRay(int x, int y) {
        vec3 pixelCenter = firstPixelLocation
                .plus(pixelDelta_x.mul(x))
                .plus(pixelDelta_y.mul(y));
        vec3 pixelSample = pixelCenter.plus(pixelSampleSquare());

        //System.out.println("sample: " + Arrays.toString(pixelSample.e));

        vec3 rayOrigin = center;
        vec3 rayDirection = pixelSample.sub(rayOrigin);

        //System.out.println("dir: " + Arrays.toString(rayDirection.e));

        return new ray(rayDirection, rayOrigin);
    }

    vec3 pixelSampleSquare() {
        double px = -0.5 + Math.random();
        double py = -0.5 + Math.random();

        return (pixelDelta_x.mul(px)).plus(pixelDelta_y.mul(py));
    }

    Color getColor(color pixelColor, int samplesPerPixel) {

        double scale = 1.0 / samplesPerPixel;

        pixelColor.e[0] *= scale;
        pixelColor.e[1] *= scale;
        pixelColor.e[2] *= scale;

        return pixelColor.toColor();
    }

    color rayColor(ray r, int depth, RayTracer.hittable world) {

        //if we've exceeded the max depth: return black
        if (depth <= 0) return new color(0,0,0);

        RayTracer.hitRecord record = new RayTracer.hitRecord();

        if (world.hit(r, new RayTracer.interval(0.001, Double.POSITIVE_INFINITY), record)) {
            ray scattered = new ray();
            color attenuation = new color(0, 0, 0);

            //System.out.println(record.mat);

            if (record.mat.scatter(r, record, attenuation, scattered))
                return attenuation.mul(rayColor(scattered, depth -1, world));
            return new color(0,0,0);

            //diffuse model
            //vec3 direction = record.normal.plus(vec3.randomUnitVector());
            //return rayColor(new ray(direction, record.point), depth - 1, world).mul(0.5);

            //display normals
            //return new color(record.normal.plus(new vec3(1, 1, 1)).mul(0.5));
        }

        vec3 unitDirection = r.direction.unit();

        //System.out.println(Arrays.toString(unitDirection.e));
        float blendValue = 0.5f * (float) (unitDirection.y() + 1.0);

        //System.out.println(unitDirection.y());

        color White = new color(1, 1, 1);
        color SkyBlue = new color(0.5, 0.7, 1.0);

        color c = White.mul(1.0 - blendValue).plus(
                SkyBlue.mul(blendValue)
        );

        //System.out.println(blendValue);

        return c;
    }


}