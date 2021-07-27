package au.com.auspost.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({ "count", "earthquakes"})
public class EarthquakeAllDTO {
    private ResponseDTO<List<ResponseDTO<EarthquakeDTO>>>  earthquakes;

    @JsonProperty("count")
    public Integer getCount() {
        return earthquakes.getData().size();
    }

    public List<ResponseDTO<EarthquakeDTO>> getEarthquakes() {
        return earthquakes.getData();
    }
    public void setEarthquakes(ResponseDTO<List<ResponseDTO<EarthquakeDTO>>> earthquakes) {
        this.earthquakes = earthquakes;
    }

    @JsonProperty("count")
    public void setCount(Integer count) {};


}
