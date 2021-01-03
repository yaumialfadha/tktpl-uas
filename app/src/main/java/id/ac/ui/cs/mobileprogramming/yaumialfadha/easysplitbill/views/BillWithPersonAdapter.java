package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.R;
import java.util.List;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.Bill;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.PersonWithFoods;

public class BillWithPersonAdapter extends RecyclerView.Adapter<BillWithPersonAdapter.BillWithPersonViewHolder> {
    private LayoutInflater layoutInflater;
    private List<PersonWithFoods> billWithPersonList;
    private Context context;
    private Bill bill;

    static {
        System.loadLibrary("native-lib");
    }

    public BillWithPersonAdapter(Context context, Bill bill) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.bill = bill;
    }

    @Override
    public BillWithPersonAdapter.BillWithPersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_detail_bill, parent, false);
        return new BillWithPersonAdapter.BillWithPersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BillWithPersonAdapter.BillWithPersonViewHolder holder, int position) {
        holder.txtPersonName.setText(billWithPersonList.get(position).person.getName());
        holder.txtPersonTotal.setText(rupiah(Integer.toString(billWithPersonList.get(position).getTotalFoodPrice(bill.getTax(), bill.getServiceFee())).toCharArray()));

        PersonWithFoodsAdapter personWithFoodsAdapter = new PersonWithFoodsAdapter(context, bill);
        personWithFoodsAdapter.setData(billWithPersonList.get(position).foods);
        holder.recyclerView.setAdapter(personWithFoodsAdapter);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public int getItemCount() {
        return (billWithPersonList != null) ? billWithPersonList.size(): 0;
    }

    void setData(List<PersonWithFoods> data) {
        this.billWithPersonList = data;
        notifyDataSetChanged();
    }

    class BillWithPersonViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtPersonName, txtPersonTotal;
        protected RecyclerView recyclerView;

        public BillWithPersonViewHolder(View itemView) {
            super(itemView);
            txtPersonName = (TextView) itemView.findViewById(R.id.txt_person_name);
            txtPersonTotal = (TextView) itemView.findViewById(R.id.txt_person_total_out);
            recyclerView = itemView.findViewById(R.id.recycler_detail_person);
        }
    }

    public native String rupiah(char[] s);
}
