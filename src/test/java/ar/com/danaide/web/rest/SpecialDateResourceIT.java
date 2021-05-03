package ar.com.danaide.web.rest;

import ar.com.danaide.TiendaVirtualApp;
import ar.com.danaide.domain.SpecialDate;
import ar.com.danaide.repository.SpecialDateRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SpecialDateResource} REST controller.
 */
@SpringBootTest(classes = TiendaVirtualApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class SpecialDateResourceIT {

    private static final LocalDate DEFAULT_BEGIN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BEGIN_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FINISH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FINISH_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private SpecialDateRepository specialDateRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecialDateMockMvc;

    private SpecialDate specialDate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecialDate createEntity(EntityManager em) {
        SpecialDate specialDate = new SpecialDate()
            .beginDate(DEFAULT_BEGIN_DATE)
            .finishDate(DEFAULT_FINISH_DATE);
        return specialDate;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecialDate createUpdatedEntity(EntityManager em) {
        SpecialDate specialDate = new SpecialDate()
            .beginDate(UPDATED_BEGIN_DATE)
            .finishDate(UPDATED_FINISH_DATE);
        return specialDate;
    }

    @BeforeEach
    public void initTest() {
        specialDate = createEntity(em);
    }

    @Test
    @Transactional
    public void createSpecialDate() throws Exception {
        int databaseSizeBeforeCreate = specialDateRepository.findAll().size();
        // Create the SpecialDate
        restSpecialDateMockMvc.perform(post("/api/special-dates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(specialDate)))
            .andExpect(status().isCreated());

        // Validate the SpecialDate in the database
        List<SpecialDate> specialDateList = specialDateRepository.findAll();
        assertThat(specialDateList).hasSize(databaseSizeBeforeCreate + 1);
        SpecialDate testSpecialDate = specialDateList.get(specialDateList.size() - 1);
        assertThat(testSpecialDate.getBeginDate()).isEqualTo(DEFAULT_BEGIN_DATE);
        assertThat(testSpecialDate.getFinishDate()).isEqualTo(DEFAULT_FINISH_DATE);
    }

    @Test
    @Transactional
    public void createSpecialDateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = specialDateRepository.findAll().size();

        // Create the SpecialDate with an existing ID
        specialDate.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecialDateMockMvc.perform(post("/api/special-dates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(specialDate)))
            .andExpect(status().isBadRequest());

        // Validate the SpecialDate in the database
        List<SpecialDate> specialDateList = specialDateRepository.findAll();
        assertThat(specialDateList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSpecialDates() throws Exception {
        // Initialize the database
        specialDateRepository.saveAndFlush(specialDate);

        // Get all the specialDateList
        restSpecialDateMockMvc.perform(get("/api/special-dates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specialDate.getId().intValue())))
            .andExpect(jsonPath("$.[*].beginDate").value(hasItem(DEFAULT_BEGIN_DATE.toString())))
            .andExpect(jsonPath("$.[*].finishDate").value(hasItem(DEFAULT_FINISH_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getSpecialDate() throws Exception {
        // Initialize the database
        specialDateRepository.saveAndFlush(specialDate);

        // Get the specialDate
        restSpecialDateMockMvc.perform(get("/api/special-dates/{id}", specialDate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(specialDate.getId().intValue()))
            .andExpect(jsonPath("$.beginDate").value(DEFAULT_BEGIN_DATE.toString()))
            .andExpect(jsonPath("$.finishDate").value(DEFAULT_FINISH_DATE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingSpecialDate() throws Exception {
        // Get the specialDate
        restSpecialDateMockMvc.perform(get("/api/special-dates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpecialDate() throws Exception {
        // Initialize the database
        specialDateRepository.saveAndFlush(specialDate);

        int databaseSizeBeforeUpdate = specialDateRepository.findAll().size();

        // Update the specialDate
        SpecialDate updatedSpecialDate = specialDateRepository.findById(specialDate.getId()).get();
        // Disconnect from session so that the updates on updatedSpecialDate are not directly saved in db
        em.detach(updatedSpecialDate);
        updatedSpecialDate
            .beginDate(UPDATED_BEGIN_DATE)
            .finishDate(UPDATED_FINISH_DATE);

        restSpecialDateMockMvc.perform(put("/api/special-dates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSpecialDate)))
            .andExpect(status().isOk());

        // Validate the SpecialDate in the database
        List<SpecialDate> specialDateList = specialDateRepository.findAll();
        assertThat(specialDateList).hasSize(databaseSizeBeforeUpdate);
        SpecialDate testSpecialDate = specialDateList.get(specialDateList.size() - 1);
        assertThat(testSpecialDate.getBeginDate()).isEqualTo(UPDATED_BEGIN_DATE);
        assertThat(testSpecialDate.getFinishDate()).isEqualTo(UPDATED_FINISH_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingSpecialDate() throws Exception {
        int databaseSizeBeforeUpdate = specialDateRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialDateMockMvc.perform(put("/api/special-dates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(specialDate)))
            .andExpect(status().isBadRequest());

        // Validate the SpecialDate in the database
        List<SpecialDate> specialDateList = specialDateRepository.findAll();
        assertThat(specialDateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSpecialDate() throws Exception {
        // Initialize the database
        specialDateRepository.saveAndFlush(specialDate);

        int databaseSizeBeforeDelete = specialDateRepository.findAll().size();

        // Delete the specialDate
        restSpecialDateMockMvc.perform(delete("/api/special-dates/{id}", specialDate.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SpecialDate> specialDateList = specialDateRepository.findAll();
        assertThat(specialDateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
