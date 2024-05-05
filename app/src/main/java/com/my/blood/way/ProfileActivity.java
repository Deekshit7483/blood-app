package com.my.blood.way;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.*;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.appbar.AppBarLayout;
import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.webkit.*;
import android.animation.*;
import android.view.animation.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.text.*;
import org.json.*;
import java.util.HashMap;
import java.util.ArrayList;
import android.widget.ScrollView;
import android.widget.LinearLayout;
import de.hdodenhof.circleimageview.*;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ChildEventListener;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import com.bumptech.glide.Glide;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.DialogFragment;

public class ProfileActivity extends AppCompatActivity {
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private Toolbar _toolbar;
	private AppBarLayout _app_bar;
	private CoordinatorLayout _coordinator;
	private HashMap<String, Object> map = new HashMap<>();
	private String number = "";
	
	private ArrayList<String> phone = new ArrayList<>();
	
	private ScrollView vscroll1;
	private LinearLayout linear1;
	private CircleImageView im_avatar;
	private LinearLayout linear3;
	private TextView number_;
	private TextView gender;
	private TextView email;
	private TextView blood_group;
	private TextView address;
	private Button button2;
	private Button button1;
	private Button button3;
	private TextView username;
	private ImageView imageview1;
	
	private FirebaseAuth fauth;
	private OnCompleteListener<AuthResult> _fauth_create_user_listener;
	private OnCompleteListener<AuthResult> _fauth_sign_in_listener;
	private OnCompleteListener<Void> _fauth_reset_password_listener;
	private OnCompleteListener<Void> fauth_updateEmailListener;
	private OnCompleteListener<Void> fauth_updatePasswordListener;
	private OnCompleteListener<Void> fauth_emailVerificationSentListener;
	private OnCompleteListener<Void> fauth_deleteUserListener;
	private OnCompleteListener<Void> fauth_updateProfileListener;
	private OnCompleteListener<AuthResult> fauth_phoneAuthListener;
	private OnCompleteListener<AuthResult> fauth_googleSignInListener;
	
