package com.rail.travelwithme.ui.saveeditview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.rail.travelwithme.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class EditViewFragment extends Fragment {

    EditText userProvidedText;
    private EditViewModel editViewModel;
    private String extStore = System.getenv("EXTERNAL_STORAGE");
    private File f_exts = new File(extStore);
    private String filepath = f_exts.getPath() + "/userProvided.txt";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 1);


        editViewModel =
                ViewModelProviders.of(this).get(EditViewModel.class);
        View root = inflater.inflate(R.layout.fragment_prgressbarview, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);

        userProvidedText = root.findViewById(R.id.userProvidedText);


        Button submitButton = root.findViewById(R.id.submitToLocalDB);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userText = userProvidedText.getText().toString();

                try {
                    writeFileExternalStorage(userText);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(v.getContext(), userText, Toast.LENGTH_SHORT).show();

            }
        });


        editViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        return root;

    }

    private void writeFileExternalStorage(String userProvidedText) throws IOException {

        FileOutputStream fos = null;
        if (f_exts.exists()) {
            try {
                fos = new FileOutputStream(filepath);
                byte[] buffer = userProvidedText.getBytes();
                fos.write(buffer, 0, buffer.length);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null)
                    fos.close();
            }
        }

    }


}