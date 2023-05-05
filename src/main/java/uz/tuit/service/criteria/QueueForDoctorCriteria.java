package uz.tuit.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link uz.tuit.domain.QueueForDoctor} entity. This class is used
 * in {@link uz.tuit.web.rest.QueueForDoctorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /queue-for-doctors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QueueForDoctorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter number;

    private LongFilter userId;

    private LongFilter doctorId;

    private LongFilter departmentId;

    private LongFilter hospitalId;

    private Boolean distinct;

    public QueueForDoctorCriteria() {}

    public QueueForDoctorCriteria(QueueForDoctorCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.number = other.number == null ? null : other.number.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.doctorId = other.doctorId == null ? null : other.doctorId.copy();
        this.departmentId = other.departmentId == null ? null : other.departmentId.copy();
        this.hospitalId = other.hospitalId == null ? null : other.hospitalId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public QueueForDoctorCriteria copy() {
        return new QueueForDoctorCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getNumber() {
        return number;
    }

    public IntegerFilter number() {
        if (number == null) {
            number = new IntegerFilter();
        }
        return number;
    }

    public void setNumber(IntegerFilter number) {
        this.number = number;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getDoctorId() {
        return doctorId;
    }

    public LongFilter doctorId() {
        if (doctorId == null) {
            doctorId = new LongFilter();
        }
        return doctorId;
    }

    public void setDoctorId(LongFilter doctorId) {
        this.doctorId = doctorId;
    }

    public LongFilter getDepartmentId() {
        return departmentId;
    }

    public LongFilter departmentId() {
        if (departmentId == null) {
            departmentId = new LongFilter();
        }
        return departmentId;
    }

    public void setDepartmentId(LongFilter departmentId) {
        this.departmentId = departmentId;
    }

    public LongFilter getHospitalId() {
        return hospitalId;
    }

    public LongFilter hospitalId() {
        if (hospitalId == null) {
            hospitalId = new LongFilter();
        }
        return hospitalId;
    }

    public void setHospitalId(LongFilter hospitalId) {
        this.hospitalId = hospitalId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final QueueForDoctorCriteria that = (QueueForDoctorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(number, that.number) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(doctorId, that.doctorId) &&
            Objects.equals(departmentId, that.departmentId) &&
            Objects.equals(hospitalId, that.hospitalId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, userId, doctorId, departmentId, hospitalId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QueueForDoctorCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (number != null ? "number=" + number + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (doctorId != null ? "doctorId=" + doctorId + ", " : "") +
            (departmentId != null ? "departmentId=" + departmentId + ", " : "") +
            (hospitalId != null ? "hospitalId=" + hospitalId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
