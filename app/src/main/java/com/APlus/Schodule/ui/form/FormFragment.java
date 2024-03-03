package com.APlus.Schodule.ui.form;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.APlus.Schodule.R;
import com.google.android.material.snackbar.Snackbar;

/* renamed from: com.App.SchoduleT.ui.form.FormFragment */
public class FormFragment extends Fragment {
    private int[] Bt_formn_id = {R.id.Bt_form1, R.id.Bt_form2, R.id.Bt_form3, R.id.Bt_form4, R.id.Bt_form5};
    private int[] PDF_id = {R.raw.form_ci, R.raw.form_cd};

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.form_fragment, container, false);
        getActivity().setTitle("Formularios");
        int i = 0;
        while (true) {
            int[] iArr = this.Bt_formn_id;
            if (i >= iArr.length) {
                return root;
            }
            try {
                pdf(root, iArr[i], i);
            } catch (Exception e) {
                on_click_carga(root, this.Bt_formn_id[i]);
            }
            i++;
        }
    }

    private void pdf(View root, int id, final int i) throws NullPointerException {
        int[] iArr = this.PDF_id;
        iArr[i] = iArr[i];
        root.findViewById(id).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), form_ci.class);
                intent.putExtra("id_pdf", String.valueOf(i));
                startActivity(intent);
            }
        });
    }

    private void on_click_carga(View root, int Bt_formnn) {
        root.findViewById(Bt_formnn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Snackbar.make(getActivity().findViewById(R.id.frame), (CharSequence) "Aún estamos trabajando en esto", Snackbar.LENGTH_LONG).setAction((CharSequence) "OK", (View.OnClickListener) new View.OnClickListener() {
                    public void onClick(View v) {
                    }
                }).show();
            }
        });
    }
}
