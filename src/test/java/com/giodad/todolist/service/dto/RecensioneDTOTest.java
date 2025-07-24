package com.giodad.todolist.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.giodad.todolist.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecensioneDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecensioneDTO.class);
        RecensioneDTO recensioneDTO1 = new RecensioneDTO();
        recensioneDTO1.setId(1L);
        RecensioneDTO recensioneDTO2 = new RecensioneDTO();
        assertThat(recensioneDTO1).isNotEqualTo(recensioneDTO2);
        recensioneDTO2.setId(recensioneDTO1.getId());
        assertThat(recensioneDTO1).isEqualTo(recensioneDTO2);
        recensioneDTO2.setId(2L);
        assertThat(recensioneDTO1).isNotEqualTo(recensioneDTO2);
        recensioneDTO1.setId(null);
        assertThat(recensioneDTO1).isNotEqualTo(recensioneDTO2);
    }
}
