package com.giodad.todolist.domain;

import static com.giodad.todolist.domain.AutoreTestSamples.*;
import static com.giodad.todolist.domain.LibroTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.giodad.todolist.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AutoreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Autore.class);
        Autore autore1 = getAutoreSample1();
        Autore autore2 = new Autore();
        assertThat(autore1).isNotEqualTo(autore2);

        autore2.setId(autore1.getId());
        assertThat(autore1).isEqualTo(autore2);

        autore2 = getAutoreSample2();
        assertThat(autore1).isNotEqualTo(autore2);
    }

    @Test
    void libriTest() {
        Autore autore = getAutoreRandomSampleGenerator();
        Libro libroBack = getLibroRandomSampleGenerator();

        autore.addLibri(libroBack);
        assertThat(autore.getLibris()).containsOnly(libroBack);
        assertThat(libroBack.getAutore()).isEqualTo(autore);

        autore.removeLibri(libroBack);
        assertThat(autore.getLibris()).doesNotContain(libroBack);
        assertThat(libroBack.getAutore()).isNull();

        autore.libris(new HashSet<>(Set.of(libroBack)));
        assertThat(autore.getLibris()).containsOnly(libroBack);
        assertThat(libroBack.getAutore()).isEqualTo(autore);

        autore.setLibris(new HashSet<>());
        assertThat(autore.getLibris()).doesNotContain(libroBack);
        assertThat(libroBack.getAutore()).isNull();
    }
}
