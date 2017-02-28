package rs.etf.ga070530.monopoly;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import rs.etf.ga070530.monopoly.database.MonopolyOpenHelper;

public class SettingsActivity extends AppCompatActivity {

    private static final int SHAKE_SENSITIVITY = 4;

    public static final int SENSOR_MAX = 15;

    private Spinner mCash, mLandSalary, mPassSalary;
    private SeekBar mSensorMax;
    private Button mCancel, mAccept;
    private int newSens = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mCash = (Spinner) findViewById(R.id.settings_initial_cash);
        ArrayAdapter<CharSequence> initialCash = ArrayAdapter
                .createFromResource(this, R.array.initial_cash_options, android.R.layout.simple_spinner_item);
        initialCash.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCash.setAdapter(initialCash);


        mLandSalary = (Spinner) findViewById(R.id.settings_go_land);
        ArrayAdapter<CharSequence> landSalary = ArrayAdapter
                .createFromResource(this, R.array.land_salary_options, android.R.layout.simple_spinner_item);
        landSalary.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLandSalary.setAdapter(landSalary);


        mPassSalary = (Spinner) findViewById(R.id.settings_go_pass);
        final ArrayAdapter<CharSequence> passSalary = ArrayAdapter
                .createFromResource(this, R.array.pass_salary_options, android.R.layout.simple_spinner_item);
        passSalary.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPassSalary.setAdapter(passSalary);

        mSensorMax = (SeekBar) findViewById(R.id.sensor_max);
        mSensorMax.setMax(SENSOR_MAX);
        mSensorMax.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
                Toast.makeText(getApplicationContext(), "Max sensitivity initiate at: " + Integer.toString(viewProgress), Toast.LENGTH_SHORT).show();
                newSens = onProgressChanged;
            }
        });

        mCancel = (Button) findViewById(R.id.settings_cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancelIntent = new Intent(SettingsActivity.this, MainActivity.class);
                cancelIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(cancelIntent);
                finish();
            }
        });

        mAccept = (Button) findViewById(R.id.settings_accept);
        mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int initialCash;
                int passGoSalary;
                int landGoSalary;

                switch (mCash.getSelectedItem().toString()){
                    case "$10000":
                        initialCash = 10000;
                        break;
                    case "$15000":
                        initialCash = 15000;
                        break;
                    case "$20000":
                        initialCash = 20000;
                        break;
                    default:
                        initialCash = 15000;
                        break;
                }

                switch (mPassSalary.getSelectedItem().toString()){
                    case "$2000":
                        passGoSalary = 2000;
                        break;
                    case "$4000":
                        passGoSalary = 4000;
                        break;
                    default:
                        passGoSalary = 2000;
                        break;
                }

                switch (mLandSalary.getSelectedItem().toString()){
                    case "Normal":
                        landGoSalary = passGoSalary;
                        break;
                    case "Double":
                        landGoSalary = passGoSalary * 2;
                        break;
                    default:
                        landGoSalary = passGoSalary;
                }

                MonopolyOpenHelper.insertSettings(SettingsActivity.this, initialCash, passGoSalary, landGoSalary);
                if(newSens > 0){
                    setSensitivity(SettingsActivity.this, newSens);
                }

//                Settings s = MonopolyOpenHelper.getSettings(SettingsActivity.this);

//                Toast.makeText(SettingsActivity.this, "ID: " + s.getId() +
//                        "Init Cash: " + s.getInitialCash() +
//                        "Go Salary: " + s.getGoSalary() +
//                        "Go Land: " + s.getLandOnGoSalary(), Toast.LENGTH_SHORT).show();

                Intent i = new Intent(SettingsActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();

            }
        });
    }
    public static void setSensitivity(Context context, int sensitivity){
        SharedPreferences preferences = (context).getSharedPreferences("rs.etf.ga070530.monopoly.SENS", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("SENS", sensitivity);
        editor.commit();
    }

    public static int getSensitivity(Context context) {
        SharedPreferences preferences = (context).getSharedPreferences("rs.etf.ga070530.monopoly.SENS", MODE_PRIVATE);
        return preferences.getInt("SENS", SHAKE_SENSITIVITY);
    }
}