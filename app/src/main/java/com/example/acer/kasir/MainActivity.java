package com.example.acer.kasir;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    String[]tambah;
    ListView listView;
    Menu menu;
    protected Cursor cursor;
    DataHelper dbcenter;
    public  static MainActivity utama;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button tombol=(Button)findViewById(R.id.button);
        tombol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent buat = new Intent(MainActivity.this,TambahBarang.class);
                startActivity(buat);
            }
        });

        utama=this;
        dbcenter=new DataHelper(this);
        Refreshlist();

    }

    public void Refreshlist(){
        SQLiteDatabase db=dbcenter.getReadableDatabase();
        cursor=db.rawQuery("SELECT*FROM kasir",null);
        tambah=new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc=0; cc<cursor.getCount();cc++){
            cursor.moveToPosition(cc);
            tambah[cc]=cursor.getString(1).toString();
        }
        listView=(ListView)findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter(this,android.R.layout.simple_list_item_1,tambah));
        listView.setSelected(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                final  String selection=tambah[arg2];//.getItemAtPosition(arg2).toString();
                final CharSequence[]dialogitem={"Jual", "Beli", "Hapus"};
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Pilihan");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item){
                            case 0:
                                Intent jual=new Intent(getApplicationContext(), Pembelian.class);
                                jual.putExtra("nama",selection);
                                startActivity(jual);
                                break;
                            case 1:
                                Intent beli=new Intent(getApplicationContext(),Penjualan.class);
                                beli.putExtra("nama",selection);
                                startActivity(beli);
                                break;
                            case 2:
                                SQLiteDatabase db=dbcenter.getWritableDatabase();
                                db.execSQL("delete from kasir where nama='"+selection+"'");
                                Refreshlist();
                                break;

                        }
                    }
                });
                builder.create().show();
            }});
        ((ArrayAdapter)listView.getAdapter()).notifyDataSetInvalidated();
    }
}
