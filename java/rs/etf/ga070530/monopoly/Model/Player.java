package rs.etf.ga070530.monopoly.Model;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import rs.etf.ga070530.monopoly.database.CardsDB;
import rs.etf.ga070530.monopoly.database.PlayerDB;

public class Player {

    private Context mContext;
    private Card card;
    private int mId;
    private String mName;
    private String mType;
    private View mView;
    private TextView mCashView;
    private int mCurrentIndex;
    private int mMoney;
    private ArrayList<Card> mCards;

    public Player(Context context, int id, String name, String type, View view, TextView cashView, int money) {
        mContext = context;
        mId = id;
        mName = name;
        mType = type;
        mView = view;
        mCurrentIndex = 0;
//        setMoney(context, id, money);
        mMoney = money;
        mCashView = cashView;
        mCards = new ArrayList<>();
        for (int i = 0; i < 39; i++) {
            mCards.add(card);
        }
    }

    public Player(Context context, int id, String name, String type, int money){
        mId = id;
        mName = name;
        mType = type;
//        setMoney(context, id, money);
        mMoney = money;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public View getView() {
        return mView;
    }

    public void setView(View view) {
        mView = view;
    }

    public int getMoney(Context context) {

        return mMoney = PlayerDB.getMoney(context, mId);
    }

    public void setMoney(Context context) {

                PlayerDB.setMoney(context, mId, mMoney);
//        mMoney = money;
    }

    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        mCurrentIndex = currentIndex;
    }

    public ArrayList<Card> getCards() {
        return mCards;
    }

    public void setCards(ArrayList<Card> cards) {
        mCards = cards;
    }

    public void addCard(Context context, Card card) {
        CardsDB.insertPlayerCard(context, mId, card.getNumberOfField());
//        mCards.add(card.getNumberOfField(), card);
    }

    public TextView getCashView() {
        return mCashView;
    }

    public void setPlayer(Context context, TextView nameTv, Button cardsB) {
        if (mName.equals("")) {
            mView.setVisibility(View.INVISIBLE);
            nameTv.setEnabled(false);
            cardsB.setEnabled(false);
        } else {
            mCashView.setText("Money: $ " + Integer.toString(mMoney));
            PlayerDB.setPlayer(context, mId, mName, mType, mMoney);
        }
    }
}