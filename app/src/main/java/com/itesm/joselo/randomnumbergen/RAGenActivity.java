package com.itesm.joselo.randomnumbergen;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.HashMap;

public class RAGenActivity extends AppCompatActivity {

    private String generator, funcion, params, x0, a, c, m;
    EditText param_x, param_a, param_c, param_m, paramv_m, paramv_n;
    TextView pvalue_x, pvalue_a, pvalue_c, pvalue_m, pvalue_f;
    HashMap <String,String> valuesGen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen);

        initialize();
    }

    private void initialize(){

        pvalue_x = (TextView) findViewById(R.id.param_value_x0);
        pvalue_a = (TextView) findViewById(R.id.param_value_a);
        pvalue_c = (TextView) findViewById(R.id.param_value_c);
        pvalue_m = (TextView) findViewById(R.id.param_value_m);
        pvalue_f = (TextView) findViewById(R.id.value_function);
        paramv_m = (EditText) findViewById(R.id.paramv_m);
        paramv_n = (EditText) findViewById(R.id.paramv_n);

        generator = "-1";
        funcion   = "-1";
        params    = "-1";
        x0        = "-1";
        a         = "-1";
        c         = "-1";
        m         = "-1";

        valuesGen = new HashMap<>();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onGenClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_mixed:
                if (checked)
                    generator = "mixed";
                    //Toast.makeText(this, generator, Toast.LENGTH_LONG).show();
                    break;
            case R.id.radio_multiplicative:
                if (checked)
                    generator = "multiplicative";
                    //Toast.makeText(this, generator, Toast.LENGTH_LONG).show();
                    break;
        }
    }

    public void onParams(View v){

        hideParams();

        boolean wrapInScrollView = true;
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.parameters)
                .customView(R.layout.dialog_custom_parameters, wrapInScrollView)
                .positiveText(R.string.agree)
                .negativeText(R.string.disagree)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        setParameters(
                            param_x.getText().toString(),
                            param_a.getText().toString(),
                            param_c.getText().toString(),
                            param_m.getText().toString());
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
                    }
                })
                .build();

        param_x = (EditText) dialog.getCustomView().findViewById(R.id.param_x0);
        param_a = (EditText) dialog.getCustomView().findViewById(R.id.param_a);
        param_c = (EditText) dialog.getCustomView().findViewById(R.id.param_c);
        param_m = (EditText) dialog.getCustomView().findViewById(R.id.param_m);

        if(generator.equals("multiplicative")){
            param_c.setVisibility(View.GONE);
            param_c.setText("0");
        }

        dialog.show();

    }

    public void onListFunction(View v){

        hideFunctionSelected();

        new MaterialDialog.Builder(this)
                .title(R.string.function)
                .items(R.array.namesFunctions)
                .itemsCallbackSingleChoice(2, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        showFunctionSelected(""+text);
                        return true;
                    }
                })
                .positiveText(R.string.agree)
                .show();

    }

    public void onGeneratorClicked(View v){

        /*valuesGen.put("generator", generator);
        valuesGen.put("x0", x0);
        valuesGen.put("a", a);
        valuesGen.put("c", c);
        valuesGen.put("m", m);
        valuesGen.put("function", funcion);
        valuesGen.put("valueN", paramv_n.getText().toString());
        valuesGen.put("valueM", paramv_m.getText().toString());*/
        Toast.makeText(this, "Datos almacenados", Toast.LENGTH_LONG).show();

/*
    arguments["n"] = 10000;
    arguments["m"] = 100000;
    arguments["mean"] = 5.23;
    arguments["var"] = 2.3;
    arguments["a"] = 0;
    arguments["b"] = 1;
    arguments["c"] = 2;

    uniformArguments["n"] = 10000;
    uniformArguments["x0"] = 17;
    uniformArguments["a"] = 203;
    uniformArguments["m"] = 100000;
* */

        generator = "mixed";
        x0 = "17";
        a="203";
        c="2";
        m="10000";
        funcion="binomial";
        paramv_m.setText("10000");
        paramv_n.setText("1000");

        Bundle bundle = new Bundle();
        bundle.putString("GENERATOR", generator);
        bundle.putString("VALUE_X0", x0);
        bundle.putString("VALUE_A", a);
        bundle.putString("VALUE_C", c);
        bundle.putString("VALUE_MOD", m);
        bundle.putString("FUNCTION", funcion);
        bundle.putString("VALUE_N", empty(paramv_n.getText().toString())? "0": paramv_n.getText().toString());
        bundle.putString("VALUE_M", empty(paramv_m.getText().toString())? "0": paramv_m.getText().toString());

        Intent i = new Intent(this, RAPlotData.class);
        i.putExtras(bundle);
        startActivity(i);
        overridePendingTransition(R.anim.slide_up_info, R.anim.no_change);

    }


    private void setParameters(String px0, String pa, String pc, String pm){

        x0 = empty(px0)? "0" : px0;
        a  = empty(pa)? "0" : pa;
        c  = empty(pc)? "0" : pc;
        m  = empty(pm)? "0" : pm;

        showParams();

    }

    private void showParams(){
        pvalue_x.setText("x0 = "+x0);
        pvalue_a.setText("a = "+a);
        pvalue_c.setText("c = "+c);
        pvalue_m.setText("m = "+m);

        pvalue_x.setVisibility(View.VISIBLE);
        pvalue_a.setVisibility(View.VISIBLE);
        pvalue_m.setVisibility(View.VISIBLE);
        if(generator.equals("mixed"))
            pvalue_c.setVisibility(View.VISIBLE);
    }

    private void hideParams(){
        pvalue_x.setVisibility(View.GONE);
        pvalue_a.setVisibility(View.GONE);
        pvalue_m.setVisibility(View.GONE);
        pvalue_c.setVisibility(View.GONE);
    }

    private void showFunctionSelected(String nameFunction){
        funcion = nameFunction;
        pvalue_f.setText(funcion);
        pvalue_f.setVisibility(View.VISIBLE);
        //Toast.makeText(this, funcion, Toast.LENGTH_LONG).show();
    }

    private void hideFunctionSelected(){
        pvalue_f.setText("");
        pvalue_f.setVisibility(View.GONE);
    }

    //VALIDATE EDITTEXT
    private boolean empty(String data){
        return data.isEmpty();
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gen, menu);
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
    }*/

}
