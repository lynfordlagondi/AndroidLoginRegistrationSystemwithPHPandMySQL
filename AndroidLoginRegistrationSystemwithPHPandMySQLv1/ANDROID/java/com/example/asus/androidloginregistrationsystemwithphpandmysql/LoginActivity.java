package com.example.asus.androidloginregistrationsystemwithphpandmysql;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {


    /**
     * tags
     */
    private static final String TAG = LoginActivity.class.getSimpleName();

    /**
     * progress dialog
     */
    private ProgressDialog progressDialog;

    /**
     * input text
     */
    private EditText input_email;
    private EditText input_password;

    /**
     * listeners
     */
    private Button btnLogin;
    private TextView register_now;
    private AlertDialog alertdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        input_email = (EditText) findViewById(R.id.Email);
        input_password = (EditText) findViewById(R.id.Password);
        btnLogin = (Button) findViewById(R.id.button_login);
        register_now = (TextView) findViewById(R.id.register_now);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setCancelable(false);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = input_email.getText().toString();
                String password = input_password.getText().toString();

                if (email.equals("")){
                    input_email.setError("Email required.");
                }else if (password.equals("")){
                    input_password.setError("Password required.");
                }else {

                    UserAuthentication(email,password);

                    input_email.setText("");
                    input_password.setText("");
                }
            }
        });

        register_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = getLayoutInflater();
                View conView = inflater.inflate(R.layout.activity_register,null);


                final EditText txtFullname = (EditText) conView.findViewById(R.id.input_fullname);
                final EditText txtEmail = (EditText) conView.findViewById(R.id.input_email);
                final EditText txtPassword = (EditText) conView.findViewById(R.id.input_password);
                final Button btnRegister = (Button) conView.findViewById(R.id.button_register);


                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setView(conView).setNegativeButton("close X", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertdialog.dismiss();
                    }
                });

                btnRegister.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String name = txtFullname.getText().toString();
                        String email = txtEmail.getText().toString();
                        String password = txtPassword.getText().toString();

                        if (name.equals("")){

                            txtFullname.setError("Fullname required!");

                        }else if (email.equals("")){

                            txtEmail.setError("Email required!");

                        }else if (password.equals("")){

                            txtPassword.setError("Password required!");

                        }else {

                            alertdialog.dismiss();
                            RegisterUser(name,email,password);

                            txtFullname.setText("");
                            txtEmail.setText("");
                            txtPassword.setText("");
                        }
                    }
                });

                alertdialog = builder.create();
                alertdialog.show();
            }
        });

    }

    /**
     * User Registration
     * @param name
     * @param email
     * @param password
     */
    private void RegisterUser(final String name, final String email, final String password) {

        progressDialog.setMessage("Registering...");
        showpDialog();
        String tag_register = "";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.REGISTRATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG,"json response: " + response.toString());
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (jsonObject.getBoolean("error")==false){
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    Log.d(TAG,"json exception: " + e.getMessage());
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"json response something error: " + error.getMessage());
                Toast.makeText(LoginActivity.this, "json response something error.", Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        }){
            protected Map<String,String>getParams(){

                Map<String,String>maps = new HashMap<String,String>();
                maps.put("fullname",name);
                maps.put("email",email);
                maps.put("password",password);
                Log.d(TAG,"maps requests: " + maps.toString());
                return maps;
            }
        };

        MyApplication.getInstance().addToRequestQueue(stringRequest,tag_register);
    }

    /**
     * User Authentication
     * @param email
     * @param password
     */
    private void UserAuthentication(final String email, final String password) {

        String tag_request= "";
        progressDialog.setMessage("Authenticating...");
        showpDialog();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.AUTHENTICATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            Log.d(TAG,"json response: "  + response.toString());
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (jsonObject.getBoolean("error") == false){
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    Log.d(TAG,"json exception: " + e.getMessage());
                }

                hidepDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d(TAG,"json response down:" + error.getMessage());
                Toast.makeText(LoginActivity.this, "server error", Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        }){
            protected Map<String,String>getParams(){
                Map<String,String>params = new HashMap<String, String>();

                params.put("email",email);
                params.put("password",password);
                Log.d(TAG,"maps: " + params.toString());
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(stringRequest,tag_request);
    }

    private void hidepDialog() {

        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void showpDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
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
