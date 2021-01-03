package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.R;
import java.util.List;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.Bill;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.Food;

public class PersonWithFoodsAdapter extends RecyclerView.Adapter<PersonWithFoodsAdapter.PersonWithFoodsViewHolder> {
    private LayoutInflater layoutInflater;
    private List<Food> foodList;
    private Bill bill;
    private Context context;

    static {
        System.loadLibrary("native-lib");
    }

    public PersonWithFoodsAdapter(Context context, Bill bill) {
        layoutInflater = LayoutInflater.from(context);
        this.bill = bill;
        this.context = context;
    }

    @Override
    public PersonWithFoodsAdapter.PersonWithFoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_bill_person_foods, parent, false);
        if (bill.getServiceFee() == 0) view.findViewById(R.id.txt_food_service_fee).setVisibility(View.GONE);
        return new PersonWithFoodsAdapter.PersonWithFoodsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PersonWithFoodsAdapter.PersonWithFoodsViewHolder holder, int position) {
        int rowPos = holder.getAdapterPosition();
        if (rowPos == 0) {
            holder.txtFoodName.setText(context.getResources().getString(R.string.food_name));
            holder.txtFoodPrice.setText(context.getResources().getString(R.string.price));
            holder.txtFoodTax.setText(context.getResources().getString(R.string.tax));
            if (bill.getServiceFee() != 0) holder.txtFoodServiceFee.setText(context.getResources().getString(R.string.service));
        } else {
            int price = foodList.get(rowPos - 1).getPrice();
            int tax = bill.getTax();
            int serviceFee = bill.getServiceFee();
            int foodTax = price * tax / 100;
            int foodServiceFee = price * serviceFee / 100;
            holder.txtFoodName.setText(foodList.get(rowPos - 1).getName());
            holder.txtFoodPrice.setText(rupiah(Integer.toString(price).toCharArray()));
            holder.txtFoodTax.setText(rupiah(Integer.toString(foodTax).toCharArray()));
            if (bill.getServiceFee() != 0) holder.txtFoodServiceFee.setText(rupiah(Integer.toString(foodServiceFee).toCharArray()));
        }
    }

    @Override
    public int getItemCount() {
        return (foodList != null) ? foodList.size() + 1 : 0 + 1;
    }

    void setData(List<Food> data) {
        this.foodList = data;
        notifyDataSetChanged();
    }

    class PersonWithFoodsViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtFoodName, txtFoodPrice, txtFoodTax, txtFoodServiceFee;

        public PersonWithFoodsViewHolder(View itemView) {
            super(itemView);
            txtFoodName = (TextView) itemView.findViewById(R.id.txt_food_name);
            txtFoodPrice = (TextView) itemView.findViewById(R.id.txt_food_price);
            txtFoodTax = (TextView) itemView.findViewById(R.id.txt_food_tax);
            txtFoodServiceFee = (TextView) itemView.findViewById(R.id.txt_food_service_fee);
        }
    }

    public native String rupiah(char[] s);
}
