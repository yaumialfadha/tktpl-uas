package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.BillWithFoods;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.Food;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.FoodWithPerson;

import java.util.List;

@Dao
public interface FoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Food food);

    @Update
    void update(Food food);

    @Delete
    void delete(Food food);

    @Query("SELECT * FROM Food WHERE id_bill = :idBill")
    LiveData<List<BillWithFoods>> getBillFoods(int idBill);

    @Query("SELECT * FROM Food WHERE id_bill = :idBill")
    LiveData<List<FoodWithPerson>> getBillFoodWithPerson(int idBill);

    @Query("SELECT * FROM Food")
    List<Food> getAllFood();
}
