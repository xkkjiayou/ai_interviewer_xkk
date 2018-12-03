package com.findai.xkk.ai_interviewer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.findai.xkk.ai_interviewer.Dao.Question_Data_Exe;
import com.findai.xkk.ai_interviewer.Http.Commiuncate_Server;
import com.findai.xkk.ai_interviewer.domain.QuestionList;
import com.google.gson.Gson;

public class WelcomeIndexActivity extends AppCompatActivity {

    private Question_Data_Exe question_data_exe;
    private QuestionList questionList=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_waiting_index);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Thread  thread= new Thread(new Runnable() {
            @Override
            public void run() {
            question_data_exe = new Question_Data_Exe(getBaseContext());
            try{
                questionList = question_data_exe.Add_Question_To_DB(1);
                Thread.sleep(4000);

                Intent intent = new Intent(getBaseContext(),MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("questionlist",questionList);
                intent.putExtra("index_get_questionlist_bundle",bundle);
                startActivity(intent);
                finish();
            }catch (Exception ex){
                ex.printStackTrace();
            }
            }
        });
        thread.start();
//        while(questionList == null)
//        {
//
//        }
//        try {
//
//            Thread.sleep(20000);
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }


    }
}
