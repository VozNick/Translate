package com.example.vmm408.translatefeb_27;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.vmm408.translatefeb_27.workwithnet.WorkWithNet;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity
        implements PopupMenu.OnMenuItemClickListener, TextWatcher {
    private ArrayList<String> langs = new ArrayList<>();
    private WorkWithNet workWithNet = new WorkWithNet(this);
    private String langTranslate = "ru-en";
    private Menu menuM;
    private int countKey = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText textForTranslate = (EditText) findViewById(R.id.textForTranslate);
        textForTranslate.addTextChangedListener(this);

        workWithNet.getLangs();
        registerReceiver(broadcastReceiver, new IntentFilter(addActions()));
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (("loadedLangsAction").equals(intent.getAction())) {
                langs = intent.getStringArrayListExtra("langs");
            } else if (("changeLangsAction").equals(intent.getAction())) {
                detectLang(intent);
            } else if (("responseAction").equals(intent.getAction())) {
                ((TextView) findViewById(R.id.resultTranslate))
                        .setText(intent.getStringExtra("textTranslated"));
            }
        }
    };

    private IntentFilter addActions() {
        IntentFilter intent = new IntentFilter();
        intent.addAction("loadedLangsAction");
        intent.addAction("changeLangsAction");
        intent.addAction("responseAction");
        return intent;
    }

    private void detectLang(Intent intent) {
        String detectLang = intent.getStringExtra("lang");
        Iterator<String> langIterator = langs.iterator();
        while (langIterator.hasNext()) {
            String lang = langIterator.next();
            if (!lang.contains(detectLang)) langIterator.remove();
        }
        for (int i = 0; i < langs.size(); i++) {
            if (langs.get(i).contains(detectLang)) langs.remove(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menuM = menu;
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.menuItemMenu));
        if (!langs.isEmpty()) initArrayLangs(popupMenu);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(this);
        return super.onOptionsItemSelected(item);
    }

    private void initArrayLangs(PopupMenu popupMenu) {
        for (int i = 0; i < langs.size(); i++) {
            popupMenu.getMenu().add(langs.get(i));
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        menuM.findItem(R.id.langItemMenu).setTitle(item.getTitle().toString());
        langTranslate = item.getTitle().toString();
        return false;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    public void translateMethod(View view) {
        workWithNet.translate(((EditText) findViewById(R.id.textForTranslate)).getText().toString(),
                langTranslate);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (countKey == 3) {
            countKey = 0;
            workWithNet.detect(s.toString());
        } else {
            countKey++;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
