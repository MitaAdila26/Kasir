package com.example.acer.kasir;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Pembelian extends AppCompatActivity {
    TextView txt1, txt2, txt3, txt4;
    EditText jumlahBeli;
    Button beli, ok;
    protected Cursor cursor;
    DataHelper dbHelper;
    int harganya,total, stokLama, idnya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembelian);
        txt1=(TextView)findViewById(R.id.namaBarang);
        txt2=(TextView)findViewById(R.id.harga);
        txt3=(TextView)findViewById(R.id.stok);
        jumlahBeli=(EditText)findViewById(R.id.edt_jumlahBeli);
        txt4=(TextView)findViewById(R.id.totalBeli);
        ok=(Button)findViewById(R.id.ok);

        dbHelper=new DataHelper(this);
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        cursor=db.rawQuery("SELECT*FROM kasir WHERE namaBarang='"+
                getIntent().getStringExtra("nama")+"'",null);

        cursor.moveToFirst();
        if(cursor.getCount()>0)
        {
            cursor.moveToPosition(0);
            idnya=cursor.getInt(0);
            stokLama=cursor.getInt(4);
            harganya=cursor.getInt(3);

            txt2.setText(Integer.toString(harganya));
            txt3.setText(Integer.toString(stokLama));
            txt1.setText(cursor.getString(1));
            }
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    total=harganya*Integer.parseInt(jumlahBeli.getText().toString());
                    String totalnya=Integer.toString(total);
                    txt4.setText(totalnya);
                }
            });

        beli=(Button)findViewById(R.id.btn_beli);

        beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                int jmlBeli=Integer.parseInt(jumlahBeli.getText().toString());
                int stokBaru=stokLama-jmlBeli;

                db.execSQL("update kasir set stok='"+ stokBaru +"' where id= '"+ idnya+"'");

                finish();
            }
        });


    }
}
