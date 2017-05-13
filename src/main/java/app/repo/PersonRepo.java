package app.repo;

import app.helpers.JooqDsl;
import app.model.PersonModel;
import app.model.TownModel;
import constants.CommonConstants;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.jooq.exception.DataAccessException;
import org.jooq.util.maven.example.tables.Person;
import org.jooq.util.maven.example.tables.Town;
import org.jooq.util.maven.example.tables.records.PersonRecord;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.jooq.RecordValueReader;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.jooq.util.maven.example.tables.Person.*;

/**
 * Created by Justin on 23/04/2017.
 */
public class PersonRepo {

    private final DSLContext dsl ;
    private final Connection conn;
    private ModelMapper mapper;
    public PersonRepo(Connection conn)
    {
        this.conn = conn;
        dsl = JooqDsl.getDSL(conn);
         mapper = new ModelMapper();
        mapper.getConfiguration().addValueReader(new RecordValueReader());
        mapper.getConfiguration().setSourceNameTokenizer(NameTokenizers.UNDERSCORE);

    }

    public String  addPerson(PersonModel person){
        String myReturn = CommonConstants.ERROR;
        try {
            TownModel townModel = null;
            if(person.getTown()!=null)
            {
                SelectQuery townQuery = dsl.selectQuery();
                townQuery.addSelect();
                townQuery.addFrom(Town.TOWN);
                townQuery.addConditions(Town.TOWN.NAME.trim().eq(person.getTown().getName().trim()));
                townQuery.addConditions((Town.TOWN.DISTRICT.trim().eq(person.getTown().getDistrict().trim())));
                Record record = townQuery.fetchOne();
                if(record!=null){
                     townModel = mapper.map(record,TownModel.class);
                    person.setTown(townModel);
                    PersonRecord personRec = dsl.newRecord(PERSON);
                    personRec.setFirstName(person.getFirstName());
                    personRec.setLastName(person.getLastName());
                    personRec.setTownId(person.getTown().getId());
                    dsl.insertInto(PERSON).set(personRec).execute();
                    myReturn =  CommonConstants.SUCCESS;
                }
                if(townModel == null){
                    myReturn= CommonConstants.INVALID_TOWN;
                }
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return myReturn;
    }

    public List<PersonModel> getPersons(String name)
    {
        List<PersonModel> myReturn = new ArrayList<>();
        if(StringUtils.isNotBlank(name)){
            StringBuilder searchText = new StringBuilder("%");
            searchText.append(name);
            searchText.append("%");
            SelectQuery query = dsl.selectQuery();
            query.addSelect();
            query.addFrom(PERSON);
            query.addJoin(Town.TOWN, PERSON.TOWN_ID.eq(Town.TOWN.ID));
            Condition firstNameCondition = Person.PERSON.FIRST_NAME.trim().like(searchText.toString());
            Condition lastNameCondition = Person.PERSON.LAST_NAME.trim().like(searchText.toString());
            query.addConditions(firstNameCondition.or(lastNameCondition));
            Result<?> result = query.fetch();
            result.forEach(rec ->
            {
                PersonModel person = mapper.map(rec,PersonModel.class);
                TownModel town = mapper.map(rec,TownModel.class);
                town.setId(rec.getValue(Town.TOWN.ID));
                person.setTown(town);
                myReturn.add(person);
            });
        }
        return  myReturn;
    }

    public List<PersonModel> getPeopleInTown(String town){
        List<PersonModel> people = null;
        if(StringUtils.isNotEmpty(town)){
            SelectQuery<Record> query = dsl.selectQuery();
            query.addSelect(Person.PERSON.FIRST_NAME,Person.PERSON.LAST_NAME,Person.PERSON.TOWN_ID);
            query.addFrom(Person.PERSON);
            query.addJoin(Town.TOWN,Town.TOWN.ID.eq(Person.PERSON.TOWN_ID));
            query.addConditions(Town.TOWN.NAME.eq(town.trim()));
            Result<Record> res = query.fetch();
            if(res!=null && res.isNotEmpty()){
                people = new ArrayList<PersonModel>();
                for(Record rec : res){
                    PersonModel person = mapper.map(rec,PersonModel.class);
                    people.add(person);
                }
            }
        }

        return people;
    }
}
