package com.giodad.todolist.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RecensioneTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Recensione getRecensioneSample1() {
        return new Recensione().id(1L).descrizione("descrizione1");
    }

    public static Recensione getRecensioneSample2() {
        return new Recensione().id(2L).descrizione("descrizione2");
    }

    public static Recensione getRecensioneRandomSampleGenerator() {
        return new Recensione().id(longCount.incrementAndGet()).descrizione(UUID.randomUUID().toString());
    }
}
