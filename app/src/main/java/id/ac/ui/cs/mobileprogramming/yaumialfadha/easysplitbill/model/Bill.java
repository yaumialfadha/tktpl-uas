package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.converter.DateConverter;

import java.util.Date;

//@Entity(tableName = "bill")
@Entity
@TypeConverters(DateConverter.class)
public class Bill {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "due_date")
    private Date dueDate;

    @NonNull
    @ColumnInfo(name = "tax")
    private int tax;

    @ColumnInfo(name = "service_fee")
    private int serviceFee;

    public int getId() {
        return id;
    }

    public Date getDueDate() {
        return dueDate;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }

    public int getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(int serviceFee) {
        this.serviceFee = serviceFee;
    }
}
