package codepath.apps.simpletodo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Build;

public class TodoActivity extends ActionBarActivity {
	private ArrayList<String> items;
	private ArrayAdapter<String> itemsAdapter;
	private ListView lvItems;
	private EditText etNewItem;
	
	private final int EDIT_REQUEST_CODE = 100;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        
        etNewItem 		= (EditText) findViewById(R.id.etNewItem);
        lvItems 		= (ListView) findViewById(R.id.lvItems);
        
        // load items from saved file
        loadItems();
        itemsAdapter 	= new ArrayAdapter<String>(this, 
        					android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        
        /* For cosmetic reasons, add 2 examples if list is empty */
        if(items.size() == 0) { 
        	items.add("First Item");
        	items.add("Second Item");
        }
        setupListViewListener();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todo, menu);
        return true;
    }

    // Function to call on 'add item' button click
    public void addToDoItem(View v) {
    	//Get new item and add
    	String newItemStr = etNewItem.getText().toString();
    	
    	// Basic validation 
    	if(!newItemStr.isEmpty()) {    		
    		// Valid item found
    		itemsAdapter.add(newItemStr);
    		
        	// Reset new item text field 
        	etNewItem.setText("");
        	
        	// Save new list to file
        	saveItems();
    	} 
    	else {
    		Toast.makeText(this, "Enter a valid item to add!", Toast.LENGTH_SHORT).show();
    	}
    }
    
    public void setupListViewListener() {
    	lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {
    		@Override
    		public boolean onItemLongClick(AdapterView<?> parent, View view, 
    									   int position, long rowId) {
    			items.remove(position);
    			itemsAdapter.notifyDataSetChanged();
    			
    			//save new list to file
    			saveItems();
    			
    			return true;
    		}
		});
    	
    	lvItems.setOnItemClickListener(new OnItemClickListener() {
    		@Override
    		public void onItemClick(AdapterView<?> parent, View view, 
    								   int position, long rowId) {
    			
    			String currItem = items.get(position);
    			launchEditView(currItem, position);
    		}
    		
    		private void launchEditView(String editItem, int position) {
    			  Intent i = new Intent(TodoActivity.this, EditItemActivity.class);
    			  
    			  // data to pass goes in extras
    			  i.putExtra("item", editItem); 
    			  i.putExtra("position", position); 
    			  
    			  // brings up the edit activity
    			  startActivityForResult(i, EDIT_REQUEST_CODE);
    		}
		});
    }
    
    private void loadItems() {
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "todo.txt");
    	
    	try {
    		items = new ArrayList<String>(FileUtils.readLines(todoFile));
    	} catch(IOException ioe) {
    		items = new ArrayList<String>();
    		ioe.printStackTrace();
    	}
    }
    
    private void saveItems() {
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "todo.txt");
    	
    	try {
    		FileUtils.writeLines(todoFile, items);
    	} catch(IOException ioe) {    		
    		Toast.makeText(this, "ERROR: Couldn't save todo list!", Toast.LENGTH_SHORT).show();
    		ioe.printStackTrace();
    	}
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
         String itemStr = data.getExtras().getString("item");
         int position   = data.getExtras().getInt("position");
         
         //Update list and refresh display
         items.remove(position);
         items.add(position, itemStr);
		 itemsAdapter.notifyDataSetChanged();
			
		 //save new list to file
		 saveItems();

         Toast.makeText(this, "Item editted successfully!", Toast.LENGTH_SHORT).show();
      }
    }
}
