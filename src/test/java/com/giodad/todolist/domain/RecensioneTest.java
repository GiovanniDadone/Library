package com.giodad.todolist.domain;

import static com.giodad.todolist.domain.LibroTestSamples.*;
import static com.giodad.todolist.domain.RecensioneTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.giodad.todolist.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecensioneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Recensione.class);
        Recensione recensione1 = getRecensioneSample1();
        Recensione recensione2 = new Recensione();
        assertThat(recensione1).isNotEqualTo(recensione2);

        recensione2.setId(recensione1.getId());
        assertThat(recensione1).isEqualTo(recensione2);

        recensione2 = getRecensioneSample2();
        assertThat(recensione1).isNotEqualTo(recensione2);
    }

    @Test
    void libroTest() {
        Recensione recensione = getRecensioneRandomSampleGenerator();
        Libro libroBack = getLibroRandomSampleGenerator();

        recensione.setLibro(libroBack);
        assertThat(recensione.getLibro()).isEqualTo(libroBack);

        recensione.libro(null);
        assertThat(recensione.getLibro()).isNull();
    }
}
