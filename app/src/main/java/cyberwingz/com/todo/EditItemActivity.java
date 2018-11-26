package cyberwingz.com.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    EditText etItemText;
    int position;
    public  final static String ITEM_TEXT= "ItemText";
    public  final static String ITEM_POSITION= "ItemPosition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        //resolve edit text from layout
        etItemText = findViewById(R.id.etItemText);
        //set edit text value from intent extra
        etItemText.setText(getIntent().getStringExtra(ITEM_TEXT));
        position = getIntent().getIntExtra(ITEM_POSITION, 0);
        getSupportActionBar().setTitle("EditItem");

    }
    public void onSaveItem(View v){
        Intent i = new Intent();
        i.putExtra(ITEM_TEXT, etItemText.getText().toString());
        i.putExtra(ITEM_POSITION, position);
        setResult(RESULT_OK, i);
        finish();

    }
}
