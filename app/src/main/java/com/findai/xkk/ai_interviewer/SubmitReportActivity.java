package com.findai.xkk.ai_interviewer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.findai.xkk.ai_interviewer.Dao.Question_Data_Exe;
import com.findai.xkk.ai_interviewer.Http.Commiuncate_Server;
import com.findai.xkk.ai_interviewer.domain.QuestionList;
import com.google.gson.Gson;

public class SubmitReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_interview_waiting_for_report);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        Intent intent = getIntent();
        QuestionList questionList = (QuestionList)intent.getBundleExtra("bundle_questionlist").getSerializable("questionlist");
//        System.out.println(questionList.getQuestionList().get(0).answer);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        Gson gson = new Gson();
        String json = gson.toJson(questionList);
        System.out.println(json);
        Commiuncate_Server cs = new Commiuncate_Server();
        try{
            //拿到报告返回结果，上传图像和结果
            String report_result = cs.post_answer(json,null);


        }catch (Exception ex){ex.printStackTrace();}

    }


}
