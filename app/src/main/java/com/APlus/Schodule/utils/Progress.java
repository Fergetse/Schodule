package com.APlus.Schodule.utils;

import android.content.Context;
import android.os.AsyncTask;

public class Progress extends AsyncTask<Void, Integer,String> {
    Context ct;

    public Progress(Context ct){
        this.ct = ct;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
