package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.R;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.views.CreateOrEditBillFoodsFragment;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.views.CreateOrEditBillFragment;


public class CreateOrEditBillActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bill);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();
        Fragment fragment = new CreateOrEditBillFragment();
        if (bundle != null && bundle.get("editFoods") != null) {
            fragment = new CreateOrEditBillFoodsFragment(bundle.getInt("idBill"));
        } else if (bundle != null && bundle.get("idBill") != null) {
            fragment = CreateOrEditBillFragment.newInstance(bundle.getInt("idBill"));
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment_create_bill, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //this is only needed if you have specific things
        //that you want to do when the user presses the back button.
        /* your specific things...*/
        super.onBackPressed();
    }
}
