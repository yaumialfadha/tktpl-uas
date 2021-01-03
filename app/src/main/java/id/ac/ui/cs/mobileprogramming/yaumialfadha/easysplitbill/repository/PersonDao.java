package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.BillWithPerson;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.Person;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.PersonWithFoods;

import java.util.List;

@Dao
public interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Person person);

    @Update
    void update(Person person);

    @Delete
    void delete(Person person);

    @Query("SELECT * FROM Person WHERE id_bill = :idBill")
    LiveData<List<BillWithPerson>> getBillPerson(int idBill);

    @Query("SELECT * FROM Person")
    List<Person> getAllPerson();

    @Query("SELECT * FROM Person WHERE id_bill = :idBill and name = :name limit 1")
    Person getPersonInBill(int idBill, String name);

    @Query("SELECT * FROM Person WHERE id_bill = :idBill and id= :idPerson limit 1")
    PersonWithFoods getPersonWithFoodsInBill(int idBill, int idPerson);
}
