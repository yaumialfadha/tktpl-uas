package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.views;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.BillWithPerson;

public class BillHomeAdapter extends RecyclerView.Adapter<BillHomeAdapter.BillRecentViewHolder> {
    private LayoutInflater layoutInflater;
    private List<BillWithPerson> billList;
    private boolean fromHome;

    static {
        System.loadLibrary("native-lib");
    }

    public BillHomeAdapter(Context context, boolean fromHome) {
        layoutInflater = LayoutInflater.from(context);
        this.fromHome = fromHome;
    }

    @Override
    public BillRecentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_bill, parent, false);
        return new BillRecentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BillRecentViewHolder holder, int position) {
//        int totalPrice = 0;
//        for (int i = 0; i < billList.get(position).personWithFoods.size(); i++) {
//            billList.get(position)
//        }
        if (billList.size() != 0) {
            Log.i("pos", Integer.toString(position));
            holder.txtBillName.setText(billList.get(position).bill.getName());
            holder.txtBillPrice.setText(rupiah(Integer.toString(billList.get(position).getTotalPrice()).toCharArray()));
//        holder.txtBillPrice.setText("0");
            Date dueDate = billList.get(position).bill.getDueDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(dueDate);
            String date = (cal.get(Calendar.DATE) < 10) ? "0" + Integer.toString(cal.get(Calendar.DATE)) : Integer.toString(cal.get(Calendar.DATE));
            String month = (cal.get(Calendar.MONTH)+1 < 10) ? "0" + Integer.toString(cal.get(Calendar.MONTH)+1) : Integer.toString(cal.get(Calendar.MONTH)+1);
            holder.txtDueDate.setText(date + "/" + month + "/" + (cal.get(Calendar.YEAR)));
        }
    }

    @Override
    public int getItemCount() {
        int res = (billList != null) ? ((!fromHome) ? billList.size() : (billList.size()<=1) ? billList.size() : 2) : 0;
        return res;
    }

    void setData(List<BillWithPerson> recentBill){
        this.billList = recentBill;
        notifyDataSetChanged();
    }

    public BillWithPerson getData(int position) {
        return this.billList.get(position);
    }

    private View.OnClickListener onItemClickListener;

    public void setItemClickListener(View.OnClickListener clickListener) {
        onItemClickListener = clickListener;
    }

    class BillRecentViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtBillName, txtBillPrice, txtDueDate;

        public BillRecentViewHolder (View itemView) {
            super(itemView);
            txtBillName = (TextView) itemView.findViewById(R.id.txt_bill_name);
            txtBillPrice = (TextView) itemView.findViewById(R.id.txt_bill_price);
            txtDueDate = (TextView) itemView.findViewById(R.id.txt_due_date);
            itemView.setTag(this);
            itemView.setOnClickListener(onItemClickListener);
        }
    }

    public native String rupiah(char[] s);
}
