package cn.ezandroid.ezfilter.demo;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import cn.ezandroid.ezfilter.EZFilter;
import cn.ezandroid.ezfilter.core.GLRender;
import cn.ezandroid.ezfilter.core.RenderPipeline;
import cn.ezandroid.ezfilter.core.environment.SurfaceFitView;
import cn.ezandroid.ezfilter.demo.render.OverlayMultiInput;
import cn.ezandroid.ezfilter.media.record.RecordableRender;
import cn.ezandroid.ezfilter.video.player.IMediaPlayer;

/**
 * MultiInputActivity
 *
 * @author like
 * @date 2018-07-13
 */
public class MultiVideoInputActivity extends BaseActivity {

    private SurfaceFitView mRenderView;
    private Button mRecordButton;

    private RenderPipeline mRenderPipeline;

    private RecordableRender mSupportRecord;

    private OverlayMultiInput mTwoInput;
    private EZFilter.Builder mLeftBuilder;
    private EZFilter.Builder mRightBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_video_input);

        mRenderView = $(R.id.render_view);
        mRecordButton = $(R.id.record);

        mLeftBuilder = EZFilter.input(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.test3))
                .setLoop(true)
                .setPreparedListener(new IMediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(IMediaPlayer var1) {
                        Log.e("MultiInputActivity", "onPrepared");
                    }
                })
                .setCompletionListener(new IMediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(IMediaPlayer var1) {
                        Log.e("MultiInputActivity", "onCompletion");
                    }
                });

        mRightBuilder = EZFilter.input(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.glitchfx_overlay))
                .setLoop(true)
                .setPreparedListener(new IMediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(IMediaPlayer var1) {
                        Log.e("MultiInputActivity", "onPrepared");
                    }
                })
                .setCompletionListener(new IMediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(IMediaPlayer var1) {
                        Log.e("MultiInputActivity", "onCompletion");
                    }
                });

        List<EZFilter.Builder> builders = new ArrayList<>();
        builders.add(mLeftBuilder);
        builders.add(mRightBuilder);

        mTwoInput = new OverlayMultiInput();
        mRenderPipeline = EZFilter.input(builders, mTwoInput)
                .enableRecord("/sdcard/recordOverly.mp4", true, false)
                .into(mRenderView, false);

        for (GLRender render : mRenderPipeline.getEndPointRenders()) {
            if (render instanceof RecordableRender) {
                mSupportRecord = (RecordableRender) render;
            }
        }

        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSupportRecord != null) {
                    if (mSupportRecord.isRecording()) {
                        stopRecording();
                    } else {
                        startRecording();
                    }
                }
            }
        });
    }

    private void startRecording() {
        mRecordButton.setText("Stop recording");
        if (mSupportRecord != null) {
            mSupportRecord.startRecording();
        }
    }

    private void stopRecording() {
        mRecordButton.setText("Start recording");
        if (mSupportRecord != null) {
            mSupportRecord.stopRecording();
        }
    }
}
