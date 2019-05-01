
package com.company;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "temp",
    "pressure",
    "humidity",
    "temp_min",
    "temp_max",
    "sea_level",
    "grnd_level"
})
public class Main {

    @JsonProperty("temp")
    public Float temp;
    @JsonProperty("pressure")
    public Float pressure;
    @JsonProperty("humidity")
    public Integer humidity;
    @JsonProperty("temp_min")
    public Float tempMin;
    @JsonProperty("temp_max")
    public Float tempMax;
    @JsonProperty("sea_level")
    public Float seaLevel;
    @JsonProperty("grnd_level")
    public Float grndLevel;

}
