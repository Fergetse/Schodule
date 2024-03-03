package com.APlus.Schodule.ui.form;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* renamed from: com.App.SchoduleT.ui.form.PDF */
public class PDF {

    /* renamed from: ct */
    private Context f164ct;

    /* renamed from: id */
    private int f165id;
    private InputStream imput;
    private String name;
    private PDFView pdfView;
    private ProgressBar progressBar;

    PDF(Context ct, String name2, PDFView pdf, ProgressBar progressBar2, int id) {
        this.f164ct = ct;
        this.name = name2;
        this.pdfView = pdf;
        this.progressBar = progressBar2;
        this.f165id = id;
        this.imput = ct.getResources().openRawResource(id);
    }

    public void vista() {
        File dir = this.f164ct.getExternalFilesDir("Formularios");
        File doc = new File(dir + "/" + this.name + ".pdf");
        try {
            if (!doc.exists()) {
                InputStream inputStream = this.imput;
                copyInputStreamToFile(inputStream, new File(dir + "/" + this.name + ".pdf"));
            }
            this.pdfView.fromStream(new FileInputStream(doc)).load();
            this.progressBar.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
        }
    }

    private void copyInputStreamToFile(InputStream in, File file) throws IOException {
        OutputStream out = null;
        try {
            OutputStream out2 = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            while (true) {
                int read = in.read(buf);
                int len = read;
                if (read > 0) {
                    out2.write(buf, 0, len);
                } else {
                    try {
                        out2.close();
                        in.close();
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            if (out != null) {
                out.close();
            }
            in.close();
        } catch (Throwable th) {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                    throw th;
                }
            }
            in.close();
            throw th;
        }
    }
}
