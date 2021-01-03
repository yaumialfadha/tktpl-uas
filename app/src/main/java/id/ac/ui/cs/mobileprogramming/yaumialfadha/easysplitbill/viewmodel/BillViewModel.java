package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.Bill;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.BillWithFoods;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.BillWithPerson;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.repository.BillRepository;
import java.util.List;

public class BillViewModel extends AndroidViewModel {
    private BillRepository billRepository;

    public BillViewModel(Application application) {
        super(application);
        billRepository = new BillRepository(application);
    }

    public LiveData<List<BillWithFoods>> getBillFoods(int id) {
        return billRepository.getBillFoods(id);
    }

    public LiveData<List<BillWithPerson>> getDueBills() {
        return billRepository.getDueBills();
    }

    public LiveData<List<BillWithPerson>> getRecentBills() {
        return billRepository.getRecentBills();
    }

    public BillWithPerson getBillWithPersonDetail(int idBill) {
        return billRepository.getBillWithPersonDetail(idBill);
    }

    public int insert(Bill bill) {
        return billRepository.insert(bill);
    }

    public void delete(Bill bill) {
        billRepository.delete(bill);
    }

    public void update(Bill bill) {
        billRepository.update(bill);
    }

    public List<Bill> getFirstDueBill() {
        return billRepository.getFirstDueBill();
    }
}
