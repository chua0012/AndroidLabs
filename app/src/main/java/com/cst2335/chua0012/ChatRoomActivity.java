package com.cst2335.chua0012;

import static java.lang.String.format;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;

public class ChatRoomActivity extends AppCompatActivity {

    //private ArrayList<String> elements = new ArrayList<>(  );
    Button send;
    Button receive;
    EditText edit;
    RecyclerView rView;
    MyAdapter theAdapter;
    ArrayList<Message> messages = new ArrayList<>();
    boolean decision;
    SQLiteDatabase db;
    long id;
    public static final String TAG2 = "CHAT_ROOM_ACTIVITY";
    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";


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
        rView.setAdapter(theAdapter);
        rView.setLayoutManager(new LinearLayoutManager(this));
        long id = 0;

        loadDataFromDatabase(); //get any previously saved Contact objects


        send.setOnClickListener(click -> {
            String whatIsTyped = edit.getText().toString();

            //add to the database and get the new ID
            ContentValues newRowValues = new ContentValues();

            //Now provide a value for every database column defined in MyOpener.java:
            //put string name in the MESSAGE column:
            newRowValues.put(MyOpener.COL_MESSAGE, whatIsTyped);

            //Now insert in the database:
            long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);

            //now you have the newId, you can create the Contact object
            Message newMessage = new Message(whatIsTyped, newId);

            //add the new contact to the list:
            messages.add(newMessage);
            //update the listView:
            theAdapter.notifyDataSetChanged();

            //adding a new message to our history if not empty
            if (!whatIsTyped.isEmpty()) {
                decision = true;

                edit.setText("");//clear the text

                //notify that new data was added at a row:
                //theAdapter.notifyDataSetChanged(); //at the end of ArrayList,
                theAdapter.notifyItemInserted(messages.size() - 1);

            }

            //Show the id of the inserted item:
            Toast.makeText(this, "Inserted item id:" + newId, Toast.LENGTH_LONG).show();
        });

        receive.setOnClickListener(click -> {
            String whatIsTyped = edit.getText().toString();

            //add to the database and get the new ID
            ContentValues newRowValues = new ContentValues();

            //Now provide a value for every database column defined in MyOpener.java:
            //put string name in the MESSAGE column:
            newRowValues.put(MyOpener.COL_MESSAGE, whatIsTyped);

            //Now insert in the database:
            long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);

            //now you have the newId, you can create the Contact object
            Message newMessage = new Message(whatIsTyped, newId);

            //add the new contact to the list:
            messages.add(newMessage);
            //update the listView:
            theAdapter.notifyDataSetChanged();

            //adding a new message to our history if not empty
            if (!whatIsTyped.isEmpty()) {
                decision = false;
                edit.setText("");//clear the text

                //notify that new data was added at a row:
                //theAdapter.notifyDataSetChanged(); //at the end of ArrayList,
                theAdapter.notifyItemInserted(messages.size() - 1);

            }

            //Show the id of the inserted item:
            Toast.makeText(this, "Inserted item id:" + newId, Toast.LENGTH_LONG).show();
        });

        /*boolean isTablet = findViewById(R.id.fragmentLocation) != null;

        ArrayAdapter<String> theAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messages);
        theList.setAdapter(theAdapter);
        theList.setOnItemClickListener((list, item, position, id) -> {

        Bundle dataToPass = new Bundle();
        dataToPass.putString(ITEM_SELECTED, messages.get(position) );
        dataToPass.putInt(ITEM_POSITION, position);
        dataToPass.putLong(ITEM_ID, id);

        if(isTablet)
        {
            DetailFragment dFragment = new DetailFragment(); //add a DetailFragment
            dFragment.setArguments( dataToPass ); //pass it a bundle for information
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                    .commit(); //actually load the fragment. Calls onCreate() in DetailFragment
        }
        else //isPhone
        {
            Intent nextActivity = new Intent(FragmentExample.this, EmptyActivity.class);
            nextActivity.putExtras(dataToPass); //send data to next activity
            startActivity(nextActivity); //make the transition
        }*/

    }


    private void loadDataFromDatabase() {
        //get a database connection:
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer


        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String[] columns = {MyOpener.COL_ID, MyOpener.COL_MESSAGE};
        //query all the results from the database:
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int messageColIndex = results.getColumnIndex(MyOpener.COL_MESSAGE);
        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);

        //iterate over the results, return true if there is a next item:
        while (results.moveToNext()) {
            String message = results.getString(messageColIndex);
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            messages.add(new Message(message, id));
        }

        //At this point, the contactsList array has loaded every row from the cursor.
    }


    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

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

            if (decision) {
                thisRow = li.inflate(R.layout.activity_sent, parent, false);
            } else {
                thisRow = li.inflate(R.layout.activity_recieved, parent, false);

            }

            return new MyViewHolder(thisRow);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) { //need an ArrayList to hold all the messages.
            //MyViewHolder has time and message textViews

            // What message object is at position:
            Message thisRow = messages.get(position);//

            //                      String object:

            holder.messageView.setText(thisRow.getMessage());//what message goes on row position

        }

        @Override
        public int getItemCount() {

            return messages.size(); //how many items in the list
        }
    }

    //this holds TextViews on a row:
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView messageView;

        //View will be a ConstraintLayout
        public MyViewHolder(View itemView) {
            super(itemView);

            messageView = itemView.findViewById(R.id.row_view);

            itemView.setOnClickListener(click -> {
                int position = getAdapterPosition();//which row was clicked.
                Message whatWasClicked = messages.get(position);

                View contact_view = getLayoutInflater().inflate(R.layout.message_edit, null);

                //get the TextViews
                EditText rowMessage = contact_view.findViewById(R.id.row_view);
                TextView rowId = contact_view.findViewById(R.id.row_id);

                //set the fields for the alert dialog
                rowMessage.setText(whatWasClicked.getMessage());
                rowId.setText("id:" + whatWasClicked.getId());

                /*AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoomActivity.this );

                builder.setTitle("Question:")
                        .setMessage("Do you want to delete this:" + whatWasClicked.getMessage())
                        .setNegativeButton("Negative", (dialog, click1)->{ })
                        .setPositiveButton("Positive", (dialog, click2)->{
                            //actually delete something:
                            messages.remove(position);
                            theAdapter.notifyItemRemoved(position);
                        }).create().show();*/

                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoomActivity.this);
                builder.setTitle("You clicked on item #" + position)
                        .setMessage("You can update the fields and then click update to save in the database")
                        .setView(contact_view) //add the 3 edit texts showing the contact information
                        .setPositiveButton("Update", (dialog, click1) -> {
                            whatWasClicked.update(rowMessage.getText().toString());
                            updateMessage(whatWasClicked);
                            theAdapter.notifyDataSetChanged(); //the email and name have changed so rebuild the list
                        })
                        .setNegativeButton("Delete", (dialog, click2) -> {
                            deleteMessage(whatWasClicked); //remove the contact from database
                            messages.remove(position); //remove the contact from contact list
                            theAdapter.notifyDataSetChanged(); //there is one less item so update the list
                        })
                        .setNeutralButton("dismiss", (dialog, click3) -> {
                        })
                        .create().show();

            });

        }
    }

    protected void updateMessage(Message m) {
        //Create a ContentValues object to represent a database row:
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(MyOpener.COL_MESSAGE, m.getMessage());

        //now call the update function:
        db.update(MyOpener.TABLE_NAME, updatedValues, MyOpener.COL_ID + "= ?", new String[]{Long.toString(m.getId())});
    }

    protected void deleteMessage(Message m) {
        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[]{Long.toString(m.getId())});
    }


    public static class Message {
        String messageTyped;
        long id;

        public Message(String messageTyped, long id) {
            this.messageTyped = messageTyped;
            this.id = id;
        }

        public void update(String m) {
            messageTyped = m;
        }

        /**
         * Chaining constructor:
         */
        /*public Message(String m) {
            this(m, 0);
        }*/

        public String getMessage() {
            return messageTyped;
        }

        public long getId() {
            return id;
        }
    }

    public void printCursor(Cursor c, int version) {

        String columnNames = Arrays.toString(c.getColumnNames());

        Log.e(TAG2, "Database Version: " + db.getVersion());
        Log.e(TAG2, "Number of Columns: " + c.getColumnCount());
        Log.e(TAG2, "Column Names: " + c.getColumnNames());
        Log.e(TAG2, "Number of Rows: " + c.getCount());

        if (c.moveToFirst()) {
            String tableHead = "Row | id\t| message\t| send or recieve";
            Log.e(TAG2, tableHead);
            do {
                StringBuilder sb = new StringBuilder();
                int columnCount = c.getColumnCount();
                for (int i = 0; i < columnCount; i++) {
                    sb.append(c.getString(i));
                    if (i < columnCount - 1)
                        sb.append("\t| ");
                }

                Log.e(TAG2, format("%2d | %s", c.getPosition(), sb));

            }

            while (c.moveToNext());

        }

    }

}

