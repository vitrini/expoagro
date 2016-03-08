package br.com.vitrini.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import br.com.expoagro.R;
import br.com.vitrini.persistence.DatabaseManager;
import br.com.vitrini.utils.ApplicationContextProvider;
import br.com.vitrini.utils.BitmapHelper;
import br.com.vitrini.utils.CirclePageIndicator;
import br.com.vitrini.view.AboutDialogFragment;
import br.com.vitrini.view.InfoPosition3Fragment;
import br.com.vitrini.view.InfoEventFragment;
import br.com.vitrini.view.InfoPosition4Fragment;
import br.com.vitrini.view.InfoPosition6Fragment;
import br.com.vitrini.view.InfoPosition5Fragment;
import br.com.vitrini.view.InfoPosition2Fragment;
import br.com.vitrini.view.adapters.ImagePagerAdapter;

public class MainActivity extends FragmentActivity {

	private static final String TAG_ABOUT_POPUP = "ABOUT_POPUP";
	private int _xDelta;
	private int _lastX;

	ViewGroup _root;

	private ImageButton btnEvent;
	private ImageButton btnInfoPos2;
	private ImageButton btnInfoPos3;
	private ImageButton btnPlaces;
	private ImageButton btnRestaurants;
	private ImageButton btnInfoPos4;

	private static final String FRAGMENT_TAG = "INFO_TAG";


	private int _dragLeftLimit;
	private int _dragRightLimit;

	CirclePageIndicator mIndicator;
	ViewPager viewPager;

	protected ActivityController controller;

	protected DatabaseManager databaseManager;

	protected void initDatabaseAndServices() {
		if(databaseManager == null){
			databaseManager = DatabaseManager.getInstance();
		}		
		controller = new ActivityController( this, databaseManager );
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initDatabaseAndServices();//should be 
		BitmapHelper.initializeSegmentImages(this);

		initView();
		if(((ApplicationContextProvider)getApplicationContext()).tabCallStack.isEmpty()) {
			ActivityController.pushTabCall((ApplicationContextProvider)getApplicationContext(), 0);
		}
	}

	protected void initEventInfoDrag() {

		_root = (ViewGroup)findViewById(R.id.drag_event_details);

		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) _root.getLayoutParams();
		Button dragButton = (Button) findViewById(R.id.button_drag_event_details);
		

		_dragLeftLimit = layoutParams.leftMargin;
		_dragRightLimit = 0;

