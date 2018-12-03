package com.findai.xkk.ai_interviewer.Http;

import android.util.Log;

import com.findai.xkk.ai_interviewer.Dao.Question_Data_Exe;
import com.findai.xkk.ai_interviewer.domain.QuestionList;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Commiuncate_Server {

    String get_question_url = "http://115.159.59.188:5000/get_question_by_industry_id?iid=";
    String post_answer_url = "http://172.30.183.1:5000/post_answer_head_result";

    public QuestionList get_question_by_iid(int qid) throws Exception {
        OkHttpClient client = new OkHttpClient();
        String qurl = get_question_url + qid;
        System.out.println(qurl);
        Request request = new Request.Builder().url(qurl).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String str = response.body().string();
            System.out.println(str);
            return get_question_list(str);
        } else {
            throw new IOException("Unexpected code " + response);
        }
//        return get_question_list("");
    }

    public String get_my_report_by_id(int uid) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String qurl = get_question_url + "get_user_report_list/?userid=" + uid;
        Request request = new Request.Builder().url(qurl).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }



    private QuestionList get_question_list(String resp_json) throws Exception{

//        resp_json = "{\"questionList\":[\n" +
//                "\t\t\t\t{\"qid\":11,\"qtitle\":\"人工智能好不好\",\"question_choose_items\":[\"好的图塔糊涂哦\",\"不好\",\"凑合吧\"],\"answer\":\"\",\"type\":\"0\"}\t,{\"qid\":4,\"qtitle\":\"LSTM原理是什么样的，请简述\",\"question_choose_items\":[],\"answer\":\"\",\"type\":\"1\"},{\"qid\":5,\"qtitle\":\"计算机好不好\",\"question_choose_items\":[\"好\",\"不好\"],\"answer\":\"\",\"type\":\"0\"}\t\t\t\n" +
//                "\t\t\t\t\t\n" +
//                "\n" +
//                "\n" +
//                "\t\t\t\t]}";
        JSONObject json = new JSONObject(resp_json);
        System.out.println(resp_json);
        Gson gson = new Gson();
        QuestionList qlist = gson.fromJson(resp_json,QuestionList.class);
        return qlist;
    }

    public String post_answer(String answer,File file) throws Exception{
        OkHttpClient client = new OkHttpClient();
        String url = post_answer_url + answer;
        System.out.println(url);

        RequestBody filebody = RequestBody.create(MediaType.parse("image/png"),file);

        RequestBody requestBody  = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("head_image","head_image",filebody)
                .addFormDataPart("answer",answer)
                .build();

        Request request = new Request.Builder()
                .url(post_answer_url)
                .post(requestBody)
                .build();

        Response response;
        try{
            response = client.newCall(request).execute();
            String jsonString = response.body().string();
            Log.d("面试结果上传",jsonString);

//            Gson gson = new Gson();
            return jsonString;



        }catch (Exception ex){
            ex.printStackTrace();
            return "";
        }


    }
}
