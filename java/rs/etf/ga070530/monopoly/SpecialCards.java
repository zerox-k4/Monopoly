package rs.etf.ga070530.monopoly;

import android.content.Context;

import rs.etf.ga070530.monopoly.Model.Card;
import rs.etf.ga070530.monopoly.Model.Dice;
import rs.etf.ga070530.monopoly.Model.Player;
import rs.etf.ga070530.monopoly.Model.Settings;
import rs.etf.ga070530.monopoly.database.MonopolyOpenHelper;
import rs.etf.ga070530.monopoly.database.PlayerDB;
public class SpecialCards {


    private static Card[] mChances = new Card[]{
            new Card(0, R.string.chance_back),
            new Card(1, R.string.chance_istanbul),
            new Card(2, R.string.chance_jail),
            new Card(3, R.string.chance_montreal),
            new Card(4, R.string.chance_london),
            new Card(5, R.string.chance_start)

    };

    private static Card[] mCommunityChest = new Card[]{
            new Card(0, R.string.community_chest_bank_error),
            new Card(1, R.string.community_chest_beauty),
            new Card(2, R.string.community_consultancy),
            new Card(3, R.string.community_hospital_tax),
            new Card(4, R.string.community_school_tax),
            new Card(5, R.string.community_income_tax)

    };

    public static int checkCard(Player p, Card card, Context activity) {
        Settings settings = MonopolyOpenHelper.getSettings(activity);
        int index = p.getCurrentIndex();
        if ((index == 2) || (index == 17) || (index == 33)) {
            int value = getValue(activity, p, R.string.community_chest_title, mCommunityChest);
            switch (value) {
                case 0:
                    transferCash(activity, p, 2000);
                    break;
                case 1:
                    transferCash(activity, p, 100);
                    break;
                case 2:
                    transferCash(activity, p, 250);
                    break;
                case 3:
                    transferCash(activity, p, -1000);
                    break;
                case 4:
                    transferCash(activity, p, -150);
                    break;
                case 5:
                    transferCash(activity, p, 200);
                    break;
            }

        }
        if ((index == 7) || (index == 22) || (index == 36)) {
            int value = getValue(activity,p, R.string.chance_title, mChances);
            switch (value) {
                case 0:
                    index = index - 3;
                    if (index == 4)
                        transferCash(activity, p, -2000);
//                    if(index == 33){
//                        index = checkCard(p, card, activity);
//                    }
                    break;
                case 1:
                    if ((p.getCurrentIndex() == 22) || (p.getCurrentIndex() == 36)) {
                        transferCash(activity,p, settings.getGoSalary());
                    }
                    index = 11;
                    buyPayOrPass(p, card, activity);
                    break;
                case 2:
                    index = 10;
                    break;
                case 3:
                    index = 39;
                    buyPayOrPass(p, card, activity);
                    break;
                case 4:
                    if (p.getCurrentIndex() == 36) {
                        transferCash(activity, p, settings.getGoSalary());
                    }
                    index = 24;
                    buyPayOrPass(p, card, activity);
                    break;
                case 5:
                    index = 0;
                    transferCash(activity, p, settings.getLandOnGoSalary());
                    break;
            }

        }
        if (index == 30) {
            if (p.getType().equals("Human")) {
                PopupDialog.showMsg(activity, R.string.jail_title, R.string.jail);
            }
            index = 10;
        }
        if (index == 4)
            transferCash(activity, p, -2000);
        if (index == 38)
            transferCash(activity, p, -1000);

        buyPayOrPass(p, card, activity);
        return index;
    }

    private static void buyPayOrPass(Player p, Card card, Context activity) {
        if (card.getIsBuyable()) {
            if (card.getOwner() == null) {
                if (p.getType().equals("Ai")) {
                    if (p.getMoney(activity) > card.getPrice()) {
                        PlayerController.buyCard(activity, p, card);
                    }
                } else if (p.getType().equals("Human")) {
                    PopupDialog.showBuyingMsg(activity, p, card);
                }
            } else {
                transferCash(activity, card.getOwner(), (card.getPrice() / 10)*card.getHouses()+1);
                transferCash(activity, p, -(card.getPrice() / 10)*card.getHouses()+1);
            }
        }
    }

    public static void transferCash(Context context, Player p, int moreCash) {
        int cash = p.getMoney(context) + moreCash;
        p.setMoney(context);
        PlayerDB.setMoney(context, p.getId(), cash);
        if(GameActivity.active){
            RefreshVeiw.refreshMoney(context, p);
        }
    }

    private static int getValue(Context activity, Player p, int type, Card[] cards) {
        Dice mDice = new Dice();
        int value = mDice.roll() - 1;
        if (p.getType().equals("Human")) {
            PopupDialog.showMsg(activity, type, cards[value].getResNameOfCard());
        }
        return value;
    }
}