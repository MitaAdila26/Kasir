package com.example.acer.kasir;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Penjualan extends AppCompatActivity {
    TextView nama, harga, supplier, total;
    EditText jumlahBeli;
    Button jual, oke;
    protected Cursor cursor;
    DataHelper dbHelper;
    int id, stokLama, harganya, total1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan);
        nama=(TextView)findViewById(R.id.nama);
        harga=(TextView)findViewById(R.id.harga2);
        supplier=(TextView)findViewById(R.id.supplier);
        total=(TextView)findViewById(R.id.total2);
        jumlahBeli=(EditText)findViewById(R.id.edt_jmlBeli);

        jual=(Button)findViewById(R.id.btn_jual);
        oke=(Button)findViewById(R.id.oke);

        dbHelper=new DataHelper(this);
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        cursor=db.rawQuery("SELECT*FROM kasir WHERE namaBarang='"+
                getIntent().getStringExtra("nama")+"'",null);

        cursor.moveToFirst();
        if(cursor.getCount()>0)
        {
            cursor.moveToPosition(0);
            id=cursor.getInt(0);
            nama.setText(cursor.getString(1));
            supplier.setText(cursor.getString(2));

            harganya=cursor.getInt(3);
            harga.setText(Integer.toString(harganya));

            stokLama=cursor.getInt(4);

        }
            oke.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   total1=harganya*Integer.parseInt(jumlahBeli.getText().toString());
                   String totalnya=Integer.toString(total1);
                   total.setText(totalnya);
                }
            });


        jual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db=dbHelper.getWritableDatabase();

                int jmlBeli=Integer.parseInt(jumlahBeli.getText().toString());
                int stokBaru=stokLama+jmlBeli;

                db.execSQL("update kasir set stok='"+ stokBaru +"' where id= '"+ id+"'");

                finish();
            }
        });
    }
}
