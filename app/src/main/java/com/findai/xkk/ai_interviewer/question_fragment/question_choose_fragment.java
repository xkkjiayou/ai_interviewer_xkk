package com.findai.xkk.ai_interviewer.question_fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.findai.xkk.ai_interviewer.R;
import com.findai.xkk.ai_interviewer.domain.Question;

@SuppressLint("ValidFragment")
public class question_choose_fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.question_choose_templates, container, false);
        RadioGroup radioGroup = view.findViewById(R.id.tv_question_radio);
        Bundle bundle = getArguments();
        Question q= (Question)bundle.getSerializable("question");
        TextView qtitle = (TextView)view.findViewById(R.id.tv_question_title);
        qtitle.setText(q.getTitle());
        int i =0;
        for(String qchoose_item:q.getQuestion_choose_items()){
            RadioButton rb = new RadioButton(getContext());
            rb.setId(i);
            rb.setText(qchoose_item);
            i++;
            radioGroup.addView(rb);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                System.out.println("------------------");
                String answer = ((RadioButton)group.getChildAt(checkedId)).getText().toString();
                System.out.println(answer);
                callbackQuestion_choose_fragment.get_question_answer(answer);
            }
        });
        return view;

    }

    public interface callbackQuestion_Choose_Fragment{
        public String get_question_answer(String answer);
    }
    callbackQuestion_Choose_Fragment callbackQuestion_choose_fragment = null;

    public question_choose_fragment(){}
    public question_choose_fragment(callbackQuestion_Choose_Fragment callbackQuestionChooseFragment){
        this.callbackQuestion_choose_fragment = callbackQuestionChooseFragment;

    }
}
