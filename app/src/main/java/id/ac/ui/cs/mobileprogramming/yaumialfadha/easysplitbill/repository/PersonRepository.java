package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.BillWithPerson;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.Person;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.PersonWithFoods;

public class PersonRepository {
    private PersonDao personDao;
    private LiveData<List<BillWithPerson>> billPersons;

    public PersonRepository(Application application) {
        RoomDatabase db = RoomDatabase .getInstance(application);
        personDao = db.getPersonDao();
    }

    public LiveData<List<BillWithPerson>> getBillPersons(int idBill) {
        return personDao.getBillPerson(idBill);
    }

    public int insert(Person person) {
        return insertAsyncTask(person).intValue();
    }

    public void delete(Person person) {
        deleteAsyncTask(person);
    }

    public void update(Person person) {
        updateAsyncTask(person);
    }

    public Integer insertAsyncTask(final Person person) {
        int idBaru = 0;
        try {
            idBaru = new AsyncTask<Integer, Void, Integer>() {
                @Override
                protected Integer doInBackground(Integer... integers) {
                    return (int) personDao.insert(person);
                }
            }.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } return idBaru;
    }

    public void deleteAsyncTask(final Person person) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                personDao.delete(person);
                return null;
            }
        }.execute();
    }

    public void updateAsyncTask(final Person person) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                personDao.update(person);
                return null;
            }
        }.execute();
    }

    public Person getPersonFromBill(int idBill, String personName) {
        return personDao.getPersonInBill(idBill, personName);
    }

    public PersonWithFoods getPersonWithFoods(int idBill, int idPerson) {
        return personDao.getPersonWithFoodsInBill(idBill, idPerson);
    }
}
