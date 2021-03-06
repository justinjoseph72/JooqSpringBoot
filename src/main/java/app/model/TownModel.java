package app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Justin on 17/04/2017.
 */
public class TownModel {
    private Integer id;
    private String name;
    private String district;

    @JsonIgnore
    public Integer getId() {
        return id;
    }

    @JsonIgnore
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
