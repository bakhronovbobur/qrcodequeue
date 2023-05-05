package uz.tuit.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link uz.tuit.domain.Hospital} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HospitalDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Double longitude;

    private Double latitude;

    private String description;

    private String address;

    private RegionDTO region;

    private DistrictDTO district;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public RegionDTO getRegion() {
        return region;
    }

    public void setRegion(RegionDTO region) {
        this.region = region;
    }

    public DistrictDTO getDistrict() {
        return district;
    }

    public void setDistrict(DistrictDTO district) {
        this.district = district;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HospitalDTO)) {
            return false;
        }

        HospitalDTO hospitalDTO = (HospitalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, hospitalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HospitalDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", longitude=" + getLongitude() +
            ", latitude=" + getLatitude() +
            ", description='" + getDescription() + "'" +
            ", address='" + getAddress() + "'" +
            ", region=" + getRegion() +
            ", district=" + getDistrict() +
            "}";
    }
}
