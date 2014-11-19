package com.longtech;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;

public class MyCalcActivity extends Activity implements OnClickListener {
	public static final int OP_NONE = 1000;
	public static final int OP_EQU = 1001;
	public static final int OP_DOT = 1002;
	public static final int OP_ADD = 1003;
	public static final int OP_SUB = 1004;
	public static final int OP_MUL = 1005;
	public static final int OP_DIV = 1006;
	public static final int OP_NEG = 1007;
	public static final int OP_C = 1008;
	public static final int OP_CE = 1009;
	public static final int OP_DEL = 1010;
	
	static ArrayList<CalcObject> entryList = null;
	static int opToPerform = OP_NONE;
	static int [] Button = {
		R.id.button0,
		R.id.button1,
		R.id.button2,
		R.id.button3,
		R.id.button4,
		R.id.button5,
		R.id.button6,
		R.id.button7,
		R.id.button8,
		R.id.button9,
		R.id.buttonBack,
		R.id.buttonC,
		R.id.buttonCE,
		R.id.buttonDivide,
		R.id.buttonDot,
		R.id.buttonEquals,
		R.id.buttonMinus,
		R.id.buttonMultiply,
		R.id.buttonNegate,
		R.id.buttonPlus
	};
	
	public class CalcObject {
		public float num = 0;
		public int op = OP_NONE;
		
		public CalcObject(int op, float num) {
			this.op = op;
			this.num = num;
		}
	}
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //Tell all of the buttons in the array to use the onClick()
        //event handler defined in this class 
        Button btn = null;
        for(int id : Button) {
        	btn = (Button)this.findViewById(id);
        	btn.setOnClickListener(this);
        }
        
        entryList = new ArrayList<CalcObject>();
    }

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		//If button0 was clicked, call numberButton(0).
		case R.id.button0: numberButton(0); break;
		//If button1 was clicked, call numberButton(1).
		case R.id.button1: numberButton(1); break;
		//...
		case R.id.button2: numberButton(2); break;
		case R.id.button3: numberButton(3); break;
		case R.id.button4: numberButton(4); break;
		case R.id.button5: numberButton(5); break;
		case R.id.button6: numberButton(6); break;
		case R.id.button7: numberButton(7); break;
		case R.id.button8: numberButton(8); break;
		case R.id.button9: numberButton(9); break;
		case R.id.buttonBack: numberButton(OP_DEL); break;
		case R.id.buttonDot: numberButton(OP_DOT); break;
		case R.id.buttonC: operationButton(OP_C); break;
		case R.id.buttonCE: operationButton(OP_CE); break;
		case R.id.buttonDivide: operationButton(OP_DIV); break;
		case R.id.buttonEquals: operationButton(OP_EQU); break;
		case R.id.buttonMinus: operationButton(OP_SUB); break;
		case R.id.buttonMultiply: operationButton(OP_MUL); break;
		case R.id.buttonNegate: operationButton(OP_NEG); break;
		case R.id.buttonPlus: operationButton(OP_ADD); break;
		default: break;
		}
	}
	
	void numberButton(int op) {
	  //button0-9 + buttonBack
		if(op != OP_DEL) {
			//Add the number or dot to the right side of the EditText content
			getEntryEditText().append(op == OP_DOT ? "." : op + "");
		} else {
			//Get the text from the EditText control
			String buf = getEntryEditText().getText().toString();
			if(buf.length() > 1) {
				//If there's multiple characters in the text,
				//delete the last character in the window
				getEntryEditText().setText(buf.substring(0, buf.length() - 1));
			} else {
				//If there's 1 or less characters to delete,
				//just make sure the box is empty
				getEntryEditText().setText("");
			}
		}
	}
	
	void operationButton(int op) {
		//If we have no data to operate on, just return
		if(getEntryEditText().getText().length() < 1) {
			//TODO: If there's no data to operate on, grab the newest value
			//      from the calculator's "memory buffer" and use that instead
			//      So we can chain operations like: 2 + 2 = 4 / 3 = 1.334
			return;
		}
		
		//Grab the data from the EditText and convert it to a float
		float entry = 0;
		try {
			entry = Float.parseFloat(getEntryEditText().getText().toString());
			//This value will be stored in the operations ArrayList until the user hits 'Enter'
			entryList.add(new CalcObject(op, entry));
			//Clear the textbox
			getEntryEditText().setText("");
		} catch(Exception ex) {return;}
		
		//This is how you perform debug logging for Android
		//Log.d("MyCalc", "Number: " + entry + ", Operation: " + op);
		
		//We won't perform any calculations until the user presses the = button
		if(op == OP_EQU) {
			//When we do, we'll iterate through the list, and perform the
			//operations on the numbers we logged in the list
			float results = 0;
			int lastOp = OP_NONE;
			for(CalcObject cObj : entryList) {
				switch(cObj.op) {
				case OP_NONE: results = cObj.num; break;
				case OP_ADD: results += cObj.num; break;
				case OP_SUB: results -= cObj.num; break;
				case OP_MUL: results *= cObj.num; break;
				case OP_DIV: results /= cObj.num; break;
				//This won't work! Will negate the entire result,
				//not just the number it's bound to
				//case OP_NEG: results *= cObj.num; break;
				
				//case OP_C: break; //Clear TextBox
				//case OP_CE: break; //Clear Memory Buffer
				//case OP_DEL: break; //Delete newest entry from Memory Buffer
				//You could also add M+/M-/MC/MR/MS memory functionality here...
				case OP_EQU:
					switch(lastOp) {
					case OP_ADD: results += entry; break;
					case OP_SUB: results -= entry; break;
					case OP_MUL: results *= entry; break;
					case OP_DIV: results /= entry; break;
					}
					break;
				default: break;
				}
				
				lastOp = cObj.op;
			}
			
			//Finally, reset the operations list,
			entryList.clear();
			entryList.add(new CalcObject(OP_NONE, results));
			
			// and display the result
			getEntryEditText().setText(results + "");
		}
	}
	
	protected EditText getEntryEditText() {
		return (EditText)this.findViewById(R.id.entryEditText);
	}
}

