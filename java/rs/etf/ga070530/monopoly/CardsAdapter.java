package rs.etf.ga070530.monopoly;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rs.etf.ga070530.monopoly.Model.Card;
import rs.etf.ga070530.monopoly.Model.Player;
import rs.etf.ga070530.monopoly.database.CardsDB;
import rs.etf.ga070530.monopoly.database.PlayerDB;


public class CardsAdapter extends ArrayAdapter<Card> implements View.OnClickListener {

    private List<Card> mCards = new ArrayList<>();
    Context mContext;
    private int buildings;
    private Player mPlayer;

    private static class ViewHolder{
        TextView mNameTextView;
        TextView mHousesTextView;
        TextView mHousesTitle;
        Button mAddHousesButton;
        Button mRemHousesButton;
        Button mMortgageButton;
        Button mSellButton;
    }

    public CardsAdapter(Context context, List<Card> cards, int playerId) {
        super(context, R.layout.card_layout, cards);
        mContext = context;
        mCards = cards;
        mPlayer = CardsActivity.switchPlayer(context, playerId);
    }

    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object = getItem(position);
        Card card = (Card) object;

    }

    private int lastPosition = -1;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Card card = getItem(position);
        buildings = GameActivity.mCards.get(card.getNumberOfField()).getHouses();
        final ViewHolder holder;

        final View result;
        final Card currCard = GameActivity.mCards.get(card.getNumberOfField());

        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.card_layout, parent, false);
            holder.mNameTextView = (TextView) convertView.findViewById(R.id.card_name);
            holder.mHousesTextView = (TextView) convertView.findViewById(R.id.card_houses_number);
            holder.mHousesTitle = (TextView) convertView.findViewById(R.id.card_houses_title);
            holder.mRemHousesButton = (Button) convertView.findViewById(R.id.minus_card);
            holder.mAddHousesButton = (Button) convertView.findViewById(R.id.plus_card);
            holder.mMortgageButton = (Button) convertView.findViewById(R.id.mortgage_card);
            holder.mSellButton = (Button) convertView.findViewById(R.id.sell_card);

            result = convertView;
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        holder.mNameTextView.setText(card.getNameOfField());
        refreshHouseView(card, holder);

        int fieldNo = card.getNumberOfField();
        if(fieldNo == 5 || fieldNo == 12 || fieldNo == 15 || fieldNo == 28 || fieldNo == 25 || fieldNo == 35){
            holder.mHousesTextView.setVisibility(View.INVISIBLE);
            holder.mHousesTitle.setVisibility(View.INVISIBLE);
            holder.mAddHousesButton.setVisibility(View.INVISIBLE);
            holder.mRemHousesButton.setVisibility(View.INVISIBLE);
        }

        if(!currCard.getMortgaged()){
            holder.mMortgageButton.setText("Mortgage");
        } else{
            holder.mMortgageButton.setText("Unmortgage");}
        refreshHouseView(card, holder);

        holder.mSellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.mMortgageButton.getText().equals("Mortgage")){
                PlayerDB.setMoney(mContext, mPlayer.getId(), mPlayer.getMoney(mContext) + (card.getPrice()/2 + card.getPrice()/4));}
                else{
                    PlayerDB.setMoney(mContext, mPlayer.getId(), mPlayer.getMoney(mContext) + card.getPrice()/6);}
                currCard.setOwner(null);
                CardsDB.deleteSpecPlayerCards(mContext, mPlayer.getId(), currCard.getNumberOfField());
                holder.mSellButton.setEnabled(false);
                holder.mMortgageButton.setEnabled(false);
                holder.mRemHousesButton.setEnabled(false);
                holder.mAddHousesButton.setEnabled(false);
                notifyDataSetChanged();
            }
        });

        holder.mMortgageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.mMortgageButton.getText().equals("Mortgage")){
                    PlayerDB.setMoney(mContext, mPlayer.getId(), mPlayer.getMoney(mContext) + (card.getPrice()/2));
                    currCard.setMortgaged(true);
                    holder.mMortgageButton.setText("Unmortgage");
                } else{
                    PlayerDB.setMoney(mContext, mPlayer.getId(), mPlayer.getMoney(mContext) - (card.getPrice()/2 + card.getPrice()/4));
                    currCard.setMortgaged(false);
                    holder.mMortgageButton.setText("Mortgage");
                }
                notifyDataSetChanged();
            }

        });

        holder.mAddHousesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(buildings < 5 && mPlayer.getMoney(mContext) > (card.getPrice()/2)){
                    buildings++;
                    card.setHouses(currCard.getHouses() + 1);
                    currCard.setHouses(card.getHouses());
                    PlayerDB.setMoney(mContext, mPlayer.getId(), mPlayer.getMoney(mContext) - (card.getPrice()/2));
                    refreshHouseView(card, holder);
                }
                notifyDataSetChanged();
            }
        });

        holder.mRemHousesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buildings > 0){
                    buildings--;
                    card.setHouses(card.getHouses() - 1);
                    PlayerDB.setMoney(mContext, mPlayer.getId(), mPlayer.getMoney(mContext) + (card.getPrice()/4));
                    refreshHouseView(card, holder);
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private void refreshHouseView(Card card, ViewHolder holder) {
        if(buildings == 5){
            holder.mHousesTextView.setText(String.valueOf(mContext.getResources().getString(R.string.hotel)));
            holder.mHousesTextView.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        else if(buildings > 0 && buildings < 5){
            holder.mHousesTextView.setText(String.valueOf(buildings));
            holder.mHousesTextView.setTextColor(mContext.getResources().getColor(R.color.green));}
        else{
            holder.mHousesTextView.setText(String.valueOf(buildings));
            holder.mHousesTextView.setTextColor(mContext.getResources().getColor(R.color.night_sky));}

        if(buildings == 0){
            holder.mHousesTextView.setText("None");
            holder.mRemHousesButton.setEnabled(false);
            holder.mAddHousesButton.setEnabled(true);
        }else if(buildings == 5){
            holder.mAddHousesButton.setEnabled(false);
            holder.mRemHousesButton.setEnabled(true);
        } else{
            holder.mAddHousesButton.setEnabled(true);
            holder.mRemHousesButton.setEnabled(true);
        }
        notifyDataSetChanged();
    }

}
