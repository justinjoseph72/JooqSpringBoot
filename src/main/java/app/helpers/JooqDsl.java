package app.helpers;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

import java.sql.Connection;

/**
 * Created by Justin on 17/04/2017.
 */
public class JooqDsl {
    public static final SQLDialect sqlDialect = SQLDialect.POSTGRES_9_4;
    private static final Boolean withRenderSchema = Boolean.FALSE;

    public static DSLContext getDSL(Connection conn){

        Settings settings = new Settings().withRenderSchema(withRenderSchema);

        return DSL.using(conn, sqlDialect, settings);
    }


    public static DSLContext getDSL(Connection conn, boolean withRenderSchema){

        Settings settings = new Settings().withRenderSchema(withRenderSchema);

        return DSL.using(conn, sqlDialect, settings);
    }

}
