package rs.etf.ga070530.monopoly.Model;

public class Card {

    private boolean selected;

    private int mNumberOfCard;
    private String mNameOfCard;
    private int mResNameOfCard;
    private int mPrice;
    private int mHouses;
    private float mX;
    private float mY;
    private Player mOwner;
    private Boolean mIsBuyable;
    private Boolean mIsMortgaged;

    public Card(int numberOfField, String nameOfField, float x, float y, int price, Player owner,
                Boolean isMortgaged, Boolean isBuyable, int houses) {
        mNumberOfCard = numberOfField;
        mNameOfCard = nameOfField;
        mX = x;
        mY = y;
        mPrice = price;
        mOwner = owner;
        mIsMortgaged = isMortgaged;
        mIsBuyable = isBuyable;
    }

    public Card(int numberOfCard){
        mNumberOfCard = numberOfCard;
    }

    public Card(int numberOfCard, String nameOfCard) {
        mNumberOfCard = numberOfCard;
        mNameOfCard = nameOfCard;
    }

    public Card(int numberOfCard, int resNameOfCard) {
        mNumberOfCard = numberOfCard;
        mResNameOfCard = resNameOfCard;
    }

     public int getNumberOfField() {
        return mNumberOfCard;
    }

//    public void setNumberOfField(int mNumberOfField) {
//        this.mNumberOfCard = mNumberOfField;
//    }

    public String getNameOfField() {
        return mNameOfCard;
    }

    public void setNameOfField(String mNameOfField) {
        this.mNameOfCard = mNameOfField;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public Player getOwner() {
        return mOwner;
    }

    public void setOwner(Player owner) {
        mOwner = owner;
    }

    public float getX() {
        return mX;
    }

    public float getY() {
        return mY;
    }

    public Boolean getIsBuyable() {
        return mIsBuyable;
    }

    public int getResNameOfCard() {
        return mResNameOfCard;
    }

    public Boolean getMortgaged() {
        return mIsMortgaged;
    }

    public void setMortgaged(Boolean mortgaged) {
        mIsMortgaged = mortgaged;
    }

    public int getHouses() {
        return mHouses;
    }

    public void setHouses(int houses) {
        mHouses = houses;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}