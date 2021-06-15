package com.neuronestech.visitor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.samepleapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button button_read;
    Button button_read_mrz;
    ImageView pic_result_iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_read = findViewById(R.id.button_read);
        button_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("SDK_VAL",32);
                intent.putExtra("token","3HQvX7SsimAA1udBQWktT4hS4w4VPizCQszKE57bhDWWLEY2etQwZdqwoRuMa2kh8HpHE");
                intent.setAction("com.sarwar.passportreader.REQUEST");
                startActivityForResult(intent, 0);
            }
        });
        button_read_mrz = findViewById(R.id.button_read_mrz);
        button_read_mrz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("SDK_VAL",33);
                intent.putExtra("token","3HQvX7SsimAA1udBQWktT4hS4w4VPizCQszKE57bhDWWLEY2etQwZdqwoRuMa2kh8HpHE");
                intent.setAction("com.sarwar.passportreader.mrz.REQUEST");
                startActivityForResult(intent, 0);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            try {
                String result = data.getStringExtra("data");
//                WrongPhoneNumberDIalog_finish_print(result);

                try {
                    showDIaloWithMessage(result);
                }catch (Exception ee){
                    ee.printStackTrace();
                    showDIaloWithMessage(result);
                }
            }catch (Exception ees ){
                ees.printStackTrace();

                String result = data.getStringExtra("data");
                showDIaloWithMessage(result);
//                WrongPhoneNumberDIalog_finish_print("Exception");
            }
            }
        if (resultCode == RESULT_CANCELED) {
                showDIaloWithMessage(null);
//            WrongPhoneNumberDIalog_finish_print("RESULT_CANCELED");
            }
    }

    AlertDialog alertDialog;
    public void showDIaloWithMessage(String message){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialo_result, null);
        dialogBuilder.setView(dialogView);
        ImageView image_view_verified =  dialogView.findViewById(R.id. image_view_verified);
        LinearLayout holder_container_ll = dialogView.findViewById(R.id.holder_container_ll);
        LinearLayout id_doc_holder_ll = dialogView.findViewById(R.id.id_doc_holder_ll);
        TextView is_expiry_text_m = dialogView.findViewById(R.id.is_expiry_text_m);
        JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(message);

                try {
                    ImageView image_view_head_nfc = dialogView.findViewById(R.id.image_view_head_nfc);
                    image_view_head_nfc.setImageBitmap(Base64ToBitmap(jsonObject.getString("photo")));
                }catch (Exception e){
                    e.printStackTrace();
                }

                EditText doc_num_et_nfc = dialogView.findViewById(R.id.doc_num_et_nfc);
                doc_num_et_nfc.setText(jsonObject.getString("doc_number"));
                EditText issue_date_et_nfc = dialogView.findViewById(R.id.issue_date_et_nfc);
                issue_date_et_nfc.setText(jsonObject.getString("place_of_issue"));
                EditText expiry_et_nfc_fr = dialogView.findViewById(R.id.expiry_et_nfc_fr);
                expiry_et_nfc_fr.setText(jsonObject.getString("date_of_expiry"));
                EditText birth_day_et_nfc_fr = dialogView.findViewById(R.id.birth_day_et_nfc_fr);
                birth_day_et_nfc_fr.setText(jsonObject.getString("date_of_birth"));
                EditText last_name_et_nfc = dialogView.findViewById(R.id.last_name_et_nfc);
                last_name_et_nfc.setText(jsonObject.getString("last_name"));
                EditText given_name_et_nfc = dialogView.findViewById(R.id.given_name_et_nfc);
                given_name_et_nfc.setText(jsonObject.getString("first_name"));

                EditText issue_place_et_nfc = dialogView.findViewById(R.id.issue_place_et_nfc);
                issue_place_et_nfc.setText(jsonObject.getString("place_of_issue"));

                EditText sex_et_nfc = dialogView.findViewById(R.id.sex_et_nfc);
                sex_et_nfc.setText(jsonObject.getString("sex"));
                EditText nationality_et_nfc = dialogView.findViewById(R.id.nationality_et_nfc);

                nationality_et_nfc.setText(jsonObject.getString("nationality"));

                boolean isExpiryOkay = jsonObject.getBoolean("expired");
                if(isExpiryOkay) {
                    is_expiry_text_m.setText("Not Expired");
                    is_expiry_text_m.setTextColor(getResources().getColor(R.color.darkgreen));
                }
                else {
                    is_expiry_text_m.setText("Expired");
                    is_expiry_text_m.setTextColor(getResources().getColor(R.color.red));
                }
                boolean docStatus = jsonObject.getBoolean("docStatus");
                if(docStatus){
                    image_view_verified.setImageDrawable(getResources().getDrawable(R.drawable.ok));
                }
                else{
                    image_view_verified.setImageDrawable(getResources().getDrawable(R.drawable.ko));
                }


                holder_container_ll.setVisibility(View.VISIBLE);
                id_doc_holder_ll .setVisibility(View.VISIBLE);
                doc_num_et_nfc.setEnabled(false);
                issue_date_et_nfc.setEnabled(false);
                expiry_et_nfc_fr.setEnabled(false);
                birth_day_et_nfc_fr.setEnabled(false);
                last_name_et_nfc.setEnabled(false);
                given_name_et_nfc.setEnabled(false);
                issue_place_et_nfc.setEnabled(false);
                sex_et_nfc.setEnabled(false);
                nationality_et_nfc.setEnabled(false);
            } catch (JSONException e) {
                holder_container_ll.setVisibility(View.GONE);
                id_doc_holder_ll .setVisibility(View.GONE);
                image_view_verified.setImageDrawable(getResources().getDrawable(R.drawable.ko));
                is_expiry_text_m.setText(message);
                is_expiry_text_m.setTextColor(getResources().getColor(R.color.red));
                e.printStackTrace();
            } catch (NullPointerException e) {
                holder_container_ll.setVisibility(View.GONE);
                id_doc_holder_ll .setVisibility(View.GONE);
                image_view_verified.setImageDrawable(getResources().getDrawable(R.drawable.ko));
                is_expiry_text_m.setText("Something went wrong");
                is_expiry_text_m.setTextColor(getResources().getColor(R.color.red));
                e.printStackTrace();
            }

        Button okay_button =  dialogView.findViewById(R.id.okay_button);
        okay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }


    public void WrongPhoneNumberDIalog_finish_print(String str) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(str);
        alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        alertDialog.cancel();
                        finish();
                    }
                });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static Bitmap Base64ToBitmap(String encodedImage){
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

}
