package io.mikankenshi.memo;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MemoActivity extends AppCompatActivity {
    static final int num = 3;
    EditText[] titleEditText = new EditText[num];
    EditText[] contentEditText = new EditText[num];
    TextView result;
    String text;


    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        result = (TextView) findViewById(R.id.result);
        for (int i = 0; i < num; i++) {
            titleEditText[i] = (EditText) findViewById(R.id.title + i);
            contentEditText[i] = (EditText) findViewById(R.id.content + i);
        }


        pref = getSharedPreferences("pref_memo", MODE_PRIVATE); //DBにしたい
        //SharedPreferencesに保存されているものを出力
        /*for (int i = 0; i < num; i++) {
            titleEditText[i].setText(pref.getString("key_title" + i, ""));
            contentEditText[i].setText(pref.getString("key_content" + i, ""));
        }*/

        titleEditText[0].setText(pref.getString("key_title0", ""));
        titleEditText[1].setText(pref.getString("key_title1", ""));
        titleEditText[2].setText(pref.getString("key_title2", ""));
        contentEditText[0].setText(pref.getString("key_content0", ""));
        contentEditText[1].setText(pref.getString("key_content1", ""));
        contentEditText[2].setText(pref.getString("key_content2", ""));

        result.setText(pref.getString("key_result", ""));

        for (int i = 0; i < num; i++) {
            titleEditText[i].setInputType(InputType.TYPE_CLASS_DATETIME); //日時のみ入力可能
            contentEditText[i].setInputType(InputType.TYPE_CLASS_DATETIME);
        }
    }

    //SharedPreferencesにEditTextの内容を保存
    public void save(View v) {
        String[] titleText = new String[num];
        String[] contentText = new String[num];

        SharedPreferences.Editor editor = pref.edit();

        for (int i = 0; i < num; i++) {
            titleText[i] = titleEditText[i].getText().toString();
            contentText[i] = contentEditText[i].getText().toString();
        }
        editor.putString("key_title0", titleText[0]);
        editor.putString("key_title1", titleText[1]);
        editor.putString("key_title2", titleText[2]);
        editor.putString("key_content0", contentText[0]);
        editor.putString("key_content1", contentText[1]);
        editor.putString("key_content2", contentText[2]);

        editor.putString("key_result", text);
        editor.commit();

        finish();
    }

    public void clear(View v) {
        for (int i = 0; i < num; i++) {
            titleEditText[i].getEditableText().clear();  //get...でedittextの中身取り出せる
            contentEditText[i].getEditableText().clear();
        }
        result.setText("");
    }

    public void cal(View v) {
        String[] a = new String[num];
        String[] b = new String[num];
        int[][] timec = new int[num][2];

        int hour, minute;
        hour = 0;
        minute = 0;

        for (int i = 0; i < num; i++) {
            a[i] = titleEditText[i].getText().toString(); //toStringしなかったら型は何？ Editable型:変更可能な文字列
            b[i] = contentEditText[i].getText().toString();
           int hoge =   Integer.parseInt(a[i]);

            if (b[i].length() == 0) {
                Toast toast = Toast.makeText(MemoActivity.this, "内容を入力してね！", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                    timec[i] = getTime(a[i], b[i]);
                    //Log.d("timec","timec[0][i]="+timec[0][i] );//timec[][0]:hour  timec[][1]:minute  timec[i]:列 左が列、右が行
                    hour = hour + timec[i][0];
                    minute = minute + timec[i][1];
                if (minute >= 60) {
                    minute = minute - 60;
                    hour = hour + 1;
                }
                text = String.valueOf(String.format("%02d", hour)) + ":" + String.valueOf(String.format("%02d", minute));
                result.setText(text);//more beautiful
            }
        }
    }

    public int[] getTime(String str1, String str2) {
        int[] time11 = new int[2];
        int[] time22 = new int[2];

        String[] time1 = str1.split(":", 0); // string
        String[] time2 = str2.split(":", 0); // string

        for (int i = 0; i < 2; i++) {
            time11[i] = Integer.parseInt(time1[i]); //oo:oo-> oo  oo (11[0] = hour,,11[1] = minute)
            time22[i] = Integer.parseInt(time2[i]);
        }
        int[] timec = new int[2];

        timec[0] = time22[0] - time11[0]; //hour
        timec[1] = time22[1] - time11[1]; //minute
        if (timec[1] < 0) {
            timec[0]--;
            timec[1] = timec[1] + 60;
        }

        return timec;
}

}
//まずdaily
//複数データの時の動き
//edittextが空の時など例外処理
//oo:ooの形で入力されないとき→入力制限を設ける
//auto save??
//textviewも保存したい    done!
//