package com.rail.travelwithme.ui.saveeditview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class EditViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public EditViewModel() throws Exception {
        mText = new MutableLiveData<>();

        String extStore = System.getenv("EXTERNAL_STORAGE");
        assert extStore != null;
        File f_exts = new File(extStore);
        String filepath = f_exts.getPath() + "/userProvided.txt";
        try {
            mText.setValue(getStringFromFile(filepath));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String getStringFromFile(String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = txtStreamToString(fin);
        fin.close();
        return ret;
    }

    private static String txtStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    LiveData<String> getText() {
        return mText;
    }


}