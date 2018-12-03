package com.findai.xkk.ai_interviewer.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.findai.xkk.ai_interviewer.Http.Commiuncate_Server;
import com.findai.xkk.ai_interviewer.domain.Question;
import com.findai.xkk.ai_interviewer.domain.QuestionList;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

public class Question_Data_Exe {
    Communicate_SQLite dbhelper;
    SQLiteDatabase db;
    public Question_Data_Exe(Context context) {

        dbhelper = new Communicate_SQLite(context,"ai_interview",null,1);
        db = dbhelper.getWritableDatabase();
    }

    public QuestionList Add_Question_To_DB(int iid) throws Exception{

        Commiuncate_Server cs = new Commiuncate_Server();
        dbhelper.delete(db,"DELETE FROM question_choose_items");
        dbhelper.delete(db,"DELETE FROM question");
        QuestionList questionList = cs.get_question_by_iid(iid);

        for (Question q:questionList.getQuestionList()) {
            String answer = q.getAnswer();
            int qid = q.getQid();
            String qtitle = q.getTitle();
            List<String> qcitems = q.getQuestion_choose_items();
            int type = q.getType();


            String qsql = "INSERT INTO question(qid,qtitle,answer,type) VALUES('"+qid+"','"+qtitle+"','"+" "+"','"+type+"')";
            for(String item : qcitems) {
                String itemsql = "INSERT INTO question_choose_items(qid,item) VALUES('" + qid + "','" + item + "')";
                dbhelper.insert(db,itemsql);
            }

            dbhelper.insert(db,qsql);
        }
        return questionList;
    }

}
