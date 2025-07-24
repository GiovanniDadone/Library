package com.giodad.todolist.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.giodad.todolist.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AutoreDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AutoreDTO.class);
        AutoreDTO autoreDTO1 = new AutoreDTO();
        autoreDTO1.setId(1L);
        AutoreDTO autoreDTO2 = new AutoreDTO();
        assertThat(autoreDTO1).isNotEqualTo(autoreDTO2);
        autoreDTO2.setId(autoreDTO1.getId());
        assertThat(autoreDTO1).isEqualTo(autoreDTO2);
        autoreDTO2.setId(2L);
        assertThat(autoreDTO1).isNotEqualTo(autoreDTO2);
        autoreDTO1.setId(null);
        assertThat(autoreDTO1).isNotEqualTo(autoreDTO2);
    }
}
