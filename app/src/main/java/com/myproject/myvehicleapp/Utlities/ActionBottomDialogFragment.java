package com.myproject.myvehicleapp.Utlities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.myproject.myvehicleapp.AddActivities.AddEditDeleteExpenseActivity;
import com.myproject.myvehicleapp.AddActivities.AddEditDeleteNoteActivity;
import com.myproject.myvehicleapp.AddActivities.AddEditDeleteRefuelingActivity;
import com.myproject.myvehicleapp.AddActivities.AddEditDeleteReminderActivity;
import com.myproject.myvehicleapp.AddActivities.AddEditDeleteServiceActivity;
import com.myproject.myvehicleapp.AppActivities.ExpenseActivity;
import com.myproject.myvehicleapp.AppActivities.NoteActivity;
import com.myproject.myvehicleapp.AppActivities.RefuelingActivity;
import com.myproject.myvehicleapp.AppActivities.ReminderActivity;
import com.myproject.myvehicleapp.AppActivities.ServiceActivity;
import com.myproject.myvehicleapp.R;


public class ActionBottomDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener{

    public static final String TAG = "ActionBottomDialog";
    private ItemClickListener bottomSheetMenuListener;

    public static ActionBottomDialogFragment newInstance() {
        return new ActionBottomDialogFragment();
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_menu, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.reminderBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddEditDeleteReminderActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        view.findViewById(R.id.noteBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(view.getContext(), NoteActivity.class);
                Intent intent = new Intent(view.getContext(), AddEditDeleteNoteActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        view.findViewById(R.id.serviceBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(view.getContext(), ServiceActivity.class);
                Intent intent = new Intent(view.getContext(), AddEditDeleteServiceActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        view.findViewById(R.id.expenseBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(view.getContext(), ExpenseActivity.class);
                Intent intent = new Intent(view.getContext(), AddEditDeleteExpenseActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        view.findViewById(R.id.refuelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(view.getContext(), RefuelingActivity.class);
                Intent intent = new Intent(view.getContext(), AddEditDeleteRefuelingActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        view.findViewById(R.id.closeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ItemClickListener) {
            bottomSheetMenuListener = (ItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        bottomSheetMenuListener = null;
    }

    @Override public void onClick(View view) {

    }

    public interface ItemClickListener {
        void onItemClick(String item);
    }
}
