package com.rx.timer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.edit_mobi)
    EditText editMobi;
    @BindView(R.id.btn_send)
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    public void click(View view) {

        final int count = 60;
        MainActivity.countDown(count)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        btnSend.setEnabled(false);
                        btnSend.setTextColor(Color.BLACK);
                    }
                }).subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {

                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        btnSend.setText(aLong + "秒后可重新发送");
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        btnSend.setEnabled(true);
                        btnSend.setTextColor(Color.WHITE);
                        btnSend.setText("发送验证码");
                    }
        });

    }

    /**
     * interval:每隔一秒发送一条数据
     * take:总共发送的次数
     * @param countTime
     * @return
     */
    public static Observable<Long> countDown(int countTime){
        if(countTime < 0){
            countTime = 0;
        }
        final int time = countTime;
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(time + 1)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@NonNull Long aLong) throws Exception {
                        return time - aLong;  //long 值是从小到大，倒计时需要将值倒置
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());//切换到主线程更新UI
    }
}
