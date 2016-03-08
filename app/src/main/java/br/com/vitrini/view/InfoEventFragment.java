package br.com.vitrini.view;

import br.com.expoagro.R;
import br.com.vitrini.utils.StringUtils;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class InfoEventFragment extends Fragment 
{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle b) {

		View view = inflater.inflate(R.layout.fragment_craft, container, false);  //!!! this is important
		setContent(view);
		return view;
	}
	
	/**
	 * Preenche a view com o conteúdo obtido do arquivo html
	 * @param view View a ser preenchida
	 */
	private void setContent( View view ) {
		// Realiza  aleitura do arquivo
		String content = StringUtils.htmlFileFromAssetsToString("main_screen_content/lingueta_event.html", view.getContext());

		// Popula a view
		TextView textviewCraftRelease = (TextView)view.findViewById( R.id.lingueta_event);
		textviewCraftRelease.setText(Html.fromHtml( content ));
		
		// Associa o link da página ao botão
		ImageButton facebook = (ImageButton)view.findViewById(R.id.button_event_facebook);
		facebook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse( getResources().getString(R.string.url_event_facebook)));
		        startActivity(i);				
			}
		});

		// Associa o link da página ao botão
//		ImageButton instagram = (ImageButton)view.findViewById(R.id.button_event_instagram);
//		instagram.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse( getResources().getString(R.string.url_event_instagram)));
//		        startActivity(i);				
//			}
//		});

		// Associa o link da página ao botão
//		ImageButton twitter = (ImageButton)view.findViewById(R.id.button_event_twitter);
//		twitter.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse( getResources().getString(R.string.url_event_twitter)));
//		        startActivity(i);				
//			}
//		});

		// Associa o link da página ao botão
//		ImageButton pintrest = (ImageButton)view.findViewById(R.id.button_event_pintrest);
//		pintrest.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse( getResources().getString(R.string.url_event_pintrest)));
//		        startActivity(i);				
//			}
//		});
	}
}