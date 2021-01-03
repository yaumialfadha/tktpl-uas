package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.Person;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.PersonWithFoods;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.repository.PersonRepository;

public class PersonViewModel extends AndroidViewModel {
    public PersonRepository personRepository;

    public PersonViewModel(Application application) {
        super(application);
        personRepository = new PersonRepository(application);
    }
    public int insert(Person person) {
        return personRepository.insert(person);
    }

    public Person getPersonFromBill(int idBill, String personName) {
        return personRepository.getPersonFromBill(idBill, personName);
    }

    public PersonWithFoods getPersonWithFoods(int idBill, int idPerson) {
        return personRepository.getPersonWithFoods(idBill, idPerson);
    }

    public void delete(Person person) {
        personRepository.delete(person);
    }
}
