package org.jumpmind.symmetric.symmetricandroidclientdemo;

import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang.StringUtils;
import org.jumpmind.symmetric.android.SQLiteOpenHelperRegistry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String INSERT_SQL = "insert into SALE_TRANSACTION (STORE_ID, WORKSTATION, DAY, SEQ) values ('301','3','1',55)";

    private static final String SELECT_SQL = "select * from ITEM";

    private final int COLUMN_WIDTH = 14;

    /**
     * onCreate is called when Android starts this Activity from scratch.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final EditText sqlTextField = (EditText)findViewById(R.id.sqlTextField);
        final TextView outputTextView = (TextView)findViewById(R.id.outputTextView);
        outputTextView.setMovementMethod(new ScrollingMovementMethod());

        Button runButton = (Button)findViewById(R.id.runButton);
        runButton.setOnClickListener(v -> {
            String sql = sqlTextField.getText().toString();

            SQLiteDatabase db = SQLiteOpenHelperRegistry.lookup(DbProvider.DATABASE_NAME).getReadableDatabase();
            Cursor cursor = null;
            try {
                cursor = db.rawQuery(sql, null);
            } catch (Exception ex) {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, ex.toString(), duration);
                toast.show();
                ex.printStackTrace();
                return;
            }

            String tableFormat = "";
            StringBuilder buff = new StringBuilder();

            while (cursor.moveToNext()) {
                List<String> values = new ArrayList<String>();
                String[] columnNames = cursor.getColumnNames();
                if (buff.length() == 0) {
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        tableFormat += "%-" + COLUMN_WIDTH + "s|";
                    }

                    for (int j = 0; j < columnNames.length; j++) {
                        columnNames[j] = StringUtils.abbreviate(columnNames[j], COLUMN_WIDTH);
                    }

                    buff.append(String.format(tableFormat, columnNames)).append("\n");
                }

                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    values.add(StringUtils.abbreviate(cursor.getString(i), COLUMN_WIDTH));
                }
                buff.append(String.format(tableFormat, values.toArray(new String[0]))).append("\n");
            }

            outputTextView.setText(buff.toString());

            Context context = getApplicationContext();
            CharSequence text = "Executed " + sql;
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        });

        Button clearButton = (Button)findViewById(R.id.clearButton);
        clearButton.setOnClickListener(v -> {
            sqlTextField.setText("");
            outputTextView.setText("");
        });

        Button selectButton = (Button)findViewById(R.id.selectButton);
        selectButton.setOnClickListener(v -> sqlTextField.setText(SELECT_SQL));

        Button insertButton = (Button)findViewById(R.id.insertButton);
        insertButton.setOnClickListener(v -> sqlTextField.setText(INSERT_SQL));

        Button newFileButton = (Button)findViewById(R.id.newFileButton);
        newFileButton.setOnClickListener(v -> {
            String fileName = sqlTextField.getText().toString();
            File newFile = new File(getStorageDir(v.getContext(), "SymDS").getAbsoluteFile() + "/" + fileName);
            try {
                newFile.createNewFile();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public File getStorageDir(Context context, String directoryName) {
        File file =
                new File(Environment.getExternalStoragePublicDirectory("/"), directoryName);

        System.out.println("canRead " + Environment.getExternalStorageDirectory().canRead());
        System.out.println("canWrite " + Environment.getExternalStorageDirectory().canWrite());

        boolean ok = file.mkdirs(); //create folders where write files
        if (!ok) {
            CharSequence text = "Failed to create dir " + file;
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        return file;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return false;
    }

}
