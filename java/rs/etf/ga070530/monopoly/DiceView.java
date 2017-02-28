package rs.etf.ga070530.monopoly;

import android.widget.ImageView;

public class DiceView {

    public static ImageView generateImage(ImageView image, int value){

        switch (value){
            case 1:
                image.setImageResource(R.drawable.dice_1);
                break;
            case 2:
                image.setImageResource(R.drawable.dice_2);
                break;
            case 3:
                image.setImageResource(R.drawable.dice_3);
                break;
            case 4:
                image.setImageResource(R.drawable.dice_4);
                break;
            case 5:
                image.setImageResource(R.drawable.dice_5);
                break;
            case 6:
                image.setImageResource(R.drawable.dice_6);
                break;
        }

        return image;
    }
}
