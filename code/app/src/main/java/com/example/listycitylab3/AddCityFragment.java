package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment implements DialogInterface.OnDismissListener {
    public interface AddCityDialogListener {
        void addCity(City city);
        void editCity(String cityName, String provinceName, int position);
        // interface method to convey information between MainActivity and AddCityFragment
        void dismissFragment();
    }

    private AddCityDialogListener listener;

    public static AddCityFragment newInstance(City city, int position) {
        // create a new AddCityFragment but have it contain a bundle with information about a city
        AddCityFragment newFragment = new AddCityFragment();
        Bundle args = new Bundle();
        args.putSerializable("city", city);
        args.putInt("position", position);
        newFragment.setArguments(args);
        return newFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        }
        else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Bundle bundledArgs = getArguments();
        if (bundledArgs == null) { // no bundled arguments means we're creating a new list object
            return builder
                    .setView(view)
                    .setTitle("Add City")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Add", (dialog, which) -> {
                        String cityName = editCityName.getText().toString();
                        String provinceName = editProvinceName.getText().toString();
                        listener.addCity(new City(cityName, provinceName));
                    })
                    .create();
        }
        else { // having bundled arguments means we're editing an existing list object
            City bundledCity = (City) bundledArgs.get("city");
            int position = bundledArgs.getInt("position");
            assert bundledCity != null;
            // autocomplete the editTexts with the selected list object's city name and province name
            editCityName.setText(bundledCity.getName());
            editProvinceName.setText(bundledCity.getProvince());
            return builder
                    .setView(view)
                    .setTitle("Edit City")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Ok", (dialog, which) -> {
                        String cityName = editCityName.getText().toString();
                        String provinceName = editProvinceName.getText().toString();
                        listener.editCity(cityName, provinceName, position);
                    })
                    .create();
        }
    }

    // onDismiss is called whenever the AlertDialog is closed in any way
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        listener.dismissFragment();
    }
}
