package codepath.com.nytimessearch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import codepath.com.nytimessearch.activities.SearchActivity;


/**
 * Created by floko_000 on 7/31/2016.
 */
public class FragmentFilter extends DialogFragment{

    public Spinner spOrderBy;
    public Button btnOnSetFilter;
    public CheckBox ckbxArts;
    public CheckBox ckbxFashion;
    public CheckBox ckbxSports;

    public FragmentFilter() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static FragmentFilter newInstance(String title) {
        FragmentFilter frag = new FragmentFilter();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    // 1. Defines the listener interface with a method passing back data result.
    public interface FilterDialogListener {
        void onFinishEditDialog(String inputText);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        spOrderBy = (Spinner) view.findViewById(R.id.spOrder);
        btnOnSetFilter = (Button) view.findViewById(R.id.btnOnSetFilter);
        ckbxArts = (CheckBox) view.findViewById(R.id.ckbx_arts);
        ckbxSports = (CheckBox) view.findViewById(R.id.ckbx_sports);
        ckbxFashion = (CheckBox) view.findViewById(R.id.ckbx_Fashion);
        btnOnSetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newsDeskItems = "";
                //String orderBy = "sort=" + spOrderBy.getSelectedItem().toString();
                String orderBy = spOrderBy.getSelectedItem().toString();
                if(ckbxArts.isChecked())
                    newsDeskItems += "'" + (ckbxArts.getText()+ "' ");
                if(ckbxSports.isChecked())
                    newsDeskItems += "'" + (ckbxSports.getText() + "' ");
                if(ckbxFashion.isChecked())
                    newsDeskItems += "'" + ckbxFashion.getText() + " '";
                String newsDesk = "news_desk:(" + newsDeskItems + ")";
                String queryStringParams = orderBy + newsDesk;

                SearchActivity listener = (SearchActivity) getActivity();
                listener.onFinishFilterDialog(orderBy, newsDeskItems);
                Log.d("DEBUG", v.toString());
                dismiss();
            }
        });

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }




}