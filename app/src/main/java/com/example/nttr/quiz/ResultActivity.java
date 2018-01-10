package com.example.nttr.quiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends Activity implements View.OnClickListener{

    int score = 0;

    TextView textView;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);

        textView = (TextView) findViewById(R.id.scoreText);
        textView.setText("score : " + score);

        button = (Button) findViewById(R.id.retryButton);
    }

    // ⑦ setOnClickListerを呼び出したViewをクリックした時に呼び出されるメソッド
    @Override
    public void onClick(View view) {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
