package rs.etf.ga070530.monopoly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class PlayActivity extends AppCompatActivity {

    private RadioGroup mRadioGroup1, mRadioGroup2, mRadioGroup3, mRadioGroup4;
    private EditText mPlayer1ET, mPlayer2ET, mPlayer3ET, mPlayer4ET;
    private String mPlayer1, mPlayer2, mPlayer3, mPlayer4;
    private String mP1Type = "Human", mP2Type = "Ai", mP3Type = "Off", mP4Type = "Off";
    private Button mStartButton, mCancelButton;
    private int p, p1, p2, p3, p4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);


        mPlayer1ET = (EditText) findViewById(R.id.name_player1);
        mPlayer2ET = (EditText) findViewById(R.id.name_player2);
        mPlayer3ET = (EditText) findViewById(R.id.name_player3);
        mPlayer4ET = (EditText) findViewById(R.id.name_player4);

        mStartButton = (Button) findViewById(R.id.button_start_game);
        mCancelButton = (Button) findViewById(R.id.button_cancel_game);


        mRadioGroup1 = (RadioGroup) findViewById(R.id.radio_group_player1);
        mRadioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button_player1:
                        mPlayer1ET.setEnabled(true);
                        mPlayer1ET.setText("Human 1");
                        mP1Type = "Human";
                        break;
                    case R.id.radio_button_ai1:
                        mPlayer1ET.setText("Computer 1");
                        mPlayer1ET.setEnabled(false);
                        mP1Type = "Ai";
                        break;
                    case R.id.radio_button_off1:
                        mPlayer1ET.setText(null);
                        mPlayer1ET.setEnabled(false);
                        mP1Type = "Off";
                        break;
                }

            }
        });

        mRadioGroup2 = (RadioGroup) findViewById(R.id.radio_group_player2);
        mRadioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button_player2:
                        mPlayer2ET.setEnabled(true);
                        mPlayer2ET.setText("Human 2");
                        mP2Type = "Human";
                        break;
                    case R.id.radio_button_ai2:
                        mPlayer2ET.setText("Computer 2");
                        mPlayer2ET.setEnabled(false);
                        mP2Type = "Ai";
                        break;
                    case R.id.radio_button_off2:
                        mPlayer2ET.setText(null);
                        mPlayer2ET.setEnabled(false);
                        mP2Type = "Off";
                        break;
                }

            }
        });

        mRadioGroup3 = (RadioGroup) findViewById(R.id.radio_group_player3);
        mRadioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button_player3:
                        mPlayer3ET.setEnabled(true);
                        mPlayer3ET.setText("Human 3");
                        mP3Type = "Human";
                        break;
                    case R.id.radio_button_ai3:
                        mPlayer3ET.setText("Computer 3");
                        mPlayer3ET.setEnabled(false);
                        mP3Type = "Ai";
                        break;
                    case R.id.radio_button_off3:
                        mPlayer3ET.setText(null);
                        mPlayer3ET.setEnabled(false);
                        mP3Type = "Off";
                        break;
                }

            }
        });

        mRadioGroup4 = (RadioGroup) findViewById(R.id.radio_group_player4);
        mRadioGroup4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button_player4:
                        mPlayer4ET.setEnabled(true);
                        mPlayer4ET.setText("Human 4");
                        mP4Type = "Human";
                        break;
                    case R.id.radio_button_ai4:
                        mPlayer4ET.setText("Computer 4");
                        mPlayer4ET.setEnabled(false);
                        mP4Type = "Ai";
                        break;
                    case R.id.radio_button_off4:
                        mPlayer4ET.setText(null);
                        mPlayer4ET.setEnabled(false);
                        mP4Type = "Off";
                        break;
                }

            }
        });

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mRadioGroup1.getCheckedRadioButtonId() == R.id.radio_button_off1) {
                    p1 = 0;
                } else p1 = 1;
                if (mRadioGroup2.getCheckedRadioButtonId() == R.id.radio_button_off2) {
                    p2 = 0;
                } else p2 = 1;
                if (mRadioGroup3.getCheckedRadioButtonId() == R.id.radio_button_off3) {
                    p3 = 0;
                } else p3 = 1;
                if (mRadioGroup4.getCheckedRadioButtonId() == R.id.radio_button_off4) {
                    p4 = 0;
                } else p4 = 1;

                p = p1 + p2 + p3 + p4;


                if (p < 2) {
                    PopupDialog.showMsg(PlayActivity.this, R.string.cant_start, R.string.not_enough_players);
                } else {

                    mPlayer1 = mPlayer1ET.getText().toString();
                    mPlayer2 = mPlayer2ET.getText().toString();
                    mPlayer3 = mPlayer3ET.getText().toString();
                    mPlayer4 = mPlayer4ET.getText().toString();

                    if (mP1Type != "Human" && mP2Type != "Human" && mP3Type != "Human" && mP4Type != "Human") {
                        Intent startGame = AiGameActivity.getNames(PlayActivity.this, mPlayer1, mP1Type, mPlayer2, mP2Type, mPlayer3, mP3Type, mPlayer4, mP4Type, p);
                        startActivity(startGame);
                    } else {
                        Intent startGame = GameActivity.getNames(PlayActivity.this, mPlayer1, mP1Type, mPlayer2, mP2Type, mPlayer3, mP3Type, mPlayer4, mP4Type, p);
                        startActivity(startGame);
                }
            }
        }
    }

    );

    mCancelButton.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){


        Intent cancelGame = new Intent(PlayActivity.this, MainActivity.class);
        cancelGame.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(cancelGame);
        finish();
    }
    }

    );

}
}