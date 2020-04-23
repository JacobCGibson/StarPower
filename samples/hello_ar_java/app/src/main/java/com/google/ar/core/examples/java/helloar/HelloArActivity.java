/*
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.ar.core.examples.java.helloar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AtomicFile;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.ar.core.Anchor;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.Camera;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Point;
import com.google.ar.core.Point.OrientationMode;
import com.google.ar.core.PointCloud;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.google.ar.core.examples.java.common.helpers.CameraPermissionHelper;
import com.google.ar.core.examples.java.common.helpers.DisplayRotationHelper;
import com.google.ar.core.examples.java.common.helpers.FullScreenHelper;
import com.google.ar.core.examples.java.common.helpers.SnackbarHelper;
import com.google.ar.core.examples.java.common.helpers.TapHelper;
import com.google.ar.core.examples.java.common.helpers.TrackingStateHelper;
import com.google.ar.core.examples.java.common.rendering.BackgroundRenderer;
import com.google.ar.core.examples.java.common.rendering.ObjectRenderer;
import com.google.ar.core.examples.java.common.rendering.ObjectRenderer.BlendMode;
import com.google.ar.core.examples.java.common.rendering.PlaneRenderer;
import com.google.ar.core.examples.java.common.rendering.PointCloudRenderer;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException;
import java.io.IOException;
import java.util.ArrayList;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.res.Configuration;

import org.w3c.dom.Text;

/**
 * This is a simple example that shows how to create an augmented reality (AR) application using the
 * ARCore API. The application will display any detected planes and will allow the user to tap on a
 * plane to place a 3d model of the Android robot.
 */
public class HelloArActivity extends AppCompatActivity implements GLSurfaceView.Renderer, View.OnClickListener {
  private static final String TAG = HelloArActivity.class.getSimpleName();

  // Rendering. The Renderers are created here, and initialized when the GL surface is created.
  private GLSurfaceView surfaceView;

  private boolean installRequested;

  private Session session;
  private final SnackbarHelper messageSnackbarHelper = new SnackbarHelper();
  private DisplayRotationHelper displayRotationHelper;
  private final TrackingStateHelper trackingStateHelper = new TrackingStateHelper(this);
  private TapHelper tapHelper;

  private final BackgroundRenderer backgroundRenderer = new BackgroundRenderer();
  private final ObjectRenderer virtualObject = new ObjectRenderer();
  private final PlaneRenderer planeRenderer = new PlaneRenderer();
  private final PointCloudRenderer pointCloudRenderer = new PointCloudRenderer();

  // Temporary matrix allocated here to reduce number of allocations for each frame.
  private final float[] anchorMatrix = new float[16];
  private static final float[] DEFAULT_COLOR = new float[]{225f, 0f, 0f, 255f};

  private static final String SEARCHING_PLANE_MESSAGE = "Searching for surfaces...";

  // score
  private Score m_Score = new Score();

  // Anchors created from taps used for object placing with a given color.
  private static class ColoredAnchor {
    public final Anchor anchor;
    public final float[] color;

    public ColoredAnchor(Anchor a, float[] color4f) {
      this.anchor = a;
      this.color = color4f;
    }
  }

  // handles state
  enum Mode {
    SCANNING,
    SHOOTING
  }

  private Mode currMode = Mode.SCANNING;

  private Button switchMode;
  private TextView points;
  private TextView timerText;

  private Timer timer = new Timer();

  private MediaPlayer laserSound;

  private final ArrayList<ColoredAnchor> anchors = new ArrayList<>();
  private ArrayList<java.lang.Boolean> isVisible = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    surfaceView = findViewById(R.id.surfaceview);
    displayRotationHelper = new DisplayRotationHelper(/*context=*/ this);

    // Set up tap listener.
    tapHelper = new TapHelper(/*context=*/ this);
    surfaceView.setOnTouchListener(tapHelper);

    //play button
    switchMode = (Button) findViewById(R.id.switchButton);
    switchMode.setOnClickListener(this);

    // points label
    points = (TextView) findViewById(R.id.pointsView);
    timerText = (TextView) findViewById(R.id.timeView);

    // sounds
    laserSound = MediaPlayer.create(this, R.raw.laser);

