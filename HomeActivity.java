package com.example.qq;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.BreakIterator;

public class HomeActivity extends AppCompatActivity {

    private Context context;
    private String[] animals;
    private String[] animalQuestions;
    private int[] images;
    private TextView txtUsername;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // 初始化数据
        context = this;
        animals = new String[]{"牛说", "狗说", "鸭说", "鱼说","马说"};
        animalQuestions = new String[]{
                "你是牛魔王吗?",
                "你是哮天犬吗?",
                "你是丑小鸭吗?",
                "你是美人鱼吗?",
                "你是白龙马吗?"
        };
        images = new int[]{
                R.drawable.cow, R.drawable.dog, R.drawable.duck, R.drawable.fish,R.drawable.horse
        };

        txtUsername = findViewById(R.id.txtUsername);
        String username = getIntent().getStringExtra("username");
        txtUsername.setText("Welcome, " + username + " 来到QQ动物问答");

        // 获取 ListView 并设置适配器
        ListView listView = findViewById(R.id.animal_list);
        AnimalAdapter adapter = new AnimalAdapter(context, animals, animalQuestions, images);
        listView.setAdapter(adapter);
    }

    // 适配器类
    public class AnimalAdapter extends android.widget.BaseAdapter {

        private Context context;
        private String[] animals;
        private String[] animalQuestions;
        private int[] images;

        public AnimalAdapter(Context context, String[] animals, String[] animalQuestions, int[] images) {
            this.context = context;
            this.animals = animals;
            this.animalQuestions = animalQuestions;
            this.images = images;
        }

        @Override
        public int getCount() {
            return animals.length;
        }

        @Override
        public Object getItem(int position) {
            return animals[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_animal, parent, false);
            }

            // 获取视图中的控件
            ImageView imageView = convertView.findViewById(R.id.animal_image);
            TextView textView = convertView.findViewById(R.id.animal_text);
            TextView questionTextView = convertView.findViewById(R.id.animal_question);

            // 设置数据
            imageView.setImageResource(images[position]);
            textView.setText(animals[position]);
            questionTextView.setText(animalQuestions[position]);

            return convertView;
        }
    }
}
