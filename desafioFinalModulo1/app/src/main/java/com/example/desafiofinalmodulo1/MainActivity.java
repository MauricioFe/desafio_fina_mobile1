package com.example.desafiofinalmodulo1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Accounts> accountsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    URL api = new URL("https://igti-film.herokuapp.com/api/accounts");
                    HttpURLConnection conn = (HttpURLConnection) api.openConnection();
                    conn.setRequestMethod("GET");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }

                    retTodos(stringBuilder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
        });
    }

    private void retTodos(StringBuilder stringBuilder) {
        accountsList = parseJson(stringBuilder.toString());
        for (Accounts account : accountsList) {
            Log.i("Id", "id " + account.getId() + "\n" +
                    "agencia " + account.getAgencia() + "\n " +
                    "conta " + account.getConta() + "\n " +
                    "name " + account.getName() + "\n " +
                    "balance" + account.getBalance());
        }
    }

    private List<Accounts> parseJson(String content) {
        List<Accounts> accountsList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Accounts account = new Accounts();
                account.setId(jsonObject.getInt("id"));
                account.setAgencia(jsonObject.getInt("agencia"));
                account.setConta(jsonObject.getInt("conta"));
                account.setName(jsonObject.getString("name"));
                account.setBalance(jsonObject.getInt("balance"));
                accountsList.add(account);
            }
            return accountsList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}