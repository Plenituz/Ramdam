package com.plenituz.ramdam;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.plenituz.ramdam.utils.P;
import com.plenituz.ramdam.utils.SoundPlayer;

public class MainActivity extends Activity {

    public static final String PAGE_URL = "http://ramdam.fm/audio/";
    public static String RADIO_URL = "";//"http://5.196.67.109:8000/live.mp3";
    public static final int HIDE_PLAY = 1;
    public static final int SHOW_PLAY = 2;
    public static final int HIDE_TEXT = 3;

    public static PlayView playView;
    public static TextView waitingText;
    public static CacaHandler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            getActionBar().hide();
        }catch (Exception e){}
        P.init(this);
        SoundPlayer.init();
        setContentView(R.layout.activity_main);

        handler = new CacaHandler();

        //on fabrique le layout dans le code comme un gros tas
        RelativeLayout mainLayout = ((RelativeLayout) findViewById(R.id.mainLay));
        RelativeLayout sub = new RelativeLayout(this);

        View v = new View(this){
            Paint paint;
            @Override
            protected void onDraw(Canvas canvas) {
                if(paint == null){
                    paint = new Paint();
                    paint.setColor(Color.parseColor("#B74848"));
                    paint.setStyle(Paint.Style.FILL);
                }
                canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
            }
        };
        v.setLayoutParams(new RelativeLayout.LayoutParams(P.poc(1.0f)[0], P.poc(0.6f)[1]));
        sub.addView(v);


        playView = new PlayView(this, P.poc(0.2f)[0], P.poc(0.2f)[0]);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        playView.setLayoutParams(params);
        sub.addView(playView);
        playView.setVisibility(RadioService.isActive ? View.INVISIBLE : View.VISIBLE);

        sub.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ((RelativeLayout.LayoutParams)sub.getLayoutParams()).setMargins(0, P.poc(0.4f)[1], 0, 0);
        mainLayout.addView(sub);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playView.performClick();
            }
        });

        //playView.setOnClickListener(); add the on click after the fetching is done

        waitingText = new TextView(this);
        RelativeLayout.LayoutParams par = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        waitingText.setText(getString(R.string.searching));
        waitingText.setLayoutParams(par);
        sub.addView(waitingText);

        new Thread(new Runnable() {
            @Override
            public void run() {
               // try {
                    /*Document doc = Jsoup.connect(PAGE_URL).get();
                    Element element = doc.getElementById("jquery_jplayer_2");
                    String s = element.attr("data-mp3");
                    Log.v("attr", s);
                    RADIO_URL = s;*/
                    //CHANGE THE RADIO URL HERE IF YOU HAVE TO CHANGE IT MANUALLY
                    //I KEEP THE REST OF THE CODE IN CASE THE WEBSITE COMES BACK UP
                    RADIO_URL = "http://5.196.67.109:8000/live.mp3";
                    playView.setOnClickListener(playViewOnClick());
                    handler.sendEmptyMessage(HIDE_TEXT);
               /*} catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
        }).start();
    }

    View.OnClickListener playViewOnClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RadioService.class);
                i.putExtra("posX", P.poc(0.395f)[0]);
                i.putExtra("posY", P.poc(0.633f)[1]);
                i.setAction(RadioService.ACTION_NEW_BUBBLE);
                startService(i);
                Toast.makeText(getBaseContext(), "Tu peux quitter l'appli, la bulle te suit !", Toast.LENGTH_LONG).show();
            }
        };
    }

    /**
     * Handle le caca :)
     */
    static class CacaHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HIDE_PLAY:
                    if(playView != null)
                        playView.setVisibility(View.INVISIBLE);
                    break;
                case SHOW_PLAY:
                    if(playView != null)
                        playView.setVisibility(View.VISIBLE);
                    break;
                case HIDE_TEXT:
                    if(waitingText != null)
                        waitingText.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    }
}
