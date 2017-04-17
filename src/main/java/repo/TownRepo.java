package repo;

import helpers.JooqDsl;
import helpers.LogHelper;
import model.TownModel;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
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
        List<TownModel> towns = null;
        towns = dsl.selectFrom(Town.TOWN).fetchInto(TownModel.class);
        System.out.println("the towns are ");
        return towns;
    }

    public  List<TownModel> getTown(String name){
        LogHelper.writeLog(this,"start get town","getgetTown");
        List<TownModel> towns = null;
        towns =dsl.selectFrom(Town.TOWN).where(Town.TOWN.NAME.trim().equal(name)).fetchInto(TownModel.class);
        return towns;
    }
}
