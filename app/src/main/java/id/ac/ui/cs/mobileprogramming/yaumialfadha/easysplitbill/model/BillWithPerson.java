package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class BillWithPerson {
    @Embedded
    public Bill bill;

    @Relation(parentColumn = "id", entityColumn = "id_bill", entity = Person.class)
    public List<PersonWithFoods> personWithFoods;

    public int getTotalPrice() {
        int res = 0;
        for (PersonWithFoods person : personWithFoods) {
            res += person.getTotalFoodPrice(bill.getTax(), bill.getServiceFee());
        }
        return res;
    }
}
