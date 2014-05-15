package codepath.apps.simpletodo;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Selection;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class EditItemActivity extends ActionBarActivity {
	private EditText etEditItem;
	private int itemPosition;
	
	private final int EDIT_REQUEST_CODE = 100;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		
		etEditItem = (EditText) findViewById(R.id.etEditItem);
		
		// Read from Bundle
		String item  = getIntent().getStringExtra("item");
		itemPosition = getIntent().getIntExtra("position", 0);
		
		etEditItem.setText(item);
		
		// To set cursor position
		Editable editItem = etEditItem.getText();
		Selection.setSelection(editItem, item.length());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
		return true;
	}

	// Function to call on 'save editted item' button click
	public void saveEditedItem(View v) {
    	//Get edited item
    	String newItemStr = etEditItem.getText().toString();
    	
    	// Basic validation 
    	if(!newItemStr.isEmpty()) {    		
    		// Valid item found
    		Intent data = new Intent();

    		data.putExtra("item", newItemStr);
    		data.putExtra("position", itemPosition);

    		setResult(RESULT_OK, data);
    		finish();
    	} 
    	else {
    		Toast.makeText(this, "Enter a valid item!", Toast.LENGTH_SHORT).show();
    	}
    }

}
