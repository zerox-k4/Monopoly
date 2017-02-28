package rs.etf.ga070530.monopoly;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import rs.etf.ga070530.monopoly.Model.*;
import rs.etf.ga070530.monopoly.database.*;

public class AiGameActivity extends AppCompatActivity {

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

    private LinearLayout mRoll;

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
    private boolean playing = true;
    private long gameStartTime;
    private long duration;
    private int gameId;
    private Player winner = null;

    private Settings mPlayerSettings;
    private List<Card> mCards;
    private Player player1, player2, player3, player4;

    Dice dice1 = new Dice();
    Dice dice2 = new Dice();

    public static Intent getNames(Context packageContext, String player1Name, String p1Type,
                                  String player2Name, String p2Type,
                                  String player3Name, String p3Type,
                                  String player4Name, String p4Type, int noPlayers) {
        Intent i = new Intent(packageContext, AiGameActivity.class);
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


        mRoll = (LinearLayout) findViewById(R.id.dice_roll);

        mDice1Image = (ImageView) findViewById(R.id.dice_image1);
        mDice2Image = (ImageView) findViewById(R.id.dice_image2);

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

        mPlayerSettings = MonopolyOpenHelper.getSettings(AiGameActivity.this);
        int initialCash = mPlayerSettings.getInitialCash();

        player1 = new Player(AiGameActivity.this, 1, p1, p1t, mPlayer1View, mP1CashTv, initialCash);
        player2 = new Player(AiGameActivity.this, 2, p2, p2t, mPlayer2View, mP2CashTv, 6);
        player3 = new Player(AiGameActivity.this, 3, p3, p3t, mPlayer3View, mP3CashTv, 6);
        player4 = new Player(AiGameActivity.this, 4, p4, p4t, mPlayer4View, mP4CashTv, initialCash);


        if (savedInstanceState != null) {
            gameId = savedInstanceState.getInt(KEY_GAME_ID, 0);
        } else {
            gameId = MonopolyOpenHelper.setId(AiGameActivity.this);
            gameStartTime = System.currentTimeMillis();
            MonopolyOpenHelper.initStatistic(AiGameActivity.this, gameId, player1.getName() + ", " +
                    player2.getName() + "," + player3.getName() + ", " + player4.getName(), gameStartTime);
        }

        player1.setPlayer(this, mPlayer1Tv, mP1Cards);
        player2.setPlayer(this, mPlayer2Tv, mP2Cards);
        player3.setPlayer(this, mPlayer3Tv, mP3Cards);
        player4.setPlayer(this, mPlayer4Tv, mP4Cards);


        playerFlag = PlayerController.playerExists(player1, player2, playerFlag);
        RefreshVeiw.refreshButtons(playerFlag, mP1Cards, mP2Cards, mP3Cards, mP4Cards);
        gamePlay();
        mRoll.setEnabled(true);


        mRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (playerFlag) {

                    case 0:
                        playerMove(player1, player2, player3);
                        updateUI();
                        break;
                    case 1:
                        playerMove(player2, player3, player4);
                        updateUI();
                        break;
                    case 2:
                        playerMove(player3, player4, player1);
                        updateUI();
                        break;
                    case 3:
                        playerMove(player4, player1, player2);
                        updateUI();
                        break;
                }
            }
        });

    }

    private void updateUI() {
        player1.getView().setX(mCards.get(player1.getCurrentIndex()).getX());
        player1.getView().setY(mCards.get(player1.getCurrentIndex()).getY());
        player2.getView().setX(mCards.get(player2.getCurrentIndex()).getX());
        player2.getView().setY(mCards.get(player2.getCurrentIndex()).getY() - 30);
        player3.getView().setX(mCards.get(player3.getCurrentIndex()).getX() + 30);
        player3.getView().setY(mCards.get(player3.getCurrentIndex()).getY());
        player4.getView().setX(mCards.get(player4.getCurrentIndex()).getX() + 30);
        player4.getView().setY(mCards.get(player4.getCurrentIndex()).getY() - 30);
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

    private void playerMove(final Player player1, final Player player2, final Player player3) {

        if (noPlayers < 2 || player1.getMoney(AiGameActivity.this) > 30000) {
            playing = false;
            updateUI();
            if (this.player1.getMoney(this) > 30000) {
                winner = this.player1;
            } else if (this.player2.getMoney(this) > 30000) {
                winner = this.player2;
            } else if (this.player3.getMoney(this) > 30000) {
                winner = this.player3;
            } else if (this.player4.getMoney(this) > 30000) {
                winner = this.player4;
            } else if (this.player1.getName() != "" & this.player2.getName().equals("")
                    & this.player3.getName().equals("") & this.player4.getName().equals("")) {
                winner = this.player1;
            } else if (this.player1.getName().equals("") & this.player2.getName() != ""
                    & this.player3.getName().equals("") & this.player4.getName().equals("")) {
                winner = this.player2;
            } else if (this.player1.getName().equals("") & this.player2.getName().equals("")
                    & this.player3.getName() != "" & this.player4.getName().equals("")) {
                winner = this.player3;
            } else if (this.player1.getName().equals("") & this.player2.getName().equals("")
                    & this.player3.getName().equals("") & this.player4.getName() != "") {
                winner = this.player4;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    duration = (System.currentTimeMillis() - gameStartTime) / 1000;
                    MonopolyOpenHelper.addWinner(AiGameActivity.this, gameId, winner.getName(), duration, winner.getMoney(AiGameActivity.this));
                }
            }).start();

            PopupDialog.announceWinner(AiGameActivity.this, winner);
        }

        AsyncTask<Void, Void, Integer> task = new AsyncTask<Void, Void, Integer>() {
            @Override
            protected void onPostExecute(Integer aVoid) {
                super.onPostExecute(aVoid);


                DiceView.generateImage(mDice1Image, mDice1Value);
                DiceView.generateImage(mDice2Image, mDice2Value);
                player1.setCurrentIndex(SpecialCards.checkCard(player1, mCards.get(player1.getCurrentIndex()), AiGameActivity.this));
                updateUI();
                MonopolyOpenHelper.addMove(AiGameActivity.this, gameId, player1.getName() + " - " + mCards.get(player1.getCurrentIndex()).getNameOfField());
                mRoll.setEnabled(false);
                enableMenu();
                if (player1.getMoney(AiGameActivity.this) <= 0) {
                    noPlayers--;
                    CardsDB.deletePlayerCards(AiGameActivity.this, player1.getId());
                    player1.getView().setVisibility(View.INVISIBLE);
                    player1.setName("");
                    for(int i = 0; i < mCards.size(); i++){
                        if(mCards.get(i).getOwner() == player1){
                            mCards.get(i).setOwner(null);
                        }
                    }

                    player1.setCards(null);

                }
                if (player1.getType().equals("Ai")) {
                    RefreshVeiw.refreshButtons(playerFlag, mP1Cards, mP2Cards, mP3Cards, mP4Cards);
                }
                updateUI();
                gamePlay();
            }

            @Override
            protected Integer doInBackground(Void... params) {

                mDice1Value = dice1.roll();
                mDice2Value = dice2.roll();
                if (mDice1Value == mDice2Value) {
                    if (player1.getMoney(AiGameActivity.this) <= 0) {
                        repeatFlag = false;
                    } else {
                        repeatFlag = true;
                    }
                }

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
                        PlayerController.passedStart(AiGameActivity.this, player1, oldIndex1);
                    }
                } else {
                    PlayerController.passedStart(AiGameActivity.this, player1, oldIndex1);
                    playerFlag = (PlayerController.playerExists(player2, player3, playerFlag) + 1) % 4;
                    doublesFlag = 0;
                }
                repeatFlag = false;
                if (player1.getMoney(AiGameActivity.this) < 0) {
                    return 1;
                }

                return 0;
            }
        };
        if (playing) {
            task.execute();
        }

    }

    @Override
    public void onBackPressed() {
        PopupDialog.goingBack(this);
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
                break;
        }
        return true;
    }

    public void enableMenu() {
        doneMenu.findItem(R.id.done_item).setIcon(R.drawable.ic_action_done_true);
        doneMenu.findItem(R.id.done_item).setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
