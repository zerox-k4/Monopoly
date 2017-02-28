package rs.etf.ga070530.monopoly;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rs.etf.ga070530.monopoly.Model.Card;
import rs.etf.ga070530.monopoly.Model.Player;
import rs.etf.ga070530.monopoly.database.CardsDB;
import rs.etf.ga070530.monopoly.database.PlayerDB;

public class CardsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String EXTRA_PLAYER_ID = "player_id";

    private ListView mPlayerListView;
    private ListView mOpponentListView;

    private TextView mNameTextView;
    private Spinner mOpponentSpinner;

    private Button mConfirmButton;
    private RadioGroup mRadioGroup;

    private List<Player> mOpponents = new ArrayList<>();
    private List<String> mOpponentNames = new ArrayList<>();
    private List<Integer> mCards = new ArrayList();
    private List<Card> mCardName = new ArrayList();
    private List<Integer> mOpponentCards = new ArrayList();
    private ArrayList<Card> mCardsNameOpponent;
    private List<Card> mDefaultCards = new ArrayList<>();

    TradeCardsAdapter mOpponentAdapter;

    private int oId;
    Player player;


    int p1Id = 1, p2Id = 2, p3Id = 3, p4Id = 4;

    public static Intent getIntent(Context context, int playerId) {
        Intent i = new Intent(context, CardsActivity.class);
        i.putExtra(EXTRA_PLAYER_ID, playerId);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        final int playerId = (Integer) getIntent().getSerializableExtra(EXTRA_PLAYER_ID);

        mDefaultCards = CardsDB.getCards(CardsActivity.this);
        mOpponents = PlayerDB.getPlayers(CardsActivity.this, playerId);

        mCards = CardsDB.getPlayerCards(CardsActivity.this, playerId);
        for (int i = 0; i < mCards.size(); i++) {
            Card c = mDefaultCards.get(mCards.get(i));
            mCardName.add(c);
        }

        player = switchPlayer(this, playerId);

        for (int i = 0; i < mOpponents.size(); i++) {
            String s = mOpponents.get(i).getName();
            mOpponentNames.add(s);
        }


        mConfirmButton = (Button) findViewById(R.id.card_confirm_button);
        mConfirmButton.setEnabled(false);

        mPlayerListView = (ListView) findViewById(R.id.cards_frame_player);
        mOpponentListView = (ListView) findViewById(R.id.cards_frame_opponent);
        mNameTextView = (TextView) findViewById(R.id.cards_frame_player_title);
        mNameTextView.setText(player.getName() + ": $" + String.valueOf(player.getMoney(CardsActivity.this)));

        final CardsAdapter mPlayerAdapter = new CardsAdapter(CardsActivity.this, mCardName, playerId);
        final TradeCardsAdapter mPlayerTrAdapter = new TradeCardsAdapter(CardsActivity.this, mCardName);
        final ArrayAdapter mOpponentNamesAdapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mOpponentNames);

        mOpponentSpinner = (Spinner) findViewById(R.id.cards_frame_opponent_spinner);
        mOpponentSpinner.setOnItemSelectedListener(this);
        mOpponentNamesAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mOpponentSpinner.setAdapter(mOpponentNamesAdapter);
        mOpponentSpinner.setEnabled(false);

        mPlayerListView.setAdapter(mPlayerAdapter);
        mPlayerListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Card card = mCardName.get(position);
                Toast.makeText(CardsActivity.this, card.getNameOfField() + " clicked", Toast.LENGTH_SHORT).show();
                mNameTextView.setText(player.getName() + ": $" + String.valueOf(player.getMoney(CardsActivity.this)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mOpponentListView.setEnabled(false);
        mOpponentListView.setVisibility(View.INVISIBLE);

        mRadioGroup = (RadioGroup) findViewById(R.id.card_option_radio_group);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.card_option_manage_radio:
                        mPlayerListView.setAdapter(mPlayerAdapter);
                        mOpponentListView.setEnabled(false);
                        mOpponentSpinner.setEnabled(false);
                        mOpponentListView.setVisibility(View.INVISIBLE);
                        mConfirmButton.setEnabled(false);
                        break;
                    case R.id.card_option_trade_radio:
                        mPlayerListView.setAdapter(mPlayerTrAdapter);
                        mOpponentListView.setEnabled(true);
                        mOpponentSpinner.setEnabled(true);
                        mConfirmButton.setEnabled(true);
                        mOpponentListView.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });


        mConfirmButton = (Button) findViewById(R.id.card_confirm_button);
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transferCards(mPlayerTrAdapter, player);


            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(parent.getContext(),
//                "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
//                Toast.LENGTH_SHORT).show();
        mCardsNameOpponent = new ArrayList();
        oId = mOpponents.get(position).getId();

        mOpponentCards = CardsDB.getPlayerCards(CardsActivity.this, oId);
        for (int i = 0; i < mOpponentCards.size(); i++) {
            Card c = mDefaultCards.get(mOpponentCards.get(i));
            mCardsNameOpponent.add(c);
        }

        mOpponentAdapter = new TradeCardsAdapter(CardsActivity.this, mCardsNameOpponent);
        mOpponentListView.setAdapter(mOpponentAdapter);
        mOpponentListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Card card = mCardsNameOpponent.get(position);
                Toast.makeText(CardsActivity.this, card.getNameOfField() + " clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void transferCards(TradeCardsAdapter mPlayerTrAdapter, final Player p) {
        final Player opp = switchPlayer(this, oId);
        final List<Card> list1 = mPlayerTrAdapter.getSelectedCards();
        final List<Card> list2 = mOpponentAdapter.getSelectedCards();
//        Log.i("SELECTED", mPlayerTrAdapter.getSelectedCards()+"");

        if (list1.size() > 0 && list2.size() > 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < list1.size(); i++) {
                        CardsDB.transferCards(CardsActivity.this, p.getId(), oId, list1.get(i).getNumberOfField());
                        GameActivity.mCards.get(list1.get(i).getNumberOfField()).setOwner(opp);
                    }

                    for (int i = 0; i < list2.size(); i++) {
                        CardsDB.transferCards(CardsActivity.this, oId, p.getId(), list2.get(i).getNumberOfField());
                        GameActivity.mCards.get(list2.get(i).getNumberOfField()).setOwner(player);
                    }

                }
            }).start();
            onBackPressed();
        } else {
            PopupDialog.showMsg(CardsActivity.this, R.string.transfer_fail_title, R.string.transfer_fail_msg);
        }
    }

    public static Player switchPlayer(Context context, int playerId) {
        Player player = null;
        int p1Id = PlayerDB.getPlayer(context, playerId);
        switch (p1Id) {
            case 1:
                player = GameActivity.player1;
                break;
            case 2:
                player = GameActivity.player2;
                break;
            case 3:
                player = GameActivity.player3;
                break;
            case 4:
                player = GameActivity.player4;
                break;
        }
        return player;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
