package rs.etf.ga070530.monopoly;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import rs.etf.ga070530.monopoly.Model.Card;

public class CardLogic {
    private Context mContext;
    private List<Card> mCards;
    private Card mCard;

    public CardLogic(Context context, List<Card> cards, Card card) {
        mContext = context;
        mCards = cards;
        mCard = card;
    }

    public static List<Card> groups(){
        List<Card> groupCards= new ArrayList<>();

        return groupCards;
    }

    public static boolean checkGroup(){


        return true;
    }
}
