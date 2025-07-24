package com.giodad.todolist.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LibroTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Libro getLibroSample1() {
        return new Libro().id(1L).titolo("titolo1");
    }

    public static Libro getLibroSample2() {
        return new Libro().id(2L).titolo("titolo2");
    }

    public static Libro getLibroRandomSampleGenerator() {
        return new Libro().id(longCount.incrementAndGet()).titolo(UUID.randomUUID().toString());
    }
}
