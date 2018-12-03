package com.findai.xkk.ai_interviewer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.findai.xkk.ai_interviewer.Dao.Question_Data_Exe;
import com.findai.xkk.ai_interviewer.domain.Question;
import com.findai.xkk.ai_interviewer.domain.QuestionList;
import com.findai.xkk.ai_interviewer.question_fragment.question_choose_fragment;
import com.findai.xkk.ai_interviewer.question_fragment.question_wenda_fragment;

import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,question_choose_fragment.callbackQuestion_Choose_Fragment {

    @Override
    public String get_question_answer(String answer) {
        System.out.println("ACTivity"+answer);
        current_question.setAnswer(answer);
        return answer;
    }

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private Camera camera;
    private SurfaceHolder sh;
    private SurfaceView sv_camera;
    private question_choose_fragment qcfragment = new question_choose_fragment(this);
    private question_wenda_fragment qwdfragment = new question_wenda_fragment();
    private FragmentManager fm = getSupportFragmentManager();
    private FragmentTransaction ft = fm.beginTransaction();
    private Matrix matrix = new Matrix();
    private SurfaceView sv_camera_face;
    private SurfaceHolder sv_camera_face_holder;
    private Button btn_next;
    private Question_Data_Exe question_data_exe;
    private Question current_question;
    private Button btn_former;
    private Iterator<Question> questionIterator;
    QuestionList questionList=null;
    //语音

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("index_get_questionlist_bundle");
        questionList = (QuestionList)bundle.getSerializable("questionlist");
        questionIterator = get_question_iterator(questionList);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        question_data_exe = new Question_Data_Exe(getBaseContext());
        init();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    private void init() {
        current_question = questionIterator.next();
        show_fragment(current_question);
        camera = Camera.open(1);

        sv_camera = findViewById(R.id.sv_camera);
        sv_camera_face = findViewById(R.id.sv_camera_face);
        btn_next = findViewById(R.id.btn_next_question);
        btn_next.setOnClickListener(this);

        sh = sv_camera.getHolder();
        sv_camera_face_holder = sv_camera_face.getHolder();
        sv_camera_face_holder.setFormat(PixelFormat.TRANSLUCENT);
//        sv_camera_face_holder.addCallback();

        sh.addCallback(new SurfaceCallback());
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 1);//1 can be another integer
        }



    }
    public Iterator<Question> get_question_iterator(QuestionList ql) {
        return ql.getQuestionList().iterator();
    }

    @Override
    public void onClick(View v) {
        if(questionIterator.hasNext()) {
            switch (v.getId()) {
                case R.id.btn_next_question:
                    current_question = questionIterator.next();
                    System.out.println(current_question.getTitle());
                    show_fragment(current_question);
                    if(!questionIterator.hasNext()){
                        btn_next.setText("完成面试");
                    }
                    break;

                default:
                    break;
            }
        }else{
            Intent intent = new Intent(this,SubmitReportActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("questionlist",questionList);
            intent.putExtra("bundle_questionlist",bundle);
            startActivity(intent);
        }
    }

    private void show_fragment(Question q) {
        Bundle bundle;
        ft = fm.beginTransaction();
        switch (q.type) {
            case 0:
                bundle = new Bundle();
                bundle.putSerializable("question",q);
                qcfragment = new question_choose_fragment(this);
                qcfragment.setArguments(bundle);
                ft.replace(R.id.fragment_question, qcfragment);
                break;
            case 1:
                bundle = new Bundle();
                bundle.putSerializable("question",q);
                qwdfragment.setArguments(bundle);
                ft.replace(R.id.fragment_question, qwdfragment);
                break;
            default:
                break;
        }
        ft.commit();

    }

    private class SurfaceCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            sh = holder;
            try {
                followScreenOrientation(getBaseContext(), camera);
                camera.startFaceDetection();
                camera.setFaceDetectionListener(new FaceDetectListener());

                final Camera.Parameters params = camera.getParameters();
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                params.setPictureSize(sv_camera.getWidth(), sv_camera.getHeight());
                params.setPreviewSize(sv_camera.getWidth(), sv_camera.getHeight());
                params.setSceneMode(Camera.Parameters.SCENE_MODE_BARCODE);
                params.setJpegQuality(100); // 设置照片质量

                camera.setPreviewDisplay(holder);
                camera.startPreview();
                Log.i("camera", "create");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.i("camera", "change");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            try{
                camera.setPreviewCallback(null);
                camera.stopPreview();
                camera.release();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private class FaceDetectListener implements Camera.FaceDetectionListener {
        @Override
        public void onFaceDetection(Camera.Face[] faces, Camera camera) {
//            Log.i("face_position",faces.toString());

//                Log.i("检测到了",face_rect.toString());
            try {

                Canvas clear_canvas = sv_camera_face_holder.lockCanvas();
                clear_canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                sv_camera_face_holder.unlockCanvasAndPost(clear_canvas);

                if (faces.length > 0) {
                    Rect face_rect = faces[0].rect;
                    Paint paint = new Paint();
                    paint.setColor(Color.parseColor("#222222"));
                    paint.setStrokeWidth(5f);
                    paint.setAlpha(180);
                    paint.setStyle(Paint.Style.STROKE);
                    Canvas face_canvas = sv_camera_face_holder.lockCanvas();
                    face_canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    //                    face_canvas.drawColor(Color.BLUE);

                    prepareMatrix(matrix, true, 90, sv_camera.getWidth(), sv_camera.getHeight());
                    RectF rf = new RectF(face_rect.left,
                            face_rect.top,
                            face_rect.right,
                            face_rect.bottom);
                    matrix.mapRect(rf);

                    face_rect = new Rect();
                    rf.round(face_rect);

                    face_canvas.drawRect(new Rect(face_rect.left,
                            face_rect.top + 5,
                            face_rect.right,
                            face_rect.bottom + 5), paint);//绘制矩形
                    sv_camera_face_holder.unlockCanvasAndPost(face_canvas);
                }
            } catch (Exception ex) {
                Log.i("出错了", "2");
            }

        }
    }

    public static void followScreenOrientation(Context context, Camera camera) {
        final int orientation = context.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            camera.setDisplayOrientation(180);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            camera.setDisplayOrientation(90);
        }
    }

    public static void prepareMatrix(Matrix matrix, boolean mirror, int displayOrientation,
                                     int viewWidth, int viewHeight) {
        // Need mirror for front camera.
        matrix.setScale(mirror ? -1 : 1, 1);
        // This is the value for android.hardware.Camera.setDisplayOrientation.
        matrix.postRotate(displayOrientation);
        // Camera driver coordinates range from (-1000, -1000) to (1000, 1000).
        // UI coordinates range from (0, 0) to (width, height).
        matrix.postScale(viewWidth / 2000f, viewHeight / 2000f);
        matrix.postTranslate(viewWidth / 2f, viewHeight / 2f);
    }



}
