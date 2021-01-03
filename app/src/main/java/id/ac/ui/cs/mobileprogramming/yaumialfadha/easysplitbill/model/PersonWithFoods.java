package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model;

import android.util.Log;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class PersonWithFoods {
    @Embedded
    public Person person;

    @Relation(parentColumn = "id", entityColumn = "id_person", entity = Food.class)
    public List<Food> foods;

    public int getTotalFoodPrice(int tax, int serviceFee) {
        int res = 0;
        for (Food food : foods) {
            res += (food.getPrice() * ((double)(100+tax+serviceFee) / 100));
            Log.i("res", food.getName()+" "+((double)food.getPrice() * ((double)(100+tax) / 100)));
        }
        return res;
    }
}