package uz.tuit.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link uz.tuit.domain.Hospital} entity. This class is used
 * in {@link uz.tuit.web.rest.HospitalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /hospitals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HospitalCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private DoubleFilter longitude;

    private DoubleFilter latitude;

    private StringFilter description;

    private StringFilter address;

    private LongFilter regionId;

    private LongFilter districtId;

    private LongFilter departmentsId;

    private Boolean distinct;

    public HospitalCriteria() {}

    public HospitalCriteria(HospitalCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.longitude = other.longitude == null ? null : other.longitude.copy();
        this.latitude = other.latitude == null ? null : other.latitude.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.regionId = other.regionId == null ? null : other.regionId.copy();
        this.districtId = other.districtId == null ? null : other.districtId.copy();
        this.departmentsId = other.departmentsId == null ? null : other.departmentsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public HospitalCriteria copy() {
        return new HospitalCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public DoubleFilter getLongitude() {
        return longitude;
    }

    public DoubleFilter longitude() {
        if (longitude == null) {
            longitude = new DoubleFilter();
        }
        return longitude;
    }

    public void setLongitude(DoubleFilter longitude) {
        this.longitude = longitude;
    }

    public DoubleFilter getLatitude() {
        return latitude;
    }

    public DoubleFilter latitude() {
        if (latitude == null) {
            latitude = new DoubleFilter();
        }
        return latitude;
    }

    public void setLatitude(DoubleFilter latitude) {
        this.latitude = latitude;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getAddress() {
        return address;
    }

    public StringFilter address() {
        if (address == null) {
            address = new StringFilter();
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public LongFilter getRegionId() {
        return regionId;
    }

    public LongFilter regionId() {
        if (regionId == null) {
            regionId = new LongFilter();
        }
        return regionId;
    }

    public void setRegionId(LongFilter regionId) {
        this.regionId = regionId;
    }

    public LongFilter getDistrictId() {
        return districtId;
    }

    public LongFilter districtId() {
        if (districtId == null) {
            districtId = new LongFilter();
        }
        return districtId;
    }

    public void setDistrictId(LongFilter districtId) {
        this.districtId = districtId;
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
        final HospitalCriteria that = (HospitalCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(description, that.description) &&
            Objects.equals(address, that.address) &&
            Objects.equals(regionId, that.regionId) &&
            Objects.equals(districtId, that.districtId) &&
            Objects.equals(departmentsId, that.departmentsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, longitude, latitude, description, address, regionId, districtId, departmentsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HospitalCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (longitude != null ? "longitude=" + longitude + ", " : "") +
            (latitude != null ? "latitude=" + latitude + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (regionId != null ? "regionId=" + regionId + ", " : "") +
            (districtId != null ? "districtId=" + districtId + ", " : "") +
            (departmentsId != null ? "departmentsId=" + departmentsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
