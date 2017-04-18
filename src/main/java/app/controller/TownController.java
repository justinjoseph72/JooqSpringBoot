package app.controller;

import app.helpers.ConnectionHelper;
import app.model.TownModel;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import app.repo.TownRepo;

import java.sql.Connection;
import java.util.List;

/**
 * Created by Justin on 17/04/2017.
 */
@RestController
public class TownController {

    @RequestMapping(value="/towns",method = RequestMethod.GET)
    public List<TownModel> getAllTowns(){
        List<TownModel> townList = null;
        try(Connection conn = ConnectionHelper.getConnectionFromDataSource()){
            TownRepo myRepo = new TownRepo(conn);
             townList = myRepo.getAllTowns();

        }
        catch(Exception ex)
        {
            ex.printStackTrace();

        }

        return townList;
    }

    @RequestMapping(value = "/towns/{town}", method = RequestMethod.GET)
    public List<TownModel> getTown(@PathVariable String town){
        List<TownModel> towns = null;
        try(Connection conn = ConnectionHelper.getConnectionFromDataSource()){
            towns = new TownRepo(conn).getTown(town);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return towns;
    }
    @RequestMapping(value ="/towns/new", method = RequestMethod.GET)
    public String newTown(TownModel town){
        String myReturn ="Error";
        try(Connection conn = ConnectionHelper.getConnectionFromDataSource()){
            if(new TownRepo(conn).insertTown(town)){
                myReturn = "Success";
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return myReturn;
    }

}
