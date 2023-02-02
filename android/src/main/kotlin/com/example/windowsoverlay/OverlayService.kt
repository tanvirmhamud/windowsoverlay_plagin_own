package com.example.windowsoverlay
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.os.*
import android.util.DisplayMetrics
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.work.WorkManager
import com.google.gson.Gson
import okhttp3.*
import okio.IOException
import org.json.JSONArray
import java.net.URL


class overlayservice() : Service(), View.OnTouchListener, View.OnClickListener, GestureDetector.OnGestureListener {

    private var moving = false;
    private var initialtouchy = 0.0f;
    private var initialtuchx = 0.0f;
    private var initialy = 0;
    private var initialx = 0;
    lateinit var windomanager: WindowManager;
    lateinit var overlaybutton : ImageButton;
    lateinit var params : WindowManager.LayoutParams;
    lateinit var closeparams : WindowManager.LayoutParams;
    lateinit var overlayout : View ;
    lateinit var closelayout : View ;
    lateinit var gson : Gson;
    lateinit var teamascore: TextView;
    lateinit var teambscore: TextView;
    lateinit var teama: ImageView;
    lateinit var teamb: ImageView;
    lateinit var teamaname : TextView;
    lateinit var teambname : TextView;
    lateinit var matchruntime : TextView;
    lateinit var mainlayout : LinearLayout;
    lateinit var loadinglayout : LinearLayout;
    lateinit var resultreceiver : ResultReceiver;
    private val client = OkHttpClient()
    lateinit var  workManager : WorkManager;
    lateinit var  handler : Handler;
    lateinit var roundable : Runnable;
    lateinit var cardview : CardView;
    lateinit var gesuredector : GestureDetector;
    lateinit var packagemanager : PackageManager;
    private lateinit var context: Context
    private lateinit var activity: Activity
    private lateinit var packagename : String;
    private lateinit var displayMetrics : DisplayMetrics;
    private  var height : Int = 0;
    private  var width : Int = 0;
    lateinit var removelayout : CardView;
    lateinit var getintent: Intent;

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate() {
        super.onCreate()
        handler = Handler();
         context = this;
        Toast.makeText(this, "service created", Toast.LENGTH_SHORT).show();
        windomanager = getSystemService(WINDOW_SERVICE) as WindowManager;

        width = Resources.getSystem().getDisplayMetrics().widthPixels
        height =  Resources.getSystem().getDisplayMetrics().heightPixels;
        println(height)
        overlayout = LayoutInflater.from(this).inflate(R.layout.overlayui,null);
        closelayout = LayoutInflater.from(this).inflate(R.layout.remove,null);
        overlayout.setOnTouchListener(this);
        gesuredector = GestureDetector(this, this);
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

        // close layout

        var closelayoutglag = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else{
            WindowManager.LayoutParams.TYPE_PHONE;
        }

        closeparams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            closelayoutglag,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        closeparams.gravity = Gravity.BOTTOM
        closeparams.x = 0;
        closeparams .y = 100;
        windomanager.addView(closelayout, closeparams);
        cardview = overlayout.findViewById(R.id.card_view);
        teamascore = overlayout.findViewById(R.id.team_a_score);
        teambscore = overlayout.findViewById(R.id.team_b_score);
        teama =overlayout.findViewById(R.id.team_a);
        teamb = overlayout.findViewById(R.id.team_b);
        teamaname = overlayout.findViewById(R.id.team_a_name);
        teambname = overlayout.findViewById(R.id.team_b_name);
        matchruntime = overlayout.findViewById(R.id.match_time);
        mainlayout =overlayout.findViewById(R.id.mainlayot);
        loadinglayout = overlayout.findViewById(R.id.loading_indi);
        removelayout = closelayout.findViewById(R.id.remove);
        workManager = WorkManager.getInstance(application)


    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null);
        if (overlayout != null){
            windomanager.removeView(overlayout);
        }
        if (closelayout != null){
            windomanager.removeView(closelayout);
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null;
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var matchid = intent!!.getIntExtra("matchid",980339);
        var token = intent.getStringExtra("token");
        getintent = intent;
        packagemanager = context.packageManager;
        packagename = context.packageName;
        streamdata(matchid, token!!)
        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("ResourceAsColor")
    override fun onTouch(view: View?, event: MotionEvent): Boolean {

            gesuredector.onTouchEvent(event)
        if (event != null) {
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    removelayout.visibility = View.VISIBLE;
                    initialx = params.x;
                    initialy = params.y;
                    initialtuchx = event.rawX;
                    initialtouchy = event.rawY
                    moving = true;

                }
                MotionEvent.ACTION_UP ->{
                    removelayout.visibility = View.GONE;
                    moving = false;
                }

                MotionEvent.ACTION_MOVE -> {
                    params.x = initialx + (event.rawX - initialtuchx).toInt()
                    params.y = initialy + (event.rawY - initialtouchy).toInt()
                    windomanager.updateViewLayout(overlayout, params)
                    val location = IntArray(2)
                    closelayout.getLocationOnScreen(location)
                    if (params.y + 250 >= location[1]){
                        println("tanvir" + location[1]);
                        removelayout.setCardBackgroundColor(R.color.cardview_light_background);
                        handler.removeCallbacksAndMessages(null);
                        stopService(getintent)
                    }else{
                        removelayout.setCardBackgroundColor(R.color.black);
                    }

                }
                MotionEvent.ACTION_CANCEL->{

                }

            }
        }
        return  true;
    }
    override fun onClick(p0: View?) {
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
                    var obj = JSONArray(response.body!!.string())
                    val teamaurl = URL(obj.getJSONObject(0).getJSONObject("teams").getJSONObject("home").getString("logo"))
                    val teamabmp: Bitmap =
                        BitmapFactory.decodeStream(teamaurl.openConnection().getInputStream())
                    val teamburl = URL(obj.getJSONObject(0).getJSONObject("teams").getJSONObject("away").getString("logo"))
                    val teambbmp: Bitmap =
                        BitmapFactory.decodeStream(teamburl.openConnection().getInputStream())

                    Handler(Looper.getMainLooper()).post {
                        teama.setImageBitmap(teamabmp);
                        teamb.setImageBitmap(teambbmp);
                        teamaname.text = obj.getJSONObject(0).getJSONObject("teams").getJSONObject("home").getString("name");
                        teambname.text = obj.getJSONObject(0).getJSONObject("teams").getJSONObject("away").getString("name");
                        matchruntime.text = "${obj.getJSONObject(0).getJSONObject("fixture").getJSONObject("status").getInt("elapsed")}'"
                        teamascore.text = obj.getJSONObject(0).getJSONObject("goals").getInt("home").toString();
                        teambscore.text = obj.getJSONObject(0).getJSONObject("goals").getInt("away").toString();
                        mainlayout.visibility = View.VISIBLE;
                        loadinglayout.visibility = View.GONE;
                    }
                }
            }
        })
    }

