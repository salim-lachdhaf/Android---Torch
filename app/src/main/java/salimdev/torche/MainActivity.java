package salimdev.torche;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;
import java.io.File;
import static salimdev.torche.Widget.remoteViews;


public class MainActivity extends Activity {

    public static Button btnSwitch;
    public static Camera camera;
    static Camera.Parameters params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // flash switch button
        btnSwitch = (Button) findViewById(R.id.btnSwitch);
        ImageView share = (ImageView) findViewById(R.id.share);
        ImageView other = (ImageView) findViewById(R.id.other);
        ImageView rate = (ImageView) findViewById(R.id.rate);

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = "salimdev.torche"; // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (Exception anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String devName = "SalimDev"; // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:"+devName)));
                } catch (Exception anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/search?q=pub:" + devName)));
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareApplication();
            }
        });
        // First check if device is supporting flashlight or not
        boolean hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasFlash) {
            // device doesn't support flash
            // Show alert message and close the application
            AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
            alert.setTitle("Error");
            alert.setMessage("Sorry, your device doesn't support flash light!");
            alert.setButton(DialogInterface.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alert.show();
            return;
        }

        if(camera==null){
            btnSwitch.setBackgroundResource(R.drawable.widgete_off);
        }else{
            btnSwitch.setBackgroundResource(R.drawable.widgete_on);
        }
        // Switch button click event to toggle flash on/off
        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToggleCamera(getApplicationContext());
            }
        });
    }
    private void shareApplication() {
        ApplicationInfo app = getApplicationContext().getApplicationInfo();
        String filePath = app.sourceDir;
        Intent intent = new Intent(Intent.ACTION_SEND);
        // MIME of .apk is "application/vnd.android.package-archive".
        // but Bluetooth does not accept this. Let's use "*/*" instead.
        intent.setType("*/*");

        // Append file and send Intent
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
        startActivity(Intent.createChooser(intent, "Share app via"));
    }

    static void ToggleCamera(Context context) {
        if (camera != null) {
            try {
                if(btnSwitch==null){
                    btnSwitch = new Button(context);
                }
                btnSwitch.setBackgroundResource(R.drawable.widgete_off);
                if(remoteViews==null)
                    remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
                remoteViews.setImageViewResource(R.id.btnSwitche, R.drawable.widgete_off);

                camera.lock();
                camera.stopPreview();
                camera.release();
                camera = null;
            } catch (Exception e) {
                Log.e("Camera Error Open. ", e.getMessage());
            }
        } else {
            try {
                if(btnSwitch==null){
                    btnSwitch = new Button(context);
                }
                btnSwitch.setBackgroundResource(R.drawable.widgete_on);
                if(remoteViews==null)
                    remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
                remoteViews.setImageViewResource(R.id.btnSwitche, R.drawable.widgete_on);
                camera = Camera.open();
                params = camera.getParameters();
                params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(params);
                camera.startPreview();
            } catch (Exception e) {
                Toast.makeText(context, "Camera Error Open. " + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Camera Error Open. ", e.getMessage());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
