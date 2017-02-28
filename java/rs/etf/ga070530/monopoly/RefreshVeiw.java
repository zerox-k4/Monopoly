package rs.etf.ga070530.monopoly;

import android.content.Context;
import android.widget.Button;

import rs.etf.ga070530.monopoly.Model.Player;

public class RefreshVeiw {

    public static void refreshMoney(Context context, Player p){
        p.getCashView().setText("Money: $ " + Integer.toString(p.getMoney(context)));
    }

    public static void refreshButtons(int id, Button b1, Button b2, Button b3, Button b4){
        switch (id){
            case 0:
                b1.setEnabled(true);
                b2.setEnabled(false);
                b3.setEnabled(false);
                b4.setEnabled(false);
                break;
            case 1:
                b1.setEnabled(false);
                b2.setEnabled(true);
                b3.setEnabled(false);
                b4.setEnabled(false);
                break;
            case 2:
                b1.setEnabled(false);
                b2.setEnabled(false);
                b3.setEnabled(true);
                b4.setEnabled(false);
                break;
            case 3:
                b1.setEnabled(false);
                b2.setEnabled(false);
                b3.setEnabled(false);
                b4.setEnabled(true);
                break;
        }
    }
}
