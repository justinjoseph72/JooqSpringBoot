package app.repo;

import app.helpers.JooqDsl;
import app.model.PersonModel;
import app.model.TownModel;
import constants.CommonConstants;
import org.apache.commons.lang3.StringUtils;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SelectQuery;
import org.jooq.exception.DataAccessException;
import org.jooq.util.maven.example.tables.Person;
import org.jooq.util.maven.example.tables.Town;
import org.jooq.util.maven.example.tables.records.PersonRecord;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.jooq.util.maven.example.tables.Person.*;
import static org.jooq.util.maven.example.tables.Town.*;

/**
 * Created by Justin on 23/04/2017.
 */
public class PersonRepo {

    private final DSLContext dsl ;
    private final Connection conn;
    public PersonRepo(Connection conn)
    {
        this.conn = conn;
        dsl = JooqDsl.getDSL(conn);

    }

    public String  addPerson(PersonModel person){
        String myReturn = CommonConstants.ERROR;
        try {
            TownModel townModel = null;
            if(person.getTown()!=null){
                SelectQuery townQuery = dsl.selectQuery();
                townQuery.addSelect();
                townQuery.addFrom(TOWN);
                townQuery.addConditions(TOWN.NAME.trim().eq(person.getTown().getName().trim()));
                townModel = townQuery.fetchOne().into(TownModel.class);
                if(townModel == null){
                    myReturn= CommonConstants.INVALID_TOWN;
                }
                else{
                    person.setTown(townModel);
                    PersonRecord personRec = dsl.newRecord(PERSON);
                    personRec.setFirstName(person.getFirstName());
                    personRec.setLastName(person.getSecondName());
                    personRec.setTownId(person.getTown().getId());
                    dsl.insertInto(PERSON).set(personRec).execute();
                    myReturn =  CommonConstants.SUCCESS;
                }
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return myReturn;
    }

    public List<PersonModel> getPerson(String firstName, String lastName)
    {
        List<PersonModel> myReturn = new ArrayList<>();
        if(StringUtils.isNotBlank(firstName) || StringUtils.isNotBlank(lastName)){
            SelectQuery query = dsl.selectQuery();
            query.addSelect();
            query.addFrom(PERSON);
            query.addJoin(TOWN, PERSON.TOWN_ID.eq(TOWN.ID));
            if((StringUtils.isNotBlank(firstName))){
                query.addConditions(PERSON.FIRST_NAME.eq(firstName));
            }
            if(StringUtils.isNotBlank(lastName)){
                query.addConditions(PERSON.LAST_NAME.eq(lastName));
            }
            Result<?> result = query.fetch();
            result.forEach(rec ->
            {
//                PersonModel person = new PersonModel();
//                TownModel town = new TownModel();
                ModelMapper mapper = new ModelMapper();
                PersonModel person = mapper.map(rec,PersonModel.class);
                TownModel town = mapper.map(rec,TownModel.class);
                person.setTown(town);
                myReturn.add(person);
            });
        }
        SelectQuery query = dsl.selectQuery();

        return  myReturn;
    }
}
