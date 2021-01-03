package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.R;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.views.BillDueFragment;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.views.BillRecentFragment;


public class AllRecentOrDueBillActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_bill_recent_or_due);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Fragment fragment = null;
        if (getIntent().getExtras().getString("from").equals("recent")) {
            fragment = BillRecentFragment.newInstance(false);
            setTitle(R.string.recent_bills);
        }
        else {
            fragment = BillDueFragment.newInstance(false);
            setTitle(R.string.due_bills);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container_first,fragment).commit();
    }
}
