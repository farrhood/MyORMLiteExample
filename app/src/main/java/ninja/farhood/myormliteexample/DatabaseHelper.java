package ninja.farhood.myormliteexample;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "bank";
    private static final int DATABASE_VERSION = 1;

    private Context mContext;
    private Dao<Person, Long> mSimpleDao = null;

    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;

    }

    public Dao<Person, Long> getDao(){

        try {
            if (mSimpleDao == null) {

                mSimpleDao = getDao(Person.class);

            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        return mSimpleDao;

    }

    /**
     * Fetch all the Person records in the database
     *
     * @return List of Person records found in the database
     *
     */

    public List<Person> GetData() {

        DatabaseHelper helper = new DatabaseHelper(mContext);

        try {

            Dao<Person, Long> simpleDao = helper.getDao();
            return simpleDao.queryForAll();

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return new ArrayList<>();

    }

    /**
     * Add the specified Person object to the database
     *
     * @param person Person object to persist to the database
     * @return
     *      The number of rows updated in the database. This should be 1.
     *      -1 is returned in case an exception has occured
     */

    public int addData(Person person){

        try {

            Dao<Person, Long> dao = getDao();
            return dao.create(person);

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return 1;

    }

    /**
     *
     * Delete all Person records in the database
     *
     */

    public void deleteAll() {

        try {

            Dao<Person, Long> dao = getDao();
            List<Person> list = dao.queryForAll();
            dao.delete(list);

        } catch (SQLException e) {

            e.printStackTrace();

        }


    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {

        try {

            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Person.class);

        } catch (SQLException e) {

            Log.e(DatabaseHelper.class.getName(), "Cannot create database", e);
            throw new RuntimeException(e);

        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {

        try {

            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, Person.class, true);
            onCreate(db, connectionSource);

        } catch (SQLException e) {

            Log.e(DatabaseHelper.class.getName(), "Cannot drop databases", e);
            throw new RuntimeException(e);

        }

    }


}
