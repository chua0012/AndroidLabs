package com.cst2335.chua0012;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class DetailsFragment extends Fragment {
    private Bundle dataFromActivity;
    private long id;
    private AppCompatActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(ChatRoomActivity.ITEM_ID );

        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.fragment_details, container, false);

        //show the message
        TextView message = (TextView)result.findViewById(R.id.message);
        message.setText(dataFromActivity.getString(ChatRoomActivity.ITEM_SELECTED));

        //show the id:
        TextView idView = (TextView)result.findViewById(R.id.idText);
        idView.setText("ID=" + id);

        //show the id:
        //TextView messageTypeView = (TextView)result.findViewById(R.id.messageType);
        //idView.setText("Is a " + id + "message");
        CheckBox cb = result.findViewById(R.id.messageType);

                cb.setChecked(ChatRoomActivity.decision);

        // get the delete button, and add a click listener:
        Button finishButton = (Button)result.findViewById(R.id.hideButton);
        finishButton.setOnClickListener( clk -> {

            //Tell the parent activity to remove
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });

        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }
}