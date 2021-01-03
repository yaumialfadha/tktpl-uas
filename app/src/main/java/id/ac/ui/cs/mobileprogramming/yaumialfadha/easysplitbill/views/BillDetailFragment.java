package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.views;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.R;

import java.util.Calendar;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.CreateOrEditBillActivity;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.BillWithPerson;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.receiver.AlarmReceiver;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.viewmodel.BillViewModel;

public class BillDetailFragment extends Fragment {
    private View billDetailView;
    private BillWithPersonAdapter billWithPersonAdapter = null;
    private BillViewModel billViewModel;
    private int idBill;
    private RecyclerView recyclerView;

    static {
        System.loadLibrary("native-lib");
    }

    public BillDetailFragment(int idBill) {
        this.idBill = idBill;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        billViewModel = ViewModelProviders.of(this).get(BillViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        billDetailView = inflater.inflate(R.layout.fragment_bill_detail, container, false);
        recyclerView = (RecyclerView) billDetailView.findViewById(R.id.recycler_bill_detail);
        TextView dueDate = billDetailView.findViewById(R.id.bill_due_date);
        TextView totalPrice = billDetailView.findViewById(R.id.bill_total_price);

        BillWithPerson billWithPerson = billViewModel.getBillWithPersonDetail(idBill);

        getActivity().setTitle(getResources().getString(R.string.bill_detail) + ": " + billWithPerson.bill.getName());

        billWithPersonAdapter = new BillWithPersonAdapter(getActivity(), billWithPerson.bill);
        billWithPersonAdapter.setData(billWithPerson.personWithFoods);
        if (billWithPerson.personWithFoods.size() > 0) {
            billDetailView.findViewById(R.id.alert_bill_detail_is_empty).setVisibility(View.GONE);
        }
        recyclerView.setAdapter(billWithPersonAdapter);
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(billWithPerson.bill.getDueDate());
        String date = (cal.get(Calendar.DATE) < 10) ? "0" + Integer.toString(cal.get(Calendar.DATE)) : Integer.toString(cal.get(Calendar.DATE));
        String month = (cal.get(Calendar.MONTH)+1 < 10) ? "0" + Integer.toString(cal.get(Calendar.MONTH)+1) : Integer.toString(cal.get(Calendar.MONTH)+1);
        dueDate.setText(getActivity().getString(R.string.due_on) + " " + date + "/" + month + "/" + (cal.get(Calendar.YEAR)));
        totalPrice.setText(getResources().getString(R.string.total_price) + " " + rupiah(Integer.toString(billWithPerson.getTotalPrice()).toCharArray()));

        return billDetailView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit_bill_detail) {
            Intent intent = new Intent(getActivity(), CreateOrEditBillActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("idBill", idBill);
            intent.putExtras(bundle);
            startActivity(intent);
            getActivity().finish();
            return true;
        } else if (id == R.id.action_edit_bill_foods) {
            Intent intent = new Intent(getActivity(), CreateOrEditBillActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("editFoods", true);
            bundle.putInt("idBill", idBill);
            intent.putExtras(bundle);
            startActivity(intent);
            getActivity().finish();
            return true;
        } else if (id == R.id.action_delete_bill) {
            billViewModel.delete(billViewModel.getBillWithPersonDetail(idBill).bill);
            AlarmManager manager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(getContext(), AlarmReceiver.class);
            alarmIntent.setAction(String.valueOf(idBill));
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            pendingIntent.cancel();
            manager.cancel(pendingIntent);
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration conf) {
        super.onConfigurationChanged(conf);
        if (conf.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    public native String rupiah(char[] s);
}
