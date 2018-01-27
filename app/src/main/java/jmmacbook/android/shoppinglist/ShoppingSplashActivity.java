package jmmacbook.android.shoppinglist;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

// Starting activity that serves as the loading screen before the main app is loaded
public class ShoppingSplashActivity extends AppCompatActivity
{
    Animation splashAnim;

    @Bind(R.id.shoppingCartIcon)
    ImageView shoppingCartIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_splash);

        ButterKnife.bind(this);

        splashAnim = AnimationUtils.loadAnimation(ShoppingSplashActivity.this, R.anim.splash_anim);
        shoppingCartIcon.startAnimation(splashAnim);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashIntent = new Intent(ShoppingSplashActivity.this, MainActivity.class);
                startActivity(splashIntent);
            }
        }, 3000); //change back to 3000 after debugging
    }
}
