package in.ultraneo.farecalculator9;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

public class CustomizeDialog extends Dialog
{
    public CustomizeDialog(Context context,int layout)
    {
        super(context);
       
        /** It will hide the title */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(layout);
    }}
