package com.giodad.todolist.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AutoreTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Autore getAutoreSample1() {
        return new Autore().id(1L).nome("nome1");
    }

    public static Autore getAutoreSample2() {
        return new Autore().id(2L).nome("nome2");
    }

    public static Autore getAutoreRandomSampleGenerator() {
        return new Autore().id(longCount.incrementAndGet()).nome(UUID.randomUUID().toString());
    }
}
