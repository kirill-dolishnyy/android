package com.example.myapplication.myapplication;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

public class  MainActivity extends AppCompatActivity {

    // Объявим переменные для того, чтобы связать их с элементами интерфейса.
    Spinner day, month, year, sex;
    EditText pulse1, pulse2;
    Button mainBtn;

    // Объявим статические (static) видимые другим классам (public) константы (final).
    // INTENT_RESULTS_KEY -- ключ, по которому мы будем передавать данные в интент.
    final public static String INTENT_RESULTS_KEY = "results";
    // Эти константы нужны для того, чтобы сохранить текст результатов, которые может выдавать
    // сервер. Их мы будем прередавать по ключу в интет, чтобы другая активити имела
    // возможность понимать, какой результат, и по-своему интерпретировать его.
    final public static String GOOD_RESULTS_TEXT = "Введенные значения соответствуют отсутствию переутомления.";
    final public static String AVG_RESULTS_TEXT = "Введенные значения соответствуют небольшому переутомлению. Рекомендуется снижение нагрузки.";
    final public static String BAD_RESULTS_TEXT = "Введенные значения соответствуют высокому уровню переутомления. Рекомендуется снижение нагрузки или отпуск.";
    final public static String ERROR_RESULTS_TEXT = "Введенные значения являются некорректными.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Свяжем переменные и элементы интерфейса через идентификаторы, которые мы указали в
        // activity_main.xml
        day = findViewById(R.id.spinnerDay);
        month = findViewById(R.id.spinnerMonth);
        year = findViewById(R.id.spinnerYear);
        sex = findViewById(R.id.spinnerSex);
        pulse1 = findViewById(R.id.pulse_1);
        pulse2 = findViewById(R.id.pulse_2);
        mainBtn = findViewById(R.id.mainButton);

