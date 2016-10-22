package com.example.fugliara.ccs002.views.tracking;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fugliara.ccs002.R;
import com.example.fugliara.ccs002.server.AzureResponse;
import com.example.fugliara.ccs002.server.AzureService;
import com.example.fugliara.ccs002.server.dataobjects.ImageItem;
import com.example.fugliara.ccs002.server.dataobjects.OrderStatusItem;
import com.example.fugliara.ccs002.server.dataobjects.UserRatingItem;
import com.example.fugliara.ccs002.views.fragments.Camera;
import com.example.fugliara.ccs002.views.fragments.CameraPosResult;
import com.example.fugliara.ccs002.views.fragments.ProgressWait;
import com.example.fugliara.ccs002.views.provider.ProviderService;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;

import java.io.File;
import java.util.Date;

public class StatusActivity extends AppCompatActivity {
    private static final String TAG = "TAG_StatusActivity";
    private StatusActivity passThis = this;
    private OrderStatusItem status;
    private boolean isSender;

    private RatingBar ratingBarStatus;
    private TextView textViewStatus;
    private Button buttonStatus;
    private Button buttonExtra;
    private Button buttonUpdatePoints;
    private Button buttonPhoto;
    private ImageView mImageView;

    private Camera camera;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Camera.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            final ProgressWait progress = new ProgressWait(passThis, "Salvando Foto");
            progress.show();
            camera.onCameraResult(requestCode, resultCode, "status", new CameraPosResult() {
                @Override
                public void action(final File to, final ImageItem img) {
                    status.imageItems.add(img);

                    AzureService.getInstance().getOrderController().updateOrderStatusItem(new AzureResponse<OrderStatusItem>() {
                        @Override
                        public void receive(MobileServiceList<OrderStatusItem> result) {
                            status = result.get(0);
                            status.imageItems.clear();

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
                            Log.d(TAG, "insertImage()");
                        }
                    }, status);
                }

                @Override
                public void fail() {
                    Log.d(TAG, "onCameraResult()");
                    progress.dismiss();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        ratingBarStatus = (RatingBar) findViewById(R.id.ratingBarStatus);
        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        buttonStatus = (Button) findViewById(R.id.buttonStatus);
        buttonExtra = (Button) findViewById(R.id.buttonExtra);
        buttonPhoto = (Button) findViewById(R.id.buttonPhoto);
        buttonUpdatePoints = (Button) findViewById(R.id.buttonUpdatePoints);
        mImageView = (ImageView) findViewById(R.id.imageView);

        camera = new Camera(this);

        ratingBarStatus.setVisibility(View.GONE);
        textViewStatus.setVisibility(View.GONE);
        buttonStatus.setVisibility(View.GONE);
        buttonPhoto.setVisibility(View.GONE);
        buttonExtra.setVisibility(View.GONE);
        mImageView.setVisibility(View.GONE);

        buttonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.dispatchTakePictureIntent();
            }
        });

        buttonUpdatePoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressWait progress;
                progress = new ProgressWait(passThis, "Atualizar");
                progress.show();

