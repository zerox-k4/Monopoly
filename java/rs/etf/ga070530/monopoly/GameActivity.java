package rs.etf.ga070530.monopoly;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import rs.etf.ga070530.monopoly.Model.*;
import rs.etf.ga070530.monopoly.database.*;

public class GameActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "GameActivity";

//    public static final String KEY_P1_INDEX = "Index1";
//    public static final String KEY_P2_INDEX = "Index2";
//    public static final String KEY_P3_INDEX = "Index3";
//    public static final String KEY_P4_INDEX = "Index4";

    public static final String KEY_GAME_ID = "game_id";

    public static final String EXTRA_PLAYER1 = "Player1";
    public static final String EXTRA_PLAYER2 = "Player2";
    public static final String EXTRA_PLAYER3 = "Player3";
    public static final String EXTRA_PLAYER4 = "Player4";
    public static final String EXTRA_PLAYER1_TYPE = "Player1Type";
    public static final String EXTRA_PLAYER2_TYPE = "Player2Type";
    public static final String EXTRA_PLAYER3_TYPE = "Player3Type";
    public static final String EXTRA_PLAYER4_TYPE = "Player4Type";
    public static final String EXTRA_NO_PLAYERS = "NoPlayers";

//    public static final String EXTRA_CARDS_1 = "Cards1";
//    public static final String EXTRA_CARDS_2 = "Cards2";
//    public static final String EXTRA_CARDS_3 = "Cards3";
//    public static final String EXTRA_CARDS_4 = "Cards4";

    private MediaPlayer mMediaPlayer;

    private LinearLayout mRoll;

    private SensorManager mSensorManager;

    private View mPlayer1View, mPlayer2View, mPlayer3View, mPlayer4View;

    private TextView mPlayer1Tv, mPlayer2Tv, mPlayer3Tv, mPlayer4Tv, mP1CashTv, mP2CashTv, mP3CashTv, mP4CashTv;

    private ImageView mDice1Image, mDice2Image;

    private Menu doneMenu;

    private Button mP1Cards, mP2Cards, mP3Cards, mP4Cards;
    private int noPlayers = 0;
    private int mDiceValue = 0;
    private int mDice1Value = 0;
    private int mDice2Value = 0;
    private int playerFlag = 0;
    private int doublesFlag = 0;
    private boolean repeatFlag;
    private boolean played = true;
    private boolean playerDone = true;
    private long gameStartTime;
    private long duration;
    private int gameId;
    private Player winner;

    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private long shakeStartTime;
    private boolean shakeStarted = false;

    public static boolean active;

    private Settings mPlayerSettings;
    public static List<Card> mCards;
    public static Player player1, player2, player3, player4;

    Dice dice1 = new Dice();
    Dice dice2 = new Dice();

    public static Intent getNames(Context packageContext, String player1Name, String p1Type,
                                  String player2Name, String p2Type,
                                  String player3Name, String p3Type,
                                  String player4Name, String p4Type, int noPlayers) {
        Intent i = new Intent(packageContext, GameActivity.class);
        i.putExtra(EXTRA_PLAYER1, player1Name);
        i.putExtra(EXTRA_PLAYER1_TYPE, p1Type);
        i.putExtra(EXTRA_PLAYER2, player2Name);
        i.putExtra(EXTRA_PLAYER2_TYPE, p2Type);
        i.putExtra(EXTRA_PLAYER3, player3Name);
        i.putExtra(EXTRA_PLAYER3_TYPE, p3Type);
        i.putExtra(EXTRA_PLAYER4, player4Name);
        i.putExtra(EXTRA_PLAYER4_TYPE, p4Type);
        i.putExtra(EXTRA_NO_PLAYERS, noPlayers);
        return i;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        CardsDB.reinitiatePCards(this);
        PlayerDB.reinitiatePlayers(this);
        if (MonopolyOpenHelper.chkCards(this)) {
            mCards = CardsDB.initCards(this);
        } else {
            mCards = CardsDB.getCards(this);
            for (int i = 0; i < mCards.size(); i++) {
                mCards.get(i).setOwner(null);
                mCards.get(i).setMortgaged(false);
                mCards.get(i).setHouses(0);
            }
        }
        if (!MonopolyOpenHelper.chkSettings(this)) {
            MonopolyOpenHelper.insertSettings(this, 20000, 2000, 4000);
        }


        mPlayer1Tv = (TextView) findViewById(R.id.id_player1);
        mPlayer2Tv = (TextView) findViewById(R.id.id_player2);
        mPlayer3Tv = (TextView) findViewById(R.id.id_player3);
        mPlayer4Tv = (TextView) findViewById(R.id.id_player4);

        mP1CashTv = (TextView) findViewById(R.id.id_cash_player1);
        mP2CashTv = (TextView) findViewById(R.id.id_cash_player2);
        mP3CashTv = (TextView) findViewById(R.id.id_cash_player3);
        mP4CashTv = (TextView) findViewById(R.id.id_cash_player4);

        mP1Cards = (Button) findViewById(R.id.p1_cards);
        mP2Cards = (Button) findViewById(R.id.p2_cards);
        mP3Cards = (Button) findViewById(R.id.p3_cards);
        mP4Cards = (Button) findViewById(R.id.p4_cards);

        mPlayer1View = findViewById(R.id.player1);
        mPlayer2View = findViewById(R.id.player2);
        mPlayer3View = findViewById(R.id.player3);
        mPlayer4View = findViewById(R.id.player4);

        mMediaPlayer = MediaPlayer.create(GameActivity.this, R.raw.dice_roll);
        mMediaPlayer.setLooping(true);


        mRoll = (LinearLayout) findViewById(R.id.dice_roll);

        mDice1Image = (ImageView) findViewById(R.id.dice_image1);
        mDice2Image = (ImageView) findViewById(R.id.dice_image2);


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);

        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        String p1 = (String) getIntent().getSerializableExtra(EXTRA_PLAYER1);
        String p2 = (String) getIntent().getSerializableExtra(EXTRA_PLAYER2);
        String p3 = (String) getIntent().getSerializableExtra(EXTRA_PLAYER3);
        String p4 = (String) getIntent().getSerializableExtra(EXTRA_PLAYER4);
        String p1t = (String) getIntent().getSerializableExtra(EXTRA_PLAYER1_TYPE);
        String p2t = (String) getIntent().getSerializableExtra(EXTRA_PLAYER2_TYPE);
        String p3t = (String) getIntent().getSerializableExtra(EXTRA_PLAYER3_TYPE);
        String p4t = (String) getIntent().getSerializableExtra(EXTRA_PLAYER4_TYPE);
        noPlayers = (Integer) getIntent().getSerializableExtra(EXTRA_NO_PLAYERS);

        mPlayer1Tv.setText(p1);
        mPlayer2Tv.setText(p2);
        mPlayer3Tv.setText(p3);
        mPlayer4Tv.setText(p4);

        mPlayerSettings = MonopolyOpenHelper.getSettings(GameActivity.this);
        int initialCash = mPlayerSettings.getInitialCash();

        /** Initial money for players 2 and 3 hard-coded to $6 for testing   */
        player1 = new Player(GameActivity.this, 1, p1, p1t, mPlayer1View, mP1CashTv, initialCash);
        player2 = new Player(GameActivity.this, 2, p2, p2t, mPlayer2View, mP2CashTv, 6);
        player3 = new Player(GameActivity.this, 3, p3, p3t, mPlayer3View, mP3CashTv, 6);
        player4 = new Player(GameActivity.this, 4, p4, p4t, mPlayer4View, mP4CashTv, initialCash);


        if (savedInstanceState != null) {
            gameId = savedInstanceState.getInt(KEY_GAME_ID, 0);
        } else {
            gameId = MonopolyOpenHelper.setId(GameActivity.this);
            gameStartTime = System.currentTimeMillis();
            MonopolyOpenHelper.initStatistic(GameActivity.this, gameId, player1.getName() + ", " +
                    player2.getName() + "," + player3.getName() + ", " + player4.getName(), gameStartTime);
        }

        player1.setPlayer(this, mPlayer1Tv, mP1Cards);
        player2.setPlayer(this, mPlayer2Tv, mP2Cards);
        player3.setPlayer(this, mPlayer3Tv, mP3Cards);
        player4.setPlayer(this, mPlayer4Tv, mP4Cards);


        mP1Cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = CardsActivity.getIntent(GameActivity.this, 1);
                startActivity(i);
            }
        });

        mP2Cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = CardsActivity.getIntent(GameActivity.this, 2);
                startActivity(i);
            }
        });

        mP3Cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = CardsActivity.getIntent(GameActivity.this, 3);
                startActivity(i);
            }
        });

        mP4Cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = CardsActivity.getIntent(GameActivity.this, 4);
                startActivity(i);
            }
        });


        playerFlag = PlayerController.playerExists(player1, player2, playerFlag);
        RefreshVeiw.refreshButtons(playerFlag, mP1Cards, mP2Cards, mP3Cards, mP4Cards);
        gamePlay();
        mRoll.setEnabled(true);

        mRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (playerFlag) {

                    case 0:
//                        aiRollEnable(player1);
                        noPlayers = noPlayers - (playerMove(player1, player2, player3));
                        player1.getView().setX(mCards.get(player1.getCurrentIndex()).getX());
                        player1.getView().setY(mCards.get(player1.getCurrentIndex()).getY());
                        aiRollDisable(player1);
                        break;
                    case 1:
//                        aiRollEnable(player2);
                        noPlayers = noPlayers - (playerMove(player2, player3, player4));
                        player2.getView().setX(mCards.get(player2.getCurrentIndex()).getX());
                        player2.getView().setY(mCards.get(player2.getCurrentIndex()).getY() - 30);
                        aiRollDisable(player2);
                        break;
                    case 2:
//                        aiRollEnable(player3);
                        noPlayers = noPlayers - (playerMove(player3, player4, player1));
                        player3.getView().setX(mCards.get(player3.getCurrentIndex()).getX() + 30);
                        player3.getView().setY(mCards.get(player3.getCurrentIndex()).getY());
                        aiRollDisable(player3);
                        break;
                    case 3:
//                        aiRollEnable(player4);
                        noPlayers = noPlayers - (playerMove(player4, player1, player2));
                        player4.getView().setX(mCards.get(player4.getCurrentIndex()).getX() + 30);
                        player4.getView().setY(mCards.get(player4.getCurrentIndex()).getY() - 30);
                        aiRollDisable(player4);
                        break;
                }
            }
        });

    }

    private void aiRollDisable(Player p) {
        if (p.getType().equals("Human")) {
            mRoll.setEnabled(false);
        } else {
            gamePlay();
        }
    }

    private void aiRollEnable(Player p) {
        if (p.getType().equals("Ai")) {
            mRoll.setEnabled(true);
        }
    }

    private void gamePlay() {
        switch (playerFlag) {
            case 0:
                PlayerController.playIfAi(player1, mRoll);
                break;
            case 1:
                PlayerController.playIfAi(player2, mRoll);
                break;
            case 2:
                PlayerController.playIfAi(player3, mRoll);
                break;
            case 3:
                PlayerController.playIfAi(player4, mRoll);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        PopupDialog.goingBack(this);
    }

    private int playerMove(Player player1, Player player2, Player player3) {

        if (noPlayers < 2 || player1.getMoney(GameActivity.this) > 30000) {
            if (this.player1.getMoney(this) > 30000) {
                winner = this.player1;
            } else if (this.player2.getMoney(this) > 30000) {
                winner = this.player2;
            } else if (this.player3.getMoney(this) > 30000) {
                winner = this.player3;
            } else if (this.player4.getMoney(this) > 30000) {
                winner = this.player4;
            } else if (!this.player1.getName().equals("") & this.player2.getName().equals("")
                    & this.player3.getName().equals("") & this.player4.getName().equals("")) {
                winner = this.player1;
            } else if (this.player1.getName().equals("") & !this.player2.getName().equals("")
                    & this.player3.getName().equals("") & this.player4.getName().equals("")) {
                winner = this.player2;
            } else if (this.player1.getName().equals("") & this.player2.getName().equals("")
                    & !this.player3.getName().equals("") & this.player4.getName().equals("")) {
                winner = this.player3;
            } else if (this.player1.getName().equals("") & this.player2.getName().equals("")
                    & this.player3.getName().equals("") & !this.player4.getName().equals("")) {
                winner = this.player4;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    duration = (System.currentTimeMillis() - gameStartTime) / 1000;
                    MonopolyOpenHelper.addWinner(GameActivity.this, gameId, winner.getName(), duration, winner.getMoney(GameActivity.this));
                }
            }).start();
        }

        mDice1Value = dice1.roll();
        mDice2Value = dice2.roll();
        if (mDice1Value == mDice2Value) {
            if (player1.getMoney(GameActivity.this) <= 0) {
                repeatFlag = false;
            } else {
                repeatFlag = true;
            }
        }
        DiceView.generateImage(mDice1Image, mDice1Value);
        DiceView.generateImage(mDice2Image, mDice2Value);

        mDiceValue = mDice1Value + mDice2Value;
        int oldIndex1 = player1.getCurrentIndex();
        player1.setCurrentIndex((player1.getCurrentIndex() + mDiceValue) % 40);
        if (repeatFlag) {
            doublesFlag++;
            if (doublesFlag == 3) {
                player1.setCurrentIndex(30);
                playerFlag = (PlayerController.playerExists(player2, player3, playerFlag) + 1) % 4;
                doublesFlag = 0;
            } else {
                PlayerController.passedStart(GameActivity.this, player1, oldIndex1);
            }
        } else {
            PlayerController.passedStart(GameActivity.this, player1, oldIndex1);
            playerFlag = (PlayerController.playerExists(player2, player3, playerFlag) + 1) % 4;
            doublesFlag = 0;
        }
        repeatFlag = false;
        Toast.makeText(GameActivity.this, player1.getName() + " landed on " + "\"" + mCards.get(player1.getCurrentIndex()).getNameOfField() + "\"", Toast.LENGTH_SHORT).show();
        player1.setCurrentIndex(SpecialCards.checkCard(player1, mCards.get(player1.getCurrentIndex()), GameActivity.this));
        MonopolyOpenHelper.addMove(GameActivity.this, gameId, player1.getName() + " - " + mCards.get(player1.getCurrentIndex()).getNameOfField());
        mRoll.setEnabled(false);
        enableMenu();
        if (player1.getType().equals("Ai")) {
            RefreshVeiw.refreshButtons(playerFlag, mP1Cards, mP2Cards, mP3Cards, mP4Cards);
        }
        if (player1.getMoney(GameActivity.this) < 0) {

            CardsDB.deletePlayerCards(GameActivity.this, player1.getId());
            player1.getView().setVisibility(View.INVISIBLE);
            player1.setName("");
            for(int i = 0; i < mCards.size(); i++){
                if(mCards.get(i).getOwner() == player1){
                    mCards.get(i).setOwner(null);
                }
            }

            player1.setCards(null);
            return 1;
        }

        return 0;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        doneMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done_item:
                mRoll.setEnabled(true);
                RefreshVeiw.refreshButtons(playerFlag, mP1Cards, mP2Cards, mP3Cards, mP4Cards);
                gamePlay();
                playerDone = true;
                item.setIcon(getResources().getDrawable(R.drawable.ic_action_done_false));
                item.setEnabled(false);
                mRoll.setEnabled(true);
                break;
            case R.id.sens_item:
                PopupDialog.sensorPopup(this);
        }
        return true;
    }

    public void enableMenu() {
        doneMenu.findItem(R.id.done_item).setIcon(R.drawable.ic_action_done_true);
        doneMenu.findItem(R.id.done_item).setEnabled(true);
    }

    public void disableMenu() {
        doneMenu.findItem(R.id.done_item).setIcon(R.drawable.ic_action_done_false);
        doneMenu.findItem(R.id.done_item).setEnabled(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.getInt(KEY_GAME_ID, gameId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.stop();
//        active = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        int sensorPref = SettingsActivity.getSensitivity(GameActivity.this);
        float accelPref = sensorPref;
        float deltaMin = accelPref/2.5f;
        float deltaMax = deltaMin*2;

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
        float delta = mAccelCurrent - mAccelLast;
        long currTime = System.currentTimeMillis();
        mAccel = mAccel * 0.9f + delta;

        if (mAccel > accelPref && Math.abs(delta) < deltaMax && Math.abs(delta) > deltaMin && playerDone) {
            Log.i(TAG + "-DELTA", Math.abs(delta) + " " + mAccel);
            if (!shakeStarted) {
                mMediaPlayer.start();
                shakeStarted = true;
            }
            shakeStartTime = currTime;
        } else {
            if (currTime - shakeStartTime > 1000 && shakeStarted) {
                shakeStarted = false;
                mRoll.performClick();
                shakeStartTime = currTime;
                mAccelLast = SensorManager.GRAVITY_EARTH;
                mAccelCurrent = SensorManager.GRAVITY_EARTH;
                mAccel = 0;
                mMediaPlayer.pause();
                playerDone = false;

                enableMenu();
            }

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(this.mPlayer1Tv.isEnabled()){
            RefreshVeiw.refreshMoney(this, player1);
        }
        if(this.mPlayer2Tv.isEnabled()){
            RefreshVeiw.refreshMoney(this, player2);
        }
        if(this.mPlayer3Tv.isEnabled()){
            RefreshVeiw.refreshMoney(this, player3);
        }
        if(this.mPlayer4Tv.isEnabled()){
            RefreshVeiw.refreshMoney(this, player4);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
