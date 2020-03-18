package com.example.tema2;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText e1, e2;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.my_recycle_view);
        recyclerView.setHasFixedSize(true);
        e1 = findViewById(R.id.edit1);
        e2 = findViewById(R.id.edit2);
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser();
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
            }
        });
        getUsers();
    }

    private void getUsers()
    {
        LinearLayoutManager x = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(x);
        class GetUsers extends AsyncTask<Void,Void, List<User>>
        {
            @Override
            protected List<User> doInBackground(Void... voids) {
                List<User> users = DatabaseUser.getInstance(getApplicationContext()).getAppDatabase().userDAO().getAll();
                return users;
            }

            @Override
            protected void onPostExecute(List<User> users) {
                super.onPostExecute(users);
                MyAdapter adapter = new MyAdapter(users);
                recyclerView.setAdapter(adapter);
            }
        }

        GetUsers getUsers = new GetUsers();
        getUsers.execute();
    }

    private boolean isInteger(String a)
    {
        for(int i =0;i<a.length();i++)
            if(a.charAt(i)>'9'||a.charAt(i)<'0') {
                return false;
            }
        return true;
    }

    private void saveUser()
    {
        final String mName = e1.getText().toString().trim();
        final String mMark= e2.getText().toString().trim();
        if(mName.isEmpty())
        {
            e1.setError("Name required");
            e1.requestFocus();
            return;
        }
        if(mMark.isEmpty())
        {
            e2.setError("Mark required");
            e2.requestFocus();
            return;
        }
        if(isInteger(mMark) & (Integer.parseInt(mMark)<1 ||Integer.parseInt(mMark)>10))
        {
            e2.setError("Mark must be a number between 1 and 10");
            e2.requestFocus();
            return;
        }
        class SaveTask extends AsyncTask<Void,Void, Void>
        {
            @Override
            protected Void doInBackground(Void... voids) {
                User user = new User();
                user.setName(mName);
                user.setMark(Integer.parseInt(mMark));
                DatabaseUser.getInstance(getApplicationContext()).getAppDatabase().userDAO().insert(user);
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_LONG).show();
            }
        }
        SaveTask saveTask = new SaveTask();
        saveTask.execute();
    }

    private void deleteUser()
    {
        final String mName = e1.getText().toString().trim();

        if(mName.isEmpty()){
            e1.setError("Name is required");
            e1.requestFocus();
            return;
        }

        class DeleteUser extends AsyncTask<Void,Void,Void>{
            Boolean found = false;
            @Override
            protected Void doInBackground(Void...voids){
                User user = DatabaseUser.getInstance(getApplicationContext()).getAppDatabase().userDAO().findByName(mName);
                if(user!=null) {
                    this.found = true;
                    DatabaseUser.getInstance(getApplicationContext()).getAppDatabase().userDAO().deleteByName(mName);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid){
                super.onPostExecute(aVoid);
                if(this.found == true) {
                    Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else {
                    e2.setText("");
                    Toast.makeText(getApplicationContext(), "Don't found", Toast.LENGTH_LONG).show();
                }

            }
        }
        DeleteUser deleteUser = new DeleteUser();
        deleteUser.execute();
    }
}