                AzureService.getInstance().getOrderController().getOrderStatus(new AzureResponse<OrderStatusItem>() {
                    @Override
                    public void receive(MobileServiceList<OrderStatusItem> result) {
                        try {
                            status = result.get(0);
                            status.imageItems.clear();
                            isSender = status.orderItem.user.id.equals(ProviderService.getInstance().getSessionItemView().userItem.id);
                            updateActivity();

                        } catch (Exception exception) {
                            Intent intent = new Intent(passThis, TrackingActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            Toast.makeText(passThis, "Falha ao atualizar status", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "atualizar status() Error: " + exception.getMessage());
                        }
                        progress.dismiss();
                    }

                    @Override
                    public void fail() {
                        progress.dismiss();
                        Toast.makeText(passThis, "Falha ao atualizar", Toast.LENGTH_SHORT).show();
                    }
                }, getIntent().getStringExtra("orderId"));
            }
        });

        buttonExtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.statusItem.type += 2;

                final ProgressWait progress;
                progress = new ProgressWait(passThis, "Atualizando");
                progress.show();

                AzureService.getInstance().getOrderController().updateOrderStatusItem(new AzureResponse<OrderStatusItem>() {
                    @Override
                    public void receive(MobileServiceList<OrderStatusItem> result) {
                        try {
                            status = result.get(0);
                            status.imageItems.clear();
                            updateActivity();
                        } catch (Exception exception) {
                            Toast.makeText(passThis, "Falha ao atualizar", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "atualizar status() Error: " + exception.getMessage());
                        }

                        progress.dismiss();
                    }

                    @Override
                    public void fail() {
                        progress.dismiss();
                        Toast.makeText(passThis, "Falha ao atualizar", Toast.LENGTH_SHORT).show();
                    }
                }, status);
            }
        });

        buttonStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.statusItem.type < 9) {
                    if (status.statusItem.type == 8) {
                        saveUserRating();
                    }
                    status.statusItem.type += 1;
                } else {
                    saveUserRating();
                    status.statusItem.type = 98;
                }
                final ProgressWait progress;
                progress = new ProgressWait(passThis, "Atualizando");
                progress.show();

                AzureService.getInstance().getOrderController().updateOrderStatusItem(new AzureResponse<OrderStatusItem>() {
                    @Override
                    public void receive(MobileServiceList<OrderStatusItem> result) {
                        try {
                            status = result.get(0);
                            status.imageItems.clear();
                            updateActivity();
                        } catch (Exception exception) {
                            Toast.makeText(passThis, "Falha ao atualizar", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "atualizar status() Error: " + exception.getMessage());
                        }

                        progress.dismiss();
                    }

                    @Override
                    public void fail() {
                        progress.dismiss();
                        Toast.makeText(passThis, "Falha ao atualizar", Toast.LENGTH_SHORT).show();
                    }
                }, status);
            }
        });

        final ProgressWait progress;
        progress = new ProgressWait(passThis, "Buscando");
        progress.show();

        AzureService.getInstance().getOrderController().getOrderStatus(new AzureResponse<OrderStatusItem>() {
            @Override
            public void receive(MobileServiceList<OrderStatusItem> result) {
                try {
                    status = result.get(0);
                    status.imageItems.clear();
                    isSender = status.orderItem.user.id.equals(ProviderService.getInstance().getSessionItemView().userItem.id);

                    updateActivity();

                } catch (Exception exception) {
                    Intent intent = new Intent(passThis, TrackingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Toast.makeText(passThis, "Falha ao buscar status", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "buscar status() Error: " + exception.getMessage());
                }
                progress.dismiss();
            }

            @Override
            public void fail() {
                progress.dismiss();
                Intent intent = new Intent(passThis, TrackingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                Toast.makeText(passThis, "Falha ao buscar", Toast.LENGTH_SHORT).show();
            }
        }, getIntent().getStringExtra("orderId"));
    }

    private void updateActivity() {
        ratingBarStatus.setVisibility(View.GONE);
        textViewStatus.setVisibility(View.GONE);
        buttonStatus.setVisibility(View.GONE);
        buttonExtra.setVisibility(View.GONE);
        buttonPhoto.setVisibility(View.GONE);
        mImageView.setVisibility(View.GONE);

        String infoText = "";
        if (isSender) { // quem envia
            switch (status.statusItem.type) {
                case 6:
                    buttonExtra.setText("Qualificar");
                    buttonExtra.setVisibility(View.VISIBLE);
                    break;
                case 7:
                    buttonStatus.setText("Qualificar");
                    buttonStatus.setVisibility(View.VISIBLE);
                    break;
                case 8:
                    infoText = "Qualifique quem transportou o produto.";
                    buttonStatus.setText("Enviar");
                    buttonStatus.setVisibility(View.VISIBLE);
                    ratingBarStatus.setVisibility(View.VISIBLE);
                    break;
            }
        } else { // quem transporta
            switch (status.statusItem.type) {
                case 1:
                    infoText = "Confirme que está disponivel para transportar o produto.";
                    buttonStatus.setText("Confirmar Transporte");
                    buttonStatus.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    infoText = "Fotografe o produto e informe que ele foi retirado para transporte.";
                    buttonStatus.setText("Produto Retirado");
                    buttonStatus.setVisibility(View.VISIBLE);
                    buttonPhoto.setVisibility(View.VISIBLE);
                    mImageView.setImageResource(android.R.color.transparent);
                    mImageView.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    infoText = "Notifique que iniciou o transporte do produto.";
                    buttonStatus.setText("Saiu para Transporte");
                    buttonStatus.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    infoText = "Fotografe o produto e entregue como combinado.";
                    buttonStatus.setText("Chegou ao Destino");
                    buttonStatus.setVisibility(View.VISIBLE);
                    buttonPhoto.setVisibility(View.VISIBLE);
                    mImageView.setImageResource(android.R.color.transparent);
                    mImageView.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    infoText = "Notifique foi realizada a entrega.";
                    buttonStatus.setText("Produto Entregue");
                    buttonStatus.setVisibility(View.VISIBLE);
                    //buttonExtra.setText("Produto Entregue a Pessoa");
                    //buttonExtra.setVisibility(View.VISIBLE);
                    break;
                case 9:
                    infoText = "Qualifique quem agendou o envio.";
                    buttonStatus.setText("Enviar");
                    buttonStatus.setVisibility(View.VISIBLE);
                    ratingBarStatus.setVisibility(View.VISIBLE);
                    break;
            }
        }

        String address = "não definido";
        String addressRetirada = "não definido";
        try{
            address = status.orderItem.productDetailItems.get(0).contentDescription;
            addressRetirada = status.orderItem.productDetailItems.get(0).address;
        }catch (Exception e){
            Log.d(TAG, "address() Error: " + e.getMessage());
        }

        textViewStatus.setText(infoText + "\n\n" + status.statusItem.name + "(" + status.dateTime + ")\n\nEndereços:\nRetirada(" + addressRetirada + ")\nEntrega(" + address +  ")");
        textViewStatus.setVisibility(View.VISIBLE);
    }

    public void saveUserRating() {
        UserRatingItem rating = null;
        try {
            rating = new UserRatingItem();
            rating.dateTime = new Date().toString();
            rating.orderItem = status.orderItem;
            rating.userSource = ProviderService.getInstance().getSessionItemView().userItem;
            if (isSender) {
                rating.userDestin = status.orderItem.route.user;
            } else {
                rating.userDestin = status.orderItem.user;
            }
            rating.comments = "Sucesso!";
            rating.value = Math.round(ratingBarStatus.getRating());
        } catch (Exception exception) {
            Toast.makeText(passThis, "Falha na qualificação", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "saveUserRating() Error: " + exception.getMessage());
        }
        final ProgressWait progress;
        progress = new ProgressWait(passThis, "Qualificando");
        progress.show();

        AzureService.getInstance().getUserController().addUserRating(new AzureResponse<UserRatingItem>() {
            @Override
            public void receive(MobileServiceList<UserRatingItem> result) {
                progress.dismiss();
                Toast.makeText(passThis, "Qualificado com sucesso", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void fail() {
                progress.dismiss();
                Toast.makeText(passThis, "Falha ao qualificar", Toast.LENGTH_SHORT).show();
            }
        }, rating);
    }
}
