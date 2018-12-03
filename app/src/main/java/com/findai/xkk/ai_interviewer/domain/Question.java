package com.findai.xkk.ai_interviewer.domain;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {

    //问题id
    public int qid;

    //问题题目
    public String qtitle;

    //问题选项
    public List<String> question_choose_items;

    //用户回答
    public String answer="";

    //问题类型：        type 1：问答题  type 0：选择题
    public int type;


    public int getQid() {
        return qid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public String getTitle() {
        return qtitle;
    }

    public void setTitle(String title) {
        this.qtitle = title;
    }

    public List<String> getQuestion_choose_items() {
        return question_choose_items;
    }

    public void setQuestion_choose_items(List<String> question_choose_items) {
        this.question_choose_items = question_choose_items;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
