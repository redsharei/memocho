package io.mikankenshi.memo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void memo(View v){
        //メモ画面に遷移
        Intent intent = new Intent(this, MemoActivity.class);
        startActivity(intent);
    }
}
