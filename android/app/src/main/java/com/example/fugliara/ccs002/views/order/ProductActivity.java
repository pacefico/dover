package com.example.fugliara.ccs002.views.order;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.fugliara.ccs002.R;
import com.example.fugliara.ccs002.server.AzureResponse;
import com.example.fugliara.ccs002.server.AzureService;
import com.example.fugliara.ccs002.server.dataobjects.ImageItem;
import com.example.fugliara.ccs002.server.dataobjects.OrderItem;
import com.example.fugliara.ccs002.server.dataobjects.ProductDetailItem;
import com.example.fugliara.ccs002.server.dataobjects.ServiceTypeItem;
import com.example.fugliara.ccs002.views.fragments.Camera;
import com.example.fugliara.ccs002.views.fragments.CameraPosResult;
import com.example.fugliara.ccs002.views.fragments.ProgressWait;
import com.example.fugliara.ccs002.views.home.HomeActivity;
import com.example.fugliara.ccs002.views.provider.ProviderService;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;

import java.io.File;
import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {

    private static final String TAG = "TAG_ProductActivity";
    public ImageView mImageView;
    public ProductDetailItem productDetailItem;
    private ProductActivity passThis = this;
    private Button buttonPhoto;
    private Button buttonAgenda;
    private Camera camera;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Camera.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            final ProgressWait progress = new ProgressWait(passThis, "Salvando Foto");
            progress.show();

            camera.onCameraResult(requestCode, resultCode, "product", new CameraPosResult() {
                @Override
                public void action(final File to, final ImageItem img) {
                    productDetailItem.imageItems.add(img);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap imageBitmap = BitmapFactory.decodeFile(to.getAbsolutePath());
                            mImageView.setImageBitmap(imageBitmap);
                        }
                    });

                    progress.dismiss();
                }

                @Override
                public void fail() {
                    progress.dismiss();
                    Log.d(TAG, "onCameraResult()");
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        productDetailItem = new ProductDetailItem();
        productDetailItem.imageItems = new ArrayList<ImageItem>();

        mImageView = (ImageView) findViewById(R.id.imageViewPhoto);
        camera = new Camera(this);
        buttonPhoto = (Button) findViewById(R.id.buttonPhoto);

        buttonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.dispatchTakePictureIntent();
            }
        });

        buttonAgenda = (Button) findViewById(R.id.buttonProductSave);

        buttonAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean formOk = true;

                EditText editTextProductName = (EditText) findViewById(R.id.editTextProductName);
                EditText editTextProductDecricao = (EditText) findViewById(R.id.editTextProductDecricao);
                EditText editTextProductHeight = (EditText) findViewById(R.id.editTextProductHeight);
                EditText editTextProductWidth = (EditText) findViewById(R.id.editTextProductWidth);
                EditText editTextProductLength = (EditText) findViewById(R.id.editTextProductLength);
                EditText editTextProductWeightKg = (EditText) findViewById(R.id.editTextProductWeightKg);
                EditText editTextRetirada = (EditText) findViewById(R.id.editTextRetirada);
                RadioButton radioButtonHand = (RadioButton) findViewById(R.id.radioButtonHand);

                if( !editTextProductName.getText().toString().isEmpty() &&
                        !editTextProductDecricao.getText().toString().isEmpty() &&
                        !editTextProductHeight.getText().toString().isEmpty() &&
                        !editTextProductWidth.getText().toString().isEmpty() &&
                        !editTextProductLength.getText().toString().isEmpty() &&
                        !editTextProductWeightKg.getText().toString().isEmpty() &&
                        !editTextRetirada.getText().toString().isEmpty()
                        ) {
                    productDetailItem.title = editTextProductName.getText().toString();
                    productDetailItem.contentDescription = editTextProductDecricao.getText().toString();
                    productDetailItem.height = Integer.valueOf(editTextProductHeight.getText().toString());
                    productDetailItem.width = Integer.valueOf(editTextProductWidth.getText().toString());
                    productDetailItem.length = Integer.valueOf(editTextProductLength.getText().toString());
                    productDetailItem.weightKg = Integer.valueOf(editTextProductWeightKg.getText().toString());
                    productDetailItem.address = editTextRetirada.getText().toString();
                    productDetailItem.serviceTypeItem = new ServiceTypeItem();
                    if(radioButtonHand.isChecked()) {
                        productDetailItem.service = "Em Mãos";
                    }else{
                        productDetailItem.service = "No correio mais próximo";
                    }
                    if (formOk) {
                        final ProgressWait progress;
                        progress = new ProgressWait(passThis, "Agendando");
                        progress.show();

                        AzureService.getInstance().getOrderController().createOrder(new AzureResponse<OrderItem>() {
                            @Override
                            public void receive(MobileServiceList<OrderItem> result) {
                                progress.dismiss();
                                Intent intent = new Intent(passThis, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                Toast.makeText(passThis, "Agendado", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void fail() {
                                progress.dismiss();
                                Toast.makeText(passThis, "Falha ao agendar", Toast.LENGTH_SHORT).show();
                            }
                        }, ProviderService.getInstance().getSessionItemView().routeItem, productDetailItem);
                    }
                }else{
                    Toast.makeText(passThis, "Informações faltando", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
