package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.R;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.views.BillDetailFragment;


public class BIllDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();

        setContentView(R.layout.activity_bill_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_bill_detail,new BillDetailFragment(bundle.getInt("idBill"))).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }
}
