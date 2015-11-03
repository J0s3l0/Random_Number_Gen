package com.itesm.joselo.randomnumbergen;

import android.annotation.SuppressLint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.filter.Approximator;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.itesm.joselo.randomnumbergen.utils.MyYAxisValueFormatter;
import com.itesm.joselo.randomnumbergen.utils.RAAlgorithm;

import java.util.ArrayList;

public class RAPlotData extends FragmentActivity implements OnChartValueSelectedListener {

    protected BarChart mChart;
    String nameGenerator, x0, a, c, mod, nameFunction, n, m;
    ArrayList<Integer> valueDataset;
    ArrayList<Float> valueDatasetAlgor;

    private static int TIME_OUT = 2000;
    private static int TIME_OUT2 = 4000;

    /*protected String[] nameFunctions = new String[] {
            "Uniforme", "Exponencial", "Normal", "Triangular", "Poisson", "Binomial"};*/

    private Typeface mTf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_raplot_data);

        initialize();

        getValues();

    }


    /*
    * Inicialización de los ArrayList que contendran los números aleatorios
    * y los números de la distribución elegida
    *
    * Configuración del ambiente de graficación, añadiendo formato de texto,
    * leyenda, valores
    * */
    private void initialize(){

        valueDataset      = new ArrayList<>();
        valueDatasetAlgor = new ArrayList<>();

        mChart = (BarChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(200);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);

        mTf = Typeface.createFromAsset(getAssets(), "fonts/OpenSansRegular.ttf");

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);

        YAxisValueFormatter custom = new MyYAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

    }

    /*
    * Obtiene los valores que el usuario ha ingresado
    * Mecanismo de transferencia de datos entre Activities
    *
    * Se inicia como primer tarea generar el dataset de números aleatorios
    * Se pasa como parametro el nombre del generador
    * */
    private void getValues(){

        Bundle bundle = this.getIntent().getExtras();

        nameGenerator = bundle.getString("GENERATOR");
        x0            = bundle.getString("VALUE_X0");
        a             = bundle.getString("VALUE_A");
        c             = bundle.getString("VALUE_C");
        mod           = bundle.getString("VALUE_MOD");
        nameFunction  = bundle.getString("FUNCTION");
        n             = bundle.getString("VALUE_N");
        m             = bundle.getString("VALUE_M");

        new GenerateNumbers().execute(nameGenerator);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /*
    * Una vez calculado el dataset de números aleatorios por el generador
    * Se pasa al algoritmo de la función para que tenga la distribución
    * definida por la función
    *
    * Cada conjunto de datos se setea al widget de graficación
    * */
    private void setData(int count, float range) {

        ArrayList<String> xVals = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            xVals.add(String.valueOf(i % count));
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<>();

        for (int i = 0; i < valueDatasetAlgor.size(); i++) {
            yVals1.add(new BarEntry(valueDatasetAlgor.get(i), i));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "DataSet");
        set1.setBarSpacePercent(35f);
        set1.setColor(ColorTemplate.COLORFUL_COLORS[0]);

        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        final BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);
        data.setValueTypeface(mTf);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                mChart.setData(data);
                mChart.animateXY(3000, 3000);

            }
        }, TIME_OUT);
    }

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;

        RectF bounds = mChart.getBarBounds((BarEntry) e);
        PointF position = mChart.getPosition(e, YAxis.AxisDependency.LEFT);

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        Log.i("x-index",
                "low: " + mChart.getLowestVisibleXIndex() + ", high: "
                        + mChart.getHighestVisibleXIndex());
    }

    public void onNothingSelected() {
    };

    /*
    * Acción del botón físico del teléfono para
    * regresar a la vista anterior
    * */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.no_change, R.anim.slide_down_info);
    }

    /*
    * Generador congruencial Mixto
    * Usa dicho generador para calcular los números aleatorios y tener un dataset
    *
    * params
    * * * Se hace un cast dentro del método
    * x0  = String => x0
    * a   = String => a
    * c   = String => c
    * mod = String => mod
    * n   = String => n
    * */
    public int genMixed(){
        valueDataset =  RAAlgorithm.generateMixed(x0, a, c, mod, n);
        return valueDataset.size();
    }

    /*
    * Generador congruencial Multiplicativo
    * Usa dicho generador para calcular los números aleatorios y tener un dataset
    *
    * params
    * * * Se hace un cast dentro del método
    * x0  = String => x0
    * a   = String => a
    * mod = String => mod
    * n   = String => n
    * */
    public int genMulti(){
        valueDataset = RAAlgorithm.generateMultiplicative(x0, a, mod, n);
        return valueDataset.size();
    }

    /*
    * Algoritmos de funciones de distribucion
    *
    * params
    * nombre  = String => nombreFuncion
    * */
    public int genAlgorithm(){

        if(nameFunction.equals("Uniforme"))
            valueDatasetAlgor = RAAlgorithm.generateUniform(valueDataset);
        else if(nameFunction.equals("Exponencial"))
            valueDatasetAlgor = RAAlgorithm.generateExponential(valueDataset, "1", m, n);
        else if(nameFunction.equals("Normal"))
            valueDatasetAlgor = RAAlgorithm.generateNormal(valueDataset, "5.23", "2.53", m, n);
        else if(nameFunction.equals("Triangular"))
            valueDatasetAlgor = RAAlgorithm.generateTriangular(valueDataset, m, n, "1", "2", "3");
        else if(nameFunction.equals("Poisson"))
            valueDatasetAlgor = RAAlgorithm.generatePoisson(valueDataset, "1", m, n);
        else if(nameFunction.equals("Binomial"))
            valueDatasetAlgor = RAAlgorithm.generateBinommial(valueDataset, x0, a, m, n, "5");

        return valueDatasetAlgor.size();
    }

    /*
    * Envía la notificación de que ya puede ser pintado la gráfica de acuerdo
    * a los números aleatorios generados y a los parámetros que recibe del usuario
    *
    * params
    *
    * count = int   => m
    * range = float => n
    * */
    public void plotData(){
        setData(Integer.parseInt(m), Float.valueOf(n));
    }

    public void doAlgor(){
        setData(Integer.parseInt(m), Float.valueOf(n));
        new GenerateNumbersWithAlgor().execute(nameFunction);
    }

    /*
    * Tarea Asíncrona para generar el dataset de los números aleatorios
    * de acuerdo al generador elegido {Mixto, Multiplicativo}
    * El resultado se guarda en un variable global que será ocupada por
    * el algoritmo de la función elegida
    *
    * param
    * params[0] = String => nombreGenerador
    * */
    private class GenerateNumbers extends AsyncTask<String, Integer, Integer> {

        protected Integer doInBackground(String... params) {
            int request = 0;
            if(params[0].equals("mixed"))
                request = genMixed();
            else
                request = genMulti();

            return request;
        }

        protected void onPostExecute(Integer result) {
            if(result > 0)
                doAlgor();

        }
    }

    /*
    * Tarea Asíncrona para generar el dataset de los números aleatorios
    * usando la funcion de distribucion
    * de acuerdo a la funcion elegida de la lista de opciones
    * El resultado se guarda en un variable global que será ocupada por
    * para settear los datos en la gráfica
    *
    * param
    * params[0] = String => nombreFuncion
    * */
    private class GenerateNumbersWithAlgor extends AsyncTask<String, Integer, Integer> {

        protected Integer doInBackground(String... params) {
            int request = genAlgorithm();

            return request;
        }

        protected void onPostExecute(Integer result) {
            if(result > 0)
                plotData();
        }
    }


    /*
    * Widget de tiempo de espera
    * */
    private void waitPlease(){
        new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .progress(true, 0)
                .show();
    }
}
