package cyberwingz.com.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //A numeric code to idnetify the edit activity
    public final static int EDIT_REQUEST_CODE = 20;
    //keys used for passing data between activities
    public  final static String ITEM_TEXT= "ItemText";
    public  final static String ITEM_POSITION= "ItemPosition";



    //declare variables to be initialized in OnCreate
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter; //an intermediary object that wires the Array to the view
    ListView lvItems;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        items = new ArrayList<>();
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items );
        //resolving instance that already exitsts R finds the int Id. cast to ListView item.
        lvItems = (ListView)findViewById(R.id.lvItems);

      //wire the adapter to the listview
        lvItems.setAdapter(itemsAdapter);

//        //mock data
//        items.add("First Item");
//        items.add("second item");
        setupListViewListener();


    }
    //add item functionality
    public void onAddItem(View v ) {
        EditText etNewItem = findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();

    }
    private void setupListViewListener() {//need to explicitly call this in onCreate
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            //consuming this, so return true
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
        //set up listener for edit(regular click)
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //create new activity
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                //pass the data being edited to the activity
                i.putExtra(ITEM_TEXT, items.get(position));
                i.putExtra(ITEM_POSITION, position);
                //display activity to the user
                startActivityForResult(i, EDIT_REQUEST_CODE);
            }
        });
    }

    //handle results from edit activity


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if the edit activity completed ok
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE){
            String updatedItem = data.getExtras().getString(ITEM_TEXT);
            int position = data.getExtras().getInt(ITEM_POSITION);
            items.set(position, updatedItem);
            itemsAdapter.notifyDataSetChanged();
            //persist the model
            writeItems();
            Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show();

        }
    }

    private File getDataFile(){
        return new File(getFilesDir(), "todo.txt");
    }
    private void readItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading file", e);
            items= new ArrayList<>();
//            e.printStackTrace();
        }
    }
    //call every time the model is changed
    private void writeItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing file", e);
        }

    }

}