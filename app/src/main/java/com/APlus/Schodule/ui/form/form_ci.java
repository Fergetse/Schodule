package com.APlus.Schodule.ui.form;

import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.APlus.Schodule.R;
import com.github.barteksc.pdfviewer.PDFView;

/* renamed from: com.App.SchoduleT.ui.form.form_ci */
public class form_ci extends AppCompatActivity {
    int[] PDF_id = {R.raw.form_ci, R.raw.form_cd};
    int id_def;
    private String[] nomb = {"form_ci", "form_cd"};
    PDFView pdfView;
    ProgressBar progressBar;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_form_ci);
        this.pdfView = (PDFView) findViewById(R.id.pdf_viewer);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.id_def = Integer.parseInt(getIntent().getExtras().get("id_pdf").toString());
        Context applicationContext = getApplicationContext();
        String[] strArr = this.nomb;
        int i = this.id_def;
        new PDF(applicationContext, strArr[i], this.pdfView, this.progressBar, this.PDF_id[i]).vista();
    }
}
