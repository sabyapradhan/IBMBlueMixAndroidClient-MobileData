package shastra.com.ibmbluemixandroidclient;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.mobile.services.core.IBMBluemix;
import com.ibm.mobile.services.data.IBMData;
import com.ibm.mobile.services.data.IBMDataException;
import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMQuery;

import java.util.List;

import javax.xml.transform.Result;

import bolts.Continuation;
import bolts.Task;


public class MainActivity extends Activity {
    private  static final String CLASS_NAME = MainActivity.class.getName();
    private static final String APP_ID = "91f5bf17-dbad-458e-8959-b3eab458d4a4";
    private static final String APP_SECRET = "4395619432f6f7aa3a7ac5145ab26a3a11c827a0";
    private static final String APP_ROUTE = "BlueMixDevSachi.mybluemix.net";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize the IBM core backend-as-a-service
        IBMBluemix.initialize(this, APP_ID, APP_SECRET, APP_ROUTE);
        // initialize the IBM Data Service
        IBMData.initializeService();
        // register the Item Specialization
        MessageText.registerSpecialization(MessageText.class);
    }

    public void onClickPushMessage(View view){
        EditText textView = (EditText) findViewById(R.id.textView);
        String txt = textView.getText().toString();
         new UpdateText().execute( txt != null ? txt : "");
    }

    public void onClickClear(View view){
        SetText("");
        Toast.makeText(this, R.string.clear_text,Toast.LENGTH_LONG).show();
    }

    private void SetText(String txt) {
        EditText textView = (EditText) findViewById(R.id.textView);
        textView.setText(txt);
    }

    public void onClickGetMessage(View view){
        new GetText().execute();
    }

    public class GetText extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                IBMQuery<MessageText> query = IBMQuery.queryForClass(MessageText.class);
                // Query all the Item objects from the server.
                query.find().continueWith(new Continuation<List<MessageText>, Void>() {

                    @Override
                    public Void then(Task<List<MessageText>> task) throws Exception {
                        final List<MessageText> objects = task.getResult();
                        // Log if the find was cancelled.
                        if (task.isCancelled()) {
                            Log.e(CLASS_NAME, "Exception : Task " + task.toString() + " was cancelled.");
                        }
                        // Log error message, if the find task fails.
                        else if (task.isFaulted()) {
                            Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
                        }

                        // If the result succeeds, load the list.
                        else {
                            MessageText msgText = null;
                            StringBuffer outputTxt = new StringBuffer();
                            for (IBMDataObject item : objects) {
                                msgText = (MessageText) item;
                                outputTxt.append(msgText.getName()).append("\n");
                             }
                            SetText(outputTxt.toString());
                        }
                        return null;
                    }
                }, Task.UI_THREAD_EXECUTOR);

            } catch (IBMDataException error) {
                Log.e(CLASS_NAME, "Exception : " + error.getMessage());
            }
        return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getApplicationContext(), R.string.pull_succeeded,Toast.LENGTH_LONG).show();
        }

    }

    public class UpdateText extends AsyncTask<String,Void,Void>
    {
        @Override
        protected Void doInBackground(String... params) {
            String txt = params[0];
            MessageText msgText = new MessageText();
            msgText.setName(txt);

            msgText.save().continueWith(new Continuation<IBMDataObject, Void>() {

                @Override
                public Void then(Task<IBMDataObject> task) throws Exception {
                    // Log if the save was cancelled.
                    if (task.isCancelled()) {
                        Log.e(MainActivity.class.getName(), "Exception : Task " + task.toString() + " was cancelled.");
                    }
                    // Log error message, if the save task fails.
                    else if (task.isFaulted()) {
                        Log.e(MainActivity.class.getName(), "Exception : " + task.getError().getMessage());
                    }
                    return null;
                }

            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getApplicationContext(), R.string.msg_updated,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
