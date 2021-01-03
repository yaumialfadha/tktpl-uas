package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//@Entity(tableName = "food", foreignKeys = @ForeignKey(entity = Bill.class,
//                                parentColumns = "id",
//                                childColumns = "id_bill",
//                                onDelete = CASCADE))
//@Entity(tableName = "food")
@Entity
public class Food {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "price")
    private int price;

    @ColumnInfo(name = "id_bill")
    private int idBill;

    @ColumnInfo(name = "id_person")
    private int idPerson;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdBill() {
        return idBill;
    }

    public void setIdBill(int idBill) {
        this.idBill = idBill;
    }

    public int getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(int idPerson) {
        this.idPerson = idPerson;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
