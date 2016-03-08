package br.com.vitrini.view;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import br.com.expoagro.R;
import br.com.vitrini.utils.StringUtils;

public class InfoPosition2Fragment extends Fragment 
{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle b) {

		if(container == null) {
			return null;
		}
		View view = inflater.inflate(R.layout.fragment_rules, container, false);  //!!! this is important		
		setContent(view);		
		return view;
	}
	
	private void setContent( View view ) {
		// Realiza  aleitura do arquivo
		String content = StringUtils.htmlFileFromAssetsToString("main_screen_content/lingueta_regras.html", view.getContext());

		// Popula a viewv
//		TextView textviewRelease = (TextView)view.findViewById( R.id.text_view_content);
//		textviewRelease.setText(Html.fromHtml( content ));
		
		WebView wv = (WebView)view.findViewById(R.id.webPage);
		
		if (Build.VERSION.SDK_INT >= 11) {
		    wv.setBackgroundColor(0x01000000);
		} else {
		    wv.setBackgroundColor(0x00000000);
		}
		wv.loadData(content, "text/html; charset=UTF-8", null);
		// Permite que a view dispare os links html
//		textviewRelease.setMovementMethod(LinkMovementMethod.getInstance());
	}


}
