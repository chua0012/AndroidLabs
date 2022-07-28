package com.cst2335.chua0012;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    //private ArrayList<String> elements = new ArrayList<>(  );
    Button send;
    Button receive;
    EditText edit;
    RecyclerView rView;
    MyAdapter theAdapter;
    ArrayList<Message> messages = new ArrayList<>();
    boolean decision;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Your program starts here
        super.onCreate(savedInstanceState);

        // setContentView loads objects onto the screen.
        // Before this function, the screen is empty.
        setContentView(R.layout.activity_chat_room);


        edit = findViewById(R.id.textView9);
        send = findViewById(R.id.send);
        receive = findViewById(R.id.receive);
        rView = findViewById(R.id.myRecycleView);
        theAdapter = new MyAdapter();
        rView.setAdapter( theAdapter ) ;
        rView.setLayoutManager(new LinearLayoutManager(this));



        send.setOnClickListener( click -> {
            String whatIsTyped = edit.getText().toString();

            //adding a new message to our history if not empty
            if ( !whatIsTyped.isEmpty()) {
                decision = true;
                messages.add(new Message(whatIsTyped));
                edit.setText("");//clear the text

                //notify that new data was added at a row:
                //theAdapter.notifyDataSetChanged(); //at the end of ArrayList,
                theAdapter.notifyItemInserted(messages.size() - 1);

            }
        });

        receive.setOnClickListener( click -> {
            String whatIsTyped = edit.getText().toString();

            //adding a new message to our history if not empty
            if ( !whatIsTyped.isEmpty()) {
                decision = false;
                messages.add(new Message(whatIsTyped));

                edit.setText("");//clear the text

                //notify that new data was added at a row:
                //theAdapter.notifyDataSetChanged(); //at the end of ArrayList,
                theAdapter.notifyItemInserted(messages.size() - 1);

            }
        });


    }


    public class MyAdapter extends RecyclerView.Adapter< MyViewHolder > {

        //It inflates the view hierarchy
        //and creates an instance of the ViewHolder class
        //initialized with the view hierarchy before
        //returning it to the RecyclerView.

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View thisRow;
            //Load a new row from the layout file:
            LayoutInflater li = getLayoutInflater();

            //import layout for a row:

            if (decision)
            {
                 thisRow = li.inflate(R.layout.activity_sent, parent, false);
            }
            else{
                 thisRow = li.inflate(R.layout.activity_recieved, parent, false);

            }

            return new MyViewHolder( thisRow );
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) { //need an ArrayList to hold all the messages.
            //MyViewHolder has time and message textViews

            // What message object is at position:
            Message thisRow = messages.get(position);//

            //                      String object:

            holder.messageView.setText( thisRow.getMessageTyped());//what message goes on row position


        }

        @Override
        public int getItemCount() {

            return messages.size() ; //how many items in the list
        }
    }

    //this holds TextViews on a row:
    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView messageView;


        //View will be a ConstraintLayout
        public MyViewHolder(View itemView) {
            super(itemView);

            messageView = itemView.findViewById(R.id.row_view);

            itemView.setOnClickListener( click -> {
                int position = getAdapterPosition();//which row was clicked.
                Message whatWasClicked = messages.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoomActivity.this );

                builder.setTitle("Question:")
                        .setMessage("Do you want to delete this:" + whatWasClicked.getMessageTyped())
                        .setNegativeButton("Negative", (dialog, click1)->{ })
                        .setPositiveButton("Positive", (dialog, click2)->{
                            //actually delete something:
                            messages.remove(position);
                            theAdapter.notifyItemRemoved(position);
                        }).create().show();
            });


        }
    }

        public static class Message{
        String messageTyped;

        public Message(String messageTyped) {
            this.messageTyped = messageTyped;

        }


        public String getMessageTyped() {
            return messageTyped;
        }


    }



}