        // Создадим список строк. С помощью цикла заполним список номерами дней месяца.
        ArrayList<String> days = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            days.add(Integer.toString(i));
        }

        // Создадим список строк. С помощью цикла заполним список годами с 1910 по текущий.
        ArrayList<String> years = new ArrayList<>();
        // Получим текущий год с помощью класса Calendar.
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1910; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }

        // Вручную создадим строковый массив для названий месяцев.
        String[] months = new String[]{"Январь", "Февраль",
                "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь",
                "Октябрь", "Ноябрь", "Декабрь"};

        // Вручную создадим строковый массив для названий полов.
        String[] sexes = new String[]{"М", "Ж"};

        // Создадим строковый адаптер. Адаптер позволяет указать разметку, которую мы хотим
        // использовать для одного элемента. В данном случае мы берем стандартную
        // (android.R.layout.simple_spinner_item). А в качестве элементов указываем наш список дней
        // (days).
        ArrayAdapter<String> adapterDay = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, days);
        // Затем, устанавливаем разметку для обертки элементов. Также берем стандартный layout
        // (android.R.layout.simple_spinner_dropdown_item).
        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Аналогично с остальными адаптерами и списками или массивами.
        ArrayAdapter<String> adapterMonth = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, months);
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapterYear = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, years);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapterSex = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sexes);
        adapterSex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Теперь мы можем в качестве источника данных для элементов Spinner указать наш адаптер,
        // который содержит разметку и сами элементы.
        day.setAdapter(adapterDay);
        month.setAdapter(adapterMonth);
        year.setAdapter(adapterYear);
        sex.setAdapter(adapterSex);
    }

    // Реализуем обработчик нажатия на кнопку, который мы указали в activity_main.xml.
    public void OnClick(View v){
        // Если поля, в которые нужно вводить значения не заполнены, то есть их
        // содержимое (getText), приведенное к строке (toString) равно пустой строке (equals("")),
        // то выводим пользователю Toast.
        if(pulse1.getText().toString().equals("") || pulse2.getText().toString().equals("")){
            // Текст Toast будет "Заполните пустые поля!". Указываем, чтобы он показывался
            // дольше (Toast.LENGTH_SHORT).
            Toast toast =  Toast.makeText(MainActivity.this,
                    "Заполните пустые поля!", Toast.LENGTH_SHORT);
            // Показываем пользователю Toast.
            toast.show();
        }
        else {
             // Если же пульс введен, то вызываем функцию для попытки показать результаты.
            showResults();
        }
    }

    private void showResults() {
        // Создаем новый поток. В качестве аргумента конструктора Thread указываем
        // лямба-функцию (() -> {...), которая генерирует класс
        // Runnable (именно его ожидает получить констурктор Thread), в котором переопределен
        // единственный метод -- run. Создавая объект Thread, мы тут же его запускаем (.start()).
        new Thread(() -> {
            // Код который будет выполнять в потоке написан тут.
            // Используем обработчик ислючений (try ... catch ...), т.к. наш метод получения ответа
            // от сервера (getServerResponse) может выбрасывать исключения.
            try {
                // Получаем ответ севера, используя наш метод getServerResponse. Передаем в него
                // массив строк в формате {день, месяц, год, пол, пульс1, пульс2}.
                String response = getServerResponse(new String[] {
                        // Берем у спиннера для выбопа дней выбранный элемент и приводим его к
                        // строке.
                        day.getSelectedItem().toString(),
                        // Для месяца берем индекс выбранного элемента и
                        // прибавляем 1 (индксация с 0). Приводим к строке через String.valueOf.
                        String.valueOf(month.getSelectedItemPosition() + 1),
                        // Аналогично спиннеру для выбора дней.
                        year.getSelectedItem().toString(),
                        // Если выбранный элемент спиннера -- "М", то в пол передаем "1", иначе "2".
                        sex.getSelectedItem() == "М" ? "1" : "2",
                        // Аналогично спиннеру для выбора дней.
                        pulse1.getText().toString(),
                        // Аналогично спиннеру для выбора дней.
                        pulse2.getText().toString()
                });
                // Создаем новый интент для вызова MainActivity2.
                Intent intent = new Intent(this, MainActivity2.class);
                // По умолчанию результат совпадает с текстом константы для ошибки.
                String results = ERROR_RESULTS_TEXT;
                // Проверим, содержит ли ответ сервера другие тектовые результаты и если да, то
                // переприсвоим results
                if (response.contains(GOOD_RESULTS_TEXT)) {
                    results = GOOD_RESULTS_TEXT;
                } else if (response.contains(AVG_RESULTS_TEXT)) {
                    results = AVG_RESULTS_TEXT;
                } else if (response.contains(BAD_RESULTS_TEXT)) {
                    results = BAD_RESULTS_TEXT;
                }
                // Добавим в интент наш тектовый результат по ключу, указанному в константе.
                intent.putExtra(INTENT_RESULTS_KEY, results);
                // Запустим активити.
                startActivity(intent);
            } catch (Exception ignored) {
                // Если все же исключение было сгенерировано, то просто ничего не делаем.
            }
        }).start();
    }

    // Метод для запроса к серверу.
    private String getServerResponse(String[] parameters) throws Exception {
        // На основе объекта URL, в который мы передаем ссылку к серверу, мы открываем соединения
        // и получаем объект HttpURLConnection.
        HttpURLConnection connection = (HttpURLConnection)
                new URL("http://abashin.ru/cgi-bin/ru/tests/burnout").openConnection();
        // Устанавливаем метод запроса POST.
        connection.setRequestMethod("POST");
        // Ставим дополнительный заголовок, чтобы сервер думал, что мы браузер.
        connection.setRequestProperty("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");
        // Включаем режим, который позволяет записать информацию в тело запроса.
        connection.setDoOutput(true);
        // Получаем поток вывода нашего соединения (connection.getOutputStream()) и пишем
        // туда (write) набор параметров, который на передали в строку. Строку предварительно
        // переводим в массив байтов (getBytes).
        connection.getOutputStream().write(
                String.format("day=%s&month=%s&year=%s&sex=%s&m1=%s&m2=%s", parameters).getBytes());
        // Получаем поток ввода connection.getInputStream() и передаем его сканнеру Scanner. Далее,
        // прочитать все, что отсканировано, мы используем разделитель \\A -- разделитель начала,
        // т.е. считываем весь текст и вызываем метод получения следующего блока текста next.
        // Таким образом получаем весь текст сразу.
        return new Scanner(connection.getInputStream()).useDelimiter("\\A").next();
    }
}