package material.kcci.mystudio;

import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static android.graphics.Color.YELLOW;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    private final int FRAGMENT1 = 1;
    private final int FRAGMENT2 = 2;

    private Button bt_tab1, bt_tab2;

    private Camera camera = null;
    public static boolean STATE = false;
    TextView setValue;
    float inputValue;
    int testNum = 0;
    float testNum2 = 0;
    Access access = new Access();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camera = Camera.open(1);
        camera.setFaceDetectionListener(new FaceDetect());

        // 위젯에 대한 참조
        bt_tab1 = (Button) findViewById(R.id.bt_tab1);
        bt_tab2 = (Button) findViewById(R.id.bt_tab2);

        // 탭 버튼에 대한 리스너 연결
        bt_tab1.setOnClickListener(this);
        bt_tab2.setOnClickListener(this);
        setValue = (TextView) findViewById(R.id.stateWindow);

        Thread thread = new setText();
        Log.d("TAG_11","thread Start");
        thread.start();
        // 임의로 액티비티 호출 시점에 어느 프레그먼트를 프레임레이아웃에 띄울 것인지를 정함
        callFragment(FRAGMENT1);
    }

    public class setText extends Thread
    {
        @Override
        public void run()
        {
            Log.d("여기오니?", "여기 오는거지?");
            float test = access.getDistanceValue();

            if (test > 30.0)
            {
                setValue.setBackgroundColor(Color.GREEN);
                Log.d("TAG", "RED");
            } else if (test < 30.0 && test > 10.0)
            {
                setValue.setBackgroundColor(YELLOW);
                Log.d("TAG", "YELLOW");
            } else
            {
                setValue.setBackgroundColor(Color.RED);
                Log.d("TAG", "RED");
            }
        }
    }

    protected void distanceValue(String distance)
    {
        inputValue = Float.parseFloat(distance);
        access.setDistanceValue(inputValue);
        Log.d("tag_10",String.valueOf(inputValue));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_tab1 :
                // '버튼1' 클릭 시 '프래그먼트1' 호출
                callFragment(FRAGMENT1);
                break;

            case R.id.bt_tab2 :
                // '버튼2' 클릭 시 '프래그먼트2' 호출
                callFragment(FRAGMENT2);
                break;
        }
    }

    private void callFragment(int frament_no){

        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (frament_no){
            case 1:
                // '프래그먼트1' 호출
                Fragment1 fragment1 = new Fragment1();
                transaction.replace(R.id.fragment_container, fragment1);
                transaction.commit();
                break;

            case 2:
                // '프래그먼트2' 호출
                Fragment2 fragment2 = new Fragment2();
                transaction.replace(R.id.fragment_container, fragment2);
                transaction.commit();
                break;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        camera.startFaceDetection();
        camera.startPreview();
        STATE = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.stopFaceDetection();
        camera.stopPreview();
        STATE = false;

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (STATE != true) {
            camera.startFaceDetection();
            camera.startPreview();
            STATE = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        camera.stopFaceDetection();
        camera.stopPreview();
        STATE = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.release();
        camera = null;
        STATE = false;
    }

    class FaceDetect implements Camera.FaceDetectionListener{
        @Override
        public void onFaceDetection(Camera.Face[] faces, Camera camera) {
            if (faces.length > 0){
                Log.d("FaceDetection", "face detected: "+ faces.length +
                        " Face 1 Location X: " + faces[0].rect.centerX() +
                        "Y: " + faces[0].rect.centerY() );
            }
        }

    }
}