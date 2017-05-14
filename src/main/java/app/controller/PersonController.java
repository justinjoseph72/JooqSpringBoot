package app.controller;

import app.helpers.ConnectionHelper;
import app.model.PersonModel;
import app.model.TownModel;
import app.repo.PersonRepo;
import constants.CommonConstants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.awt.SunHints;

import java.sql.Connection;
import java.util.List;

/**
 * Created by Justin on 13/05/2017.
 */
@RestController
public class PersonController {

    public PersonController(){

    }

    @ApiOperation(value = "Get all the people for a town")
    @RequestMapping(value = "/people/{town}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<PersonModel>> getPeopleOfTown(@ApiParam(value = "town name") @PathVariable String town){
        List<PersonModel> people = null;
        try(Connection conn = ConnectionHelper.getConnectionFromDataSource()){
            PersonRepo myRepo = new PersonRepo(conn);
            people = myRepo.getPeopleInTown(town);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return new ResponseEntity<List<PersonModel>>(people,HttpStatus.INTERNAL_SERVER_ERROR);

        }
        if(people!=null){
            return new ResponseEntity<List<PersonModel>>(people, HttpStatus.OK);
        }
        return new ResponseEntity<List<PersonModel>>(people,HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value="Add a new person to a town")
    @RequestMapping(value = "/addPerson",method = RequestMethod.POST)
    public ResponseEntity<Void> addNewPerson(@ApiParam(value = "person details") @RequestBody PersonModel person){
        String response = null;
        try(Connection conn = ConnectionHelper.getConnectionFromDataSource()) {
            if(StringUtils.isNotEmpty(person.getTown().getName()) && StringUtils.isNotEmpty(person.getLastName())){
               response = new PersonRepo(conn).addPerson(person);
                if( StringUtils.isNumeric(response) && Integer.parseInt(response)>0){
                    HttpHeaders header = new HttpHeaders();
                    header.set("personId",response);
                    return  new ResponseEntity<Void>(header,HttpStatus.CREATED);
                }
                if(CommonConstants.ERROR.equals(response)){
                    return  new ResponseEntity<Void>(HttpStatus.EXPECTATION_FAILED);
                }
                if(CommonConstants.INVALID_TOWN.equals(response)){
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("error", response);
                    return  new ResponseEntity<Void>(headers,HttpStatus.EXPECTATION_FAILED);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<Void>(HttpStatus.EXPECTATION_FAILED);
    }

    @ApiOperation(value = "Get list of matching persons")
    @RequestMapping(value = "/person/lookUp", method = RequestMethod.GET)
    public ResponseEntity<List<PersonModel>> getPerson(@ApiParam(value = "person search text") @RequestParam(value = "searchText", required = true) String searchText){
        List<PersonModel> peopleList = null;
        try(Connection conn = ConnectionHelper.getConnectionFromDataSource()){
                peopleList = new PersonRepo(conn).getPersons(searchText);
        }catch (Exception ex){
            ex.printStackTrace();
            new ResponseEntity<List<PersonModel>>(peopleList, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<PersonModel>>(peopleList, HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value ="updating a person's info")
    @RequestMapping(value = "/person/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> updatePerson(@PathVariable Integer id,@RequestBody PersonModel person){
        if(person== null || id ==null || id<1 ){
            return new ResponseEntity<String>(CommonConstants.INVALID_PERSON,HttpStatus.BAD_REQUEST);
        }
        try(Connection conn = ConnectionHelper.getConnectionFromDataSource()){
            String response = new PersonRepo(conn).updatePerson(id,person);
            return new ResponseEntity<String>(response,HttpStatus.OK);
        }
        catch(Exception ex){
            ex.printStackTrace();
            return  new ResponseEntity<String>(CommonConstants.ERROR,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
