package rs.etf.ga070530.monopoly;

import android.content.Context;
import android.widget.LinearLayout;

import rs.etf.ga070530.monopoly.Model.Card;
import rs.etf.ga070530.monopoly.Model.Player;
import rs.etf.ga070530.monopoly.Model.Settings;
import rs.etf.ga070530.monopoly.database.MonopolyOpenHelper;

public class PlayerController {

    public static int playerExists(Player player1, Player player2, int playerFlag) {
        if (player1.getName().equals("")) {
            if (player2.getName().equals("")) {
                playerFlag = (playerFlag + 1) % 4;
            }

            playerFlag = (playerFlag + 1) % 4;
        }
        return playerFlag;
    }

    public static void passedStart(Context context, Player player, int oldIndex) {
        Settings settings = MonopolyOpenHelper.getSettings(context);
        int index = player.getCurrentIndex();
        if ((index < oldIndex) && (index != 30)) {
            if (index == 0) {
                SpecialCards.transferCash(context, player, settings.getLandOnGoSalary());
            } else {
                SpecialCards.transferCash(context, player, settings.getGoSalary());
            }
        }
    }

    public static void buyCard(final Context context, final Player p, final Card card) {
        SpecialCards.transferCash(context, p, -(card.getPrice()));
//        switch (p.getName()){
//            case
//        }
        new Thread(new Runnable() {
            @Override
            public void run() {
//                CardsDB.insertPlayerCard(context, p.getId(), card.getNumberOfField());
//        p.getCards().add(card.getNumberOfField(), card);
                p.addCard(context, card);
                card.setOwner(p);
            }
        }).start();
    }

    public static void playIfAi(Player p, LinearLayout l) {
        if (p.getType().equals("Ai")) {
            l.performClick();
        } else{
            l.setEnabled(false);
        }
    }
}
