package com.earaujo.app.moneyexchangerate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduardo on 04/07/2016.
 */
public class FileOperations {

    public static List<CountryItem> readExcludedList(Context context) {

        File directory = new File(context.getFilesDir().getAbsolutePath()
                + File.separator + "save");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filename = "excludedList.sav";
        ObjectInput in = null;

        try {
            in = new ObjectInputStream(new FileInputStream(directory
                    + File.separator + filename));
            List<String> currencyCode = (List<String>) in.readObject();
            List<CountryItem> ci = new ArrayList<CountryItem>();
            in.close();
            for(String cc : currencyCode) {
                ci.add(new CountryItem(context, "",cc,""));
            }
            return ci;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void writeExcludedList(Context context, List<CountryItem> toWrite) {

        File directory = new File(context.getFilesDir().getAbsolutePath()
                + File.separator + "save");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filename = "excludedList.sav";
        ObjectOutput out = null;
        List<String> currencyCode = new ArrayList<String>();

        for (CountryItem ci: toWrite) {
            currencyCode.add(ci.getCurrencyCode());
        }

        try {
            out = new ObjectOutputStream(new FileOutputStream(directory
                    + File.separator + filename));
            out.writeObject(currencyCode);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readSpinPosition(Context context) {

        File directory = new File(context.getFilesDir().getAbsolutePath()
                + File.separator + "save");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filename = "spinpos.sav";
        ObjectInput in = null;

        try {
            in = new ObjectInputStream(new FileInputStream(directory
                    + File.separator + filename));
            String spinPos = (String) in.readObject();
            in.close();
            return spinPos;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void writeSpinPosition(Context context, String pos1, String pos2) {

        File directory = new File(context.getFilesDir().getAbsolutePath()
                + File.separator + "save");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filename = "spinpos.sav";
        ObjectOutput out = null;
        String spinPos = pos1 + pos2;

        try {
            out = new ObjectOutputStream(new FileOutputStream(directory
                    + File.separator + filename));
            out.writeObject(spinPos);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveBitmap(Context context, Bitmap bmp, String filename) {
        File directory = new File(context.getFilesDir().getAbsolutePath()
                + File.separator + "flag");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(directory + File.separator + filename);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap readBitmap(Context context, String filename) {
        File directory = new File(context.getFilesDir().getAbsolutePath()
                + File.separator + "flag");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        Bitmap bmp = BitmapFactory.decodeFile(directory + File.separator + filename);

        return bmp;
    }

    public static void deleteFlags(Context context) {
        File directory = new File(context.getFilesDir().getAbsolutePath()
                + File.separator + "flag");
        deleteRecursive(directory);
    }

    private static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

    public static String readCurrencyJson(Context context) {

        File directory = new File(context.getFilesDir().getAbsolutePath()
                + File.separator + "save");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filename = "currencyjson.sav";
        ObjectInput in = null;

        try {
            in = new ObjectInputStream(new FileInputStream(directory
                    + File.separator + filename));
            String currencyJSON = (String) in.readObject();
            in.close();
            return currencyJSON;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void writeCurrencyJson(Context context, String currencyJSON) {

        File directory = new File(context.getFilesDir().getAbsolutePath()
                + File.separator + "save");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filename = "currencyjson.sav";
        ObjectOutput out = null;

        try {
            out = new ObjectOutputStream(new FileOutputStream(directory
                    + File.separator + filename));
            out.writeObject(currencyJSON);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
