package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.BillWithFoods;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.Food;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.FoodWithPerson;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FoodRepository {
    private FoodDao foodDao;
    private LiveData<List<BillWithFoods>> billFoods;
    private LiveData<List<FoodWithPerson>> listFoodWithPerson;

    public FoodRepository(Application application) {
        RoomDatabase db = RoomDatabase.getInstance(application);
        foodDao = db.getFoodDao();
    }

    public LiveData<List<BillWithFoods>> getBillFoods(int idBill) {
        return foodDao.getBillFoods(idBill);
    }

    public LiveData<List<FoodWithPerson>> getBillFoodWithPerson(int idBill) {
        return foodDao.getBillFoodWithPerson(idBill);
    }

    public int getAllFoodSize() {
        return foodDao.getAllFood().size();
    }

//    public Food createNewFood(int id, String name, int price, int idBill, int idPerson) {
//        Food food = new Food(id, name, price, idBill, idPerson);
//        return food;
//    }

    public int insert(Food food) {
        return insertAsyncTask(food).intValue();
    }

    public void delete(Food food) {
        deleteAsyncTask(food);
    }

    public void update(Food food) {
        updateAsyncTask(food);
    }

    public Integer insertAsyncTask(final Food food) {
        int idBaru = 0;
        try {
            idBaru = new AsyncTask<Integer, Void, Integer>() {
                @Override
                protected Integer doInBackground(Integer... integer) {
                    return (int) foodDao.insert(food);
                }
            }.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return idBaru;
    }

    public void deleteAsyncTask(final Food food) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                foodDao.delete(food);
                return null;
            }
        }.execute();
    }

    public void updateAsyncTask(final Food food) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                foodDao.update(food);
                return null;
            }
        }.execute();
    }
}
