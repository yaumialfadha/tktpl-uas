package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.Bill;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.BillWithFoods;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.BillWithPerson;

@Dao
public interface BillDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Bill bill);

    @Update
    void update(Bill bill);

    @Delete
    void delete(Bill bill);

    @Query("SELECT * FROM Bill ORDER BY id DESC")
    LiveData<List<BillWithPerson>> getRecentBill();

    @Query("SELECT * FROM Bill WHERE due_date >= :dateNow ORDER BY due_date ASC")
    LiveData<List<BillWithPerson>> getDueBill(long dateNow);

    @Query("SELECT * FROM Bill WHERE due_date >= :dateNow ORDER BY due_date ASC LIMIT 1")
    List<Bill> getFirstDueBill(long dateNow);

    @Query("SELECT * FROM Bill WHERE id = :id")
    LiveData<List<BillWithFoods>> getBillFoods(int id);

    @Query("SELECT * FROM Bill WHERE id = :idBill")
    BillWithPerson getBillWithPersonDetail(int idBill);
}
