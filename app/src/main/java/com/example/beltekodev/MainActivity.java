package com.example.beltekodev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.beltekodev.databinding.ActivityMainBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    SharedPreferences sharedPreferences;

    ArrayAdapter arrayAdapter;

    ArrayList<String> dataList;

    String gender;

    private boolean isValidDate(String inputDate) {
        // Girilen tarihi kontrol etmek için bir SimpleDateFormat kullanıyoruz
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        dateFormat.setLenient(true);
//
//        // Bu ifade hata vermez, çünkü 32 Ocak gibi bir tarih kabul edilir
//        Date date = dateFormat.parse("32/01/2022");
//        ********************************************************************************
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        dateFormat.setLenient(false);
//
//        // Bu ifade ParseException hatası verir, çünkü 32 Ocak gibi bir tarih geçerli değildir
//        Date date = dateFormat.parse("32/01/2022");

        try {
            Date date = dateFormat.parse(inputDate);


            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            int month=cal.get(Calendar.MONTH);
            //girilen yılın 4 basamaklı olması koşulu gerçekleştirilir.
            if (String.valueOf(year).length() == 4) {
                if(0<month && month<=12){
                    return true;  // Eğer tarih uygunsa true döner
                }else {
                    return false;// Ay 12 den büyük ve 0 dan küçük olurda false döner
                }
            } else {
                return false; // Yıl dört basamaklı değilse false döner
            }
        } catch (ParseException e) {
            return false; // ParseException hatası alınırsa, tarih uygun değildir
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences=this.getSharedPreferences("com.example.beltekodev",MODE_PRIVATE);

        dataList=new ArrayList<>();

        arrayAdapter=new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,android.R.id.text1, dataList);
        binding.dataLV.setAdapter(arrayAdapter);


        //Uygulama tekrar başlatıldığında sharedpreferences ile kaydedilen ifadeleri listview da görüntülemek için kullanılan kod satırları
//        dataList.add(sharedPreferences.getString("nameSurname","-1"));
//        dataList.add(sharedPreferences.getString("birthDay","-1"));
//        dataList.add(sharedPreferences.getString("hobi","-1"));
//        dataList.add(sharedPreferences.getString("gender","-1"));
//        arrayAdapter.notifyDataSetChanged();

        binding.listeleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dataList.clear();

                String nameSurname = binding.nameSurnameEdt.getText().toString();

                if (TextUtils.isEmpty(nameSurname)){
                    Toast.makeText(MainActivity.this, "Ad Soyad Bölümünü Boş Bırakmayınız", Toast.LENGTH_SHORT).show();
                }
                else{
                    sharedPreferences.edit().putString("nameSurname",nameSurname).apply();

                    String birthDay=binding.birthDayEdt.getText().toString();
                    Boolean isBirtday=isValidDate(birthDay);
                    if(isBirtday){
                        sharedPreferences.edit().putString("birthDay",birthDay).apply();

                        binding.genderRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                RadioButton selectedButton=findViewById(radioGroup.getCheckedRadioButtonId());
                                gender=selectedButton.getText().toString();
                            }
                        });

                        if(TextUtils.isEmpty(gender)){
                            Toast.makeText(MainActivity.this, "Lütfen Cinsiyet Seçmeyi Unutmayınız", Toast.LENGTH_SHORT).show();
                        }else{
                            sharedPreferences.edit().putString("gender",gender).apply();

                            String hobi=binding.hobiEdt.getText().toString();
                            sharedPreferences.edit().putString("hobi",hobi).apply();

                            dataList.add(sharedPreferences.getString("nameSurname","-1"));
                            dataList.add(sharedPreferences.getString("birthDay","-1"));
                            dataList.add(sharedPreferences.getString("hobi","-1"));
                            dataList.add(sharedPreferences.getString("gender","-1"));

                            //veri setindeki değişiklikleri bildirmek için kullanılır
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Lütfen Doğum Tarihinizi Doğru Giriniz", Toast.LENGTH_SHORT).show();
                    }
                }



            }
        });

        binding.dataLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

    }
}