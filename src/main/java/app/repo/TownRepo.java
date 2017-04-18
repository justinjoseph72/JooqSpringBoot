package app.repo;

import app.helpers.JooqDsl;
import app.helpers.LogHelper;
import app.model.TownModel;
import org.jooq.DSLContext;
import org.jooq.util.maven.example.tables.Town;
import org.jooq.util.maven.example.tables.records.TownRecord;


import java.sql.Connection;
import java.util.List;

/**
 * Created by Justin on 16/04/2017.
 */
public class TownRepo {

    private final DSLContext dsl ;
    private final Connection conn;
    public TownRepo(Connection conn)
    {
        this.conn = conn;
        dsl = JooqDsl.getDSL(conn);

    }


    public List<TownModel> getAllTowns(){
        LogHelper.writeLog(this,"start all town","getAllTowns");
        List<TownModel> towns = dsl.selectFrom(Town.TOWN).fetchInto(TownModel.class);
        System.out.println("the towns are ");
        return towns;
    }

    public  List<TownModel> getTown(String name){
        LogHelper.writeLog(this,"start get town","getgetTown");
        List<TownModel> towns = dsl.selectFrom(Town.TOWN).where(Town.TOWN.NAME.trim().equal(name)).fetchInto(TownModel.class);
        return towns;
    }

    public boolean insertTown(List<TownModel> models){
        LogHelper.writeLog(this,"write start","insertTown");
        boolean myReturn = true;
        try {
            models.forEach(model ->{
                TownRecord newRec = dsl.newRecord(Town.TOWN);
                newRec.setName(model.getName());
                newRec.setDistrict(model.getDistrict());
                dsl.insertInto(Town.TOWN).set(newRec).execute();
                LogHelper.writeLog(this,"the id is " + newRec.getId(),"insertTown");
            });

        } catch (Exception e) {
            myReturn = false;
            e.printStackTrace();
        }
        return myReturn;
    }
}
