package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.R;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.view.View;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.views.BillDueFragment;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.views.BillRecentFragment;

public class HomeActivity extends AppCompatActivity {

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Fragment billRecentFragment = BillRecentFragment.newInstance(true);
        Fragment billDueFragment = BillDueFragment.newInstance(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_first,billRecentFragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_second,billDueFragment).commit();

        activity = this;

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CreateOrEditBillActivity.class);
                startActivity(intent);
            }
        });
    }
}
