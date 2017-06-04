package com.jws.pandahealth.component.app.widget;

import android.app.ProgressDialog;
import android.content.Context;

public class Loadingdialog extends ProgressDialog {

	public Loadingdialog(Context paramContext) {
		super(paramContext);
		setMessage("please waiting");
		setCanceledOnTouchOutside(false);
	}

	public Loadingdialog(Context paramContext, String title, String message) {
		super(paramContext);
		setTitle(title);
		setMessage(message);
		setProgressStyle(ProgressDialog.STYLE_SPINNER);
		setCanceledOnTouchOutside(false);
	}
}