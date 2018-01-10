package com.example.nttr.quiz;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
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

    boolean kaitou;

    Hero hero;

    int countScore = 0;

    int score = 0;

    int nowNumber = -1;

    MediaPlayer maru;
    MediaPlayer batu;

    final int maxQuestionNumber = 5;

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

        countScore = 0;
        score = 0;

        maru = MediaPlayer.create(getApplicationContext(),R.raw.maru);
        batu = MediaPlayer.create(getApplicationContext(),R.raw.batu);

        // Quizを1から始めるのでリセットする
        resetQuiz();
    }

    // ③ 出題するクイズのリストを作成します
    void createQuizList() {
        Quiz quiz1 = new Quiz("日本で一番高い山は富士山ですが、世界で一番高い山は？", "キリマンジャロ", "エベレスト", "K2", "エベレスト");
        Quiz quiz2 = new Quiz("元素記号Coで表されるものはコバルトですが、化学式COで表されるものは？", "一酸化炭素", "コロンビア", "クーリングオフ", "一酸化炭素");
        Quiz quiz3 = new Quiz("重さの単位でmgはミリグラムと読みますが、Mgは何と読むでしょう？", "マグネシウム", "メタグラム", "メガグラム", "メガグラム");
        Quiz quiz4 = new Quiz("日本で一番大きい湖は琵琶湖ですが、世界で一番広い湖は？", "カスピ海", "ビクトリア湖", "太平洋", "カスピ海");
        Quiz quiz5 = new Quiz("世界で一番高いビルはブルジュ・ハリファですが、日本一高いビルは？", "あべのハルカス", "横浜ランドマークタワー", "東京スカイツリー", "あべのハルカス");
        Quiz quiz6 = new Quiz("赤外線の略称はIRですが、紫外線の略称は？", "UV", "AR", "DD", "UV");
        Quiz quiz7 = new Quiz("rightは日本語で右という意味ですが、lightは日本語で何という意味？", "センター", "光", "左", "光");
        Quiz quiz8 = new Quiz("アイスランドの首都はレイキャヴィークですが、アイルランドの首都は？", "リムリック", "コーク", "ダブリン", "ダブリン");
        Quiz quiz9 = new Quiz("ギリシャ文字の最初の文字はαですが、最後の文字は", "プサイ", "カイ", "オメガ", "オメガ");
        Quiz quiz10 = new Quiz("日本で一番多い苗字は佐藤ですが、アメリカで一番多い名字は？", "マイク", "ブラウン", "スミス", "スミス");
        // Listを実装したArrayListというものを使います
        quizList = new ArrayList<>();
        quizList.add(quiz1);
        quizList.add(quiz2);
        quizList.add(quiz3);
        quizList.add(quiz4);
        quizList.add(quiz5);
        quizList.add(quiz6);
        quizList.add(quiz7);
        quizList.add(quiz8);
        quizList.add(quiz9);
        quizList.add(quiz10);
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
        optionButtons[0].setClickable(true);
        optionButtons[1].setClickable(true);
        optionButtons[2].setClickable(true);
        optionButtons[0].setPressed(false);
        optionButtons[1].setPressed(false);
        optionButtons[2].setPressed(false);
        questionNumber++;
        if (questionNumber < quizList.size() && questionNumber < maxQuestionNumber) {
            imageView.setImageResource(R.mipmap.syakin_rr2);
            showQuiz();
        } else {
            finish();
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("score", score);
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
        if(nowNumber != questionNumber){
            nowNumber = questionNumber;
            optionButtons[0].setClickable(false);
            optionButtons[1].setClickable(false);
            optionButtons[2].setClickable(false);
            // 引数のViewには、押されたViewが入っています。
            // Buttonが押された時しかよばれないので、キャストといって型を示してあげます。
            progressClass.cancel(true);
            Button clickedButton = (Button) view;
            if (questionNumber < quizList.size()) {
                Quiz quiz = quizList.get(questionNumber);
                // ボタンの文字と、答えが同じかチェックします。
                if (TextUtils.equals(clickedButton.getText(), quiz.answer)) {
                    // 正解の場合だけ1ポイント加算します。
                    kaitou = true;
                    point++;
                    score += countScore;
//                Thread nT = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                imageView.setImageResource(R.drawable.maru);
//                                try {
//                                    Thread.sleep(1000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                    }
//                });
//                nT.start();
//                try {
//                    nT.join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                Toast.makeText(this, "正解", Toast.LENGTH_SHORT).show();
                } else {
                    kaitou = false;
//                Toast.makeText(this, "はずれ", Toast.LENGTH_SHORT).show();
                }
                // 次の問題にアップデートします。
//            updateQuiz();
                hero = new Hero(this);
                hero.execute();
            }
        }else{
            Log.d("連打","やめて");
        }
    }

    private void startProgress() {
        progressBar.setProgress(250);
        countScore = 0;
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

//    public void displayToast() {
//        imageView.setImageResource(R.drawable.batu);
//        try {
//            Thread.sleep(1000);
//        } catch (Exception e) {
//
//        }
////        Toast.makeText(this, "時間切れ", Toast.LENGTH_SHORT).show();
//        updateQuiz();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            progressClass.cancel(true);
            hero.cancel(true);
            maru.stop();
            batu.stop();
        }catch (Exception e){

        }
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
                    mainActivity.countScore = 10000 - progressTime;

                    if (progressTime >= 10000) {
                        return true;
                    }

//                    mainActivity.progressBar.setProgress(progressTime);
                    if (progressTime >= c *250) {
                        c += 1;
                        publishProgress(c);
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
                    mainActivity.progressBar.setProgress(count * 250);

                    if(count % 2 == 0){
                        mainActivity.imageView.setImageResource(R.mipmap.syakin_rr2);
                    }else{
                        mainActivity.imageView.setImageResource(R.mipmap.syakin_r2);
                    }

                    if (len >= count) {
                        String que = question.substring(0, count);
                        mainActivity.contentTextView.setText(que);

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
//                mainActivity.displayToast();
                this.mainActivity.kaitou = false;
                Hero hero = new Hero(this.mainActivity);
                hero.execute();
            }else {
                cancel(true);
            }
        }
    }

    private static class Hero extends AsyncTask<Void, Boolean, Boolean> {

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
        public Hero(MainActivity mainActivity) {
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
                if(!isCancelled()){
                    publishProgress(this.mainActivity.kaitou);
                    Thread.sleep(2000);
                    return true;
                }else{
                    cancel(true);
                    return false;
                }
            } catch (Exception e) {
                exception = e;
                cancel(true);
                return false;
            }
        }

        // doInBackground中にUI更新
        @Override
        protected void onProgressUpdate(Boolean... progress) {
            for (Boolean bool : progress) {
                try{
                    if(bool){
                        this.mainActivity.imageView.setImageResource(R.mipmap.maru2);
                        this.mainActivity.maru.start();
                    }else{
                        this.mainActivity.imageView.setImageResource(R.mipmap.batu2);
                        this.mainActivity.batu.start();
                    }
                }catch (Exception e){
                    cancel(true);
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
            cancel(true);
            this.mainActivity.updateQuiz();
        }
    }
}

// maru batu 中にバックキーがやばい

