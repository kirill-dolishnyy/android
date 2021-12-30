package com.example.myapplication.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {
    // Объявим переменные для того, чтобы связать их с элементами интерфейса.
    TextView status;
    ImageView emoji;
    ProgressBar stressLvl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Свяжем переменные и элементы интерфейса через идентификаторы, которые мы указали в
        // activity_main2.xml
        emoji = findViewById(R.id.resultImage);
        status = findViewById(R.id.resultTxt);
        stressLvl = findViewById(R.id.progressBar);

        // Получим интент, который созавал эту активити (getIntent()), и получим из него
        // текстовый результат по ключу, который берем из констатнты MainActivity.
        String results = getIntent().getStringExtra(MainActivity.INTENT_RESULTS_KEY);
        // Показываем пользователю в TextView тектовый результат.
        status.setText(results);
        // В качестве картинки поставим картинку по умолчанию.
        emoji.setImageResource(R.drawable.attention);
        // Переберем результат и в зависимости от совпадаения с к константами из MainActivity
        // установим прогрес прогресбара и картинку.
        switch (results) {
            case MainActivity.GOOD_RESULTS_TEXT:
                stressLvl.setProgress(2);
                emoji.setImageResource(R.drawable.ok);
                break;
            case MainActivity.AVG_RESULTS_TEXT:
                stressLvl.setProgress(1);
                break;
            case MainActivity.BAD_RESULTS_TEXT:
                emoji.setImageResource(R.drawable.bad);
                break;
        }
    }

}