package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.Bill;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.BillWithFoods;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.BillWithPerson;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.utils.Constant;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BillRepository {
    private BillDao billDao;
    private LiveData<List<BillWithPerson>> recentBills;
    private LiveData<List<BillWithPerson>> dueBills;

    public BillRepository(Application application) {
        RoomDatabase db = RoomDatabase.getInstance(application);
        billDao = db.getBillDao();
    }

    public LiveData<List<BillWithPerson>> getRecentBills() {
        return billDao.getRecentBill();
    }

    public LiveData<List<BillWithPerson>> getDueBills() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(calendar.getTimeInMillis()-Constant.ONE_DAY_TIME_IN_MILLIS);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);
        return billDao.getDueBill(calendar.getTimeInMillis());
    }

    public List<Bill> getFirstDueBill() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(calendar.getTimeInMillis()-Constant.ONE_DAY_TIME_IN_MILLIS);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);
        return billDao.getFirstDueBill(calendar.getTimeInMillis());
    }

    public LiveData<List<BillWithFoods>> getBillFoods(int id) {
        return billDao.getBillFoods(id);
    }

    public int insert(Bill bill) {
        return insertAsyncTask(bill).intValue();
    }

    public void delete(Bill bill) {
        deleteAsyncTask(bill);
    }

    public void update(Bill bill) {
        updateAsyncTask(bill);
    }

    public Integer insertAsyncTask(final Bill bill) {
        int idBaru = 0;
        try {
            idBaru= new AsyncTask<Integer, Void, Integer>() {
                @Override
                protected Integer doInBackground(Integer... integers) {
                    return (int) billDao.insert(bill);
                }
            }.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } return idBaru;
    }

    public void deleteAsyncTask(final Bill bill) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                billDao.delete(bill);
                return null;
            }
        }.execute();
    }

    public void updateAsyncTask(final Bill bill) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                billDao.update(bill);
                return null;
            }
        }.execute();
    }

    public BillWithPerson getBillWithPersonDetail(int idBill) {
        return billDao.getBillWithPersonDetail(idBill);
    }
}