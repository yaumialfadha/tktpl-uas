package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.views;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.BIllDetailActivity;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.Food;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.FoodWithPerson;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.Person;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.PersonWithFoods;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.viewmodel.FoodViewModel;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.viewmodel.PersonViewModel;

public class CreateOrEditBillFoodsFragment extends Fragment {

    private int idBill;
    private View billFoodFragmentView;
    private FoodViewModel foodViewModel;
    private PersonViewModel personViewModel;
    private BillFoodAdapter bIllFoodAdapter = null;
    private ArrayList<String> stringPersonNameList;
    private ArrayList<String> stringFoods;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private TextView alert_blank_table;
    private ArrayAdapter<String> adapterFoodList;
    BroadcastReceiver networkInfoReceiver;

    public CreateOrEditBillFoodsFragment(int idBill) {
        this.idBill = idBill;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foodViewModel = ViewModelProviders.of(this).get(FoodViewModel.class);
        personViewModel = ViewModelProviders.of(this).get(PersonViewModel.class);
        stringPersonNameList = new ArrayList<>();
        stringFoods = new ArrayList<>();
        adapterFoodList = new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, stringFoods);
        setHasOptionsMenu(true);

        final ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        networkInfoReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
                if (stringFoods.size() == 0) {
                    if (null == activeNetworkInfo) showAlertForNetworkConnectivity();
                    else insertFood();
                }
            }
        };
        getContext().registerReceiver(networkInfoReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(networkInfoReceiver);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        billFoodFragmentView = inflater.inflate(R.layout.fragment_create_bill_food, container, false);
        RecyclerView recyclerView = (RecyclerView) billFoodFragmentView.findViewById(R.id.bill_food_recycler);

        foodViewModel.getBillFoodsWithPerson(this.idBill).observe(this, new Observer<List<FoodWithPerson>>() {
            @Override
            public void onChanged(List<FoodWithPerson> foodWithPersonList) {
                if (foodWithPersonList.size() > 0) {
                    alert_blank_table.setVisibility(View.GONE);
                } else {
                    alert_blank_table.setVisibility(View.VISIBLE);
                }
                bIllFoodAdapter.setData(foodWithPersonList);
            }
        });

        bIllFoodAdapter = new BillFoodAdapter(getActivity());
        bIllFoodAdapter.setItemClickListener(onRowClicked());

        if (bIllFoodAdapter.getItemCount()>1) addExistingPersonNameToList(bIllFoodAdapter.getAllData());

        alert_blank_table = billFoodFragmentView.findViewById(R.id.alert_dont_have_food);

        FloatingActionButton fab = billFoodFragmentView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeDialogCreateFood(billFoodFragmentView);
            }
        });

        recyclerView.setAdapter(bIllFoodAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return billFoodFragmentView;
    }

    private void makeDialogCreateFood(View view) {

        getContactsPermission();

        final Dialog dialog_create_food = new Dialog(view.getContext());
        dialog_create_food.setContentView(R.layout.fragment_create_food);
        dialog_create_food.setCanceledOnTouchOutside(false);

        final CardView dialog_box = dialog_create_food.findViewById(R.id.card_create_food);

        final AutoCompleteTextView foodName = dialog_box.findViewById(R.id.food_name);
        final AutoCompleteTextView foodPerson = dialog_box.findViewById(R.id.food_person);
        final EditText foodPrice = dialog_box.findViewById(R.id.food_price);

        ArrayAdapter<String> adapterPersonName = new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, stringPersonNameList);
        foodPerson.setThreshold(1); //will start working from first character
        foodPerson.setAdapter(adapterPersonName);
        foodName.setThreshold(1);
        foodName.setAdapter(adapterFoodList);

        Button save = dialog_box.findViewById(R.id.button_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean valid = dialogInputValidation(foodName.getText().toString(), foodPrice.getText().toString(), foodPerson.getText().toString());

                if (valid == true) {
                    String personName = foodPerson.getText().toString();
                    int idPerson = 0;
                    Person checkPerson = personViewModel.getPersonFromBill(idBill, personName);
                    if (checkPerson == null) {
                        Person person = new Person();
                        person.setName(personName);
                        person.setIdBill(idBill);
                        idPerson = personViewModel.insert(person);
                        if (!stringPersonNameList.contains(personName)) stringPersonNameList.add(personName);
                    } else {
                        idPerson = checkPerson.getId();
                    }
                    Food food = new Food();
                    food.setName(foodName.getText().toString());
                    food.setPrice(Integer.valueOf(foodPrice.getText().toString()));
                    food.setIdBill(idBill);
                    food.setIdPerson(idPerson);
                    foodViewModel.insert(food);
                    dialog_create_food.dismiss();
                } else {
                    Toast.makeText(getContext(), getContext().getString(R.string.alert_not_complete), Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button cancel = dialog_box.findViewById(R.id.button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_create_food.dismiss();
            }
        });

        dialog_create_food.show();
    }

    public View.OnClickListener onRowClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                makeDialogEditFood(billFoodFragmentView, viewHolder.getAdapterPosition());
            }
        };
    }

    private void addExistingPersonNameToList(List<FoodWithPerson> foodWithPersonList) {
        for (FoodWithPerson foodWithPerson : foodWithPersonList) {
            if (!stringPersonNameList.contains(foodWithPerson.person.getName())) stringPersonNameList.add(foodWithPerson.person.getName());
        }
    }

    private void makeDialogEditFood(View view, int viewHolderPosition) {
        getContactsPermission();

        final FoodWithPerson itemSelected = bIllFoodAdapter.getData(viewHolderPosition);

        final Dialog dialog_edit_food = new Dialog(view.getContext());
        dialog_edit_food.setContentView(R.layout.fragment_edit_food);
        dialog_edit_food.setCanceledOnTouchOutside(false);

        final CardView dialog_box = dialog_edit_food.findViewById(R.id.card_create_food);

        final AutoCompleteTextView foodName = dialog_box.findViewById(R.id.food_name);
        final AutoCompleteTextView foodPerson = dialog_box.findViewById(R.id.food_person);
        final EditText foodPrice = dialog_box.findViewById(R.id.food_price);

        ArrayAdapter<String> adapterPersonName = new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, stringPersonNameList);
        foodPerson.setThreshold(1); //will start working from first character
        foodPerson.setAdapter(adapterPersonName);
        foodPerson.setText(itemSelected.person.getName());
        foodName.setThreshold(1);
        foodName.setAdapter(adapterFoodList);
        foodName.setText(itemSelected.food.getName());
        foodPrice.setText(Integer.toString(itemSelected.food.getPrice()));

        Button save = dialog_box.findViewById(R.id.button_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean valid = dialogInputValidation(foodName.getText().toString(), foodPrice.getText().toString(), foodPerson.getText().toString());

                if (valid == true) {
                    String personName = foodPerson.getText().toString();
                    int idPerson = 0;
                    Person checkPerson = personViewModel.getPersonFromBill(idBill, personName);
                    if (checkPerson == null) {
                        Person person = new Person();
                        person.setName(personName);
                        person.setIdBill(idBill);
                        idPerson = personViewModel.insert(person);
                        if (!stringPersonNameList.contains(personName)) stringPersonNameList.add(personName);
                        PersonWithFoods personWithFoods = personViewModel.getPersonWithFoods(idBill, idPerson);
                        if (personWithFoods.foods.size() == 0) personViewModel.delete(personWithFoods.person);
                    } else {
                        idPerson = checkPerson.getId();
                    }
                    Food food = itemSelected.food;
                    String newName = foodName.getText().toString();
                    int newPrice = Integer.valueOf(foodPrice.getText().toString());
                    if (!food.getName().equals(newName)) food.setName(newName);
                    if (food.getPrice() != newPrice) food.setPrice(newPrice);
                    food.setIdPerson(idPerson);
                    foodViewModel.update(food);
                    dialog_edit_food.dismiss();
                }
                else {
                    Toast.makeText(getContext(), getContext().getString(R.string.alert_not_complete), Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button cancel = dialog_box.findViewById(R.id.button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_edit_food.dismiss();
            }
        });

        Button delete = dialog_box.findViewById(R.id.button_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodViewModel.delete(itemSelected.food);
                dialog_edit_food.dismiss();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("size", String.valueOf(personViewModel.getPersonWithFoods(itemSelected.person.getIdBill(), itemSelected.person.getId()).foods.size()));
                        if (personViewModel.getPersonWithFoods(itemSelected.person.getIdBill(), itemSelected.person.getId()).foods.size() == 0)
                            personViewModel.delete(itemSelected.person);
                    }
                }, 250);
            }
        });

        dialog_edit_food.show();
    }

    public boolean dialogInputValidation(String foodName, String price, String foodPerson) {
        if (foodName.equals("") || price.equals("") || foodPerson.equals(""))
            return false;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save_item) { ;
            Intent intent = new Intent(getActivity(), BIllDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("idBill", idBill);
            intent.putExtras(bundle);
            startActivity(intent);
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getContactsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            readContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                readContacts();
            } else {
                this.showRejectedContactsPermissionAlert();
            }
        }
    }

    public void readContacts() {
        Cursor cur = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                // Get contact name (displayName)
                String displayName = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (!stringPersonNameList.contains(displayName)) stringPersonNameList.add(displayName);
            }
        }
        cur.close();
    }

    public void insertFood() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                String url = "https://tktpl-req.herokuapp.com/listFood";
                RequestQueue rq = Volley.newRequestQueue(getActivity());
                rq.start();
                JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, (JSONObject) null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse JSON
                            JSONArray jsonArray = response.getJSONArray("data");
                            Log.i("json", Integer.toString(jsonArray.length()));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                stringFoods.add(jsonArray.getString(i));
                                Log.i("json", Integer.toString(stringFoods.size()));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle rerror
                    }
                });
                jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                rq.add(jsonArrayRequest);
                return null;
            }
        }.execute();
    }

    public void showRejectedContactsPermissionAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.message_alert_rejected_contacts_permission)
                .setCancelable(false)
                .setPositiveButton(R.string.allow_permission, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getContactsPermission();
                    }
                })
                .setNegativeButton(R.string.deny_permission, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle(R.string.title_alert_rejected_contacts_permission);
        alert.show();
    }

    public void showAlertForNetworkConnectivity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.message_alert_network_connectivity)
                .setCancelable(false)
                .setPositiveButton(R.string.activate_wifi, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        dialog.cancel();
                    }
                })
                .setNeutralButton(R.string.activate_mobile, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle(R.string.title_alert_network_connectivity);
        alert.show();
    }
}