package uz.tuit.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link uz.tuit.domain.Doctor} entity. This class is used
 * in {@link uz.tuit.web.rest.DoctorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /doctors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DoctorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstname;

    private StringFilter lastname;

    private StringFilter middleName;

    private StringFilter phone;

    private StringFilter position;

    private StringFilter qualification;

    private LongFilter departmentsId;

    private Boolean distinct;

    public DoctorCriteria() {}

    public DoctorCriteria(DoctorCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstname = other.firstname == null ? null : other.firstname.copy();
        this.lastname = other.lastname == null ? null : other.lastname.copy();
        this.middleName = other.middleName == null ? null : other.middleName.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.position = other.position == null ? null : other.position.copy();
        this.qualification = other.qualification == null ? null : other.qualification.copy();
        this.departmentsId = other.departmentsId == null ? null : other.departmentsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DoctorCriteria copy() {
        return new DoctorCriteria(this);
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

    public StringFilter getFirstname() {
        return firstname;
    }

    public StringFilter firstname() {
        if (firstname == null) {
            firstname = new StringFilter();
        }
        return firstname;
    }

    public void setFirstname(StringFilter firstname) {
        this.firstname = firstname;
    }

    public StringFilter getLastname() {
        return lastname;
    }

    public StringFilter lastname() {
        if (lastname == null) {
            lastname = new StringFilter();
        }
        return lastname;
    }

    public void setLastname(StringFilter lastname) {
        this.lastname = lastname;
    }

    public StringFilter getMiddleName() {
        return middleName;
    }

    public StringFilter middleName() {
        if (middleName == null) {
            middleName = new StringFilter();
        }
        return middleName;
    }

    public void setMiddleName(StringFilter middleName) {
        this.middleName = middleName;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getPosition() {
        return position;
    }

    public StringFilter position() {
        if (position == null) {
            position = new StringFilter();
        }
        return position;
    }

    public void setPosition(StringFilter position) {
        this.position = position;
    }

    public StringFilter getQualification() {
        return qualification;
    }

    public StringFilter qualification() {
        if (qualification == null) {
            qualification = new StringFilter();
        }
        return qualification;
    }

    public void setQualification(StringFilter qualification) {
        this.qualification = qualification;
    }

    public LongFilter getDepartmentsId() {
        return departmentsId;
    }

    public LongFilter departmentsId() {
        if (departmentsId == null) {
            departmentsId = new LongFilter();
        }
        return departmentsId;
    }

    public void setDepartmentsId(LongFilter departmentsId) {
        this.departmentsId = departmentsId;
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
        final DoctorCriteria that = (DoctorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(firstname, that.firstname) &&
            Objects.equals(lastname, that.lastname) &&
            Objects.equals(middleName, that.middleName) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(position, that.position) &&
            Objects.equals(qualification, that.qualification) &&
            Objects.equals(departmentsId, that.departmentsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, middleName, phone, position, qualification, departmentsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DoctorCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (firstname != null ? "firstname=" + firstname + ", " : "") +
            (lastname != null ? "lastname=" + lastname + ", " : "") +
            (middleName != null ? "middleName=" + middleName + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (position != null ? "position=" + position + ", " : "") +
            (qualification != null ? "qualification=" + qualification + ", " : "") +
            (departmentsId != null ? "departmentsId=" + departmentsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
