package au.com.auspost.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@JsonPropertyOrder({ "src", "eqid", "timedate", "lat", "lon", "magnitude", "depth"})
public class EarthquakeDTO // extends ResourceSupport{
{


    @JsonIgnore
    private Long iid;
    @JsonProperty("src")
    private String stationSrc;
    @JsonProperty("eqid")
    private String eqId;
    @JsonProperty("timedate")
     private Date timeDate;
    @JsonProperty("lat")
    private Long latitude;
    @JsonProperty("lon")
    private Long longitude;
    @JsonProperty("magnitude")
    private BigDecimal magnitude;
    @JsonProperty("depth")
    private BigDecimal depth;

    @JsonProperty("region")
    private String regionName;

    public void setIid(Long iid) {
        this.iid = iid;
    }
    public Long getIid() {
        return iid;
    }

    @NotEmpty (message = "Station must be supplied")
    public String getStationSrc() {
        return stationSrc;
    }

    public void setStationSrc(String stationSrc) {
        this.stationSrc = stationSrc;
    }

    @NotEmpty (message = "Earthquake id must be supplied")
    public String getEqId() {
        return eqId;
    }

    public void setEqId(String eqId) {
        this.eqId = eqId;
    }

    @NotNull (message = "TimeDate must be a valid date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Australia/Melbourne")
    public Date getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(Date timeDate) {
        this.timeDate = timeDate;
    }

    @NotNull
    @Range(min = -90, max = 90, message = "Latitude must be between -90 and 90")
    public Long getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    @NotNull
    @Range(min = -180, max = 180, message = "Longitude must be between -180 and 180")
    public Long getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    @NotNull
    @DecimalMin("0.001")
    public BigDecimal getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(BigDecimal magnitude) {
        this.magnitude = magnitude;
    }

    @NotNull
    @DecimalMin("0.001")
    public BigDecimal getDepth() {
        return depth;
    }

    public void setDepth(BigDecimal depth) {
        this.depth = depth;
    }

    @NotEmpty (message = "Region must be supplied")
    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }


}
