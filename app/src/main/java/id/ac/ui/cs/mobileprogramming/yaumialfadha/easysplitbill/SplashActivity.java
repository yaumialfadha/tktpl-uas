package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.opengl.OpenGLView;

public class SplashActivity extends AppCompatActivity {

    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new OpenGLView(this));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(activity, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
