package uz.tuit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A QueueForDoctor.
 */
@Entity
@Table(name = "queue_for_doctor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QueueForDoctor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private Integer number;

    @ManyToOne
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "departments" }, allowSetters = true)
    private Doctor doctor;

    @ManyToOne
    @JsonIgnoreProperties(value = { "hospital", "doctors" }, allowSetters = true)
    private Department department;

    @ManyToOne
    @JsonIgnoreProperties(value = { "region", "district", "departments" }, allowSetters = true)
    private Hospital hospital;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public QueueForDoctor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return this.number;
    }

    public QueueForDoctor number(Integer number) {
        this.setNumber(number);
        return this;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public QueueForDoctor user(User user) {
        this.setUser(user);
        return this;
    }

    public Doctor getDoctor() {
        return this.doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public QueueForDoctor doctor(Doctor doctor) {
        this.setDoctor(doctor);
        return this;
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public QueueForDoctor department(Department department) {
        this.setDepartment(department);
        return this;
    }

    public Hospital getHospital() {
        return this.hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public QueueForDoctor hospital(Hospital hospital) {
        this.setHospital(hospital);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QueueForDoctor)) {
            return false;
        }
        return id != null && id.equals(((QueueForDoctor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QueueForDoctor{" +
            "id=" + getId() +
            ", number=" + getNumber() +
            "}";
    }
}