		dragButton.setOnTouchListener( new OnTouchListener() 
		{

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				final int X = (int) event.getRawX();

				RelativeLayout.LayoutParams layoutParams = null;			        
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					layoutParams = (RelativeLayout.LayoutParams) _root.getLayoutParams();
					_xDelta = X - layoutParams.leftMargin;
					_lastX = X;				        	
					break;
				case MotionEvent.ACTION_UP:
					layoutParams = (RelativeLayout.LayoutParams) _root.getLayoutParams();

					if(X - _lastX > 0)
						layoutParams.leftMargin = _dragRightLimit;
					else
						layoutParams.leftMargin = _dragLeftLimit;

					layoutParams.topMargin = layoutParams.topMargin;
					layoutParams.rightMargin = layoutParams.rightMargin;
					layoutParams.bottomMargin = layoutParams.bottomMargin;
					setLayoutParams( _root, layoutParams );

					// Define a imagem da lingueta que deve ser utilizada
					if(layoutParams.leftMargin == _dragRightLimit)
						_root.setBackgroundResource( R.drawable.event_details_area_close );		
					else 
						if(layoutParams.leftMargin == _dragLeftLimit)
							_root.setBackgroundResource( R.drawable.event_details_area_open );							
						
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					break;
				case MotionEvent.ACTION_POINTER_UP:
					break;
				case MotionEvent.ACTION_MOVE:
					layoutParams = (RelativeLayout.LayoutParams) _root.getLayoutParams();
					layoutParams.leftMargin = X - _xDelta;
					layoutParams.topMargin = layoutParams.topMargin;
					layoutParams.rightMargin = layoutParams.rightMargin;
					layoutParams.bottomMargin = layoutParams.bottomMargin;

					// Define a imagem da lingueta que deve ser utilizada
					if(X - _lastX > 0)
						if(layoutParams.leftMargin >= _dragRightLimit)
							{
								_root.setBackgroundResource( R.drawable.event_details_area_close );
// BUG conhecido mais pouco frequente
								_lastX = _dragRightLimit;
//								_xDelta = _lastX - layoutParams.leftMargin;
							}							
						else
							_root.setBackgroundResource( R.drawable.event_details_area_open );
					else
						if(layoutParams.leftMargin <= _dragLeftLimit)
							{
								_root.setBackgroundResource( R.drawable.event_details_area_open );							
								_lastX = _dragLeftLimit;
//								_xDelta = _lastX - layoutParams.leftMargin;
							}
						else
							_root.setBackgroundResource( R.drawable.event_details_area_close );
					
					setLayoutParams( _root, layoutParams );

					break;
				}
				_root.invalidate();
				return true;
			}
		});

	}

	private Boolean setLayoutParams( View view, RelativeLayout.LayoutParams layoutParams )
	{
		if(layoutParams.leftMargin >= _dragLeftLimit && layoutParams.leftMargin <= _dragRightLimit)
		{
			view.setLayoutParams(layoutParams);
			return true;
		}
		else
		{
			return false;
		}
	}

	protected void initImageSliderWidget() {
		String[] imagesPath = new String[] {
				"expoagro/tela_inicial/event0.jpg",
				"expoagro/tela_inicial/event1.jpg",
				"expoagro/tela_inicial/event2.jpg",
				"expoagro/tela_inicial/event3.jpg",
				"expoagro/tela_inicial/event4.jpg",
				"expoagro/tela_inicial/event5.jpg"
		};

		viewPager = (ViewPager) findViewById(R.id.view_pager );
		ImagePagerAdapter adapter = new ImagePagerAdapter( this, imagesPath );
		viewPager.setAdapter(adapter);

		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager( (ViewPager)viewPager);    
	}

	protected void initView() {

		initEventInfoDrag();
		initImageSliderWidget();
		initInfoButtons();
		intiAboutBtn();
	}

	private void intiAboutBtn() 
	{
		ImageButton aboutBtn = (ImageButton)findViewById(R.id.img_button_about);
		
		aboutBtn.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				FragmentManager manager = getSupportFragmentManager();
				
				AboutDialogFragment dialog = new AboutDialogFragment();
				
				dialog.show(manager, TAG_ABOUT_POPUP);
				
			}
		});
	}

	private void initFragment ( FragmentManager manager, Fragment frag, FrameLayout fl )
	{
		
		manager.beginTransaction().replace(fl.getId(), frag).commit();
//		Fragment fragment = manager.findFragmentByTag( FRAGMENT_TAG );
//		FragmentTransaction xact = manager.beginTransaction();
//		if ( fragment != null )
//		{
//			xact.remove( fragment );
//		}
//		xact.add(R.id.frame_fragment_layout, frag,  FRAGMENT_TAG);
//		xact.commit();
	}

	private void resetBackgroundImg() {
		btnInfoPos3.setImageResource( R.drawable.info_position3_icon );
		btnInfoPos2.setImageResource( R.drawable.info_position2_icon );
		btnEvent.setImageResource( R.drawable.info_event_icon );
//		btnPlaces.setImageResource( R.drawable.info_position6_icon );
//		btnRestaurants.setImageResource( R.drawable.info_position5_icon );
		btnInfoPos4.setImageResource( R.drawable.info_position4_icon );
	}

	private void initInfoButtons() 
	{
		 

		btnEvent = (ImageButton) findViewById(R.id.img_btn_event);
		btnInfoPos2 = (ImageButton) findViewById(R.id.img_btn_info_pos2);
		btnInfoPos3 = (ImageButton) findViewById(R.id.img_btn_info_pos3);		
		btnInfoPos4 = (ImageButton) findViewById( R.id.img_btn_info_pos4 );
//		btnRestaurants = (ImageButton) findViewById( R.id.img_btn_restaurants );
//		btnPlaces = (ImageButton) findViewById(R.id.img_btn_places);

		final FragmentManager manager = getSupportFragmentManager();
		
		final FrameLayout fl = (FrameLayout)findViewById( R.id.frame_fragment_layout );
		

		if ( manager.findFragmentByTag( FRAGMENT_TAG ) == null )
		{
			manager.beginTransaction().replace(fl.getId(),  new InfoEventFragment()).commit();
//			FragmentTransaction xact = manager.beginTransaction();
//			xact.add(R.id.frame_fragment_layout, new InfoCraftFragment(), FRAGMENT_TAG );
//			xact.commit();
			resetBackgroundImg();
			btnEvent.setImageResource(R.drawable.info_event_icon_selected);
		}

		btnInfoPos2.setOnClickListener( new View.OnClickListener() {

			public void onClick(View v) 
			{
				resetBackgroundImg();
				btnInfoPos2.setImageResource(R.drawable.info_position2_icon_selected);
				initFragment( manager, new InfoPosition2Fragment(), fl );
			}


		});

		btnEvent.setOnClickListener( new View.OnClickListener() {

			public void onClick(View v) {

				resetBackgroundImg();
				btnEvent.setImageResource(R.drawable.info_event_icon_selected);
				initFragment( manager, new InfoEventFragment(), fl );
			}
		});

		btnInfoPos3.setOnClickListener( new View.OnClickListener() {

			public void onClick(View v) {
				resetBackgroundImg();
				btnInfoPos3.setImageResource(R.drawable.info_position3_icon_selected);
				initFragment( manager, new InfoPosition3Fragment(), fl );
			}
		});
		
		//TODO Adicionar um arquivo de configuração para a lingueta de forma a fazer o setup dos botões automaticamente. 

//		btnPlaces.setOnClickListener( new View.OnClickListener() {
//
//			public void onClick(View v) {
//				resetBackgroundImg();
//				btnPlaces.setImageResource(R.drawable.info_position6_icon_selected);
//				initFragment( manager, new InfoPosition6Fragment(), fl );
//			}
//		});


//		btnRestaurants.setOnClickListener( new View.OnClickListener() {
//
//			public void onClick(View v) {
//				resetBackgroundImg();
//				btnRestaurants.setImageResource(R.drawable.info_position5_icon_selected);
//				initFragment( manager, new InfoPosition5Fragment(), fl );
//			}
//		});

		btnInfoPos4.setOnClickListener( new View.OnClickListener() {

			public void onClick(View v) {
				resetBackgroundImg();
				btnInfoPos4.setImageResource(R.drawable.info_position4_icon_selected);
				initFragment( manager, new InfoPosition4Fragment(), fl );
			}
		});

	}
	@Override
	public void onBackPressed() {
		int lastTab = ActivityController.getLastCalledTab((ApplicationContextProvider)getApplicationContext());
		if(lastTab > 0) {
			((VitriniTabActivity)getParent()).setCurrentTab(lastTab);
		} else {
			finish();
		}
		
		
	}

}
