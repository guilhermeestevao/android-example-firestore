package br.com.delxmobile.fidelidade.db;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


/**
 * Created by Guilherme on 26/09/2017.
 */
public class GenericSQLFileReader {

    private static String readFile(Context context, String path) {
        try {
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input, "UTF-8");
            BufferedReader r = new BufferedReader(reader);
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }

            return total.toString();
        } catch(IOException e) {
            return null;
        }

    }

    public static String[] getCommands(Context context, String path) {

        String[] commands = readFile(context, path).split(";");
        for(int k = 0; k < commands.length; k++) {
            commands[k] = commands[k]+";";
        }
        return commands;
    }
}