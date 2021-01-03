package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.converter.DateConverter;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.Bill;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.Food;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.Person;

@Database(entities = { Bill.class, Food.class, Person.class }, version = 1, exportSchema = false)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    private static final String DB_NAME = "database.db";
    private static RoomDatabase INSTANCE;

    public abstract BillDao getBillDao();
    public abstract FoodDao getFoodDao();
    public abstract PersonDao getPersonDao();

    public static synchronized RoomDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = create(context);
        }
        return INSTANCE;
    }

    private static RoomDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                RoomDatabase.class,
                DB_NAME)
//                .addCallback(sRoomDatabaseCallback)
                .allowMainThreadQueries()
                .build();
    }

    private static androidx.room.RoomDatabase.Callback sRoomDatabaseCallback =
        new androidx.room.RoomDatabase.Callback(){

            @Override
            public void onOpen (@NonNull SupportSQLiteDatabase db){
                super.onOpen(db);
                new PopulateDbAsync(INSTANCE).execute();
            }
        };
}

class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

    private final BillDao billDao;
    private final FoodDao foodDao;
    private final PersonDao personDao;

    PopulateDbAsync(RoomDatabase db) {
        billDao = db.getBillDao();
        foodDao = db.getFoodDao();
        personDao = db.getPersonDao();
    }

    @Override
    protected Void doInBackground(final Void... params) {
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date fd;
        Date date = null;
        try {
            fd = formatter.parse("20191028");
            date = DateConverter.toDate(fd.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Bill bill = new Bill();
        bill.setId(1);
        bill.setName("Bill satu");
        bill.setTax(10);
        bill.setServiceFee(0);
        bill.setDueDate(date);
        billDao.insert(bill);
        Log.i("liat", bill.getName());

        Person person = new Person();
        personDao.insert(person);

        Food food = new Food();
        foodDao.insert(food);

        return null;
    }
}
