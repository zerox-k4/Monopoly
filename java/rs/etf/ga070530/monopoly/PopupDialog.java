package rs.etf.ga070530.monopoly;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.PorterDuff;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.zip.Inflater;

import rs.etf.ga070530.monopoly.Model.Card;
import rs.etf.ga070530.monopoly.Model.Player;
import rs.etf.ga070530.monopoly.database.MonopolyOpenHelper;

public class PopupDialog {

    private static final int SENSOR_MAX = 15;
    private static int newSens = 0;

    public static void showMsg(final Context activity, int title, int msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle(title);
        alert.setMessage(msg);
        alert.setPositiveButton(android.R.string.ok, null);
        alert.show();
    }

    public static void showBuyingMsg(final Context activity, final Player p, final Card card) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        String cardName = card.getNameOfField();

        if (p.getMoney(activity) > card.getPrice()) {
            alert.setTitle(R.string.for_sale);
            alert.setMessage("Do you want to buy " + cardName + " for $" + Integer.toString(card.getPrice()) + "?");
            alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PlayerController.buyCard(activity, p, card);
                    dialog.dismiss();
                }
            });
            alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
        } else {
            alert.setTitle(R.string.insufficient_funds_title);
            alert.setMessage(R.string.insufficient_funds_msg);
            alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }
    }

    public static int sensorPopup(final Context context) {
        try {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.sensor_settings, (ViewGroup) ((Activity)context).findViewById(R.id.popup_element));
            layout.findViewById(R.id.popup_element);
            layout.setBackgroundColor(0x22ff0000);
            layout.bringToFront();
            final PopupWindow pWindow = new PopupWindow(layout, 400,200, true);
            pWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

            SeekBar bar = (SeekBar)layout.findViewById(R.id.game_sensor_bar);
            bar.setMax(SENSOR_MAX);
            bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int onProgressChanged = 4;
                int viewProgress;
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    onProgressChanged = SENSOR_MAX - progress;
                    viewProgress = progress;

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    Toast.makeText(context, "Max sensitivity initiate at: " + Integer.toString(viewProgress), Toast.LENGTH_SHORT).show();
                    newSens = onProgressChanged;
                }
            });

            Button sensorButton = (Button) layout.findViewById(R.id.game_sensor_button);
            sensorButton.setText(android.R.string.ok);
            sensorButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(newSens > 0){
                        SettingsActivity.setSensitivity(context, newSens);
                    }
                    pWindow.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static void showForeignLandingMsg(final Activity activity, final Player p, final Card card) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        String cardName = card.getNameOfField();

        if (p.getMoney(activity) > card.getPrice()) {
            alert.setTitle(R.string.foreign_land);
            alert.setMessage("Do you want to buy " + cardName + " for $" + Integer.toString(card.getPrice()) + "?");
            alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SpecialCards.transferCash(activity, p, -(card.getPrice()));
                    p.getCards().add(card);
                    card.setOwner(p);
                }
            });
            alert.setNegativeButton(android.R.string.no, null);
            alert.show();
        } else {
            alert.setTitle(R.string.insufficient_funds_title);
            alert.setMessage(R.string.insufficient_funds_msg);
            alert.setPositiveButton(android.R.string.ok, null);
            alert.show();
        }
    }


    public static void goingBack(final Activity activity) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle(R.string.exit_game);
        alert.setMessage(R.string.exit_game_msg);
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
//                CardsDB.deleteCards(activity);
                activity.finish();
            }
        });
        alert.setNegativeButton(android.R.string.no, null);
        alert.show();
    }

    public static void announceWinner(final Activity activity, Player p) {
        SQLiteOpenHelper openHelper = new MonopolyOpenHelper(activity);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setCancelable(false);
        alert.setTitle(R.string.game_over);
        alert.setMessage(p.getName() + " won!" + " With: $" + p.getMoney(activity));
//        db.close();
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
//                CardsDB.deleteCards(activity);
                Intent i = new Intent(activity, StatisticActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(i);
                activity.finish();
                dialog.dismiss();
            }
        });
        Log.i("POPUP LOG", "POPUP");
        alert.show();
    }

}
