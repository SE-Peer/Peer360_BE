package com.example.peer360.user.controller;

import com.kennycason.kumo.image.AngleGenerator;

import java.util.Random;

public class RandomAngleGenerator extends AngleGenerator {
    private final Random random;
    private final int minAngle;
    private final int maxAngle;

    public RandomAngleGenerator(int minAngle, int maxAngle) {
        this.random = new Random();
        this.minAngle = minAngle;
        this.maxAngle = maxAngle;
    }

    @Override
    public double next() {
        return minAngle + (maxAngle - minAngle) * random.nextDouble();
    }
}
