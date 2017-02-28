package rs.etf.ga070530.monopoly;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rs.etf.ga070530.monopoly.Model.Card;

public class TradeCardsAdapter extends ArrayAdapter<Card> {

    public static final int UNSELECTED = 0;
    public static final int SELECTED = 1;

    private List<Card> mCards = new ArrayList<>();
    Context mContext;

    private static class ViewHolder{
        View mContainer;
        TextView mNameTextView;
        TextView mHousesTextView;
        TextView mHousesTitle;
    }

    @Override
    public int getItemViewType(int position) {
        if (mCards.get(position).isSelected()) return SELECTED;
        return UNSELECTED;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public TradeCardsAdapter(Context context, List<Card> cards) {
        super(context, R.layout.trade_card_layout, cards);
        mContext = context;
        mCards = cards;
    }

    private int lastPosition = -1;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Card card = getItem(position);
        final ViewHolder holder;

        final View result;

        int viewType = getItemViewType(position);

        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            if (viewType == SELECTED){
                convertView = inflater.inflate(R.layout.trade_card_layout_selected, parent, false);
            }else {
                convertView = inflater.inflate(R.layout.trade_card_layout, parent, false);
            }
            holder.mNameTextView = (TextView) convertView.findViewById(R.id.card_name);
            holder.mHousesTextView = (TextView) convertView.findViewById(R.id.card_houses_number);
            holder.mHousesTitle = (TextView) convertView.findViewById(R.id.card_houses_title);
            holder.mContainer = convertView.findViewById(R.id.container);

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
        }
        refreshHouseView(card, holder);

        holder.mContainer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Card card = getItem(position);
                card.setSelected(!card.isSelected());
                Log.i("CLICKED", "CLICK");

                notifyDataSetChanged();
            }
        });

        return result;
    }

    private void refreshHouseView(Card card, ViewHolder holder) {
        if(card.getHouses() == 5){
            holder.mHousesTextView.setText(String.valueOf(mContext.getResources().getString(R.string.hotel)));
            holder.mHousesTextView.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        else if(card.getHouses() > 0 && card.getHouses() < 5){
            holder.mHousesTextView.setText(String.valueOf(card.getHouses()));
            holder.mHousesTextView.setTextColor(mContext.getResources().getColor(R.color.green));}
        else{
            holder.mHousesTextView.setText(String.valueOf(card.getHouses()));
            holder.mHousesTextView.setTextColor(mContext.getResources().getColor(R.color.night_sky));}
    }

    public List<Card> getSelectedCards(){
        List<Card> returnCards = new ArrayList<>();
        for (Card card: mCards){
            if (card.isSelected())
                returnCards.add(card);
        }
        return returnCards;
    }
}
