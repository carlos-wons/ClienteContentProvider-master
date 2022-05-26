package com.example.clientecontentprovider;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.UserDictionary;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView cajaMostrar;
    EditText txtNombre;
    EditText txtApellido;
    EditText txtID;
    String mostrarUsuarios="";

    private void consultarContentProvider(){
        Cursor cursor = getContentResolver().query(
                UsuarioContrato.CONTENT_URI,
                UsuarioContrato.COLUMNS_NAME,
                null,null,null
        );
         mostrarUsuarios="";
        if(cursor!=null) {

            while (cursor.moveToNext()) {
                Log.d("CPCliente",
                        cursor.getInt(0) + " - " + cursor.getString(1)

                );
                mostrarUsuarios=mostrarUsuarios.concat(cursor.getInt(0) + " - " + cursor.getString(1)
                        +" "+cursor.getString(2)+"\n");
            }
        }else{
            Log.d("USUARIOCONTENTPROVIDER",
                    "NO DEVUELVE"
            );
        }
        cajaMostrar.setText(mostrarUsuarios);

    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cajaMostrar= findViewById(R.id.txtMostrar);
        txtNombre=findViewById(R.id.txtNombre);
        txtApellido=findViewById(R.id.txtApellido);
        txtID=findViewById(R.id.txtID);


        Cursor c = getContentResolver().query(UserDictionary.Words.CONTENT_URI,
                new String[] {UserDictionary.Words.WORD,
                        UserDictionary.Words.LOCALE},
                null,null,null
                );
        if(c!=null) {
            while (c.moveToNext()) {
                Log.d("DICCIONARIOUSARUI",
                        c.getString(0) + " - " + c.getString(1)
                );
            }
        }

        consultarContentProvider();

        findViewById(R.id.btnInsert).setOnClickListener(
                view -> {

                    ContentValues cv = new ContentValues();
                    cv.put(UsuarioContrato.COLUMN_FIRSTNAME,txtNombre.getText().toString() );
                    cv.put(UsuarioContrato.COLUMN_LASTNAME, txtApellido.getText().toString());

                    Uri uriInsert = getContentResolver().insert(
                            UsuarioContrato.CONTENT_URI,
                            cv
                    );
                    Log.d("CPCliente", uriInsert.toString() );
                    Toast.makeText(this, "Usuario insertado: \n"+
                            uriInsert.getLastPathSegment(), Toast.LENGTH_SHORT).show();

                }
        );


        findViewById(R.id.btnUpdate).setOnClickListener(
                view -> {

                    ContentValues cv = new ContentValues();
                    cv.put(UsuarioContrato.COLUMN_FIRSTNAME, txtNombre.getText().toString() );
                    cv.put(UsuarioContrato.COLUMN_LASTNAME, txtApellido.getText().toString() );

                    int elemtosAfectados = getContentResolver().update(
                            Uri.withAppendedPath(UsuarioContrato.CONTENT_URI, txtID.getText().toString() )   ,
                            cv,
                            null, null
                    );
                    Toast.makeText(this, "Usuario actualizado", Toast.LENGTH_SHORT).show();
                }
        );

        findViewById(R.id.btnConsultar).setOnClickListener(v -> {
            consultarContentProvider();
        });


        findViewById(R.id.btnDelete).setOnClickListener(v ->{
            int elemtosAfectados = getContentResolver().delete(
                    Uri.withAppendedPath(UsuarioContrato.CONTENT_URI,txtID.getText().toString() ),
                    null, null);
            Log.d("CPCliente", "Elementos afectados eliminados: " +elemtosAfectados );
            Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show();

        });

        findViewById(R.id.btnConsultarApellido).setOnClickListener(v->{
            Cursor cursor = getContentResolver().query(
                    Uri.withAppendedPath(UsuarioContrato.CONTENT_URI,txtApellido.getText().toString() ),
                    UsuarioContrato.COLUMNS_NAME,
                    null,null,null
            );
            if(cursor!=null) {

                String mostrarUsuarios="";
                while (cursor.moveToNext()) {
                    Log.d("CPCliente",
                            cursor.getInt(0) + " - " + cursor.getString(1)
                    );
                    mostrarUsuarios=mostrarUsuarios.concat(cursor.getInt(0) + " - " + cursor.getString(1)
                            +" "+cursor.getString(2)+"\n");
                    cajaMostrar.setText(mostrarUsuarios);
                }
            }else{
                Log.d("USUARIOCONTENTPROVIDER", "NO DEVUELVE");
                Toast.makeText(this,"No existe el usuario", Toast.LENGTH_SHORT ).show();
            }

        });


    }
}