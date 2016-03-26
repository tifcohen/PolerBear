package com.example.tiferet.polerbear.Picker.Time;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

/**
 * Created by TIFERET on 26-Dec-15.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
    private onTimeSetListener listener;

    int hour;
    int minute;

    interface onTimeSetListener{
        void onTimeSet(int hour, int minute);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        listener.onTimeSet(hourOfDay,minute);
    }

    public void setOnTimeSetListener(onTimeSetListener ls){
        listener = ls;
    }

    public void setTime(int hour, int minute){
        this.hour=hour;
        this.minute=minute;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new TimePickerDialog(getActivity(), this, hour, minute, true);
        return dialog;
    }

}