//    fun  getmatchdetails(matchid : Int, token : String) {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://gtvcricketlive.com/fixtures/")
//            .build()
//
//        val service: GitHubService = retrofit.create(GitHubService::class.java)
//
//        service.listRepos(matchid.toString(),token)!!.enqueue(object :Callback<ArrayList<Matchdetail?>?>{
//            override fun onResponse(
//                call: Call<ArrayList<Matchdetail?>?>,
//                response: Response<ArrayList<Matchdetail?>?>
//            ) {
//                println(response.body().toString())
//                if (response.isSuccessful){
//                    val teamaurl = URL(response.body()?.get(0)!!.teams!!.home!!.logo)
//                    val teamabmp: Bitmap =
//                        BitmapFactory.decodeStream(teamaurl.openConnection().getInputStream())
//                    val teamburl = URL(response.body()?.get(0)!!.teams!!.away!!.logo)
//                    val teambbmp: Bitmap =
//                        BitmapFactory.decodeStream(teamburl.openConnection().getInputStream())
//                    Handler(Looper.getMainLooper()).post {
//                        teama.setImageBitmap(teamabmp);
//                        teamb.setImageBitmap(teambbmp)
//                        teamascore.text = response.body()?.get(0)!!.goals!!.home.toString();
//                        teambscore.text = response.body()?.get(0)!!.goals!!.away.toString();
//                        mainlayout.visibility = View.VISIBLE;
//                        loadinglayout.visibility = View.GONE;
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<ArrayList<Matchdetail?>?>, t: Throwable) {
//                println(t.localizedMessage);
//                TODO("Not yet implemented")
//            }
//        });
//
//    }

    fun streamdata(matchid: Int, token : String) {
        var t = 0

        handler.postDelayed(object : Runnable {
            override fun run() {
                t++
                getrun(matchid,token);
                handler.postDelayed(this, 1000)
            }
        }, 1000)
    }

    override fun onDown(p0: MotionEvent): Boolean {
        removelayout.visibility = View.VISIBLE;
        return  false;
    }

    override fun onShowPress(p0: MotionEvent) {


    }

    override fun onSingleTapUp(p0: MotionEvent): Boolean {
         var data = packagemanager.getLaunchIntentForPackage(packagename);
        startActivity(data)
        return true;
    }

    override fun onScroll(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {

        return  false;
    }

    override fun onLongPress(p0: MotionEvent) {


    }

    override fun onFling(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        println("flag")
        return  false;
    }





}




