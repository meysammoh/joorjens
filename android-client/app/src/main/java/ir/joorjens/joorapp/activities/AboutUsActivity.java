package ir.joorjens.joorapp.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.ActivityKeys;
import ir.joorjens.joorapp.adapters.DiscountListAdapter;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.customViews.TitleBar;
import ir.joorjens.joorapp.webService.APICode;

public class AboutUsActivity extends AppCompatActivity {

    // ------------------------------ isActive ---------------------------
    private boolean _isActive;
    @Override
    protected void onResume() {
        super.onResume();
        _isActive = true;
    }
    @Override
    protected void onStop() {
        _isActive = false;
        super.onStop();
    }
//    @Override
//    public boolean isActive() {
//        return _isActive;
//    }
    // ------------------------------ isActive ---------------------------

//    @BindView(R.id.imgv) ImageView mImgv;
//    @Override
//    public ActivityKeys getActivityId() {
//        return ActivityKeys.AboutUs;
//    }
//
//    @Override
//    public String getActivityTitle() {
//        return "درباره ما";
//    }
//
//    private static final int SELECT_PICTURE = 0;
//    private static final int PIC_CROP = 2;
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_about_us);


    }
//
//    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
//
//        private int space;
//
//        public SpacesItemDecoration(int space) {
//            this.space = space;
//        }
//
//        @Override
//        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//            outRect.left = space;
//            outRect.right = space;
//            outRect.bottom = space;
//            outRect.top = space;
//            // Add top margin only for the first item to avoid double space between items
////            if(parent.getChildAdapterPosition(view) == 0) {
////                outRect.top = space;
////            }
//        }
//    }
//
//    private int dpToPx(int dp) {
//        Resources r = getResources();
//        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
//    }
//
//    private View.OnClickListener listener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Toast.makeText(AboutUsActivity.this, "نمایش همه", Toast.LENGTH_LONG).show();
//        }
//    };
//
//    private void performCrop(Uri picUri){
//
//        try {
////call the standard crop action intent (the user device may not support it)
//            Intent cropIntent = new Intent("com.android.camera.action.CROP");
//            //indicate image type and Uri
//            cropIntent.setDataAndType(picUri, "image/*");
//            //set crop properties
//            cropIntent.putExtra("crop", "true");
//            //indicate aspect of desired crop
//            cropIntent.putExtra("aspectX", 1);
//            cropIntent.putExtra("aspectY", 1);
//            //indicate output X and Y
//            cropIntent.putExtra("outputX", 256);
//            cropIntent.putExtra("outputY", 256);
//            //retrieve data on return
//            cropIntent.putExtra("return-data", true);
//            //start the activity - we handle returning in onActivityResult
//            startActivityForResult(cropIntent, PIC_CROP);
//        }
//        catch(ActivityNotFoundException anfe){
//            //display an error message
//            String errorMessage = "Whoops - your device doesn't support the crop action!";
//            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
//            toast.show();
//        }
//
//    }
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == SELECT_PICTURE) {
//                Uri selectedImageUri = data.getData();
//                performCrop(selectedImageUri);
//
//                //mImgProfilePic.setImageURI(selectedImageUri);
//                //String imagePath = StaticHelperFunctions.getPathFromURI(this, selectedImageUri);
//                //creatingStore.setManagerImageP(imagePath);
//            }
//
//            else if(requestCode == PIC_CROP){
//                //get the returned data
//                Bundle extras = data.getExtras();
//                //get the cropped bitmap
//                Bitmap thePic = extras.getParcelable("data");
//                mImgv.setImageBitmap(thePic);
//            }
//        }
//    }
//
//
//    @Override
//    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
//        if(apiCode == APICode.logout){
//            super.onServiceSuccess(apiCode, data, requestCode);
//        }
//    }
//
//    @Override
//    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
//        if(apiCode == APICode.logout){
//            super.onServiceFail(apiCode, data, requestCode);
//        }
//    }
//
//    @Override
//    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
//        if(apiCode == APICode.logout){
//            super.onNetworkFail(apiCode, data, requestCode);
//        }
//    }
}
