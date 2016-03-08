package br.com.vitrini.view;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import br.com.expoagro.R;
import br.com.vitrini.utils.StringUtils;

public class InfoPosition3Fragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle b) {

		View view = inflater.inflate(R.layout.fragment_comfort,	container,
				false);  //!!! this is important
		setContent(view);
		return view;
	}
	
	/**
	 * Preenche a view com o conteúdo obtido do arquivo html
	 * @param view View a ser preenchida
	 */
	private void setContent( View view ) {
		// Realiza  aleitura do arquivo
		String content = StringUtils.htmlFileFromAssetsToString("main_screen_content/lingueta_comodidades.html", view.getContext());

		// Popula a view
		WebView wv = (WebView)view.findViewById(R.id.webPage);
		if (Build.VERSION.SDK_INT >= 11) {
		    wv.setBackgroundColor(0x01000000);
		} else {
		    wv.setBackgroundColor(0x00000000);
		}		
		wv.loadUrl("file:///android_asset/main_screen_content/lingueta_comodidades.html");
	}

}
