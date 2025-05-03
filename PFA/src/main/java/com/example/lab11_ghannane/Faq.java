package com.example.lab11_ghannane;

import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Faq extends BaseActivity {

    ExpandableListView expandableListView;
    List<Map<String, String>> groupData = new ArrayList<>();
    List<List<Map<String, String>>> childData = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        String[] questions = {
                getString(R.string.faq_question_1),
                getString(R.string.faq_question_2),
                getString(R.string.faq_question_3),
                getString(R.string.faq_question_4),
                getString(R.string.faq_question_5),
                getString(R.string.faq_question_6),
                getString(R.string.faq_question_7),
                getString(R.string.faq_question_8),
                getString(R.string.faq_question_9),
                getString(R.string.faq_question_10)
        };

        String[] answers = {
                getString(R.string.faq_answer_1),
                getString(R.string.faq_answer_2),
                getString(R.string.faq_answer_3),
                getString(R.string.faq_answer_4),
                getString(R.string.faq_answer_5),
                getString(R.string.faq_answer_6),
                getString(R.string.faq_answer_7),
                getString(R.string.faq_answer_8),
                getString(R.string.faq_answer_9),
                getString(R.string.faq_answer_10)
        };

        expandableListView = findViewById(R.id.faqListView);

        for (int i = 0; i < questions.length; i++) {
            Map<String, String> groupMap = new HashMap<>();
            groupMap.put("group", questions[i]);
            groupData.add(groupMap);

            List<Map<String, String>> childList = new ArrayList<>();
            Map<String, String> childMap = new HashMap<>();
            childMap.put("child", answers[i]);
            childList.add(childMap);

            childData.add(childList);
        }

        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                this,
                groupData,
                android.R.layout.simple_expandable_list_item_1,
                new String[]{"group"},
                new int[]{android.R.id.text1},
                childData,
                R.layout.faq_child_item, // custom layout for answer
                new String[]{"child"},
                new int[]{R.id.faqAnswer} // custom TextView ID
        );

        expandableListView.setAdapter(adapter);
    }
}