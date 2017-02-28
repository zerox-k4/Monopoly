package rs.etf.ga070530.monopoly.Model;

public class Settings {

    private int mId;
    private int mInitialCash;
    private int mGoSalary;
    private int mLandOnGoSalary;

    public Settings(int id, int initialCash, int goSalary, int landOnGoSalary){
        mId = id;
        mInitialCash = initialCash;
        mGoSalary = goSalary;
        mLandOnGoSalary = landOnGoSalary;
    }

    public int getId() {
        return mId;
    }

    public int getInitialCash() {
        return mInitialCash;
    }

    public void setInitialCash(int initialCash) {
        mInitialCash = initialCash;
    }

    public int getGoSalary() {
        return mGoSalary;
    }

    public void setGoSalary(int goSalary) {
        mGoSalary = goSalary;
    }

    public int getLandOnGoSalary() {
        return mLandOnGoSalary;
    }

    public void setLandOnGoSalary(int landOnGoSalary) {
        mLandOnGoSalary = landOnGoSalary;
    }
}