    // Set up renderer.
    surfaceView.setPreserveEGLContextOnPause(true);
    surfaceView.setEGLContextClientVersion(2);
    surfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0); // Alpha used for plane blending.
    surfaceView.setRenderer(this);
    surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    surfaceView.setWillNotDraw(false);

    installRequested = false;
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);

    // Checks the orientation of the screen
    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
      setContentView(R.layout.activity_main);
    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
      setContentView(R.layout.activity_main);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();

    if (session == null) {
      Exception exception = null;
      String message = null;
      try {
        switch (ArCoreApk.getInstance().requestInstall(this, !installRequested)) {
          case INSTALL_REQUESTED:
            installRequested = true;
            return;
          case INSTALLED:
            break;
        }

        // ARCore requires camera permissions to operate. If we did not yet obtain runtime
        // permission on Android M and above, now is a good time to ask the user for it.
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
          CameraPermissionHelper.requestCameraPermission(this);
          return;
        }

        // Create the session.
        session = new Session(/* context= */ this);

      } catch (UnavailableArcoreNotInstalledException
              | UnavailableUserDeclinedInstallationException e) {
        message = "Please install ARCore";
        exception = e;
      } catch (UnavailableApkTooOldException e) {
        message = "Please update ARCore";
        exception = e;
      } catch (UnavailableSdkTooOldException e) {
        message = "Please update this app";
        exception = e;
      } catch (UnavailableDeviceNotCompatibleException e) {
        message = "This device does not support AR";
        exception = e;
      } catch (Exception e) {
        message = "Failed to create AR session";
        exception = e;
      }

      if (message != null) {
        messageSnackbarHelper.showError(this, message);
        Log.e(TAG, "Exception creating session", exception);
        return;
      }
    }

    // Note that order matters - see the note in onPause(), the reverse applies here.
    try {
      session.resume();
    } catch (CameraNotAvailableException e) {
      messageSnackbarHelper.showError(this, "Camera not available. Try restarting the app.");
      session = null;
      return;
    }

    surfaceView.onResume();
    displayRotationHelper.onResume();

    Toast.makeText(HelloArActivity.this,
            "place some targets!", Toast.LENGTH_LONG).show();
  }

  @Override
  public void onPause() {
    super.onPause();
    if (session != null) {
      // Note that the order matters - GLSurfaceView is paused first so that it does not try
      // to query the session. If Session is paused before GLSurfaceView, GLSurfaceView may
      // still call session.update() and get a SessionPausedException.
      displayRotationHelper.onPause();
      surfaceView.onPause();
      session.pause();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
    if (!CameraPermissionHelper.hasCameraPermission(this)) {
      Toast.makeText(this, "Camera permission is needed to run this application", Toast.LENGTH_LONG)
              .show();
      if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(this)) {
        // Permission denied with checking "Do not ask again".
        CameraPermissionHelper.launchPermissionSettings(this);
      }
      finish();
    }
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    FullScreenHelper.setFullScreenOnWindowFocusChanged(this, hasFocus);
  }

  @Override
  public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

    // Prepare the rendering objects. This involves reading shaders, so may throw an IOException.
    try {
      // Create the texture and pass it to ARCore session to be filled during update().
      backgroundRenderer.createOnGlThread(/*context=*/ this);
      planeRenderer.createOnGlThread(/*context=*/ this, "models/trigrid.png");
      pointCloudRenderer.createOnGlThread(/*context=*/ this);

      virtualObject.createOnGlThread(/*context=*/ this, "models/target.obj", "models/andy.png");
      virtualObject.setMaterialProperties(2.0f, 1.0f, 0.5f, 1.0f);

    } catch (IOException e) {
      Log.e(TAG, "Failed to read an asset file", e);
    }
  }

  @Override
  public void onSurfaceChanged(GL10 gl, int width, int height) {
    displayRotationHelper.onSurfaceChanged(width, height);
    GLES20.glViewport(0, 0, width, height);
  }

  @Override
  public void onDrawFrame(GL10 gl) {
    // Clear screen to notify driver it should not load any pixels from previous frame.
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

    if (session == null) {
      return;
    }
    // Notify ARCore session that the view size changed so that the perspective matrix and
    // the video background can be properly adjusted.
    displayRotationHelper.updateSessionIfNeeded(session);

    try {
      session.setCameraTextureName(backgroundRenderer.getTextureId());

      // Obtain the current frame from ARSession. When the configuration is set to
      // UpdateMode.BLOCKING (it is by default), this will throttle the rendering to the
      // camera framerate.
      Frame frame = session.update();
      Camera camera = frame.getCamera();

      // Handle one tap per frame.
      handleTap(frame, camera);

      // If frame is ready, render camera preview image to the GL surface.
      backgroundRenderer.draw(frame);

      // Keep the screen unlocked while tracking, but allow it to lock when tracking stops.
      trackingStateHelper.updateKeepScreenOnFlag(camera.getTrackingState());

      // If not tracking, don't draw 3D objects, show tracking failure reason instead.
      if (camera.getTrackingState() == TrackingState.PAUSED) {
        messageSnackbarHelper.showMessage(
                this, TrackingStateHelper.getTrackingFailureReasonString(camera));
        return;
      }

      // Get projection matrix.
      float[] projmtx = new float[16];
      camera.getProjectionMatrix(projmtx, 0, 0.1f, 100.0f);

      // Get camera matrix and draw.
      float[] viewmtx = new float[16];
      camera.getViewMatrix(viewmtx, 0);

      // Compute lighting from average intensity of the image.
      // The first three components are color scaling factors.
      // The last one is the average pixel intensity in gamma space.
      final float[] colorCorrectionRgba = new float[4];
      frame.getLightEstimate().getColorCorrection(colorCorrectionRgba, 0);

      // Visualize tracked points.
      // Use try-with-resources to automatically release the point cloud.
      try (PointCloud pointCloud = frame.acquirePointCloud()) {
        pointCloudRenderer.update(pointCloud);
        pointCloudRenderer.draw(viewmtx, projmtx);
      }

      // No tracking error at this point. If we detected any plane, then hide the
      // message UI, otherwise show searchingPlane message.
      if (hasTrackingPlane()) {
        messageSnackbarHelper.hide(this);
      } else {
        messageSnackbarHelper.showMessage(this, SEARCHING_PLANE_MESSAGE);
      }

      // Visualize planes.
      planeRenderer.drawPlanes(
              session.getAllTrackables(Plane.class), camera.getDisplayOrientedPose(), projmtx);

      // Visualize anchors created by touch.
      float scaleFactor = 0.005f;
      for (ColoredAnchor coloredAnchor : anchors) {
        if (isVisible.get(anchors.indexOf(coloredAnchor)))
        {
          if (coloredAnchor.anchor.getTrackingState() != TrackingState.TRACKING) {
            continue;
          }
          // Get the current pose of an Anchor in world space. The Anchor pose is updated
          // during calls to session.update() as ARCore refines its estimate of the world.
          coloredAnchor.anchor.getPose().toMatrix(anchorMatrix, 0);

          // Update and draw the model and its shadow.
          virtualObject.updateModelMatrix(anchorMatrix, scaleFactor);
          virtualObject.draw(viewmtx, projmtx, colorCorrectionRgba, coloredAnchor.color);
        }
      }

      // if shooting do timer
      if (currMode == Mode.SHOOTING) UpdateTimer();

    } catch (Throwable t) {
      // Avoid crashing the application due to unhandled exceptions.
      Log.e(TAG, "Exception on the OpenGL thread", t);
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.switchButton:
        // if we're scanning and there's at least two targets
        if (currMode == Mode.SCANNING
                && anchors.size() >= 2) {
          currMode = Mode.SHOOTING;
          switchMode.setText("Shooting");
          switchMode.setEnabled(false);

          points.setText("0");

          timer.startTimer();
          timerText.setText(timer.getTimeLeftText());

          Toast.makeText(HelloArActivity.this,
                  "Shoot em!", Toast.LENGTH_LONG).show();
        }
        else Toast.makeText(HelloArActivity.this,
                "Please place at least two targets", Toast.LENGTH_LONG).show();
        break;
    }
  }

  private void UpdateTimer()
  {
    timer.tickTimer();
    timerText.setText(timer.getTimeLeftText());
    timerText.invalidate();
    timerText.requestLayout();

    if(timer.getTimeLeft()<= 0){
      Intent i = new Intent(this, ScoreboardActivity.class);
      Bundle bundle = new Bundle();
      bundle.putString("SCORE", String.valueOf(m_Score.getPoints()));
      bundle.putString("TIME", String.valueOf(timer.getTimerDuration()));
      i.putExtras(bundle);
      startActivity(i);
    }
  }

  // Handle only one tap per frame, as taps are usually low frequency compared to frame rate.
  private void handleTap(Frame frame, Camera camera) {
    MotionEvent tap = tapHelper.poll();
    switch (currMode) {
      case SCANNING:
        if (tap != null && camera.getTrackingState() == TrackingState.TRACKING) {
          for (HitResult hit : frame.hitTest(tap)) {
            // Check if any plane was hit, and if it was hit inside the plane polygon
            Trackable trackable = hit.getTrackable();
            // Creates an anchor if a plane or an oriented point was hit.
            if ((trackable instanceof Plane
                    && ((Plane) trackable).isPoseInPolygon(hit.getHitPose())
                    && (PlaneRenderer.calculateDistanceToPlane(hit.getHitPose(), camera.getPose()) > 0))
                    || (trackable instanceof Point
                    && ((Point) trackable).getOrientationMode()
                    == OrientationMode.ESTIMATED_SURFACE_NORMAL)) {
              // Hits are sorted by depth. Consider only closest hit on a plane or oriented point.
              // Cap the number of objects created. This avoids overloading both the
              // rendering system and ARCore.
              if (anchors.size() >= 20) {
                anchors.get(0).anchor.detach();
                anchors.remove(0);
                isVisible.remove(0);
              }

              // assign red color to the targets
              float[] objColor;
              objColor = DEFAULT_COLOR;

              // Adding an Anchor tells ARCore that it should track this position in
              // space. This anchor is created on the Plane to place the 3D model
              // in the correct position relative both to the world and to the plane.
              anchors.add(new ColoredAnchor(hit.createAnchor(), objColor));
              isVisible.add(true);
              break;
            }
          }
        }
        break;
      case SHOOTING:
        if (tap != null && camera.getTrackingState() == TrackingState.TRACKING
            && !laserSound.isPlaying()) {
          // play sound
          laserSound.start();

          // set tap to middle of screen
          tap.setLocation(500, 1000);

          for (HitResult hit : frame.hitTest(tap)) {
            // hit detection
            float hitRange = 0.2f;    // radius of the target - size of hitbox

            float[] hitSpot = hit.getHitPose().getTranslation();
            boolean isHit = false;
            // check all targets
            for (ColoredAnchor ca : anchors) {
              if (isVisible.get(anchors.indexOf(ca)))
              {
                float[] target = ca.anchor.getPose().getTranslation();
                // if user hit a target
                if (Math.abs(hitSpot[0] - target[0]) < hitRange
                        && Math.abs(hitSpot[1] - target[1]) < hitRange
                        && Math.abs(hitSpot[2] - target[2]) < hitRange) {
                  isHit = true;

                  // also need to set target to invisible
                  isVisible.set(anchors.indexOf(ca), false);
                  // if all are invisible we need to show all except one they shot
                  if (!isVisible.contains(true))
                  {
                    for(int ii=0; ii<isVisible.size(); ii++) isVisible.set(ii, true);
                    isVisible.set(anchors.indexOf(ca), false);
                  }
                }
                if (isHit) break;  // we have a hit so we don't need to check other targets
              }
            }
            // if target is hit add points and update display
            if (isHit) {
              int adjustedPoints = (int)(hit.getDistance()*100);
              m_Score.addPoints(adjustedPoints);
              points.setText(String.valueOf(m_Score.getPoints()));
              points.invalidate();  //need to force android to update points before continuing
              points.requestLayout();
            }
          }
        }
        break;
    }
  }

  /**
   * Checks if we detected at least one plane.
   */
  private boolean hasTrackingPlane() {
    for (Plane plane : session.getAllTrackables(Plane.class)) {
      if (plane.getTrackingState() == TrackingState.TRACKING) {
        return true;
      }
    }
    return false;
  }

}


