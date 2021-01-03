package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.Food;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.FoodWithPerson;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.repository.FoodRepository;
import java.util.List;

public class FoodViewModel extends AndroidViewModel {
    public FoodViewModel(Application application) {
        super(application);
    }
    public FoodRepository foodRepository;

    public LiveData<List<FoodWithPerson>> getBillFoodsWithPerson(int idBill) {
        foodRepository = new FoodRepository(getApplication());
        LiveData<List<FoodWithPerson>> billFoodsWithPerson = foodRepository.getBillFoodWithPerson(idBill);
        return billFoodsWithPerson;
    }

    public void insert(Food food) {
        foodRepository.insert(food);
    }

    public void update(Food food) {
        foodRepository.update(food);
    }

    public int getAllFoodSize() {
        return foodRepository.getAllFoodSize();
    }

    public void delete(Food food) {
        foodRepository.delete(food);
    }
}
