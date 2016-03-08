 package br.com.vitrini.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.expoagro.Constants;
import br.com.expoagro.R;
import br.com.vitrini.entities.Vitrini;
import br.com.vitrini.persistence.VitriniDBAdapter;
import br.com.vitrini.utils.BitmapHelper;
import br.com.vitrini.utils.CirclePageIndicator;
import br.com.vitrini.utils.StringUtils;
import br.com.vitrini.view.adapters.ImagePagerAdapterVitrinis;

public class VitriniActivity extends AbstractActivity {

	protected Vitrini vitrini;
	private VitriniDBAdapter vitriniDbAdapter;
	
    ViewPager viewPager;
	CirclePageIndicator mIndicator;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDatabaseAndServices();
        setContentView(R.layout.activity_vitrini);
        
        final Intent intent = getIntent();
		String vitriniName = intent.getExtras().getString(Constants.VITRINI_FIED_NAME);
		
		vitriniDbAdapter = databaseManager.getVitriniAdapter();
		vitrini = vitriniDbAdapter.findVitriniByName(vitriniName);
		initView();
    }

    public void onFavoriteCheckboxClicked(View view){
    	CheckBox favoriteCheckbox = (CheckBox)view;
    	vitrini.setfavorite(favoriteCheckbox.isChecked());
    		if(vitrini.isFavorite()) {
    			vitriniDbAdapter.setAsFavorite(vitrini);
    		}else
    			vitriniDbAdapter.removeFavorite(vitrini);
    }

    @Override
	protected void initView() {

		initImageSliderWidget();

		CheckBox favoriteCheck = (CheckBox) findViewById(R.id.checkbox_favorite);
		if(vitrini.isFavorite()){
			favoriteCheck.setChecked(true);
		}
		else{
			favoriteCheck.setChecked(false);
		}
		
		final TextView txtViewVitriniName = (TextView) findViewById( R.id.text_vitrini_name);
		final String vitriniNameTxt = String.format("%s", vitrini.getName() );
		txtViewVitriniName.setText( vitriniNameTxt);
		
		ImageView segmentImage = (ImageView) findViewById(R.id.view_vitrini_segment_icon);
		
		// Recupera a chave do segmento a ser pesquisada no hasmap
		String hashKey = StringUtils.normalize( vitrini.getSegment() );

		// Adiciona as fotos dos segmentos
		Bitmap segmentBitmap = BitmapHelper.segmentImages.get( hashKey );
		if (segmentBitmap != null)
			segmentImage.setImageBitmap( segmentBitmap );		
		
		final TextView textSite = (TextView) findViewById(R.id.text_vitrini_site);
		textSite.setText(vitrini.getSite());
		
		final TextView txtDescription =  (TextView) findViewById( R.id.text_vitrini_description);
		txtDescription.setText( vitrini.getDescription() );
		
		final TextView txtCityEstate =  (TextView) findViewById( R.id.text_city_estate);
		txtCityEstate.setText( vitrini.getCity() + "/" + vitrini.getEstate());
		
		final TextView txtSegment =  (TextView) findViewById( R.id.text_segment);
		txtSegment.setText( vitrini.getSegment());
		
		final TextView txtEmail =  (TextView) findViewById( R.id.text_email);
		txtEmail.setText( vitrini.getEmail());
		
		final TextView txtPhone =  (TextView) findViewById( R.id.text_telephone);
		txtPhone.setText( vitrini.getPhone());
		
		// Definindo que o textview pode sofre scrool
		txtDescription.setMovementMethod(new ScrollingMovementMethod());
		
		// Recupera o site da vitrini
		String strSite = vitrini.getSite();
		String htmlSite = "<a href=http://" + strSite + ">" + strSite + "</a>";		
		if(strSite.contains("facebook.")) {
			final ImageView siteIcon = (ImageView) findViewById(R.id.vitrini_site_icon);
			siteIcon.setBackgroundResource(R.drawable.icon_social_facebook);
		}
		//Removendo o underline do link
		Spannable s = (Spannable) Html.fromHtml(htmlSite);
		for (URLSpan u: s.getSpans(0, s.length(), URLSpan.class)) {
		    s.setSpan(new UnderlineSpan() {
		        public void updateDrawState(TextPaint tp) {
		            tp.setUnderlineText(false);
		        }
		    }, s.getSpanStart(u), s.getSpanEnd(u), 0);
		}
		
		final EditText annotation = (EditText)findViewById(R.id.text_vitrini_annotation);
		annotation.setText( returnAnnotationText() );
		annotation.addTextChangedListener( new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) 
			{
				final String text = annotation.getText().toString();
				controller.setAnnotation( vitrini, text );
			}
		});
		
		textSite.setText(s);
		textSite.setMovementMethod(LinkMovementMethod.getInstance());
		
		//Define a cor do link
		if ( BitmapHelper.segmentColors.containsKey(hashKey) )
		{
			textSite.setLinkTextColor( Color.parseColor(BitmapHelper.segmentColors.get(hashKey).toString()) );
		}
		


				
		ImageButton map = (ImageButton) findViewById(R.id.button_map);
		
		map.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
//				controller.goToPhotoMapActivity();
				controller.goToPhotoMapActivity( vitrini.getName() );
			}
		});
	}

	private String returnAnnotationText() {
		String annotation = vitrini.getAnnotation();
			return annotation;	
	}
	
	protected void initImageSliderWidget() {

		final String vitriniName = StringUtils.normalize(vitrini.getName());
		
	    viewPager = (ViewPager) findViewById(R.id.view_pager_vitrini );
	    ImagePagerAdapterVitrinis adapter = new ImagePagerAdapterVitrinis( this, vitriniName );
	    viewPager.setAdapter(adapter);
	    
	    mIndicator = (CirclePageIndicator) findViewById(R.id.indicator_vitrini);
	    mIndicator.setViewPager( (ViewPager)viewPager);    
	}	

}
