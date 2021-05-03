package ar.com.danaide.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ar.com.danaide.web.rest.TestUtil;

public class SpecialDateTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpecialDate.class);
        SpecialDate specialDate1 = new SpecialDate();
        specialDate1.setId(1L);
        SpecialDate specialDate2 = new SpecialDate();
        specialDate2.setId(specialDate1.getId());
        assertThat(specialDate1).isEqualTo(specialDate2);
        specialDate2.setId(2L);
        assertThat(specialDate1).isNotEqualTo(specialDate2);
        specialDate1.setId(null);
        assertThat(specialDate1).isNotEqualTo(specialDate2);
    }
}
