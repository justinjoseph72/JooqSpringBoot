package app.controller;

import app.helpers.ConnectionHelper;
import app.model.TownModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import app.repo.TownRepo;

import java.sql.Connection;
import java.util.List;

/**
 * Created by Justin on 17/04/2017.
 */
@RestController
public class TownController {

    @RequestMapping(value="/towns",method = RequestMethod.GET)
    @ResponseBody
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
    @ResponseBody
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
    @RequestMapping(value ="/towns/new", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> newTown(@RequestBody List<TownModel> towns){
        try(Connection conn = ConnectionHelper.getConnectionFromDataSource()){
            if(new TownRepo(conn).insertTown(towns)){
                return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<Void>(HttpStatus.I_AM_A_TEAPOT);
    }

}
