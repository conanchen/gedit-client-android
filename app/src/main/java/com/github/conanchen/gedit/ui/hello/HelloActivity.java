package com.github.conanchen.gedit.ui.hello;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseActivity;

import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/app/HelloActivity")
public class HelloActivity extends BaseActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private HelloViewModel helloViewModel;
    private final HelloAdapter helloAdapter = new HelloAdapter();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.listbutton)
    AppCompatButton listButton;

    @BindView(R.id.addbutton)
    AppCompatButton addbutton;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.edithello)
    AppCompatEditText edithello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
        ButterKnife.bind(this);
//        setSupportActionBar(toolbar);
        setupViewModel();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(helloAdapter);
    }

    private void setupViewModel() {
        helloViewModel = ViewModelProviders.of(this, viewModelFactory).get(HelloViewModel.class);
        helloViewModel.getHelloPagedListLiveData().observe(this, hellos -> {
            if (hellos != null) {
                helloAdapter.setList(hellos);
            }
        });

        edithello.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (StringUtils.isEmpty(editable.toString())) {
                    addbutton.setEnabled(false);
                } else {
                    addbutton.setEnabled(true);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hello, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    public void onFabClicked(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @OnClick(R.id.listbutton)
    public void onListButtonClicked(View view) {
        helloViewModel.reloadHellos();
    }

    @OnClick(R.id.addbutton)
    public void onAddButtonClicked(View view) {
        String helloTxt = edithello.getText().toString();
        helloViewModel.setHelloName(helloTxt);
    }

}
