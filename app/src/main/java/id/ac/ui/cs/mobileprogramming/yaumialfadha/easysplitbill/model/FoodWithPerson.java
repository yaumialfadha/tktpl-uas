package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model;

import androidx.room.Embedded;
import androidx.room.Relation;

public class FoodWithPerson {
    @Embedded
    public Food food;

    @Relation(parentColumn = "id_person", entityColumn = "id", entity = Person.class)
    public Person person;
}
