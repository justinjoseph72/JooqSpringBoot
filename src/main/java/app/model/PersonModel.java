package app.model;

/**
 * Created by Justin on 23/04/2017.
 */
public class PersonModel {
    private Integer id;
    private String firstName;
    private String secondName;
    private TownModel town;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public TownModel getTown() {
        return town;
    }

    public void setTown(TownModel town) {
        this.town = town;
    }
}
