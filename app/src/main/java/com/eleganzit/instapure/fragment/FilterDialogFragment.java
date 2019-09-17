/*
package com.eleganzit.instapure.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eleganzit.instapure.R;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public class FilterDialogFragment extends DialogFragment {

    public static final String TAG = "example_dialog";

    private ImageView close;
    TextView apply;

    public interface FilterDialogListener {
        void onFilterApply(String inputText);
    }

    public static FilterDialogFragment display(FragmentManager fragmentManager) {
        FilterDialogFragment exampleDialog = new FilterDialogFragment();
        exampleDialog.show(fragmentManager, TAG);
        return exampleDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.filter_dialog_layout, container, false);

        close=view.findViewById(R.id.close);
        apply=view.findViewById(R.id.apply);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                */
/*HistoryFragment historyFragment=new HistoryFragment();
                historyFragment.onUserSelectValue("testtttt");
                dismiss();*//*


                FilterDialogListener activity = (FilterDialogListener) getActivity();
                activity.onFilterApply("tsttstststs");
                dismiss();
            }
        });

    }
}*/
