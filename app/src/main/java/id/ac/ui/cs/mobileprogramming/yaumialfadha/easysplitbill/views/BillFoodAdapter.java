package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.R;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.FoodWithPerson;
import java.util.List;

public class BillFoodAdapter extends RecyclerView.Adapter<BillFoodAdapter.BillFoodViewHolder> {
    private LayoutInflater layoutInflater;
    private List<FoodWithPerson> foodWithPersonList;
    private Context context;

    public BillFoodAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public BillFoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_food, parent, false);
        return new BillFoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BillFoodViewHolder holder, int position) {
        int rowPos = holder.getAdapterPosition();
        if (rowPos == 0) {
            holder.txtFoodName.setText(context.getResources().getString(R.string.food_name));
            holder.txtFoodPerson.setText(context.getResources().getString(R.string.person));
            holder.txtFoodPrice.setText(context.getResources().getString(R.string.price));
            holder.txtFoodName.setBackgroundResource(R.drawable.table_header_cell_bg);
            holder.txtFoodPerson.setBackgroundResource(R.drawable.table_header_cell_bg);
            holder.txtFoodPrice.setBackgroundResource(R.drawable.table_header_cell_bg);
        } else {
            holder.txtFoodName.setText(foodWithPersonList.get(rowPos-1).food.getName());
            holder.txtFoodPerson.setText(foodWithPersonList.get(rowPos-1).person.getName());
            holder.txtFoodPrice.setText(Integer.toString(foodWithPersonList.get(rowPos-1).food.getPrice()));
        }
    }

    @Override
    public int getItemCount() {
        return (foodWithPersonList != null) ? foodWithPersonList.size()+1 : 0+1;
    }

    void setData(List<FoodWithPerson> data) {
        this.foodWithPersonList = data;
        notifyDataSetChanged();
    }

    public FoodWithPerson getData(int position) {
        return foodWithPersonList.get(position-1);
    }

    private View.OnClickListener onItemClickListener;

    public void setItemClickListener(View.OnClickListener clickListener) {
        onItemClickListener = clickListener;
    }

    public List<FoodWithPerson> getAllData() {
        return this.foodWithPersonList;
    }

    class BillFoodViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtFoodPerson, txtFoodName, txtFoodPrice;

        public BillFoodViewHolder(View itemView) {
            super(itemView);
            txtFoodPerson = (TextView) itemView.findViewById(R.id.txt_food_person);
            txtFoodName = (TextView) itemView.findViewById(R.id.txt_food_name);
            txtFoodPrice = (TextView) itemView.findViewById(R.id.txt_food_price);
            itemView.setTag(this);
            itemView.setOnClickListener(onItemClickListener);
        }
    }
}
