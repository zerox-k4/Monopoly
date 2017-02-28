package rs.etf.ga070530.monopoly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import rs.etf.ga070530.monopoly.database.MonopolyOpenHelper;

public class StatisticActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private ListView mStatListView;
    private Menu clearMenu;
    private List<String> mStats = new ArrayList<>();
    private List<String> mCardNames = new ArrayList<>();
    private ArrayAdapter statAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        mStats = MonopolyOpenHelper.getStatistic(StatisticActivity.this);
//        for(int i=0 ; i<mCards.size(); i++){
//            mCardNames.add(mCards.get(i).getNameOfField());
//        }

        mStatListView = (ListView) findViewById(R.id.statistic_view);
        mStatListView.setOnItemSelectedListener(this);
        statAdapter = new ArrayAdapter(StatisticActivity.this, android.R.layout.simple_dropdown_item_1line, mStats);
//        mStatListView.setAdapter(statAdapter);
        refreshUI();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        clearMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.clear_stat_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.clear_stat:
                mStats.clear();
                MonopolyOpenHelper.clearStatistic(StatisticActivity.this);
                refreshUI();
                break;

        }

        return true;

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(StatisticActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    private void refreshUI(){
        mStatListView.setAdapter(statAdapter);
    }
}
