package com.example.emery.andfixframwork;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

     public void crash(View  v){
         Caculator caculator=new Caculator();
        int  caculate=caculator.caculate();
         Toast.makeText(this,"result="+caculate,Toast.LENGTH_SHORT).show();

     }
     public void fix(View view){
        FixManager.getInstance(this).loadFile(new File("/mnt/sdcard/fix.dex"));

     }
}
