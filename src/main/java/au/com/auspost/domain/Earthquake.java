package au.com.auspost.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Earthquake {

    @Id
    @GeneratedValue
    @Column(name="ID")
    private Long iid;
    @ManyToOne
    @JoinColumn(name="STATION_ID")
    private Station station;
    @Column(name="EQ_ID")
    private String eqId;
    @Column(name="TIME_DATE")
    private Date timeDate;
    @Column(name="LATITUDE")
    private Long latitude;
    @Column(name="LONGITUDE")
    private Long longitude;
    @Column(name="MAGNITUDE")
    private BigDecimal magnitude;
    @Column(name="DEPTH")
    private BigDecimal depth;
    @ManyToOne
    @JoinColumn(name="REGION_ID")
    private Region region;

    public Long getIid() {
        return iid;
    }

    public void setIid(Long iid) {
        this.iid = iid;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public String getEqId() {
        return eqId;
    }

    public void setEqId(String eqId) {
        this.eqId = eqId;
    }

    public Date getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(Date timeDate) {
        this.timeDate = timeDate;
    }

    public Long getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public Long getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(BigDecimal magnitude) {
        this.magnitude = magnitude;
    }

    public BigDecimal getDepth() {
        return depth;
    }

    public void setDepth(BigDecimal depth) {
        this.depth = depth;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

}
