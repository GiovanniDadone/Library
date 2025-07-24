package com.giodad.todolist.domain;

import static com.giodad.todolist.domain.AutoreTestSamples.*;
import static com.giodad.todolist.domain.LibroTestSamples.*;
import static com.giodad.todolist.domain.RecensioneTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.giodad.todolist.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class LibroTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Libro.class);
        Libro libro1 = getLibroSample1();
        Libro libro2 = new Libro();
        assertThat(libro1).isNotEqualTo(libro2);

        libro2.setId(libro1.getId());
        assertThat(libro1).isEqualTo(libro2);

        libro2 = getLibroSample2();
        assertThat(libro1).isNotEqualTo(libro2);
    }

    @Test
    void autoreTest() {
        Libro libro = getLibroRandomSampleGenerator();
        Autore autoreBack = getAutoreRandomSampleGenerator();

        libro.setAutore(autoreBack);
        assertThat(libro.getAutore()).isEqualTo(autoreBack);

        libro.autore(null);
        assertThat(libro.getAutore()).isNull();
    }

    @Test
    void recensioniTest() {
        Libro libro = getLibroRandomSampleGenerator();
        Recensione recensioneBack = getRecensioneRandomSampleGenerator();

        libro.addRecensioni(recensioneBack);
        assertThat(libro.getRecensionis()).containsOnly(recensioneBack);
        assertThat(recensioneBack.getLibro()).isEqualTo(libro);

        libro.removeRecensioni(recensioneBack);
        assertThat(libro.getRecensionis()).doesNotContain(recensioneBack);
        assertThat(recensioneBack.getLibro()).isNull();

        libro.recensionis(new HashSet<>(Set.of(recensioneBack)));
        assertThat(libro.getRecensionis()).containsOnly(recensioneBack);
        assertThat(recensioneBack.getLibro()).isEqualTo(libro);

        libro.setRecensionis(new HashSet<>());
        assertThat(libro.getRecensionis()).doesNotContain(recensioneBack);
        assertThat(recensioneBack.getLibro()).isNull();
    }
}
