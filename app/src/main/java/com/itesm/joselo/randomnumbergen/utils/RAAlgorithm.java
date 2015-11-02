package com.itesm.joselo.randomnumbergen.utils;

import java.util.ArrayList;
import java.util.Random;

public class RAAlgorithm {

    public ArrayList<Integer> generateRandomNumbers(int N){

        ArrayList<Integer> serie = new ArrayList<> ();
        //Inicializamos el ArrayList lleno de ceros
        for (int i=0; i<N; i++) { serie.add(0); }

        Random numAleatorio;
        numAleatorio = new Random ();
        for (int i=0; i < serie.size(); i++) {
            serie.set(i, numAleatorio.nextInt(N) );
        }

        return serie;

    }

    public ArrayList<Integer> generateMixed(String x0, String a, String c, String m, String n){
        ArrayList<Integer> valuesData = new ArrayList<>();

        int value_x0 = Integer.parseInt(x0);
        int value_a = Integer.parseInt(a);
        int value_c = Integer.parseInt(c);
        int value_m = Integer.parseInt(m);
        int value_n = Integer.parseInt(n);
        int x = 0;

        for (int i = 0; i < value_n - 1; ++i) {
            if (i == 0) {
                valuesData.add(value_x0);
                valuesData.add(((value_a * value_x0) + value_c) % value_m);
                x = 2;
            } else {
                valuesData.add(((value_a * valuesData.get(x - 1)) + value_c) % value_m);
                ++x;
            }
        }

        return valuesData;
    }

    public ArrayList<Integer> generateMultiplicative(String x0, String a, String m, String n){
        ArrayList<Integer> valuesData = new ArrayList<>();

        int value_x0 = Integer.parseInt(x0);
        int value_a = Integer.parseInt(a);
        int value_m = Integer.parseInt(m);
        int value_n = Integer.parseInt(n);
        int x = 0;

        for (int i = 0; i < value_n - 1; ++i) {
            if (i == 0) {
                valuesData.add(value_x0);
                valuesData.add((value_a * value_x0) % value_m);
                x = 2;
            } else {
                valuesData.add((value_a * valuesData.get(x - 1)) % value_m);
                ++x;
            }
        }

        return valuesData;
    }

    public ArrayList<Float> generateUniform(ArrayList<Integer> arrayValues/*, String a, String m, String n*/){
        ArrayList<Float> valuesData = new ArrayList<>();

        for (int i = 0; i < arrayValues.size(); ++i) {
            valuesData.add(Float.valueOf(arrayValues.get(i)));
        }

        return valuesData;
    }

    public ArrayList<Float> generateExponential(ArrayList<Float> arrayValues, String lambda, String m, String n){
        //ArrayList<Float> valuesData = new ArrayList<>();
        float value_lambda = Float.valueOf(lambda);
        int value_m = Integer.parseInt(m);
        int value_n = Integer.parseInt(n);

        ArrayList<Float> R = new ArrayList<>();
        ArrayList<Float> X = new ArrayList<>();
        float x = 0f;

        for (int i = 0; i < arrayValues.size(); ++i) {
            R.add(arrayValues.get(i) / (float)value_m );
        }

        Random random = new Random();
        for (int i = 0; i < value_n; ++i) {
            int size = R.size();
            float R1 = R.get(random.nextInt(size));
            x = (float) -(Math.log(1 - R1) * value_lambda);
            X.add(x);
        }

        return X;
    }

    /*
    public ArrayList<Integer> generateNormal(String x0, String a, String m, String n){
        ArrayList<Integer> valuesData = new ArrayList<>();

        return valuesData;
    }*/

    public ArrayList<Float> generateTriangular(ArrayList<Integer> arrayValues, String n, String m, String a, String b, String c){

        int value_n = Integer.parseInt(n);
        int value_m = Integer.parseInt(m);
        float value_a = Float.valueOf(a);
        float value_b = Float.valueOf(b);
        float value_c = Float.valueOf(c);

        float A1 = (value_b - value_a) / (value_c - value_a);
        ArrayList<Float> R = null;
        ArrayList<Float> X = new ArrayList<>();

        for (int i = 0; i < arrayValues.size(); ++i) {
            R.add(arrayValues.get(i) / (float)value_m);
        }

        Random random = new Random();
        for (int i = 0; i < value_n; ++i) {
            int size = R.size();
            float R1 = R.get(random.nextInt(size));
            float R2 = R.get(random.nextInt(size));

            if (R1 < A1) {
                X.add((float) (value_a + ((value_b - value_a) * Math.sqrt(R2))));
            } else {
                X.add((float) (value_c - ((value_c - value_b) * Math.sqrt(R2))));
            }

        }

        return X;
    }

    /*
    public ArrayList<Integer> generatePoisson(String x0, String a, String m, String n){
        ArrayList<Integer> valuesData = new ArrayList<>();

        return valuesData;
    }

    public ArrayList<Integer> generateBinommial(String x0, String a, String m, String n){
        ArrayList<Integer> valuesData = new ArrayList<>();

        return valuesData;
    }*/


}