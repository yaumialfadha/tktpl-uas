package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class BillWithFoods {
    @Embedded
    public Bill bill;

    @Relation(parentColumn = "id", entityColumn = "id_bill", entity = Food.class)
    public List<Food> foods;

}
