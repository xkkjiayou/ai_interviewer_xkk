package com.findai.xkk.ai_interviewer.domain;

import java.io.Serializable;
import java.util.List;

public class QuestionList implements Serializable {
    List<Question> questionList;

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }
}
