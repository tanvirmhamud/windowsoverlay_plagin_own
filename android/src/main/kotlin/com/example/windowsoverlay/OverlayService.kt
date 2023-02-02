package com.example.windowsoverlay

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.opengl.Visibility
import android.os.*
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.work.WorkManager
import com.google.gson.Gson
import okhttp3.*
import okio.IOException
import org.json.JSONArray
import java.net.URL
import java.util.concurrent.TimeUnit


class overlayservice() : Service(), View.OnTouchListener, View.OnClickListener {

    private var moving = false;
    private var initialtouchy = 0.0f;
    private var initialtuchx = 0.0f;
    private var initialy = 0;
    private var initialx = 0;
    lateinit var windomanager: WindowManager;
    lateinit var overlaybutton : ImageButton;
    lateinit var params : WindowManager.LayoutParams;
    lateinit var overlayout : View ;
    lateinit var gson : Gson;
    lateinit var teamascore: TextView;
    lateinit var teambscore: TextView;
    lateinit var teama: ImageView;
    lateinit var teamb: ImageView;
    lateinit var mainlayout : LinearLayout;
    lateinit var loadinglayout : LinearLayout;
    lateinit var resultreceiver : ResultReceiver;
    private val client = OkHttpClient()
    lateinit var  workManager : WorkManager;

    override fun onCreate() {
        super.onCreate()
        Toast.makeText(this, "service created", Toast.LENGTH_SHORT).show();
        windomanager = getSystemService(WINDOW_SERVICE) as WindowManager;
        overlaybutton = ImageButton(this);
        overlaybutton.setOnClickListener(this);
        overlaybutton.setOnTouchListener(this);
        overlayout = LayoutInflater.from(this).inflate(R.layout.overlayui,null);
        overlayout.setOnClickListener(this);
        overlayout.setOnTouchListener(this);
        var layoutglag = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else{
            WindowManager.LayoutParams.TYPE_PHONE;
        }
        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutglag,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.TOP or Gravity.START;
        params.x = 0;
        params.y = 100;
        windomanager.addView(overlayout, params);
        teamascore = overlayout.findViewById(R.id.team_a_score);
        teambscore = overlayout.findViewById(R.id.team_b_score);
        teama =overlayout.findViewById(R.id.team_a);
        teamb = overlayout.findViewById(R.id.team_b);
        mainlayout =overlayout.findViewById(R.id.mainlayot);
        loadinglayout = overlayout.findViewById(R.id.loading_indi);
        workManager = WorkManager.getInstance(application)

    }

    override fun onDestroy() {
        super.onDestroy()
        windomanager.removeView(overlayout);
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null;
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var matchid = intent!!.getIntExtra("matchid",980339);
        var token = intent.getStringExtra("token");
        streamdata(matchid, token!!)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        view!!.performClick();

        if (event != null) {
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    initialx = params.x;
                    initialy = params.y;
                    initialtuchx = event.rawX;
                    initialtouchy = event.rawY
                    moving = true;
                }
                MotionEvent.ACTION_UP ->{
                    moving = false;
                }
                MotionEvent.ACTION_MOVE -> {
                    params.x = initialx + (event.rawX - initialtuchx).toInt()
                    params.y = initialy + (event.rawY - initialtouchy).toInt()
                    windomanager.updateViewLayout(overlayout, params)

                }
            }
        }
        return  true;
    }
    override fun onClick(p0: View?) {


//        if (!moving) Toast.makeText(this, "click now", Toast.LENGTH_SHORT).show();
    }



    public fun getrun(matchid : Int, token : String) {

        val request = Request.Builder()
            .url("http://gtvcricketlive.com/fixtures/id=${matchid}")
            .addHeader("ab", "${token}")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
//
                    var obj = JSONArray(response.body!!.string())
                    // obj.getJSONObject(0).getJSONObject("teams").getJSONObject("away").getString("logo")
                    val teamaurl = URL(obj.getJSONObject(0).getJSONObject("teams").getJSONObject("home").getString("logo"))
                    val teamabmp: Bitmap =
                        BitmapFactory.decodeStream(teamaurl.openConnection().getInputStream())
                    val teamburl = URL(obj.getJSONObject(0).getJSONObject("teams").getJSONObject("away").getString("logo"))
                    val teambbmp: Bitmap =
                        BitmapFactory.decodeStream(teamburl.openConnection().getInputStream())

                    Handler(Looper.getMainLooper()).post {
                        teama.setImageBitmap(teamabmp);
                        teamb.setImageBitmap(teambbmp)
                        teamascore.text = obj.getJSONObject(0).getJSONObject("goals").getInt("home").toString();
                        teambscore.text = obj.getJSONObject(0).getJSONObject("goals").getInt("away").toString();
                        mainlayout.visibility = View.VISIBLE;
                        loadinglayout.visibility = View.GONE;
                    }
                }
            }
        })
    }

    fun streamdata(matchid: Int, token : String) {
        var t = 0
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                t++
                println("run"+t);
                getrun(matchid,token);
//                handler.postDelayed(this, 1000)
            }
        }, 1000)
    }


}