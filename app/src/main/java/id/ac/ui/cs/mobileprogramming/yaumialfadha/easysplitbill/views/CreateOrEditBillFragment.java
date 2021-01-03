package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.views;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.R;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.converter.DateConverter;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.Bill;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.BillWithPerson;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.receiver.AlarmReceiver;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.utils.Constant;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.viewmodel.BillViewModel;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.worker.BackgroundNotificationWorker;

public class CreateOrEditBillFragment extends Fragment {

    private View createBillView;
    private BillViewModel billViewModel;
    final Calendar myCalendar = Calendar.getInstance();
    private EditText dueDate, billName, billTax, billServiceFee;
    private String existingBillName, existingBillTax, existingBillServiceFee, existingBillDueDate;

    public static CreateOrEditBillFragment newInstance(int idBill) {
        CreateOrEditBillFragment cb = new CreateOrEditBillFragment();
        Bundle args = new Bundle();
        args.putInt("idBill", idBill);
        cb.setArguments(args);
        return cb;
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
        createBillView = inflater.inflate(R.layout.fragment_create_bill, container, false);

        billName = (EditText) createBillView.findViewById(R.id.txt_bill_name);
        billTax = (EditText) createBillView.findViewById(R.id.txt_bill_tax);
        billServiceFee = (EditText) createBillView.findViewById(R.id.txt_bill_service_fee);
        dueDate = (EditText) createBillView.findViewById(R.id.txt_due_date);

        billTax.setText("0");
        billServiceFee.setText("0");

        if (getArguments() != null && getArguments().get("idBill") != null) {
            Bill bill = billViewModel.getBillWithPersonDetail(getArguments().getInt("idBill")).bill;
            billName.setText(bill.getName());
            billTax.setText(Integer.toString(bill.getTax()));
            billServiceFee.setText(Integer.toString(bill.getServiceFee()));
            getActivity().setTitle(getActivity().getResources().getString(R.string.action_edit_bill));

            Calendar cal = Calendar.getInstance();
            cal.setTime(bill.getDueDate());
            String date = (cal.get(Calendar.DATE) < 10) ? "0" + Integer.toString(cal.get(Calendar.DATE)) : Integer.toString(cal.get(Calendar.DATE));
            String month = (cal.get(Calendar.MONTH)+1 < 10) ? "0" + Integer.toString(cal.get(Calendar.MONTH)+1) : Integer.toString(cal.get(Calendar.MONTH)+1);
            dueDate.setText(date + "/" + month + "/" + (cal.get(Calendar.YEAR)));
        } else {
            getActivity().setTitle(getActivity().getResources().getString(R.string.create_bill));
        }

        dueDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if (focused) {
                    new DatePickerDialog(getActivity(), getDatePickerDialog(), myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });
        dueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), getDatePickerDialog(), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        return createBillView;
    }

    public DatePickerDialog.OnDateSetListener getDatePickerDialog() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDueDateLabel();
            }
        };
        return date;
    }

    private void updateDueDateLabel() {
        String myFormat = "dd/MM/YYYY";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        dueDate.setText(sdf.format(myCalendar.getTime()));
    }

    private boolean dataIsComplete() {
        boolean res = true;
        String billName = this.billName.getText().toString();
        String tax = this.billTax.getText().toString();
        String serviceFee = this.billServiceFee.getText().toString();
        String dueDate = this.dueDate.getText().toString();

        if (billName.matches("") || tax.matches("") || serviceFee.matches("") || dueDate.matches(""))
            res = false;
        return res;
    }

    public Date convertDate(String date) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date fd;
        Date dateConverted = null;
        try {
            fd = formatter.parse(date);
            dateConverted = DateConverter.toDate(fd.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateConverted;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save_item) {
            if (dataIsComplete()) {
                String billName = this.billName.getText().toString();
                int tax = Integer.valueOf(this.billTax.getText().toString());
                int serviceFee = Integer.valueOf(this.billServiceFee.getText().toString());
                Date dueDate = convertDate(this.dueDate.getText().toString());
                Bill bill = null;
                if (getArguments() == null) {
                    bill = new Bill();
                } else {
                    bill = billViewModel.getBillWithPersonDetail(getArguments().getInt("idBill")).bill;
                }
                bill.setName(billName);
                bill.setTax(tax);
                bill.setServiceFee(serviceFee);
                bill.setDueDate(dueDate);
                int idBill = 0;
                if (getArguments() == null) {
                    idBill = billViewModel.insert(bill);
                    if (idBill == 1) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(calendar.getTimeInMillis() + Constant.ONE_DAY_TIME_IN_MILLIS); // diset biar jadi besok
                        calendar.set(Calendar.HOUR_OF_DAY, 6); // diset biar bisa waktunya pagi
                        calendar.set(Calendar.MINUTE, 1);
                        calendar.set(Calendar.SECOND, 1);
                        triggerFirstTimeWorker(calendar.getTimeInMillis());
                    }
                } else {
                  idBill =  getArguments().getInt("idBill");
                  billViewModel.update(bill);
                }
//                myCalendar.setTimeInMillis(dueDate.getTime());
//                myCalendar.set(Calendar.HOUR_OF_DAY, 15);
//                myCalendar.set(Calendar.MINUTE, 4);
//                myCalendar.set(Calendar.SECOND, 1);
//                boolean bool = myCalendar.getTimeInMillis() >= System.currentTimeMillis() ?  true : false;
//                AlarmManager manager = (AlarmManager) getActivity().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//                if (bool == true) {
//                    Intent alarmIntent = new Intent(getActivity().getApplicationContext(), AlarmReceiver.class);
//                    alarmIntent.setAction(String.valueOf(idBill));
//                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                    manager.set(AlarmManager.RTC_WAKEUP,myCalendar.getTimeInMillis(),pendingIntent);
//                } else {
//                    Intent alarmIntent = new Intent(getActivity().getApplicationContext(), AlarmReceiver.class);
//                    alarmIntent.setAction(String.valueOf(idBill));
//                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                    pendingIntent.cancel();
//                    manager.cancel(pendingIntent);
//                }
                if (getArguments() != null) getActivity().finish();
                else getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_fragment_create_bill, new CreateOrEditBillFoodsFragment(idBill))
                        .commit();
            } else {
                Toast.makeText(getActivity(), R.string.alert_not_complete, Toast.LENGTH_LONG).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void triggerFirstTimeWorker(long tomorrowMillis) {
        Calendar calendar = Calendar.getInstance();
        long nowMillis = calendar.getTimeInMillis();
        long diff = tomorrowMillis - nowMillis;

        boolean[] firstWorker = {true};

        Data data = new Data.Builder()
                .putBooleanArray("firstWorker", firstWorker)
                .putLong("diffTomorrowMIllis", diff)
                .build();

        WorkManager mWorkManager = WorkManager.getInstance(getContext());
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build();

        OneTimeWorkRequest mRequest = new OneTimeWorkRequest.Builder(BackgroundNotificationWorker.class)
                .setConstraints(constraints)
                .setInputData(data)
                .addTag("backgroundNotification")
                .build();
        mWorkManager.enqueue(mRequest);
    }
}
