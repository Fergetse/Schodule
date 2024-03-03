package com.APlus.Schodule.ui.apuntes;

import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CopiarArchivos {
    private static final String TAG = "logcat";

    private CopiarArchivos(String sourceFile, String destinationFile) {
        try {
            File inFile = new File(sourceFile);
            File outFile = new File(destinationFile);
            FileInputStream in = new FileInputStream(inFile);
            FileOutputStream out = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            while (true) {
                int read = in.read(buffer);
                int c = read;
                if (read != -1) {
                    out.write(buffer, 0, c);
                } else {
                    out.flush();
                    in.close();
                    out.close();
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

            Log.e(TAG, sourceFile + ", " + destinationFile);
            Log.e(TAG, "Hubo un error de entrada/salida!!!");
        }
    }

    public static void main(String[] args) {
        if (args.length == 2) {
            new CopiarArchivos(args[0], args[1]);
        }
    }
}
