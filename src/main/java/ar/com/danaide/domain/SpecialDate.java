package ar.com.danaide.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A SpecialDate.
 */
@Entity
@Table(name = "special_date")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SpecialDate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "begin_date")
    private LocalDate beginDate;

    @Column(name = "finish_date")
    private LocalDate finishDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public SpecialDate beginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
        return this;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public SpecialDate finishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
        return this;
    }

    public void setFinishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpecialDate)) {
            return false;
        }
        return id != null && id.equals(((SpecialDate) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpecialDate{" +
            "id=" + getId() +
            ", beginDate='" + getBeginDate() + "'" +
            ", finishDate='" + getFinishDate() + "'" +
            "}";
    }
}
