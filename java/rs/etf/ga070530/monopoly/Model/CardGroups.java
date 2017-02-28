package rs.etf.ga070530.monopoly.Model;

public class CardGroups {
    private int mId;
    private int mCard1;
    private int mCard2;
    private int mCard3;
    private int mCard4;

    public CardGroups(int id, int card1, int card2) {
        mId = id;
        mCard1 = card1;
        mCard2 = card2;
    }

    public CardGroups(int id, int card1, int card2, int card3) {
        mId = id;
        mCard1 = card1;
        mCard2 = card2;
        mCard3 = card3;
    }

    public CardGroups(int id, int card1, int card2, int card3, int card4) {
        mId = id;
        mCard1 = card1;
        mCard2 = card2;
        mCard3 = card3;
        mCard4 = card4;
    }

    public int getId() {
        return mId;
    }

    public int getCard1() {
        return mCard1;
    }

    public int getCard2() {
        return mCard2;
    }

    public int getCard3() {
        return mCard3;
    }

    public int getCard4() {
        return mCard4;
    }
}