	private DatabaseReference user = _firebase.getReference("user");
	private ChildEventListener _user_child_listener;
	private Intent i = new Intent();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.profile);
		initialize(_savedInstanceState);
		com.google.firebase.FirebaseApp.initializeApp(this);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		_app_bar = findViewById(R.id._app_bar);
		_coordinator = findViewById(R.id._coordinator);
		_toolbar = findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});
		vscroll1 = findViewById(R.id.vscroll1);
		linear1 = findViewById(R.id.linear1);
		im_avatar = findViewById(R.id.im_avatar);
		linear3 = findViewById(R.id.linear3);
		number_ = findViewById(R.id.number_);
		gender = findViewById(R.id.gender);
		email = findViewById(R.id.email);
		blood_group = findViewById(R.id.blood_group);
		address = findViewById(R.id.address);
		button2 = findViewById(R.id.button2);
		button1 = findViewById(R.id.button1);
		button3 = findViewById(R.id.button3);
		username = findViewById(R.id.username);
		imageview1 = findViewById(R.id.imageview1);
		fauth = FirebaseAuth.getInstance();
		
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				i.setClass(getApplicationContext(), EditProfileActivity.class);
				startActivity(i);
			}
		});
		
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				FirebaseAuth.getInstance().signOut();
				i.setClass(getApplicationContext(), ManageLoginActivity.class);
				startActivity(i);
			}
		});
		
		button3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				SketchwareUtil.showMessage(getApplicationContext(), "please wait");
				shareApplication();
			}
		});
		
		_user_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if (_childKey.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
					username.setText("Username:- ".concat(_childValue.get("username").toString()));
					email.setText("Email:- ".concat(_childValue.get("email").toString()));
					if (_childValue.containsKey("address")) {
						address.setText("Address:- ".concat(_childValue.get("address").toString()));
					}
					if (_childValue.containsKey("gender")) {
						gender.setText("Gender:- ".concat(_childValue.get("gender").toString()));
					}
					if (_childValue.containsKey("blood group")) {
						blood_group.setText("Blood group:- ".concat(_childValue.get("blood group").toString()));
					}
					if (_childValue.containsKey("number")) {
						number_.setText("Number:- ".concat(_childValue.get("number").toString()));
					}
					if (_childValue.containsKey("avatar")) {
						if (_childValue.get("avatar").toString().equals("")) {
							im_avatar.setImageResource(R.drawable.admin);
						}
						else {
							Glide.with(getApplicationContext()).load(Uri.parse(_childValue.get("avatar").toString())).into(im_avatar);
						}
					}
					if (_childValue.get("verification").toString().equals("true")) {
						imageview1.setVisibility(View.VISIBLE);
					}
					else {
						if (_childValue.get("verification").toString().equals("false")) {
							imageview1.setVisibility(View.GONE);
						}
					}
				}
			}
			
			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if (_childKey.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
					username.setText("Username:- ".concat(_childValue.get("username").toString()));
					email.setText("Email:- ".concat(_childValue.get("email").toString()));
					if (_childValue.containsKey("address")) {
						address.setText("Address:- ".concat(_childValue.get("address").toString()));
					}
					if (_childValue.containsKey("gender")) {
						gender.setText("Gender:- ".concat(_childValue.get("gender").toString()));
					}
					if (_childValue.containsKey("blood group")) {
						blood_group.setText("Blood group:- ".concat(_childValue.get("blood group").toString()));
					}
					if (_childValue.containsKey("avatar")) {
						if (_childValue.get("avatar").toString().equals("")) {
							im_avatar.setImageResource(R.drawable.admin);
						}
						else {
							Glide.with(getApplicationContext()).load(Uri.parse(_childValue.get("avatar").toString())).into(im_avatar);
						}
					}
					if (_childValue.get("verification").toString().equals("true")) {
						imageview1.setVisibility(View.VISIBLE);
					}
					else {
						if (_childValue.get("verification").toString().equals("false")) {
							imageview1.setVisibility(View.GONE);
						}
					}
				}
			}
			
			@Override
			public void onChildMoved(DataSnapshot _param1, String _param2) {
				
			}
			
			@Override
			public void onChildRemoved(DataSnapshot _param1) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onCancelled(DatabaseError _param1) {
				final int _errorCode = _param1.getCode();
				final String _errorMessage = _param1.getMessage();
				
			}
		};
		user.addChildEventListener(_user_child_listener);
		
		fauth_updateEmailListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		fauth_updatePasswordListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		fauth_emailVerificationSentListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		fauth_deleteUserListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		fauth_phoneAuthListener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> task) {
				final boolean _success = task.isSuccessful();
				final String _errorMessage = task.getException() != null ? task.getException().getMessage() : "";
				
			}
		};
		
		fauth_updateProfileListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		fauth_googleSignInListener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> task) {
				final boolean _success = task.isSuccessful();
				final String _errorMessage = task.getException() != null ? task.getException().getMessage() : "";
				
			}
		};
		
		_fauth_create_user_listener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		_fauth_sign_in_listener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		_fauth_reset_password_listener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				
			}
		};
	}
	
	private void initializeLogic() {
		_Shadow(15, 25, "#F8BBD0", button2);
		_Shadow(15, 25, "#F8BBD0", button1);
		_Shadow(15, 25, "#F8BBD0", button3);
		StrictMode.VmPolicy.Builder builder = 
		    new StrictMode.VmPolicy.Builder(); 
		    StrictMode.setVmPolicy(builder.build());
		    if(Build.VERSION.SDK_INT>=24){ 
			         try{
				        java.lang.reflect.Method m = 
				              StrictMode.class.getMethod(
				        "disableDeathOnFileUriExposure"); 
				              m.invoke(null); 
				       }
			      catch(Exception e){ 
				        showMessage(e.toString()); 
				       } 
			    }
	}
	
	@Override
	public void onBackPressed() {
		i.setClass(getApplicationContext(), HomeActivity.class);
		startActivity(i);
	}
	public void _Shadow(final double _sadw, final double _cru, final String _wc, final View _widgets) {
		android.graphics.drawable.GradientDrawable wd = new android.graphics.drawable.GradientDrawable();
		wd.setColor(Color.parseColor(_wc));
		wd.setCornerRadius((int)_cru);
		_widgets.setElevation((int)_sadw);
		_widgets.setBackground(wd);
	}
	
	
	public void _share() {
	}
	private void shareApplication() { 
		      android.content.pm.ApplicationInfo app = 
		      getApplicationContext().getApplicationInfo(); 
		      String filePath = app.sourceDir;
		      Intent intent = new Intent(Intent.ACTION_SEND); 
		      intent.setType("*/*"); 
		      java.io.File originalApk = new java.io.File(filePath); 
		      try {  
			        java.io.File tempFile = new java.io.File(getExternalCacheDir() + "/ExtractedApk"); 
			         if (!tempFile.isDirectory()) 
			         if (!tempFile.mkdirs()) 
			         return; 
			         tempFile = new java.io.File(tempFile.getPath() + "/" + 
			         "export.apk");
			         if (!tempFile.exists()) 
			          {
				           try{
					             if (!tempFile.createNewFile()) { 
						               return; }
					            }
				           catch (java.io.IOException e){} 
				          } 
			         java.io.InputStream in = new java.io.FileInputStream (originalApk);
			         java.io.OutputStream out = new java.io.FileOutputStream(tempFile);
			         byte[] buf = new byte[1024];
			         int len; 
			         while ((len = in.read(buf)) > 0) { 
				            out.write(buf, 0, len); 
				         } 
			         in.close(); 
			         out.close(); 
			         intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
			         startActivity(Intent.createChooser(intent, "Share app via"));
			      } 
		      catch (java.io.IOException e) 
		      { showMessage(e.toString()); 
			      } 
		   }
	{
	}
	
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}