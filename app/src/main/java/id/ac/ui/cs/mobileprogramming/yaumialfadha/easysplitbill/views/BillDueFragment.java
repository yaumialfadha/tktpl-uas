package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.views;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.AllRecentOrDueBillActivity;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.BIllDetailActivity;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.BillWithPerson;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.viewmodel.BillViewModel;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.R;

import java.util.List;

public class BillDueFragment extends Fragment {
    private View billDueView;
    private BillHomeAdapter billAdapter = null;
    private BillViewModel billViewModel;
    private TextView more;
    private TextView alert;
    private boolean fromHome;
    private RecyclerView recyclerView;

    public static Fragment newInstance(boolean fromHome) {
        Bundle args = new Bundle();
        BillDueFragment fragment = new BillDueFragment();
        args.putBoolean("fromHome", fromHome);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        billViewModel = ViewModelProviders.of(this).get(BillViewModel.class);
        this.fromHome = getArguments().getBoolean("fromHome");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        billDueView = inflater.inflate(R.layout.fragment_bill_due, container, false);
        recyclerView = (RecyclerView) billDueView.findViewById(R.id.recycler_recent_bill);
        more = billDueView.findViewById(R.id.more_due);
        alert = billDueView.findViewById(R.id.alert_due_bill_not_found);

        billViewModel.getDueBills().observe(this, new Observer<List<BillWithPerson>>() {
            @Override
            public void onChanged(List<BillWithPerson> billWithPeople) {
                billAdapter.setData(billWithPeople);
                if (billWithPeople.size() <= 2 || fromHome == false) {
                    more.setVisibility(View.GONE);
                }
                if (billWithPeople.size() > 0) {
                    alert.setVisibility(View.GONE);
                }
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AllRecentOrDueBillActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("from", "due");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        billAdapter = new BillHomeAdapter(getActivity(), fromHome);
        billAdapter.setItemClickListener(onRowClicked());
        recyclerView.setAdapter(billAdapter);
        if (fromHome == false && getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        return billDueView;
    }

    public View.OnClickListener onRowClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                int idBill = billAdapter.getData(viewHolder.getAdapterPosition()).bill.getId();
                Intent intent = new Intent(getActivity(), BIllDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("idBill", idBill);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
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
}
