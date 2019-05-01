
package com.company;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "coord",
    "weather",
    "base",
    "main",
    "wind",
    "rain",
    "clouds",
    "dt",
    "sys",
    "id",
    "name",
    "cod"
})
public class Model1 {

    @JsonProperty("coord")
    public Coord coord;
    @JsonProperty("weather")
    public List<Weather1> weather = null;
    @JsonProperty("base")
    public String base;
    @JsonProperty("main")
    public Main main;
    @JsonProperty("wind")
    public Wind wind;
    @JsonProperty("rain")
    public Rain rain;
    @JsonProperty("clouds")
    public Clouds clouds;
    @JsonProperty("dt")
    public Integer dt;
    @JsonProperty("sys")
    public Sys sys;
    @JsonProperty("id")
    public Integer id;
    @JsonProperty("name")
    public String name;
    @JsonProperty("cod")
    public Integer cod;

}
