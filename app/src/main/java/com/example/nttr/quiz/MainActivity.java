package com.example.nttr.quiz;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
    // ① 変数の宣言
    TextView countTextView;
    TextView contentTextView;
    // Buttonの配列
    // ボタンを3つセットで管理したいので、配列で宣言します。
    Button[] optionButtons;
    // 現在の問題番号
    int questionNumber;
    // 獲得したポイント数
    int point;
    // Listという、配列よりもデータを追加しやすい型を使います。
    List<Quiz> quizList;

    ProgressBar progressBar;

    ProgressClass progressClass;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ② 初期化処理
        // Viewの関連付けを行います
        countTextView = findViewById(R.id.countTextView);
        contentTextView = findViewById(R.id.contentTextView);
        // 配列を初期化します
        // 今回、ボタンは3つなので、3の大きさの配列を用意します
        optionButtons = new Button[3];
        optionButtons[0] = findViewById(R.id.optionButton1);
        optionButtons[1] = findViewById(R.id.optionButton2);
        optionButtons[2] = findViewById(R.id.optionButton3);
        for (Button button : optionButtons) {
            // レイアウトではなくコードからonClickを設定します
            // setOnClickListenerに、thisを入れて呼び出します
            button.setOnClickListener(this);
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        imageView = (ImageView) findViewById(R.id.imageRed);

        // Quizを1から始めるのでリセットする
        resetQuiz();
    }

    // ③ 出題するクイズのリストを作成します
    void createQuizList() {
        Quiz quiz1 = new Quiz("ドラえもんの身長は何センチ？", "127.3cm", "128.3cm", "129.3cm", "129.3cm");
        Quiz quiz2 = new Quiz("ドラえもんの嫌いな生き物は？", "猫", "タヌキ", "ネズミ", "ネズミ");
        Quiz quiz3 = new Quiz("ドラえもんの好きな食べ物は？", "ラーメン", "牛丼", "どら焼き", "どら焼き");
        Quiz quiz4 = new Quiz("ドラえもんは昔何色だった？", "黄色", "赤色", "水色", "黄色");
        Quiz quiz5 = new Quiz("アニメ版ドラえもん、「ドラえもんのうた」に出てこない道具は？", "タケコプター", "スモールライト", "どこでもドア", "スモールライト");
        // Listを実装したArrayListというものを使います
        quizList = new ArrayList<>();
        quizList.add(quiz1);
        quizList.add(quiz2);
        quizList.add(quiz3);
        quizList.add(quiz4);
        quizList.add(quiz5);
        // Listの中身をシャッフルします
        Collections.shuffle(quizList);
    }

    // ④ クイズを表示します
    void showQuiz() {
        countTextView.setText((questionNumber + 1) + "問目");
        // 表示する問題をリストから取得します。
        // 配列では[番号]でしたが、リストでは get(番号)で取得します。配列と同じく0からスタートです。
        Quiz quiz = quizList.get(questionNumber);
//        contentTextView.setText(quiz.content);
        optionButtons[0].setText(quiz.option1);
        optionButtons[1].setText(quiz.option2);
        optionButtons[2].setText(quiz.option3);
        startProgress();
    }

    // ⑤ クイズのリセット
    void resetQuiz() {
        questionNumber = 0;
        point = 0;
        progressBar.setProgress(0);
        createQuizList();
        showQuiz();
    }

    // ⑥ クイズのアップデート
    void updateQuiz() {
        questionNumber++;
        if (questionNumber < quizList.size()) {
            if (questionNumber % 2 == 0) {
                imageView.setImageResource(R.drawable.syakin_rr);
            } else {
                imageView.setImageResource(R.drawable.syakin_r);
            }
            showQuiz();
        } else {
            finish();
            Intent intent = new Intent(this, ResultActivity.class);
            startActivity(intent);

//            // 全ての問題を解いてしまったので、結果を表示します。
//            // 結果表示にはDialogを使います。
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("結果");
//            builder.setMessage(quizList.size() + "問中、" + point + "問正解でした。");
//            builder.setPositiveButton("リトライ", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    // もう一度クイズをやり直す
////                    resetQuiz();
//
//                    // ここではresetquizを呼び出さず、
//                    // ボタンもしくは画面タップでダイアログが消えた時に呼び出されるようにする。
//                }
//            });
//
//            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                @Override
//                public void onDismiss(DialogInterface dialog) {
//                    // もう一度クイズをやり直す
//                    resetQuiz();
//                }
//            });
//
//            builder.show();
        }
    }

    // ⑦ setOnClickListerを呼び出したViewをクリックした時に呼び出されるメソッド
    @Override
    public void onClick(View view) {
        // 引数のViewには、押されたViewが入っています。
        // Buttonが押された時しかよばれないので、キャストといって型を示してあげます。
        progressClass.cancel(true);
        Button clickedButton = (Button) view;
        if (questionNumber < quizList.size()) {
            Quiz quiz = quizList.get(questionNumber);
            // ボタンの文字と、答えが同じかチェックします。
            if (TextUtils.equals(clickedButton.getText(), quiz.answer)) {
                // 正解の場合だけ1ポイント加算します。
                point++;
                Thread nT = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageResource(R.drawable.maru);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
                nT.start();
                try {
                    nT.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                Toast.makeText(this, "正解", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(this, "はずれ", Toast.LENGTH_SHORT).show();
            }
            // 次の問題にアップデートします。
            updateQuiz();
        }
    }

    private void startProgress() {
        progressClass = new ProgressClass(this);
        progressClass.execute();

//        timer = new Thread() {
//
//            private boolean active = true;
//
//            public void run() {
//                long startTime = System.currentTimeMillis();
//                while (active) {
//                    try {
//                        Thread.sleep(0);
//                        long nowTime = System.currentTimeMillis();
//                        int progressTime = (int) (nowTime - startTime);
//                        progressBar.setProgress(progressTime);
//                        if (progressTime >= 10000) {
//                            displayToast();
//                            break;
//                        }
//                    } catch (InterruptedException e) {
//                        break;
//                    }
//                }
//            }
//        };
//        timer.start();
    }

    public void displayToast() {
        imageView.setImageResource(R.drawable.batu);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }
//        Toast.makeText(this, "時間切れ", Toast.LENGTH_SHORT).show();
        updateQuiz();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressClass.cancel(true);
    }

    private static class ProgressClass extends AsyncTask<Void, Integer, Boolean> {

        private MainActivity mainActivity;

        private Exception exception;

        // AsyncTaskのシングルトンは厳しいらしい
//    private static ProgressClass progressClass = null;
//
//    public static ProgressClass getInstance(MainActivity mainActivity){
//        if(progressClass == null){
//            progressClass = new ProgressClass(mainActivity);
//        }
//        return progressClass;
//    }

        /**
         * コンストラクタ
         */
        public ProgressClass(MainActivity mainActivity) {
            super();
            this.mainActivity = mainActivity;
        }

//        private void setNullActivity(){
//            this.mainActivity = null;
//        }

        /**
         * バックグランドで行う処理
         */
        @Override
        protected Boolean doInBackground(Void... value) {
            try {
                long startTime = System.currentTimeMillis();
                int c = 0;
                while (!isCancelled()) {
                    long nowTime = System.currentTimeMillis();
                    int progressTime = (int) (nowTime - startTime);
                    mainActivity.progressBar.setProgress(progressTime);
                    if (progressTime >= c * 300) {
                        c += 1;
                        publishProgress(c);
                    }

                    if (progressTime >= 10000) {
                        return true;
                    }
                }
                return false;
            } catch (Exception e) {
                exception = e;
                cancel(true);
                return false;
            }
        }

        // doInBackground中にUI更新
        @Override
        protected void onProgressUpdate(Integer... progress) {
            if (mainActivity.questionNumber < mainActivity.quizList.size()) {
                String question = mainActivity.quizList.get(mainActivity.questionNumber).content;
                int len = question.length();
                for (Integer count : progress) {
                    if (len >= count) {
                        String que = question.substring(0, count);
                        mainActivity.contentTextView.setText(que);
                        if(count % 2 == 0){
                            mainActivity.imageView.setImageResource(R.drawable.syakin_rr);
                        }else{
                            mainActivity.imageView.setImageResource(R.drawable.syakin_r);
                        }
                    }
                }
            }
        }

        private boolean shouldReject() {
            MainActivity activity = this.mainActivity;

            if (activity != null) {
                return activity.isFinishing() || !activity.hasWindowFocus();
            } else {
                return true;
            }
        }

        @Override
        protected void onCancelled() {
            if (!shouldReject()) {
                if (exception != null) {
                    Log.e("exception", "exception : " + exception);
                }
            }
            exception = null;
        }

        /**
         * バックグランド処理が完了し、UIスレッドに反映する
         */
        @Override
        protected void onPostExecute(Boolean result) {
            if (result && !isCancelled()) {
                mainActivity.displayToast();
            }
        }
    }

//    private static class Hero extends AsyncTask<Void, Boolean, Boolean> {
//
//        private MainActivity mainActivity;
//
//        private Exception exception;
//
//        // AsyncTaskのシングルトンは厳しいらしい
////    private static ProgressClass progressClass = null;
////
////    public static ProgressClass getInstance(MainActivity mainActivity){
////        if(progressClass == null){
////            progressClass = new ProgressClass(mainActivity);
////        }
////        return progressClass;
////    }
//
//        /**
//         * コンストラクタ
//         */
//        public Hero(MainActivity mainActivity) {
//            super();
//            this.mainActivity = mainActivity;
//        }
//
////        private void setNullActivity(){
////            this.mainActivity = null;
////        }
//
//        /**
//         * バックグランドで行う処理
//         */
//        @Override
//        protected Boolean doInBackground(Void... value) {
//            try {
//                publishProgress(this.mainActivity.kaitou);
//                return true;
//            } catch (Exception e) {
//                exception = e;
//                cancel(true);
//                return false;
//            }
//        }
//
//        // doInBackground中にUI更新
//        @Override
//        protected void onProgressUpdate(Boolean... progress) {
//            for (Boolean bool : progress) {
//                try{
//                    if(bool){
//                        this.mainActivity.imageView.setImageResource(R.drawable.maru);
//                    }else{
//                        this.mainActivity.imageView.setImageResource(R.drawable.maru);
//                    }
//                }catch (Exception e){
//                    cancel(true);
//                }
//            }
//        }
//
//        private boolean shouldReject() {
//            MainActivity activity = this.mainActivity;
//
//            if (activity != null) {
//                return activity.isFinishing() || !activity.hasWindowFocus();
//            } else {
//                return true;
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            if (!shouldReject()) {
//                if (exception != null) {
//                    Log.e("exception", "exception : " + exception);
//                }
//            }
//            exception = null;
//        }
//
//        /**
//         * バックグランド処理が完了し、UIスレッドに反映する
//         */
//        @Override
//        protected void onPostExecute(Boolean result) {
//            if (result && !isCancelled()) {
//
//            }
//        }
//    }
}

