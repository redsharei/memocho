package io.mikankenshi.memo;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MemoActivity extends AppCompatActivity {
    EditText titleEditText,titleEditText2;
    EditText contentEditText,contentEditText2;
    TextView result;
    String text;

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        result = (TextView) findViewById(R.id.result);
        titleEditText = (EditText) findViewById(R.id.title);
        contentEditText = (EditText) findViewById(R.id.content);
        titleEditText2 = (EditText) findViewById(R.id.title2);
        contentEditText2 = (EditText) findViewById(R.id.content2);


        pref = getSharedPreferences("pref_memo", MODE_PRIVATE); //DB
        //SharedPreferencesに保存されているものを出力
        titleEditText.setText(pref.getString("key_title", ""));
        contentEditText.setText(pref.getString("key_content", ""));
        titleEditText2.setText(pref.getString("key_title2", ""));
        contentEditText2.setText(pref.getString("key_content2", ""));
        result.setText(pref.getString("key_result",""));

        titleEditText.setInputType(InputType.TYPE_CLASS_DATETIME); //日時のみ入力可能
        contentEditText.setInputType(InputType.TYPE_CLASS_DATETIME);
        titleEditText2.setInputType(InputType.TYPE_CLASS_DATETIME); //日時のみ入力可能
        contentEditText2.setInputType(InputType.TYPE_CLASS_DATETIME);
    }

    //SharedPreferencesにEditTextの内容を保存
    public void save(View v) {
        String titleText = titleEditText.getText().toString();
        String contentText = contentEditText.getText().toString();
        String titleText2 = titleEditText2.getText().toString();
        String contentText2 = contentEditText2.getText().toString();


        SharedPreferences.Editor editor = pref.edit();
        editor.putString("key_title", titleText);
        editor.putString("key_content", contentText);
        editor.putString("key_title2", titleText2);
        editor.putString("key_content2", contentText2);
        editor.putString("key_result",text);//textview の中身も保存したい    ***
        editor.commit();

        finish();
    }

    public void clear(View v) {
        titleEditText.getEditableText().clear();  //get...でedittextの中身取り出せる
        contentEditText.getEditableText().clear();
        result.setText("");
    }

    public void cal(View v) {
        String a = titleEditText.getText().toString(); //toStringしなかったら型は何？
        String b = contentEditText.getText().toString();
        String c = titleEditText2.getText().toString();
        String d = contentEditText2.getText().toString();
        String[] check = {a,b,c,d};

        //2つ埋まってなかったら　すべて埋まっている必要はない
        /*
        for(int i=0; i<check.length;i++){
        if(i%2==0 && check[i].length() != 0 && check[i+1].length() !=0){
            int[] timec = getTime(a,b);
            int[] timec2 = getTime(c,d);
            result.setText(String.valueOf(String.format("%02d", timec[0])) + ":" + String.valueOf(String.format("%02d", timec[1])));
        }
        if(b.length() == 0){
            Toast toast = Toast.makeText(MemoActivity.this,"内容を入力してね！",Toast.LENGTH_SHORT);
            toast.show();
        }/else {
            Toast toast = Toast.makeText(MemoActivity.this,"テキストを入力してね！",Toast.LENGTH_SHORT);
            toast.show();
        }}
        */
        if(b.length() == 0){
            Toast toast = Toast.makeText(MemoActivity.this,"内容を入力してね！",Toast.LENGTH_SHORT);
            toast.show();
        }else {
            int[] timec = getTime(a, b);
            int[] timec2 = getTime(c, d);
            int hour = timec[0] + timec2[0];
            int minute = timec[1] + timec2[1];
            if(minute>=60){
                minute = minute - 60;
                hour = hour+1;
            }
            text = String.valueOf(String.format("%02d", hour)) + ":" + String.valueOf(String.format("%02d", minute));
            result.setText(text);//more beautiful
        }
    }

    public int[] getTime(String str1,String str2){
        String[] time1 = str1.split(":", 0); // string
        String[] time2 = str2.split(":", 0); // string
        int[] time11 = new int[2];          //int
        int[] time22 = new int[2];
        for (int i = 0; i < 2; i++) {
            time11[i] = Integer.parseInt(time1[i]); //oo:oo-> oo  oo
            time22[i] = Integer.parseInt(time2[i]); //oo:oo-> oo  oo
        }
        int[] timec = new int[2];           //複数データとなると、2D配列使う?
        for (int i = 0; i < 2; i++) {
            timec[i] = time22[i] - time11[i];
            if(timec[1]<0) {
                timec[0]--;
                timec[1]=timec[1]+60;
            }
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